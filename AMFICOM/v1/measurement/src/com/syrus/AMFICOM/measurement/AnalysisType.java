/*-
 * $Id: AnalysisType.java,v 1.107.2.3 2006/02/14 01:26:43 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisType;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisTypeHelper;

/**
 * @version $Revision: 1.107.2.3 $, $Date: 2006/02/14 01:26:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class AnalysisType extends ActionType<AnalysisType> {
	private static final long serialVersionUID = -852425306818650355L;

	AnalysisType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id, creatorId, version, codename, description);
	}

	public AnalysisType(final IdlAnalysisType idlAnalysisType) throws CreateObjectException {
		super(idlAnalysisType);
	}

	public static AnalysisType createInstance(final Identifier creatorId,
			final String codename,
			final String description) throws ApplicationException {
		if (creatorId == null || codename == null || description == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final AnalysisType analysisType = new AnalysisType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSIS_TYPE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					codename,
					description);

			assert analysisType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			analysisType.markAsChanged();

			return analysisType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlAnalysisType getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlAnalysisTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "");
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlAnalysisType idlAnalysisType = (IdlAnalysisType) transferable;
		super.fromTransferable(idlAnalysisType, idlAnalysisType.codename, idlAnalysisType.description);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	@Override
	public AnalysisTypeWrapper getWrapper() {
		return AnalysisTypeWrapper.getInstance();
	}
}
