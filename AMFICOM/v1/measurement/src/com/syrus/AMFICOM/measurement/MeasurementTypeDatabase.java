/*-
 * $Id: MeasurementTypeDatabase.java,v 1.118 2006/03/27 10:10:07 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TableNames;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.118 $, $Date: 2006/03/27 10:10:07 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementTypeDatabase {

	private MeasurementTypeDatabase() {
		//Empty
	}

	public static void insertAll() throws CreateObjectException {
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
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			for (final MeasurementType measurementType : MeasurementType.values()) {
				preparedStatement.setInt(1, measurementType.ordinal());
				preparedStatement.setString(2, measurementType.getCodename());
				Log.debugMessage("Inserting measurement type '" + measurementType.getCodename() + "'", Log.DEBUGLEVEL09);
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
				Log.errorMessage(sqle1);
			}
		}
	}
}
