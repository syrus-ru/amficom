/*
 * $Id: Action.java,v 1.46 2006/03/15 14:47:30 bass Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import java.util.Date;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.46 $, $Date: 2006/03/15 14:47:30 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public abstract class Action extends StorableObject {
	private static final long serialVersionUID = 8504255613322384909L;

	ActionType type;
	Identifier monitoredElementId;

	Identifier parentActionId;

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
			final Identifier parentActionId) {
		super(id,
				created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.monitoredElementId = monitoredElementId;

		this.parentActionId = parentActionId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 *
	 * <p>
	 * Non-synchronized.
	 * Non-overriding.
	 * Non-overridable.
	 * </p>
	 *
	 * @param transferable
	 * @param type1
	 * @param monitoredElementId1
	 * @param parentActionId1
	 */
	final void fromIdlTransferable(final IdlStorableObject transferable,
			final ActionType type1,
			final Identifier monitoredElementId1,
			final Identifier parentActionId1) {
		super.fromIdlTransferable(transferable);
		this.type = type1;
		this.monitoredElementId = monitoredElementId1;

		this.parentActionId = parentActionId1;
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
				&& this.parentActionId != null
				&& (entityCode == ObjectEntities.MEASUREMENT_CODE || entityCode == ObjectEntities.MODELING_CODE || this.parentActionId != VOID_IDENTIFIER);
	}

	public ActionType<?> getType() {
		return this.type;
	}

	public final Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}

	/**
	 * Returns the {@link MonitoredElement} associated with this
	 * {@link Action}. The {@link MonitoredElement} is guaranteed to exist.
	 *
	 * @return the {@link MonitoredElement} associated with this
	 *         {@link Action}.
	 * @throws ApplicationException
	 */
	public final MonitoredElement getMonitoredElement() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getMonitoredElementId(), true);
	}

	public final void setMonitoredElementId(final Identifier monitoredElementId) {
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
			final Identifier parentActionId) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.type = type;
		this.monitoredElementId = monitoredElementId;

		this.parentActionId = parentActionId;
	}

	public abstract Result createResult(final Identifier resultCreatorId, final Parameter[] parameters)
			throws CreateObjectException;

	/**
	 * @return Returns the parentAction.
	 */
	protected final Action getParentAction() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getParentActionId(), true);
	}

	protected final Identifier getParentActionId() {
		return this.parentActionId;
	}
}
