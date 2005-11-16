/*-
 * $Id: PopupNotificationEventProcessor.java,v 1.7 2005/11/16 10:27:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.NOTIFICATION;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.MessageReceiver;
import com.syrus.AMFICOM.eventv2.corba.MessageReceiverHelper;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.leserver.corba.MessageReceiverExt;
import com.syrus.AMFICOM.leserver.corba.MessageReceiverExtHelper;
import com.syrus.AMFICOM.security.ClientUserLogin;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/11/16 10:27:02 $
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

			final ORB orb = LEServerSessionEnvironment.getInstance()
					.getLEServerServantManager()
					.getCORBAServer().getOrb();

			for (final Object object : getObjects()) {
				try {
					if (!object._is_a(MessageReceiverExtHelper.id())) {
						Log.debugMessage("Object: " + object
								+ " is not a MessageReceiverExt; skipping",
								FINEST);
						continue;
					}
					
					Log.debugMessage("Object: " + object
							+ " is a valid MessageReceiverExt; continuing",
							FINEST);
					final MessageReceiverExt messageReceiverExt = MessageReceiverExtHelper.narrow(object);
					final Identifier loggedUserId = Identifier.valueOf(messageReceiverExt.getSystemUserId());
					if (!targetUserId.equals(loggedUserId)) {
						Log.debugMessage("Message is for: " + targetUserId
								+ "; while the user currently logged in is: "
								+ loggedUserId + "; skipping",
								FINEST);
						continue;
					}
					Log.debugMessage("Message will be delivered to: "
							+ targetUserId,
							FINEST);
					
					if (object._is_a(MessageReceiverHelper.id())) {
						final MessageReceiver messageReceiver = MessageReceiverHelper.narrow(object);
						messageReceiver.receiveMessages(popupNotificationEvent.getTransferable(orb));
					} else {
						/*
						 * Everyone who implements MessageReceiverExt,
						 * must also implement MessageReceiver.
						 */
						assert false;
					}
				} catch (final SystemException se) {
					Log.debugMessage(se, SEVERE);
				}
			}				

			Log.debugMessage("Exiting...", SEVERE);
		} catch (final Throwable t) {
			Log.debugMessage(t, SEVERE);
		}
	}

	private static List<Object> getObjects() {
		final List<Object> objects = new LinkedList<Object>();

		final Collection<UserLogin> userLogins = LoginProcessor.getUserLogins();
		for (final UserLogin userLogin : userLogins) {
			if (userLogin instanceof ClientUserLogin) {
				try {
					final ClientUserLogin clientUserLogin = (ClientUserLogin) userLogin;
					final String servantName = clientUserLogin.getServantName();
					final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
					final Object object = corbaServer.resolveReference(servantName);
					objects.add(object);
					Log.debugMessage("Added object for servant '" + servantName + "'", SEVERE);
				} catch (CommunicationException ce) {
					Log.errorMessage(ce);
				}
			}
		}

		return objects;
	}

}
