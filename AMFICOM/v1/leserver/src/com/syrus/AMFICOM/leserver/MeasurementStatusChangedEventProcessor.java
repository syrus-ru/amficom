/*-
 * $Id: MeasurementStatusChangedEventProcessor.java,v 1.2 2006/04/04 06:08:46 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.MEASUREMENT_STATUS_CHANGED;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.MeasurementStatusChangedEvent;
import com.syrus.AMFICOM.eventv2.corba.EventReceiver;
import com.syrus.AMFICOM.eventv2.corba.EventReceiverHelper;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/04/04 06:08:46 $
 * @module leserver
 */
final class MeasurementStatusChangedEventProcessor implements EventProcessor {
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
		@SuppressWarnings("unchecked")
		final MeasurementStatusChangedEvent<?> measurementStatusChangedEvent = (MeasurementStatusChangedEvent) event;

		try {
			Log.debugMessage("A new event has arrived", SEVERE);
			final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
			for (final UserLogin userLogin : LoginProcessor.getUserLogins()) {
				try {
					final Object object = corbaServer.stringToObject(userLogin.getUserIOR());
					if (!object._is_a(EventReceiverHelper.id())) {
						Log.debugMessage("Object: " + object + " is not an EventReceiver; skipping", FINEST);
						continue;
					}
					final EventReceiver eventReceiver = EventReceiverHelper.narrow(object);
					eventReceiver.receiveEvent(measurementStatusChangedEvent.getIdlTransferable(corbaServer.getOrb()));
				} catch (final SystemException se) {
					Log.debugMessage(se, SEVERE);
				}
			}
			Log.debugMessage("Exiting...", SEVERE);
		} catch (final Throwable t) {
			Log.debugMessage(t, SEVERE);
		}
	}
}
