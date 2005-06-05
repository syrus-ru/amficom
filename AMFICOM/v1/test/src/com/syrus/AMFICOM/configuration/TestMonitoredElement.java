/*
 * $Id: TestMonitoredElement.java,v 1.4 2005/06/05 18:40:41 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/05 18:40:41 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class TestMonitoredElement extends CommonTest {

	public TestMonitoredElement(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestMonitoredElement.class);
	}

	public void testCreate() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		MeasurementPort measurementPort = (MeasurementPort) it.next();

		String localAddress = "SW=01:06";

		ec = new EquivalentCondition(ObjectEntities.TRANSPATH_ENTITY_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		TransmissionPath transmissionPath = (TransmissionPath) it.next();

		MonitoredElement monitoredElement = MonitoredElement.createInstance(creatorUser.getId(),
				accessIdentity.getDomainId(),
				"monitored element",
				measurementPort.getId(),
				MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
				localAddress,
				Collections.singleton(transmissionPath.getId()));

		this.checkMonitoredElement(monitoredElement);

		ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_ENTITY_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		Equipment equipment = (Equipment) it.next();

		MonitoredElement monitoredElement1 = MonitoredElement.createInstance(creatorUser.getId(),
				accessIdentity.getDomainId(),
				"monitored element 1",
				measurementPort.getId(),
				MonitoredElementSort.MONITOREDELEMENT_SORT_EQUIPMENT,
				localAddress,
				Collections.singleton(equipment.getId()));

		this.checkMonitoredElement(monitoredElement1);

		StorableObjectPool.flush(ObjectEntities.MONITOREDELEMENT_ENTITY_CODE, false);
	}

	private void checkMonitoredElement(MonitoredElement monitoredElement) {
		MonitoredElement_Transferable met = (MonitoredElement_Transferable) monitoredElement.getTransferable();

		MonitoredElement monitoredElement1 = null;
		try {
			monitoredElement1 = new MonitoredElement(met);
		}
		catch (CreateObjectException e) {
			e.printStackTrace();
		}
		assertEquals(monitoredElement.getId(), monitoredElement1.getId());
		assertEquals(monitoredElement.getCreated(), monitoredElement1.getCreated());
		assertEquals(monitoredElement.getModified(), monitoredElement1.getModified());
		assertEquals(monitoredElement.getCreatorId(), monitoredElement1.getCreatorId());
		assertEquals(monitoredElement.getModifierId(), monitoredElement1.getModifierId());
		assertEquals(monitoredElement.getVersion(), monitoredElement1.getVersion());
		assertEquals(monitoredElement.getDomainId(), monitoredElement1.getDomainId());
		assertEquals(monitoredElement.getName(), monitoredElement1.getName());
		assertEquals(monitoredElement.getMeasurementPortId(), monitoredElement1.getMeasurementPortId());
		assertEquals(monitoredElement.getSort(), monitoredElement1.getSort());
		assertEquals(monitoredElement.getLocalAddress(), monitoredElement1.getLocalAddress());

		Collection monitoredDomainMemberIds = monitoredElement.getMonitoredDomainMemberIds();
		for (Iterator it = monitoredDomainMemberIds.iterator(); it.hasNext();)
			System.out.println("monitored entity: " + ((Identifier) it.next()).toString());
	}

//	public void testUpdate() throws ApplicationException {
//		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.ME_ENTITY_CODE);
//		Collection collection = ConfigurationStorableObjectPool.getStorableObjectsByCondition(ec, true);
//		System.out.println("size: " + collection.size());
//
//		MonitoredElement[] monitoredElements = new MonitoredElement[collection.size()];
//		int j = 0;
//		for (Iterator it = collection.iterator(); it.hasNext(); j++) {
//			monitoredElements[j] = (MonitoredElement) it.next();
//			System.out.println("Monitored element: " + monitoredElements[j].getId() + ", name: '" + monitoredElements[j].getName() + "'");
//		}
//
//		monitoredElements[0].setName("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
//		monitoredElements[1].setName("FFFFFFFFFFFFFF");
//
//		try {
//			ConfigurationStorableObjectPool.flush(false);
//		}
//		finally {
//			for (int i = 0; i < monitoredElements.length; i++)
//				System.out.println("name: '" + monitoredElements[i].getName()
//						+ "' version: " + monitoredElements[i].getVersion()
//						+ ", changed: " + monitoredElements[i].isChanged());
//		}
//	}

//	public void testDelete() throws ApplicationException {
//		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.ME_ENTITY_CODE);
//		Collection monitoredElements = ConfigurationStorableObjectPool.getStorableObjectsByCondition(ec, true);
//		MonitoredElement monitoredElement;
//		for (Iterator it = monitoredElements.iterator(); it.hasNext();) {
//			monitoredElement = (MonitoredElement) it.next();
//			System.out.println("MonitoredElement: " + monitoredElement.getId());
//		}
//		ConfigurationStorableObjectPool.delete(((StorableObject) monitoredElements.iterator().next()).getId());
//		ConfigurationStorableObjectPool.flush(true);
//	}
}
