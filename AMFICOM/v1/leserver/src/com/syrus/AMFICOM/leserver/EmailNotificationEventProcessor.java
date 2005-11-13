/*-
 * $Id: EmailNotificationEventProcessor.java,v 1.5 2005/11/13 06:29:01 bass Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/11/13 06:29:01 $
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
	 * @throws EventProcessingException
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event<?> event) throws EventProcessingException {
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
				throw new EventProcessingException("Event: "
						+ emailNotificationEvent
						+ " delivery failed, see event server log for details");
			}
		}
	}
}
