/*
 * $Id: TestReflectogramma.java,v 1.1 2005/06/17 15:48:34 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.analysis.dadara;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.AnalysisWrapper;
import com.syrus.AMFICOM.measurement.ResultWrapper;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/17 15:48:34 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestReflectogramma extends TestCase {
	public static final String APPLICATION_NAME = "test";
	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";

	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";

	public TestReflectogramma(String name) {
		super(name);
	}

	public void setUp() {
		Application.init(APPLICATION_NAME);
		establishDatabaseConnection();
	}

	public void tearDown() {
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

	public void testRetrieve() throws SQLException, IOException {
		final String sql = "SELECT " + StorableObjectWrapper.COLUMN_ID + ", " + ResultWrapper.LINK_COLUMN_PARAMETER_VALUE
				+ " FROM " + ObjectEntities.RESULTPARAMETER
				+ " WHERE " + ResultWrapper.LINK_COLUMN_RESULT_ID + " IN ("
					+ "SELECT " + StorableObjectWrapper.COLUMN_ID
					+ " FROM " + ObjectEntities.RESULT
					+ " WHERE " + ResultWrapper.COLUMN_MEASUREMENT_ID + " IN ("
						+ "SELECT " + AnalysisWrapper.COLUMN_MEASUREMENT_ID
							+ " FROM " + ObjectEntities.ANALYSIS
						+ ")"
					+ ")";
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			final String idStr = resultSet.getString(StorableObjectWrapper.COLUMN_ID);
			final byte[] ba = ByteArrayDatabase.toByteArray(resultSet.getBlob(ResultWrapper.LINK_COLUMN_PARAMETER_VALUE));
			final FileOutputStream fos = new FileOutputStream("ref" + idStr);
			fos.write(ba, 0, ba.length);
			fos.close();
		}
	}
}
