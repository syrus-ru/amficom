/*
 * $Id: QP1643ACharacteristrics.java,v 1.3 2005/05/26 13:02:51 bob Exp $
 *
 * Copyright ฟ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/26 13:02:51 $
 * @author $Author: bob $
 * @module tools
 */
public class QP1643ACharacteristrics extends CommonTest {

	public QP1643ACharacteristrics(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = QP1643ACharacteristrics.class;
		junit.awtui.TestRunner.run(clazz);
		// junit.swingui.TestRunner.run(clazz);
		// junit.textui.TestRunner.run(clazz);

	}

	public static Test suite() {
		return suiteWrapper(QP1643ACharacteristrics.class);
	}

	public void testCreateCharacteristics() throws ApplicationException {

		MeasurementPort measurementPort = (MeasurementPort) StorableObjectPool.getStorableObject(
			new Identifier("MeasurementPort_19"), true);
		Identifier measurementPortTypeId = measurementPort.getType().getId();

		TypicalCondition typicalCondition;
		Set storableObjects;
		String wavelength = "1625";
		{
			CharacteristicType waveLengthType;
			typicalCondition = new TypicalCondition(CharacteristicTypeCodenames.TRACE_WAVELENGTH,
													OperationSort.OPERATION_EQUALS,
													ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
													StorableObjectWrapper.COLUMN_CODENAME);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				waveLengthType = CharacteristicType.createInstance(creatorUser.getId(),
					CharacteristicTypeCodenames.TRACE_WAVELENGTH, "reflectometer wavelength",
					DataType.DATA_TYPE_INTEGER, CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				StorableObjectPool.putStorableObject(waveLengthType);
			} else
				waveLengthType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic wlCharacteristic = Characteristic.createInstance(creatorUser.getId(), waveLengthType,
				CharacteristicTypeCodenames.TRACE_WAVELENGTH, "QP1643A wavelength",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, wavelength, measurementPortTypeId, false,
				true);
			StorableObjectPool.putStorableObject(wlCharacteristic);
		}

		{
			String codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + wavelength
					+ CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX;
			typicalCondition.setValue(codename);

			CharacteristicType traceLengthType;
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				traceLengthType = CharacteristicType.createInstance(creatorUser.getId(), codename,
					"reflectometer trace length", DataType.DATA_TYPE_DOUBLE,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				StorableObjectPool.putStorableObject(traceLengthType);
			} else
				traceLengthType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic traceLengthCharacteristic = Characteristic.createInstance(creatorUser.getId(),
				traceLengthType, codename, "QP1643A trace length at wavelength 1625.00 nm",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
				"5.00 20.00 50.00 75.00 125.00 250.00 300.00", measurementPortTypeId, false, true);

			StorableObjectPool.putStorableObject(traceLengthCharacteristic);
		}

		{

			String codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + wavelength
					+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX;
			typicalCondition.setValue(codename);

			CharacteristicType pulseWidthType;
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				pulseWidthType = CharacteristicType.createInstance(creatorUser.getId(), codename,
					"reflectometer pulse width", DataType.DATA_TYPE_LONG,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				StorableObjectPool.putStorableObject(pulseWidthType);
			} else
				pulseWidthType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic pulseWidthCharacteristic = Characteristic.createInstance(creatorUser.getId(),
				pulseWidthType, codename, "QP1643A high resolution pulse width at wavelength 1625.00 nm",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "5 10 20 50 100 200 500 1000 2000 5000",
				measurementPortTypeId, false, true);

			StorableObjectPool.putStorableObject(pulseWidthCharacteristic);

		}

		{

			String codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + wavelength
					+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX;
			typicalCondition.setValue(codename);

			CharacteristicType pulseWidthType;
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				pulseWidthType = CharacteristicType.createInstance(creatorUser.getId(), codename,
					"reflectometer pulse width", DataType.DATA_TYPE_LONG,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				StorableObjectPool.putStorableObject(pulseWidthType);
			} else
				pulseWidthType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic pulseWidthCharacteristic = Characteristic.createInstance(creatorUser.getId(),
				pulseWidthType, codename, "QP1643A low resolution pulse width at wavelength 1625.00 nm",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "10000 20000 30000", measurementPortTypeId,
				false, true);

			StorableObjectPool.putStorableObject(pulseWidthCharacteristic);

		}

		{
			String codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + wavelength
					+ CharacteristicTypeCodenames.TRACE_INDEX_OF_REFRACTION_SUFFIX;
			CharacteristicType indexOfRefractionType;

			typicalCondition.setValue(codename);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				indexOfRefractionType = CharacteristicType.createInstance(creatorUser.getId(), codename,
					"reflectometer index of refraction", DataType.DATA_TYPE_DOUBLE,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				StorableObjectPool.putStorableObject(indexOfRefractionType);
			} else
				indexOfRefractionType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic indexOfRefractionCharacteristic = Characteristic.createInstance(creatorUser.getId(),
				indexOfRefractionType, codename, "QP1643A index of refraction at wavelength 1625.00 nm",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "1.468200", measurementPortTypeId, false,
				true);
			StorableObjectPool.putStorableObject(indexOfRefractionCharacteristic);
		}

		{
			String codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + wavelength
					+ CharacteristicTypeCodenames.TRACE_AVERAGE_COUNT_SUFFIX;
			CharacteristicType averageCountType;
			typicalCondition.setValue(codename);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				averageCountType = CharacteristicType.createInstance(creatorUser.getId(), codename,
					"reflectometer average count", DataType.DATA_TYPE_DOUBLE,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				StorableObjectPool.putStorableObject(averageCountType);
			} else
				averageCountType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic averageCountCharacteristic = Characteristic.createInstance(creatorUser.getId(),
				averageCountType, codename, "QP1643A average count at wavelength 1625.00 nm",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "4096 45312 262144", measurementPortTypeId,
				false, true);
			StorableObjectPool.putStorableObject(averageCountCharacteristic);
		}

		{
			CharacteristicType resoulutionType;
			typicalCondition.setValue(CharacteristicTypeCodenames.TRACE_RESOLUTION);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				resoulutionType = CharacteristicType.createInstance(creatorUser.getId(),
					CharacteristicTypeCodenames.TRACE_RESOLUTION, "reflectometer resolution",
					DataType.DATA_TYPE_DOUBLE, CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				StorableObjectPool.putStorableObject(resoulutionType);
			} else
				resoulutionType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic resolutionCharacteristic = Characteristic.createInstance(creatorUser.getId(),
				resoulutionType, CharacteristicTypeCodenames.TRACE_RESOLUTION, "QP1643A resolution",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
				"16.000 8.000 4.000 2.000 1.000 0.500 0.250 0.125", measurementPortTypeId, false, true);
			StorableObjectPool.putStorableObject(resolutionCharacteristic);

		}

		{
			CharacteristicType maxPointsType;
			typicalCondition.setValue(CharacteristicTypeCodenames.TRACE_MAXPOINTS);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				maxPointsType = CharacteristicType.createInstance(creatorUser.getId(),
					CharacteristicTypeCodenames.TRACE_MAXPOINTS, "reflectometer max points",
					DataType.DATA_TYPE_INTEGER, CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				StorableObjectPool.putStorableObject(maxPointsType);
			} else
				maxPointsType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic maxPointsCharacteristic = Characteristic.createInstance(creatorUser.getId(), maxPointsType,
				CharacteristicTypeCodenames.TRACE_MAXPOINTS, "QP1643A maxpoints",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "262144", measurementPortTypeId, false,
				true);
			StorableObjectPool.putStorableObject(maxPointsCharacteristic);

		}

		/* TODO units characteristic to parameter type */
		{

			typicalCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_RESOLUTION,
													OperationSort.OPERATION_EQUALS,
													ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
													StorableObjectWrapper.COLUMN_CODENAME);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			ParameterType parameterType = null;
			if (storableObjects.isEmpty()) {
				fail();
			} else
				parameterType = (ParameterType) storableObjects.iterator().next();

			CharacteristicType resolutionType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_RESOLUTION, "resolution unit type", DataType.DATA_TYPE_STRING,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			StorableObjectPool.putStorableObject(resolutionType);

			Characteristic resolutionCharacteristic = Characteristic.createInstance(creatorUser.getId(),
				resolutionType, "resolution unit", "resolution unit",
				CharacteristicSort.CHARACTERISTIC_SORT_PARAMETER_TYPE, "อ", parameterType.getId(), false, true);
			StorableObjectPool.putStorableObject(resolutionCharacteristic);
		}

		{
			typicalCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_WAVELENGTH,
													OperationSort.OPERATION_EQUALS,
													ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
													StorableObjectWrapper.COLUMN_CODENAME);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			ParameterType parameterType = null;
			if (storableObjects.isEmpty()) {
				fail();
			} else
				parameterType = (ParameterType) storableObjects.iterator().next();

			CharacteristicType waveLengthType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_WAVELENGTH, "wave length unit type", DataType.DATA_TYPE_STRING,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			StorableObjectPool.putStorableObject(waveLengthType);

			Characteristic characteristic = Characteristic.createInstance(creatorUser.getId(), waveLengthType,
				"wavelength unit", "wavelength unit", CharacteristicSort.CHARACTERISTIC_SORT_PARAMETER_TYPE, "ฮอ",
				parameterType.getId(), false, true);
			StorableObjectPool.putStorableObject(characteristic);
		}

		{
			typicalCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_LENGTH,
													OperationSort.OPERATION_EQUALS,
													ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
													StorableObjectWrapper.COLUMN_CODENAME);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			ParameterType parameterType = null;
			if (storableObjects.isEmpty()) {
				fail();
			} else
				parameterType = (ParameterType) storableObjects.iterator().next();
			CharacteristicType traceLengthType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_TRACE_LENGTH, "trace length unit type", DataType.DATA_TYPE_STRING,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			StorableObjectPool.putStorableObject(traceLengthType);
			Characteristic characteristic = Characteristic.createInstance(creatorUser.getId(), traceLengthType,
				"trace length unit", "trace length unit", CharacteristicSort.CHARACTERISTIC_SORT_PARAMETER_TYPE, "หอ",
				parameterType.getId(), false, true);
			StorableObjectPool.putStorableObject(characteristic);
		}
		//
		{
			typicalCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES,
													OperationSort.OPERATION_EQUALS,
													ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
													StorableObjectWrapper.COLUMN_CODENAME);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			ParameterType parameterType = null;
			if (storableObjects.isEmpty()) {
				fail();
			} else
				parameterType = (ParameterType) storableObjects.iterator().next();
			CharacteristicType pulseWidthType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_PULSE_WIDTH, "pulse width unit type", DataType.DATA_TYPE_STRING,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			StorableObjectPool.putStorableObject(pulseWidthType);
			Characteristic characteristic = Characteristic.createInstance(creatorUser.getId(), pulseWidthType,
				"pulse width unit", "pulse width unit", CharacteristicSort.CHARACTERISTIC_SORT_PARAMETER_TYPE, "ฮำ",
				parameterType.getId(), false, true);
			StorableObjectPool.putStorableObject(characteristic);
		}

		{
			typicalCondition = new TypicalCondition(ParameterTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES,
													OperationSort.OPERATION_EQUALS,
													ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
													StorableObjectWrapper.COLUMN_CODENAME);
			storableObjects = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			ParameterType parameterType = null;
			if (storableObjects.isEmpty()) {
				fail();
			} else
				parameterType = (ParameterType) storableObjects.iterator().next();
			CharacteristicType pulseWidthType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_PULSE_WIDTH, "pulse width unit type", DataType.DATA_TYPE_STRING,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			StorableObjectPool.putStorableObject(pulseWidthType);
			Characteristic characteristic = Characteristic.createInstance(creatorUser.getId(), pulseWidthType,
				"pulse width unit", "pulse width unit", CharacteristicSort.CHARACTERISTIC_SORT_PARAMETER_TYPE, "ฮำ",
				parameterType.getId(), false, true);
			StorableObjectPool.putStorableObject(characteristic);
		}
	}
}
