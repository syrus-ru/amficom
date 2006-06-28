/*-
 * $Id: PopupNotificationEventProcessor.java,v 1.13 2006/06/02 13:46:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.FINEST;

import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.13 $, $Date: 2006/06/02 13:46:45 $
 * @module leserver
 */
final class PopupNotificationEventProcessor extends AbstractNotificationEventProcessor {
	PopupNotificationEventProcessor(final int capacity) {
		super(capacity);
	}

	PopupNotificationEventProcessor() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * @param notificationEvent
	 * @see AbstractNotificationEventProcessor#processEvent(NotificationEvent)
	 */
	@Override
	void processEvent(final NotificationEvent<?> notificationEvent) {
		if (!(notificationEvent instanceof PopupNotificationEvent)) {
			return;
		}

		@SuppressWarnings("unchecked")
		final PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent) notificationEvent;

		Log.debugMessage(popupNotificationEvent
				+ " | Event started being processed",
				FINEST);
		final Identifier targetUserId = popupNotificationEvent.getTargetUserId();
		Log.debugMessage(popupNotificationEvent
				+ " | Message will be delivered to: "
				+ targetUserId,
				FINEST);

		this.deliverToClients(popupNotificationEvent, LoginProcessor.getInstance().getUserLogins(targetUserId));
	}
}
