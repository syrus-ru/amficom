/*-
 * $Id: ModelingResultParameter.java,v 1.1.2.5 2006/02/28 15:20:04 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELINGRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlModelingResultParameter;
import com.syrus.AMFICOM.measurement.corba.IdlModelingResultParameterHelper;

/**
 * @version $Revision: 1.1.2.5 $, $Date: 2006/02/28 15:20:04 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ModelingResultParameter extends ActionResultParameter<ModelingResultParameter> {
	private static final long serialVersionUID = -2708066503219610625L;

	ModelingResultParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final byte[] value,
			final Identifier typeId,
			final Identifier modelingId) {
		super(id, creatorId, version, value, typeId, modelingId);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public ModelingResultParameter(final IdlModelingResultParameter idlModelingResultParameter) throws CreateObjectException {
		super(idlModelingResultParameter);
	}

	static ModelingResultParameter createInstance(final Identifier creatorId,
			final byte[] value,
			final Identifier typeId,
			final Identifier modelingId) throws CreateObjectException {
		try {
			final ModelingResultParameter modelingResultParameter = new ModelingResultParameter(IdentifierPool.getGeneratedIdentifier(MODELINGRESULTPARAMETER_CODE),
					creatorId,
					INITIAL_VERSION,
					value,
					typeId,
					modelingId);

			assert modelingResultParameter.isValid() : OBJECT_STATE_ILLEGAL;

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
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlModelingResultParameterHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.getValue(),
				super.getTypeId().getIdlTransferable(orb),
				this.getModelingId().getIdlTransferable(orb));
	}

	public Identifier getModelingId() {
		return super.getActionId();
	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.getModelingId().getMajor() == MODELING_CODE;
	}

	@Override
	protected ModelingResultParameterWrapper getWrapper() {
		return ModelingResultParameterWrapper.getInstance();
	}

}
