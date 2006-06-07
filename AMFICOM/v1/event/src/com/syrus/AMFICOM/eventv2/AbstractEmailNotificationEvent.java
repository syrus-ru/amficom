/*-
 * $Id: AbstractEmailNotificationEvent.java,v 1.3.4.1 2006/06/07 09:07:09 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.3.4.1 $, $Date: 2006/06/07 09:07:09 $
 * @module event
 */
public abstract class AbstractEmailNotificationEvent
		extends AbstractNotificationEvent<IdlEmailNotificationEvent>
		implements EmailNotificationEvent {
	public final DeliveryMethod getDeliveryMethod() {
		return EMAIL;
	}

	protected String paramString() {
		return "plainTextMessage = " + this.getPlainTextMessage();
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}
}
