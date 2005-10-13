/*-
 * $Id: IdlSmsNotificationEventImpl.java,v 1.2 2005/10/13 09:57:25 bass Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/13 09:57:25 $
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
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IdlEvent#getNativeEvent()
	 */
	public SmsNotificationEvent getNativeEvent() {
		return DefaultSmsNotificationEvent.valueOf(this);
	}
}
