/*
 * $Id: SetupDataType.java,v 1.1.2.2 2006/02/17 15:53:08 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;
import junit.framework.TestCase;

public final class SetupDataType extends TestCase {

	public SetupDataType(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(SetupDataType.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws CreateObjectException {
		DataTypeDatabase.insertAll();
	}
}
