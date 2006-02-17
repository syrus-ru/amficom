/*
 * $Id: TestPort.java,v 1.3.2.1 2006/02/17 12:28:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

public final class TestPort extends TestCase {

	public TestPort(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestPort.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final Map<String, Integer> portNumberMap = new HashMap<String, Integer>();
		portNumberMap.put(EQUIPMENT + SEPARATOR + "1", new Integer(8));
		portNumberMap.put(EQUIPMENT + SEPARATOR + "2", new Integer(8));
		portNumberMap.put(EQUIPMENT + SEPARATOR + "3", new Integer(4));

		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.PORT_TYPE_CODE);
		final Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final PortType portType = (PortType) it.next();

		ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_CODE);
		final Set<Equipment> equipments = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		final Map<String, Equipment> equipmentsMap = new HashMap<String, Equipment>();
		for (final Equipment equipment : equipments) {
			equipmentsMap.put(equipment.getDescription(), equipment);
		}

		final String equipment1Description = EQUIPMENT + SEPARATOR + "1";
		final Equipment equipment1 = equipmentsMap.get(equipment1Description);
		if (equipment1 != null) {
			Port.createInstance(userId, portType, "finish", equipment1.getId());
		} else {
			fail("Cannot find '" + equipment1Description + "'");
		}

		for (final Equipment equipment : equipments) {
			final String equipmentDescription = equipment.getDescription();
			final Integer portNumberInt = portNumberMap.get(equipmentDescription);
			final int portNumber = (portNumberInt != null) ? portNumberInt.intValue() : 0;
			for (int i = 1; i <= portNumber; i++) {
				final String portDescription = PORT + SEPARATOR + i + SEPARATOR + equipmentDescription;
				Port.createInstance(userId, portType, portDescription, equipment.getId());
			}
		}

		StorableObjectPool.flush(ObjectEntities.PORT_CODE, userId, false);
	}
}
