/*-
 * $Id: DatabaseVersionTestCase.java,v 1.1 2006/06/26 17:23:13 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.oneTimeSetUp;
import static com.syrus.AMFICOM.eventv2.EventHierarchyTestCase.oneTimeTearDown;
import static com.syrus.util.database.DatabaseConnection.USERNAME;
import static java.util.logging.Level.SEVERE;

import java.sql.SQLException;

import junit.awtui.TestRunner;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/06/26 17:23:13 $
 * @module event
 */
public final class DatabaseVersionTestCase extends TestCase {
	private static final String KEY_DB_HOST_NAME = "DBHostName";
	private static final String KEY_DB_SID = "DBSID";
	private static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	private static final String KEY_DB_LOGIN_NAME = "DBLoginName";

	private static final String DEFAULT_DB_HOST_NAME = "localhost";
	private static final String DEFAULT_DB_SID = "amficom";
	private static final int DEFAULT_DB_CONNECTION_TIMEOUT = 120;
	private static final String DEFAULT_DB_LOGIN_NAME = USERNAME;

	public DatabaseVersionTestCase(final String method) {
		super(method);
	}

	public static void main(final String args[]) {
		TestRunner.main(new String[] {DatabaseVersionTestCase.class.getName()});
	}

	public static Test suite() {
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(new EventHierarchyTestCase("testAssertionStatus"));
		testSuite.addTest(new DatabaseVersionTestCase("testVersionComparison"));
		return new TestSetup(testSuite) {
			@Override
			protected void setUp() {
				oneTimeSetUp();

				ApplicationProperties.init("common");
				final String dbHostName = ApplicationProperties.getString(
						KEY_DB_HOST_NAME,
						DEFAULT_DB_HOST_NAME);
				final String dbSid = ApplicationProperties.getString(
						KEY_DB_SID,
						DEFAULT_DB_SID);
				final long dbConnectionTimeout = ApplicationProperties.getInt(
						KEY_DB_CONNECTION_TIMEOUT,
						DEFAULT_DB_CONNECTION_TIMEOUT) * 1000;
				final String dbLoginName = ApplicationProperties.getString(
						KEY_DB_LOGIN_NAME,
						DEFAULT_DB_LOGIN_NAME);
				try {
					DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnectionTimeout, dbLoginName);
				} catch (final SQLException sqle) {
					Log.debugMessage(sqle, SEVERE);
				}
			}

			@Override
			protected void tearDown() {
				oneTimeTearDown();

				DatabaseConnection.closeConnection();
			}
		};
	}

	public void testVersionComparison() {
		new ReflectogramMismatchEventDatabase();
		new LineMismatchEventDatabase();
	}
}
