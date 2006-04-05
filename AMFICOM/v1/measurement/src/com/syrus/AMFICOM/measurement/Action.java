/*
 * $Id: Action.java,v 1.43.2.11 2006/04/05 10:43:28 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlAction;
import com.syrus.AMFICOM.measurement.corba.IdlActionStatus;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.43.2.11 $, $Date: 2006/04/05 10:43:28 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public abstract class Action<R extends ActionResultParameter> extends StorableObject {

	public static enum ActionStatus {
		ACTION_STATUS_NEW,
		ACTION_STATUS_RUNNING,
		ACTION_STATUS_COMPLETED,
		ACTION_STATUS_ABORTED;

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

		@Override
		public String toString() {
			return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
		}
	}

	private Identifier typeId;
	private Identifier monitoredElementId;
	private Identifier actionTemplateId;
	private String name;
	private Date startTime;
	private long duration;
	private ActionStatus status;

	private transient String typeCodename;
	private transient LinkedIdsCondition actionResultParametersCondition;

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

	Action() {
		//Empty
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 * <p>
	 * Non-synchronized.
	 * Non-overriding.
	 * Non-overridable.
	 * </p>
	 * 
	 * @param idlAction
	 */
	final void fromIdlTransferable(final IdlAction idlAction) {
		super.fromIdlTransferable(idlAction);
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

	public final ActionTemplate getActionTemplate() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.actionTemplateId, true);
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

	public final String getTypeCodename() throws ApplicationException {
		if (this.typeCodename == null) {
			final ActionType actionType = StorableObjectPool.getStorableObject(this.typeId, true);
			this.typeCodename = actionType.getCodename();
		}
		return this.typeCodename;
	}

	public R getActionResultParameter(final ParameterType parameterType) throws ApplicationException {
		assert parameterType != null : NON_NULL_EXPECTED;

		this.ensureActionResultParametersConditionIsCreated();
		final CompoundCondition condition = new CompoundCondition(this.actionResultParametersCondition,
				AND,
				new LinkedIdsCondition(parameterType, this.getResultParameterEntityCode()));

		final Set<R> actionResultParameters = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		if (actionResultParameters.isEmpty()) {
			return null;
		}
		if (actionResultParameters.size() > 1) {
			Log.errorMessage("Action '"
					+ this.id + "' has more, than 1 ActionResultParameter of type '" + parameterType.getCodename() + "': "
					+ actionResultParameters);
		}
		return actionResultParameters.iterator().next();
	}

	public final Set<Identifier> getActionResultParameterIds() throws ApplicationException {
		this.ensureActionResultParametersConditionIsCreated();
		return StorableObjectPool.getIdentifiersByCondition(this.actionResultParametersCondition, true);
	}

	public final Set<R> getActionResultParameters() throws ApplicationException {
		this.ensureActionResultParametersConditionIsCreated();
		return StorableObjectPool.getStorableObjectsByCondition(this.actionResultParametersCondition, true);
	}

	private void ensureActionResultParametersConditionIsCreated() {
		if (this.actionResultParametersCondition == null) {
			this.actionResultParametersCondition = new LinkedIdsCondition(super.id, this.getResultParameterEntityCode());
		}
	}

	abstract short getResultParameterEntityCode();

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
				&& this.monitoredElementId != null && this.monitoredElementId.getMajor() == MONITOREDELEMENT_CODE
				&& this.actionTemplateId != null && this.actionTemplateId.getMajor() == ACTIONTEMPLATE_CODE
				&& this.startTime != null
				&& this.status != null;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.typeId);
		dependencies.add(this.monitoredElementId);
		dependencies.add(this.actionTemplateId);
		return dependencies;
	}

	public abstract R createActionResultParameter(final Identifier resultParameterCreatorId,
			final byte[] resultParameterValue,
			final Identifier resultParameterTypeId) throws CreateObjectException;
}
