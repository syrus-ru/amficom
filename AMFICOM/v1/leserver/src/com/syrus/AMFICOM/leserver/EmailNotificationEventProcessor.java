/*-
 * $Id: EmailNotificationEventProcessor.java,v 1.6 2006/04/04 06:08:46 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.NOTIFICATION;
import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.SEVERE;

import javax.mail.MessagingException;

import com.syrus.AMFICOM.eventv2.EmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2006/04/04 06:08:46 $
 * @module leserver
 */
final class EmailNotificationEventProcessor implements EventProcessor {
	/**
	 * @see EventProcessor#getEventType()
	 */
	public EventType getEventType() {
		return NOTIFICATION;
	}

	/**
	 * @param event
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event<?> event) {
		@SuppressWarnings("unchecked")
		final NotificationEvent<?> notificationEvent = (NotificationEvent) event;

		if (notificationEvent instanceof EmailNotificationEvent) {
			@SuppressWarnings("unchecked")
			final EmailNotificationEvent emailNotificationEvent = (EmailNotificationEvent) notificationEvent;

			try {
				SimpleMailer.sendMail(emailNotificationEvent.getEmail(), emailNotificationEvent.getSubject(), emailNotificationEvent.getMessage());
				Log.debugMessage("Event: "
						+ emailNotificationEvent
						+ " delivered successfully",
						CONFIG);
			} catch (MessagingException me) {
				while (me != null) {
					Log.debugMessage(me, SEVERE);
					final Exception nextException = me.getNextException();
					if (nextException instanceof MessagingException
							|| nextException == null) {
						me = (MessagingException) nextException;
						continue;
					}
					Log.debugMessage(nextException, SEVERE);
					break;
				}
				Log.debugMessage("Event: "
						+ emailNotificationEvent
						+ " delivery failed",
						SEVERE);
			}
		}
	}
}
