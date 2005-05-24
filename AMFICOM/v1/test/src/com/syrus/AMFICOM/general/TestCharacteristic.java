/*
 * $Id: TestCharacteristic.java,v 1.1 2005/05/24 17:24:58 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.corba.OperationSort;

import junit.framework.Test;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/24 17:24:58 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestCharacteristic extends CommonTest {

	public TestCharacteristic(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestCharacteristic.class);
	}

	public void t1estUpdate() throws ApplicationException {
		TypicalCondition tc = new TypicalCondition(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "1625" + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		CharacteristicType characteristicType = (CharacteristicType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();

		LinkedIdsCondition lc = new LinkedIdsCondition(characteristicType.getId(), ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
		Characteristic characteristic = (Characteristic) StorableObjectPool.getStorableObjectsByCondition(lc, true).iterator().next();
		characteristic.setValue("5 10 20 50 100 200 500 1000 2000 5000");
		characteristic.setDescription("QP1643A high resolution pulse width at wavelength 1625.00 nm");

		tc = new TypicalCondition(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "1625" + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		characteristicType = (CharacteristicType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		characteristic = Characteristic.createInstance(creatorUser.getId(),
				characteristicType,
				characteristicType.getCodename(),
				"QP1643A low resolution pulse width at wavelength 1625.00 nm",
				characteristic.getSort(),
				"10000 20000 30000",
				characteristic.getCharacterizableId(),
				characteristic.isEditable(),
				characteristic.isVisible());
		StorableObjectPool.putStorableObject(characteristic);

		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, true);
	}

	public void testRetrieve() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
		MeasurementPortType measurementPortType = (MeasurementPortType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();

		LinkedIdsCondition lc = new LinkedIdsCondition(measurementPortType.getId(), ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
		Set characteristics = StorableObjectPool.getStorableObjectsByCondition(lc, true);
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			final Characteristic characteristic = (Characteristic) it.next();
			System.out.println("Characteristic: '" + characteristic.getId() + "'");
			System.out.println("Description: '" + characteristic.getDescription() + "'");
			System.out.println("Value: '" + characteristic.getValue() + "'");
		}
	}
}
