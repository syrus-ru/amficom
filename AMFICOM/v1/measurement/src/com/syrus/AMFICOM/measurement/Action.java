package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public abstract class Action extends StorableObject implements TypedObject {
	ActionType type;
	Identifier monitoredElementId;

	public Action(Identifier id) {
		super(id);
	}

	public Action(Identifier id,
								Date created,
								Date modified,
								Identifier creatorId,
								Identifier modifierId,
								ActionType type,
								Identifier monitoredElementId) {
		super(id,
					created,
					modified,
					creatorId,
					modifierId);
		this.type = type;
		this.monitoredElementId = monitoredElementId;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						ActionType type,
																						Identifier monitoredElementId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.type = type;
		this.monitoredElementId = monitoredElementId;
	}

	public abstract Result createResult(Identifier id,
																			Identifier creatorId,
																			Measurement measurement,
																			AlarmLevel alarmLevel,
																			SetParameter[] parameters) throws CreateObjectException;
}
