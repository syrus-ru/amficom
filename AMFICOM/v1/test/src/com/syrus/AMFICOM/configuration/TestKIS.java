/*
 * $Id: TestKIS.java,v 1.4 2005/08/20 19:40:40 arseniy Exp $
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
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.KIS;

public final class TestKIS extends TestCase {

	public TestKIS(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestKIS.class);
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
}
