/*
 * $Id: Action.java,v 1.27 2005/04/13 13:52:05 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Date;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;

/**
 * @version $Revision: 1.27 $, $Date: 2005/04/13 13:52:05 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public abstract class Action extends StorableObject implements TypedObject {
	private static final long serialVersionUID = 8504255613322384909L;

	ActionType type;
	Identifier monitoredElementId;

	Action parentAction;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @param id
	 */	
	Action(Identifier id) {
		super(id);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Action() {
		super();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Action(Identifier id,
			  Date created,
			  Date modified,
			  Identifier creatorId,
			  Identifier modifierId,
			  long version,
			  ActionType type,
			  Identifier monitoredElementId,
			  Action parentAction) {
		super(id,
				created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.monitoredElementId = monitoredElementId;

		this.parentAction = parentAction;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable, ActionType type1, Identifier monitoredElementId1, Action parentAction1)
			throws ApplicationException {
		super.fromTransferable(transferable);
		this.type = type1;
		this.monitoredElementId = monitoredElementId1;

		this.parentAction = parentAction1;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		/* XXX : fix checking parentAction w/o check id for concrete impementation as measurement or modeling
		 * which have null parent action */	
		short entityCode = this.id.getMajor();
		return super.isValid() && this.type != null && this.monitoredElementId != null && 
			(entityCode == ObjectEntities.MEASUREMENT_ENTITY_CODE || entityCode == ObjectEntities.MODELING_ENTITY_CODE || this.parentAction != null);
	}

	public StorableObjectType getType() {
		return this.type;
	}
	
	public void setType(ActionType type) {
		this.type = type;
		super.changed = true;
	}

	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}
	
	public void setMonitoredElementId(Identifier monitoredElementId) {
		this.monitoredElementId = monitoredElementId;
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  ActionType type,
											  Identifier monitoredElementId,
											  Action parentAction) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
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
