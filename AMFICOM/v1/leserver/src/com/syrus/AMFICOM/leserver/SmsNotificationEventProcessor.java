/*-
 * $Id: SmsNotificationEventProcessor.java,v 1.6.2.1 2006/04/07 08:44:07 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.FINEST;

import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.AMFICOM.eventv2.SmsNotificationEvent;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6.2.1 $, $Date: 2006/04/07 08:44:07 $
 * @module leserver
 */
final class SmsNotificationEventProcessor extends AbstractNotificationEventProcessor {
	SmsNotificationEventProcessor(final int capacity) {
		super(capacity);
	}

	SmsNotificationEventProcessor() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * @param notificationEvent
	 * @see AbstractNotificationEventProcessor#processEvent(NotificationEvent)
	 */
	@Override
	void processEvent(final NotificationEvent<?> notificationEvent) {
		final long t0 = System.nanoTime();

		if (!(notificationEvent instanceof SmsNotificationEvent)) {
			return;
		}

		@SuppressWarnings("unchecked")
		final SmsNotificationEvent smsNotificationEvent = (SmsNotificationEvent) notificationEvent;
		Log.debugMessage("Event: "
				+ smsNotificationEvent
				+ " delivered successfully",
				CONFIG);

		final long t1 = System.nanoTime();
		Log.debugMessage(((t1 - t0) / 1e9) + " second(s)", FINEST);
	}
}
