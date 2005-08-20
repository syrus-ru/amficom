/*-
 * $Id: TestParameterType.java,v 1.16 2005/08/20 20:15:12 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.16 $, $Date: 2005/08/20 20:15:12 $
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

	public void testCreate() throws CreateObjectException {
		ParameterTypeDatabase.insertParameterTypes();
	}
}
