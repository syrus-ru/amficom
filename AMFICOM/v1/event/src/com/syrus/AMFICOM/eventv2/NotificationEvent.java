/*-
 * $Id: NotificationEvent.java,v 1.3.2.1 2006/03/23 10:48:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import com.syrus.AMFICOM.eventv2.corba.IdlNotificationEvent;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3.2.1 $, $Date: 2006/03/23 10:48:43 $
 * @module event
 */
public interface NotificationEvent<T extends IdlNotificationEvent> extends Event<T> {
	DeliveryMethod getDeliveryMethod();
}
