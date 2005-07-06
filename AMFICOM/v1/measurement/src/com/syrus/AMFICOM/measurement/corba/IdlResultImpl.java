/*-
 * $Id: IdlResultImpl.java,v 1.1 2005/07/06 19:10:54 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement.corba;

import static com.syrus.util.Log.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/06 19:10:54 $
 * @module measurement_v1
 */
final class IdlResultImpl extends IdlResult {
	private static final long serialVersionUID = -6011384199761834881L;

	IdlResultImpl() {
		// empty
	}

	IdlResultImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier measurementId,
			final IdlIdentifier analysisId,
			final IdlIdentifier evaluationId,
			final IdlIdentifier modelingId,
			final ResultSort sort,
			final IdlParameter parameters[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.measurementId = measurementId;
		this.analysisId = analysisId;
		this.evaluationId = evaluationId;
		this.modelingId = modelingId;
		this.sort = sort;
		this.parameters = parameters;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public Result getNative() throws IdlCreateObjectException {
		try {
			return new Result(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
