/*-
 * $Id: MeasurementResultParameter.java,v 1.1.2.3 2006/02/16 12:50:09 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
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
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementResultParameter;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementResultParameterHelper;

/**
 * @version $Revision: 1.1.2.3 $, $Date: 2006/02/16 12:50:09 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementResultParameter extends ActionResultParameter<MeasurementResultParameter> {

	MeasurementResultParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final byte[] value,
			final Identifier typeId,
			final Identifier measurementId) {
		super(id, creatorId, version, value, typeId, measurementId);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public MeasurementResultParameter(final IdlMeasurementResultParameter idlMeasurementResultParameter) throws CreateObjectException {
		super(idlMeasurementResultParameter);
	}

	static MeasurementResultParameter createInstance(final Identifier creatorId,
			final byte[] value,
			final Identifier typeId,
			final Identifier measurementId) throws CreateObjectException {
		try {
			final MeasurementResultParameter measurementResultParameter = new MeasurementResultParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE),
					creatorId,
					INITIAL_VERSION,
					value,
					typeId,
					measurementId);

			assert measurementResultParameter.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurementResultParameter.markAsChanged();

			return measurementResultParameter;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlMeasurementResultParameter getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlMeasurementResultParameterHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.getValue(),
				super.getTypeId().getIdlTransferable(orb),
				this.getMeasurementId().getIdlTransferable(orb));
	}

	public Identifier getMeasurementId() {
		return super.getActionId();
	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.getMeasurementId().getMajor() == ObjectEntities.MEASUREMENT_CODE;
	}

	@Override
	protected MeasurementResultParameterWrapper getWrapper() {
		return MeasurementResultParameterWrapper.getInstance();
	}

}