/*
 * $Id: TestMonitoredElement.java,v 1.1 2005/02/16 21:25:52 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Iterator;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SessionContext;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/16 21:25:52 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class TestMonitoredElement extends CommonConfigurationTest {

	public TestMonitoredElement(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestMonitoredElement.class);
	}

	public void testCreateInstance() throws ApplicationException {
		AccessIdentity accessIdentity = SessionContext.getAccessIdentity();

		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
		Iterator it = ConfigurationStorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		MeasurementPort measurementPort = (MeasurementPort) it.next();

		String localAddress = "SW=01:06";

		ec = new EquivalentCondition(ObjectEntities.TRANSPATH_ENTITY_CODE);
		it = ConfigurationStorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		TransmissionPath transmissionPath = (TransmissionPath) it.next();

		MonitoredElement monitoredElement = MonitoredElement.createInstance(accessIdentity.getUserId(),
				accessIdentity.getDomainId(),
				"monitored element",
				measurementPort.getId(),
				MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
				localAddress,
				Collections.singleton(transmissionPath.getId()));

		MonitoredElement_Transferable met = (MonitoredElement_Transferable) monitoredElement.getTransferable();

		MonitoredElement monitoredElement1 = new MonitoredElement(met);
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

		ConfigurationStorableObjectPool.putStorableObject(monitoredElement);
		ConfigurationStorableObjectPool.flush(true);
	}

}
