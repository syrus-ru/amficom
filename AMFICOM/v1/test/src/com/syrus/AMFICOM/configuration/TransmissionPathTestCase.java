/*
 * $Id: TransmissionPathTestCase.java,v 1.1 2004/11/09 16:00:25 cvsadmin Exp $
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
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.1 $, $Date: 2004/11/09 16:00:25 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class TransmissionPathTestCase extends ConfigureTestCase {

	public TransmissionPathTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = TransmissionPathTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(TransmissionPathTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		TransmissionPathDatabase transmissionPathDatabase = (TransmissionPathDatabase) ConfigurationDatabaseContext
				.getTransmissionPathDatabase();
		PortDatabase portDatabase = (PortDatabase) ConfigurationDatabaseContext.getPortDatabase();

		List list = transmissionPathDatabase.retrieveAll();

		List portList = portDatabase.retrieveAll();

		if (portList.isEmpty())
			fail("must be at less one port at db");

		Port port = (Port) portList.get(0);

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TRANSPATH_ENTITY_CODE);

		TransmissionPath transmissionPath = TransmissionPath
				.createInstance(id, creatorId, domainId, "testCaseTransmissionPath",
						" created by TransmissionPathTestCase", port.getId(), port.getId());

		TransmissionPath transmissionPath2 = TransmissionPath
				.getInstance((TransmissionPath_Transferable) transmissionPath.getTransferable());

		assertEquals(transmissionPath.getId(), transmissionPath2.getId());

		TransmissionPath transmissionPath3 = new TransmissionPath(transmissionPath2.getId());

		assertEquals(transmissionPath2.getId(), transmissionPath3.getId());

		if (!list.isEmpty())
			transmissionPathDatabase.delete(transmissionPath);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		TransmissionPathDatabase transmissionPathDatabase = (TransmissionPathDatabase) ConfigurationDatabaseContext
				.getTransmissionPathDatabase();
		List list = transmissionPathDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			TransmissionPath transmissionPath = (TransmissionPath) it.next();
			TransmissionPath transmissionPath2 = new TransmissionPath(transmissionPath.getId());
			assertEquals(transmissionPath.getId(), transmissionPath2.getId());
		}
	}

}