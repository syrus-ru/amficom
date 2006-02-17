/*
 * $Id: Setup.java,v 1.1.2.2 2006/02/17 15:54:32 arseniy Exp $
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
import com.syrus.AMFICOM.administration.SetupServer;
import com.syrus.AMFICOM.administration.SetupSystemUser;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.AMFICOM.general.SetupDataType;
import com.syrus.AMFICOM.general.SetupMeasurementUnit;
import com.syrus.AMFICOM.reflectometry.SetupParameterType;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/17 15:54:32 $
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

		//-Create parameter types
		databaseCommonTest.addTest(new SetupParameterType("testCreate"));

//		sqlCommonTest.addTest(new TestEquipmentType("testCreateAll"));
//		sqlCommonTest.addTest(new TestMeasurementType("testCreateAll"));
//		sqlCommonTest.addTest(new TestAnalysisType("testCreateAll"));
//		sqlCommonTest.addTest(new TestModelingType("testCreateAll"));
//
//		//-Create users for server processes and processes themself.
//		databaseCommonTest.addTest(new TestSystemUser("testCreateServerProcessUsers"));
//		databaseCommonTest.addTest(new TestServerProcess("testCreateAll"));
//
//		//-Create users for MCM and MCMs itself.
//		databaseCommonTest.addTest(new TestSystemUser("testCreateMCMUsers"));
//		databaseCommonTest.addTest(new TestMCM("testCreateInstance"));
//
//		//-Create configuration objects.
//		databaseCommonTest.addTest(new TestPortType("testCreateInstance"));
//		databaseCommonTest.addTest(new TestTransmissionPathType("testCreateInstance"));
//		databaseCommonTest.addTest(new TestProtoEquipment("testCreateInstance"));
//		databaseCommonTest.addTest(new TestEquipment("testCreateAll"));
//		databaseCommonTest.addTest(new TestPort("testCreateAll"));
//		databaseCommonTest.addTest(new TestTransmissionPath("testCreateAll"));
//
//		//-Create measurement objects.
//		databaseCommonTest.addTest(new TestMeasurementPortType("testCreateInstance"));
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
