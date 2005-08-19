/*-
 * $Id: IdlAnalysisTypeImpl.java,v 1.4 2005/08/19 14:19:04 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.IdlParameterTypeEnum;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/19 14:19:04 $
 * @module measurement
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
			final IdlParameterTypeEnum inParameterTypes[],
			final IdlParameterTypeEnum criteriaParameterTypes[],
			final IdlParameterTypeEnum etalonParameterTypes[],
			final IdlParameterTypeEnum outParameterTypes[],
			final IdlIdentifier measurementTypeIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.description = description;
		this.inParameterTypes = inParameterTypes;
		this.criteriaParameterTypes = criteriaParameterTypes;
		this.etalonParameterTypes = etalonParameterTypes;
		this.outParameterTypes = outParameterTypes;
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
