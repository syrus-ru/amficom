/*-
 * $Id: ModelingType.java,v 1.65.2.11 2006/04/11 13:06:58 arseniy Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_TYPE_CODE;
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
import com.syrus.AMFICOM.measurement.corba.IdlModelingType;
import com.syrus.AMFICOM.measurement.corba.IdlModelingTypeHelper;
import com.syrus.util.Codename;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.65.2.11 $, $Date: 2006/04/11 13:06:58 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ModelingType extends ActionType implements IdlTransferableObjectExt<IdlModelingType> {
	private static final long serialVersionUID = -4924424744229026447L;

	private static TypicalCondition codenameCondition;
	private static EquivalentCondition equivalentCondition;

	ModelingType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id, creatorId, version, codename, description);
	}

	public ModelingType(final IdlModelingType idlModelingType) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlModelingType);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	public static ModelingType createInstance(final Identifier creatorId,
			final String codename,
			final String description) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final ModelingType modelingType = new ModelingType(IdentifierPool.getGeneratedIdentifier(MODELING_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					description);

			assert modelingType.isValid() : OBJECT_STATE_ILLEGAL;

			modelingType.markAsChanged();

			return modelingType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlModelingType getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlModelingTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "");
	}

	public synchronized void fromIdlTransferable(final IdlModelingType idlModelingType) throws IdlConversionException {
		super.fromIdlTransferable(idlModelingType);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	@Override
	public ModelingTypeWrapper getWrapper() {
		return ModelingTypeWrapper.getInstance();
	}

	public static ModelingType valueOf(final Codename codename) throws ApplicationException {
		assert codename != null : NON_NULL_EXPECTED;
		return valueOf(codename.stringValue());
	}

	public static ModelingType valueOf(final String codename) throws ApplicationException {
		assert codename != null : NON_NULL_EXPECTED;

		if (codenameCondition == null) {
			codenameCondition = new TypicalCondition(codename,
					OPERATION_EQUALS,
					MODELING_TYPE_CODE,
					COLUMN_CODENAME);
		} else {
			codenameCondition.setValue(codename);
		}

		final Set<ModelingType> modelingTypes = StorableObjectPool.getStorableObjectsByCondition(codenameCondition, true);
		if (modelingTypes.isEmpty()) {
			throw new ObjectNotFoundException(OBJECT_NOT_FOUND + ": '" + codename + "'");
		}
		assert modelingTypes.size() == 1 : ONLY_ONE_EXPECTED;
		return modelingTypes.iterator().next();
	}

	/**
	 * Найти все существующие типы моделирования.
	 * 
	 * @return Все существующие типы моделирования.
	 * @throws ApplicationException
	 */
	public static Set<ModelingType> getValues() throws ApplicationException {
		if (equivalentCondition == null) {
			equivalentCondition = new EquivalentCondition(MODELING_TYPE_CODE);
		}

		return StorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);
	}
}
