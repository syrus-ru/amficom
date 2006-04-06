/*
 * $Id: AnalysisDatabase.java,v 1.76.2.6 2006/04/06 11:14:20 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;


import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_ACTION_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_DURATION;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_MONITORED_ELEMENT_ID;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_START_TIME;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_STATUS;
import static com.syrus.AMFICOM.measurement.AnalysisWrapper.COLUMN_MEASUREMENT_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.Action.ActionStatus;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.76.2.6 $, $Date: 2006/04/06 11:14:20 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class AnalysisDatabase extends ActionDatabase<Analysis, AnalysisResultParameter> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ANALYSIS_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = super.getColumnsTmpl() + COMMA
				+ COLUMN_MEASUREMENT_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = super.getUpdateMultipleSQLValuesTmpl() + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final Analysis storableObject) throws IllegalDataException {
		final String sql = super.getUpdateSingleSQLValuesTmpl(storableObject) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMeasurementId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Analysis storableObject, final PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		startParameterNumber = super.setEntityForPreparedStatementTmpl(storableObject, preparedStatement, startParameterNumber);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementId());
		return startParameterNumber;
	}

	@Override
	protected Analysis updateEntityFromResultSet(final Analysis storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Analysis analysis = (storableObject == null)
				? new Analysis(DatabaseIdentifier.getIdentifier(resultSet,
						COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						0,
						null,
						null)
					: storableObject;
		analysis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MONITORED_ELEMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ACTION_TEMPLATE_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_START_TIME),
				resultSet.getLong(COLUMN_DURATION),
				ActionStatus.valueOf(resultSet.getInt(COLUMN_STATUS)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_ID));
		return analysis;
	}

}
