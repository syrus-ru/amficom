/*-
 * $Id: ActionTypeDatabase.java,v 1.17.2.3 2006/04/12 13:00:03 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.measurement.ActionTypeWrapper.ActionTypeKindWrapper.COLUMN_ACTION_TYPE_KIND_CODE;
import static com.syrus.AMFICOM.measurement.ActionTypeWrapper.ActionTypeKindWrapper.COLUMN_ANALYSIS_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionTypeWrapper.ActionTypeKindWrapper.COLUMN_MEASUREMENT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionTypeWrapper.ActionTypeKindWrapper.COLUMN_MODELING_TYPE_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.measurement.ActionType.ActionTypeKind;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.17.2.3 $, $Date: 2006/04/12 13:00:03 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionTypeDatabase<T extends ActionType> extends StorableObjectDatabase<T> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected final String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION;
		}
		return columns;
	}

	@Override
	protected final String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION;
    	}
		return updateMultipleSQLValues;
	}

	@Override
	protected final String getUpdateSingleSQLValuesTmpl(final T storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected final int setEntityForPreparedStatementTmpl(final T storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}


	static class ActionTypeKindDatabase {
		private static String columns1;
		private static String updateMultipleSQLValues1;

		private static ActionTypeKindDatabase instance;

		static ActionTypeKindDatabase getInstance() {
			if (instance == null) {
				instance = new ActionTypeKindDatabase();
			}
			return instance;
		}

		String getColumns() {
			if (columns1 == null) {
				columns1 = COLUMN_ACTION_TYPE_KIND_CODE + COMMA
				+ COLUMN_MEASUREMENT_TYPE_ID + COMMA
				+ COLUMN_ANALYSIS_TYPE_ID + COMMA
				+ COLUMN_MODELING_TYPE_ID;
			}
			return columns1;
		}

		String getUpdateMultipleSQLValues() {
			if (updateMultipleSQLValues1 == null) {
				updateMultipleSQLValues1 = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
			}
			return updateMultipleSQLValues1;
		}

		String getUpdateSingleSQLValues(final Identifier actionTypeId) {
			final ActionTypeKind actionTypeKind = ActionTypeKind.valueOf(actionTypeId);
			final StringBuffer sql = new StringBuffer(Integer.toString(actionTypeKind.ordinal()));
			sql.append(COMMA);
			switch (actionTypeKind) {
				case MEASUREMENT_TYPE:
					sql.append(DatabaseIdentifier.toSQLString(actionTypeId));
					sql.append(COMMA);
					sql.append(SQL_NULL);
					sql.append(COMMA);
					sql.append(SQL_NULL);
					break;
				case ANALYSIS_TYPE:
					sql.append(SQL_NULL);
					sql.append(COMMA);
					sql.append(DatabaseIdentifier.toSQLString(actionTypeId));
					sql.append(COMMA);
					sql.append(SQL_NULL);
					break;
				case MODELING_TYPE:
					sql.append(SQL_NULL);
					sql.append(COMMA);
					sql.append(SQL_NULL);
					sql.append(COMMA);
					sql.append(DatabaseIdentifier.toSQLString(actionTypeId));
					break;
				default:
					throw new IllegalArgumentException("Unknown action type kind: " + actionTypeKind);
			}
			return sql.toString();
		}

		int setEntityForPreparedStatement(final Identifier actionTypeId,
				final PreparedStatement preparedStatement,
				int startParameterNumber) throws SQLException {
			final ActionTypeKind actionTypeKind = ActionTypeKind.valueOf(actionTypeId);
			preparedStatement.setInt(++startParameterNumber, actionTypeKind.ordinal());
			switch (actionTypeKind) {
				case MEASUREMENT_TYPE:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, actionTypeId);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
					break;
				case ANALYSIS_TYPE:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, actionTypeId);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
					break;
				case MODELING_TYPE:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, actionTypeId);
					break;
				default:
					throw new IllegalArgumentException("Unknown action type kind: " + actionTypeKind);
			}
			return startParameterNumber;
		}

		Identifier getActionTypeId(final ResultSet resultSet) throws SQLException {
			final ActionTypeKind actionTypeKind = ActionTypeKind.valueOf(resultSet.getInt(COLUMN_ACTION_TYPE_KIND_CODE));
			switch (actionTypeKind) {
				case MEASUREMENT_TYPE:
					return DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_TYPE_ID);
				case ANALYSIS_TYPE:
					return DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ANALYSIS_TYPE_ID);
				case MODELING_TYPE:
					return DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODELING_TYPE_ID);
				default:
					throw new IllegalArgumentException("Unknown action type kind: " + actionTypeKind);
			}
		}
	}

}
