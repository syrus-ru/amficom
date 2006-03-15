/*-
 * $Id: IdlAnalysisImpl.java,v 1.7.2.4 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.7.2.4 $, $Date: 2006/03/15 15:50:02 $
 * @module measurement
 */
final class IdlAnalysisImpl extends IdlAnalysis {
	private static final long serialVersionUID = -8448761881228596739L;

	IdlAnalysisImpl() {
		// empty
	}

	IdlAnalysisImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier _typeId,
			final IdlIdentifier monitoredElementId,
			final IdlIdentifier actionTemplateId,
			final String name,
			final long startTime,
			final long duration,
			final IdlActionStatus status,
			final IdlIdentifier measurementId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this._typeId = _typeId;
		this.monitoredElementId = monitoredElementId;
		this.actionTemplateId = actionTemplateId;
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
		this.measurementId = measurementId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Analysis getNative() throws IdlCreateObjectException {
		try {
			return new Analysis(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}
}
