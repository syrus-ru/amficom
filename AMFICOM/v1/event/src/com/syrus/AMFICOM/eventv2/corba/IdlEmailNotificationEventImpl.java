/*-
 * $Id: IdlEmailNotificationEventImpl.java,v 1.6.2.1 2006/06/07 09:07:10 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import com.syrus.AMFICOM.eventv2.DefaultEmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.EmailNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlNotificationEventPackage.IdlDeliveryMethod;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.6.2.1 $, $Date: 2006/06/07 09:07:10 $
 * @module event
 */
final class IdlEmailNotificationEventImpl extends IdlEmailNotificationEvent {
	private static final long serialVersionUID = 5044915545850297329L;

	IdlEmailNotificationEventImpl() {
		// empty
	}

	IdlEmailNotificationEventImpl(final String email, final String subject,
			final String plainTextMessage, final String richTextMessage) {
		final IdlIdentifier voidId = VOID_IDENTIFIER.getIdlTransferable();
		this.id = voidId;
		this.creatorId = voidId;
		this.modifierId = voidId;

		this.email = email;
		this.subject = subject;
		this.plainTextMessage = plainTextMessage;
		this.richTextMessage = richTextMessage;
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
	 * @see IdlEmailNotificationEvent#getPlainTextMessage()
	 */
	@Override
	public String getPlainTextMessage() {
		return this.plainTextMessage;
	}

	/**
	 * @see IdlEmailNotificationEvent#getRichTextMessage()
	 */
	@Override
	public String getRichTextMessage() {
		return this.richTextMessage;
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
	 * @throws IdlCreateObjectException
	 * @see IdlEvent#getNativeEvent()
	 */
	public EmailNotificationEvent getNativeEvent() throws IdlCreateObjectException {
		try {
			return DefaultEmailNotificationEvent.valueOf(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
