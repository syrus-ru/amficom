/*
 * $Id: Action.java,v 1.17 2005/01/12 13:34:13 arseniy Exp $
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

/**
 * @version $Revision: 1.17 $, $Date: 2005/01/12 13:34:13 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public abstract class Action extends StorableObject implements TypedObject {
	private static final long serialVersionUID = 8504255613322384909L;

	ActionType type;
	Identifier monitoredElementId;

	Action(Identifier id) {
		super(id);
	}

	Action(StorableObject_Transferable transferable,
				  ActionType type,
				  Identifier monitoredElementId) {
		super(transferable);
		this.type = type;
		this.monitoredElementId = monitoredElementId;
	}
	
	Action(Identifier id,
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

		super.currentVersion = super.getNextVersion();
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

	public abstract Result createResult(Identifier creatorId,
										Measurement measurement,
										SetParameter[] parameters) throws CreateObjectException;
	
	public abstract Result createResult(Identifier creatorId,
										SetParameter[] parameters) throws CreateObjectException;

}
