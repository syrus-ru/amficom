/*-
 * $Id: AbstractNotificationEventProcessor.java,v 1.2 2006/04/19 14:13:46 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.NOTIFICATION;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.NotificationEvent;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/04/19 14:13:46 $
 * @module leserver
 */
abstract class AbstractNotificationEventProcessor extends AbstractEventProcessor {
	AbstractNotificationEventProcessor(final int capacity) {
		super(capacity);
	}

	/**
	 * @see EventProcessor#getEventType()
	 */
	public final EventType getEventType() {
		return NOTIFICATION;
	}

	/**
	 * @see EventProcessor#processEvent(Event)
	 */
	public final void processEvent(final Event<?> event) {
		@SuppressWarnings("unchecked")
		final NotificationEvent<?> notificationEvent = (NotificationEvent) event;
		this.processEvent(notificationEvent);
	}

	abstract void processEvent(final NotificationEvent<?> notificationEvent);
}
