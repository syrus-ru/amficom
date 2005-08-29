/*
 * $Id: TestKIS.java,v 1.2 2005/08/29 11:32:55 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

public final class TestKIS extends TestCase {

	public TestKIS(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTest( new TestKIS("testGetByCondition"));
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Equipment equipment = (Equipment) it.next();

		ec = new EquivalentCondition(ObjectEntities.MCM_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final MCM mcm = (MCM) it.next();

		ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		final KIS kis = KIS.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				"Рефлектометр",
				"Рефлектометр QP1640MR",
				"rtu-1",
				(short) 7501,
				equipment.getId(),
				mcm.getId());
		StorableObjectPool.flush(kis, DatabaseCommonTest.getSysUser().getId(), false);
	}

	public void testGetByCondition() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.KIS_CODE);
		final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final KIS kis : kiss) {
			System.out.println("KIS: '" + kis.getId() + "', '" + kis.getDescription() + "'");
		}
	}
}
