/*
 * $Id: CommonEventTest.java,v 1.1 2005/02/07 12:00:58 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.DefaultIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/07 12:00:58 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class CommonEventTest extends TestCase {
	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";

	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";

	public CommonEventTest(String name) {
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
		EventDatabaseContext.init(new EventTypeDatabase(), new EventDatabase());
	}

	private static void initStorableObjectPools() {
		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader());
		EventStorableObjectPool.init(new DatabaseEventObjectLoader());
	}

	private static void initIdentifierPool() {
		IdentifierPool.init(new DefaultIdentifierGeneratorServer(), 1);
	}
}

/*assertEquals
assertNull
assertSame
assertTrue
assertFalse

fail
*/
