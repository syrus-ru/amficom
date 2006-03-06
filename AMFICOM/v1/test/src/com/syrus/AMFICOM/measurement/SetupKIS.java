/*
 * $Id: SetupKIS.java,v 1.1.2.1 2006/03/06 19:02:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;

public final class SetupKIS extends TestCase {

	public SetupKIS(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupKIS.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final EquivalentCondition domainCondition = new EquivalentCondition(DOMAIN_CODE);
		final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(domainCondition, true);
		if (domains.isEmpty()) {
			throw new ObjectNotFoundException("Cannot find Domains");
		}
		final Domain domain = domains.iterator().next();

		final EquivalentCondition mcmCondition = new EquivalentCondition(MCM_CODE);
		final Set<MCM> mcms = StorableObjectPool.getStorableObjectsByCondition(mcmCondition, true);
		if (mcms.isEmpty()) {
			throw new ObjectNotFoundException("Cannot find MCMs");
		}
		final MCM mcm = mcms.iterator().next();

		final EquivalentCondition equipmentCondition = new EquivalentCondition(EQUIPMENT_CODE);
		final Set<Equipment> equipments = StorableObjectPool.getStorableObjectsByCondition(equipmentCondition, true);
		if (equipments.isEmpty()) {
			throw new ObjectNotFoundException("Cannot find Equipments");
		}

		final Set<KIS> kiss = new HashSet<KIS>();
		for (final Equipment equipment : equipments) {
			final String equipmentDescription = equipment.getDescription();
			final int p = equipmentDescription.indexOf(SEPARATOR);
			final int n = Integer.parseInt(equipmentDescription.substring(p + 1));
			final String kisDescription = ObjectEntities.KIS + SEPARATOR + n + SEPARATOR + equipmentDescription;
			final String hostname = "rtu-" + n;
			kiss.add(KIS.createInstance(userId,
					domain.getId(),
					"Рефлектометр " + n,
					kisDescription,
					hostname,
					(short) 7501,
					equipment.getId(),
					mcm.getId()));
		}

		StorableObjectPool.flush(kiss, userId, false);
	}
}
