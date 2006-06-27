/*
 * $Id: Setup.java,v 1.1.2.8 2006/06/27 17:33:28 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.setup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.SetupCreateSysUser;
import com.syrus.AMFICOM.administration.SetupDomain;
import com.syrus.AMFICOM.administration.SetupMCM;
import com.syrus.AMFICOM.administration.SetupServer;
import com.syrus.AMFICOM.administration.SetupSystemUser;
import com.syrus.AMFICOM.administration.SetupSystemUserPasswords;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.AMFICOM.general.SetupDataType;
import com.syrus.AMFICOM.general.SetupMeasurementUnit;

/**
 * @version $Revision: 1.1.2.8 $, $Date: 2006/06/27 17:33:28 $
 * @author $Author: arseniy $
 * @module test
 */
public final class Setup extends TestCase {

	public Setup(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest sqlCommonTest = new SQLCommonTest();

		//-Create system administrator.
		sqlCommonTest.addTest(new SetupCreateSysUser("testCreate"));

		//-Create dictionaries.
		sqlCommonTest.addTest(new SetupDataType("testCreate"));
		sqlCommonTest.addTest(new SetupMeasurementUnit("testCreate"));


		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();

		//-Create root domain and server
		databaseCommonTest.addTest(new SetupDomain("testCreate"));

		//-Create users for server processes.
		databaseCommonTest.addTest(new SetupSystemUser("testCreate"));

		//-Create server and MCMs
		databaseCommonTest.addTest(new SetupServer("testCreate"));
		databaseCommonTest.addTest(new SetupMCM("testCreate"));

		//-Set passwords for base system users.
		databaseCommonTest.addTest(new SetupSystemUserPasswords("testSetPassword"));

		//-Create test suite contains all operations
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(sqlCommonTest.createTestSetup());
		testSuite.addTest(databaseCommonTest.createTestSetup());
		return testSuite;
	}
}
