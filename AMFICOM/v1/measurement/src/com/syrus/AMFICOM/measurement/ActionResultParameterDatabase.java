/*-
 * $Id: ActionResultParameterDatabase.java,v 1.1.2.1 2006/02/11 18:40:45 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionResultParameterWrapper.COLUMN_VALUE;

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
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/11 18:40:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionResultParameterDatabase<T extends ActionResultParameter<T>> extends StorableObjectDatabase<T> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_TYPE_ID + COMMA
				+ this.getActionColumnName() + COMMA
				+ COLUMN_VALUE;
		}
		return columns;
	}

	abstract String getActionColumnName();

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ SQL_FUNCTION_EMPTY_BLOB;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final T storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getActionId()) + COMMA
				+ SQL_FUNCTION_EMPTY_BLOB;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final T storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getActionId());
		return startParameterNumber;
	}

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
			for (final T actionResultParameter : storableObjects) {
				ByteArrayDatabase.saveAsBlob(actionResultParameter.getValue(),
						connection,
						this.getEntityName(),
						COLUMN_VALUE,
						COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(actionResultParameter.getId()));
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