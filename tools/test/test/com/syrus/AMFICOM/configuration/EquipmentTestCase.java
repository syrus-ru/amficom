/*
 * $Id: EquipmentTestCase.java,v 1.4 2004/08/31 15:29:12 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/31 15:29:12 $
 * @author $Author: bob $
 * @module tools
 */
public class EquipmentTestCase extends ConfigureTestCase {

	public EquipmentTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = EquipmentTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(EquipmentTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		EquipmentDatabase equipmentDatabase = (EquipmentDatabase) ConfigurationDatabaseContext
				.getEquipmentDatabase();

		EquipmentTypeDatabase equipmentTypeDatabase = (EquipmentTypeDatabase) ConfigurationDatabaseContext
				.getEquipmentTypeDatabase();

		KISDatabase kisDatabase = (KISDatabase) ConfigurationDatabaseContext.getKISDatabase();

		List list = equipmentDatabase.retrieveAll();

		List eqTypelist = equipmentTypeDatabase.retrieveAll();

		if (eqTypelist.isEmpty())
			fail("must be at less one equipment type at db");

		EquipmentType eqType = (EquipmentType) eqTypelist.get(0);

		List kislist = kisDatabase.retrieveAll();

		if (kislist.isEmpty())
			fail("must be at less one kis at db");

		KIS kis = (KIS) kislist.get(0);

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.EQUIPMENT_ENTITY_CODE);

		Equipment eq = Equipment.createInstance(id, creatorId, domainId, eqType, "testCaseEquipment",
							"equipment created by EquipmentTestCase",
							new Identifier("Image_1"));

		Equipment eq2 = Equipment.getInstance((Equipment_Transferable) eq.getTransferable());

		assertEquals(eq.getId(), eq2.getId());

		Equipment eq3 = new Equipment(eq2.getId());

		assertEquals(eq2.getId(), eq3.getId());

		if (!list.isEmpty())
			equipmentDatabase.delete(eq);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		EquipmentDatabase equipmentDatabase = (EquipmentDatabase) ConfigurationDatabaseContext
				.getEquipmentDatabase();
		List list = equipmentDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Equipment eq = (Equipment) it.next();
			Equipment eq2 = new Equipment(eq.getId());
			assertEquals(eq.getId(), eq2.getId());
		}
	}

}