/*-
 * $Id: ModelingResultParameter.java,v 1.1.2.2 2006/02/14 01:26:43 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlModelingResultParameter;
import com.syrus.AMFICOM.measurement.corba.IdlModelingResultParameterHelper;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/14 01:26:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ModelingResultParameter extends ActionResultParameter<ModelingResultParameter> {
	private static final long serialVersionUID = -5514937939025109447L;

	ModelingResultParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier modelingId,
			final byte[] value) {
		super(id, creatorId, version, typeId, modelingId, value);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public ModelingResultParameter(final IdlModelingResultParameter idlModelingResultParameter) throws CreateObjectException {
		super(idlModelingResultParameter);
	}

	static ModelingResultParameter createInstance(final Identifier creatorId,
			final Identifier typeId,
			final Identifier modelingId,
			final byte[] value) throws CreateObjectException {
		try {
			final ModelingResultParameter modelingResultParameter = new ModelingResultParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELINGRESULTPARAMETER_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					typeId,
					modelingId,
					value);

			assert modelingResultParameter.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			modelingResultParameter.markAsChanged();

			return modelingResultParameter;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlModelingResultParameter getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlModelingResultParameterHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.getTypeId().getIdlTransferable(orb),
				this.getModelingId().getIdlTransferable(orb),
				super.getValue());
	}

	public Identifier getModelingId() {
		return super.getActionId();
	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.getModelingId().getMajor() == ObjectEntities.MODELING_CODE;
	}

	@Override
	protected ModelingResultParameterWrapper getWrapper() {
		return ModelingResultParameterWrapper.getInstance();
	}

}
