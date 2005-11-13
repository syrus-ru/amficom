/*-
 * $Id: DefaultPopupNotificationEvent.java,v 1.11 2005/11/13 11:47:42 bass Exp $
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
 * @version $Revision: 1.11 $, $Date: 2005/11/13 11:47:42 $
 * @module event
 */
public final class DefaultPopupNotificationEvent extends
		AbstractPopupNotificationEvent {
	private static final long serialVersionUID = 3901755454384903933L;


	private static final char NEWLINE = '\n';

	private static final char SPACE = ' ';

	private static final String COLON_TAB = ":\t";

	private static final String PHYSICAL_DISTANCE_TO = I18N.getString("NotificationEvent.PhysicalDistanceTo");

	private static final String METERS = I18N.getString("NotificationEvent.Meters");

	private static final String END_OF = I18N.getString("NotificationEvent.EndOf");

	private static final String START_OF = I18N.getString("NotificationEvent.StartOf");

	private static final String PATH_ELEMENT_GENITIVE = I18N.getString("NotificationEvent.PathElementGenitive");

	private static final String PATH_ELEMENT = I18N.getString("NotificationEvent.PathElement");

	private static final String AFFECTED = I18N.getString("NotificationEvent.Affected");

	private static final String MISMATCH_LEVEL = I18N.getString("NotificationEvent.MismatchLevel");

	private static final String MAXIMUM = I18N.getString("NotificationEvent.Maximum");

	private static final String MINIMUM = I18N.getString("NotificationEvent.Minimum");


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
		this.message = lineMismatchEvent.getAlarmType().getLocalizedDescription() + NEWLINE
				+ lineMismatchEvent.getSeverity().getLocalizedDescription() + NEWLINE
				+ (lineMismatchEvent.hasMismatch()
						? MINIMUM + SPACE + MISMATCH_LEVEL + COLON_TAB + lineMismatchEvent.getMinMismatch() + NEWLINE
						+ MAXIMUM + SPACE + MISMATCH_LEVEL + COLON_TAB + lineMismatchEvent.getMaxMismatch() + NEWLINE
						: "")
				+ AFFECTED + SPACE + PATH_ELEMENT + COLON_TAB + lineMismatchEvent.getAffectedPathElementId() + NEWLINE
				+ (lineMismatchEvent.isAffectedPathElementSpacious()
						? PHYSICAL_DISTANCE_TO + SPACE + START_OF + SPACE + PATH_ELEMENT_GENITIVE + COLON_TAB + ((int) lineMismatchEvent.getPhysicalDistanceToStart()) + SPACE + METERS + NEWLINE
						+ PHYSICAL_DISTANCE_TO + SPACE + END_OF + SPACE + PATH_ELEMENT_GENITIVE + COLON_TAB + ((int) lineMismatchEvent.getPhysicalDistanceToEnd()) + SPACE + METERS + NEWLINE
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
