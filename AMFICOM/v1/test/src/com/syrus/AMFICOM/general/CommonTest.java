/*
 * $Id: CommonTest.java,v 1.2 2005/05/11 06:44:47 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.administration.UserWrapper;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.CronTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ModelingDatabase;
import com.syrus.AMFICOM.measurement.ModelingTypeDatabase;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/11 06:44:47 $
 * @author $Author: arseniy $
 * @module test
 */
public abstract class CommonTest extends TestCase {
	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";

	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";

	public CommonTest(String name) {
		super(name);
	}

	protected static Test suiteWrapper(Class clazz) {
		TestSuite testSuite = new TestSuite(clazz);
		TestSetup testSetupWrapper = new TestSetup(testSuite) {

			protected void setUp() {
				oneTimeSetUp();
			}

			protected void tearDown() {
				oneTimeTearDown();
			}

		};
		return testSetupWrapper;
	}

	public static void oneTimeSetUp() {
		Application.init("tests");
		establishDatabaseConnection();
		initDatabaseContext();
		initStorableObjectPools();
		initIdentifierPool();
	}

	public static void oneTimeTearDown() {
		DatabaseConnection.closeConnection();
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT)*1000;
		String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}

	private static void initDatabaseContext() {
		GeneralDatabaseContext.init(new ParameterTypeDatabase(), new CharacteristicTypeDatabase(), new CharacteristicDatabase());
		AdministrationDatabaseContext.init(new UserDatabase(),
				new DomainDatabase(),
				new ServerDatabase(),
				new MCMDatabase(),
				new ServerProcessDatabase());
		ConfigurationDatabaseContext.init(new EquipmentTypeDatabase(),
				new PortTypeDatabase(),
				new MeasurementPortTypeDatabase(),
				new LinkTypeDatabase(),
				new CableLinkTypeDatabase(),
				new CableThreadTypeDatabase(),
				new EquipmentDatabase(),
				new PortDatabase(),
				new MeasurementPortDatabase(),
				new TransmissionPathDatabase(),
				new TransmissionPathTypeDatabase(),
				new KISDatabase(),
				new MonitoredElementDatabase(),
				new LinkDatabase(),
				new CableThreadDatabase());
		MeasurementDatabaseContext.init(new MeasurementTypeDatabase(),
				new AnalysisTypeDatabase(),
				new EvaluationTypeDatabase(),
				new ModelingTypeDatabase(),
				new SetDatabase(),
				new ModelingDatabase(),
				new MeasurementSetupDatabase(),
				new MeasurementDatabase(),
				new AnalysisDatabase(),
				new EvaluationDatabase(),
				new TestDatabase(),
				new ResultDatabase(),
				new CronTemporalPatternDatabase(),
				new IntervalsTemporalPatternDatabase(),
				new PeriodicalTemporalPatternDatabase());
		//More database contexts...
	}

	private static void initStorableObjectPools() {
		try {
			User sysUser = AdministrationDatabaseContext.getUserDatabase().retrieveForLogin(UserWrapper.SYS_LOGIN);
			DatabaseObjectLoader.init(sysUser.getId());
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			fail(ae.getMessage());
		}
		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(), StorableObjectResizableLRUMap.class);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(), StorableObjectResizableLRUMap.class);
		ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(), StorableObjectResizableLRUMap.class);
		MeasurementStorableObjectPool.init(new DatabaseMeasurementObjectLoader(), StorableObjectResizableLRUMap.class);
		//More pools...
	}

	private static void initIdentifierPool() {
		IdentifierPool.init(new DatabaseIdentifierGeneratorServer(), 1);
	}
}
