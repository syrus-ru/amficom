/*
 * $Id: TestMonitoredElement.java,v 1.6 2005/06/28 15:28:24 arseniy Exp $
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
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.configuration.corba.IdlMonitoredElement;
import com.syrus.AMFICOM.configuration.corba.IdlMonitoredElementPackage.MonitoredElementSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/28 15:28:24 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class TestMonitoredElement extends TestCase {

	public TestMonitoredElement(String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestMonitoredElement.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final MeasurementPort measurementPort = (MeasurementPort) it.next();

		ec.setEntityCode(ObjectEntities.DOMAIN_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		String localAddress = "SW=01:06";

		ec.setEntityCode(ObjectEntities.TRANSPATH_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final TransmissionPath transmissionPath = (TransmissionPath) it.next();

		MonitoredElement monitoredElement = MonitoredElement.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				"monitored element",
				measurementPort.getId(),
				MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
				localAddress,
				Collections.singleton(transmissionPath.getId()));

		this.checkMonitoredElement(monitoredElement);

		StorableObjectPool.flush(ObjectEntities.MONITOREDELEMENT_CODE, false);
	}

	private void checkMonitoredElement(MonitoredElement monitoredElement) {
		IdlMonitoredElement met = monitoredElement.getTransferable();

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


}
