/*-
 * $Id: IdlReflectogramMismatchEventImpl.java,v 1.12 2006/03/30 12:10:05 bass Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import com.syrus.AMFICOM.eventv2.DefaultReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorData;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlMismatchData;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorDataPackage.IdlAnchor;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlMismatchDataPackage.IdlMismatch;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.reflectometry.corba.IdlAlarmType;
import com.syrus.AMFICOM.reflectometry.corba.IdlSeverity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2006/03/30 12:10:05 $
 * @module event
 */
final class IdlReflectogramMismatchEventImpl
		extends IdlReflectogramMismatchEvent {
	private static final long serialVersionUID = -3105440293968978282L;

	IdlReflectogramMismatchEventImpl() {
		// empty
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param mismatchData
	 * @param severity
	 * @param anchorData
	 * @param coord
	 * @param endCoord
	 * @param alarmType
	 * @param deltaX
	 * @param measurementId
	 */
	IdlReflectogramMismatchEventImpl(final IdlIdentifier id,
			final long created, final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId, final long version,
			final IdlMismatchData mismatchData,
			final IdlSeverity severity,
			final IdlAnchorData anchorData, final int coord,
			final int endCoord, final IdlAlarmType alarmType,
			final double deltaX,
			final IdlIdentifier measurementId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;

		this.mismatchData = mismatchData;
		this.severity = severity;
		this.anchorData = anchorData;
		this.coord = coord;
		this.endCoord = endCoord;
		this.alarmType = alarmType;
		this.deltaX = deltaX;
		this.measurementId = measurementId;
	}

	/**
	 * @see IdlReflectogramMismatchEvent#getCreated()
	 */
	@Override
	public long getCreated() {
		return this.created;
	}

	@Override
	public boolean hasMismatch() {
		return this.mismatchData.discriminator() == IdlMismatch._TRUE;
	}

	@Override
	public double getMinMismatch() {
		if (this.hasMismatch()) {
			return this.mismatchData.mismatchPair().minMismatch;
		}
		throw new IllegalStateException();
	}

	@Override
	public double getMaxMismatch() {
		if (this.hasMismatch()) {
			return this.mismatchData.mismatchPair().maxMismatch;
		}
		throw new IllegalStateException();
	}

	@Override
	public IdlSeverity getSeverity() {
		return this.severity;
	}

	@Override
	public boolean hasAnchors() {
		return this.anchorData.discriminator() == IdlAnchor._TRUE;
	}

	@Override
	public IdlIdentifier getAnchor1Id() {
		if (this.hasAnchors()) {
			return this.anchorData.anchorPair().anchor1Id;
		}
		throw new IllegalStateException();
	}

	@Override
	public IdlIdentifier getAnchor2Id() {
		if (this.hasAnchors()) {
			return this.anchorData.anchorPair().anchor2Id;
		}
		throw new IllegalStateException();
	}

	@Override
	public int getAnchor1Coord() {
		if (this.hasAnchors()) {
			return this.anchorData.anchorPair().anchor1Coord;
		}
		throw new IllegalStateException();
	}

	@Override
	public int getAnchor2Coord() {
		if (this.hasAnchors()) {
			return this.anchorData.anchorPair().anchor2Coord;
		}
		throw new IllegalStateException();
	}

	@Override
	public int getCoord() {
		return this.coord;
	}

	@Override
	public int getEndCoord() {
		return this.endCoord;
	}

	@Override
	public IdlAlarmType getAlarmType() {
		return this.alarmType;
	}

	@Override
	public double getDeltaX() {
		return this.deltaX;
	}

	/**
	 * @see IdlReflectogramMismatchEvent#getMeasurementId()
	 */
	@Override
	public IdlIdentifier getMeasurementId() {
		return this.measurementId;
	}

	public IdlEventType getType() {
		return IdlEventType.REFLECTOGRAM_MISMATCH;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public DefaultReflectogramMismatchEvent getNative()
	throws IdlCreateObjectException {
		try {
			return new DefaultReflectogramMismatchEvent(this, false);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.eventv2.corba.IdlEvent#getNativeEvent()
	 */
	public ReflectogramMismatchEvent getNativeEvent()
	throws IdlCreateObjectException {
		try {
			return new DefaultReflectogramMismatchEvent(this, true);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}
}
