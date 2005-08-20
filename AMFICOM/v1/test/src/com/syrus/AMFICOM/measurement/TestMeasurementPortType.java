/*
 * $Id: TestMeasurementPortType.java,v 1.1 2005/08/20 19:45:03 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
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

	public void testCreateInstance() throws ApplicationException {
		final MeasurementPortType measurementPortType = MeasurementPortType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				"reflectometry",
				"В него лазер светит",
				"Рефлектометрический");
		StorableObjectPool.flush(measurementPortType, DatabaseCommonTest.getSysUser().getId(), false);
	}
}
