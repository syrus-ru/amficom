/*
 * $Id: TestCharacteristic.java,v 1.2 2005/06/19 18:43:56 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/19 18:43:56 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestCharacteristic extends DatabaseCommonTest {

	public TestCharacteristic(String name) {
		super(name);
	}

	public static Test suite() {
		addTestSuite(TestCharacteristic.class);
		return createTestSetup();
	}

	public void t1estUpdate() throws ApplicationException {
		TypicalCondition tc = new TypicalCondition(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "1625" + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTIC_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		CharacteristicType characteristicType = (CharacteristicType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();

		LinkedIdsCondition lc = new LinkedIdsCondition(characteristicType.getId(), ObjectEntities.CHARACTERISTIC_CODE);
		Characteristic characteristic = (Characteristic) StorableObjectPool.getStorableObjectsByCondition(lc, true).iterator().next();
		characteristic.setValue("5 10 20 50 100 200 500 1000 2000 5000");
		characteristic.setDescription("QP1643A high resolution pulse width at wavelength 1625.00 nm");

		tc = new TypicalCondition(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "1625" + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTIC_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		characteristicType = (CharacteristicType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		characteristic = Characteristic.createInstance(creatorUser.getId(),
				characteristicType,
				characteristicType.getCodename(),
				"QP1643A low resolution pulse width at wavelength 1625.00 nm",
				"10000 20000 30000",
				(Characterizable) StorableObjectPool.getStorableObject(characteristic.getCharacterizableId(), true),
				characteristic.isEditable(),
				characteristic.isVisible());

		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, true);
	}

	public void testRetrieve() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
		MeasurementPortType measurementPortType = (MeasurementPortType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();

		LinkedIdsCondition lc = new LinkedIdsCondition(measurementPortType.getId(), ObjectEntities.CHARACTERISTIC_CODE);
		Set characteristics = StorableObjectPool.getStorableObjectsByCondition(lc, true);
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			final Characteristic characteristic = (Characteristic) it.next();
			System.out.println("Characteristic: '" + characteristic.getId() + "'");
			System.out.println("Description: '" + characteristic.getDescription() + "'");
			System.out.println("Value: '" + characteristic.getValue() + "'");
		}
	}
}
