package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.ModelingResultParameter;
import com.syrus.util.Log;

final class IdlModelingResultParameterImpl extends IdlModelingResultParameter {
	private static final long serialVersionUID = -1034747719852426086L;

	IdlModelingResultParameterImpl() {
		// empty
	}

	IdlModelingResultParameterImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final byte[] value,
			final IdlIdentifier _typeId,
			final IdlIdentifier analysisId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.value = value;
		this._typeId = _typeId;
		this.actionId = analysisId;
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
