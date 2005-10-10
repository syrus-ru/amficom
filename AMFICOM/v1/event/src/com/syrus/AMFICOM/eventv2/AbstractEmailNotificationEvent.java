/*-
 * $Id: AbstractEmailNotificationEvent.java,v 1.2 2005/10/10 14:30:40 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.DeliveryMethod.EMAIL;

import com.syrus.AMFICOM.eventv2.corba.IdlEmailNotificationEvent;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/10 14:30:40 $
 * @module event
 */
public abstract class AbstractEmailNotificationEvent
		extends AbstractNotificationEvent<IdlEmailNotificationEvent>
		implements EmailNotificationEvent {
	public final DeliveryMethod getDeliveryMethod() {
		return EMAIL;
	}
}
