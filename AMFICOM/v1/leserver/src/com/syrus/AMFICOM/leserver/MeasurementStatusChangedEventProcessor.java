/*-
 * $Id: MeasurementStatusChangedEventProcessor.java,v 1.2.2.2 2006/04/07 10:04:23 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.MEASUREMENT_STATUS_CHANGED;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.MeasurementStatusChangedEvent;
import com.syrus.AMFICOM.eventv2.corba.EventReceiver;
import com.syrus.AMFICOM.eventv2.corba.EventReceiverHelper;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2.2.2 $, $Date: 2006/04/07 10:04:23 $
 * @module leserver
 */
final class MeasurementStatusChangedEventProcessor extends AbstractEventProcessor {
	private final Map<SessionKey, ExecutorService> executors =
		Collections.synchronizedMap(new HashMap<SessionKey, ExecutorService>());

	MeasurementStatusChangedEventProcessor(final int capacity) {
		super(capacity);
	}

	MeasurementStatusChangedEventProcessor() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * @see EventProcessor#getEventType()
	 */
	public EventType getEventType() {
		return MEASUREMENT_STATUS_CHANGED;
	}

	/**
	 * @param event
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(Event<?> event) {
		final long t0 = System.nanoTime();

		@SuppressWarnings("unchecked")
		final MeasurementStatusChangedEvent<?> measurementStatusChangedEvent = (MeasurementStatusChangedEvent) event;

		Log.debugMessage(measurementStatusChangedEvent
				+ " started being processed",
				FINEST);
		final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
		for (final UserLogin userLogin : LoginProcessor.getUserLogins()) {
			/**
			 * @todo This code is a copy-paste of one in
			 * PopupNotificationEventProcessor.
			 */
			final SessionKey sessionKey = userLogin.getSessionKey();
			ExecutorService executorService = this.executors.get(sessionKey);
			if (executorService == null) {
				executorService = Executors.newSingleThreadExecutor();
				this.executors.put(sessionKey, executorService);
			}
			executorService.submit(new Runnable() {
				public void run() {
					try {
						final Object object = corbaServer.stringToObject(userLogin.getUserIOR());
						if (!object._is_a(EventReceiverHelper.id())) {
							Log.debugMessage("Object: " + object + " is not an EventReceiver; skipping", FINEST);
							return;
						}
						final EventReceiver eventReceiver = EventReceiverHelper.narrow(object);
						eventReceiver.receiveEvent(measurementStatusChangedEvent.getIdlTransferable(corbaServer.getOrb()));
						final long t1 = System.nanoTime();
						Log.debugMessage(measurementStatusChangedEvent
								+ " delivered in "
								+ ((t1 - t0) / 1e9)
								+ " second(s)",
								FINEST);
					} catch (final SystemException se) {
						/**
						 * @todo catch COMM_FAILURE separately
						 * and request failed session removal.
						 */
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

		final long t1 = System.nanoTime();
		Log.debugMessage(measurementStatusChangedEvent + " submitted in "
				+ ((t1 - t0) / 1e9) + " second(s); delivery pending",
				FINEST);
	}
}
