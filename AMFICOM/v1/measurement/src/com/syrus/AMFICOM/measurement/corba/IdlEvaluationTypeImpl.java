/*-
 * $Id: IdlEvaluationTypeImpl.java,v 1.3 2005/08/08 11:31:46 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:31:46 $
 * @module measurement
 */
final class IdlEvaluationTypeImpl extends IdlEvaluationType {
	private static final long serialVersionUID = 6198456599726646530L;

	IdlEvaluationTypeImpl() {
		// empty
	}

	IdlEvaluationTypeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final String description,
			final IdlIdentifier inParameterTypeIds[],
			final IdlIdentifier thresholdParameterTypeIds[],
			final IdlIdentifier etalonParameterTypeIds[],
			final IdlIdentifier outParameterTypeIds[],
			final IdlIdentifier measurementTypeIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.description = description;
		this.inParameterTypeIds = inParameterTypeIds;
		this.thresholdParameterTypeIds = thresholdParameterTypeIds;
		this.etalonParameterTypeIds = etalonParameterTypeIds;
		this.outParameterTypeIds = outParameterTypeIds;
		this.measurementTypeIds = measurementTypeIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public EvaluationType getNative() throws IdlCreateObjectException {
		try {
			return new EvaluationType(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
