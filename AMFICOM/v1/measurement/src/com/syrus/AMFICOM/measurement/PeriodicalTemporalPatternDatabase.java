/*-
 * $Id: PeriodicalTemporalPatternDatabase.java,v 1.13 2005/12/02 11:24:09 bass Exp $
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.13 $, $Date: 2005/12/02 11:24:09 $
 * @author $Author: bass $
 * @module measurement
 */

public final class PeriodicalTemporalPatternDatabase extends StorableObjectDatabase<PeriodicalTemporalPattern> {
	private static String columns;
	private static String updateMultipleSQLValues;

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
	protected String getUpdateSingleSQLValuesTmpl(final PeriodicalTemporalPattern storableObject) throws IllegalDataException {
		final String values = "" + storableObject.getPeriod();
		return values;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final PeriodicalTemporalPattern storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {		
		preparedStatement.setLong(++startParameterNumber, storableObject.getPeriod());
		return startParameterNumber;
	}

	@Override
	protected PeriodicalTemporalPattern updateEntityFromResultSet(final PeriodicalTemporalPattern storableObject,
			final ResultSet resultSet) throws IllegalDataException, SQLException {
		final PeriodicalTemporalPattern periodicalTemporalPattern = (storableObject == null)
				? new PeriodicalTemporalPattern(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						0)
					: storableObject;
		periodicalTemporalPattern.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				resultSet.getLong(PeriodicalTemporalPatternWrapper.COLUMN_PERIOD));
		return periodicalTemporalPattern;
	}

}
