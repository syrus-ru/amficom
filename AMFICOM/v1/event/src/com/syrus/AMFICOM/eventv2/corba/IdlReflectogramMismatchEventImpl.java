/*-
 * $Id: IdlReflectogramMismatchEventImpl.java,v 1.9 2005/10/22 19:07:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import com.syrus.AMFICOM.eventv2.DefaultReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchData;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchDataPackage.IdlMismatch;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorData;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorDataPackage.IdlAnchor;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.reflectometry.corba.IdlAlarmType;
import com.syrus.AMFICOM.reflectometry.corba.IdlSeverity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/22 19:07:25 $
 * @module event
 */
final class IdlReflectogramMismatchEventImpl
		extends IdlReflectogramMismatchEvent {
	private static final long serialVersionUID = -3105440293968978282L;

	IdlReflectogramMismatchEventImpl() {
		// empty
	}

	/**
	 * @param created
	 * @param mismatchData
	 * @param severity
	 * @param anchorData
	 * @param coord
	 * @param endCoord
	 * @param alarmType
	 * @param deltaX
	 * @param resultId
	 * @param monitoredElementId
	 */
	IdlReflectogramMismatchEventImpl(final long created,
			final IdlMismatchData mismatchData,
			final IdlSeverity severity,
			final IdlAnchorData anchorData, final int coord,
			final int endCoord, final IdlAlarmType alarmType,
			final double deltaX,
			final IdlIdentifier resultId,
			final IdlIdentifier monitoredElementId) {
		final IdlIdentifier voidId = VOID_IDENTIFIER.getTransferable();

		this.id = voidId;
		this.created = created;
		this.creatorId = voidId;
		this.modifierId = voidId;

		this.mismatchData = mismatchData;
		this.severity = severity;
		this.anchorData = anchorData;
		this.coord = coord;
		this.endCoord = endCoord;
		this.alarmType = alarmType;
		this.deltaX = deltaX;
		this.resultId = resultId;
		this.monitoredElementId = monitoredElementId;
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

	@Override
	public IdlIdentifier getResultId() {
		return this.resultId;
	}

	/**
	 * @see IdlReflectogramMismatchEvent#getMonitoredElementId()
	 */
	@Override
	public IdlIdentifier getMonitoredElementId() {
		return this.monitoredElementId;
	}

	public IdlEventType getType() {
		return IdlEventType.REFLECTOGRAM_MISMATCH;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException();
	}

	public ReflectogramMismatchEvent getNativeEvent() {
		return DefaultReflectogramMismatchEvent.valueOf(this);
	}
}
