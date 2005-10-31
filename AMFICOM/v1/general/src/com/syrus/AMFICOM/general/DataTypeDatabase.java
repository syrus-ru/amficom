/*-
 * $Id: DataTypeDatabase.java,v 1.9 2005/10/31 12:30:19 bass Exp $
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
import static com.syrus.AMFICOM.general.TableNames.DATA_TYPE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.9 $, $Date: 2005/10/31 12:30:19 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class DataTypeDatabase {
	private static final String COLUMN_CODE = "code";
	private static final String COLUMN_CODENAME = "codename";
	private static final String COLUMN_DESCRIPTION = "description";

	private DataTypeDatabase() {
		//empty
	}

	public static void insertAll() throws CreateObjectException {
		final String sql = SQL_INSERT_INTO + DATA_TYPE + OPEN_BRACKET
				+ COLUMN_CODE + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int code = 0;
		String codename = null;
		String description = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (final DataType dataType : DataType.values()) {
				code = dataType.getCode();
				codename = dataType.getCodename();
				description = dataType.getDescription();

				Log.debugMessage("Inserting data type of code: " + code + ", codename: '" + codename + "', description: '" + description + "'",
						Log.DEBUGLEVEL09);

				preparedStatement.setInt(1, code);
				DatabaseString.setString(preparedStatement, 2, codename, SIZE_CODENAME_COLUMN);
				DatabaseString.setString(preparedStatement, 3, description, SIZE_DESCRIPTION_COLUMN);

				preparedStatement.executeUpdate();
				connection.commit();
			}
		} catch (SQLException sqle) {
			final String mesg = "Cannot insert data type; code: " + code + ", codename: '" + codename
					+ "', description: '" + description + "' -- " + sqle.getMessage();
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
