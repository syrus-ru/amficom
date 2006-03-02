package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.util.Log;

final class IdlMeasurementSetupImpl extends IdlMeasurementSetup {
	private static final long serialVersionUID = 6803393489149894831L;

	IdlMeasurementSetupImpl() {
		// empty
	}

	IdlMeasurementSetupImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier measurementTemplateId,
			final IdlIdentifier analysisTemplateId,
			final String description,
			final IdlIdentifier[] monitoredElementIds) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.description = description;
		this.measurementTemplateId = measurementTemplateId;
		this.analysisTemplateId = analysisTemplateId;
		this.monitoredElementIds = monitoredElementIds;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new MeasurementSetup(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}

}
