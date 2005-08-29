/*
 * $Id: TestAnalysisType.java,v 1.9 2005/08/29 10:00:32 arseniy Exp $
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
 * @version $Revision: 1.9 $, $Date: 2005/08/29 10:00:32 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestAnalysisType extends TestCase {

	public TestAnalysisType(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestAnalysisType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws CreateObjectException {
		AnalysisTypeDatabase.insertAll();
	}

}
