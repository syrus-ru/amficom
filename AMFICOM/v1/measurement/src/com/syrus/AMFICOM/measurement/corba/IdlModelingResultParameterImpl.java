package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.ModelingResultParameter;
import com.syrus.util.Log;

final class IdlModelingResultParameterImpl extends IdlModelingResultParameter {

	IdlModelingResultParameterImpl() {
		// empty
	}

	IdlModelingResultParameterImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier parameterTypeId,
			final IdlIdentifier analysisId,
			final byte[] value) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.parameterTypeId = parameterTypeId;
		this.actionId = analysisId;
		this.value = value;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new ModelingResultParameter(this);
		} catch (CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}

}
