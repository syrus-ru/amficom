/*
 * $Id: SetupMonitoredElement.java,v 1.1.2.2 2006/03/23 14:24:11 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.IdlMonitoredElementKind.MONITOREDELEMENT_KIND_TRANSMISSION_PATH;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/03/23 14:24:11 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class SetupMonitoredElement extends TestCase {

	public SetupMonitoredElement(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupMonitoredElement.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final EquivalentCondition domainCondition = new EquivalentCondition(DOMAIN_CODE);
		final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(domainCondition, true);
		if (domains.isEmpty()) {
			throw new ObjectNotFoundException("Cannot find Domains");
		}
		final Domain domain = domains.iterator().next();

		final EquivalentCondition measurementPortCondition = new EquivalentCondition(MEASUREMENTPORT_CODE);
		final Set<MeasurementPort> measurementPorts = StorableObjectPool.getStorableObjectsByCondition(measurementPortCondition, true);
		final Map<Identifier, Identifier> portIdMeasurementPortIdMap = new HashMap<Identifier, Identifier>();
		for (final MeasurementPort measurementPort : measurementPorts) {
			portIdMeasurementPortIdMap.put(measurementPort.getPortId(), measurementPort.getId());
		}

		final EquivalentCondition transmissionPathCondition = new EquivalentCondition(TRANSMISSIONPATH_CODE);
		final Set<TransmissionPath> transmissionPaths = StorableObjectPool.getStorableObjectsByCondition(transmissionPathCondition, true);

		final Set<MonitoredElement> monitoredElements = new HashSet<MonitoredElement>();
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
			final String localAddress = "0:2:0:" + n;
			monitoredElements.add(MonitoredElement.createInstance(userId,
					domain.getId(),
					monitoredElementName,
					measurementPortId,
					MONITOREDELEMENT_KIND_TRANSMISSION_PATH,
					localAddress,
					Collections.singleton(transmissionPath.getId())));
		}

		StorableObjectPool.flush(monitoredElements, userId, false);
	}


}
