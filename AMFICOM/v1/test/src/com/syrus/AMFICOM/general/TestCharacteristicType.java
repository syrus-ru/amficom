/*
 * $Id: TestCharacteristicType.java,v 1.2 2005/06/19 18:43:56 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;

import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/19 18:43:56 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestCharacteristicType extends DatabaseCommonTest {

	public TestCharacteristicType(String name) {
		super(name);
	}

	public static Test suite() {
		addTestSuite(TestCharacteristicType.class);
		return createTestSetup();
	}

	public void testUpdate() throws ApplicationException {
		TypicalCondition tc = new TypicalCondition("ref_wvlen_1625_ref_pulswd_high_r",
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTIC_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		CharacteristicType characteristicType = (CharacteristicType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		characteristicType.setCodename(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "1625" + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX);

		tc.setValue("ref_wvlen_1625_ref_pulswd_low_re");
		characteristicType = (CharacteristicType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		characteristicType.setCodename(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "1625" + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX);

		StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_TYPE_CODE, true);
	}
}
