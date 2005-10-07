/*-
 * $Id: DefaultReflectogramMismatchEvent.java,v 1.1 2005/10/07 14:58:57 bass Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorData;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlMismatchData;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorDataPackage.IdlAnchor;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlAnchorDataPackage.IdlAnchorPair;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlMismatchDataPackage.IdlMismatch;
import com.syrus.AMFICOM.eventv2.corba.IdlReflectogramMismatchEventPackage.IdlMismatchDataPackage.IdlMismatchPair;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.AMFICOM.reflectometry.SOAnchor;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/07 14:58:57 $
 * @module event
 */
public final class DefaultReflectogramMismatchEvent extends
		AbstractReflectogramMismatchEvent {
	private static final long serialVersionUID = -1232479623372509377L;

	private boolean mismatch;

	private double minMismatch;

	private double maxMismatch;

	private Severity severity;

	private double distance;

	private boolean anchors;

	private SoAnchorImpl anchor1Id;

	private SoAnchorImpl anchor2Id;

	private int anchor1Coord;

	private int anchor2Coord;

	private int coord;

	private int endCoord;

	private AlarmType alarmType;

	private double deltaX;

	private DefaultReflectogramMismatchEvent(
			final ReflectogramMismatch reflectogramMismatch) {
		/*
		 * Бляццкий иклипс ругаецца
		 */
/*
		if (this.mismatch = reflectogramMismatch.hasMismatch())
/*/
		this.mismatch = reflectogramMismatch.hasMismatch();
		if (this.mismatch)
//*/
		{
			this.minMismatch = reflectogramMismatch.getMinMismatch();
			this.maxMismatch = reflectogramMismatch.getMaxMismatch();

			if (this.minMismatch > this.maxMismatch) {
				throw new IllegalArgumentException();
			}
		}

		this.severity = reflectogramMismatch.getSeverity();
		this.distance = reflectogramMismatch.getDistance();

		/*
		 * Сдесь бляццкий иклипс ругаецца исчо
		 */
/*
		if (this.anchors = reflectogramMismatch.hasAnchors())
/*/
		this.anchors = reflectogramMismatch.hasAnchors();
		if (this.anchors)
//*/
		{
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
	}

	private DefaultReflectogramMismatchEvent(
			final IdlReflectogramMismatchEvent reflectogramMismatchEvent) {
		this.mismatch = reflectogramMismatchEvent.hasMismatch();
		if (this.mismatch) {
			this.minMismatch = reflectogramMismatchEvent.getMinMismatch();
			this.maxMismatch = reflectogramMismatchEvent.getMaxMismatch();

			if (this.minMismatch > this.maxMismatch) {
				throw new IllegalArgumentException();
			}
		}

		this.severity = Severity.valueOf(reflectogramMismatchEvent.getSeverity());
		this.distance = reflectogramMismatchEvent.getDistance();

		this.anchors = reflectogramMismatchEvent.hasAnchors();
		if (this.anchors) {
			this.anchor1Id = new SoAnchorImpl(Identifier.valueOf(reflectogramMismatchEvent.getAnchor1Id()));
			this.anchor2Id = new SoAnchorImpl(Identifier.valueOf(reflectogramMismatchEvent.getAnchor2Id()));
			this.anchor1Coord = reflectogramMismatchEvent.getAnchor1Coord();
			this.anchor2Coord = reflectogramMismatchEvent.getAnchor2Coord();
		}

		this.coord = reflectogramMismatchEvent.getCoord();
		this.endCoord = reflectogramMismatchEvent.getEndCoord();
		this.alarmType = AlarmType.valueOf(reflectogramMismatchEvent.getAlarmType());
		this.deltaX = reflectogramMismatchEvent.getDeltaX();
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
				mismatchData,
				this.getSeverity().getTransferable(orb),
				this.getDistance(),
				anchorData,
				this.getCoord(),
				this.getEndCoord(),
				this.getAlarmType().getTransferable(orb),
				this.getDeltaX());
	}

	public static ReflectogramMismatchEvent valueOf(
			final ReflectogramMismatch reflectogramMismatch) {
		return new DefaultReflectogramMismatchEvent(reflectogramMismatch);
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
		return this.distance;
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
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.1 $, $Date: 2005/10/07 14:58:57 $
	 * @module event
	 */
	private class SoAnchorImpl implements SOAnchor, Identifiable {
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
