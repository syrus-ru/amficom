package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.MeasurementResultParameter;
import com.syrus.util.Log;

final class IdlMeasurementResultParameterImpl extends IdlMeasurementResultParameter {

	IdlMeasurementResultParameterImpl() {
		// empty
	}

	IdlMeasurementResultParameterImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final byte[] value,
			final IdlIdentifier _typeId,
			final IdlIdentifier measurementId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.value = value;
		this._typeId = _typeId;
		this.actionId = measurementId;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new MeasurementResultParameter(this);
		} catch (CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}

}
