/*
 * $Id: Action.java,v 1.38 2005/09/08 18:26:30 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Date;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.38 $, $Date: 2005/09/08 18:26:30 $
 * @author $Author: bass $
 * @module measurement
 */

public abstract class Action extends StorableObject {
	private static final long serialVersionUID = 8504255613322384909L;

	ActionType type;
	Identifier monitoredElementId;

	Action parentAction;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Action() {
		// super();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Action(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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
	protected synchronized void fromTransferable(final IdlStorableObject transferable,
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
	@Override
	protected boolean isValid() {
		/* XXX : fix checking parentAction w/o check id for concrete impementation as measurement or modeling
		 * which have null parent action */	
		short entityCode = this.id.getMajor();
		return super.isValid()
				&& this.type != null
				&& this.monitoredElementId != null
				&& (entityCode == ObjectEntities.MEASUREMENT_CODE || entityCode == ObjectEntities.MODELING_CODE || this.parentAction != null);
	}

	public ActionType getType() {
		return this.type;
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
			final StorableObjectVersion version,
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
