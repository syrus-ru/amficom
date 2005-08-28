/*
 * $Id: TestMonitoredElement.java,v 1.2 2005/08/28 16:43:51 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/28 16:43:51 $
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

	public void testCreateInstance() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final MeasurementPort measurementPort = (MeasurementPort) it.next();

		ec.setEntityCode(ObjectEntities.DOMAIN_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		final String localAddress = "2:0:1";

		ec.setEntityCode(ObjectEntities.TRANSMISSIONPATH_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final TransmissionPath transmissionPath = (TransmissionPath) it.next();

		MonitoredElement.createInstance(DatabaseCommonTest.getSysUser().getId(),
				domain.getId(),
				"monitored element",
				measurementPort.getId(),
				MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
				localAddress,
				Collections.singleton(transmissionPath.getId()));

		StorableObjectPool.flush(ObjectEntities.MONITOREDELEMENT_CODE, DatabaseCommonTest.getSysUser().getId(), false);
	}


}
