/*-
 * $Id: IdlSmsNotificationEventImpl.java,v 1.1 2005/10/11 08:58:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import com.syrus.AMFICOM.eventv2.DefaultSmsNotificationEvent;
import com.syrus.AMFICOM.eventv2.SmsNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlNotificationEventPackage.IdlDeliveryMethod;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/11 08:58:25 $
 * @module event
 */
final class IdlSmsNotificationEventImpl extends IdlSmsNotificationEvent {
	private static final long serialVersionUID = -15657033467016928L;

	IdlSmsNotificationEventImpl() {
		// empty
	}

	IdlSmsNotificationEventImpl(final String cellular, final String message) {
		this.cellular = cellular;
		this.message = message;
	}

	/**
	 * @see IdlEvent#getType()
	 */
	public IdlEventType getType() {
		return IdlEventType.NOTIFICATION;
	}

	/**
	 * @see IdlNotificationEvent#getDeliveryMethod()
	 */
	public IdlDeliveryMethod getDeliveryMethod() {
		return IdlDeliveryMethod.SMS;
	}

	/**
	 * @see IdlSmsNotificationEvent#getCellular()
	 */
	@Override
	public String getCellular() {
		return this.cellular;
	}

	/**
	 * @see IdlNotificationEvent#getMessage()
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @see IdlEvent#getNative()
	 */
	public SmsNotificationEvent getNative() {
		return DefaultSmsNotificationEvent.valueOf(this);
	}
}
