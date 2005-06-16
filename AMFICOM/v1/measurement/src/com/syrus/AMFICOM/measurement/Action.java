/*
 * $Id: Action.java,v 1.30 2005/06/16 10:34:03 bass Exp $
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
 * @version $Revision: 1.30 $, $Date: 2005/06/16 10:34:03 $
 * @author $Author: bass $
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
	Action(final Identifier id) {
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
	Action(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final ActionType type,
			final Identifier monitoredElementId,
			final Action parentAction) {
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
	protected void fromTransferable(final IDLEntity transferable,
			final ActionType type1,
			final Identifier monitoredElementId1,
			final Action parentAction1) throws ApplicationException {
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
		return super.isValid()
				&& this.type != null
				&& this.monitoredElementId != null
				&& (entityCode == ObjectEntities.MEASUREMENT_ENTITY_CODE || entityCode == ObjectEntities.MODELING_ENTITY_CODE || this.parentAction != null);
	}

	public StorableObjectType getType() {
		return this.type;
	}
	
	public void setType(final ActionType type) {
		this.type = type;
		super.markAsChanged();
	}

	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}
	
	public void setMonitoredElementId(final Identifier monitoredElementId) {
		this.monitoredElementId = monitoredElementId;
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final ActionType type,
			final Identifier monitoredElementId,
			final Action parentAction) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.type = type;
		this.monitoredElementId = monitoredElementId;

		this.parentAction = parentAction;
	}

	public abstract Result createResult(final Identifier resultCreatorId, final Parameter[] parameters)
			throws CreateObjectException;

	/**
	 * @return Returns the parentAction.
	 */
	protected final Action getParentAction() {
		return this.parentAction;
	}
}
