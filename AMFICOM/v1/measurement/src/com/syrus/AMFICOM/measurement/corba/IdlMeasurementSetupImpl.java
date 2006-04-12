package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.util.Log;

final class IdlMeasurementSetupImpl extends IdlMeasurementSetup {
	private static final long serialVersionUID = 77221861881120876L;

	IdlMeasurementSetupImpl() {
		// empty
	}

	IdlMeasurementSetupImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier measurementPortTypeId,
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
		this.measurementPortTypeId = measurementPortTypeId;
		this.measurementTemplateId = measurementTemplateId;
		this.analysisTemplateId = analysisTemplateId;
		this.description = description;
		this.monitoredElementIds = monitoredElementIds;
	}

	@Override
	public MeasurementSetup getNative() throws IdlCreateObjectException {
		try {
			return new MeasurementSetup(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}

}
