/*
 * $Id: TemporalPatternDatabase.java,v 1.41 2005/02/19 20:33:58 arseniy Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.ora.CronStringArray;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.41 $, $Date: 2005/02/19 20:33:58 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class TemporalPatternDatabase extends StorableObjectDatabase {
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private TemporalPattern fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TemporalPattern)
			return (TemporalPattern)storableObject;
		throw new IllegalDataException("TemporalPatternDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	private static String columns;
	private static String updateMultiplySQLValues;	
	
	protected String getEnityName() {		
		return ObjectEntities.TEMPORALPATTERN_ENTITY;
	}
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ TemporalPatternWrapper.COLUMN_VALUE;
		}
		return columns;
	}	

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {	
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		throw new UnsupportedOperationException("Entity contain complex field");		
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
		String[] cronStrings = ((CronStringArray)(((OracleResultSet)resultSet).getORAData(TemporalPatternWrapper.COLUMN_VALUE, CronStringArray.getORADataFactory()))).getArray();
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
//		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
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
			+ this.getUpdateMultiplySQLValues(MODE_INSERT)
			+ CLOSE_BRACKET;
			preparedStatement = connection.prepareStatement(sql);
		}
		finally {
			DatabaseConnection.releaseConnection(connection);
		}
		return preparedStatement;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);

		preparedStatement.setString(++i, temporalPattern.getDescription());
		((OraclePreparedStatement) preparedStatement).setORAData(++i, new CronStringArray(temporalPattern.getCronStrings()));
		return i;
	}	

	private void insertTemporalPattern(TemporalPattern temporalPattern) throws CreateObjectException {
		String tpIdCode = DatabaseIdentifier.toSQLString(temporalPattern.getId());
		
		PreparedStatement preparedStatement = null;
		try {			
			preparedStatement = insertTemporalPatternPreparedStatement();
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

	
	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
//		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);		
				return;
		}
	}
	
	public void update(Collection storableObjects, Identifier modifierId, int updateKind) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);		
			return;
		}
	}
	
	public Collection retrieveAll() throws RetrieveObjectException {
		try{
			return this.retrieveByIds(null, null);
		}
		catch(IllegalDataException ide){
			throw new RetrieveObjectException(ide);
		}
	}
	
	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);

		return this.retrieveByIdsOneQuery(ids, condition);
	}
	
}
