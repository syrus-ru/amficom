/*-
 * $Id: IdlEvaluationImpl.java,v 1.2 2005/07/11 08:20:00 bass Exp $
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
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 08:20:00 $
 * @module measurement_v1
 */
final class IdlEvaluationImpl extends IdlEvaluation {
	private static final long serialVersionUID = 1681928700060333L;

	IdlEvaluationImpl() {
		// empty
	}

	IdlEvaluationImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier typeId,
			final IdlIdentifier monitoredElementId,
			final IdlIdentifier measurementId,
			final IdlIdentifier thresholdSetId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this._typeId = typeId;
		this.monitoredElementId = monitoredElementId;
		this.measurementId = measurementId;
		this.thresholdSetId = thresholdSetId;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Evaluation getNative() throws IdlCreateObjectException {
		try {
			return new Evaluation(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
