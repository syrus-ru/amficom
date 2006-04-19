/*-
 * $Id: ParameterTypeDatabase.java,v 1.44 2006/04/19 13:22:17 bass Exp $
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
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SIZE_DESCRIPTION_COLUMN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_INSERT_INTO;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_VALUES;
import static com.syrus.AMFICOM.general.TableNames.PARAMETER_TYPE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.44 $, $Date: 2006/04/19 13:22:17 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class ParameterTypeDatabase {
	private static final String COLUMN_CODE = "code";
	private static final String COLUMN_CODENAME = "codename";
	private static final String COLUMN_DATA_TYPE_CODE = "data_type_code";
	private static final String COLUMN_MEASUREMENT_UNIT_CODE = "measurement_unit_code";
	private static final String COLUMN_DESCRIPTION = "description";

	private ParameterTypeDatabase() {
		//Empty
	}

	public static void insertAll() throws CreateObjectException {
		final String sql = SQL_INSERT_INTO + PARAMETER_TYPE + OPEN_BRACKET
		+ COLUMN_CODE + COMMA
		+ COLUMN_CODENAME + COMMA
		+ COLUMN_DATA_TYPE_CODE + COMMA
		+ COLUMN_MEASUREMENT_UNIT_CODE + COMMA
		+ COLUMN_DESCRIPTION
		+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
		+ QUESTION + COMMA
		+ QUESTION + COMMA
		+ QUESTION + COMMA
		+ QUESTION + COMMA
		+ QUESTION
		+ CLOSE_BRACKET;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int code = 0;
		String codename = null;
		DataType dataType = null;
		MeasurementUnit measurementUnit = null;
		String description = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (final ParameterType parameterType : ParameterType.values()) {
				code = parameterType.ordinal();
				codename = parameterType.getCodename();
				dataType = parameterType.getDataType();
				measurementUnit = parameterType.getMeasurementUnit();
				description = parameterType.getDescription();

				Log.debugMessage("Inserting parameter type of code: " + code
						+ ", codename: '" + codename
						+ ", data type: '" + dataType.getCodename()
						+ ", measurement unit: '" + measurementUnit.getCodename()
						+ "', description: '" + description
						+ "'", Log.DEBUGLEVEL09);

				preparedStatement.setInt(1, code);
				DatabaseString.setString(preparedStatement, 2, codename, SIZE_CODENAME_COLUMN);
				preparedStatement.setInt(3, dataType.ordinal());
				preparedStatement.setInt(4, measurementUnit.ordinal());
				DatabaseString.setString(preparedStatement, 5, description, SIZE_DESCRIPTION_COLUMN);

				try {
					preparedStatement.executeUpdate();
				} catch (SQLException sqle) {
					Log.errorMessage(sqle);
					continue;
				}
				connection.commit();
			}
		} catch (SQLException sqle) {
			final String mesg = "Cannot insert parameter type; code: " + code
					+ ", codename: '" + codename
					+ ", data type: '" + (dataType == null ? "null" : dataType.getCodename())
					+ ", measurement unit: '" + (measurementUnit == null ? "null" : measurementUnit.getCodename())
					+ "', description: '" + description
					+ "' -- " + sqle.getMessage();
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
