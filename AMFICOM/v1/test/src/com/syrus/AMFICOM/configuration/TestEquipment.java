/*
 * $Id: TestEquipment.java,v 1.4 2005/08/30 19:58:39 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT;

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

	public void testCreateAll() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_TYPE_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final EquipmentType equipmentType = (EquipmentType) it.next();

		ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		Equipment.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				equipmentType,
				"������������ 1",
				EQUIPMENT + SEPARATOR + "1",
				Identifier.VOID_IDENTIFIER,
				"Nettest",
				"Nettest",
				(float) 55.750,
				(float) 37.583,
				"1111",
				"2222",
				"3333",
				"4444",
				"5555");

		Equipment.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				equipmentType,
				"������������ 2",
				EQUIPMENT + SEPARATOR + "2",
				Identifier.VOID_IDENTIFIER,
				"Nettest",
				"Nettest",
				(float) 39.900,
				(float) 116.413,
				"1111",
				"2222",
				"3333",
				"4444",
				"5555");

		Equipment.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				equipmentType,
				"������������ 3",
				EQUIPMENT + SEPARATOR + "3",
				Identifier.VOID_IDENTIFIER,
				"Nettest",
				"Nettest",
				(float) 28.667,
				(float) 77.217,
				"1111",
				"2222",
				"3333",
				"4444",
				"5555");

		StorableObjectPool.flush(ObjectEntities.EQUIPMENT_CODE, DatabaseCommonTest.getSysUser().getId(), false);
	}
}
