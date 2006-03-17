/*-
 * $Id: MeasurementType.java,v 1.111.2.7 2006/03/17 11:54:48 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_FOUND;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementTypeHelper;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.111.2.7 $, $Date: 2006/03/17 11:54:48 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementType extends ActionType implements IdlTransferableObjectExt<IdlMeasurementType> {
	private static final long serialVersionUID = -5293028501528188012L;

	private static TypicalCondition codenameCondition;

	MeasurementType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id, creatorId, version, codename, description);
	}

	public MeasurementType(final IdlMeasurementType idlMeasurementType) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlMeasurementType);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	public static MeasurementType createInstance(final Identifier creatorId,
			final String codename,
			final String description) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final MeasurementType measurementType = new MeasurementType(IdentifierPool.getGeneratedIdentifier(MEASUREMENT_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					description);

			assert measurementType.isValid() : OBJECT_STATE_ILLEGAL;

			measurementType.markAsChanged();

			return measurementType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlMeasurementType getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlMeasurementTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "");
	}

	public synchronized void fromIdlTransferable(final IdlMeasurementType idlMeasurementType) throws IdlConversionException {
		super.fromIdlTransferable(idlMeasurementType);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	@Override
	public MeasurementTypeWrapper getWrapper() {
		return MeasurementTypeWrapper.getInstance();
	}

	public static MeasurementType valueOf(final String codename) throws ApplicationException {
		assert codename != null : NON_NULL_EXPECTED;

		if (codenameCondition == null) {
			codenameCondition = new TypicalCondition(codename,
					OPERATION_EQUALS,
					MEASUREMENT_TYPE_CODE,
					COLUMN_CODENAME);
		} else {
			codenameCondition.setValue(codename);
		}

		final Set<MeasurementType> measurementTypes = StorableObjectPool.getStorableObjectsByCondition(codenameCondition, true);
		if (measurementTypes.isEmpty()) {
			throw new ObjectNotFoundException(OBJECT_NOT_FOUND + ": '" + codename + "'");
		}
		assert measurementTypes.size() == 1 : ONLY_ONE_EXPECTED;
		return measurementTypes.iterator().next();
	}
}
