/*
 * $Id: TemporalPatternDatabase.java,v 1.49 2005/03/11 09:08:23 bob Exp $
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
import java.util.Collection;

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
 * @version $Revision: 1.49 $, $Date: 2005/03/11 09:08:23 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class TemporalPatternDatabase extends StorableObjectDatabase {
	private static final String CRONSTRINGARRAY_TYPE_NAME = "CronStringArray";

	private TemporalPattern fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TemporalPattern)
			return (TemporalPattern)storableObject;
		throw new IllegalDataException("TemporalPatternDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	private static String columns;
	private static String updateMultipleSQLValues;	
	
	protected String getEnityName() {		
		return ObjectEntities.TEMPORALPATTERN_ENTITY;
	}
	
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ TemporalPatternWrapper.COLUMN_VALUE;
		}
		return columns;
	}	

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {	
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	private StringBuffer getUpdateCronStringArray(TemporalPattern temporalPattern) {
		String[] cronStrings = temporalPattern.getCronStrings();
		StringBuffer stringBuffer = new StringBuffer(CRONSTRINGARRAY_TYPE_NAME + OPEN_BRACKET);
		for (int i = 0; i < cronStrings.length; i++) {
			stringBuffer.append(APOSTOPHE);
			stringBuffer.append(cronStrings[i]);
			stringBuffer.append(APOSTOPHE);
			if (i != cronStrings.length -1)
				stringBuffer.append(COMMA);
		}
		stringBuffer.append(CLOSE_BRACKET);
		
		return stringBuffer;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(temporalPattern.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ this.getUpdateCronStringArray(temporalPattern);
		return sql;
	}	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.retrieveEntity(temporalPattern);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		TemporalPattern temporalPattern = (storableObject == null) ?
				new TemporalPattern(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null):
				this.fromStorableObject(storableObject);
		String[] cronStrings = ((CronStringArray) (((OracleResultSet) resultSet).getORAData(TemporalPatternWrapper.COLUMN_VALUE,
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws ObjectNotFoundException, RetrieveObjectException, IllegalDataException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  temporalPattern.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.insertTemporalPattern(temporalPattern);
	}
	
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}
	
	private PreparedStatement insertTemporalPatternPreparedStatement() throws SQLException{
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try{
			String sql = SQL_INSERT_INTO + ObjectEntities.TEMPORALPATTERN_ENTITY 
			+ OPEN_BRACKET
			+ this.getColumns(MODE_INSERT)
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ this.getInsertMultipleSQLValues()
			+ CLOSE_BRACKET;
			preparedStatement = connection.prepareStatement(sql);
		}
		finally {
			DatabaseConnection.releaseConnection(connection);
		}
		return preparedStatement;
	}
	
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		preparedStatement.setString(++startParameterNumber, temporalPattern.getDescription());
		((OraclePreparedStatement) preparedStatement).setORAData(++startParameterNumber, new CronStringArray(temporalPattern.getCronStrings()));
		return startParameterNumber;
	}	

	private void insertTemporalPattern(TemporalPattern temporalPattern) throws CreateObjectException {
		String tpIdCode = DatabaseIdentifier.toSQLString(temporalPattern.getId());
		
		PreparedStatement preparedStatement = null;
		try {			
			preparedStatement = this.insertTemporalPatternPreparedStatement();
			this.setEntityForPreparedStatement(temporalPattern, preparedStatement, MODE_INSERT);
			Log.debugMessage("TemporalPatternDatabase.insertTemporalPattern | Inserting temporal pattern " + tpIdCode, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		catch (IllegalDataException ide){
			String mesg = "TemporalPatternDatabase.insertTemporalPattern | Cannot insert temporal pattern '" + tpIdCode + "' -- " + ide.getMessage();
			throw new CreateObjectException(mesg, ide);
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPatternDatabase.insertTemporalPattern | Cannot insert temporal pattern '" + tpIdCode + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}
	
}
