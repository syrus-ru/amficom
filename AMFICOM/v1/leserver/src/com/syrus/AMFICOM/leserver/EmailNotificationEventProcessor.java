/*-
 * $Id: EmailNotificationEventProcessor.java,v 1.6.2.1 2006/04/07 08:44:07 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import javax.mail.MessagingException;

import com.syrus.AMFICOM.eventv2.EmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6.2.1 $, $Date: 2006/04/07 08:44:07 $
 * @module leserver
 */
final class EmailNotificationEventProcessor extends AbstractNotificationEventProcessor {
	EmailNotificationEventProcessor(final int capacity) {
		super(capacity);
	}

	EmailNotificationEventProcessor() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * @param notificationEvent
	 * @see AbstractNotificationEventProcessor#processEvent(NotificationEvent)
	 */
	@Override
	void processEvent(final NotificationEvent<?> notificationEvent) {
		final long t0 = System.nanoTime();

		if (!(notificationEvent instanceof EmailNotificationEvent)) {
			return;
		}

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

		final long t1 = System.nanoTime();
		Log.debugMessage(((t1 - t0) / 1e9) + " second(s)", FINEST);
	}
}
