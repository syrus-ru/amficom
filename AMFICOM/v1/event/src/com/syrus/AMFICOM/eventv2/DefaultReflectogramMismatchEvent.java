/*-
 * $Id: DefaultReflectogramMismatchEvent.java,v 1.9 2005/10/22 19:07:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;

import java.io.Serializable;
import java.util.Date;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchData;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchDataPackage.IdlMismatch;
import com.syrus.AMFICOM.eventv2.corba.IdlMismatchContainerPackage.IdlMismatchDataPackage.IdlMismatchPair;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorData;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorDataPackage.IdlAnchor;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorDataPackage.IdlAnchorPair;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.AMFICOM.reflectometry.SOAnchor;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/22 19:07:25 $
 * @module event
 */
public final class DefaultReflectogramMismatchEvent extends
		AbstractReflectogramMismatchEvent {
	private static final long serialVersionUID = -1232479623372509377L;

	/**
	 * @serial include
	 */
	private Date created;

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

	private DefaultReflectogramMismatchEvent(
			final ReflectogramMismatch reflectogramMismatch,
			final Identifier resultId,
			final Identifier monitoredElementId) {
		this.created = new Date();

		if (resultId == null) {
			throw new NullPointerException("resultId is null");
		}
		if (monitoredElementId == null) {
			throw new NullPointerException("monitoredElementId is null");
		}
		/*
		 * Currently, the second check is unnecessary. But
		 * implementation may eventually change.
		 */
		if (resultId.getMajor() != RESULT_CODE
				|| resultId.isVoid()) {
			throw new IllegalArgumentException("Either resultId is void or its type is invalid");
		}
		if (monitoredElementId.getMajor() != MONITOREDELEMENT_CODE
				|| monitoredElementId.isVoid()) {
			throw new IllegalArgumentException("Either monitoredElementId is void or its type is invalid");
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

	private DefaultReflectogramMismatchEvent(
			final IdlReflectogramMismatchEvent reflectogramMismatchEvent) {
		this.created = new Date(reflectogramMismatchEvent.getCreated());

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
		this.resultId = Identifier.valueOf(reflectogramMismatchEvent.getResultId());
		this.monitoredElementId = Identifier.valueOf(reflectogramMismatchEvent.getMonitoredElementId());
	}

	/**
	 * @see ReflectogramMismatchEvent#getCreated()
	 */
	public Date getCreated() {
		return (Date) this.created.clone();
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(ORB)
	 */
	public IdlReflectogramMismatchEvent getTransferable(final ORB orb) {
		final IdlMismatchData mismatchData = new IdlMismatchData();
		if (this.hasMismatch()) {
			mismatchData.mismatchPair(IdlMismatch._TRUE, new IdlMismatchPair(this.getMinMismatch(), this.getMaxMismatch()));
		} else {
			mismatchData._default(IdlMismatch._FALSE);
		}

		final IdlAnchorData anchorData = new IdlAnchorData();
		if (this.hasAnchors()) {
			anchorData.anchorPair(IdlAnchor._TRUE, new IdlAnchorPair(
					this.anchor1Id.getId().getTransferable(orb),
					this.anchor2Id.getId().getTransferable(orb),
					this.getAnchor1Coord(),
					this.getAnchor2Coord()));
		} else {
			anchorData._default(IdlAnchor._FALSE);
		}

		return IdlReflectogramMismatchEventHelper.init(orb,
				this.created.getTime(),
				mismatchData,
				this.getSeverity().getTransferable(orb),
				anchorData,
				this.getCoord(),
				this.getEndCoord(),
				this.getAlarmType().getTransferable(orb),
				this.getDeltaX(),
				this.getResultId().getTransferable(orb),
				this.getMonitoredElementId().getTransferable(orb));
	}

	public static ReflectogramMismatchEvent valueOf(
			final ReflectogramMismatch reflectogramMismatch,
			final Identifier resultId,
			final Identifier monitoredElementId) {
		return new DefaultReflectogramMismatchEvent(reflectogramMismatch,
				resultId, monitoredElementId);
	}

	public static ReflectogramMismatchEvent valueOf(
			final IdlReflectogramMismatchEvent reflectogramMismatchEvent) {
		return new DefaultReflectogramMismatchEvent(reflectogramMismatchEvent);
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

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.9 $, $Date: 2005/10/22 19:07:25 $
	 * @module event
	 */
	private class SoAnchorImpl implements SOAnchor, Identifiable, Serializable {
		private static final long serialVersionUID = -3382445238828239272L;

		private Identifier anchorId;

		/**
		 * @param anchor
		 */
		private SoAnchorImpl(final SOAnchor anchor) {
			this.anchorId = Identifier.valueOf(anchor.getValue());
		}

		/**
		 * @param anchorId
		 */
		private SoAnchorImpl(final Identifier anchorId) {
			this.anchorId = anchorId;
		}

		/**
		 * @see Identifiable#getId()
		 */
		public Identifier getId() {
			return this.anchorId;
		}

		/**
		 * @see SOAnchor#getValue()
		 */
		public long getValue() {
			return this.anchorId.getIdentifierCode();
		}
	}
}
