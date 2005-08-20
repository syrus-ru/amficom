/*
 * $Id: TestMeasurementPort.java,v 1.3 2005/08/20 19:40:40 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementPortType;

public final class TestMeasurementPort extends TestCase {

	public TestMeasurementPort(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestMeasurementPort.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final MeasurementPortType measurementPortType = (MeasurementPortType) it.next();

		ec = new EquivalentCondition(ObjectEntities.KIS_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final KIS kis = (KIS) it.next();

		final Equipment equipment = (Equipment) StorableObjectPool.getStorableObject(kis.getEquipmentId(), true);
		final Port port = (Port) equipment.getPorts().iterator().next();

		MeasurementPort measurementPort = MeasurementPort.createInstance(DatabaseCommonTest.getSysUser().getId(),
				measurementPortType,
				"Порт 1",
				"Порт 1 на оптическом переключателе",
				kis.getId(),
				port.getId());
		StorableObjectPool.flush(measurementPort, DatabaseCommonTest.getSysUser().getId(), true);
	}
}
