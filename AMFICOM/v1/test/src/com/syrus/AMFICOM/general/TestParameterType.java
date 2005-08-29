/*-
 * $Id: TestParameterType.java,v 1.18 2005/08/29 10:13:35 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.18 $, $Date: 2005/08/29 10:13:35 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestParameterType extends TestCase {

	public TestParameterType(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestParameterType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws CreateObjectException {
		ParameterTypeDatabase.insertAll();
	}
}
