/*
 * $Id: Setup.java,v 1.1.2.6 2006/02/27 16:26:21 arseniy Exp $
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
import com.syrus.AMFICOM.administration.SetupServerProcess;
import com.syrus.AMFICOM.administration.SetupSystemUser;
import com.syrus.AMFICOM.administration.SetupSystemUserPasswords;
import com.syrus.AMFICOM.configuration.SetupEquipmentType;
import com.syrus.AMFICOM.configuration.SetupPortType;
import com.syrus.AMFICOM.configuration.SetupTransmissionPathType;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.AMFICOM.general.SetupDataType;
import com.syrus.AMFICOM.general.SetupMeasurementUnit;
import com.syrus.AMFICOM.measurement.SetupActionParameterTypeBinding;
import com.syrus.AMFICOM.measurement.SetupActionType;
import com.syrus.AMFICOM.measurement.SetupMeasurementPortType;
import com.syrus.AMFICOM.reflectometry.SetupActionParameter;
import com.syrus.AMFICOM.reflectometry.SetupParameterType;

/**
 * @version $Revision: 1.1.2.6 $, $Date: 2006/02/27 16:26:21 $
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
		databaseCommonTest.addTest(new SetupServer("testCreate"));

		//-Create users for server processes and processes themselves.
		databaseCommonTest.addTest(new SetupSystemUser("testCreate"));
		databaseCommonTest.addTest(new SetupServerProcess("testCreate"));
		databaseCommonTest.addTest(new SetupMCM("testCreate"));

		//-Set passwords for base system users.
		databaseCommonTest.addTest(new SetupSystemUserPasswords("testSetPassword"));
//
//		//-Create configuration objects.
//		databaseCommonTest.addTest(new TestProtoEquipment("testCreateInstance"));
//		databaseCommonTest.addTest(new TestEquipment("testCreateAll"));
//		databaseCommonTest.addTest(new TestPort("testCreateAll"));
//		databaseCommonTest.addTest(new TestTransmissionPath("testCreateAll"));
//
//		//-Create measurement objects.
//		databaseCommonTest.addTest(new TestKIS("testCreateAll"));
//		databaseCommonTest.addTest(new TestMeasurementPort("testCreateAll"));
//		databaseCommonTest.addTest(new TestMonitoredElement("testCreateAll"));
//
//		//-Create equipment-specific characteristics. 
//		//databaseCommonTest.addTest(new TestCharacteristicQP1640A("testCreate"));


		//-Create test suite contains all operations
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(sqlCommonTest.createTestSetup());
		testSuite.addTest(databaseCommonTest.createTestSetup());
		return testSuite;
	}
}
