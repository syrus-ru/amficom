/*
 * $Id: TestKIS.java,v 1.3 2005/08/30 19:58:39 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;

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

	public void testCreateAll() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MCM_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final MCM mcm = (MCM) it.next();

		ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_CODE);
		final Set<Equipment> equipments = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final Equipment equipment : equipments) {
			final String equipmentDescription = equipment.getDescription();
			final int p = equipmentDescription.indexOf(SEPARATOR);
			final int n = Integer.parseInt(equipmentDescription.substring(p + 1));
			final String kisDescription = ObjectEntities.KIS + SEPARATOR + n
					+ SEPARATOR
					+ equipmentDescription;
			final String hostname = "rtu-" + n;
			KIS.createInstance(DatabaseCommonTest.getSysUser().getId(),
					domain.getId(),
					"Рефлектометр " + n,
					kisDescription,
					hostname,
					(short) 7501,
					equipment.getId(),
					mcm.getId());
		}

		StorableObjectPool.flush(ObjectEntities.KIS_CODE, DatabaseCommonTest.getSysUser().getId(), false);
	}

	public void testGetByCondition() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.KIS_CODE);
		final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final KIS kis : kiss) {
			System.out.println("KIS: '" + kis.getId() + "', '" + kis.getDescription() + "'");
		}
	}
}
