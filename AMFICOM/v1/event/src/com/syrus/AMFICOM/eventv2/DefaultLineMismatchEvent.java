/*-
 * $Id: DefaultLineMismatchEvent.java,v 1.1 2005/10/10 11:03:22 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialData;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialDataPackage.IdlAffectedPathElementSpatious;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialDataPackage.IdlPhysicalDIstancePair;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchData;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchDataPackage.IdlMismatch;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchDataPackage.IdlMismatchPair;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/10 11:03:22 $
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
	private double physicalDistanceFromStart;

	/**
	 * @serial include
	 */
	private double physicalDistanceFromEnd;

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
			this.physicalDistanceFromStart = lineMismatchEvent.getPhysicalDistanceFromStart();
			this.physicalDistanceFromEnd = lineMismatchEvent.getPhysicalDistanceFromEnd();
		}
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
					new IdlPhysicalDIstancePair(
							this.getPhysicalDistanceFromStart(),
							this.getPhysicalDistanceFromEnd()));
		} else {
			spatialData._default(IdlAffectedPathElementSpatious._FALSE);
		}

		return IdlLineMismatchEventHelper.init(orb,
				this.getAlarmType().getTransferable(orb),
				this.getSeverity().getTransferable(orb),
				mismatchData,
				this.affectedPathElementId.getTransferable(orb),
				spatialData);
	}

	public static LineMismatchEvent valueOf(
			final IdlLineMismatchEvent lineMismatchEvent) {
		return new DefaultLineMismatchEvent(lineMismatchEvent);
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

	public double getPhysicalDistanceFromStart() {
		if (this.isAffectedPathElementSpacious()) {
			return this.physicalDistanceFromStart;
		}
		throw new IllegalStateException();
	}

	public double getPhysicalDistanceFromEnd() {
		if (this.isAffectedPathElementSpacious()) {
			return this.physicalDistanceFromEnd;
		}
		throw new IllegalStateException();
	}
}
