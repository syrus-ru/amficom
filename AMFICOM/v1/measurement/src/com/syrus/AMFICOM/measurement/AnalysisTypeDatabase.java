/*-
 * $Id: AnalysisTypeDatabase.java,v 1.109 2005/10/30 15:20:39 bass Exp $
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
 * @version $Revision: 1.109 $, $Date: 2005/10/30 15:20:39 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class AnalysisTypeDatabase {

	private AnalysisTypeDatabase() {
		//Empty
	}

	public static void insertAll() throws CreateObjectException {
		final String sql = SQL_INSERT_INTO + TableNames.ANALYSIS_TYPE + OPEN_BRACKET
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
			assert Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			for (final AnalysisType analysisType : AnalysisType.values()) {
				preparedStatement.setInt(1, analysisType.getCode());
				preparedStatement.setString(2, analysisType.getCodename());
				assert Log.debugMessage("Inserting analysis type '" + analysisType.getCodename() + "'", Log.DEBUGLEVEL09);
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
				assert Log.errorMessage(sqle1);
			}
		}
	}
}
