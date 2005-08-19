/*-
 * $Id: TestEquipmentType.java,v 1.1 2005/08/19 15:57:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.StorableObjectPool;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/19 15:57:32 $
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
				"Reflectometer",
				"Q",
				"Q");
		StorableObjectPool.flush(equipmentType, DatabaseCommonTest.getSysUser().getId(), false);
	}
}
