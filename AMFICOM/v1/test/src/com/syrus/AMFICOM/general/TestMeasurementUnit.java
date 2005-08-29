/*-
 * $Id: TestMeasurementUnit.java,v 1.2 2005/08/29 10:00:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/29 10:00:32 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestMeasurementUnit extends TestCase {

	public TestMeasurementUnit(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestMeasurementUnit.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws CreateObjectException {
		MeasurementUnitDatabase.insertAll();
	}
}
