/*-
 * $Id: ParameterType.java,v 1.74.2.3 2006/02/16 13:47:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlParameterTypeHelper;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.74.2.3 $, $Date: 2006/02/16 13:47:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class ParameterType extends StorableObjectType<ParameterType> {
	private static final long serialVersionUID = 2495470569913138317L;

	private DataType dataType;
	private MeasurementUnit measurementUnit;

	ParameterType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final DataType dataType,
			final MeasurementUnit measurementUnit) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.dataType = dataType;
		this.measurementUnit = measurementUnit;
	}

	public ParameterType(final IdlParameterType idlParameterType) throws CreateObjectException {
		try {
			this.fromTransferable(idlParameterType);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public static ParameterType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final DataType dataType,
			final MeasurementUnit measurementUnit) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null || dataType == null || measurementUnit == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final ParameterType parameterType = new ParameterType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETER_TYPE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					codename,
					description,
					dataType,
					measurementUnit);

			assert parameterType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			parameterType.markAsChanged();

			return parameterType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public DataType getDataType() {
		return this.dataType;
	}

	public MeasurementUnit getMeasurementUnit() {
		return this.measurementUnit;
	}

	@Override
	public IdlParameterType getIdlTransferable(final ORB orb) {
		return IdlParameterTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.dataType.getIdlTransferable(orb),
				this.measurementUnit.getIdlTransferable(orb));
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlParameterType idlParameterType = (IdlParameterType) transferable;
		super.fromTransferable(idlParameterType, idlParameterType.codename, idlParameterType.description);
		this.dataType = DataType.valueOf(idlParameterType.idlDataType);
		this.measurementUnit = MeasurementUnit.valueOf(idlParameterType.idlMeasurementUnit);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final DataType dataType,
			final MeasurementUnit measurementUnit) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.dataType = dataType;
		this.measurementUnit = measurementUnit;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return Collections.emptySet();
	}

	@Override
	public ParameterTypeWrapper getWrapper() {
		return ParameterTypeWrapper.getInstance();
	}
}
