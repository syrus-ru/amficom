/*
 * $Id: PortTestCase.java,v 1.1.1.1 2004/11/09 16:00:25 cvsadmin Exp $
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
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2004/11/09 16:00:25 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class PortTestCase extends ConfigureTestCase {

	public PortTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = PortTestCase.class;
		junit.awtui.TestRunner.run(clazz);
//		junit.swingui.TestRunner.run(clazz);
//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(PortTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		PortDatabase portDatabase = (PortDatabase) ConfigurationDatabaseContext.getPortDatabase();

		PortTypeDatabase portTypeDatabase = (PortTypeDatabase) ConfigurationDatabaseContext.getPortDatabase();
		
		EquipmentDatabase equipmentDatabase = (EquipmentDatabase) ConfigurationDatabaseContext.getEquipmentDatabase();
		
		List list = portDatabase.retrieveAll();

		List portTypeList = portTypeDatabase.retrieveAll();
		
		if (portTypeList.isEmpty())
			fail("must be at less one port type at db");
		
		List equipmentList = equipmentDatabase.retrieveAll();
		
		if (equipmentList.isEmpty())
			fail("must be at less one equipment at db");

		PortType type = (PortType) portTypeList.get(0);

		Equipment equipment = (Equipment) equipmentList.get(0);

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.PORT_ENTITY_CODE);

		Port port = Port.createInstance(id, ConfigureTestCase.creatorId, type, "testCasePort", equipment.getId(),
										PortSort.PORT_SORT_PORT);

		Port port2 = Port.getInstance((Port_Transferable) port.getTransferable());

		assertEquals(port.getId(), port2.getId());

		Port port3 = new Port(port2.getId());

		assertEquals(port2.getId(), port3.getId());

		if (!list.isEmpty())
			portDatabase.delete(port);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		PortDatabase portDatabase = (PortDatabase) ConfigurationDatabaseContext.getPortDatabase();

		List list = portDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Port port = (Port) it.next();
			Port port2 = new Port(port.getId());
			assertEquals(port.getId(), port2.getId());
		}
	}

}