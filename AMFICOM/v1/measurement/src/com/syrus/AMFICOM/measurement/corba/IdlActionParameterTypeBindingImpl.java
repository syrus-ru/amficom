package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding;
import com.syrus.util.Log;

final class IdlActionParameterTypeBindingImpl extends IdlActionParameterTypeBinding {
	private static final long serialVersionUID = 208234089237198593L;

	IdlActionParameterTypeBindingImpl() {
		// empty
	}

	IdlActionParameterTypeBindingImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlParameterValueKind parameterValueKind,
			final IdlIdentifier parameterTypeId,
			final IdlIdentifier actionTypeId,
			final IdlIdentifier measurementPortTypeId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.parameterValueKind = parameterValueKind;
		this.parameterTypeId = parameterTypeId;
		this.actionTypeId = actionTypeId;
		this.measurementPortTypeId = measurementPortTypeId;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new ActionParameterTypeBinding(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}

}
