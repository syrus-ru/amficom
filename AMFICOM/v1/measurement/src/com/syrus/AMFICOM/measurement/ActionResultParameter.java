/*-
 * $Id: ActionResultParameter.java,v 1.1.2.4 2006/02/16 12:50:09 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlActionResultParameter;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2006/02/16 12:50:09 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionResultParameter<T extends ActionResultParameter<T>> extends Parameter<T> {
	private Identifier typeId;
	private Identifier actionId;

	private String typeCodename;

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

	ActionResultParameter(final IdlActionResultParameter idlActionResultParameter) throws CreateObjectException {
		super(idlActionResultParameter);
	}

	@Override
	protected final void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlActionResultParameter idlActionResultParameter = (IdlActionResultParameter) transferable;
		super.fromTransferable(transferable);
		this.typeId = Identifier.valueOf(idlActionResultParameter._typeId);
		this.actionId = Identifier.valueOf(idlActionResultParameter.actionId);
	}

	public final Identifier getTypeId() {
		return this.typeId;
	}

	public final Identifier getActionId() {
		return this.actionId;
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
				&& this.typeId != null && this.typeId.getMajor() == ObjectEntities.PARAMETER_TYPE_CODE
				&& this.actionId != null;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.typeId);
		dependencies.add(this.actionId);
		return dependencies;
	}

}
