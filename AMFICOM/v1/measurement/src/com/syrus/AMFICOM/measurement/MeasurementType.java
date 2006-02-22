/*-
 * $Id: MeasurementType.java,v 1.111.2.4 2006/02/22 08:36:55 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementTypeHelper;

/**
 * @version $Revision: 1.111.2.4 $, $Date: 2006/02/22 08:36:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementType extends ActionType<MeasurementType> {
	private static final long serialVersionUID = -2653382649796391330L;

	MeasurementType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id, creatorId, version, codename, description);
	}

	public MeasurementType(final IdlMeasurementType idlMeasurementType) throws CreateObjectException {
		super(idlMeasurementType);
	}

	public static MeasurementType createInstance(final Identifier creatorId,
			final String codename,
			final String description) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final MeasurementType measurementType = new MeasurementType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_TYPE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					codename,
					description);

			assert measurementType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurementType.markAsChanged();

			return measurementType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public IdlMeasurementType getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

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

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMeasurementType idlMeasurementType = (IdlMeasurementType) transferable;
		super.fromTransferable(idlMeasurementType, idlMeasurementType.codename, idlMeasurementType.description);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	@Override
	public MeasurementTypeWrapper getWrapper() {
		return MeasurementTypeWrapper.getInstance();
	}
}
