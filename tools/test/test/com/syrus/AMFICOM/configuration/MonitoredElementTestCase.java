/*
 * $Id: MonitoredElementTestCase.java,v 1.1 2004/08/16 09:03:21 bob Exp $
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
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
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
public class MonitoredElementTestCase extends ConfigureTestCase {

	public MonitoredElementTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = MonitoredElementTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(MonitoredElementTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		List list = MonitoredElementDatabase.retrieveAll();

		List measurementPortList = MeasurementPortDatabase.retrieveAll();

		if (measurementPortList.isEmpty())
			fail("must be at less one measurement port at db");

		MeasurementPort meaurementPort = (MeasurementPort) measurementPortList.get(0);

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.ME_ENTITY_CODE);
		
		MonitoredElement me = MonitoredElement.createInstance(id, creatorId, domainId, meaurementPort.getId(), MonitoredElementSort._MONITOREDELEMENT_SORT_PORT, "testCaseAddress" );

		MonitoredElement me2 = new MonitoredElement((MonitoredElement_Transferable) me
				.getTransferable());

		assertEquals(me.getId(), me2.getId());

		MonitoredElement me3 = new MonitoredElement(me2.getId());

		assertEquals(me2.getId(), me3.getId());

		if (!list.isEmpty())
			MonitoredElementDatabase.delete(me);

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