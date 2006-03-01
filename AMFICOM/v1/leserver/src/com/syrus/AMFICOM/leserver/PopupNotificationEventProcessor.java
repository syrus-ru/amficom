/*-
 * $Id: PopupNotificationEventProcessor.java,v 1.10 2006/03/01 20:46:54 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.NOTIFICATION;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.EventReceiver;
import com.syrus.AMFICOM.eventv2.corba.EventReceiverHelper;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2006/03/01 20:46:54 $
 * @module leserver
 */
final class PopupNotificationEventProcessor implements EventProcessor {
	/**
	 * @see EventProcessor#getEventType()
	 */
	public EventType getEventType() {
		return NOTIFICATION;
	}

	/**
	 * @param event
	 * @throws EventProcessingException
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event<?> event) throws EventProcessingException {
		@SuppressWarnings("unchecked")
		final NotificationEvent<?> notificationEvent = (NotificationEvent) event;

		if (!(notificationEvent instanceof PopupNotificationEvent)) {
			return;
		}

		@SuppressWarnings("unchecked")
		final PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent) notificationEvent;

		try {
			Log.debugMessage("A new event has arrived", SEVERE);
			final Identifier targetUserId = popupNotificationEvent.getTargetUserId();
			Log.debugMessage("Message will be delivered to: " + targetUserId, FINEST);
			final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
			for (final UserLogin userLogin : LoginProcessor.getUserLogins(targetUserId)) {
				try {
					final Object object = corbaServer.stringToObject(userLogin.getUserIOR());
					if (!object._is_a(EventReceiverHelper.id())) {
						Log.debugMessage("Object: " + object + " is not an EventReceiver; skipping", FINEST);
						continue;
					}
					final EventReceiver eventReceiver = EventReceiverHelper.narrow(object);
					eventReceiver.receiveEvent(popupNotificationEvent.getIdlTransferable(corbaServer.getOrb()));
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
