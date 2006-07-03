/*-
 * $Id: AbstractEventProcessor.java,v 1.8 2006/07/03 06:26:11 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.USER_EMAIL;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.event.DeliveryAttributes;
import com.syrus.AMFICOM.eventv2.DefaultEmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.EmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.corba.EventReceiver;
import com.syrus.AMFICOM.eventv2.corba.EventReceiverHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2006/07/03 06:26:11 $
 * @module leserver
 */
abstract class AbstractEventProcessor implements EventProcessor, Runnable {
	private static Identifier characteristicTypeId = null;

	private static StorableObjectCondition condition = null;


	private final BlockingQueue<Event<?>> queue;

	private final Thread executor;

	private final Object putLock = new Object();

	private boolean poisonPut = false;

	static final Event<?> POISON = newPoison();

	/**
	 * <p>Background EXECUTORS used to asynchronously deliver events to
	 * clients.</p>
	 *
	 * <p>Note: this map is an object shared between various instances of
	 * classes that inherit from {@code AbstractEventProcessor}.</p>
	 */
	static final Map<SessionKey, ExecutorService> EXECUTORS =
			Collections.synchronizedMap(new HashMap<SessionKey, ExecutorService>());

	static {
		LoginProcessor.getInstance().addListener(new LoginProcessorAdapter() {
			@Override
			public void userLoggedOut(final UserLogin userLogin) {
				final SessionKey sessionKey = userLogin.getSessionKey();
				/*
				 * This happens AFTER LoginProcessor updates its own map,
				 * so noone will try again to talk to this session
				 * (i. e. put the same K/V pair to EXECUTORS).
				 */
				final ExecutorService executorService = EXECUTORS.remove(sessionKey);
				if (executorService != null) {
					/*
					 * This will not prevent the currently executed (blocked!) task
					 * from immediate suspension, but there's a guarantee all
					 * subsequent task will NOT run.
					 */
					final List<Runnable> unfinishedTasks = executorService.shutdownNow();
					final int droppedCount = unfinishedTasks.size();
					if (droppedCount != 0) {
						Log.debugMessage(droppedCount
								+ " event(s) dropped: addressee "
								+ sessionKey + " missing.",
								WARNING);
					}
				}
			}
		});
	}

	AbstractEventProcessor(final int capacity) {
		this.queue = new LinkedBlockingQueue<Event<?>>(capacity);
		this.executor = new Thread(this, this.getEventType().getCodename() + "EventQueue");
	}

	/**
	 * @param event
	 * @see EventProcessor#put(Event)
	 * @see BlockingQueue#put(Object)
	 */
	public final void put(final Event<?> event) {
		if (!this.executor.isAlive()) {
			Log.debugMessage(this.getName()
					+ " is in an illegal state: " + this.executor.getState()
					+ "; dropping event: " + event,
					SEVERE);
			return;
		}

		synchronized (this.putLock) {
			if (event == POISON) {
				this.poisonPut = true;
			} else if (this.poisonPut) {
				Log.debugMessage("Event: " + event
						+ " has just been dropped, for the queue is already shut down",
						SEVERE);
				return;
			}
		}

		try {
			this.queue.put(event);
		} catch (final InterruptedException ie) {
			Log.debugMessage("Event: " + event
					+ " has just been dropped",
					SEVERE);
			Log.debugMessage(ie, SEVERE);
		}
	}

	/**
	 * @see EventProcessor#putPoison()
	 */
	public final void putPoison() {
		this.put(POISON);
	}

	/**
	 * @see Runnable#run()
	 */
	public final void run() {
		while (true) {
			try {
				final Event<?> event = this.queue.take();
				if (event == POISON) {
					break;
				}
				this.processEvent(event);
			} catch (final InterruptedException ie) {
				Log.debugMessage(ie, SEVERE);
			} catch (final Throwable t) {
				Log.debugMessage(t, SEVERE);
			}
		}
	}

	/**
	 * @see EventProcessor#start()
	 * @see Thread#start()
	 */
	public final void start() {
		this.executor.start();
	}

	/**
	 * @throws InterruptedException
	 * @see EventProcessor#join()
	 * @see Thread#join()
	 */
	public final void join() throws InterruptedException {
		this.executor.join();
	}

	/**
	 * @see EventProcessor#getName()
	 * @see Thread#getName()
	 */
	public final String getName() {
		return this.executor.getName();
	}

	final void deliverToClients(final Event<?> event, final Set<UserLogin> clients) {
		final long t0 = System.nanoTime();
		final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
		for (final UserLogin client : clients) {
			final SessionKey sessionKey = client.getSessionKey();			
			ExecutorService executorService = EXECUTORS.get(sessionKey);
			if (executorService == null) {
				executorService = Executors.newSingleThreadExecutor();
				EXECUTORS.put(sessionKey, executorService);
			}
			final long t1 = System.nanoTime();
			executorService.submit(new Runnable() {
				public void run() {
					final long t2 = System.nanoTime();
					Log.debugMessage(event + " | Event has been waiting for "
							+ ((t2 - t1) / 1e9)
							+ " second(s) since submission; started being processed",
							FINEST);

					try {
						final long t3 = System.nanoTime();
						final org.omg.CORBA.Object object = corbaServer.stringToObject(client.getUserIOR());
						final long t4 = System.nanoTime();
						Log.debugMessage(event + " | String resolved to object in "
								+ ((t4 - t3) / 1e9)
								+ " second(s)",
								FINEST);

						final long t5 = System.nanoTime();
						final boolean isEventReceiver = object._is_a(EventReceiverHelper.id());
						final long t6 = System.nanoTime();
						Log.debugMessage(event + " | Remote object type found out in "
								+ ((t6 - t5) / 1e9)
								+ " second(s)",
								FINEST);

						if (!isEventReceiver) {
							Log.debugMessage(event + " | Object: " + object
									+ " is not an EventReceiver; skipping",
									FINEST);
							return;
						}

						final long t7 = System.nanoTime();
						final EventReceiver eventReceiver = EventReceiverHelper.narrow(object);
						final long t8 = System.nanoTime();
						Log.debugMessage(event + " | Remote object type casting completed in "
								+ ((t8 - t7) / 1e9)
								+ " second(s)",
								FINEST);

						final long t9 = System.nanoTime();
						eventReceiver.receiveEvent(event.getIdlTransferable(corbaServer.getOrb()));
						final long t10 = System.nanoTime();
						Log.debugMessage(event + " | Remote method invocation completed in "
								+ ((t10 - t9) / 1e9)
								+ " second(s)",
								FINEST);
						Log.debugMessage(event + " | Event delivered in "
								+ ((t10 - t0) / 1e9)
								+ " second(s)",
								FINEST);
					} catch (final COMM_FAILURE cf) {
						Log.debugMessage(cf.toString(), SEVERE);
						LoginProcessor.getInstance().removeUserLogin(sessionKey);
					} catch (final SystemException se) {
						/**
						 * @todo Generally, system
						 * exceptions should be handled
						 * separately from all other
						 * (unexpected) throwables.
						 * Custom handling code pending.
						 */
						Log.debugMessage(se, SEVERE);
					} catch (final Throwable t) {
						Log.debugMessage(t, SEVERE);
					}
				}
			});
		}

		final long t2 = System.nanoTime();
		Log.debugMessage(event + " | Event submitted in "
				+ ((t2 - t0) / 1e9) + " second(s); delivery pending",
				FINEST);
	}

	/**
	 * If event processing fails due to misconfiguration, this method can be
	 * used to notify those users who, under normal conditions, would
	 * receive the event processed.
	 *
	 * @param severity used to narrow down the circle of addressees.
	 * @param message error message.
	 * @throws ApplicationException when reporting something weird,
	 *         something even more weird can happen.
	 */
	final void reportMisconfiguration(final Severity severity, final String message)
	throws ApplicationException {
		Log.debugMessage(message, SEVERE);

		final LEServerServantManager servantManager = LEServerSessionEnvironment.getInstance().getLEServerServantManager();
		final ORB orb = servantManager.getCORBAServer().getOrb();

		final Set<SystemUser> systemUsers = DeliveryAttributes
				.getInstance(LoginManager.getUserId(), severity)
				.getSystemUsersRecursively();
		final Set<Identifier> systemUserIds = Identifier.createIdentifiers(systemUsers);

		final Set<String> addresses = getAddresses(systemUserIds);

		final IdlEvent notificationEvents[] = new IdlEvent[addresses.size()];
		int i = 0;
		for (final String address : addresses) {
			final String subject = "Misconfiguration Report";
			final EmailNotificationEvent emailNotificationEvent =
						DefaultEmailNotificationEvent.valueOf(
								address,
								subject,
								message,
								message);
			notificationEvents[i++] = emailNotificationEvent.getIdlTransferable(orb);
		}

		servantManager.getEventServerReference().receiveEvents(notificationEvents);
	}

	/**
	 * Returns a set of e-mail addresses that belong to users specified by
	 * {@code systemUserIds}.
	 *
	 * @param systemUserIds
	 * @throws ApplicationException
	 */
	static final Set<String> getAddresses(final Set<Identifier> systemUserIds) throws ApplicationException {
		if (systemUserIds.isEmpty()) {
			return Collections.emptySet();
		}

		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(
						getCondition(),
						AND,
						new LinkedIdsCondition(
								systemUserIds,
								CHARACTERISTIC_CODE)),
				true);

		final Set<String> addresses = new HashSet<String>();
		for (final Characteristic characteristic : characteristics) {
			addresses.add(characteristic.getValue());
		}
		return addresses;
	}

	/**
	 * Converts a plain-text message to basic HTML.
	 *
	 * @param plainTextMessage
	 */
	static final String toRichTextMessage(final String plainTextMessage) {
		final StringBuilder builder = new StringBuilder();
		builder.append(plainTextMessage.replaceAll("&", "&amp;").
				replaceAll("\"", "&quot;").
				replaceAll("'", "&apos;").
				replaceAll("<", "&lt;").
				replaceAll(">", "&gt;").
				replaceAll("\n", "<br>\n"));			
		return builder.toString();
	}

	private static StorableObjectCondition getCondition() {
		synchronized (LineMismatchEventProcessor.class) {
			if (condition == null) {
				condition = new LinkedIdsCondition(getCharacteristicTypeId(), CHARACTERISTIC_CODE);
			}
			return condition;
		}
	}

	private static Identifier getCharacteristicTypeId() {
		synchronized (LineMismatchEventProcessor.class) {
			if (characteristicTypeId == null) {
				try {
					final Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(
							new TypicalCondition(
									USER_EMAIL,
									OPERATION_EQUALS,
									CHARACTERISTIC_TYPE_CODE,
									COLUMN_CODENAME),
							true);
					assert characteristicTypes != null : NON_NULL_EXPECTED;
					final int size = characteristicTypes.size();
					assert size == 1 : size;
					characteristicTypeId = characteristicTypes.iterator().next().getId();
				} catch (final ApplicationException ae) {
					Log.debugMessage(ae, SEVERE);
					characteristicTypeId = VOID_IDENTIFIER;
		
					/*
					 * Never. But log the exception prior to issuing an
					 * error.
					 */
					assert false;
				}
			}
			return characteristicTypeId;
		}
	}

	@SuppressWarnings("unchecked")
	private static Event<?> newPoison() {
		return new Event() {
			public EventType getType() {
				throw new UnsupportedOperationException();
			}

			public void fromIdlTransferable(final IDLEntity transferable)
			throws IdlConversionException{
				throw new UnsupportedOperationException();
			}

			public IDLEntity getIdlTransferable(final ORB orb){
				throw new UnsupportedOperationException();
			}
		};
	}
}
