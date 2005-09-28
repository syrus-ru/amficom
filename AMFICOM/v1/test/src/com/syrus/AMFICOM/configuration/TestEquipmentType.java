/*-
 * $Id: TestEquipmentType.java,v 1.2 2005/09/28 13:25:16 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/28 13:25:16 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestEquipmentType extends TestCase {

	public TestEquipmentType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestEquipmentType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final EquipmentType equipmentType = EquipmentType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				"reflectometer",
				"Reflectometer",
				"Reflectometer");
		StorableObjectPool.flush(equipmentType, DatabaseCommonTest.getSysUser().getId(), false);
	}
}
