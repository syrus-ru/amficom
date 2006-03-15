/*-
 * $Id: ActionDatabase.java,v 1.1.2.4 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_ACTION_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_DURATION;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_MONITORED_ELEMENT_ID;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_START_TIME;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_STATUS;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionDatabase<A extends Action<R>, R extends ActionResultParameter<A>> extends StorableObjectDatabase<A> {
	static String columns;
	static String updateMultipleSQLValues;

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_TYPE_ID + COMMA
				+ COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ COLUMN_ACTION_TEMPLATE_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_START_TIME + COMMA
				+ COLUMN_DURATION + COMMA
				+ COLUMN_STATUS;
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
	protected String getUpdateSingleSQLValuesTmpl(final A storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMonitoredElementId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getActionTemplateId()) + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseDate.toUpdateSubString(storableObject.getStartTime()) + COMMA
				+ Long.toString(storableObject.getDuration()) + COMMA
				+ Integer.toString(storableObject.getStatus().ordinal());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final A storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getActionTemplateId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		preparedStatement.setTimestamp(++startParameterNumber, new Timestamp(storableObject.getStartTime().getTime()));
		preparedStatement.setLong(++startParameterNumber, storableObject.getDuration());
		preparedStatement.setInt(++startParameterNumber, storableObject.getStatus().ordinal());
		return startParameterNumber;
	}

	@Override
	protected String retrieveQuery(final String condition) {
		String query = super.retrieveQuery(condition);
		query = query.replaceFirst(COLUMN_START_TIME, DatabaseDate.toQuerySubString(COLUMN_START_TIME));
		return query;
	}

}
