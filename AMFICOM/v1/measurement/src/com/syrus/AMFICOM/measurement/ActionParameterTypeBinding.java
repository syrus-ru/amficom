/*-
 * $Id: ActionParameterTypeBinding.java,v 1.1.2.1 2006/02/11 18:40:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterTypeBinding;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterTypeBindingHelper;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterTypeBindingPackage.IdlParameterValueKind;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/11 18:40:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameterTypeBinding extends StorableObject<ActionParameterTypeBinding> {
	private static final long serialVersionUID = -5086530158193569493L;

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
		this.parameterTypeId = new Identifier(idlActionParameterTypeBinding.parameterTypeId);
		this.actionTypeId = new Identifier(idlActionParameterTypeBinding.actionTypeId);
		this.measurementPortTypeId = new Identifier(idlActionParameterTypeBinding.measurementPortTypeId);

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

}
