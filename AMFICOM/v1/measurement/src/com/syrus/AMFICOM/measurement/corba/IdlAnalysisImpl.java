/*-
 * $Id: IdlAnalysisImpl.java,v 1.8 2006/03/14 10:47:56 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2006/03/14 10:47:56 $
 * @module measurement
 */
final class IdlAnalysisImpl extends IdlAnalysis {
	private static final long serialVersionUID = -3173456978141791125L;

	IdlAnalysisImpl() {
		// empty
	}

	IdlAnalysisImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlAnalysisType type,
			final IdlIdentifier monitoredElementId,
			final IdlIdentifier measurementId,
			final String name,
			final IdlIdentifier criteriaSetId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.type = type;
		this.monitoredElementId = monitoredElementId;
		this.measurementId = measurementId;
		this.name = name;
		this.criteriaSetId = criteriaSetId;
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
