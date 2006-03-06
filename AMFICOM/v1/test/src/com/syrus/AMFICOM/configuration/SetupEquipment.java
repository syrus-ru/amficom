/*
 * $Id: SetupEquipment.java,v 1.1.2.1 2006/03/06 19:02:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.configuration.EquipmentTypeCodename.REFLECTOMETER;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;

public final class SetupEquipment extends TestCase {

	public SetupEquipment(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupEquipment.class);
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
		final Identifier domainId = domain.getId();

		final TypicalCondition equipmentTypeCondition = new TypicalCondition(REFLECTOMETER.stringValue(),
				OPERATION_EQUALS,
				EQUIPMENT_TYPE_CODE,
				COLUMN_CODENAME);
		final Set<EquipmentType> equipmentTypes = StorableObjectPool.getStorableObjectsByCondition(equipmentTypeCondition, true);
		assert equipmentTypes.size() == 1 : ONLY_ONE_EXPECTED;
		final LinkedIdsCondition protoEquipmentCondition = new LinkedIdsCondition(equipmentTypes, PROTOEQUIPMENT_CODE);
		final Set<ProtoEquipment> protoEquipments = StorableObjectPool.getStorableObjectsByCondition(protoEquipmentCondition, true);
		if (protoEquipments.isEmpty()) {
			throw new ObjectNotFoundException("Cannot find ProtoEquipments");
		}

		final Set<Equipment> equipments = new HashSet<Equipment>();
		int i = 0;
		for (final ProtoEquipment protoEquipment : protoEquipments) {
			i++;
			equipments.add(Equipment.createInstance(userId,
					domainId,
					protoEquipment.getId(),
					"Рефлектометр " + i,
					EQUIPMENT + SEPARATOR + i,
					VOID_IDENTIFIER,
					"Nettest",
					"Nettest",
					(float) 55.750,
					(float) 37.583,
					"1111",
					"2222",
					"3333",
					"4444",
					"5555"));
		}

		StorableObjectPool.flush(equipments, userId, false);
	}
}
