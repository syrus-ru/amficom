/*-
 * $Id: AbstractEventProcessor.java,v 1.2 2006/04/19 14:13:46 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.HashMap;
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

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.corba.EventReceiver;
import com.syrus.AMFICOM.eventv2.corba.EventReceiverHelper;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/04/19 14:13:46 $
 * @module leserver
 */
abstract class AbstractEventProcessor implements EventProcessor, Runnable {
	private final BlockingQueue<Event<?>> queue;

	private final Thread executor;

	private final Object putLock = new Object();

	private boolean poisonPut = false;

	static final Event<?> POISON = newPoison();

	/**
	 * Background executors used to asynchronously deliver events to clients.
	 */
	private final Map<SessionKey, ExecutorService> executors =
		Collections.synchronizedMap(new HashMap<SessionKey, ExecutorService>());

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
					+ " has just been droppped",
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

	void deliverToClients(final Event<?> event, final Set<UserLogin> clients) {
		final long t0 = System.nanoTime();
		final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
		for (final UserLogin client : clients) {
			final SessionKey sessionKey = client.getSessionKey();
			ExecutorService executorService = this.executors.get(sessionKey);
			if (executorService == null) {
				executorService = Executors.newSingleThreadExecutor();
				this.executors.put(sessionKey, executorService);
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
						/**
						 * @todo request failed session removal.
						 */
						Log.debugMessage(cf.toString(), SEVERE);
					} catch (final SystemException se) {
						Log.debugMessage(se, SEVERE);
					} catch (final Throwable t) {
						/**
						 * @todo EventReceiver#receiveEvent(...)
						 * should't throw an ARE: there's
						 * no need to, and I'm unsure
						 * which specific handling there
						 * should be.
						 */
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
