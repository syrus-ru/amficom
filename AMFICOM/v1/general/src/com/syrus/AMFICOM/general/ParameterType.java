/*-
 * $Id: ParameterType.java,v 1.74.2.10 2006/03/27 14:52:24 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
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
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlParameterTypeHelper;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;


/**
 * Тип параметра. Описывается типом данных и единицами измерения. Кодовые имена
 * типов параметров для рефлектометрических измерений см. в
 * ReflectometryParameterTypeCodename.
 * 
 * @version $Revision: 1.74.2.10 $, $Date: 2006/03/27 14:52:24 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class ParameterType extends StorableObjectType implements IdlTransferableObjectExt<IdlParameterType> {
	private static final long serialVersionUID = -2843753663001680790L;

	/**
	 * Тип данных.
	 */
	private DataType dataType;

	/**
	 * Единицы измерения.
	 */
	private MeasurementUnit measurementUnit;


	/**
	 * Условие для поиска параметра по кодовому имени. См.
	 * {@link #valueOf(String)}.
	 */
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

	public ParameterType(final IdlParameterType idlParameterType) {
		try {
			this.fromIdlTransferable(idlParameterType);
		} catch (final IdlConversionException ice) {
			/*
			 * Never.
			 */
			Log.debugMessage(ice, SEVERE);
		}
	}

	/**
	 * Создать новый экземпляр.
	 * 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param dataType
	 * @param measurementUnit
	 * @return Новый экземпляр.
	 * @throws CreateObjectException
	 */
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

	/**
	 * Получить тип данных.
	 * 
	 * @return Тип данных
	 */
	public DataType getDataType() {
		return this.dataType;
	}

	/**
	 * Получить единицы измерения.
	 * 
	 * @return единицы измерения.
	 */
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

	public synchronized void fromIdlTransferable(final IdlParameterType idlParameterType) throws IdlConversionException {
		super.fromIdlTransferable(idlParameterType, idlParameterType.codename, idlParameterType.description);
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

	/**
	 * Найти тип параметра для заданного кодового имени.
	 * 
	 * @param codename
	 *        Кодовое имя
	 * @return Тип параметра, соответствующиий заданному кодовому имени.
	 * @throws ApplicationException
	 *         {@link CreateObjectException}, если такой объект не найден;
	 *         {@link ApplicationException} в случае ошибки поиска.
	 */
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
	 * Создать карту, где по ключу - кодовое имя, а по величине - идентификатор
	 * типа параметра, соответствующего этому кодовому имени.
	 * 
	 * @param codenames
	 * @return Неизменяемая карта вида <String codename, Identifier id>
	 * @throws ApplicationException
	 */
	public static Map<String, Identifier> getCodenameIdentifierMap(final Set<String> codenames) throws ApplicationException {
		assert codenames != null : NON_NULL_EXPECTED;

		if (codenames.isEmpty()) {
			return Collections.emptyMap();
		}

		/* В случае одного объекта поиск слегка шустрее. */
		if (codenames.size() == 1) {
			final String codename = codenames.iterator().next();
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

	/**
	 * Создать карту, где по ключу - кодовое имя, а по величине - идентификатор
	 * типа параметра, соответствующего этому кодовому имени. Этот метод
	 * предполагает, что массив кодовых имён <code>codenames</code> не
	 * содержит повторяющихся значений.
	 * 
	 * @param codenames
	 * @return Неизменяемая карта вида <String codename, Identifier id>
	 * @throws ApplicationException
	 */
	public static Map<String, Identifier> getCodenameIdentifierMap(final String[] codenames) throws ApplicationException {
		assert codenames != null : NON_NULL_EXPECTED;

		final Set<String> codenamesSet = new HashSet<String>();
		for (final String codename : codenames) {
			if (!codenamesSet.contains(codename)) {
				codenamesSet.add(codename);
			} else {
				Log.errorMessage("WARNING: codename '" + codename + "' allready added");
			}
		}
		return getCodenameIdentifierMap(codenamesSet);
	}
}
