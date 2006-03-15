/*-
 * $Id: IdlPopupNotificationEventImpl.java,v 1.8 2006/03/13 13:53:59 bass Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.reflectometry.corba.IdlSeverity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2006/03/13 13:53:59 $
 * @module event
 */
final class IdlPopupNotificationEventImpl extends IdlPopupNotificationEvent {
	private static final long serialVersionUID = -2880292146560134294L;

	IdlPopupNotificationEventImpl() {
		// empty
	}

	IdlPopupNotificationEventImpl(final IdlIdentifier targetUserId,
			final String message, final IdlIdentifier resultId,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final long mismatchCreated,
			final IdlSeverity severity,
			final IdlIdentifier affectedPathElementId) {
		final IdlIdentifier voidId = VOID_IDENTIFIER.getIdlTransferable();
		this.id = voidId;
		this.creatorId = voidId;
		this.modifierId = voidId;

		this.targetUserId = targetUserId;
		this.message = message;

		this.resultId = resultId;
		this.mismatchOpticalDistance = mismatchOpticalDistance;
		this.mismatchPhysicalDistance = mismatchPhysicalDistance;
		this.mismatchCreated = mismatchCreated;
		this.severity = severity;
		this.affectedPathElementId = affectedPathElementId;
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
	 * @see IdlNotificationEvent#getMessage()
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @see IdlPopupNotificationEvent#getResultId()
	 */
	@Override
	public IdlIdentifier getResultId() {
		return this.resultId;
	}

	/**
	 * @see IdlPopupNotificationEvent#getMismatchOpticalDistance()
	 */
	@Override
	public double getMismatchOpticalDistance() {
		return this.mismatchOpticalDistance;
	}

	/**
	 * @see IdlPopupNotificationEvent#getMismatchPhysicalDistance()
	 */
	@Override
	public double getMismatchPhysicalDistance() {
		return this.mismatchPhysicalDistance;
	}

	/**
	 * @see IdlPopupNotificationEvent#getMismatchCreated()
	 */
	@Override
	public long getMismatchCreated() {
		return this.mismatchCreated;
	}

	/**
	 * @see IdlPopupNotificationEvent#getSeverity()
	 */
	@Override
	public IdlSeverity getSeverity() {
		return this.severity;
	}

	/**
	 * @see IdlPopupNotificationEvent#getAffectedPathElementId()
	 */
	@Override
	public IdlIdentifier getAffectedPathElementId() {
		return this.affectedPathElementId;
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
	public PopupNotificationEvent getNativeEvent() {
		return DefaultPopupNotificationEvent.valueOf(this);
	}
}
