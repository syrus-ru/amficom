/*-
 * $Id: TestEquipmentType.java,v 1.3 2005/09/29 08:28:27 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.SQLCommonTest;

/**
 * @version $Revision: 1.3 $, $Date: 2005/09/29 08:28:27 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestEquipmentType extends TestCase {

	public TestEquipmentType(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestEquipmentType.class);
		return commonTest.createTestSetup();
	}


	public void testCreateAll() throws CreateObjectException {
		EquipmentTypeDatabase.insertAll();
	}
}
