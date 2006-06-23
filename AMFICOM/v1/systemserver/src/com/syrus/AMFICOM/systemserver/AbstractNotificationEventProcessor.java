/*-
 * $Id: AbstractNotificationEventProcessor.java,v 1.1.1.1 2006/06/23 13:16:53 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.systemserver;

import static com.syrus.AMFICOM.eventv2.EventType.NOTIFICATION;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.NotificationEvent;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: cvsadmin $
 * @version $Revision: 1.1.1.1 $, $Date: 2006/06/23 13:16:53 $
 * @module systemserver
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
