/*-
 * $Id: IdlLineMismatchEventImpl.java,v 1.4 2005/10/13 10:47:42 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import com.syrus.AMFICOM.eventv2.DefaultLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialData;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialDataPackage.IdlAffectedPathElementSpatious;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchData;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchDataPackage.IdlMismatch;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.reflectometry.corba.IdlAlarmType;
import com.syrus.AMFICOM.reflectometry.corba.IdlSeverity;
import static com.syrus.AMFICOM.general.Identifier.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/10/13 10:47:42 $
 * @module event
 */
final class IdlLineMismatchEventImpl extends IdlLineMismatchEvent {
	private static final long serialVersionUID = -3747979777168377362L;

	IdlLineMismatchEventImpl() {
		// empty
	}

	IdlLineMismatchEventImpl(final IdlAlarmType alarmType,
			final IdlSeverity severity,
			final IdlMismatchData mismatchData,
			final IdlIdentifier affectedPathElementId,
			final IdlSpatialData spatialData) {
		final IdlIdentifier voidId = VOID_IDENTIFIER.getTransferable();
		this.id = voidId;
		this.creatorId = voidId;
		this.modifierId = voidId;

		this.alarmType = alarmType;
		this.severity = severity;
		this.mismatchData = mismatchData;
		this.affectedPathElementId = affectedPathElementId;
		this.spatialData = spatialData;
	}

	/**
	 * @see IdlLineMismatchEvent#getAlarmType()
	 */
	@Override
	public IdlAlarmType getAlarmType() {
		return this.alarmType;
	}

	/**
	 * @see IdlLineMismatchEvent#getSeverity()
	 */
	@Override
	public IdlSeverity getSeverity() {
		return this.severity;
	}

	/**
	 * @see IdlLineMismatchEvent#hasMismatch()
	 */
	@Override
	public boolean hasMismatch() {
		return this.mismatchData.discriminator() == IdlMismatch._TRUE;
	}

	/**
	 * @see IdlLineMismatchEvent#getMinMismatch()
	 */
	@Override
	public double getMinMismatch() {
		if (this.hasMismatch()) {
			return this.mismatchData.mismatchPair().minMismatch;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see IdlLineMismatchEvent#getMaxMismatch()
	 */
	@Override
	public double getMaxMismatch() {
		if (this.hasMismatch()) {
			return this.mismatchData.mismatchPair().maxMismatch;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see IdlLineMismatchEvent#getAffectedPathElementId()
	 */
	@Override
	public IdlIdentifier getAffectedPathElementId() {
		return this.affectedPathElementId;
	}

	/**
	 * @see IdlLineMismatchEvent#isAffectedPathElementSpatious()
	 */
	@Override
	public boolean isAffectedPathElementSpatious() {
		return this.spatialData.discriminator() == IdlAffectedPathElementSpatious._TRUE;
	}

	/**
	 * @see IdlLineMismatchEvent#getPhysicalDistanceToStart()
	 */
	@Override
	public double getPhysicalDistanceToStart() {
		if (this.isAffectedPathElementSpatious()) {
			return this.spatialData.physicalDistancePair().physicalDistanceToStart;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see IdlLineMismatchEvent#getPhysicalDistanceToEnd()
	 */
	@Override
	public double getPhysicalDistanceToEnd() {
		if (this.isAffectedPathElementSpatious()) {
			return this.spatialData.physicalDistancePair().physicalDistanceToEnd;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see IdlEvent#getType()
	 */
	public IdlEventType getType() {
		return IdlEventType.LINE_MISMATCH;
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
	public LineMismatchEvent getNativeEvent() {
		return DefaultLineMismatchEvent.valueOf(this);
	}
}
