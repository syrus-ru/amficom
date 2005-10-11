/*-
 * $Id: IdlPopupNotificationEventImpl.java,v 1.1 2005/10/11 08:58:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import com.syrus.AMFICOM.eventv2.DefaultPopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlNotificationEventPackage.IdlDeliveryMethod;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/11 08:58:25 $
 * @module event
 */
final class IdlPopupNotificationEventImpl extends IdlPopupNotificationEvent {
	private static final long serialVersionUID = -2880292146560134294L;

	IdlPopupNotificationEventImpl() {
		// empty
	}

	IdlPopupNotificationEventImpl(final IdlIdentifier targetUserId,
			final String message) {
		this.targetUserId = targetUserId;
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
	 * @see IdlEvent#getNative()
	 */
	public PopupNotificationEvent getNative() {
		return DefaultPopupNotificationEvent.valueOf(this);
	}
}
