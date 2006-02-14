/*
 * $Id: Action.java,v 1.43.2.3 2006/02/14 00:43:51 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlAction;
import com.syrus.AMFICOM.measurement.corba.IdlActionStatus;

/**
 * @version $Revision: 1.43.2.3 $, $Date: 2006/02/14 00:43:51 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public abstract class Action<T extends Action<T>> extends StorableObject<T> {

	static enum ActionStatus {
		ACTION_STATUS_NEW,
		ACTION_STATUS_RUNNING,
		ACTION_STATUS_COMPLETE;

		private static final ActionStatus[] VALUES = values();

		IdlActionStatus getIdlTransferable() {
			return IdlActionStatus.from_int(this.ordinal());
		}

		static ActionStatus valueOf(final int code) {
			return VALUES[code];
		}

		static ActionStatus valueOf(final IdlActionStatus idlActionStatus) {
			return valueOf(idlActionStatus.value());
		}
	}

	private Identifier typeId;
	private Identifier monitoredElementId;
	private Identifier actionTemplateId;
	private String name;
	private Date startTime;
	private long duration;
	private ActionStatus status;

	Action(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final ActionStatus status) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.typeId = typeId;
		this.monitoredElementId = monitoredElementId;
		this.actionTemplateId = actionTemplateId;
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
	}

	Action(final IdlStorableObject idlStorableObject) throws CreateObjectException {
		try {
			this.fromTransferable(idlStorableObject);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	void fromTransferable(final IdlAction idlAction) throws ApplicationException {
		super.fromTransferable(idlAction);
		this.typeId = Identifier.valueOf(idlAction._typeId);
		this.monitoredElementId = Identifier.valueOf(idlAction.monitoredElementId);
		this.actionTemplateId = Identifier.valueOf(idlAction.actionTemplateId);
		this.name = idlAction.name;
		this.startTime = new Date(idlAction.startTime);
		this.duration = idlAction.duration;
		this.status = ActionStatus.valueOf(idlAction.status);
	}

	public final Identifier getTypeId() {
		return this.typeId;
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

	public final Identifier getActionTemplateId() {
		return this.actionTemplateId;
	}

	public final String getName() {
		return this.name;
	}

	public final Date getStartTime() {
		return this.startTime;
	}

	public final long getDuration() {
		return this.duration;
	}

	public final ActionStatus getStatus() {
		return this.status;
	}

	public final void setStatus(final ActionStatus status) {
		this.status = status;
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
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final ActionStatus status) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.typeId = typeId;
		this.monitoredElementId = monitoredElementId;
		this.actionTemplateId = actionTemplateId;
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.typeId != null
				&& this.monitoredElementId != null && this.monitoredElementId.getMajor() == ObjectEntities.MONITOREDELEMENT_CODE
				&& this.actionTemplateId != null && this.actionTemplateId.getMajor() == ObjectEntities.ACTIONTEMPLATE_CODE
				&& this.startTime != null
				&& this.status != null;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.typeId);
		dependencies.add(this.monitoredElementId);
		dependencies.add(this.actionTemplateId);
		return dependencies;
	}
}
