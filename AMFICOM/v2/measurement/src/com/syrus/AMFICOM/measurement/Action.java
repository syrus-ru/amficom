package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public abstract class Action extends StorableObject {
	Identifier type_id;
	Identifier monitored_element_id;

	public Action(Identifier id) {
		super(id);
		this.type_id = null;
		this.monitored_element_id = null;
	}

	public Action(Identifier id,
								Date created,
								Date modified,
								Identifier creator_id,
								Identifier modifier_id,
								Identifier type_id,
								Identifier monitored_element_id) {
		super(id,
					created,
					modified,
					creator_id,
					modifier_id);
		this.type_id = type_id;
		this.monitored_element_id = monitored_element_id;
	}

	public Identifier getTypeId() {
		return this.type_id;
	}

	public Identifier getMonitoredElementId() {
		return this.monitored_element_id;
	}

	public abstract Result createResult(Identifier id,
																			Identifier creator_id,
																			Identifier modifier_id,
																			Measurement measurement,
																			AlarmLevel alarm_level,
																			Identifier[] parameter_ids,
																			Identifier[] parameter_type_ids,
																			byte[][] parameter_values) throws CreateObjectException;
}