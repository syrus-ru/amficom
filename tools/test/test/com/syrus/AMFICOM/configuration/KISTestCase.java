/*
 * $Id: KISTestCase.java,v 1.4 2004/08/31 15:29:12 bob Exp $
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
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
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
public class KISTestCase extends ConfigureTestCase {

	public KISTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = KISTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		EquipmentDatabase equipmentDatabase = (EquipmentDatabase) ConfigurationDatabaseContext
				.getEquipmentDatabase();

		KISDatabase kisDatabase = (KISDatabase) ConfigurationDatabaseContext.getKISDatabase();

		List list = kisDatabase.retrieveAll();

		List eqList = equipmentDatabase.retrieveAll();

		if (eqList.isEmpty())
			fail("must be at less one equipment at db");

		MCM mcm = new MCM(new Identifier("MCM_2"));

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.KIS_ENTITY_CODE);
		KIS kis = KIS.createInstance(id, ConfigureTestCase.creatorId, ConfigureTestCase.domainId,
						"testCaseKIS", "kis  created by KISTestCase ", ((Equipment) eqList
								.get(0)).getId(), mcm.getId());

		KIS kis2 = KIS.getInstance((KIS_Transferable) kis.getTransferable());

		assertEquals(kis.getId(), kis2.getId());

		KIS kis3 = new KIS(kis2.getId());

		assertEquals(kis2.getId(), kis3.getId());

		if (!list.isEmpty())
			kisDatabase.delete(kis);

	}

	public static Test suite() {
		return suiteWrapper(KISTestCase.class);
	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		KISDatabase kisDatabase = (KISDatabase) ConfigurationDatabaseContext.getKISDatabase();

		List list = kisDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			KIS kis = (KIS) it.next();
			KIS kis2 = new KIS(kis.getId());
			assertEquals(kis.getId(), kis2.getId());
		}
	}

}