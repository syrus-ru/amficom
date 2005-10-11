/*-
 * $Id: IdlEmailNotificationEventImpl.java,v 1.1 2005/10/11 08:58:25 bass Exp $
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

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/11 08:58:25 $
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
	 * @see IdlEvent#getNative()
	 */
	public EmailNotificationEvent getNative() {
		return DefaultEmailNotificationEvent.valueOf(this);
	}
}
