/*-
 * $Id: ActionParameter.java,v 1.1.2.2 2006/02/13 19:33:49 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.corba.IdlActionParameter;
import com.syrus.AMFICOM.measurement.corba.IdlActionParameterHelper;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/13 19:33:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameter extends StorableObject<ActionParameter> {
	private static final long serialVersionUID = 4848841373848672128L;

	private Identifier bindingId;
	private byte[] value;

	ActionParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier bindingId,
			final byte[] value) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.bindingId = bindingId;
		this.value = value;
	}

	public ActionParameter(final IdlActionParameter idlActionParameter) throws CreateObjectException {
		try {
			this.fromTransferable(idlActionParameter);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public static ActionParameter createInstance(final Identifier creatorId,
			final Identifier bindingId,
			final byte[] value) throws CreateObjectException{
		if (creatorId == null || bindingId == null || value == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final ActionParameter actionParameter = new ActionParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ACTIONPARAMETER_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					bindingId,
					value);

			assert actionParameter.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			actionParameter.markAsChanged();

			return actionParameter;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlActionParameter getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlActionParameterHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.bindingId.getIdlTransferable(),
				this.value);
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlActionParameter idlActionParameter = (IdlActionParameter) transferable;
		super.fromTransferable(transferable);

		this.bindingId = Identifier.valueOf(idlActionParameter.bindingId);
		this.value = idlActionParameter.value;

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier bindingId,
			final byte[] value) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.bindingId = bindingId;
		this.value = value;
	}

	public Identifier getBindingId() {
		return this.bindingId;
	}

	public byte[] getValue() {
		return this.value;
	}

	public void setValue(final byte[] value) {
		this.value = value;
		super.markAsChanged();
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.bindingId != null && this.bindingId.getMajor() == ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE
				&& this.value != null;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.bindingId);
		return dependencies;
	}

	@Override
	protected ActionParameterWrapper getWrapper() {
		return ActionParameterWrapper.getInstance();
	}

}
