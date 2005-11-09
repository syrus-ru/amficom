/*-
 * $Id: DefaultPopupNotificationEvent.java,v 1.9 2005/11/09 10:58:59 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Date;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlPopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlPopupNotificationEventHelper;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/11/09 10:58:59 $
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

	/**
	 * @serial include 
	 */
	private Identifier resultId;

	/**
	 * @serial include 
	 */
	private double mismatchOpticalDistance;

	/**
	 * @serial include 
	 */
	private double mismatchPhysicalDistance;

	/**
	 * @serial include 
	 */
	private Date mismatchCreated;

	private DefaultPopupNotificationEvent(
			final IdlPopupNotificationEvent popupNotificationEvent) {
		this.targetUserId = Identifier.valueOf(
				popupNotificationEvent.getTargetUserId());
		this.message = popupNotificationEvent.getMessage();
		this.resultId = Identifier.valueOf(
				popupNotificationEvent.getResultId());
		this.mismatchOpticalDistance =
				popupNotificationEvent.getMismatchOpticalDistance();
		this.mismatchPhysicalDistance =
				popupNotificationEvent.getMismatchPhysicalDistance();
		this.mismatchCreated = new Date(popupNotificationEvent.getMismatchCreated());
	}

	private DefaultPopupNotificationEvent(
			final LineMismatchEvent lineMismatchEvent,
			final Identifier targetUserId) {
		this.targetUserId = targetUserId;
		this.message = lineMismatchEvent.getAlarmType().localizedDescription() + '\n'
				+ lineMismatchEvent.getSeverity().localizedDescription() + '\n'
				+ (lineMismatchEvent.hasMismatch()
						? "Minimum Mismatch Level:\t" + lineMismatchEvent.getMinMismatch() + '\n'
						+ "Maximum Mismatch Level:\t" + lineMismatchEvent.getMaxMismatch() + '\n'
						: "")
				+ "Affected Path Element:\t" + lineMismatchEvent.getAffectedPathElementId() + '\n'
				+ (lineMismatchEvent.isAffectedPathElementSpacious()
						? "Physical Distance To Path Element Start:\t" + lineMismatchEvent.getPhysicalDistanceToStart() + " meter(s)" + '\n'
						+ "Physical Distance To Path Element End:\t" + lineMismatchEvent.getPhysicalDistanceToStart() + " meter(s)" + '\n'
						: "");
		this.resultId = lineMismatchEvent.getResultId();
		this.mismatchOpticalDistance = lineMismatchEvent.getMismatchOpticalDistance();
		this.mismatchPhysicalDistance = lineMismatchEvent.getMismatchPhysicalDistance();
		this.mismatchCreated = new Date(lineMismatchEvent.getMismatchCreated().getTime());
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(ORB)
	 */
	public IdlPopupNotificationEvent getTransferable(final ORB orb) {
		return IdlPopupNotificationEventHelper.init(orb,
				this.getTargetUserId().getTransferable(orb),
				this.getMessage(),
				this.getResultId().getTransferable(orb),
				this.getMismatchOpticalDistance(),
				this.getMismatchPhysicalDistance(),
				this.mismatchCreated.getTime());
	}

	public static PopupNotificationEvent valueOf(
			final IdlPopupNotificationEvent popupNotificationEvent) {
		return new DefaultPopupNotificationEvent(popupNotificationEvent);
	}

	public static PopupNotificationEvent valueOf(
			final LineMismatchEvent lineMismatchEvent,
			final Identifier targetUserId) {
		return new DefaultPopupNotificationEvent(lineMismatchEvent,
				targetUserId);
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

	/**
	 * @see PopupNotificationEvent#getResultId()
	 */
	public Identifier getResultId() {
		return this.resultId;
	}

	/**
	 * @see PopupNotificationEvent#getMismatchOpticalDistance()
	 */
	public double getMismatchOpticalDistance() {
		return this.mismatchOpticalDistance;
	}

	/**
	 * @see PopupNotificationEvent#getMismatchPhysicalDistance()
	 */
	public double getMismatchPhysicalDistance() {
		return this.mismatchPhysicalDistance;
	}

	/**
	 * @see PopupNotificationEvent#getMismatchCreated()
	 */
	public Date getMismatchCreated() {
		return (Date) this.mismatchCreated.clone();
	}
}
