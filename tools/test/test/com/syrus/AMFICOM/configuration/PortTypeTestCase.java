/*
 * $Id: PortTypeTestCase.java,v 1.1 2004/08/13 14:13:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
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
public class PortTypeTestCase extends ConfigureTestCase {


	public PortTypeTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		junit.awtui.TestRunner.run(PortTypeTestCase.class);
		//		junit.swingui.TestRunner.run(TransmissionPathTestCase.class);
		//		junit.textui.TestRunner.run(TransmissionPathTestCase.class);
	}
	
	public static Test suite() {
		return _suite(PortTypeTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.PORTTYPE_ENTITY_CODE);
		PortType portType = PortType.createInstance(id, ConfigureTestCase.creatorId, "testCasePortType",
													"portType created by PortTypeTestCase");
		PortType portType2 = new PortType((PortType_Transferable) portType.getTransferable());
		PortType portType3 = new PortType(portType2.getId());
		PortTypeDatabase.delete(portType);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		List list = PortTypeDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			PortType portType = (PortType) it.next();
			PortType portType2 = new PortType(portType.getId());

		}
	}

}