/*
 * $Id: TestEquipment.java,v 1.1 2005/06/30 16:14:17 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

public final class TestEquipment extends TestCase {

	public TestEquipment(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestEquipment.class);
		return commonTest.createTestSetup();
	}

	public void _testCreateInstance() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final EquipmentType equipmentType = (EquipmentType) it.next();

		ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		final Equipment equipment = Equipment.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				equipmentType,
				"Рефлектометр",
				"Рефлектометр QP1640",
				Identifier.VOID_IDENTIFIER,
				"Nettest",
				"Nettest",
				0,
				90,
				"1111",
				"2222",
				"3333",
				"4444",
				"5555");

		StorableObjectPool.flush(equipment, false);
	}

	public void testUpdate() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_CODE);
		final Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Equipment equipment = (Equipment) it.next();
		equipment.setDescription("Рефлектометр QP1640A");
		StorableObjectPool.flush(equipment, false);
	}
}
