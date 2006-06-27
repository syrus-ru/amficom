/*
 * $Id: CommonTest.java,v 1.13.2.1 2006/06/27 17:31:15 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.util.Application;

/**
 * @version $Revision: 1.13.2.1 $, $Date: 2006/06/27 17:31:15 $
 * @author $Author: arseniy $
 * @module test
 */
public class CommonTest {
	private static final String APPLICATION_NAME = "test";

	private final TestSuite TEST_SUITE;


	public CommonTest() {
		this.TEST_SUITE = new TestSuite();
	}

	public final void addTest(final Test test) {
		this.TEST_SUITE.addTest(test);
	}

	public final void addTestSuite(final Class testClass) {
		this.TEST_SUITE.addTestSuite(testClass);
	}

	public final TestSetup createTestSetup() {
		final TestSetup testSetup = new TestSetup(this.TEST_SUITE) {

			@Override
			protected void setUp() {
				oneTimeSetUp();
			}

			@Override
			protected void tearDown() {
				oneTimeTearDown();
			}

		};
		return testSetup;
	}

	public String getApplicationName() {
		return APPLICATION_NAME;
	}

	void oneTimeSetUp() {
		Application.init(this.getApplicationName());
		I18N.addResourceBundle("com.syrus.AMFICOM.client.resource.general");
	}

	void oneTimeTearDown() {
		//write something in descendant's
	}
}
