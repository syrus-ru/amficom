/*-
 * $Id: ParameterDatabase.java,v 1.1.2.2 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.measurement.ParameterWrapper.COLUMN_VALUE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ParameterDatabase<T extends Parameter> extends StorableObjectDatabase<T> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected final String getColumnsTmpl() {
		if (columns == null) {
			return COLUMN_VALUE + COMMA
				+ this.getColumnsTmplTmpl();
		}
		return columns;
	}

	abstract String getColumnsTmplTmpl();

	@Override
	protected final String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = SQL_FUNCTION_EMPTY_BLOB + COMMA
				+ this.getUpdateMultipleSQLValuesTmplTmpl();
    	}
		return updateMultipleSQLValues;
	}

	abstract String getUpdateMultipleSQLValuesTmplTmpl();

	@Override
	protected final String getUpdateSingleSQLValuesTmpl(final T storableObject) throws IllegalDataException {
		final String sql = SQL_FUNCTION_EMPTY_BLOB + COMMA
			+ this.getUpdateSingleSQLValuesTmplTmpl(storableObject);
		return sql;
	}

	abstract String getUpdateSingleSQLValuesTmplTmpl(final T storableObject) throws IllegalDataException;

	@Override
	protected final int setEntityForPreparedStatementTmpl(final T storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		return this.setEntityForPreparedStatementTmplTmpl(storableObject, preparedStatement, startParameterNumber);
	}

	abstract int setEntityForPreparedStatementTmplTmpl(final T storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException;

	@Override
	protected void insert(final Set<T> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);

		try {
			this.updateValues(storableObjects);
		} catch (SQLException sqle) {
			throw new CreateObjectException("Cannot insert action parameter -- " + sqle.getMessage(), sqle);
		}
	}

	@Override
	protected void update(final Set<T> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);

		try {
			this.updateValues(storableObjects);
		} catch (SQLException sqle) {
			throw new UpdateObjectException("Cannot update action parameter -- " + sqle.getMessage(), sqle);
		}
	}

	private void updateValues(final Set<T> storableObjects) throws SQLException {
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			for (final T parameter : storableObjects) {
				ByteArrayDatabase.saveAsBlob(parameter.getValue(),
						connection,
						this.getEntityName(),
						COLUMN_VALUE,
						COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameter.getId()));
			}
			connection.commit();
		} catch (SQLException sqle) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException sqle1) {
					Log.errorMessage(sqle1);
				}
			}
			throw sqle;
		} finally {
			if (connection != null) {
				DatabaseConnection.releaseConnection(connection);
				connection = null;
			}
		}
	}

}
