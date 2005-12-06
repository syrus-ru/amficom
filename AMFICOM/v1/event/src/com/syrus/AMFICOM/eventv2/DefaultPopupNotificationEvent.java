/*-
 * $Id: DefaultPopupNotificationEvent.java,v 1.14 2005/12/06 09:42:28 bass Exp $
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
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/12/06 09:42:28 $
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

	/**
	 * @serial include
	 */
	private Severity severity;

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
		this.severity = Severity.valueOf(popupNotificationEvent.getSeverity());
	}

	private DefaultPopupNotificationEvent(
			final LineMismatchEvent lineMismatchEvent,
			final String message,
			final Identifier targetUserId) {
		this.targetUserId = targetUserId;
		this.message = message;
		this.resultId = lineMismatchEvent.getResultId();
		this.mismatchOpticalDistance = lineMismatchEvent.getMismatchOpticalDistance();
		this.mismatchPhysicalDistance = lineMismatchEvent.getMismatchPhysicalDistance();
		this.mismatchCreated = new Date(lineMismatchEvent.getMismatchCreated().getTime());
		this.severity = lineMismatchEvent.getSeverity();
	}

	/**
	 * @param orb
	 * @see com.syrus.util.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlPopupNotificationEvent getIdlTransferable(final ORB orb) {
		return IdlPopupNotificationEventHelper.init(orb,
				this.getTargetUserId().getIdlTransferable(orb),
				this.getMessage(),
				this.getResultId().getIdlTransferable(orb),
				this.getMismatchOpticalDistance(),
				this.getMismatchPhysicalDistance(),
				this.mismatchCreated.getTime(),
				this.getSeverity().getIdlTransferable(orb));
	}

	public static PopupNotificationEvent valueOf(
			final IdlPopupNotificationEvent popupNotificationEvent) {
		return new DefaultPopupNotificationEvent(popupNotificationEvent);
	}

	public static PopupNotificationEvent valueOf(
			final LineMismatchEvent lineMismatchEvent,
			final String message,
			final Identifier targetUserId) {
		return new DefaultPopupNotificationEvent(lineMismatchEvent,
				message,
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

	/**
	 * @see PopupNotificationEvent#getSeverity()
	 */
	public Severity getSeverity() {
		return this.severity;
	}
}
