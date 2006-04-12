/*-
 * $Id: ActionParameterTypeBindingDatabase.java,v 1.1.2.6 2006/04/12 13:03:18 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_MEASUREMENT_PORT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_PARAMETER_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_PARAMETER_VALUE_KIND;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.AMFICOM.measurement.ActionTypeDatabase.ActionTypeKindDatabase;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1.2.6 $, $Date: 2006/04/12 13:03:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameterTypeBindingDatabase extends StorableObjectDatabase<ActionParameterTypeBinding> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ACTIONPARAMETERTYPEBINDING_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_PARAMETER_VALUE_KIND + COMMA
				+ COLUMN_PARAMETER_TYPE_ID + COMMA
				+ ActionTypeKindDatabase.getInstance().getColumns() + COMMA
				+ COLUMN_MEASUREMENT_PORT_TYPE_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ ActionTypeKindDatabase.getInstance().getUpdateMultipleSQLValues() + COMMA
				+ QUESTION;
    	}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final ActionParameterTypeBinding storableObject) throws IllegalDataException {
		final String sql = Integer.toString(storableObject.getParameterValueKind().ordinal()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParameterTypeId()) + COMMA
				+ ActionTypeKindDatabase.getInstance().getUpdateSingleSQLValues(storableObject.getActionTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMeasurementPortTypeId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final ActionParameterTypeBinding storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		preparedStatement.setInt(++startParameterNumber, storableObject.getParameterValueKind().ordinal());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParameterTypeId());
		startParameterNumber = ActionTypeKindDatabase.getInstance().setEntityForPreparedStatement(storableObject.getActionTypeId(),
				preparedStatement,
				startParameterNumber);
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

		final Identifier actionTypeId = ActionTypeKindDatabase.getInstance().getActionTypeId(resultSet);
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
