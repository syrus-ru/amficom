/*-
 * $Id: ActionParameterDatabase.java,v 1.1.2.1 2006/02/11 18:40:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.ActionParameterWrapper.COLUMN_BINDING_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterWrapper.COLUMN_VALUE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/11 18:40:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameterDatabase extends StorableObjectDatabase<ActionParameter> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ObjectEntities.ACTIONPARAMETER_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_BINDING_ID + COMMA
				+ COLUMN_VALUE;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ SQL_FUNCTION_EMPTY_BLOB;
    	}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final ActionParameter storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getBindingId()) + COMMA
				+ SQL_FUNCTION_EMPTY_BLOB;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final ActionParameter storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getBindingId());
		return startParameterNumber;
	}

	@Override
	protected ActionParameter updateEntityFromResultSet(final ActionParameter storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final ActionParameter actionParameter = (storableObject == null)
				? new ActionParameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null)
					: storableObject;
		actionParameter.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_BINDING_ID),
				ByteArrayDatabase.toByteArray(resultSet.getBlob(COLUMN_VALUE)));
		return actionParameter;
	}

	@Override
	protected void insert(final Set<ActionParameter> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);

		try {
			this.updateValues(storableObjects);
		} catch (SQLException sqle) {
			throw new CreateObjectException("Cannot insert action parameter -- " + sqle.getMessage(), sqle);
		}
	}

	@Override
	protected void update(final Set<ActionParameter> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);

		try {
			this.updateValues(storableObjects);
		} catch (SQLException sqle) {
			throw new UpdateObjectException("Cannot update action parameter -- " + sqle.getMessage(), sqle);
		}
	}

	private void updateValues(final Set<ActionParameter> storableObjects) throws SQLException {
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			for (final ActionParameter actionParameter : storableObjects) {
				ByteArrayDatabase.saveAsBlob(actionParameter.getValue(),
						connection,
						this.getEntityName(),
						COLUMN_VALUE,
						COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(actionParameter.getId()));
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
