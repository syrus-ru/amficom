/*
 * $Id: UnitsOfQP1643ACharacteristrics.java,v 1.6.2.1 2006/02/17 12:28:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementPortType;

/**
 * @version $Revision: 1.6.2.1 $, $Date: 2006/02/17 12:28:06 $
 * @author $Author: arseniy $
 * @module tools
 */
public class UnitsOfQP1643ACharacteristrics extends TestCase {

	public UnitsOfQP1643ACharacteristrics(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(UnitsOfQP1643ACharacteristrics.class);
		return commonTest.createTestSetup();
	}

	public void testCreateCharacteristics() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final MeasurementPort measurementPort = (MeasurementPort) StorableObjectPool.getStorableObject(new Identifier("MeasurementPort_19"),
				true);
		final MeasurementPortType measurementPortType = measurementPort.getType();
		if (true) {
			{
				CharacteristicType resolutionType = CharacteristicType.createInstance(userId,
						CharacteristicTypeCodenames.UNITS_RESOLUTION,
						"resolution unit type",
						"resolution unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

			Characteristic.createInstance(userId,
						resolutionType,
						"resolution unit",
						"resolution unit",
						"м",
						measurementPortType,
						false,
						true);
		}

		{
				CharacteristicType waveLengthType = CharacteristicType.createInstance(userId,
						CharacteristicTypeCodenames.UNITS_WAVELENGTH,
						"wave length unit type",
						"wave length unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

				Characteristic.createInstance(userId,
						waveLengthType,
						"wavelength unit",
						"wavelength unit",
						"нм",
						measurementPortType,
						false,
						true);
			}

		{
			CharacteristicType averageCountType = CharacteristicType.createInstance(userId,
						CharacteristicTypeCodenames.UNITS_AVERAGE_COUNT,
						"average count unit type",
						"average count unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

				Characteristic.createInstance(userId,
						averageCountType,
						"average count unit",
						"average count unit",
						"точек",
						measurementPortType,
						false,
						true);
			}

		{
				CharacteristicType traceLengthType = CharacteristicType.createInstance(userId,
						CharacteristicTypeCodenames.UNITS_TRACE_LENGTH,
						"trace length unit type",
						"trace length unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

				Characteristic.createInstance(userId,
						traceLengthType,
						"trace length unit",
						"trace length unit",
						"км",
						measurementPortType,
						false,
						true);
			}

			{
				CharacteristicType pulseWidthType = CharacteristicType.createInstance(userId,
						CharacteristicTypeCodenames.UNITS_PULSE_WIDTH,
						"average count unit type",
						"average count unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

				Characteristic.createInstance(userId,
						pulseWidthType,
						"pulse width unit",
						"pulse width unit",
						"нс",
						measurementPortType,
						false,
						true);
			}

			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, userId, true);
		}

	}
}
