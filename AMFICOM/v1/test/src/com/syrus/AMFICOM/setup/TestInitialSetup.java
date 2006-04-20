/*
 * $Id: TestInitialSetup.java,v 1.17 2006/04/20 11:04:10 arseniy Exp $
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
import com.syrus.AMFICOM.configuration.TestEquipment;
import com.syrus.AMFICOM.configuration.TestPort;
import com.syrus.AMFICOM.configuration.TestPortType;
import com.syrus.AMFICOM.configuration.TestTransmissionPath;
import com.syrus.AMFICOM.configuration.TestTransmissionPathType;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.AMFICOM.general.TestDataType;
import com.syrus.AMFICOM.general.TestMeasurementUnit;
import com.syrus.AMFICOM.general.TestParameterType;
import com.syrus.AMFICOM.measurement.TestAnalysisType;
import com.syrus.AMFICOM.measurement.TestKIS;
import com.syrus.AMFICOM.measurement.TestMeasurementPort;
import com.syrus.AMFICOM.measurement.TestMeasurementPortType;
import com.syrus.AMFICOM.measurement.TestMeasurementType;
import com.syrus.AMFICOM.measurement.TestModelingType;
import com.syrus.AMFICOM.measurement.TestMonitoredElement;

/**
 * @version $Revision: 1.17 $, $Date: 2006/04/20 11:04:10 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestInitialSetup extends TestCase {

	public TestInitialSetup(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest sqlCommonTest = new SQLCommonTest();

		//-1. Create system administrator.
		sqlCommonTest.addTest(new TestCreateSysUser("testCreateSysUser"));

		//-2.Create dictionaries.
		sqlCommonTest.addTest(new TestDataType("testCreateAll"));
		sqlCommonTest.addTest(new TestMeasurementUnit("testCreateAll"));
		sqlCommonTest.addTest(new TestParameterType("testCreateAll"));
		sqlCommonTest.addTest(new TestMeasurementType("testCreateAll"));
		sqlCommonTest.addTest(new TestAnalysisType("testCreateAll"));
		sqlCommonTest.addTest(new TestModelingType("testCreateAll"));

		final DatabaseCommonTest databaseCommonTest = new DatabaseCommonTest();

		//-3. Create default domain and server.
		databaseCommonTest.addTest(new TestDomain("testCreateInstance"));
		databaseCommonTest.addTest(new TestServer("testCreateInstance"));

		//-4. Create users for server processes and processes themself.
		databaseCommonTest.addTest(new TestSystemUser("testCreateServerProcessUsers"));
		databaseCommonTest.addTest(new TestServerProcess("testCreateAll"));

		//-5. Create users for MCM and MCMs itself.
		databaseCommonTest.addTest(new TestSystemUser("testCreateMCMUsers"));
		databaseCommonTest.addTest(new TestMCM("testCreateInstance"));

		//-6. Create configuration objects.
		databaseCommonTest.addTest(new TestPortType("testCreateInstance"));
		databaseCommonTest.addTest(new TestTransmissionPathType("testCreateInstance"));
		databaseCommonTest.addTest(new TestEquipment("testCreateAll"));
		databaseCommonTest.addTest(new TestPort("testCreateAll"));
		databaseCommonTest.addTest(new TestTransmissionPath("testCreateAll"));

		//-7. Create measurement objects.
		databaseCommonTest.addTest(new TestMeasurementPortType("testCreateInstance"));
		databaseCommonTest.addTest(new TestKIS("testCreateAll"));
		databaseCommonTest.addTest(new TestMeasurementPort("testCreateAll"));
		databaseCommonTest.addTest(new TestMonitoredElement("testCreateAll"));

		//-8. Create equipment-specific characteristics. 
		//databaseCommonTest.addTest(new TestCharacteristicQP1640A("testCreate"));

		//- Create test suite contains all operations
		TestSuite testSuite = new TestSuite();
		testSuite.addTest(sqlCommonTest.createTestSetup());
		testSuite.addTest(databaseCommonTest.createTestSetup());
		return testSuite;
	}
}
