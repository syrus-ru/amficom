/*
 * $Id: UnitsOfQP1643ACharacteristrics.java,v 1.1.1.1 2005/04/08 15:40:50 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package test.com.syrus.AMFICOM.general;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2005/04/08 15:40:50 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class UnitsOfQP1643ACharacteristrics extends GeneralTestCase {

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

		MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(
			new Identifier("MeasurementPort_19"), true);
		Identifier measurementPortTypeId = measurementPort.getType().getId();
		if (true){
		{
			CharacteristicType resolutionType = CharacteristicType.createInstance(creatorId,
				CharacteristicTypeCodenames.UNITS_RESOLUTION, "resolution unit type", DataType._DATA_TYPE_STRING,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			GeneralStorableObjectPool.putStorableObject(resolutionType);

			Characteristic resolutionCharacteristic = Characteristic.createInstance(creatorId, resolutionType,
				"resolution unit", "resolution unit", CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
				"м", measurementPortTypeId, false, true);
			GeneralStorableObjectPool.putStorableObject(resolutionCharacteristic);
		}

		{
			CharacteristicType waveLengthType = CharacteristicType.createInstance(creatorId,
				CharacteristicTypeCodenames.UNITS_WAVELENGTH, "wave length unit type", DataType._DATA_TYPE_STRING,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			GeneralStorableObjectPool.putStorableObject(waveLengthType);

			Characteristic characteristic = Characteristic.createInstance(creatorId, waveLengthType,
				"wavelength unit", "wavelength unit", CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE,
				"нм", measurementPortTypeId, false, true);
			GeneralStorableObjectPool.putStorableObject(characteristic);
		}

		{
			CharacteristicType averageCountType = CharacteristicType.createInstance(creatorId,
				CharacteristicTypeCodenames.UNITS_AVERAGE_COUNT, "average count unit type",
				DataType._DATA_TYPE_STRING, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			GeneralStorableObjectPool.putStorableObject(averageCountType);
			Characteristic characteristic = Characteristic.createInstance(creatorId, averageCountType,
				"average count unit", "average count unit",
				CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "точек", measurementPortTypeId, false,
				true);
			GeneralStorableObjectPool.putStorableObject(characteristic);
		}

		{
			CharacteristicType traceLengthType = CharacteristicType.createInstance(creatorId,
				CharacteristicTypeCodenames.UNITS_TRACE_LENGTH, "trace length unit type",
				DataType._DATA_TYPE_STRING, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			GeneralStorableObjectPool.putStorableObject(traceLengthType);
			Characteristic characteristic = Characteristic.createInstance(creatorId, traceLengthType,
				"trace length unit", "trace length unit",
				CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "км", measurementPortTypeId, false,
				true);
			GeneralStorableObjectPool.putStorableObject(characteristic);
		}

		{
			CharacteristicType pulseWidthType = CharacteristicType.createInstance(creatorId,
				CharacteristicTypeCodenames.UNITS_PULSE_WIDTH, "average count unit type",
				DataType._DATA_TYPE_STRING, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL);

			GeneralStorableObjectPool.putStorableObject(pulseWidthType);
			Characteristic characteristic = Characteristic.createInstance(creatorId, pulseWidthType,
				"pulse width unit", "pulse width unit",
				CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE, "нс", measurementPortTypeId, false,
				true);
			GeneralStorableObjectPool.putStorableObject(characteristic);
		}
		
		GeneralStorableObjectPool.flush(true);
		}

	}
}
