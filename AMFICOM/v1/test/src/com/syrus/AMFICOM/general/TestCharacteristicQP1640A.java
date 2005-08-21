/*
 * $Id: TestCharacteristicQP1640A.java,v 1.3 2005/08/21 16:12:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.MeasurementPortType;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/21 16:12:06 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestCharacteristicQP1640A extends TestCase {
	private static final String WAVELENGTH = "1625";
	private static final String TRACELENGTH = "5.00 20.00 50.00 75.00 125.00 250.00 300.00";
	private static final String PULSE_WIDTH_HIGH_RES = "5 10 20 50 100 200 500 1000 2000 5000";
	private static final String PULSE_WIDTH_LOW_RES = "1000 2000 5000 10000 20000";
	private static final String RESOLUTION = "0.125 0.250 0.500 1.000 2.000 4.000 8.000 16.000";
	private static final String IOR = "1.468200";
	private static final String AVERAGES = "4096 32768 262144";
	private static final String MAX_POINTS = "262144";
	private static final String[] CODENAMES = new String[] { CharacteristicTypeCodenames.TRACE_WAVELENGTH,
			CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX,
			CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX,
			CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX,
			CharacteristicTypeCodenames.TRACE_RESOLUTION,
			CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_INDEX_OF_REFRACTION_SUFFIX,
			CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_AVERAGE_COUNT_SUFFIX,
			CharacteristicTypeCodenames.TRACE_MAXPOINTS,
			CharacteristicTypeCodenames.UNITS_WAVELENGTH,
			CharacteristicTypeCodenames.UNITS_TRACE_LENGTH,
			CharacteristicTypeCodenames.UNITS_PULSE_WIDTH,
			CharacteristicTypeCodenames.UNITS_RESOLUTION };

	public TestCharacteristicQP1640A(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestCharacteristicQP1640A.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
		final MeasurementPortType measurementPortType = (MeasurementPortType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();

		final Map<String, CharacteristicType> characteristicTypeMap = this.retrieveExistingCharacteristicTypes();
		String codename;
		String description;
		CharacteristicType characteristicType;
		final Identifier userId = DatabaseCommonTest.getSysUser().getId();

		/*
		 * ref_wvlen
		 */
		codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH;
		description = "Измерительная длина волны";
		characteristicType = characteristicTypeMap.get(codename);
		if (characteristicType == null) {
			characteristicType = CharacteristicType.createInstance(userId,
					codename,
					description,
					"Длина волны",
					DataType.INTEGER,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
			characteristicTypeMap.put(codename, characteristicType);
		}
		Characteristic.createInstance(userId,
				characteristicType,
				codename,
				description,
				WAVELENGTH,
				measurementPortType,
				false,
				false);

		/*
		 * ref_trclen
		 */
		codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX;
		description = "Длина рефлектограммы";
		characteristicType = characteristicTypeMap.get(codename);
		if (characteristicType == null) {
			characteristicType = CharacteristicType.createInstance(userId,
					codename,
					description,
					"Длина рефлектограммы",
					DataType.DOUBLE,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
			characteristicTypeMap.put(codename, characteristicType);
		}
		Characteristic.createInstance(userId,
				characteristicType,
				codename,
				description,
				TRACELENGTH,
				measurementPortType,
				false,
				false);

		/*
		 * ref_pulswd_high_res
		 */
		codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX;
		description = "Ширина импульса в режиме высокого разрешения";
		characteristicType = characteristicTypeMap.get(codename);
		if (characteristicType == null) {
			characteristicType = CharacteristicType.createInstance(userId,
					codename,
					description,
					"Ширина импульса",
					DataType.LONG,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
			characteristicTypeMap.put(codename, characteristicType);
		}
		Characteristic.createInstance(userId,
				characteristicType,
				codename,
				description,
				PULSE_WIDTH_HIGH_RES,
				measurementPortType,
				false,
				false);

		/*
		 * ref_pulswd_low_res
		 */
		codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX;
		description = "Ширина импульса в режиме низкого разрешения";
		characteristicType = characteristicTypeMap.get(codename);
		if (characteristicType == null) {
			characteristicType = CharacteristicType.createInstance(userId,
					codename,
					description,
					"Ширина импульса",
					DataType.LONG,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
			characteristicTypeMap.put(codename, characteristicType);
		}
		Characteristic.createInstance(userId,
				characteristicType,
				codename,
				description,
				PULSE_WIDTH_LOW_RES,
				measurementPortType,
				false,
				false);

		/*
		 * ref_res
		 */
		codename = CharacteristicTypeCodenames.TRACE_RESOLUTION;
		description = "Разрешение (расстояние между соседними точками)";
		characteristicType = characteristicTypeMap.get(codename);
		if (characteristicType == null) {
			characteristicType = CharacteristicType.createInstance(userId,
					codename,
					description,
					"Разрешение",
					DataType.DOUBLE,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
			characteristicTypeMap.put(codename, characteristicType);
		}
		Characteristic.createInstance(userId, characteristicType, codename, description, RESOLUTION, measurementPortType, false, false);

		/*
		 * ref_ior
		 */
		codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_INDEX_OF_REFRACTION_SUFFIX;
		description = "Показатель преломления";
		characteristicType = characteristicTypeMap.get(codename);
		if (characteristicType == null) {
			characteristicType = CharacteristicType.createInstance(userId,
					codename,
					description,
					"Показатель преломления",
					DataType.DOUBLE,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
			characteristicTypeMap.put(codename, characteristicType);
		}
		Characteristic.createInstance(userId, characteristicType, codename, description, IOR, measurementPortType, false, false);
		
		/*
		 * ref_scans
		 */
		codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + WAVELENGTH + CharacteristicTypeCodenames.TRACE_AVERAGE_COUNT_SUFFIX;
		description = "Количество усреднений";
		characteristicType = characteristicTypeMap.get(codename);
		if (characteristicType == null) {
			characteristicType = CharacteristicType.createInstance(userId,
					codename,
					description,
					"Количество усреднений",
					DataType.DOUBLE,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
			characteristicTypeMap.put(codename, characteristicType);
		}
		Characteristic.createInstance(userId, characteristicType, codename, description, AVERAGES, measurementPortType, false, false);

		/*
		 * ref_maxpoints
		 */
		codename = CharacteristicTypeCodenames.TRACE_MAXPOINTS;
		description = "Максимальное количество точек на рефлектограмме";
		characteristicType = characteristicTypeMap.get(codename);
		if (characteristicType == null) {
			characteristicType = CharacteristicType.createInstance(userId,
					codename,
					description,
					"Максимальное количество точек",
					DataType.INTEGER,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
			characteristicTypeMap.put(codename, characteristicType);
		}
		Characteristic.createInstance(userId, characteristicType, codename, description, MAX_POINTS, measurementPortType, false, false);
		
		/*
		 * Save all new objects
		 */
		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, DatabaseCommonTest.getSysUser().getId(), false);
	}

	private Map<String, CharacteristicType> retrieveExistingCharacteristicTypes() throws ApplicationException {
		final Map<String, CharacteristicType> characteristicTypeMap = new HashMap<String, CharacteristicType>();

		final Set<StorableObjectCondition> conditions = new HashSet<StorableObjectCondition>();
		for (int i = 0; i < CODENAMES.length; i++) {
			conditions.add(new TypicalCondition(CODENAMES[i],
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.CHARACTERISTIC_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME));
		}
		final CompoundCondition cc = new CompoundCondition(conditions, CompoundConditionSort.OR);
		final Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(cc, true);
		for (final CharacteristicType characteristicType : characteristicTypes) {
			characteristicTypeMap.put(characteristicType.getCodename(), characteristicType);
		}

		return characteristicTypeMap;
	}
}
