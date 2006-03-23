/*-
 * $Id: DefaultLineMismatchEvent.java,v 1.8.2.3 2006/03/23 07:58:00 bass Exp $
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
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpatialDataPackage.IdlPhysicalDistancePair;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8.2.3 $, $Date: 2006/03/23 07:58:00 $
 * @module event
 */
public final class DefaultLineMismatchEvent extends AbstractLineMismatchEvent {
	private static final long serialVersionUID = -1651689764279078776L;

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
	private double mismatchOpticalDistance;

	/**
	 * @serial include
	 */
	private double mismatchPhysicalDistance;

	/**
	 * @serial include
	 */
	private Identifier reflectogramMismatchEventId;

	/**
	 * @param lineMismatchEvent
	 * @throws CreateObjectException
	 */
	public DefaultLineMismatchEvent(final IdlLineMismatchEvent lineMismatchEvent)
	throws CreateObjectException {
		try {
			this.fromIdlTransferable(lineMismatchEvent);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	private DefaultLineMismatchEvent(final Identifier affectedPathElementId,
			final boolean affectedPathElementSpatious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final Identifier reflectogramMismatchEventId) {
		this.affectedPathElementId = affectedPathElementId;

		if (!!(this.affectedPathElementSpatious = affectedPathElementSpatious)) {
			this.physicalDistanceToStart = physicalDistanceToStart;
			this.physicalDistanceToEnd = physicalDistanceToEnd;

			if (this.physicalDistanceToStart < 0) {
				throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToStart));
			} else if (this.physicalDistanceToEnd < 0) {
				throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToEnd));
			}
		}

		this.mismatchOpticalDistance = mismatchOpticalDistance;
		this.mismatchPhysicalDistance = mismatchPhysicalDistance;
		this.reflectogramMismatchEventId = reflectogramMismatchEventId;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	@Override
	public IdlLineMismatchEvent getIdlTransferable(final ORB orb) {
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
				this.getAffectedPathElementId().getIdlTransferable(orb),
				spatialData,
				this.getMismatchOpticalDistance(),
				this.getMismatchPhysicalDistance(),
				this.reflectogramMismatchEventId.getIdlTransferable(orb));
	}

	public void fromIdlTransferable(final IdlLineMismatchEvent lineMismatchEvent)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable((IdlStorableObject) lineMismatchEvent);

			this.affectedPathElementId = Identifier.valueOf(lineMismatchEvent.getAffectedPathElementId());

			if (!!(this.affectedPathElementSpatious = lineMismatchEvent.isAffectedPathElementSpatious())) {
				this.physicalDistanceToStart = lineMismatchEvent.getPhysicalDistanceToStart();
				this.physicalDistanceToEnd = lineMismatchEvent.getPhysicalDistanceToEnd();

				if (this.physicalDistanceToStart < 0) {
					throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToStart));
				} else if (this.physicalDistanceToEnd < 0) {
					throw new IllegalArgumentException(String.valueOf(this.physicalDistanceToEnd));
				}
			}

			this.mismatchOpticalDistance = lineMismatchEvent.getMismatchOpticalDistance();
			this.mismatchPhysicalDistance = lineMismatchEvent.getMismatchPhysicalDistance();
		}
	}

	public static LineMismatchEvent valueOf(
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpatious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final Identifier reflectogramMismatchEventId) {
		return new DefaultLineMismatchEvent(affectedPathElementId,
				affectedPathElementSpatious, physicalDistanceToStart,
				physicalDistanceToEnd, mismatchOpticalDistance,
				mismatchPhysicalDistance,
				reflectogramMismatchEventId);
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
	 * @see LineMismatchEvent#getReflectogramMismatchEventId()
	 */
	public Identifier getReflectogramMismatchEventId() {
		return this.reflectogramMismatchEventId;
	}
}
