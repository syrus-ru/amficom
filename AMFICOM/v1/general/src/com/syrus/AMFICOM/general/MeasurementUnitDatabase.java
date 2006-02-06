/*-
 * $Id: MeasurementUnitDatabase.java,v 1.6.2.1 2006/02/06 14:46:30 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.QUESTION;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SIZE_CODENAME_COLUMN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SIZE_NAME_COLUMN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_INSERT_INTO;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_VALUES;
import static com.syrus.AMFICOM.general.TableNames.MEASUREMENT_UNIT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.6.2.1 $, $Date: 2006/02/06 14:46:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class MeasurementUnitDatabase {
	private static final String COLUMN_CODE = "code";
	private static final String COLUMN_CODENAME = "codename";
	private static final String COLUMN_NAME = "name";

	private MeasurementUnitDatabase() {
		//Empty
	}

	public static void insertAll() throws CreateObjectException {
		final String sql = SQL_INSERT_INTO + MEASUREMENT_UNIT + OPEN_BRACKET
				+ COLUMN_CODE + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_NAME
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int code = 0;
		String codename = null;
		String name = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (final MeasurementUnit measurementUnit : MeasurementUnit.values()) {
				code = measurementUnit.ordinal();
				codename = measurementUnit.getCodename();
				name = measurementUnit.getName();

				Log.debugMessage("Inserting measurement unit of code: " + code + ", codename: '" + codename + "', name: '" + name + "'",
						Log.DEBUGLEVEL09);

				preparedStatement.setInt(1, code);
				DatabaseString.setString(preparedStatement, 2, codename, SIZE_CODENAME_COLUMN);
				DatabaseString.setString(preparedStatement, 3, name, SIZE_NAME_COLUMN);

				preparedStatement.executeUpdate();
				connection.commit();
			}
		} catch (SQLException sqle) {
			final String mesg = "Cannot insert measurement unit; code: " + code + ", codename: '" + codename
					+ "', name: '" + name + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
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
				Log.errorMessage(sqle1);
			}
		}

	}
}
