/*-
 * $Id: MeasurementResultParameter.java,v 1.1.2.2 2006/02/14 01:26:43 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementResultParameter;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementResultParameterHelper;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/14 01:26:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementResultParameter extends ActionResultParameter<MeasurementResultParameter> {
	private static final long serialVersionUID = 2985887561561020967L;

	MeasurementResultParameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier measurementId,
			final byte[] value) {
		super(id, creatorId, version, typeId, measurementId, value);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public MeasurementResultParameter(final IdlMeasurementResultParameter idlMeasurementResultParameter) throws CreateObjectException {
		super(idlMeasurementResultParameter);
	}

	static MeasurementResultParameter createInstance(final Identifier creatorId,
			final Identifier typeId,
			final Identifier measurementId,
			final byte[] value) throws CreateObjectException {
		try {
			final MeasurementResultParameter measurementResultParameter = new MeasurementResultParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					typeId,
					measurementId,
					value);

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
				super.getTypeId().getIdlTransferable(orb),
				this.getMeasurementId().getIdlTransferable(orb),
				super.getValue());
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
