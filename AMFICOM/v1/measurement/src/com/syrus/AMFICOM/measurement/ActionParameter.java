/*-
 * $Id: ActionParameter.java,v 1.1.2.9 2006/03/01 11:42:42 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameter;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterHelper;

/**
 * @version $Revision: 1.1.2.9 $, $Date: 2006/03/01 11:42:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameter extends Parameter<ActionParameter> {
	private static final long serialVersionUID = 3238162272695974644L;

	private Identifier bindingId;

	/*	Cached fields*/
	private ParameterValueKind valueKind;
	private Identifier typeId;
	private String typeCodename;

	ActionParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final byte[] value,
			final Identifier bindingId) {
		super(id, creatorId, version, value);
		this.bindingId = bindingId;
	}

	public ActionParameter(final IdlActionParameter idlActionParameter) throws CreateObjectException {
		super(idlActionParameter);
	}

	/**
	 * Create new instance.
	 * This method always check, if parameter value kind
	 * for the binding supplied is not ENUMERATED.
	 * 
	 * @param creatorId
	 * @param value
	 * @param bindingId
	 * @return New instance.
	 * @throws CreateObjectException
	 */
	public static ActionParameter createInstance(final Identifier creatorId, final byte[] value, final Identifier bindingId)
			throws CreateObjectException {
		return createInstance(creatorId, value, bindingId, true);
	}

	/**
	 * Create new instance.
	 * 
	 * @param creatorId
	 * @param value
	 * @param bindingId
	 * @param checkValueKind
	 * @return New instance.
	 * @throws CreateObjectException
	 */
	public static ActionParameter createInstance(final Identifier creatorId,
			final byte[] value,
			final Identifier bindingId,
			final boolean checkValueKind) throws CreateObjectException {
		if (creatorId == null || value == null || bindingId == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		if (checkValueKind) {
			final ActionParameterTypeBinding binding;
			try {
				binding = StorableObjectPool.getStorableObject(bindingId, true);
			} catch (ApplicationException ae) {
				throw new CreateObjectException(ae);
			}
			final ParameterValueKind valueKind = binding.getParameterValueKind();
			if (valueKind == ParameterValueKind.ENUMERATED) {
				throw new CreateObjectException("Cannot create new ENUMERATED action parameter; binding :'" + bindingId + "'");
			}
		}

		try {
			final ActionParameter actionParameter = new ActionParameter(IdentifierPool.getGeneratedIdentifier(ACTIONPARAMETER_CODE),
					creatorId,
					INITIAL_VERSION,
					value,
					bindingId);

			assert actionParameter.isValid() : OBJECT_STATE_ILLEGAL;

			actionParameter.markAsChanged();

			return actionParameter;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlActionParameter getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlActionParameterHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.getValue(),
				this.bindingId.getIdlTransferable());
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlActionParameter idlActionParameter = (IdlActionParameter) transferable;
		super.fromTransferable(transferable);

		this.bindingId = Identifier.valueOf(idlActionParameter.bindingId);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final byte[] value,
			final Identifier bindingId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, value	);
		this.bindingId = bindingId;
	}

	public Identifier getBindingId() {
		return this.bindingId;
	}

	@Override
	public Identifier getTypeId() throws ApplicationException {
		if (this.typeId == null) {
			final ActionParameterTypeBinding actionParameterTypeBinding = StorableObjectPool.getStorableObject(this.bindingId, true);
			this.typeId = actionParameterTypeBinding.getParameterTypeId();
		}
		return this.typeId;
	}

	@Override
	public String getTypeCodename() throws ApplicationException {
		if (this.typeCodename == null) {
			final ActionParameterTypeBinding actionParameterTypeBinding = StorableObjectPool.getStorableObject(this.bindingId, true);
			final ParameterType parameterType = StorableObjectPool.getStorableObject(actionParameterTypeBinding.getParameterTypeId(), true);
			this.typeCodename = parameterType.getCodename();
		}
		return this.typeCodename;
	}

	public ParameterValueKind getValueKind() throws ApplicationException {
		if (this.valueKind == null) {
			final ActionParameterTypeBinding actionParameterTypeBinding = StorableObjectPool.getStorableObject(this.bindingId, true);
			this.valueKind = actionParameterTypeBinding.getParameterValueKind();
		}
		return this.valueKind;
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.bindingId != null && this.bindingId.getMajor() == ACTIONPARAMETERTYPEBINDING_CODE;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.bindingId);
		return dependencies;
	}

	@Override
	protected ActionParameterWrapper getWrapper() {
		return ActionParameterWrapper.getInstance();
	}

	public static Set<ActionParameter> getValues(final ActionParameterTypeBinding actionParameterTypeBinding) throws ApplicationException {
		assert actionParameterTypeBinding != null : NON_NULL_EXPECTED;

		return getValues(actionParameterTypeBinding.getId());
	}

	public static Set<ActionParameter> getValues(final Identifier actionParameterTypeBindingId) throws ApplicationException {
		assert actionParameterTypeBindingId != null : NON_NULL_EXPECTED;
		assert actionParameterTypeBindingId.getMajor() == ACTIONPARAMETERTYPEBINDING_CODE : ILLEGAL_ENTITY_CODE;

		final StorableObjectCondition condition = new LinkedIdsCondition(actionParameterTypeBindingId, ACTIONPARAMETER_CODE);
		final Set<ActionParameter> actionParameters = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		return actionParameters;
	}
}
