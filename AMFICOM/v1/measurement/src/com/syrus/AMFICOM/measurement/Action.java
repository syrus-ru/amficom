package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public abstract class Action extends StorableObject implements TypedObject {
	Identifier typeId;
	Identifier monitoredElementId;
	
	String codename;

	public Action(Identifier id) {
		super(id);
	}

	public Action(Identifier id,
								Date created,
								Date modified,
								Identifier creatorId,
								Identifier modifierId,
								Identifier typeId,
								Identifier monitoredElementId,
								String codename) {
		super(id,
					created,
					modified,
					creatorId,
					modifierId);
		this.typeId = typeId;
		this.monitoredElementId = monitoredElementId;
		
		this.codename = codename;
	}

	public Identifier getTypeId() {
		return this.typeId;
	}

	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}
	
	public String getCodename() {
		return this.codename;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier typeId,
																						Identifier monitoredElementId,
																						String codename) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.typeId = typeId;
		this.monitoredElementId = monitoredElementId;
		this.codename = codename;
	}

	public abstract Result createResult(Identifier id,
																			Identifier creatorId,
																			Measurement measurement,
																			AlarmLevel alarmLevel,
																			SetParameter[] parameters) throws CreateObjectException;
}
