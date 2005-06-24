/*
 * $Id: TestPortType.java,v 1.1 2005/06/24 16:09:15 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

public final class TestPortType extends TestCase {
	
	public TestPortType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestPortType.class);
		return commonTest.createTestSetup();
	}

	public void _testCreateInstance() throws ApplicationException {
		final PortType portType = PortType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				PortTypeCodename.FC_PC.stringValue(),
				"FC/PC",
				"FC/PC",
				PortTypeSort.PORTTYPESORT_OPTICAL);
		StorableObjectPool.flush(ObjectEntities.PORT_TYPE_CODE, false);
	}

	public void testAddCharacteristic() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.PORT_TYPE_CODE);
		Set set = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		final PortType portType = (PortType) set.iterator().next();
		System.out.println("Port type: " + portType.getCodename() + ", " + portType.getDescription());

		ec.setEntityCode(ObjectEntities.CHARACTERISTIC_TYPE_CODE);
		set = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		final CharacteristicType characteristicType = (CharacteristicType) set.iterator().next();

		final Characteristic characteristic = Characteristic.createInstance(DatabaseCommonTest.getSysUser().getId(),
				characteristicType,
				"Some characteristic",
				"Ne znayu",
				"mnogo",
				portType,
				true,
				true);

		StorableObjectPool.flush(ObjectEntities.PORT_TYPE_CODE, false);
	}
}
