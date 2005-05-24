/*
 * $Id: CommonTest.java,v 1.4 2005/05/24 17:25:23 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import gnu.trove.TShortObjectHashMap;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
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
 * @version $Revision: 1.4 $, $Date: 2005/05/24 17:25:23 $
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

	protected static User creatorUser;

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
		final TShortObjectHashMap entityCodeDatabaseMap = new TShortObjectHashMap();

		entityCodeDatabaseMap.put(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, new ParameterTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, new CharacteristicTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, new CharacteristicDatabase());

		entityCodeDatabaseMap.put(ObjectEntities.USER_ENTITY_CODE, new UserDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.DOMAIN_ENTITY_CODE, new DomainDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SERVER_ENTITY_CODE, new ServerDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MCM_ENTITY_CODE, new MCMDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SERVERPROCESS_ENTITY_CODE, new ServerProcessDatabase());

		entityCodeDatabaseMap.put(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, new EquipmentTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PORTTYPE_ENTITY_CODE, new PortTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, new MeasurementPortTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, new TransmissionPathTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.LINKTYPE_ENTITY_CODE, new LinkTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, new CableLinkTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, new CableThreadTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EQUIPMENT_ENTITY_CODE, new EquipmentDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PORT_ENTITY_CODE, new PortDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, new MeasurementPortDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.TRANSPATH_ENTITY_CODE, new TransmissionPathDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.KIS_ENTITY_CODE, new KISDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE, new MonitoredElementDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.LINK_ENTITY_CODE, new LinkDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CABLETHREAD_ENTITY_CODE, new CableThreadDatabase());

		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, new MeasurementTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, new AnalysisTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, new EvaluationTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MODELINGTYPE_ENTITY_CODE, new ModelingTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENT_ENTITY_CODE, new MeasurementDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.ANALYSIS_ENTITY_CODE, new AnalysisDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EVALUATION_ENTITY_CODE, new EvaluationDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MODELING_ENTITY_CODE, new EvaluationDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE, new MeasurementSetupDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.RESULT_ENTITY_CODE, new ResultDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SET_ENTITY_CODE, new SetDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.TEST_ENTITY_CODE, new TestDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE, new CronTemporalPatternDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE, new IntervalsTemporalPatternDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE, new PeriodicalTemporalPatternDatabase());
		//More database drivers...

		DatabaseContext.init(entityCodeDatabaseMap);
	}

	private static void initStorableObjectPools() {
		try {
			UserDatabase userDatabase = (UserDatabase) DatabaseContext.getDatabase(ObjectEntities.USER_ENTITY_CODE);
			User sysUser = userDatabase.retrieveForLogin(UserWrapper.SYS_LOGIN);
			creatorUser = sysUser;
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
