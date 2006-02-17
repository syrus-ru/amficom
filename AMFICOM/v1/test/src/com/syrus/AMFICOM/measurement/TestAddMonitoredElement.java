/*-
 * $Id: TestAddMonitoredElement.java,v 1.2 2006/02/17 12:04:55 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.2 $, $Date: 2006/02/17 12:04:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestAddMonitoredElement extends TestCase {

	public TestAddMonitoredElement(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestAddMonitoredElement.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws ApplicationException {
		final Identifier sysUserId = LoginManager.getUserId();

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.TRANSMISSIONPATH_CODE);
		final Set<TransmissionPath> tps = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		if (tps.size() == 0) {
			fail("no transmission paths");
		}
		if (tps.size() > 1) {
			fail("transmission paths > 1");
		}
		final TransmissionPath transmissionPath1 = tps.iterator().next();
		final Port startPort1 = transmissionPath1.getStartPort();
		final LinkedIdsCondition lic = new LinkedIdsCondition(startPort1.getId(), ObjectEntities.MEASUREMENTPORT_CODE);
		final Set<MeasurementPort> mps = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		if (mps.size() == 0) {
			fail("no measurement ports");
		}
		if (mps.size() > 1) {
			fail("measurement ports > 1");
		}
		final MeasurementPort measurementPort1 = mps.iterator().next();

		final Port startPort2 = Port.createInstance(sysUserId, startPort1.getType(), "Ещё один", startPort1.getEquipmentId());
		final TransmissionPath transmissionPath2 = TransmissionPath.createInstance(sysUserId,
				transmissionPath1.getDomainId(),
				"Ещё один",
				"Такой же",
				transmissionPath1.getType(),
				startPort2.getId(),
				transmissionPath1.getFinishPortId());
		final MeasurementPort measurementPort2 = MeasurementPort.createInstance(sysUserId,
				measurementPort1.getType(),
				"Ещё один",
				"Такой же",
				measurementPort1.getKISId(),
				startPort2.getId());
		final MonitoredElement monitoredElement2 = MonitoredElement.createInstance(sysUserId,
				transmissionPath1.getDomainId(),
				"monitored element",
				measurementPort2.getId(),
				MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
				"2:0:2",
				Collections.singleton(transmissionPath2.getId()));
		final Set<Identifiable> flushObjects = new HashSet<Identifiable>();
		flushObjects.add(startPort2);
		flushObjects.add(transmissionPath2);
		flushObjects.add(measurementPort2);
		flushObjects.add(monitoredElement2);
		StorableObjectPool.flush(flushObjects, sysUserId, false);
	}
}
