package com.syrus.AMFICOM.measurement.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.util.Log;

final class IdlActionTemplateImpl extends IdlActionTemplate {
	private static final long serialVersionUID = -6518223273175288517L;

	IdlActionTemplateImpl() {
		// empty
	}

	IdlActionTemplateImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier actionTypeId,
			final IdlIdentifier measurementPortTypeId,
			final String description,
			final long approximateActionDuration,
			final IdlIdentifier[] actionParameterIds,
			final IdlIdentifier[] monitoredElementIds) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.actionTypeId = actionTypeId;
		this.measurementPortTypeId = measurementPortTypeId;
		this.description = description;
		this.approximateActionDuration = approximateActionDuration;
		this.actionParameterIds = actionParameterIds;
		this.monitoredElementIds = monitoredElementIds;
	}

	@Override
	public StorableObject getNative() throws IdlCreateObjectException {
		try {
			return new ActionTemplate(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw coe.getIdlTransferable();
		}
	}

}
