/*
 * $Id: TestMeasurementPortType.java,v 1.5 2005/12/15 14:52:27 arseniy Exp $
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
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
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
		final Identifier creatorId = DatabaseCommonTest.getSysUser().getId();

		final MeasurementPortType qpMeasurementPortType = MeasurementPortType.createInstance(creatorId,
				"REFLECTOMETRY_QP1640A",
				"В него лазер светит",
				"Рефлектометрический",
				EnumSet.of(MeasurementType.REFLECTOMETRY));
		final MeasurementPortType pkMeasurementPortType = MeasurementPortType.createInstance(creatorId,
				"REFLECTOMETRY_PK7600",
				"В него лазер светит",
				"Рефлектометрический",
				EnumSet.of(MeasurementType.REFLECTOMETRY));

		final Set<Identifiable> toFlushObjects = new HashSet<Identifiable>();
		toFlushObjects.add(qpMeasurementPortType);
		toFlushObjects.add(pkMeasurementPortType);
		StorableObjectPool.flush(toFlushObjects, creatorId, false);
	}

}
