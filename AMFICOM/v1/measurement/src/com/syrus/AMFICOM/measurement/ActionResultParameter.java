/*-
 * $Id: ActionResultParameter.java,v 1.1.2.3 2006/02/14 00:43:51 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlActionResultParameter;

/**
 * @version $Revision: 1.1.2.3 $, $Date: 2006/02/14 00:43:51 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionResultParameter<T extends ActionResultParameter<T>> extends StorableObject<T> {
	private Identifier typeId;
	private Identifier actionId;
	private byte[] value;

	ActionResultParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier actionId,
			final byte[] value) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.typeId = typeId;
		this.actionId = actionId;
		this.value = value;
	}

	ActionResultParameter(final IdlStorableObject idlStorableObject) throws CreateObjectException {
		try {
			this.fromTransferable(idlStorableObject);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	@Override
	protected final void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlActionResultParameter idlActionResultParameter = (IdlActionResultParameter) transferable;
		super.fromTransferable(transferable);
		this.typeId = Identifier.valueOf(idlActionResultParameter._typeId);
		this.actionId = Identifier.valueOf(idlActionResultParameter.actionId);
		this.value = idlActionResultParameter.value;
	}

	public final Identifier getTypeId() {
		return this.typeId;
	}

	public final Identifier getActionId() {
		return this.actionId;
	}

	public final byte[] getValue() {
		return this.value;
	}

	public void setValue(final byte[] value) {
		this.value = value;
		this.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized final void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier actionId,
			final byte[] value) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.typeId = typeId;
		this.actionId = actionId;
		this.value = value;
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.typeId != null && this.typeId.getMajor() == ObjectEntities.PARAMETER_TYPE_CODE
				&& this.actionId != null
				&& this.value != null;
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
