/*-
 * $Id: IdlEmailNotificationEventImpl.java,v 1.2 2005/10/13 09:57:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import com.syrus.AMFICOM.eventv2.DefaultEmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.EmailNotificationEvent;
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
final class IdlEmailNotificationEventImpl extends IdlEmailNotificationEvent {
	private static final long serialVersionUID = 5044915545850297329L;

	IdlEmailNotificationEventImpl() {
		// empty
	}

	IdlEmailNotificationEventImpl(final String email, final String subject,
			final String message) {
		this.email = email;
		this.subject = subject;
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
		return IdlDeliveryMethod.EMAIL;
	}

	/**
	 * @see IdlEmailNotificationEvent#getEmail()
	 */
	@Override
	public String getEmail() {
		return this.email;
	}

	/**
	 * @see IdlEmailNotificationEvent#getSubject()
	 */
	@Override
	public String getSubject() {
		return this.subject;
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
	public EmailNotificationEvent getNativeEvent() {
		return DefaultEmailNotificationEvent.valueOf(this);
	}
}
