/*-
 * $Id: AnalysisResultParameter.java,v 1.1.2.4 2006/02/22 11:26:42 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisResultParameter;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisResultParameterHelper;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2006/02/22 11:26:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class AnalysisResultParameter extends ActionResultParameter<AnalysisResultParameter> {
	private static final long serialVersionUID = 8384993460596854206L;

	AnalysisResultParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final byte[] value,
			final Identifier typeId,
			final Identifier analysisId) {
		super(id, creatorId, version, value, typeId, analysisId);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public AnalysisResultParameter(final IdlAnalysisResultParameter idlAnalysisResultParameter) throws CreateObjectException {
		super(idlAnalysisResultParameter);
	}

	static AnalysisResultParameter createInstance(final Identifier creatorId,
			final byte[] value,
			final Identifier typeId,
			final Identifier analysisId) throws CreateObjectException {
		try {
			final AnalysisResultParameter analysisResultParameter = new AnalysisResultParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSISRESULTPARAMETER_CODE),
					creatorId,
					INITIAL_VERSION,
					value,
					typeId,
					analysisId);

			assert analysisResultParameter.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			analysisResultParameter.markAsChanged();

			return analysisResultParameter;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlAnalysisResultParameter getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlAnalysisResultParameterHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.getValue(),
				super.getTypeId().getIdlTransferable(orb),
				this.getAnalysisId().getIdlTransferable(orb));
	}

	public Identifier getAnalysisId() {
		return super.getActionId();
	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.getAnalysisId().getMajor() == ObjectEntities.ANALYSIS_CODE;
	}

	@Override
	protected AnalysisResultParameterWrapper getWrapper() {
		return AnalysisResultParameterWrapper.getInstance();
	}

}
