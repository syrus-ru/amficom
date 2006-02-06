/*
 * $Id: TestMonitoredElement.java,v 1.4 2005/10/25 10:42:25 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;

/**
 * @version $Revision: 1.4 $, $Date: 2005/10/25 10:42:25 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class TestMonitoredElement extends TestCase {

	public TestMonitoredElement(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestMonitoredElement.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws ApplicationException {

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		final Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		ec.setEntityCode(ObjectEntities.MEASUREMENTPORT_CODE);
		final Set<MeasurementPort> measurementPorts = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		final Map<Identifier, Identifier> portIdMeasurementPortIdMap = new HashMap<Identifier, Identifier>();
		for (final MeasurementPort measurementPort : measurementPorts) {
			portIdMeasurementPortIdMap.put(measurementPort.getPortId(), measurementPort.getId());
		}

		ec.setEntityCode(ObjectEntities.TRANSMISSIONPATH_CODE);
		final Set<TransmissionPath> transmissionPaths = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final TransmissionPath transmissionPath : transmissionPaths) {
			final Identifier startPortId = transmissionPath.getStartPortId();
			final Identifier measurementPortId = portIdMeasurementPortIdMap.get(startPortId);

			final String transmissionPathDescription = transmissionPath.getDescription();
			final int p1 = transmissionPathDescription.indexOf(SEPARATOR);
			final int p2 = transmissionPathDescription.indexOf(SEPARATOR, p1 + 1);
			final int n = Integer.parseInt(transmissionPathDescription.substring(p1 + 1, p2));
			final String monitoredElementName = MONITOREDELEMENT + SEPARATOR + n
					+ SEPARATOR
					+ transmissionPathDescription;
			final String localAddress = "2:0:" + n;
			MonitoredElement.createInstance(DatabaseCommonTest.getSysUser().getId(),
					domain.getId(),
					monitoredElementName,
					measurementPortId,
					MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
					localAddress,
					Collections.singleton(transmissionPath.getId()));
		}

		

		StorableObjectPool.flush(ObjectEntities.MONITOREDELEMENT_CODE, DatabaseCommonTest.getSysUser().getId(), false);
	}


}
