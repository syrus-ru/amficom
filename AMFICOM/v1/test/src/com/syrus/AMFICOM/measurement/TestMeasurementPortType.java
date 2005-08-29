/*
 * $Id: TestMeasurementPortType.java,v 1.3 2005/08/29 11:05:43 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.EnumSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

public final class TestMeasurementPortType extends TestCase {

	public TestMeasurementPortType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTest(new TestMeasurementPortType("testGetByCondition"));
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final MeasurementPortType measurementPortType = MeasurementPortType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				"reflectometry",
				"В него лазер светит",
				"Рефлектометрический",
				EnumSet.of(MeasurementType.REFLECTOMETRY));
		StorableObjectPool.flush(measurementPortType, DatabaseCommonTest.getSysUser().getId(), false);
	}

	public void testGetByCondition() throws ApplicationException {
		final TypicalCondition tc = new TypicalCondition(MeasurementType.REFLECTOMETRY,
				OperationSort.OPERATION_IN,
				ObjectEntities.MEASUREMENTPORT_TYPE_CODE,
				MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE);
		final Set<MeasurementPortType> mpts = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		for (final MeasurementPortType measurementPortType : mpts) {
			System.out.println("MeasurementPortType: '" + measurementPortType.getId()
					+ "', codename: '" + measurementPortType.getCodename()
					+ "', description: '" + measurementPortType.getDescription() + "'");
		}
	}

}
