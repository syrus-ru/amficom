/*
 * $Id: SetupKIS.java,v 1.1.2.4 2006/03/27 09:37:56 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_HOSTNAME;

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
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

public final class SetupKIS extends TestCase {

	public SetupKIS(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTest(new SetupKIS("testSwitchOnService"));
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

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
			kiss.add(KIS.createInstance(creatorId,
					domain.getId(),
					"Рефлектометр " + n,
					kisDescription,
					hostname,
					(short) 7501,
					equipment.getId(),
					mcm.getId()));
		}

		StorableObjectPool.flush(kiss, creatorId, false);
	}

	public void testSwitchOnService() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();

		final String kisHostName = "rtu-3";
		final StorableObjectCondition kisCondition = new TypicalCondition(kisHostName, OPERATION_EQUALS, KIS_CODE, COLUMN_HOSTNAME);
		final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(kisCondition, true);
		if (kiss.isEmpty()) {
			throw new ObjectNotFoundException("KIS for hostname " + kisHostName + " not found");
		}
		assertTrue(ONLY_ONE_EXPECTED + ": " + kiss, kiss.size() == 1);
		final KIS kis = kiss.iterator().next();
		System.out.println("KIS: " + kis.getDescription() + ", " + kis.getHostName() + ":" + kis.getTCPPort() + ", on service: " + kis.isOnService());

		if (!kis.isOnService()) {
			kis.setOnService(true);
		}

		StorableObjectPool.flush(kis, creatorId, false);
	}
}
