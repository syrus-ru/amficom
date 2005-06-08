/*
 * $Id: TestMCM.java,v 1.1 2005/06/08 13:50:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/08 13:50:06 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestMCM extends TestCase {

	public TestMCM(final String name) {
		super(name);
	}

	protected void setUp() {
		System.out.println("********* set up **********");
	}

	protected void tearDown() {
		System.out.println("********* tear down **********");
	}

	public void test1() {
		assertEquals("test 1", 1, 1);
	}

	public void test2() {
		assertEquals("test 2", 1, 1);
	}

	public void test3() {
		assertEquals("test 3", 1, 1);
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTest(new TestMCM("test1"));
		testSuite.addTest(new TestMCM("test2"));
		return testSuite;
	}
}
