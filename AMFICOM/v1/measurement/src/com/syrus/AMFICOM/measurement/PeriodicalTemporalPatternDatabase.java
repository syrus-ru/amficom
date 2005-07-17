/*-
 * $Id: PeriodicalTemporalPatternDatabase.java,v 1.8 2005/07/17 05:07:55 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */


package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.8 $, $Date: 2005/07/17 05:07:55 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class PeriodicalTemporalPatternDatabase extends StorableObjectDatabase {

	private static String columns;

	private static String updateMultipleSQLValues;

	private PeriodicalTemporalPattern fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof PeriodicalTemporalPattern)
			return (PeriodicalTemporalPattern) storableObject;
		throw new IllegalDataException("PeriodicalTemporalPatternDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.PERIODICALTEMPORALPATTERN_CODE;
	}	

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = PeriodicalTemporalPatternWrapper.COLUMN_PERIOD;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final PeriodicalTemporalPattern periodicalTemporalPattern = this.fromStorableObject(storableObject);
		final String values = "" + periodicalTemporalPattern.getPeriod();
		return values;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {		
		final PeriodicalTemporalPattern periodicalTemporalPattern = this.fromStorableObject(storableObject);
		preparedStatement.setLong(++startParameterNumber, periodicalTemporalPattern.getPeriod());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final PeriodicalTemporalPattern periodicalTemporalPattern = (storableObject == null) ?
				new PeriodicalTemporalPattern(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								0L,
								0) :
					this.fromStorableObject(storableObject);
		periodicalTemporalPattern.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				resultSet.getLong(PeriodicalTemporalPatternWrapper.COLUMN_PERIOD));
		return periodicalTemporalPattern;
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg) throws IllegalDataException {
		final PeriodicalTemporalPattern periodicalTemporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  periodicalTemporalPattern.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
