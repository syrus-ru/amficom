/*-
 * $Id: IdlMeasurementSetupImpl.java,v 1.6 2005/10/30 15:20:39 bass Exp $
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
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/10/30 15:20:39 $
 * @module measurement
 */
final class IdlMeasurementSetupImpl extends IdlMeasurementSetup {
	private static final long serialVersionUID = -6499686203350494198L;

	IdlMeasurementSetupImpl() {
		// empty
	}

	IdlMeasurementSetupImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier parameterSetId,
			final IdlIdentifier criteriaSetId,
			final IdlIdentifier thresholdSetId,
			final IdlIdentifier etalonId,
			final String description,
			final long measurementDuration,
			final IdlIdentifier[] monitoredElementIds,
			final IdlMeasurementType[] measurementTypes) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.parameterSetId = parameterSetId;
		this.criteriaSetId = criteriaSetId;
		this.thresholdSetId = thresholdSetId;
		this.etalonId = etalonId;
		this.description = description;
		this.measurementDuration = measurementDuration;
		this.monitoredElementIds = monitoredElementIds;
		this.measurementTypes = measurementTypes;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public MeasurementSetup getNative() throws IdlCreateObjectException {
		try {
			return new MeasurementSetup(this);
		} catch (final CreateObjectException coe) {
			assert Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
