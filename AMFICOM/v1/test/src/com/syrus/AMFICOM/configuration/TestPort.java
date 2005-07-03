/*
 * $Id: TestPort.java,v 1.1 2005/06/30 16:14:17 arseniy Exp $
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

public final class TestPort extends TestCase {

	public TestPort(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestPort.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.PORT_TYPE_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final PortType portType = (PortType) it.next();

		ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Equipment equipment = (Equipment) it.next();

		Port.createInstance(DatabaseCommonTest.getSysUser().getId(), portType, "port 1", equipment.getId());
		Port.createInstance(DatabaseCommonTest.getSysUser().getId(), portType, "port 2", equipment.getId());
		StorableObjectPool.flush(ObjectEntities.PORT_CODE, false);
	}
}
