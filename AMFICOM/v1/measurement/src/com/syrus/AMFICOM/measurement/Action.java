/*
 * $Id: Action.java,v 1.12 2004/11/12 11:44:48 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

/**
 * @version $Revision: 1.12 $, $Date: 2004/11/12 11:44:48 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public abstract class Action extends StorableObject implements TypedObject {
	ActionType type;
	Identifier monitoredElementId;

	public Action(Identifier id) {
		super(id);
	}

	public Action(StorableObject_Transferable transferable,
				  ActionType type,
				  Identifier monitoredElementId) {
		super(transferable);
		this.type = type;
		this.monitoredElementId = monitoredElementId;
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
	
	public abstract Result createResult(Identifier id,
										Identifier creatorId,
										SetParameter[] parameters) throws CreateObjectException;

}
