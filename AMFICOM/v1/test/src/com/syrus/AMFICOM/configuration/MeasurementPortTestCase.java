/*
 * $Id: MeasurementPortTestCase.java,v 1.2 2005/06/02 14:31:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/02 14:31:02 $
 * @author $Author: arseniy $
 * @module tools
 */
public class MeasurementPortTestCase extends ConfigureTestCase {

	public MeasurementPortTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = MeasurementPortTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(MeasurementPortTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		PortDatabase portDatabase = (PortDatabase) ConfigurationDatabaseContext.getPortDatabase();

		MeasurementPortDatabase measurementPortDatabase = (MeasurementPortDatabase) ConfigurationDatabaseContext
				.getMeasurementPortDatabase();

		MeasurementPortTypeDatabase measurementPortTypeDatabase = (MeasurementPortTypeDatabase) ConfigurationDatabaseContext
				.getMeasurementPortTypeDatabase();

		KISDatabase kisDatabase = (KISDatabase) ConfigurationDatabaseContext.getKISDatabase();

		List list = measurementPortDatabase.retrieveAll();

		List measurementPortTypeList = measurementPortTypeDatabase.retrieveAll();

		if (measurementPortTypeList.isEmpty())
			fail("must be at less one measurement port type at db");

		List kisList = kisDatabase.retrieveAll();

		if (kisList.isEmpty())
			fail("must be at less one kis at db");

		KIS kis = (KIS) kisList.get(0);

		List portList = portDatabase.retrieveAll();

		if (portList.isEmpty())
			fail("must be at less one port at db");

		Port port = (Port) portList.get(0);

		MeasurementPortType type = (MeasurementPortType) measurementPortTypeList.get(0);

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);

		MeasurementPort measurementPort = MeasurementPort
				.createInstance(id, creatorId, type, "testCaseMeasurementPort",
						"created by MeasurementPortTestCase", kis.getId(), port.getId());

		MeasurementPort measurementPort2 = MeasurementPort
				.getInstance((MeasurementPort_Transferable) measurementPort.getTransferable());

		assertEquals(measurementPort.getId(), measurementPort2.getId());

		MeasurementPort measurementPort3 = new MeasurementPort(measurementPort2.getId());

		assertEquals(measurementPort2.getId(), measurementPort3.getId());

		if (!list.isEmpty())
			measurementPortDatabase.delete(measurementPort);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		MeasurementPortDatabase measurementPortDatabase = (MeasurementPortDatabase) ConfigurationDatabaseContext
				.getMeasurementPortDatabase();

		List list = measurementPortDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			MeasurementPort measurementPort = (MeasurementPort) it.next();
			MeasurementPort measurementPort2 = new MeasurementPort(measurementPort.getId());
			assertEquals(measurementPort.getId(), measurementPort2.getId());
		}
	}

}