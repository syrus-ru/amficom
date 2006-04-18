/*
 * $Id: SetupMeasurementPort.java,v 1.1.2.2 2006/04/18 17:35:02 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.SEPARATOR;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.configuration.SetupProtoEquipment.ProtoEquipmentName;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;

public final class SetupMeasurementPort extends TestCase {

	public SetupMeasurementPort(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupMeasurementPort.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final EquivalentCondition measurementPortTypeCondition = new EquivalentCondition(MEASUREMENTPORT_TYPE_CODE);
		final Set<MeasurementPortType> measurementPortTypes = StorableObjectPool.getStorableObjectsByCondition(measurementPortTypeCondition, true);
		if (measurementPortTypes.isEmpty()) {
			throw new ObjectNotFoundException("Cannot find MeasurementPortTypes");
		}
		final Map<String, MeasurementPortType> measurementPortTypeCodenameMap = StorableObjectType.createCodenamesMap(measurementPortTypes);

		final EquivalentCondition kisCondition = new EquivalentCondition(KIS_CODE);
		final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(kisCondition, true);
		if (kiss.isEmpty()) {
			throw new ObjectNotFoundException("Cannot find MeasurementPortTypes");
		}
		final Map<Identifier, KIS> portKISMap = new HashMap<Identifier, KIS>();
		for (final KIS kis : kiss) {
			final Equipment equipment = kis.getEquipment();
			for (final Port port : equipment.getPorts()) {
				portKISMap.put(port.getId(), kis);
			}
		}

		final EquivalentCondition portCondition = new EquivalentCondition(PORT_CODE);
		final Set<Port> ports = StorableObjectPool.getStorableObjectsByCondition(portCondition, true);

		final Set<MeasurementPort> measurementPorts = new HashSet<MeasurementPort>();
		for (final Port port : ports) {
			final String portDescription = port.getDescription();
			if (portDescription.equals("finish")) {
				continue;
			}

			final ProtoEquipment protoEquipment = port.getEquipment().getProtoEquipment();
			final String protoEquipmentName = protoEquipment.getName();
			final String measurementPortTypeCodename = ProtoEquipmentName.valueOfStr(protoEquipmentName).getMeasurementPortTypeCodename().stringValue();
			final MeasurementPortType measurementPortType = measurementPortTypeCodenameMap.get(measurementPortTypeCodename);
			if (measurementPortType == null) {
				throw new ObjectNotFoundException("Cannot find MeasurementPortType '" + measurementPortTypeCodename + "'");
			}

			final KIS kis = portKISMap.get(port.getId());
			if (kis == null) {
				throw new ObjectNotFoundException("Cannot find KIS for port '" + port.getId() + "'");
			}

			final int p1 = portDescription.indexOf(SEPARATOR);
			final int p2 = portDescription.indexOf(SEPARATOR, p1 + 1);
			final int n = Integer.parseInt(portDescription.substring(p1 + 1, p2));
			final String measurementPortDescription = MEASUREMENTPORT + SEPARATOR + n
					+ SEPARATOR
					+ kis.getDescription();
			measurementPorts.add(MeasurementPort.createInstance(userId,
					measurementPortType,
					"Порт " + n,
					measurementPortDescription,
					kis.getId(),
					port.getId()));
		}
		
		StorableObjectPool.flush(measurementPorts, userId, true);
	}
}
