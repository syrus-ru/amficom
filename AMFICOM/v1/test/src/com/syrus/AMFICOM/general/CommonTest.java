/*
 * $Id: CommonTest.java,v 1.12 2005/07/19 17:55:27 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.syrus.util.Application;

/**
 * @version $Revision: 1.12 $, $Date: 2005/07/19 17:55:27 $
 * @author $Author: arseniy $
 * @module test
 */
public class CommonTest {
	public static final String APPLICATION_NAME = "test";

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

	void oneTimeSetUp() {
		Application.init(APPLICATION_NAME);
	}

	void oneTimeTearDown() {
		//write something in descendant's
	}
}