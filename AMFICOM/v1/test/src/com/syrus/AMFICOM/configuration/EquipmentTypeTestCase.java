/*
 * $Id: EquipmentTypeTestCase.java,v 1.1.1.1 2004/11/09 16:00:24 cvsadmin Exp $
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
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2004/11/09 16:00:24 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class EquipmentTypeTestCase extends ConfigureTestCase {

	public EquipmentTypeTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = EquipmentTypeTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(EquipmentTypeTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException {
		EquipmentTypeDatabase equipmentTypeDatabase = (EquipmentTypeDatabase) ConfigurationDatabaseContext
				.getEquipmentTypeDatabase();
		List list = equipmentTypeDatabase.retrieveAll();
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE);
		EquipmentType eqType = EquipmentType.createInstance(id, ConfigureTestCase.creatorId, "testCaseEqType",
									"equipmentType created by EquipmentTypeTestCase", "equipmentType" );
		EquipmentType eqType2 = EquipmentType.getInstance((EquipmentType_Transferable) eqType.getTransferable());

		assertEquals(eqType.getId(), eqType2.getId());

		EquipmentType eqType3 = new EquipmentType(eqType2.getId());

		assertEquals(eqType2.getId(), eqType3.getId());

		if (list.size() > 3)
			equipmentTypeDatabase.delete(eqType);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		EquipmentTypeDatabase equipmentTypeDatabase = (EquipmentTypeDatabase) ConfigurationDatabaseContext
				.getEquipmentTypeDatabase();
		List list = equipmentTypeDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			EquipmentType eqType = (EquipmentType) it.next();
			EquipmentType eqType2 = new EquipmentType(eqType.getId());
			assertEquals(eqType.getId(), eqType2.getId());
		}
	}

}