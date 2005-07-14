/*
 * $Id: CronTemporalPatternDatabase.java,v 1.8 2005/07/14 16:08:07 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OracleResultSet;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.ora.CronStringArray;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.8 $, $Date: 2005/07/14 16:08:07 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class CronTemporalPatternDatabase extends StorableObjectDatabase {
	private static final String CRONSTRINGARRAY_TYPE_NAME = "CronStringArray";

	private CronTemporalPattern fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CronTemporalPattern)
			return (CronTemporalPattern)storableObject;
		throw new IllegalDataException("CronTemporalPatternDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

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
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final CronTemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(temporalPattern.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ this.getUpdateCronStringArray(temporalPattern);
		return sql;
	}

	@Override
	public void retrieve(final StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final CronTemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.retrieveEntity(temporalPattern);
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final CronTemporalPattern temporalPattern = (storableObject == null)
				? new CronTemporalPattern(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						0L,
						null,
						null) : this.fromStorableObject(storableObject);
		final String[] cronStrings = ((CronStringArray) (((OracleResultSet) resultSet).getORAData(TemporalPatternWrapper.COLUMN_VALUE,
				CronStringArray.getORADataFactory()))).getArray();
		temporalPattern.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				cronStrings);
		return temporalPattern;
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg) throws IllegalDataException {
		final CronTemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  temporalPattern.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		final CronTemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.insertTemporalPattern(temporalPattern);
	}
	
	@Override
	public void insert(final Set<? extends StorableObject> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	private PreparedStatement insertTemporalPatternPreparedStatement() throws SQLException{
		PreparedStatement preparedStatement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try{
			final String sql = SQL_INSERT_INTO + ObjectEntities.CRONTEMPORALPATTERN
			+ OPEN_BRACKET
			+ this.getColumns(ExecuteMode.MODE_INSERT)
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ this.getInsertMultipleSQLValues()
			+ CLOSE_BRACKET;
			preparedStatement = connection.prepareStatement(sql);
		} finally {
			DatabaseConnection.releaseConnection(connection);
		}
		return preparedStatement;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final CronTemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		preparedStatement.setString(++startParameterNumber, temporalPattern.getDescription());
		((OraclePreparedStatement) preparedStatement).setORAData(++startParameterNumber, new CronStringArray(temporalPattern.getCronStrings()));
		return startParameterNumber;
	}

	private void insertTemporalPattern(final CronTemporalPattern temporalPattern) throws CreateObjectException {
		final String tpIdCode = DatabaseIdentifier.toSQLString(temporalPattern.getId());

		PreparedStatement preparedStatement = null;
		try {			
			preparedStatement = this.insertTemporalPatternPreparedStatement();
			this.setEntityForPreparedStatement(temporalPattern, preparedStatement, ExecuteMode.MODE_INSERT);
			Log.debugMessage("CronTemporalPatternDatabase.insertTemporalPattern | Inserting temporal pattern " + tpIdCode, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		} catch (IllegalDataException ide){
			final String mesg = "CronTemporalPatternDatabase.insertTemporalPattern | Cannot insert temporal pattern '" + tpIdCode + "' -- " + ide.getMessage();
			throw new CreateObjectException(mesg, ide);
		} catch (SQLException sqle) {
			final String mesg = "CronTemporalPatternDatabase.insertTemporalPattern | Cannot insert temporal pattern '" + tpIdCode + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

}
