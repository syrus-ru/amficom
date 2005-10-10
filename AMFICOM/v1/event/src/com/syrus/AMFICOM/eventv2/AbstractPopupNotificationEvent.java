/*-
 * $Id: AbstractPopupNotificationEvent.java,v 1.1 2005/10/10 14:28:55 bass Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/10/10 14:28:55 $
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
}
