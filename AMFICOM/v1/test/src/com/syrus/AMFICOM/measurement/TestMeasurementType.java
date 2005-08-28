/*
 * $Id: TestMeasurementType.java,v 1.9 2005/08/28 16:43:51 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.QUESTION;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_INSERT_INTO;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_VALUES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TableNames;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.9 $, $Date: 2005/08/28 16:43:51 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestMeasurementType extends TestCase {

	public TestMeasurementType(String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestMeasurementType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws CreateObjectException {
		final String sql = SQL_INSERT_INTO + TableNames.MEASUREMENT_TYPE + OPEN_BRACKET
				+ StorableObjectWrapper.COLUMN_CODE + COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage("TestMeasurementType.testCreateAll | Trying: " + sql, Log.DEBUGLEVEL09);
			for (final MeasurementType measurementType : MeasurementType.values()) {
				preparedStatement.setInt(1, measurementType.getCode());
				preparedStatement.setString(2, measurementType.getCodename());
				Log.debugMessage("TestMeasurementType.testCreateAll | Inserting measurement type '" + measurementType.getCodename() + "'",
						Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		} catch (SQLException sqle) {
			throw new CreateObjectException(sqle.getMessage(), sqle);
		} finally {
			try {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
						preparedStatement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

}
