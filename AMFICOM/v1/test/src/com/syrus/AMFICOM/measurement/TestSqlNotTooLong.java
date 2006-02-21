/*
 * $Id: TestSqlNotTooLong.java,v 1.4 2006/02/21 11:30:27 saa Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.4 $, $Date: 2006/02/21 11:30:27 $
 * @author $Author: saa $
 * @module test
 */
public final class TestSqlNotTooLong extends TestCase {

	public TestSqlNotTooLong(final String name) {
		super(name);
	}

	public static Test suite() {
		return suiteSql();
	}

	public static Test suiteSql() {
		final CommonTest commonTest = new SQLCommonTest() {
			@Override
			public String getApplicationName() { return "test2"; }
		};
		commonTest.addTestSuite(TestSqlNotTooLong.class);
		return commonTest.createTestSetup();
	}

	private void verifyHostName() {
		final String dbHostName = ApplicationProperties.getString(
				SQLCommonTest.KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(
				SQLCommonTest.KEY_DB_SID, SQLCommonTest.DB_SID);
		final String requiredDbHostName = "mongol";
		final String requiredDbSid = "mongol";

		if (!requiredDbHostName.equals(dbHostName)) {
			throw new RuntimeException("DbHostName should be " + requiredDbHostName);
		}
		if (!requiredDbSid.equals(dbSid)) {
			throw new RuntimeException("DbSid should be " + requiredDbSid);
		}
	}

	public void testDbQuery() throws SQLException, InterruptedException {
		verifyHostName();

		String query = "SELECT id , TO_CHAR(created, 'YYYYMMDD HH24MISS') created , TO_CHAR(modified, 'YYYYMMDD HH24MISS') modified , creator_id , modifier_id , version , type_code , monitored_element_id , measurement_id , name , criteria_set_id FROM Analysis WHERE 1=1 AND  (  ( measurement_id IN  ( 145241087982707182 )  )  )";

		long sleepTime = 100; // this is important
		double maxDelay = 500.0;
		int count = 1000;
		int firstGlanceCount = 100;

		System.out.println("Please be patient, I need up to " +
				Math.max(maxDelay, sleepTime) * count / 1000 + " sec");
		final Connection connection = DatabaseConnection.getConnection();
		Statement statement = connection.createStatement();
		for (int i = 0; i < count; i++) {

			long t0 = System.nanoTime();
			statement.execute(query);
			long t1 = System.nanoTime();

			double dt = (t1 - t0) / 1000 / 1e3;
//			// make user's life funny: print some of timings
//			if (dt > 50) {
//				System.out.println("DbQuery[" + i + "]: "
//						+ dt
//						+ " ms");
//			}

			if (i > 0) {
				assertTrue("Iteration " + i
						+ ": Load time " + dt
						+ " ms is too long (expected no more than "
						+ maxDelay + " ms )", dt < maxDelay);
			}

			if (i == firstGlanceCount) {
				System.out.println("Hm... at first glance, it seems ok... Please keep waiting.");
			}

			if (dt < sleepTime) {
				Thread.sleep(sleepTime); // this is important
			}
		}
		DatabaseConnection.releaseConnection(connection);
	}
}
