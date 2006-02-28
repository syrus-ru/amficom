/*-
 * $Id: ActionParameterDatabase.java,v 1.1.2.4 2006/02/28 15:20:04 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.ActionParameterWrapper.COLUMN_BINDING_ID;
import static com.syrus.AMFICOM.measurement.ParameterWrapper.COLUMN_VALUE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2006/02/28 15:20:04 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionParameterDatabase extends ParameterDatabase<ActionParameter> {

	@Override
	protected short getEntityCode() {
		return ACTIONPARAMETER_CODE;
	}

	@Override
	String getColumnsTmplTmpl() {
		return COLUMN_BINDING_ID;
	}

	@Override
	String getUpdateMultipleSQLValuesTmplTmpl() {
		return QUESTION;
	}

	@Override
	String getUpdateSingleSQLValuesTmplTmpl(final ActionParameter storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getBindingId());
		return sql;
	}

	@Override
	int setEntityForPreparedStatementTmplTmpl(final ActionParameter storableObject,
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
				? new ActionParameter(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null,
						null)
					: storableObject;
		actionParameter.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				ByteArrayDatabase.toByteArray(resultSet.getBlob(COLUMN_VALUE)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_BINDING_ID));
		return actionParameter;
	}
}
