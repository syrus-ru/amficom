/*
 * $Id: TestInitialSetup.java,v 1.1 2005/06/19 18:43:14 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.setup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.TestCreateSysUser;
import com.syrus.AMFICOM.administration.TestDomain;
import com.syrus.AMFICOM.administration.TestMCM;
import com.syrus.AMFICOM.administration.TestServer;
import com.syrus.AMFICOM.administration.TestServerProcess;
import com.syrus.AMFICOM.administration.TestSystemUser;
import com.syrus.AMFICOM.general.DatabaseCommonTest;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/19 18:43:14 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestInitialSetup extends TestCase {

	public TestInitialSetup(final String name) {
		super(name);
	}

	public static Test suite() {
		//-1. Create system administrator
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(TestCreateSysUser.suite());

		//-2. Create default domain and server
		DatabaseCommonTest.addTest(new TestDomain("testCreateInstance"));
		DatabaseCommonTest.addTest(new TestServer("testCreateInstance"));

		//-3. Create users for server processes and processes itself
		DatabaseCommonTest.addTest(new TestSystemUser("testCreateInstance"));
		DatabaseCommonTest.addTest(new TestServerProcess("testCreateInstance"));

		//-4. Create users for MCM and MCMs itself.
		DatabaseCommonTest.addTest(new TestSystemUser("testCreateMCMUsers"));
		DatabaseCommonTest.addTest(new TestMCM("testCreateInstance"));

		testSuite.addTest(DatabaseCommonTest.createTestSetup());

		//- Returning test suite contains all operations
		return testSuite;
	}
}
