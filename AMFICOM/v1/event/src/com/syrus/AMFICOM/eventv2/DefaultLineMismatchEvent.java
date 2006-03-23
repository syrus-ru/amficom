/*-
 * $Id: DefaultLineMismatchEvent.java,v 1.8.2.5 2006/03/23 13:07:23 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Date;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8.2.5 $, $Date: 2006/03/23 13:07:23 $
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
	private String message; 

	/**
	 * @serial include
	 */
	private Identifier reflectogramMismatchEventId;

	/**
	 * Ctor used solely by database driver.
	 *
	 * @param id
	 */
	DefaultLineMismatchEvent(final Identifier id) {
		super(id, VOID_IDENTIFIER, null, ILLEGAL_VERSION);
	}

	/**
	 * @param id
	 * @param creatorId
	 * @param affectedPathElementId
	 * @param affectedPathElementSpatious
	 * @param physicalDistanceToStart
	 * @param physicalDistanceToEnd
	 * @param mismatchOpticalDistance
	 * @param mismatchPhysicalDistance
	 * @param message
	 * @param reflectogramMismatchEventId
	 */
	private DefaultLineMismatchEvent(final Identifier id,
			final Identifier creatorId,
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpatious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final String message,
			final Identifier reflectogramMismatchEventId) {
		super(id, creatorId, new Date(), INITIAL_VERSION);

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
		this.message = message;
		this.reflectogramMismatchEventId = reflectogramMismatchEventId;
	}

	/**
	 * This contructor is invoked from both
	 * {@link com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventImpl#getNativeEvent()}
	 * and {@link com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventImpl#getNative()}.
	 *
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
			this.message = lineMismatchEvent.getMessage();
			this.reflectogramMismatchEventId = Identifier.valueOf(lineMismatchEvent.getReflectogramMismatchEventId());
		}
	}

	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpatious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final String message,
			final Identifier reflectogramMismatchEventId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		
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
		this.message = message;
		this.reflectogramMismatchEventId = reflectogramMismatchEventId;
	}

	/**
	 * This method should be invoked on the event server (where line 
	 * mismatch event generation occurs) for the newly created object gets
	 * flushed prior to being returned.
	 *
	 * @param creatorId
	 * @param affectedPathElementId
	 * @param affectedPathElementSpatious
	 * @param physicalDistanceToStart
	 * @param physicalDistanceToEnd
	 * @param mismatchOpticalDistance
	 * @param mismatchPhysicalDistance
	 * @param message
	 * @param reflectogramMismatchEventId
	 * @throws CreateObjectException
	 */
	public static LineMismatchEvent newInstance(
			final Identifier creatorId,
			final Identifier affectedPathElementId,
			final boolean affectedPathElementSpatious,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd,
			final double mismatchOpticalDistance,
			final double mismatchPhysicalDistance,
			final String message,
			final Identifier reflectogramMismatchEventId)
	throws CreateObjectException {
		if (creatorId == null) {
			throw new NullPointerException("creatorId is null");
		}
		if (creatorId.isVoid()) {
			throw new IllegalArgumentException("creatorId is void");
		}

		try {
			final DefaultLineMismatchEvent lineMismatchEvent = new DefaultLineMismatchEvent(
					IdentifierPool.getGeneratedIdentifier(LINEMISMATCHEVENT_CODE),
					creatorId,
					affectedPathElementId,
					affectedPathElementSpatious, physicalDistanceToStart,
					physicalDistanceToEnd, mismatchOpticalDistance,
					mismatchPhysicalDistance, message,
					reflectogramMismatchEventId);
			lineMismatchEvent.markAsChanged();
			StorableObjectPool.flush(lineMismatchEvent, creatorId, false);
			return lineMismatchEvent;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @see LineMismatchEvent#getAffectedPathElementId()
	 */
	public Identifier getAffectedPathElementId() {
		return this.affectedPathElementId;
	}

	/**
	 * @see LineMismatchEvent#isAffectedPathElementSpacious()
	 */
	public boolean isAffectedPathElementSpacious() {
		return this.affectedPathElementSpatious;
	}

	/**
	 * @see LineMismatchEvent#getPhysicalDistanceToStart()
	 */
	public double getPhysicalDistanceToStart() {
		if (this.isAffectedPathElementSpacious()) {
			return this.physicalDistanceToStart;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see LineMismatchEvent#getPhysicalDistanceToEnd()
	 */
	public double getPhysicalDistanceToEnd() {
		if (this.isAffectedPathElementSpacious()) {
			return this.physicalDistanceToEnd;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see LineMismatchEvent#getMismatchOpticalDistance()
	 */
	public double getMismatchOpticalDistance() {
		return this.mismatchOpticalDistance;
	}

	/**
	 * @see LineMismatchEvent#getMismatchPhysicalDistance()
	 */
	public double getMismatchPhysicalDistance() {
		return this.mismatchPhysicalDistance;
	}

	/**
	 * @see LineMismatchEvent#getMessage()
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @see LineMismatchEvent#getReflectogramMismatchEventId()
	 */
	public Identifier getReflectogramMismatchEventId() {
		return this.reflectogramMismatchEventId;
	}
}
