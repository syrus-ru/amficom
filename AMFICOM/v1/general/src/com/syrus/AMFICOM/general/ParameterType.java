/*-
 * $Id: ParameterType.java,v 1.74.2.6 2006/03/03 09:49:26 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_FOUND;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlParameterTypeHelper;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.74.2.6 $, $Date: 2006/03/03 09:49:26 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class ParameterType extends StorableObjectType<ParameterType> {
	private static final long serialVersionUID = -2843753663001680790L;

	private DataType dataType;
	private MeasurementUnit measurementUnit;

	private static TypicalCondition codenameCondition;

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
			final ParameterType parameterType = new ParameterType(IdentifierPool.getGeneratedIdentifier(PARAMETER_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					description,
					dataType,
					measurementUnit);

			assert parameterType.isValid() : OBJECT_STATE_ILLEGAL;

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
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

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

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
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
		return Collections.emptySet();
	}

	@Override
	public ParameterTypeWrapper getWrapper() {
		return ParameterTypeWrapper.getInstance();
	}

	public static ParameterType valueOf(final String codename) throws ApplicationException {
		assert codename != null : NON_NULL_EXPECTED;

		if (codenameCondition == null) {
			codenameCondition = new TypicalCondition(codename,
					OPERATION_EQUALS,
					PARAMETER_TYPE_CODE,
					COLUMN_CODENAME);
		} else {
			codenameCondition.setValue(codename);
		}

		final Set<ParameterType> parameterTypes = StorableObjectPool.getStorableObjectsByCondition(codenameCondition, true);
		if (parameterTypes.isEmpty()) {
			throw new ObjectNotFoundException(OBJECT_NOT_FOUND + ": '" + codename + "'");
		}
		assert parameterTypes.size() == 1 : ONLY_ONE_EXPECTED;
		return parameterTypes.iterator().next();
	}

	/**
	 * Create Map codename-identifier.
	 * @param codenames
	 * @return Unmodifiable Map<String codename, Identifier id>
	 * @throws ApplicationException
	 */
	public static Map<String, Identifier> getCodenameIdentifierMap(final String[] codenames) throws ApplicationException {
		assert codenames != null : NON_NULL_EXPECTED;

		if (codenames.length == 0) {
			return Collections.emptyMap();
		}

		//Slightly optimized case
		if (codenames.length == 1) {
			final String codename = codenames[0];
			final TypicalCondition condition = new TypicalCondition(codename,
					OPERATION_EQUALS,
					PARAMETER_TYPE_CODE,
					COLUMN_CODENAME);
			final Set<Identifier> parameterTypeIds = StorableObjectPool.getIdentifiersByCondition(condition, true);
			if (parameterTypeIds.isEmpty()) {
				return Collections.emptyMap();
			}

			assert parameterTypeIds.size() == 1 : ONLY_ONE_EXPECTED;
			final Identifier parameterTypeId = parameterTypeIds.iterator().next();
			final Map<String, Identifier> codenameIdentifierMap = new HashMap<String, Identifier>();
			codenameIdentifierMap.put(codename, parameterTypeId);
			return Collections.unmodifiableMap(codenameIdentifierMap);

		}

		final Set<TypicalCondition> codenameConditions = new HashSet<TypicalCondition>();
		for (final String codename : codenames) {
			codenameConditions.add(new TypicalCondition(codename, OPERATION_EQUALS, PARAMETER_TYPE_CODE, COLUMN_CODENAME));
		}
		final CompoundCondition condition = new CompoundCondition(codenameConditions, OR);
		final Set<ParameterType> parameterTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		if (parameterTypes.isEmpty()) {
			return Collections.emptyMap();
		}

		final Map<String, Identifier> codenameIdentifierMap = new HashMap<String, Identifier>();
		for (final ParameterType parameterType : parameterTypes) {
			codenameIdentifierMap.put(parameterType.getCodename(), parameterType.getId());
		}
		return Collections.unmodifiableMap(codenameIdentifierMap);
	}
}
