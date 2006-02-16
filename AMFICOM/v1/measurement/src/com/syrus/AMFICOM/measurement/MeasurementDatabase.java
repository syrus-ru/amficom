/*
 * $Id: MeasurementDatabase.java,v 1.99.2.3 2006/02/16 11:12:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

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
import static com.syrus.AMFICOM.measurement.MeasurementWrapper.COLUMN_TEST_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.Action.ActionStatus;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.99.2.3 $, $Date: 2006/02/16 11:12:00 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class MeasurementDatabase extends ActionDatabase<Measurement> {

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MEASUREMENT_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = super.getColumnsTmpl() + COMMA
				+ COLUMN_TEST_ID;
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
	protected String getUpdateSingleSQLValuesTmpl(final Measurement storableObject) throws IllegalDataException {
		final String sql = super.getUpdateSingleSQLValuesTmpl(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getTestId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Measurement storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		startParameterNumber = super.setEntityForPreparedStatementTmpl(storableObject, preparedStatement, startParameterNumber);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getTestId());
		return startParameterNumber;
	}

	@Override
	protected Measurement updateEntityFromResultSet(final Measurement storableObject, final ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		final Measurement measurement = (storableObject == null)
				? new Measurement(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
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

		measurement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TEST_ID));
		return measurement;
	}

	public Measurement retrieveLast(final Identifier testId) throws RetrieveObjectException, ObjectNotFoundException {
		assert testId != null : ErrorMessages.NON_NULL_EXPECTED;
		assert testId.getMajor() == ObjectEntities.TEST_CODE : ErrorMessages.ILLEGAL_ENTITY_CODE;

		final String testIdStr = DatabaseIdentifier.toSQLString(testId);
		final String condition = COLUMN_TEST_ID + EQUALS + testIdStr
				+ SQL_AND
						+ COLUMN_START_TIME + EQUALS + OPEN_BRACKET
								+ SQL_SELECT + SQL_FUNCTION_MAX + OPEN_BRACKET + COLUMN_START_TIME + CLOSE_BRACKET
								+ SQL_FROM + this.getEntityName()
								+ SQL_WHERE + COLUMN_TEST_ID + EQUALS + testIdStr
						+ CLOSE_BRACKET;
		try {
			final Set<Measurement> measurements = this.retrieveByCondition(condition);
			if (!measurements.isEmpty()) {
				return measurements.iterator().next();
			}
			throw new ObjectNotFoundException("Cannot find measurements for test '" + testId + "'");
		} catch (IllegalDataException ide) {
			//-Never
			Log.errorMessage(ide);
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

}
