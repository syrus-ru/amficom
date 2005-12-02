/*
 * $Id: CronTemporalPatternDatabase.java,v 1.15 2005/12/02 11:24:09 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OracleResultSet;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.ora.CronStringArray;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.15 $, $Date: 2005/12/02 11:24:09 $
 * @author $Author: bass $
 * @module measurement
 */

public final class CronTemporalPatternDatabase extends StorableObjectDatabase<CronTemporalPattern> {
	private static final String CRONSTRINGARRAY_TYPE_NAME = "CronStringArray";

	private static String columns;
	private static String updateMultipleSQLValues;	

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.CRONTEMPORALPATTERN_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ TemporalPatternWrapper.COLUMN_VALUE;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {	
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	private StringBuffer getUpdateCronStringArray(final CronTemporalPattern temporalPattern) {
		final String[] cronStrings = temporalPattern.getCronStrings();
		final StringBuffer stringBuffer = new StringBuffer(CRONSTRINGARRAY_TYPE_NAME + OPEN_BRACKET);
		for (int i = 0; i < cronStrings.length; i++) {
			stringBuffer.append(APOSTROPHE);
			stringBuffer.append(cronStrings[i]);
			stringBuffer.append(APOSTROPHE);
			if (i != cronStrings.length -1)
				stringBuffer.append(COMMA);
		}
		stringBuffer.append(CLOSE_BRACKET);
		
		return stringBuffer;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final CronTemporalPattern storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ this.getUpdateCronStringArray(storableObject);
		return sql;
	}

	@Override
	protected CronTemporalPattern updateEntityFromResultSet(final CronTemporalPattern storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final CronTemporalPattern temporalPattern = (storableObject == null)
				? new CronTemporalPattern(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null)
					: storableObject;
		final String[] cronStrings = ((CronStringArray) (((OracleResultSet) resultSet).getORAData(TemporalPatternWrapper.COLUMN_VALUE,
				CronStringArray.getORADataFactory()))).getArray();
		temporalPattern.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				cronStrings);
		return temporalPattern;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final CronTemporalPattern storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		preparedStatement.setString(++startParameterNumber, storableObject.getDescription());
		((OraclePreparedStatement) preparedStatement).setORAData(++startParameterNumber,
				new CronStringArray(storableObject.getCronStrings()));
		return startParameterNumber;
	}

}
