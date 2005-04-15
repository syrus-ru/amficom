/*
 * $Id: QP1643ACharacteristrics.java,v 1.1 2005/04/15 17:34:16 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package test.com.syrus.AMFICOM.general;

import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/15 17:34:16 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class QP1643ACharacteristrics extends GeneralTestCase {

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

		MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(
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
			storableObjects = GeneralStorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				waveLengthType = CharacteristicType.createInstance(creatorId,
					CharacteristicTypeCodenames.TRACE_WAVELENGTH, "reflectometer wavelength",
					DataType.DATA_TYPE_INTEGER, CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				GeneralStorableObjectPool.putStorableObject(waveLengthType);
			} else
				waveLengthType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic wlCharacteristic = Characteristic.createInstance(creatorId, waveLengthType,
				CharacteristicTypeCodenames.TRACE_WAVELENGTH, "QP1643A wavelength",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, wavelength, measurementPortTypeId, false,
				true);
			GeneralStorableObjectPool.putStorableObject(wlCharacteristic);
		}

		{
			String codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + wavelength
					+ CharacteristicTypeCodenames.TRACE_LENGTH_SUFFIX;
			typicalCondition.setValue(codename);

			CharacteristicType traceLengthType;
			storableObjects = GeneralStorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				traceLengthType = CharacteristicType.createInstance(creatorId, codename, "reflectometer trace length",
					DataType.DATA_TYPE_DOUBLE, CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				GeneralStorableObjectPool.putStorableObject(traceLengthType);
			} else
				traceLengthType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic traceLengthCharacteristic = Characteristic.createInstance(creatorId, traceLengthType,
				codename, "QP1643A trace length at wavelength 1625.00 nm",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
				"5.00 20.00 50.00 75.00 125.00 250.00 300.00",
				measurementPortTypeId, false, true);

			GeneralStorableObjectPool.putStorableObject(traceLengthCharacteristic);
		}

		{

			String codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + wavelength
					+ CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_SUFFIX;
			typicalCondition.setValue(codename);

			CharacteristicType pulseWidthType;
			storableObjects = GeneralStorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				pulseWidthType = CharacteristicType.createInstance(creatorId, codename, "reflectometer pulse width",
					DataType.DATA_TYPE_LONG, CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				GeneralStorableObjectPool.putStorableObject(pulseWidthType);
			} else
				pulseWidthType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic pulseWidthCharacteristic = Characteristic
					.createInstance(
						creatorId,
						pulseWidthType,
						codename,
						"QP1643A pulse width at wavelength 1625.00 nm",
						CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
						"5 10 20 50 100 200 500 1000 2000 5000 10000 20000 30000",
						measurementPortTypeId, false, true);

			GeneralStorableObjectPool.putStorableObject(pulseWidthCharacteristic);

		}

		{
			String codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + wavelength
					+ CharacteristicTypeCodenames.TRACE_INDEX_OF_REFRACTION_SUFFIX;
			CharacteristicType indexOfRefractionType;

			typicalCondition.setValue(codename);
			storableObjects = GeneralStorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				indexOfRefractionType = CharacteristicType.createInstance(creatorId, codename,
					"reflectometer index of refraction", DataType.DATA_TYPE_DOUBLE,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				GeneralStorableObjectPool.putStorableObject(indexOfRefractionType);
			} else
				indexOfRefractionType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic indexOfRefractionCharacteristic = Characteristic.createInstance(creatorId,
				indexOfRefractionType, codename, "QP1643A index of refraction at wavelength 1625.00 nm",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "1.468200", measurementPortTypeId, false,
				true);
			GeneralStorableObjectPool.putStorableObject(indexOfRefractionCharacteristic);
		}

		{
			String codename = CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + wavelength
					+ CharacteristicTypeCodenames.TRACE_AVERAGE_COUNT_SUFFIX;
			CharacteristicType averageCountType;
			typicalCondition.setValue(codename);
			storableObjects = GeneralStorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				averageCountType = CharacteristicType.createInstance(creatorId, codename,
					"reflectometer average count", DataType.DATA_TYPE_DOUBLE,
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				GeneralStorableObjectPool.putStorableObject(averageCountType);
			} else
				averageCountType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic averageCountCharacteristic = Characteristic.createInstance(creatorId, averageCountType,
				codename, "QP1643A average count at wavelength 1625.00 nm",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "4096 45312 262144",
				measurementPortTypeId, false, true);
			GeneralStorableObjectPool.putStorableObject(averageCountCharacteristic);
		}

		{
			CharacteristicType resoulutionType;
			typicalCondition.setValue(CharacteristicTypeCodenames.TRACE_RESOLUTION);
			storableObjects = GeneralStorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				resoulutionType = CharacteristicType.createInstance(creatorId,
					CharacteristicTypeCodenames.TRACE_RESOLUTION, "reflectometer resolution",
					DataType.DATA_TYPE_DOUBLE, CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				GeneralStorableObjectPool.putStorableObject(resoulutionType);
			} else
				resoulutionType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic resolutionCharacteristic = Characteristic.createInstance(creatorId, resoulutionType,
				CharacteristicTypeCodenames.TRACE_RESOLUTION, "QP1643A resolution",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
				"16.000 8.000 4.000 2.000 1.000 0.500 0.250 0.125",
				measurementPortTypeId, false, true);
			GeneralStorableObjectPool.putStorableObject(resolutionCharacteristic);

		}

		{
			CharacteristicType maxPointsType;
			typicalCondition.setValue(CharacteristicTypeCodenames.TRACE_MAXPOINTS);
			storableObjects = GeneralStorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			if (storableObjects.isEmpty()) {
				maxPointsType = CharacteristicType.createInstance(creatorId,
					CharacteristicTypeCodenames.TRACE_MAXPOINTS, "reflectometer max points",
					DataType.DATA_TYPE_INTEGER, CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
				GeneralStorableObjectPool.putStorableObject(maxPointsType);
			} else
				maxPointsType = (CharacteristicType) storableObjects.iterator().next();

			Characteristic maxPointsCharacteristic = Characteristic.createInstance(creatorId, maxPointsType,
				CharacteristicTypeCodenames.TRACE_MAXPOINTS, "QP1643A maxpoints",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "262144", measurementPortTypeId, false,
				true);
			GeneralStorableObjectPool.putStorableObject(maxPointsCharacteristic);

		}

		/* TODO units characteristic to parameter type*/
//		{
//			CharacteristicType resolutionType = CharacteristicType.createInstance(creatorId,
//				CharacteristicTypeCodenames.UNITS_RESOLUTION, "resolution unit type", DataType.DATA_TYPE_STRING,
//				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);
//
//			GeneralStorableObjectPool.putStorableObject(resolutionType);
//
//			Characteristic resolutionCharacteristic = Characteristic.createInstance(creatorId, resolutionType,
//				"resolution unit", "resolution unit", CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "м",
//				measurementPortTypeId, false, true);
//			GeneralStorableObjectPool.putStorableObject(resolutionCharacteristic);
//		}
//
//		{
//			CharacteristicType waveLengthType = CharacteristicType.createInstance(creatorId,
//				CharacteristicTypeCodenames.UNITS_WAVELENGTH, "wave length unit type", DataType.DATA_TYPE_STRING,
//				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);
//
//			GeneralStorableObjectPool.putStorableObject(waveLengthType);
//
//			Characteristic characteristic = Characteristic.createInstance(creatorId, waveLengthType, "wavelength unit",
//				"wavelength unit", CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "нм",
//				measurementPortTypeId, false, true);
//			GeneralStorableObjectPool.putStorableObject(characteristic);
//		}
//
//		{
//			CharacteristicType averageCountType = CharacteristicType.createInstance(creatorId,
//				CharacteristicTypeCodenames.UNITS_AVERAGE_COUNT, "average count unit type", DataType.DATA_TYPE_STRING,
//				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);
//
//			GeneralStorableObjectPool.putStorableObject(averageCountType);
//			Characteristic characteristic = Characteristic.createInstance(creatorId, averageCountType,
//				"average count unit", "average count unit",
//				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "точек", measurementPortTypeId, false,
//				true);
//			GeneralStorableObjectPool.putStorableObject(characteristic);
//		}
//
//		{
//			CharacteristicType traceLengthType = CharacteristicType.createInstance(creatorId,
//				CharacteristicTypeCodenames.UNITS_TRACE_LENGTH, "trace length unit type", DataType.DATA_TYPE_STRING,
//				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);
//
//			GeneralStorableObjectPool.putStorableObject(traceLengthType);
//			Characteristic characteristic = Characteristic.createInstance(creatorId, traceLengthType,
//				"trace length unit", "trace length unit", CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
//				"км", measurementPortTypeId, false, true);
//			GeneralStorableObjectPool.putStorableObject(characteristic);
//		}
//
//		{
//			CharacteristicType pulseWidthType = CharacteristicType.createInstance(creatorId,
//				CharacteristicTypeCodenames.UNITS_PULSE_WIDTH, "average count unit type", DataType.DATA_TYPE_STRING,
//				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);
//
//			GeneralStorableObjectPool.putStorableObject(pulseWidthType);
//			Characteristic characteristic = Characteristic.createInstance(creatorId, pulseWidthType,
//				"pulse width unit", "pulse width unit", CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
//				"нс", measurementPortTypeId, false, true);
//			GeneralStorableObjectPool.putStorableObject(characteristic);
//		}
	}
}
