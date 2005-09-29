/*
 * $Id: TestMeasurementType.java,v 1.11 2005/09/29 08:03:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.SQLCommonTest;

/**
 * @version $Revision: 1.11 $, $Date: 2005/09/29 08:03:21 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestMeasurementType extends TestCase {

	public TestMeasurementType(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestMeasurementType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws CreateObjectException {
		MeasurementTypeDatabase.insertAll();
	}

}
