/*-
 * $Id: EmailNotificationEventProcessor.java,v 1.1 2005/10/19 13:39:20 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
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
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/19 13:39:20 $
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
			Log.debugMessage("EmailNotificationEventProcessor.processEvent() | Event: "
					+ emailNotificationEvent
					+ " delivered successfully",
					CONFIG);
		}
	}
}