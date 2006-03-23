/*-
 * $Id: IdlLineMismatchEventImpl.java,v 1.6.2.3 2006/03/23 10:48:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2.corba;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import com.syrus.AMFICOM.eventv2.DefaultLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEventPackage.IdlEventType;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialData;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialDataPackage.IdlAffectedPathElementSpatious;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6.2.3 $, $Date: 2006/03/23 10:48:43 $
 * @module event
 */
final class IdlLineMismatchEventImpl extends IdlLineMismatchEvent {
	private static final long serialVersionUID = -3747979777168377362L;

	IdlLineMismatchEventImpl() {
		// empty
	}

	IdlLineMismatchEventImpl(final IdlIdentifier affectedPathElementId,
			final IdlSpatialData spatialData,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final String message,
			final IdlIdentifier reflectogramMismatchEventId) {
		final IdlIdentifier voidId = VOID_IDENTIFIER.getIdlTransferable();
		this.id = voidId;
		this.creatorId = voidId;
		this.modifierId = voidId;

		this.affectedPathElementId = affectedPathElementId;
		this.spatialData = spatialData;

		this.mismatchOpticalDistance = mismatchOpticalDistance;
		this.mismatchPhysicalDistance = mismatchPhysicalDistance;

		this.message = message;
		this.reflectogramMismatchEventId = reflectogramMismatchEventId;
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
	 * @see IdlLineMismatchEvent#getMismatchOpticalDistance()
	 */
	@Override
	public double getMismatchOpticalDistance() {
		return this.mismatchOpticalDistance;
	}

	/**
	 * @see IdlLineMismatchEvent#getMismatchPhysicalDistance()
	 */
	@Override
	public double getMismatchPhysicalDistance() {
		return this.mismatchPhysicalDistance;
	}

	/**
	 * @see IdlLineMismatchEvent#getMessage()
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

	/**
	 * @see IdlLineMismatchEvent#getReflectogramMismatchEventId()
	 */
	@Override
	public IdlIdentifier getReflectogramMismatchEventId() {
		return this.reflectogramMismatchEventId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public DefaultLineMismatchEvent getNative() throws IdlCreateObjectException {
		try {
			return new DefaultLineMismatchEvent(this);
		} catch (final CreateObjectException coe) {
			throw coe.getIdlTransferable();
		}
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see IdlEvent#getNativeEvent()
	 */
	public LineMismatchEvent getNativeEvent() throws IdlCreateObjectException {
		return this.getNative();
	}
}
