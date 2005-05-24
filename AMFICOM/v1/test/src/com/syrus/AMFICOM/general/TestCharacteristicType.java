/*
 * $Id: TestCharacteristicType.java,v 1.1 2005/05/24 17:24:58 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;

import com.syrus.AMFICOM.general.corba.OperationSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/24 17:24:58 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestCharacteristicType extends CommonTest {

	public TestCharacteristicType(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestCharacteristicType.class);
	}

	public void testUpdate() throws ApplicationException {
		TypicalCondition tc = new TypicalCondition("ref_wvlen_1625_ref_pulswd_high_r",
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		CharacteristicType characteristicType = (CharacteristicType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		characteristicType.setCodename(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "1625" + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES_SUFFIX);

		tc.setValue("ref_wvlen_1625_ref_pulswd_low_re");
		characteristicType = (CharacteristicType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next();
		characteristicType.setCodename(CharacteristicTypeCodenames.TRACE_WAVELENGTH_PREFIX + "1625" + CharacteristicTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES_SUFFIX);

		StorableObjectPool.flush(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, true);
	}
}
