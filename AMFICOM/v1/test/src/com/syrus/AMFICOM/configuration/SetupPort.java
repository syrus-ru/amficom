/*
 * $Id: SetupPort.java,v 1.1.2.1 2006/03/06 19:02:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.configuration.PortTypeCodename.FC_PC;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

public final class SetupPort extends TestCase {
	private static final int PORT_NUMBER = 8;

	public SetupPort(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupPort.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final TypicalCondition portTypeCondition = new TypicalCondition(FC_PC.stringValue(),
				OPERATION_EQUALS,
				PORT_TYPE_CODE,
				COLUMN_CODENAME);
		final Set<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(portTypeCondition, true);
		assert portTypes.size() == 1 : ONLY_ONE_EXPECTED;
		final PortType portType = portTypes.iterator().next();

		final EquivalentCondition equipmentCondition = new EquivalentCondition(EQUIPMENT_CODE);
		final Set<Equipment> equipments = StorableObjectPool.getStorableObjectsByCondition(equipmentCondition, true);
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

		final Set<Port> ports = new HashSet<Port>();
		for (final Equipment equipment : equipments) {
			final String equipmentDescription = equipment.getDescription();
			for (int i = 1; i <= PORT_NUMBER; i++) {
				final String portDescription = PORT + SEPARATOR + i + SEPARATOR + equipmentDescription;
				ports.add(Port.createInstance(userId, portType, portDescription, equipment.getId()));
			}
		}

		StorableObjectPool.flush(ports, userId, false);
	}
}
