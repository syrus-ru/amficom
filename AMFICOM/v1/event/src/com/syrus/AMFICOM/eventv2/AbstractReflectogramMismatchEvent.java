/*-
 * $Id: AbstractReflectogramMismatchEvent.java,v 1.10 2006/06/10 19:24:05 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.REFLECTORGAM_MISMATCH;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.reflectometry.SOAnchor;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2006/06/10 19:24:05 $
 * @module event
 */
public abstract class AbstractReflectogramMismatchEvent extends StorableObject
		implements ReflectogramMismatchEvent {
	AbstractReflectogramMismatchEvent(/*IdlReflectogramMismatchEvent*/) {
		// super();
	}

	AbstractReflectogramMismatchEvent(final Identifier id,
			final Identifier creatorId,
			final Date created,
			final StorableObjectVersion version) {
		super(id, created, created, creatorId, creatorId, version);
	}

	public final EventType getType() {
		return REFLECTORGAM_MISMATCH;
	}

	/**
	 * Compares the two reflectogram mismatch events simply by their created
	 * dates.
	 *
	 * @param that
	 * @see Comparable#compareTo(Object)
	 */
	public final int compareTo(final ReflectogramMismatchEvent that) {
		final long thisCreated = this.getCreated().getTime();
		final long thatCreated = that.getCreated().getTime();

		return thisCreated <= thatCreated ? thisCreated < thatCreated ? -1 : 0 : 1;
	}

	protected String paramString() {
		final double deltaX = this.getDeltaX();
		final int coord = this.getCoord();
		return "alarmType = " + this.getAlarmType()
				+ "; severity = " + this.getSeverity()
				+ "; measurementId = " + this.getMeasurementId()
				+ "; deltaX = " + deltaX
				+ "; coord = " + coord
				+ "; distance = " + this.getDistance() + " = " + deltaX + " * " + coord;
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.StorableObject#getIdlTransferable(ORB)
	 */
	@Override
	public final IdlReflectogramMismatchEvent getIdlTransferable(final ORB orb) {
		final IdlMismatchData mismatchData = new IdlMismatchData();
		if (this.hasMismatch()) {
			mismatchData.mismatchPair(IdlMismatch._TRUE, new IdlMismatchPair(this.getMinMismatch(), this.getMaxMismatch()));
		} else {
			mismatchData._default(IdlMismatch._FALSE);
		}

		final IdlAnchorData anchorData = new IdlAnchorData();
		if (this.hasAnchors()) {
			anchorData.anchorPair(IdlAnchor._TRUE, new IdlAnchorPair(
					Identifier.valueOf(this.getAnchor1Id().getValue()).getIdlTransferable(orb),
					Identifier.valueOf(this.getAnchor2Id().getValue()).getIdlTransferable(orb),
					this.getAnchor1Coord(),
					this.getAnchor2Coord()));
		} else {
			anchorData._default(IdlAnchor._FALSE);
		}

		return IdlReflectogramMismatchEventHelper.init(orb,
				this.id.getIdlTransferable(orb),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(orb),
				this.modifierId.getIdlTransferable(orb),
				this.version.longValue(),
				mismatchData,
				this.getSeverity().getIdlTransferable(orb),
				anchorData,
				this.getCoord(),
				this.getEndCoord(),
				this.getAlarmType().getIdlTransferable(orb),
				this.getDeltaX(),
				this.getMeasurementId().getIdlTransferable(orb));
	}

	@Override
	protected final Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		if (this.hasAnchors()) {
			dependencies.add(Identifier.valueOf(this.getAnchor1Id().getValue()));
			dependencies.add(Identifier.valueOf(this.getAnchor2Id().getValue()));
		}
		dependencies.add(this.getMeasurementId());
		return dependencies;
	}

	@Override
	protected final AbstractReflectogramMismatchEventWrapper getWrapper() {
		return AbstractReflectogramMismatchEventWrapper.getInstance();
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.10 $, $Date: 2006/06/10 19:24:05 $
	 * @module event
	 */
	final class SoAnchorImpl implements SOAnchor, Identifiable, Serializable {
		private static final long serialVersionUID = -3382445238828239272L;

		private Identifier anchorId;

		/**
		 * @param anchor
		 */
		SoAnchorImpl(final SOAnchor anchor) {
			this.anchorId = Identifier.valueOf(anchor.getValue());
		}

		/**
		 * @param anchorId
		 */
		SoAnchorImpl(final Identifier anchorId) {
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
