/*
 * $Id: TestMeasurementPortType.java,v 1.2 2005/07/15 12:00:46 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

public final class TestMeasurementPortType extends TestCase {

	public TestMeasurementPortType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestMeasurementPortType.class);
		return commonTest.createTestSetup();
	}

	public void _testCreateInstance() throws ApplicationException {
		final MeasurementPortType measurementPortType = MeasurementPortType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				"reflectometry",
				"В него лазер светит",
				"Рефлектометрический");
		StorableObjectPool.flush(measurementPortType, false);
	}

	public void testUpdate() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
		final Set<MeasurementPortType> set = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		final MeasurementPortType measurementPortType = set.iterator().next();
		final String mptDescription = measurementPortType.getDescription();

		final Set<Characteristic> characteristics = measurementPortType.getCharacteristics();
		final Characteristic characteristic = characteristics.iterator().next();		
		final String cDescription = characteristic.getDescription();

		System.out.println("Measurement port type: '" + mptDescription + "', characteristic: '" + cDescription + "'");

		measurementPortType.setDescription(mptDescription + "123");
		characteristic.setDescription(cDescription + "123");
		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, false);
	}
}
