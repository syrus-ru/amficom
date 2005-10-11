/*-
 * $Id: DefaultPopupNotificationEvent.java,v 1.1 2005/10/11 08:58:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlPopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlPopupNotificationEventHelper;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/11 08:58:25 $
 * @module event
 */
public final class DefaultPopupNotificationEvent extends
		AbstractPopupNotificationEvent {
	private static final long serialVersionUID = 3901755454384903933L;

	/**
	 * @serial include 
	 */
	private Identifier targetUserId;

	/**
	 * @serial include 
	 */
	private String message;

	private DefaultPopupNotificationEvent(
			final IdlPopupNotificationEvent popupNotificationEvent) {
		this.targetUserId = Identifier.valueOf(
				popupNotificationEvent.getTargetUserId());
		this.message = popupNotificationEvent.getMessage();
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(ORB)
	 */
	public IdlPopupNotificationEvent getTransferable(final ORB orb) {
		return IdlPopupNotificationEventHelper.init(orb,
				this.targetUserId.getTransferable(orb),
				this.message);
	}

	public static PopupNotificationEvent valueOf(
			final IdlPopupNotificationEvent popupNotificationEvent) {
		return new DefaultPopupNotificationEvent(popupNotificationEvent);
	}

	/**
	 * @see PopupNotificationEvent#getTargetUserId()
	 */
	public Identifier getTargetUserId() {
		return this.targetUserId;
	}

	/**
	 * @see NotificationEvent#getMessage()
	 */
	public String getMessage() {
		return this.message;
	}
}
