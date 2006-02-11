/*-
 * $Id: ActionParameterTypeBindingDatabase.java,v 1.1.2.1 2006/02/11 18:40:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_ACTION_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_ACTION_TYPE_KIND_CODE;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_MEASUREMENT_PORT_TYPE_ID;
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
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/11 18:40:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameterTypeBindingDatabase extends StorableObjectDatabase<ActionParameterTypeBinding> {
	private enum ActionTypeKind {
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
				+ COLUMN_ACTION_TYPE_ID + COMMA
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
				+ QUESTION;
    	}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final ActionParameterTypeBinding storableObject) throws IllegalDataException {
		final String sql = Integer.toString(storableObject.getParameterValueKind().ordinal()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParameterTypeId()) + COMMA
				+ Integer.toString(ActionTypeKind.valueOf(storableObject.getActionTypeId()).ordinal()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getActionTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMeasurementPortTypeId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final ActionParameterTypeBinding storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Identifier actionTypeId = storableObject.getActionTypeId();
		preparedStatement.setInt(++startParameterNumber, storableObject.getParameterValueKind().ordinal());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParameterTypeId());
		preparedStatement.setInt(++startParameterNumber, ActionTypeKind.valueOf(actionTypeId).ordinal());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getActionTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementPortTypeId());
		return startParameterNumber;
	}

	@Override
	protected ActionParameterTypeBinding updateEntityFromResultSet(final ActionParameterTypeBinding storableObject,
			final ResultSet resultSet) throws IllegalDataException, RetrieveObjectException, SQLException {
		final ActionParameterTypeBinding actionParameterTypeBinding = (storableObject == null)
				? new ActionParameterTypeBinding(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
					: storableObject;

		actionParameterTypeBinding.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				ParameterValueKind.valueOf(resultSet.getInt(COLUMN_PARAMETER_VALUE_KIND)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARAMETER_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ACTION_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_PORT_TYPE_ID));
		return actionParameterTypeBinding;
	}

}
