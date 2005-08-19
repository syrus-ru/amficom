/*
 * $Id: TestInitialSetup.java,v 1.6 2005/08/19 15:57:33 arseniy Exp $
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
import com.syrus.AMFICOM.configuration.TestEquipmentType;
import com.syrus.AMFICOM.configuration.TestKIS;
import com.syrus.AMFICOM.configuration.TestMeasurementPort;
import com.syrus.AMFICOM.configuration.TestMeasurementPortType;
import com.syrus.AMFICOM.configuration.TestMonitoredElement;
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
import com.syrus.AMFICOM.measurement.TestMeasurementType;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/19 15:57:33 $
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

		//-3.Create measurement units
		sqlCommonTest.addTest(new TestMeasurementUnit("testCreate"));

		//-4.Create parameter types
		sqlCommonTest.addTest(new TestParameterType("testCreate"));

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

		databaseCommonTest.addTest(new TestEquipmentType("testCreateInstance"));
		databaseCommonTest.addTest(new TestPortType("testCreateInstance"));
		databaseCommonTest.addTest(new TestTransmissionPathType("testCreateInstance"));
		databaseCommonTest.addTest(new TestMeasurementPortType("testCreateInstance"));

		databaseCommonTest.addTest(new TestEquipment("testCreateInstance"));
		databaseCommonTest.addTest(new TestPort("testCreateInstance"));
		databaseCommonTest.addTest(new TestTransmissionPath("testCreateInstance"));
		databaseCommonTest.addTest(new TestKIS("testCreateInstance"));
		databaseCommonTest.addTest(new TestMeasurementPort("testCreateInstance"));
		databaseCommonTest.addTest(new TestMonitoredElement("testCreateInstance"));

		databaseCommonTest.addTestSuite(TestMeasurementType.class);
		databaseCommonTest.addTestSuite(TestAnalysisType.class);

		//- Create test suite contains all operations
		TestSuite testSuite = new TestSuite();
		testSuite.addTest(sqlCommonTest.createTestSetup());
		testSuite.addTest(databaseCommonTest.createTestSetup());
		return testSuite;
	}
}
