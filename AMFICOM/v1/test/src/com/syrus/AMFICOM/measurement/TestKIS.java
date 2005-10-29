/*
 * $Id: TestKIS.java,v 1.4 2005/10/29 20:42:51 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;

public final class TestKIS extends TestCase {

	public TestKIS(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTest( new TestKIS("testAdd"));
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MCM_CODE);
		Iterator it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final MCM mcm = (MCM) it.next();

		ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		it = StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator();
		final Domain domain = (Domain) it.next();

		ec = new EquivalentCondition(ObjectEntities.EQUIPMENT_CODE);
		final Set<Equipment> equipments = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final Equipment equipment : equipments) {
			final String equipmentDescription = equipment.getDescription();
			final int p = equipmentDescription.indexOf(SEPARATOR);
			final int n = Integer.parseInt(equipmentDescription.substring(p + 1));
			final String kisDescription = ObjectEntities.KIS + SEPARATOR + n
					+ SEPARATOR
					+ equipmentDescription;
			final String hostname = "rtu-" + n;
			KIS.createInstance(DatabaseCommonTest.getSysUser().getId(),
					domain.getId(),
					"Рефлектометр " + n,
					kisDescription,
					hostname,
					(short) 7501,
					equipment.getId(),
					mcm.getId());
		}

		StorableObjectPool.flush(ObjectEntities.KIS_CODE, DatabaseCommonTest.getSysUser().getId(), false);
	}

	public void testAdd() throws ApplicationException {
		final Identifier sysUserId = DatabaseCommonTest.getSysUser().getId();

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.KIS_CODE);
		final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final KIS kis : kiss) {
			System.out.println("KIS: '" + kis.getId() + "', '" + kis.getDescription() + "'");
		}
		assertTrue("Number of KISs: " + kiss.size(), kiss.size() == 1);
		final KIS kis1 = kiss.iterator().next();

		final Equipment equipment1 = StorableObjectPool.getStorableObject(kis1.getEquipmentId(), true);
		assert equipment1 != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set<Port> ports = equipment1.getPorts();
		assertTrue("Number of Ports: " + ports.size(), ports.size() > 0);
		final PortType portType = ports.iterator().next().getType();

		ec.setEntityCode(ObjectEntities.TRANSMISSIONPATH_CODE);
		final Set<TransmissionPath> transmissionPaths = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final TransmissionPath transmissionPath : transmissionPaths) {
			System.out.println("TransmissionPath: '" + transmissionPath.getId() + "', '" + transmissionPath.getDescription() + "'");
		}
		assertTrue("Number of TransmissionPaths: " + transmissionPaths.size(), transmissionPaths.size() > 0);
		final TransmissionPathType transmissionPathType = transmissionPaths.iterator().next().getType();

		ec.setEntityCode(ObjectEntities.MEASUREMENTPORT_CODE);
		final Set<MeasurementPort> measurementPorts = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		for (final MeasurementPort measurementPort : measurementPorts) {
			System.out.println("MeasurementPort: '" + measurementPort.getId() + "', '" + measurementPort.getDescription() + "'");
		}
		assertTrue("Number of MeasurementPorts: " + measurementPorts.size(), measurementPorts.size() > 0);
		final MeasurementPortType measurementPortType = measurementPorts.iterator().next().getType();


		final Equipment equipment2 = Equipment.createInstance(sysUserId,
				equipment1.getDomainId(),
				equipment1.getProtoEquipmentId(),
				equipment1.getName(),
				"Рефлектометр QP1640MR",
				equipment1.getImageId(),
				equipment1.getSupplier(),
				equipment1.getSupplierCode(),
				equipment1.getLatitude(),
				equipment1.getLongitude(),
				equipment1.getHwSerial(),
				equipment1.getHwVersion(),
				equipment1.getSwSerial(),
				equipment1.getSwVersion(),
				equipment1.getInventoryNumber());

		final Port startPort2 = Port.createInstance(sysUserId, portType, "start port", equipment2.getId());
		final Port finishPort2 = Port.createInstance(sysUserId, portType, "finish port", equipment2.getId());

		final TransmissionPath transmissionPath2 = TransmissionPath.createInstance(sysUserId,
				equipment1.getDomainId(),
				"TransmissionPath",
				"TransmissionPath",
				transmissionPathType,
				startPort2.getId(),
				finishPort2.getId());

		final KIS kis2 = KIS.createInstance(sysUserId,
				kis1.getDomainId(),
				kis1.getName(),
				"Рефлектометр QP1640MR",
				"rtu-1",
				(short) 7501,
				equipment2.getId(),
				kis1.getMCMId());

		final MeasurementPort measurementPort2 = MeasurementPort.createInstance(sysUserId,
				measurementPortType,
				"MeasurementPort",
				"MeasurementPort",
				kis2.getId(),
				startPort2.getId());

		final MonitoredElement monitoredElement2 = MonitoredElement.createInstance(sysUserId,
				kis1.getDomainId(),
				"MonitoredElement",
				measurementPort2.getId(),
				MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH,
				"0:2:1:1",
				Collections.singleton(transmissionPath2.getId()));

		StorableObjectPool.flush(monitoredElement2, sysUserId, false);
	}

}
