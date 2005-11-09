/*-
 * $Id: DefaultLineMismatchEvent.java,v 1.5 2005/11/09 10:58:59 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Date;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialData;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialDataPackage.IdlAffectedPathElementSpatious;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialDataPackage.IdlPhysicalDistancePair;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchData;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchDataPackage.IdlMismatch;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchDataPackage.IdlMismatchPair;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/11/09 10:58:59 $
 * @module event
 */
public final class DefaultLineMismatchEvent extends AbstractLineMismatchEvent {
	private static final long serialVersionUID = -1651689764279078776L;

	/**
	 * @serial include
	 */
	private AlarmType alarmType;

	/**
	 * @serial include
	 */
	private Severity severity;

	/**
	 * @serial include
	 */
	private boolean mismatch;

	/**
	 * @serial include
	 */
	private double minMismatch;

	/**
	 * @serial include
	 */
	private double maxMismatch;

	/**
	 * @serial include
	 */
	private Identifier affectedPathElementId;

	/**
	 * @serial include
	 */
	private boolean affectedPathElementSpatious;

	/**
	 * @serial include
	 */
	private double physicalDistanceToStart;

	/**
	 * @serial include
	 */
	private double physicalDistanceToEnd;

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

	private DefaultLineMismatchEvent(final IdlLineMismatchEvent lineMismatchEvent) {
		this.alarmType = AlarmType.valueOf(lineMismatchEvent.getAlarmType());
		this.severity = Severity.valueOf(lineMismatchEvent.getSeverity());

		if (!!(this.mismatch = lineMismatchEvent.hasMismatch())) {
			this.minMismatch = lineMismatchEvent.getMinMismatch();
			this.maxMismatch = lineMismatchEvent.getMaxMismatch();

			if (this.minMismatch > this.maxMismatch) {
				throw new IllegalArgumentException();
			}
		}

		this.affectedPathElementId = Identifier.valueOf(lineMismatchEvent.getAffectedPathElementId());

		if (!!(this.affectedPathElementSpatious = lineMismatchEvent.isAffectedPathElementSpatious())) {
			this.physicalDistanceToStart = lineMismatchEvent.getPhysicalDistanceToStart();
			this.physicalDistanceToEnd = lineMismatchEvent.getPhysicalDistanceToEnd();
		}

		this.resultId = Identifier.valueOf(lineMismatchEvent.getResultId());
		this.mismatchOpticalDistance = lineMismatchEvent.getMismatchOpticalDistance();
		this.mismatchPhysicalDistance = lineMismatchEvent.getMismatchPhysicalDistance();
		this.mismatchCreated = new Date(lineMismatchEvent.getMismatchCreated());
	}

	private DefaultLineMismatchEvent(final AlarmType alarmType,
			final Severity severity,
			final boolean mismatch,
			final double minMismatch,
			final double maxMismatch,
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpatious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final Identifier resultId,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final Date mismatchCreated) {
		this.alarmType = alarmType;
		this.severity = severity;

		if (!!(this.mismatch = mismatch)) {
			this.minMismatch = minMismatch;
			this.maxMismatch = maxMismatch;

			if (this.minMismatch > this.maxMismatch) {
				throw new IllegalArgumentException();
			}
		}

		this.affectedPathElementId = affectedPathElementId;

		if (!!(this.affectedPathElementSpatious = affectedPathElementSpatious)) {
			this.physicalDistanceToStart = physicalDistanceToStart;
			this.physicalDistanceToEnd = physicalDistanceToEnd;
		}

		this.resultId = resultId;
		this.mismatchOpticalDistance = mismatchOpticalDistance;
		this.mismatchPhysicalDistance = mismatchPhysicalDistance;
		this.mismatchCreated = new Date(mismatchCreated.getTime());
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(ORB)
	 */
	public IdlLineMismatchEvent getTransferable(final ORB orb) {
		final IdlMismatchData mismatchData = new IdlMismatchData();
		if (this.hasMismatch()) {
			mismatchData.mismatchPair(IdlMismatch._TRUE, new IdlMismatchPair(
					this.getMinMismatch(),
					this.getMaxMismatch()));
		} else {
			mismatchData._default(IdlMismatch._FALSE);
		}

		final IdlSpatialData spatialData = new IdlSpatialData();
		if (this.isAffectedPathElementSpacious()) {
			spatialData.physicalDistancePair(
					IdlAffectedPathElementSpatious._TRUE,
					new IdlPhysicalDistancePair(
							this.getPhysicalDistanceToStart(),
							this.getPhysicalDistanceToEnd()));
		} else {
			spatialData._default(IdlAffectedPathElementSpatious._FALSE);
		}

		return IdlLineMismatchEventHelper.init(orb,
				this.getAlarmType().getTransferable(orb),
				this.getSeverity().getTransferable(orb),
				mismatchData,
				this.getAffectedPathElementId().getTransferable(orb),
				spatialData,
				this.getResultId().getTransferable(orb),
				this.getMismatchOpticalDistance(),
				this.getMismatchPhysicalDistance(),
				this.mismatchCreated.getTime());
	}

	public static LineMismatchEvent valueOf(
			final IdlLineMismatchEvent lineMismatchEvent) {
		return new DefaultLineMismatchEvent(lineMismatchEvent);
	}

	public static LineMismatchEvent valueOf(final AlarmType alarmType,
			final Severity severity,
			final boolean mismatch,
			final double minMismatch,
			final double maxMismatch,
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpatious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final Identifier resultId,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final Date mismatchCreated) {
		return new DefaultLineMismatchEvent(alarmType, severity, mismatch,
				minMismatch, maxMismatch, affectedPathElementId,
				affectedPathElementSpatious, physicalDistanceToStart,
				physicalDistanceToEnd, resultId, mismatchOpticalDistance,
				mismatchPhysicalDistance, mismatchCreated);
	}

	public AlarmType getAlarmType() {
		return this.alarmType;
	}

	public Severity getSeverity() {
		return this.severity;
	}

	public boolean hasMismatch() {
		return this.mismatch;
	}

	public double getMinMismatch() {
		if (this.hasMismatch()) {
			return this.minMismatch;
		}
		throw new IllegalStateException();
	}

	public double getMaxMismatch() {
		if (this.hasMismatch()) {
			return this.maxMismatch;
		}
		throw new IllegalStateException();
	}

	public Identifier getAffectedPathElementId() {
		return this.affectedPathElementId;
	}

	public boolean isAffectedPathElementSpacious() {
		return this.affectedPathElementSpatious;
	}

	public double getPhysicalDistanceToStart() {
		if (this.isAffectedPathElementSpacious()) {
			return this.physicalDistanceToStart;
		}
		throw new IllegalStateException();
	}

	public double getPhysicalDistanceToEnd() {
		if (this.isAffectedPathElementSpacious()) {
			return this.physicalDistanceToEnd;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see LineMismatchEvent#getResultId()
	 * @see PopupNotificationEvent#getResultId()
	 */
	public Identifier getResultId() {
		return this.resultId;
	}

	/**
	 * @see LineMismatchEvent#getMismatchOpticalDistance()
	 * @see PopupNotificationEvent#getMismatchOpticalDistance()
	 */
	public double getMismatchOpticalDistance() {
		return this.mismatchOpticalDistance;
	}

	/**
	 * @see LineMismatchEvent#getMismatchPhysicalDistance()
	 * @see PopupNotificationEvent#getMismatchPhysicalDistance()
	 */
	public double getMismatchPhysicalDistance() {
		return this.mismatchPhysicalDistance;
	}

	/**
	 * @see LineMismatchEvent#getMismatchCreated()
	 * @see PopupNotificationEvent#getMismatchCreated()
	 */
	public Date getMismatchCreated() {
		return (Date) this.mismatchCreated.clone();
	}
}
