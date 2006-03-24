/*-
 * $Id: IdlPopupNotificationEventImpl.java,v 1.10.2.2 2006/03/23 10:48:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import com.syrus.AMFICOM.eventv2.DefaultPopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlNotificationEventPackage.IdlDeliveryMethod;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10.2.2 $, $Date: 2006/03/23 10:48:43 $
 * @module event
 */
final class IdlPopupNotificationEventImpl extends IdlPopupNotificationEvent {
	private static final long serialVersionUID = -2880292146560134294L;

	IdlPopupNotificationEventImpl() {
		// empty
	}

	IdlPopupNotificationEventImpl(final IdlIdentifier targetUserId,
			final IdlIdentifier lineMismatchEventId) {
		final IdlIdentifier voidId = VOID_IDENTIFIER.getIdlTransferable();
		this.id = voidId;
		this.creatorId = voidId;
		this.modifierId = voidId;

		this.targetUserId = targetUserId;
		this.lineMismatchEventId = lineMismatchEventId;
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
		return IdlDeliveryMethod.POPUP;
	}

	/**
	 * @see IdlPopupNotificationEvent#getTargetUserId()
	 */
	@Override
	public IdlIdentifier getTargetUserId() {
		return this.targetUserId;
	}

	/**
	 * @see IdlPopupNotificationEvent#getLineMismatchEventId()
	 */
	@Override
	public IdlIdentifier getLineMismatchEventId() {
		return this.lineMismatchEventId;
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
	public PopupNotificationEvent getNativeEvent()
	throws IdlCreateObjectException {
		try {
			return DefaultPopupNotificationEvent.valueOf(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
