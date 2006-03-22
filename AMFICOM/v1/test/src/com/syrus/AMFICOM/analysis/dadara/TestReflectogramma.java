/*
 * $Id: TestReflectogramma.java,v 1.3.2.1 2006/03/22 08:53:59 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.analysis.dadara;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTRESULTPARAMETER;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.measurement.MeasurementResultParameterWrapper.COLUMN_MEASUREMENT_ID;
import static com.syrus.AMFICOM.measurement.ParameterWrapper.COLUMN_VALUE;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

import com.syrus.AMFICOM.measurement.AnalysisWrapper;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.3.2.1 $, $Date: 2006/03/22 08:53:59 $
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

	@Override
	public void setUp() {
		Application.init(APPLICATION_NAME);
		establishDatabaseConnection();
	}

	@Override
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
			Log.errorMessage(e);
			System.exit(-1);
		}
	}

	public void testRetrieve() throws SQLException, IOException {
		final String sql = SQL_SELECT
				+ COLUMN_ID + COMMA
				+ COLUMN_VALUE
				+ SQL_FROM + MEASUREMENTRESULTPARAMETER
				+ SQL_WHERE + COLUMN_MEASUREMENT_ID + SQL_IN + OPEN_BRACKET
					+ SQL_SELECT + AnalysisWrapper.COLUMN_MEASUREMENT_ID
					+ SQL_FROM + ANALYSIS
				+ CLOSE_BRACKET;
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			final String idStr = resultSet.getString(COLUMN_ID);
			final byte[] ba = ByteArrayDatabase.toByteArray(resultSet.getBlob(COLUMN_VALUE));
			final FileOutputStream fos = new FileOutputStream("ref" + idStr);
			fos.write(ba, 0, ba.length);
			fos.close();
		}
	}
}
