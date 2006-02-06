/*
 * $Id: UnitsOfQP1643ACharacteristrics.java,v 1.6 2005/12/15 14:36:44 arseniy Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/12/15 14:36:44 $
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
		final Identifier creatorId = DatabaseCommonTest.getSysUser().getId();

		final MeasurementPort measurementPort = (MeasurementPort) StorableObjectPool.getStorableObject(new Identifier("MeasurementPort_19"),
				true);
		final MeasurementPortType measurementPortType = measurementPort.getType();
		if (true) {
			{
				CharacteristicType resolutionType = CharacteristicType.createInstance(creatorId,
						CharacteristicTypeCodenames.UNITS_RESOLUTION,
						"resolution unit type",
						"resolution unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

			Characteristic.createInstance(creatorId,
						resolutionType,
						"resolution unit",
						"resolution unit",
						"м",
						measurementPortType,
						false,
						true);
		}

		{
				CharacteristicType waveLengthType = CharacteristicType.createInstance(creatorId,
						CharacteristicTypeCodenames.UNITS_WAVELENGTH,
						"wave length unit type",
						"wave length unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

				Characteristic.createInstance(creatorId,
						waveLengthType,
						"wavelength unit",
						"wavelength unit",
						"нм",
						measurementPortType,
						false,
						true);
			}

		{
			CharacteristicType averageCountType = CharacteristicType.createInstance(creatorId,
						CharacteristicTypeCodenames.UNITS_AVERAGE_COUNT,
						"average count unit type",
						"average count unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

				Characteristic.createInstance(creatorId,
						averageCountType,
						"average count unit",
						"average count unit",
						"точек",
						measurementPortType,
						false,
						true);
			}

		{
				CharacteristicType traceLengthType = CharacteristicType.createInstance(creatorId,
						CharacteristicTypeCodenames.UNITS_TRACE_LENGTH,
						"trace length unit type",
						"trace length unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

				Characteristic.createInstance(creatorId,
						traceLengthType,
						"trace length unit",
						"trace length unit",
						"км",
						measurementPortType,
						false,
						true);
			}

			{
				CharacteristicType pulseWidthType = CharacteristicType.createInstance(creatorId,
						CharacteristicTypeCodenames.UNITS_PULSE_WIDTH,
						"average count unit type",
						"average count unit type",
						DataType.STRING,
						CharacteristicTypeSort.VISUAL);

				Characteristic.createInstance(creatorId,
						pulseWidthType,
						"pulse width unit",
						"pulse width unit",
						"нс",
						measurementPortType,
						false,
						true);
			}

			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, creatorId, true);
		}

	}
}
