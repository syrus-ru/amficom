/*-
 * $Id: AbstractPopupNotificationEvent.java,v 1.2.2.1 2006/03/23 10:48:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.DeliveryMethod.POPUP;

import com.syrus.AMFICOM.eventv2.corba.IdlPopupNotificationEvent;


/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2.2.1 $, $Date: 2006/03/23 10:48:43 $
 * @module event
 */
public abstract class AbstractPopupNotificationEvent
		extends AbstractNotificationEvent<IdlPopupNotificationEvent>
		implements PopupNotificationEvent {
	/**
	 * @see NotificationEvent#getDeliveryMethod()
	 */
	public final DeliveryMethod getDeliveryMethod() {
		return POPUP;
	}

	protected String paramString() {
		return "lineMismatchEventId = " + this.getLineMismatchEventId();
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}
}
