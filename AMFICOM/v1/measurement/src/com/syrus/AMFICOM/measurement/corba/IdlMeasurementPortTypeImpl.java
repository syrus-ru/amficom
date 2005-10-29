/*-
 * $Id: IdlMeasurementPortTypeImpl.java,v 1.4 2005/10/29 17:00:52 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPortType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/10/29 17:00:52 $
 * @module measurement
 */
final class IdlMeasurementPortTypeImpl extends IdlMeasurementPortType {
	private static final long serialVersionUID = 68906032588833347L;

	IdlMeasurementPortTypeImpl() {
		// empty
	}

	IdlMeasurementPortTypeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final IdlMeasurementType[] measurementTypes,
			final IdlParameterType[] parameterTypes) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.codename = codename;
		this.description = description;
		this.name = name;
		this.measurementTypes = measurementTypes;
		this.parameterTypes = parameterTypes;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public MeasurementPortType getNative() throws IdlCreateObjectException {
		try {
			return new MeasurementPortType(this);
		} catch (final CreateObjectException coe) {
			Log.debugException(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}
