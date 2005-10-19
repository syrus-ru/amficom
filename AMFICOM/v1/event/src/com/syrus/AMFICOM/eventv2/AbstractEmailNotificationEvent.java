/*-
 * $Id: AbstractEmailNotificationEvent.java,v 1.3 2005/10/19 13:46:13 bass Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/10/19 13:46:13 $
 * @module event
 */
public abstract class AbstractEmailNotificationEvent
		extends AbstractNotificationEvent<IdlEmailNotificationEvent>
		implements EmailNotificationEvent {
	public final DeliveryMethod getDeliveryMethod() {
		return EMAIL;
	}

	protected String paramString() {
		return "message = " + this.getMessage();
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}
}
