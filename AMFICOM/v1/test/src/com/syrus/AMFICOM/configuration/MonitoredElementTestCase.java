/*
 * $Id: MonitoredElementTestCase.java,v 1.2 2005/06/02 14:31:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

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
 * @version $Revision: 1.2 $, $Date: 2005/06/02 14:31:02 $
 * @author $Author: arseniy $
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

		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();
		MeasurementPortDatabase measurementPortDatabase = (MeasurementPortDatabase) ConfigurationDatabaseContext
				.getMeasurementPortDatabase();
		List list = monitoredElementDatabase.retrieveAll();

		List measurementPortList = measurementPortDatabase.retrieveAll();

		if (measurementPortList.isEmpty())
			fail("must be at less one measurement port at db");

		List monitoredDomainMemberIds = new ArrayList();

		MeasurementPort meaurementPort = (MeasurementPort) measurementPortList.get(0);

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.ME_ENTITY_CODE);

		MonitoredElement me = MonitoredElement
				.createInstance(id, creatorId, domainId, "MonitoredElement-1", 
						meaurementPort.getId(),								
						MonitoredElementSort._MONITOREDELEMENT_SORT_PORT, "testCaseAddress",
						monitoredDomainMemberIds);

		MonitoredElement me2 = MonitoredElement.getInstance((MonitoredElement_Transferable) me
				.getTransferable());

		assertEquals(me.getId(), me2.getId());

		MonitoredElement me3 = new MonitoredElement(me2.getId());

		assertEquals(me2.getId(), me3.getId());

		//		if (!list.isEmpty())
		//			monitoredElementDatabase.delete(me);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();
		List list = monitoredElementDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			MeasurementPort measurementPort = (MeasurementPort) it.next();
			MeasurementPort measurementPort2 = new MeasurementPort(measurementPort.getId());
			assertEquals(measurementPort.getId(), measurementPort2.getId());
		}
	}

}