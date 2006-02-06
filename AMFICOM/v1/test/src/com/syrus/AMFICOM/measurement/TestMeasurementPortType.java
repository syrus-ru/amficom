/*
 * $Id: TestMeasurementPortType.java,v 1.6 2005/12/20 08:48:23 arseniy Exp $
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
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

public final class TestMeasurementPortType extends TestCase {

	public TestMeasurementPortType(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
//		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTest(new TestMeasurementPortType("testRetrieveCharacteristics"));
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

	public void testRetrieveCharacteristics() throws ApplicationException {
		final TypicalCondition tc = new TypicalCondition("reflectometry",
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.MEASUREMENTPORT_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		final Set<MeasurementPortType> mpts = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		assertTrue("Size: " + mpts.size(), mpts.size() == 1);
		final MeasurementPortType measurementPortType = mpts.iterator().next();
		System.out.println("MeasurementPortType: " + measurementPortType);

		final LinkedIdsCondition lic = new LinkedIdsCondition(measurementPortType, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		for (final Characteristic characteristic : characteristics) {
			System.out.println("Characteristic: " + characteristic.getType().getCodename() + ", value: " + characteristic.getValue());
		}
	}
}
