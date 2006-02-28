/*-
 * $Id: ActionParameterTypeBinding.java,v 1.1.2.8 2006/02/28 10:51:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterTypeBinding;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterTypeBindingHelper;
import com.syrus.AMFICOM.measurement.corba.IdlParameterValueKind;

/**
 * @version $Revision: 1.1.2.8 $, $Date: 2006/02/28 10:51:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameterTypeBinding extends StorableObject<ActionParameterTypeBinding> {
	private static final long serialVersionUID = -8573213586761059060L;

	static enum ParameterValueKind {
		ENUMERATED,
		CONTINUOUS;

		private static final ParameterValueKind VALUES[] = values();

		IdlParameterValueKind getIdlTransferable() {
			return IdlParameterValueKind.from_int(this.ordinal());
		}

		static ParameterValueKind valueOf(final int code) {
			return VALUES[code];
		}

		static ParameterValueKind valueOf(final IdlParameterValueKind idlParameterValueKind) {
			return valueOf(idlParameterValueKind.value());
		}

		@Override
		public String toString() {
			return Integer.toString(this.ordinal());
		}
	}

	private ParameterValueKind parameterValueKind;
	private Identifier parameterTypeId;
	private Identifier actionTypeId;
	private Identifier measurementPortTypeId;

	ActionParameterTypeBinding(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final ParameterValueKind parameterValueKind,
			final Identifier parameterTypeId,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.parameterValueKind = parameterValueKind;
		this.parameterTypeId = parameterTypeId;
		this.actionTypeId = actionTypeId;
		this.measurementPortTypeId = measurementPortTypeId;
	}

	public ActionParameterTypeBinding(final IdlActionParameterTypeBinding idlActionParameterTypeBinding) throws CreateObjectException {
		try {
			this.fromTransferable(idlActionParameterTypeBinding);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public static ActionParameterTypeBinding createInstance(final Identifier creatorId,
			final ParameterValueKind parameterValueKind,
			final Identifier parameterTypeId,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId) throws CreateObjectException {
		if (creatorId == null
				|| parameterValueKind == null
				|| parameterTypeId == null
				|| actionTypeId == null
				|| measurementPortTypeId == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final ActionParameterTypeBinding actionParameterTypeBinding = new ActionParameterTypeBinding(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					parameterValueKind,
					parameterTypeId,
					actionTypeId,
					measurementPortTypeId);

			assert actionParameterTypeBinding.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			actionParameterTypeBinding.markAsChanged();

			return actionParameterTypeBinding;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlActionParameterTypeBinding getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlActionParameterTypeBindingHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.parameterValueKind.getIdlTransferable(),
				this.parameterTypeId.getIdlTransferable(orb),
				this.actionTypeId.getIdlTransferable(orb),
				this.measurementPortTypeId.getIdlTransferable(orb));
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlActionParameterTypeBinding idlActionParameterTypeBinding = (IdlActionParameterTypeBinding) transferable;
		super.fromTransferable(idlActionParameterTypeBinding);

		this.parameterValueKind = ParameterValueKind.valueOf(idlActionParameterTypeBinding.parameterValueKind);
		this.parameterTypeId = Identifier.valueOf(idlActionParameterTypeBinding.parameterTypeId);
		this.actionTypeId = Identifier.valueOf(idlActionParameterTypeBinding.actionTypeId);
		this.measurementPortTypeId = Identifier.valueOf(idlActionParameterTypeBinding.measurementPortTypeId);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
			

	public ParameterValueKind getParameterValueKind() {
		return this.parameterValueKind;
	}

	public Identifier getParameterTypeId() {
		return this.parameterTypeId;
	}

	public Identifier getActionTypeId() {
		return this.actionTypeId;
	}

	public Identifier getMeasurementPortTypeId() {
		return this.measurementPortTypeId;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final ParameterValueKind parameterValueKind,
			final Identifier parameterTypeId,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.parameterValueKind = parameterValueKind;
		this.parameterTypeId = parameterTypeId;
		this.actionTypeId = actionTypeId;
		this.measurementPortTypeId = measurementPortTypeId;
	}

	@Override
	protected boolean isValid() {
		if (this.actionTypeId == null) {
			return false;
		}

		final short actionTypeIdMajor = this.actionTypeId.getMajor();
		return super.isValid()
				&& this.parameterValueKind != null
				&& this.parameterTypeId != null && this.parameterTypeId.getMajor() == ObjectEntities.PARAMETER_TYPE_CODE
				&& (actionTypeIdMajor == ObjectEntities.MEASUREMENT_TYPE_CODE
						|| actionTypeIdMajor == ObjectEntities.ANALYSIS_TYPE_CODE
						|| actionTypeIdMajor == ObjectEntities.MODELING_TYPE_CODE)
				&& this.measurementPortTypeId != null && this.measurementPortTypeId.getMajor() == ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parameterTypeId);
		dependencies.add(this.actionTypeId);
		dependencies.add(this.measurementPortTypeId);
		return dependencies;
	}

	@Override
	protected ActionParameterTypeBindingWrapper getWrapper() {
		return ActionParameterTypeBindingWrapper.getInstance();
	}

	public static ActionParameterTypeBinding valueOf(final ParameterType parameterType,
			final ActionType actionType,
			final MeasurementPortType measurementPortType) throws ApplicationException {
		assert parameterType != null : NON_NULL_EXPECTED;
		assert actionType != null : NON_NULL_EXPECTED;
		assert measurementPortType != null : NON_NULL_EXPECTED;

		return valueOf(parameterType.getId(), actionType.getId(), measurementPortType.getId());
	}

	public static ActionParameterTypeBinding valueOf(final Identifier parameterTypeId,
			final Identifier actionTypeId,
			final Identifier measurementPortTypeId) throws ApplicationException {
		assert parameterTypeId != null : NON_NULL_EXPECTED;
		assert actionTypeId != null : NON_NULL_EXPECTED;
		assert measurementPortTypeId != null : NON_NULL_EXPECTED;

		assert parameterTypeId.getMajor() == PARAMETER_TYPE_CODE : ILLEGAL_ENTITY_CODE;
		assert actionTypeId.getMajor() == MEASUREMENT_TYPE_CODE
				|| actionTypeId.getMajor() == ANALYSIS_TYPE_CODE
				|| actionTypeId.getMajor() == MODELING_TYPE_CODE : ILLEGAL_ENTITY_CODE;
		assert measurementPortTypeId.getMajor() == MEASUREMENTPORT_TYPE_CODE : ILLEGAL_ENTITY_CODE;

		final CompoundCondition condition = new CompoundCondition(new LinkedIdsCondition(parameterTypeId, ACTIONPARAMETERTYPEBINDING_CODE),
				AND,
				new LinkedIdsCondition(actionTypeId, ACTIONPARAMETERTYPEBINDING_CODE));
		condition.addCondition(new LinkedIdsCondition(measurementPortTypeId, ACTIONPARAMETERTYPEBINDING_CODE));
		final Set<ActionParameterTypeBinding> actionParameterTypeBindings = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		if (actionParameterTypeBindings.isEmpty()) {
			throw new ObjectNotFoundException("Binding for '" + parameterTypeId + "', '" + actionTypeId + "', '" + measurementPortTypeId + "' not found");
		}
		assert actionParameterTypeBindings.size() == 1 : ONLY_ONE_EXPECTED;
		return actionParameterTypeBindings.iterator().next();
	}

}
