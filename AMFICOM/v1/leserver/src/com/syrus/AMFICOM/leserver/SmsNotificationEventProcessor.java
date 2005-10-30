/*-
 * $Id: SmsNotificationEventProcessor.java,v 1.3 2005/10/30 15:20:46 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.NOTIFICATION;
import static java.util.logging.Level.CONFIG;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.AMFICOM.eventv2.SmsNotificationEvent;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/30 15:20:46 $
 * @module leserver
 */
final class SmsNotificationEventProcessor implements EventProcessor {
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
	public void processEvent(final Event event) throws EventProcessingException {
		final NotificationEvent notificationEvent = (NotificationEvent) event;

		if (notificationEvent instanceof SmsNotificationEvent) {
			final SmsNotificationEvent smsNotificationEvent = (SmsNotificationEvent) notificationEvent;
			assert Log.debugMessage("Event: "
					+ smsNotificationEvent
					+ " delivered successfully",
					CONFIG);
		}
	}
}
