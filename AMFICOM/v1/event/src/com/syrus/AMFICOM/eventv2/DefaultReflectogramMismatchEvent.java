/*-
 * $Id: DefaultReflectogramMismatchEvent.java,v 1.13.4.3 2006/03/21 10:09:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REFLECTOGRAMMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Date;

import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.AMFICOM.reflectometry.SOAnchor;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.13.4.3 $, $Date: 2006/03/21 10:09:19 $
 * @module event
 */
public final class DefaultReflectogramMismatchEvent extends
		AbstractReflectogramMismatchEvent {
	private static final long serialVersionUID = -1232479623372509377L;

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
	private Severity severity;

	/**
	 * @serial include
	 */
	private boolean anchors;

	/**
	 * @serial include
	 */
	private SoAnchorImpl anchor1Id;

	/**
	 * @serial include
	 */
	private SoAnchorImpl anchor2Id;

	/**
	 * @serial include
	 */
	private int anchor1Coord;

	/**
	 * @serial include
	 */
	private int anchor2Coord;

	/**
	 * @serial include
	 */
	private int coord;

	/**
	 * @serial include
	 */
	private int endCoord;

	/**
	 * @serial include
	 */
	private AlarmType alarmType;

	/**
	 * @serial include
	 */
	private double deltaX;

	/**
	 * @serial include
	 */
	private Identifier resultId;

	/**
	 * @serial include
	 */
	private Identifier monitoredElementId;

	/**
	 * Ctor used solely by database driver.
	 *
	 * @param id
	 */
	DefaultReflectogramMismatchEvent(final Identifier id) {
		super(id, VOID_IDENTIFIER, null, ILLEGAL_VERSION);
	}

	/**
	 * @param id
	 * @param creatorId
	 * @param created
	 * @param version
	 * @param reflectogramMismatch
	 * @param resultId
	 * @param monitoredElementId
	 */
	private DefaultReflectogramMismatchEvent(final Identifier id,
			final Identifier creatorId,
			final Date created,
			final StorableObjectVersion version,
			final ReflectogramMismatch reflectogramMismatch,
			final Identifier resultId,
			final Identifier monitoredElementId) {
		super(id, creatorId, created, version);

		/*
		 * resultId: strict check (void not permitted at object creation
		 * stage)
		 */
		if (resultId == null) {
			throw new NullPointerException("resultId is null");
		}
		final short resultIdMajor = resultId.getMajor();
		if (resultIdMajor != RESULT_CODE) {
			throw new IllegalArgumentException(resultId.isVoid()
					? "resultId is void"
					: "Type of resultId: ``"
							+ ObjectEntities.codeToString(resultIdMajor)
							+ "'' is invalid");
		}

		/*
		 * monitoredElementId: strict check (void not permitted at
		 * object creation stage)
		 */
		if (monitoredElementId == null) {
			throw new NullPointerException("monitoredElementId is null");
		}
		final short monitoredElementIdMajor = monitoredElementId.getMajor();
		if (monitoredElementIdMajor != MONITOREDELEMENT_CODE) {
			throw new IllegalArgumentException(monitoredElementId.isVoid()
					? "monitoredElementId is void"
					: "Type of monitoredElementId: ``"
							+ ObjectEntities.codeToString(monitoredElementIdMajor)
							+ "'' is invalid");
		}


		if (!!(this.mismatch = reflectogramMismatch.hasMismatch())) {
			this.minMismatch = reflectogramMismatch.getMinMismatch();
			this.maxMismatch = reflectogramMismatch.getMaxMismatch();

			if (this.minMismatch > this.maxMismatch) {
				throw new IllegalArgumentException();
			}
		}

		this.severity = reflectogramMismatch.getSeverity();

		if (!!(this.anchors = reflectogramMismatch.hasAnchors())) {
			/*
			 * We can't guarantee foreign anchors to be immutable.
			 */
			this.anchor1Id = new SoAnchorImpl(reflectogramMismatch.getAnchor1Id());
			this.anchor2Id = new SoAnchorImpl(reflectogramMismatch.getAnchor2Id());
			this.anchor1Coord = reflectogramMismatch.getAnchor1Coord();
			this.anchor2Coord = reflectogramMismatch.getAnchor2Coord();
		}

		this.coord = reflectogramMismatch.getCoord();
		this.endCoord = reflectogramMismatch.getEndCoord();
		this.alarmType = reflectogramMismatch.getAlarmType();
		this.deltaX = reflectogramMismatch.getDeltaX();
		this.resultId = resultId;
		this.monitoredElementId = monitoredElementId;
	}

	/**
	 * This contructor is invoked from both
	 * {@link com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventImpl#getNativeEvent()}
	 * and {@link com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventImpl#getNative()}.
	 * However, it is assumed that the former invocation occurs on the event
	 * server, and the object should be stored in the database, while the
	 * latter one is made client-side, when the object has already been
	 * stored.
	 *
	 * @param reflectogramMismatchEvent
	 * @param store {@code true} if object should be stored in the database;
	 *              {@code false} otherwise.
	 * @throws CreateObjectException
	 */
	public DefaultReflectogramMismatchEvent(
			final IdlReflectogramMismatchEvent reflectogramMismatchEvent,
			final boolean store)
	throws CreateObjectException {
		try {
			this.fromIdlTransferable(reflectogramMismatchEvent);
			if (store) {
				this.markAsChanged();
				StorableObjectPool.flush(this, this.creatorId, false);
			}
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public void fromIdlTransferable(final IdlReflectogramMismatchEvent reflectogramMismatchEvent)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable((IdlStorableObject) reflectogramMismatchEvent);

			/*
			 * resultId: loose check (void permitted)
			 */
			@SuppressWarnings("hiding")
			final Identifier resultId = Identifier.valueOf(reflectogramMismatchEvent.getResultId());
			final short resultIdMajor = resultId.getMajor();
			if (!(resultIdMajor == RESULT_CODE
					|| resultId.isVoid())) {
				throw new IllegalArgumentException("Type of resultId: ``"
						+ ObjectEntities.codeToString(resultIdMajor)
						+ "'' is invalid");
			}

			/*
			 * monitoredElementId: loose check (void permitted)
			 */
			@SuppressWarnings("hiding")
			final Identifier monitoredElementId = Identifier.valueOf(reflectogramMismatchEvent.getMonitoredElementId());
			final short monitoredElementIdMajor = monitoredElementId.getMajor();
			if (!(monitoredElementIdMajor == MONITOREDELEMENT_CODE
					|| monitoredElementId.isVoid())) {
				throw new IllegalArgumentException("Type of monitoredElementId: ``"
						+ ObjectEntities.codeToString(monitoredElementIdMajor)
						+ "'' is invalid");
			}


			if (!!(this.mismatch = reflectogramMismatchEvent.hasMismatch())) {
				this.minMismatch = reflectogramMismatchEvent.getMinMismatch();
				this.maxMismatch = reflectogramMismatchEvent.getMaxMismatch();

				if (this.minMismatch > this.maxMismatch) {
					throw new IllegalArgumentException();
				}
			}

			this.severity = Severity.valueOf(reflectogramMismatchEvent.getSeverity());

			if (!!(this.anchors = reflectogramMismatchEvent.hasAnchors())) {
				this.anchor1Id = new SoAnchorImpl(Identifier.valueOf(reflectogramMismatchEvent.getAnchor1Id()));
				this.anchor2Id = new SoAnchorImpl(Identifier.valueOf(reflectogramMismatchEvent.getAnchor2Id()));
				this.anchor1Coord = reflectogramMismatchEvent.getAnchor1Coord();
				this.anchor2Coord = reflectogramMismatchEvent.getAnchor2Coord();
			}

			this.coord = reflectogramMismatchEvent.getCoord();
			this.endCoord = reflectogramMismatchEvent.getEndCoord();
			this.alarmType = AlarmType.valueOf(reflectogramMismatchEvent.getAlarmType());
			this.deltaX = reflectogramMismatchEvent.getDeltaX();
			this.resultId = resultId;
			this.monitoredElementId = monitoredElementId;
		}
	}

	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final AlarmType alarmType,
			final Severity severity,
			final int coord,
			final int endCoord,
			final double deltaX,
			final boolean mismatch,
			final double minMismatch,
			final double maxMismatch,
			final boolean anchors,
			final Identifier anchor1Id,
			final Identifier anchor2Id,
			final int anchor1Coord,
			final int anchor2Coord,
			final Identifier resultId,
			final Identifier monitoredElementId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		/*
		 * resultId: loose check (void permitted)
		 */
		final short resultIdMajor = resultId.getMajor();
		if (!(resultIdMajor == RESULT_CODE
				|| resultId.isVoid())) {
			throw new IllegalArgumentException("Type of resultId: ``"
					+ ObjectEntities.codeToString(resultIdMajor)
					+ "'' is invalid");
		}

		/*
		 * monitoredElementId: loose check (void permitted)
		 */
		final short monitoredElementIdMajor = monitoredElementId.getMajor();
		if (!(monitoredElementIdMajor == MONITOREDELEMENT_CODE
				|| monitoredElementId.isVoid())) {
			throw new IllegalArgumentException("Type of monitoredElementId: ``"
					+ ObjectEntities.codeToString(monitoredElementIdMajor)
					+ "'' is invalid");
		}


		if (!!(this.mismatch = mismatch)) {
			this.minMismatch = minMismatch;
			this.maxMismatch = maxMismatch;

			if (this.minMismatch > this.maxMismatch) {
				throw new IllegalArgumentException();
			}
		}

		this.severity = severity;

		if (!!(this.anchors = anchors)) {
			this.anchor1Id = new SoAnchorImpl(anchor1Id);
			this.anchor2Id = new SoAnchorImpl(anchor2Id);
			this.anchor1Coord = anchor1Coord;
			this.anchor2Coord = anchor2Coord;
		}

		this.coord = coord;
		this.endCoord = endCoord;
		this.alarmType = alarmType;
		this.deltaX = deltaX;
		this.resultId = resultId;
		this.monitoredElementId = monitoredElementId;
	}

	/**
	 * This method should be invoked agent-side when no database storing
	 * is needed (let event server take care of this), so the newly-created
	 * object is not marked as changed.
	 *
	 * @param creatorId
	 * @param reflectogramMismatch
	 * @param resultId
	 * @param monitoredElementId
	 * @throws CreateObjectException
	 */
	public static ReflectogramMismatchEvent newInstance(
			final Identifier creatorId,
			final ReflectogramMismatch reflectogramMismatch,
			final Identifier resultId,
			final Identifier monitoredElementId)
	throws CreateObjectException {
		if (creatorId == null) {
			throw new NullPointerException("creatorId is null");
		}
		if (creatorId.isVoid()) {
			throw new IllegalArgumentException("creatorId is void");
		}

		try {
			final DefaultReflectogramMismatchEvent reflectogramMismatchEvent = new DefaultReflectogramMismatchEvent(
					IdentifierPool.getGeneratedIdentifier(REFLECTOGRAMMISMATCHEVENT_CODE),
					creatorId,
					new Date(),
					INITIAL_VERSION,
					reflectogramMismatch,
					resultId, 
					monitoredElementId);
			return reflectogramMismatchEvent;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#hasMismatch()
	 */
	public boolean hasMismatch() {
		return this.mismatch;
	}

	/**
	 * @throws IllegalStateException
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getMinMismatch()
	 */
	public double getMinMismatch() {
		if (this.hasMismatch()) {
			return this.minMismatch;
		}
		throw new IllegalStateException();
	}

	/**
	 * @throws IllegalStateException
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getMaxMismatch()
	 */
	public double getMaxMismatch() {
		if (this.hasMismatch()) {
			return this.maxMismatch;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getSeverity()
	 */
	public Severity getSeverity() {
		return this.severity;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getDistance()
	 */
	public double getDistance() {
		return this.getDeltaX() * this.getCoord();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#hasAnchors()
	 */
	public boolean hasAnchors() {
		return this.anchors;
	}

	/**
	 * @throws IllegalStateException
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getAnchor1Id()
	 */
	public SOAnchor getAnchor1Id() {
		if (this.hasAnchors()) {
			return this.anchor1Id;
		}
		throw new IllegalStateException();
	}

	/**
	 * @throws IllegalStateException
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getAnchor2Id()
	 */
	public SOAnchor getAnchor2Id() {
		if (this.hasAnchors()) {
			return this.anchor2Id;
		}
		throw new IllegalStateException();
	}

	/**
	 * @throws IllegalStateException
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getAnchor1Coord()
	 */
	public int getAnchor1Coord() {
		if (this.hasAnchors()) {
			return this.anchor1Coord;
		}
		throw new IllegalStateException();
	}

	/**
	 * @throws IllegalStateException
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getAnchor2Coord()
	 */
	public int getAnchor2Coord() {
		if (this.hasAnchors()) {
			return this.anchor2Coord;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getCoord()
	 */
	public int getCoord() {
		return this.coord;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getEndCoord()
	 */
	public int getEndCoord() {
		return this.endCoord;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getAlarmType()
	 */
	public AlarmType getAlarmType() {
		return this.alarmType;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getDeltaX()
	 */
	public double getDeltaX() {
		return this.deltaX;
	}

	/**
	 * @see ReflectogramMismatchEvent#getResultId()
	 */
	public Identifier getResultId() {
		return this.resultId;
	}

	/**
	 * @see ReflectogramMismatchEvent#getMonitoredElementId()
	 */
	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}
}
