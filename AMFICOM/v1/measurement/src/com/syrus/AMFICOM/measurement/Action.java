package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public abstract class Action extends StorableObject implements TypedObject {
	Identifier typeId;
	Identifier monitoredElementId;

	public Action(Identifier id) {
		super(id);
		this.typeId = null;
		this.monitoredElementId = null;
	}

	public Action(Identifier id,
								Date created,
								Date modified,
								Identifier creatorId,
								Identifier modifierId,
								Identifier typeId,
								Identifier monitoredElementId) {
		super(id,
					created,
					modified,
					creatorId,
					modifierId);
		this.typeId = typeId;
		this.monitoredElementId = monitoredElementId;
	}

	public Identifier getTypeId() {
		return this.typeId;
	}

	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier typeId,
																						Identifier monitoredElementId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.typeId = typeId;
		this.monitoredElementId = monitoredElementId;
	}

	public abstract Result createResult(Identifier id,
																			Identifier creatorId,
																			Measurement measurement,
																			AlarmLevel alarmLevel,
																			Identifier[] parameterIds,
																			Identifier[] parameterTypeIds,
																			byte[][] parameterValues) throws CreateObjectException;
}