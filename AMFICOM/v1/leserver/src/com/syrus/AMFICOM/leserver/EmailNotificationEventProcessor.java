/*-
 * $Id: EmailNotificationEventProcessor.java,v 1.4 2005/10/31 10:49:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.NOTIFICATION;
import static java.util.logging.Level.CONFIG;

import com.syrus.AMFICOM.eventv2.EmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/10/31 10:49:45 $
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
	public void processEvent(final Event event) throws EventProcessingException {
		final NotificationEvent notificationEvent = (NotificationEvent) event;

		if (notificationEvent instanceof EmailNotificationEvent) {
			final EmailNotificationEvent emailNotificationEvent = (EmailNotificationEvent) notificationEvent;
			Log.debugMessage("Event: "
					+ emailNotificationEvent
					+ " delivered successfully",
					CONFIG);
		}
	}
}
