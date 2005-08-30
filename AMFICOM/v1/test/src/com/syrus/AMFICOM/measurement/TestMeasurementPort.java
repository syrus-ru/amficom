/*
 * $Id: TestMeasurementPort.java,v 1.2 2005/08/30 19:58:39 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

public final class TestMeasurementPort extends TestCase {

	public TestMeasurementPort(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestMeasurementPort.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final MeasurementPortType measurementPortType = (MeasurementPortType) it.next();

		ec = new EquivalentCondition(ObjectEntities.KIS_CODE);
		final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		final Map<Identifier, KIS> portKISMap = new HashMap<Identifier, KIS>();
		for (final KIS kis : kiss) {
			final Equipment equipment = StorableObjectPool.getStorableObject(kis.getEquipmentId(), true);
			for (final Port port : equipment.getPorts()) {
				portKISMap.put(port.getId(), kis);
			}
		}

		ec = new EquivalentCondition(ObjectEntities.PORT_CODE);
		final Set<Port> ports = StorableObjectPool.getStorableObjectsByCondition(ec, true);

		for (final Port port : ports) {
			final String portDescription = port.getDescription();
			if (portDescription.equals("finish")) {
				continue;
			}

			final KIS kis = portKISMap.get(port.getId());

			final int p1 = portDescription.indexOf(SEPARATOR);
			final int p2 = portDescription.indexOf(SEPARATOR, p1 + 1);
			final int n = Integer.parseInt(portDescription.substring(p1 + 1, p2));
			final String measurementPortDescription = MEASUREMENTPORT + SEPARATOR + n
					+ SEPARATOR
					+ portDescription;
			MeasurementPort.createInstance(DatabaseCommonTest.getSysUser().getId(),
					measurementPortType,
					"Порт " + n,
					measurementPortDescription,
					kis.getId(),
					port.getId());
		}
		
		StorableObjectPool.flush(ObjectEntities.MEASUREMENTPORT_CODE, DatabaseCommonTest.getSysUser().getId(), true);
	}
}
