/*
 * $Id: MeasurementPortTestCase.java,v 1.1 2004/08/16 09:03:21 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortDatabase;
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
 * @version $Revision: 1.1 $, $Date: 2004/08/16 09:03:21 $
 * @author $Author: bob $
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

		List list = MeasurementPortDatabase.retrieveAll();

		List measurementPortTypeList = MeasurementPortTypeDatabase.retrieveAll();

		if (measurementPortTypeList.isEmpty())
			fail("must be at less one measurement port type at db");

		List kisList = KISDatabase.retrieveAll();

		if (kisList.isEmpty())
			fail("must be at less one kis at db");

		KIS kis = (KIS) kisList.get(0);

		List portList = PortDatabase.retrieveAll();

		if (portList.isEmpty())
			fail("must be at less one port at db");

		Port port = (Port) portList.get(0);

		MeasurementPortType type = (MeasurementPortType) measurementPortTypeList.get(0);

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);

		MeasurementPort measurementPort = MeasurementPort.createInstance(id, creatorId, type,
																			"testCaseMeasurementPort",
																			"created by MeasurementPortTestCase", kis
																					.getId(), port.getId());

		MeasurementPort measurementPort2 = new MeasurementPort((MeasurementPort_Transferable) measurementPort
				.getTransferable());

		assertEquals(measurementPort.getId(), measurementPort2.getId());

		MeasurementPort measurementPort3 = new MeasurementPort(measurementPort2.getId());

		assertEquals(measurementPort2.getId(), measurementPort3.getId());

		if (!list.isEmpty())
			MeasurementPortDatabase.delete(measurementPort);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		List list = MeasurementPortDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			MeasurementPort measurementPort = (MeasurementPort) it.next();
			MeasurementPort measurementPort2 = new MeasurementPort(measurementPort.getId());
			assertEquals(measurementPort.getId(), measurementPort2.getId());
		}
	}

}