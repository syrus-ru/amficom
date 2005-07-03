/*
 * $Id: TestInitialSetup.java,v 1.4 2005/06/22 14:45:47 arseniy Exp $
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
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.AMFICOM.general.TestDataType;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/22 14:45:47 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestInitialSetup extends TestCase {

	public TestInitialSetup(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest sqlCommonTest = new SQLCommonTest();

		//-1. Create system administrator
		sqlCommonTest.addTest(new TestCreateSysUser("testCreateSysUser"));
		
		//-2.Create data types
		sqlCommonTest.addTest(new TestDataType("testCreate"));

		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();

		//-3. Create default domain and server
		databaseCommonTest.addTest(new TestDomain("testCreateInstance"));
		databaseCommonTest.addTest(new TestServer("testCreateInstance"));

		//-4. Create users for server processes and processes itself
		databaseCommonTest.addTest(new TestSystemUser("testCreateInstance"));
		databaseCommonTest.addTest(new TestServerProcess("testCreateInstance"));

		//-5. Create users for MCM and MCMs itself.
		databaseCommonTest.addTest(new TestSystemUser("testCreateMCMUsers"));
		databaseCommonTest.addTest(new TestMCM("testCreateInstance"));

		//- Create test suite contains all operations
		TestSuite testSuite = new TestSuite();
		testSuite.addTest(sqlCommonTest.createTestSetup());
		testSuite.addTest(databaseCommonTest.createTestSetup());
		return testSuite;
	}
}
