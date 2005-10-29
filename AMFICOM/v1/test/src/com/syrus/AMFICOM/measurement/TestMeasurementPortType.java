/*
 * $Id: TestMeasurementPortType.java,v 1.4 2005/10/29 20:43:27 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;

public final class TestMeasurementPortType extends TestCase {

	public TestMeasurementPortType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTest(new TestMeasurementPortType("testAddParameterTypes"));
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final MeasurementPortType qpMeasurementPortType = MeasurementPortType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				MeasurementPortTypeCodename.REFLECTOMETRY_QP1640A,
				"В него лазер светит",
				"Рефлектометрический",
				EnumSet.of(MeasurementType.REFLECTOMETRY),
				EnumSet.of(ParameterType.REF_PULSE_WIDTH_NS,
						ParameterType.REF_FLAG_PULSE_WIDTH_LOW_RES,
						ParameterType.REF_FLAG_GAIN_SPLICE_ON,
						ParameterType.REF_FLAG_LIFE_FIBER_DETECT));
		final MeasurementPortType pkMeasurementPortType = MeasurementPortType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				MeasurementPortTypeCodename.REFLECTOMETRY_PK7600,
				"В него лазер светит",
				"Рефлектометрический",
				EnumSet.of(MeasurementType.REFLECTOMETRY),
				EnumSet.of(ParameterType.REF_PULSE_WIDTH_M));

		final Set<Identifiable> toFlushObjects = new HashSet<Identifiable>();
		toFlushObjects.add(qpMeasurementPortType);
		toFlushObjects.add(pkMeasurementPortType);
		StorableObjectPool.flush(toFlushObjects, DatabaseCommonTest.getSysUser().getId(), false);
	}

	public void testAddParameterTypes() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.KIS_CODE);
		final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		assertTrue("Number of KISs: " + kiss.size(), kiss.size() == 2);
		KIS kisQP = null;
		KIS kisPK = null;
		for (final KIS kis : kiss) {
			final String description = kis.getDescription();
			if (description.indexOf("QP1640A") > 0) {
				kisQP = kis;
			} else if (description.indexOf("QP1640MR") > 0) {
				kisPK = kis;
			} else {
				System.out.println("ERROR: Unknown kis: " + description);
			}
		}
		assertNotNull(kisQP);
		assertNotNull(kisPK);

		final MeasurementPortType qpMeasurementPortType = kisQP.getMeasurementPorts(true).iterator().next().getType();
		System.out.println("QP MeasurementPortType '" + qpMeasurementPortType.getDescription() + "'");
		qpMeasurementPortType.setCodename(MeasurementPortTypeCodename.REFLECTOMETRY_QP1640A.stringValue());
		qpMeasurementPortType.setMeasurementTypes(EnumSet.of(MeasurementType.REFLECTOMETRY));
		qpMeasurementPortType.setParameterTypes(EnumSet.of(ParameterType.REF_PULSE_WIDTH_NS,
				ParameterType.REF_FLAG_PULSE_WIDTH_LOW_RES,
				ParameterType.REF_FLAG_GAIN_SPLICE_ON,
				ParameterType.REF_FLAG_LIFE_FIBER_DETECT));

		final MeasurementPortType pkMeasurementPortType = MeasurementPortType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				MeasurementPortTypeCodename.REFLECTOMETRY_PK7600,
				"PK7600",
				"PK7600",
				EnumSet.of(MeasurementType.REFLECTOMETRY),
				EnumSet.of(ParameterType.REF_PULSE_WIDTH_M));
		for (final MeasurementPort measurementPort : kisPK.getMeasurementPorts(true)) {
			System.out.println("PK MeasurementPort: " + measurementPort.getName());
			measurementPort.setType(pkMeasurementPortType);
		}

		final Set<Identifiable> toFlushObjects = new HashSet<Identifiable>();
		toFlushObjects.add(qpMeasurementPortType);
		toFlushObjects.add(pkMeasurementPortType);
		toFlushObjects.addAll(kisQP.getMeasurementPorts(true));
		toFlushObjects.addAll(kisPK.getMeasurementPorts(true));
		StorableObjectPool.flush(toFlushObjects, DatabaseCommonTest.getSysUser().getId(), false);
	}
}
