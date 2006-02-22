/*-
 * $Id: ActionParameterTypeBindingDatabase.java,v 1.1.2.2 2006/02/22 15:49:27 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_ACTION_TYPE_KIND_CODE;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_ANALYSIS_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_MEASUREMENT_PORT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_MEASUREMENT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_MODELING_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_PARAMETER_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_PARAMETER_VALUE_KIND;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/22 15:49:27 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameterTypeBindingDatabase extends StorableObjectDatabase<ActionParameterTypeBinding> {
	private static enum ActionTypeKind {
		MEASUREMENT_TYPE,
		ANALYSIS_TYPE,
		MODELING_TYPE;

		private static final ActionTypeKind VALUES[] = values();

		public static ActionTypeKind valueOf(final int code) {
			return VALUES[code];
		}

		public static ActionTypeKind valueOf(final Identifier actionTypeId) {
			switch (actionTypeId.getMajor()) {
				case ObjectEntities.MEASUREMENT_TYPE_CODE:
					return MEASUREMENT_TYPE;
				case ObjectEntities.ANALYSIS_TYPE_CODE:
					return ANALYSIS_TYPE;
				case ObjectEntities.MODELING_TYPE_CODE:
					return MODELING_TYPE;
				default:
					throw new IllegalArgumentException("Illegal identifier: " + actionTypeId);
			}
		}

		@Override
		public String toString() {
			return Integer.toString(this.ordinal());
		}
	}

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_PARAMETER_VALUE_KIND + COMMA
				+ COLUMN_PARAMETER_TYPE_ID + COMMA
				+ COLUMN_ACTION_TYPE_KIND_CODE + COMMA
				+ COLUMN_MEASUREMENT_TYPE_ID + COMMA
				+ COLUMN_ANALYSIS_TYPE_ID + COMMA
				+ COLUMN_MODELING_TYPE_ID + COMMA
				+ COLUMN_MEASUREMENT_PORT_TYPE_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
    	}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final ActionParameterTypeBinding storableObject) throws IllegalDataException {
		final ActionTypeKind actionTypeKind = ActionTypeKind.valueOf(storableObject.getActionTypeId());
		String actionTypeSql;
		switch (actionTypeKind) {
			case MEASUREMENT_TYPE:
				actionTypeSql = DatabaseIdentifier.toSQLString(storableObject.getActionTypeId()) + COMMA
						+ SQL_NULL + COMMA
						+ SQL_NULL;
				break;
			case ANALYSIS_TYPE:
				actionTypeSql = SQL_NULL + COMMA
						+ DatabaseIdentifier.toSQLString(storableObject.getActionTypeId()) + COMMA
						+ SQL_NULL;
				break;
			case MODELING_TYPE:
				actionTypeSql = SQL_NULL + COMMA
						+ SQL_NULL + COMMA
						+ DatabaseIdentifier.toSQLString(storableObject.getActionTypeId());
			default:
				throw new IllegalArgumentException("Unknown action type kind: " + actionTypeKind);
		}
		final String sql = Integer.toString(storableObject.getParameterValueKind().ordinal()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParameterTypeId()) + COMMA
				+ Integer.toString(actionTypeKind.ordinal()) + COMMA
				+ actionTypeSql + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMeasurementPortTypeId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final ActionParameterTypeBinding storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		preparedStatement.setInt(++startParameterNumber, storableObject.getParameterValueKind().ordinal());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParameterTypeId());

		final Identifier actionTypeId = storableObject.getActionTypeId();
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

		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementPortTypeId());

		return startParameterNumber;
	}

	@Override
	protected ActionParameterTypeBinding updateEntityFromResultSet(final ActionParameterTypeBinding storableObject,
			final ResultSet resultSet) throws IllegalDataException, RetrieveObjectException, SQLException {
		final ActionParameterTypeBinding actionParameterTypeBinding = (storableObject == null)
				? new ActionParameterTypeBinding(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
					: storableObject;


		final ActionTypeKind actionTypeKind = ActionTypeKind.valueOf(resultSet.getInt(COLUMN_ACTION_TYPE_KIND_CODE));
		Identifier actionTypeId;
		switch (actionTypeKind) {
			case MEASUREMENT_TYPE:
				actionTypeId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_TYPE_ID);
				break;
			case ANALYSIS_TYPE:
				actionTypeId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ANALYSIS_TYPE_ID);
				break;
			case MODELING_TYPE:
				actionTypeId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODELING_TYPE_ID);
				break;
			default:
				throw new IllegalArgumentException("Unknown action type kind: " + actionTypeKind);
		}
		actionParameterTypeBinding.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				ParameterValueKind.valueOf(resultSet.getInt(COLUMN_PARAMETER_VALUE_KIND)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARAMETER_TYPE_ID),
				actionTypeId,
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_PORT_TYPE_ID));
		return actionParameterTypeBinding;
	}

}
