/*-
 * $Id: AbstractLineMismatchEvent.java,v 1.6 2006/05/18 19:37:22 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialData;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialDataPackage.IdlAffectedPathElementSpacious;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialDataPackage.IdlPhysicalDistancePair;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2006/05/18 19:37:22 $
 * @module event
 */
public abstract class AbstractLineMismatchEvent extends StorableObject
		implements LineMismatchEvent {
	AbstractLineMismatchEvent(/*IdlLineMismatchEvent*/) {
		// super();
	}

	AbstractLineMismatchEvent(final Identifier id,
			final Identifier creatorId,
			final Date created,
			final StorableObjectVersion version) {
		super(id, created, created, creatorId, creatorId, version);
	}

	public final EventType getType() {
		return LINE_MISMATCH;
	}

	protected String paramString() {
		return "reflectogramMismatchEventId = " + this.getReflectogramMismatchEventId();
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	@Override
	public final IdlLineMismatchEvent getIdlTransferable(final ORB orb) {
		final IdlSpacialData spacialData = new IdlSpacialData();
		if (this.isAffectedPathElementSpacious()) {
			spacialData.physicalDistancePair(
					IdlAffectedPathElementSpacious._TRUE,
					new IdlPhysicalDistancePair(
							this.getPhysicalDistanceToStart(),
							this.getPhysicalDistanceToEnd()));
		} else {
			spacialData._default(IdlAffectedPathElementSpacious._FALSE);
		}

		return IdlLineMismatchEventHelper.init(orb,
				this.id.getIdlTransferable(orb),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(orb),
				this.modifierId.getIdlTransferable(orb),
				this.version.longValue(),
				this.getAffectedPathElementId().getIdlTransferable(orb),
				spacialData,
				this.getMismatchOpticalDistance(),
				this.getMismatchPhysicalDistance(),
				this.getPlainTextMessage(),
				this.getRichTextMessage(),
				this.getReflectogramMismatchEventId().getIdlTransferable(orb));
	}

	@Override
	protected final Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.getAffectedPathElementId());
		dependencies.add(this.getReflectogramMismatchEventId());
		return dependencies;
	}

	@Override
	protected final AbstractLineMismatchEventWrapper getWrapper() {
		return AbstractLineMismatchEventWrapper.getInstance();
	}
}
