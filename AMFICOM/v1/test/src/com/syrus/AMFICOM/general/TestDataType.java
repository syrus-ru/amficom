/*
 * $Id: TestDataType.java,v 1.2 2005/07/15 12:00:46 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;
import junit.framework.TestCase;

public final class TestDataType extends TestCase {

	public TestDataType(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestDataType.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws CreateObjectException {
		DataTypeDatabase.insertDataTypes();
	}
}
