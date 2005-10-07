/*-
 * $Id: IdlReflectogramMismatchEventImpl.java,v 1.1 2005/10/07 14:58:57 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import com.syrus.AMFICOM.eventv2.DefaultReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorData;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlMismatchData;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorDataPackage.IdlAnchor;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlMismatchDataPackage.IdlMismatch;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.reflectometry.corba.IdlAlarmType;
import com.syrus.AMFICOM.reflectometry.corba.IdlSeverity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/07 14:58:57 $
 * @module event
 */
final class IdlReflectogramMismatchEventImpl
		extends IdlReflectogramMismatchEvent {
	private static final long serialVersionUID = -3105440293968978282L;

	IdlReflectogramMismatchEventImpl() {
		// empty
	}

	/**
	 * @param mismatchData
	 * @param severity
	 * @param distance
	 * @param anchorData
	 * @param coord
	 * @param endCoord
	 * @param alarmType
	 * @param deltaX
	 */
	IdlReflectogramMismatchEventImpl(final IdlMismatchData mismatchData,
			final IdlSeverity severity, final double distance,
			final IdlAnchorData anchorData, final int coord,
			final int endCoord, final IdlAlarmType alarmType,
			final double deltaX) {
		this.mismatchData = mismatchData;
		this.severity = severity;
		this.distance = distance;
		this.anchorData = anchorData;
		this.coord = coord;
		this.endCoord = endCoord;
		this.alarmType = alarmType;
		this.deltaX = deltaX;
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
	public double getDistance() {
		return this.distance;
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

	public ReflectogramMismatchEvent getNative() {
		return DefaultReflectogramMismatchEvent.valueOf(this);
	}
}
