/*
 * $Id: UnitsOfQP1643ACharacteristrics.java,v 1.5 2005/08/20 19:40:40 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import junit.framework.Test;

import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.measurement.MeasurementPort;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/20 19:40:40 $
 * @author $Author: arseniy $
 * @module tools
 */
public class UnitsOfQP1643ACharacteristrics extends DatabaseCommonTest {

	public UnitsOfQP1643ACharacteristrics(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = UnitsOfQP1643ACharacteristrics.class;
		junit.awtui.TestRunner.run(clazz);
		// junit.swingui.TestRunner.run(clazz);
		// junit.textui.TestRunner.run(clazz);

	}

	public static Test suite() {
		return suiteWrapper(UnitsOfQP1643ACharacteristrics.class);
	}

	public void testCreateCharacteristics() throws ApplicationException {

		MeasurementPort measurementPort = (MeasurementPort) StorableObjectPool.getStorableObject(
			new Identifier("MeasurementPort_19"), true);
		Identifier measurementPortTypeId = measurementPort.getType().getId();
		if (true){
		{
			CharacteristicType resolutionType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_RESOLUTION, "resolution unit type", DataType.DATA_TYPE_STRING,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			Characteristic resolutionCharacteristic = Characteristic.createInstance(creatorUser.getId(), resolutionType,
				"resolution unit", "resolution unit", CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
				"�", measurementPortTypeId, false, true);
		}

		{
			CharacteristicType waveLengthType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_WAVELENGTH, "wave length unit type", DataType.DATA_TYPE_STRING,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			Characteristic characteristic = Characteristic.createInstance(creatorUser.getId(), waveLengthType,
				"wavelength unit", "wavelength unit", CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
				"��", measurementPortTypeId, false, true);
		}

		{
			CharacteristicType averageCountType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_AVERAGE_COUNT, "average count unit type",
				DataType.DATA_TYPE_STRING, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			Characteristic characteristic = Characteristic.createInstance(creatorUser.getId(), averageCountType,
				"average count unit", "average count unit",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "�����", measurementPortTypeId, false,
				true);
		}

		{
			CharacteristicType traceLengthType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_TRACE_LENGTH, "trace length unit type",
				DataType.DATA_TYPE_STRING, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			Characteristic characteristic = Characteristic.createInstance(creatorUser.getId(), traceLengthType,
				"trace length unit", "trace length unit",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "��", measurementPortTypeId, false,
				true);
		}

		{
			CharacteristicType pulseWidthType = CharacteristicType.createInstance(creatorUser.getId(),
				CharacteristicTypeCodenames.UNITS_PULSE_WIDTH, "average count unit type",
				DataType.DATA_TYPE_STRING, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			Characteristic characteristic = Characteristic.createInstance(creatorUser.getId(), pulseWidthType,
				"pulse width unit", "pulse width unit",
				CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "��", measurementPortTypeId, false,
				true);
		}
		
		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, true);
		}

	}
}
