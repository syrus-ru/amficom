/*-
 * $Id: AnalysisType.java,v 1.107.2.10 2006/04/07 08:14:06 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_FOUND;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisType;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisTypeHelper;
import com.syrus.util.Codename;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.107.2.10 $, $Date: 2006/04/07 08:14:06 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class AnalysisType extends ActionType implements IdlTransferableObjectExt<IdlAnalysisType> {
	private static final long serialVersionUID = 3770601862577867745L;

	private static TypicalCondition codenameCondition;
	private static EquivalentCondition equivalentCondition;

	AnalysisType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id, creatorId, version, codename, description);
	}

	public AnalysisType(final IdlAnalysisType idlAnalysisType) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlAnalysisType);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	public static AnalysisType createInstance(final Identifier creatorId,
			final String codename,
			final String description) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final AnalysisType analysisType = new AnalysisType(IdentifierPool.getGeneratedIdentifier(ANALYSIS_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					description);

			assert analysisType.isValid() : OBJECT_STATE_ILLEGAL;

			analysisType.markAsChanged();

			return analysisType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlAnalysisType getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

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

	public synchronized void fromIdlTransferable(final IdlAnalysisType idlAnalysisType) throws IdlConversionException {
		super.fromIdlTransferable(idlAnalysisType);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	@Override
	public AnalysisTypeWrapper getWrapper() {
		return AnalysisTypeWrapper.getInstance();
	}

	public static AnalysisType valueOf(final Codename codename) throws ApplicationException {
		assert codename != null : NON_NULL_EXPECTED;
		return valueOf(codename.stringValue());
	}

	public static AnalysisType valueOf(final String codename) throws ApplicationException {
		assert codename != null : NON_NULL_EXPECTED;

		if (codenameCondition == null) {
			codenameCondition = new TypicalCondition(codename,
					OPERATION_EQUALS,
					ANALYSIS_TYPE_CODE,
					COLUMN_CODENAME);
		} else {
			codenameCondition.setValue(codename);
		}

		final Set<AnalysisType> analysisTypes = StorableObjectPool.getStorableObjectsByCondition(codenameCondition, true);
		if (analysisTypes.isEmpty()) {
			throw new ObjectNotFoundException(OBJECT_NOT_FOUND + ": '" + codename + "'");
		}
		assert analysisTypes.size() == 1 : ONLY_ONE_EXPECTED;
		return analysisTypes.iterator().next();
	}

	/**
	 * Найти все существующие типы анализа.
	 * 
	 * @return Все существующие типы анализа.
	 * @throws ApplicationException
	 */
	public static Set<AnalysisType> getValues() throws ApplicationException {
		if (equivalentCondition == null) {
			equivalentCondition = new EquivalentCondition(ANALYSIS_TYPE_CODE);
		}

		return StorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);
	}
}
