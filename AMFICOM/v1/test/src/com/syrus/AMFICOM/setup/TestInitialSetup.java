/*
 * $Id: TestInitialSetup.java,v 1.2 2005/06/20 15:13:54 arseniy Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/06/20 15:13:54 $
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

		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();

		//-2. Create default domain and server
		databaseCommonTest.addTest(new TestDomain("testCreateInstance"));
		databaseCommonTest.addTest(new TestServer("testCreateInstance"));

		//-3. Create users for server processes and processes itself
		databaseCommonTest.addTest(new TestSystemUser("testCreateInstance"));
		databaseCommonTest.addTest(new TestServerProcess("testCreateInstance"));

		//-4. Create users for MCM and MCMs itself.
		databaseCommonTest.addTest(new TestSystemUser("testCreateMCMUsers"));
		databaseCommonTest.addTest(new TestMCM("testCreateInstance"));

		testSuite.addTest(databaseCommonTest.createTestSetup());

		//- Returning test suite contains all operations
		return testSuite;
	}
}
