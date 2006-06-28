/*-
 * $Id: DefaultPopupNotificationEvent.java,v 1.17 2006/03/28 10:17:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlPopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlPopupNotificationEventHelper;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.17 $, $Date: 2006/03/28 10:17:19 $
 * @module event
 */
public final class DefaultPopupNotificationEvent extends
		AbstractPopupNotificationEvent {
	/**
	 * @serial include 
	 */
	private Identifier targetUserId;

	/**
	 * @serial include
	 */
	private Identifier lineMismatchEventId;

	private DefaultPopupNotificationEvent(
			final IdlPopupNotificationEvent popupNotificationEvent)
	throws CreateObjectException {
		try {
			this.fromIdlTransferable(popupNotificationEvent);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	private DefaultPopupNotificationEvent(
			final Identifier targetUserId,
			final LineMismatchEvent lineMismatchEvent) {
		this.targetUserId = targetUserId;
		this.lineMismatchEventId = lineMismatchEvent.getId();
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlPopupNotificationEvent getIdlTransferable(final ORB orb) {
		return IdlPopupNotificationEventHelper.init(orb,
				this.getTargetUserId().getIdlTransferable(orb),
				this.getLineMismatchEventId().getIdlTransferable(orb));
	}

	public void fromIdlTransferable(
			final IdlPopupNotificationEvent popupNotificationEvent)
	throws IdlConversionException {
		synchronized (this) {
			this.targetUserId = Identifier.valueOf(
					popupNotificationEvent.getTargetUserId());
			this.lineMismatchEventId = Identifier.valueOf(
					popupNotificationEvent.getLineMismatchEventId());
		}
	}

	public static PopupNotificationEvent valueOf(
			final IdlPopupNotificationEvent popupNotificationEvent)
	throws CreateObjectException {
		return new DefaultPopupNotificationEvent(popupNotificationEvent);
	}

	public static PopupNotificationEvent valueOf(
			final Identifier targetUserId,
			final LineMismatchEvent lineMismatchEvent) {
		return new DefaultPopupNotificationEvent(targetUserId,
				lineMismatchEvent);
	}

	/**
	 * @see PopupNotificationEvent#getTargetUserId()
	 */
	public Identifier getTargetUserId() {
		return this.targetUserId;
	}

	/**
	 * @see PopupNotificationEvent#getLineMismatchEventId()
	 */
	public Identifier getLineMismatchEventId() {
		return this.lineMismatchEventId;
	}
}
