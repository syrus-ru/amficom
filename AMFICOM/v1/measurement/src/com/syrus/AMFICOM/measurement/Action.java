/*
 * $Id: Action.java,v 1.20 2005/01/27 15:54:08 arseniy Exp $
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
 * @version $Revision: 1.20 $, $Date: 2005/01/27 15:54:08 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public abstract class Action extends StorableObject implements TypedObject {
	private static final long serialVersionUID = 8504255613322384909L;

	ActionType type;
	Identifier monitoredElementId;

	Action parentAction;

	Action(Identifier id) {
		super(id);
	}

	Action(StorableObject_Transferable transferable,
				  ActionType type,
				  Identifier monitoredElementId,
				  Action parentAction) {
		super(transferable);
		this.type = type;
		this.monitoredElementId = monitoredElementId;

		this.parentAction = parentAction;
	}

	Action(Identifier id,
				  Date created,
				  Date modified,
				  Identifier creatorId,
				  Identifier modifierId,
				  ActionType type,
				  Identifier monitoredElementId,
				  Action parentAction) {
		super(id,
					created,
					modified,
					creatorId,
					modifierId);
		this.type = type;
		this.monitoredElementId = monitoredElementId;

		this.parentAction = parentAction;

		super.currentVersion = super.getNextVersion();
	}

	public StorableObjectType getType() {
		return this.type;
	}
	
	public void setType(ActionType type) {
		this.type = type;
		super.currentVersion = super.getNextVersion();
	}

	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}
	
	public void setMonitoredElementId(Identifier monitoredElementId) {
		this.monitoredElementId = monitoredElementId;
		super.currentVersion = super.getNextVersion();
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  ActionType type,
											  Identifier monitoredElementId,
											  Action parentAction) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId);
		this.type = type;
		this.monitoredElementId = monitoredElementId;

		this.parentAction = parentAction;
	}

	public abstract Result createResult(Identifier resultCreatorId, SetParameter[] parameters) throws CreateObjectException;

	/**
	 * @return Returns the parentAction.
	 */
	protected final Action getParentAction() {
		return this.parentAction;
	}
}
