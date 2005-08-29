/*-
 * $Id: TestModelingType.java,v 1.2 2005/08/29 10:00:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.SQLCommonTest;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/29 10:00:32 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestModelingType extends TestCase {

	public TestModelingType(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestModelingType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws CreateObjectException {
		ModelingTypeDatabase.insertAll();
	}

}
