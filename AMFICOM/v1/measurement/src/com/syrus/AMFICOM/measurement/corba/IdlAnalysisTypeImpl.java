/*-
 * $Id: IdlAnalysisTypeImpl.java,v 1.2 2005/07/11 08:20:01 bass Exp $
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
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 08:20:01 $
 * @module measurement_v1
 */
final class IdlAnalysisTypeImpl extends IdlAnalysisType {
	private static final long serialVersionUID = 8482119719547358951L;

	IdlAnalysisTypeImpl() {
		// empty
	}

	IdlAnalysisTypeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final String description,
			final IdlIdentifier inParameterTypeIds[],
			final IdlIdentifier criteriaParameterTypeIds[],
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
		this.criteriaParameterTypeIds = criteriaParameterTypeIds;
		this.etalonParameterTypeIds = etalonParameterTypeIds;
		this.outParameterTypeIds = outParameterTypeIds;
		this.measurementTypeIds = measurementTypeIds;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AnalysisType getNative() throws IdlCreateObjectException {
		try {
			return new AnalysisType(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
