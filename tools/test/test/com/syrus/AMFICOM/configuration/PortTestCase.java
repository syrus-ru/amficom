/*
 * $Id: PortTestCase.java,v 1.1 2004/08/13 14:13:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

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
 * @version $Revision: 1.1 $, $Date: 2004/08/13 14:13:04 $
 * @author $Author: bob $
 * @module tools
 */
public class PortTestCase extends ConfigureTestCase {

	public PortTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		junit.awtui.TestRunner.run(PortTestCase.class);
		//		junit.swingui.TestRunner.run(TransmissionPathTestCase.class);
		//		junit.textui.TestRunner.run(TransmissionPathTestCase.class);
	}

	public static Test suite() {
		return _suite(PortTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.PORT_ENTITY_CODE);

		List portTypeList = PortTypeDatabase.retrieveAll();

		PortType type = (PortType) portTypeList.get(0);

		Equipment equipment = (Equipment) EquipmentDatabase.retrieveAll().get(0);

		Port port = Port.createInstance(id, ConfigureTestCase.creatorId, type, "testCasePort", equipment.getId(),
										PortSort._PORT_SORT_PORT);

		Port port2 = new Port((Port_Transferable) port.getTransferable());
		Port port3 = new Port(port2.getId());
		// PortTypeDatabase.delete(portType);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		List list = PortDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Port port = (Port) it.next();
			Port port2 = new Port(port.getId());

		}
	}

}