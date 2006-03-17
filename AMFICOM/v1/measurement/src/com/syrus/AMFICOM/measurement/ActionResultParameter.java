/*-
 * $Id: ActionResultParameter.java,v 1.1.2.11 2006/03/17 11:54:48 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlActionResultParameter;

/**
 * @version $Revision: 1.1.2.11 $, $Date: 2006/03/17 11:54:48 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionResultParameter<A extends Action> extends Parameter {
	private Identifier typeId;
	private Identifier actionId;

	private transient String typeCodename;

	ActionResultParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final byte[] value,
			final Identifier typeId,
			final Identifier actionId) {
		super(id, creatorId, version, value);
		this.typeId = typeId;
		this.actionId = actionId;
	}

	ActionResultParameter() {
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
	 * @param idlActionResultParameter
	 * @throws IdlConversionException
	 */
	final void fromIdlTransferable(final IdlActionResultParameter idlActionResultParameter) {
		super.fromIdlTransferable(idlActionResultParameter);
		this.typeId = Identifier.valueOf(idlActionResultParameter._typeId);
		this.actionId = Identifier.valueOf(idlActionResultParameter.actionId);
	}

	@Override
	public final Identifier getTypeId() {
		return this.typeId;
	}

	public final Identifier getActionId() {
		return this.actionId;
	}

	public final A getAction() throws ApplicationException {
		return StorableObjectPool.<A>getStorableObject(this.actionId, true);
	}

	@Override
	public final String getTypeCodename() throws ApplicationException {
		if (this.typeCodename == null) {
			final ParameterType parameterType = StorableObjectPool.getStorableObject(this.typeId, true);
			this.typeCodename = parameterType.getCodename();
		}
		return this.typeCodename;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized final void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final byte[] value,
			final Identifier typeId,
			final Identifier actionId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, value);
		this.typeId = typeId;
		this.actionId = actionId;
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.typeId != null && this.typeId.getMajor() == PARAMETER_TYPE_CODE
				&& this.actionId != null;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.typeId);
		dependencies.add(this.actionId);
		return dependencies;
	}

}
