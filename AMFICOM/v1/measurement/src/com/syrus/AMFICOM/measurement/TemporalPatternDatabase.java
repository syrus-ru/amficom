/*
 * $Id: TemporalPatternDatabase.java,v 1.29 2004/12/29 10:11:46 arseniy Exp $
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
import java.util.List;

import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OracleResultSet;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.ora.CronStringArray;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.29 $, $Date: 2004/12/29 10:11:46 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class TemporalPatternDatabase extends StorableObjectDatabase {
	public static final String COLUMN_DESCRIPTION 	= "description";
	public static final String COLUMN_VALUE 		= "value";
		
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
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_VALUE;
		}
		return columns;
	}	

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){	
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		throw new UnsupportedOperationException("Entity contain complex field");		
	}	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.retrieveEntity(temporalPattern);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		TemporalPattern temporalPattern = (storableObject == null) ?
				new TemporalPattern(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null):
				fromStorableObject(storableObject);
		String[] cronStrings = ((CronStringArray)(((OracleResultSet)resultSet).getORAData(COLUMN_VALUE, CronStringArray.getORADataFactory()))).getArray();
		temporalPattern.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
									  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
									  DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
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
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
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
			+ getUpdateMultiplySQLValues(MODE_INSERT)
			+ CLOSE_BRACKET;
			preparedStatement = connection.prepareStatement(sql);
		} finally {
			DatabaseConnection.releaseConnection(connection);
		}
		return preparedStatement;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		TemporalPattern temporalPattern = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			preparedStatement.setString(++i, temporalPattern.getDescription());
			((OraclePreparedStatement)preparedStatement).setORAData(++i, new CronStringArray(temporalPattern.getCronStrings()));
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}	
	
	private void insertTemporalPattern(TemporalPattern temporalPattern) throws CreateObjectException {
		String tpIdCode = DatabaseIdentifier.toSQLString(temporalPattern.getId());
		
		PreparedStatement preparedStatement = null;
		try {			
			preparedStatement = insertTemporalPatternPreparedStatement();
			setEntityForPreparedStatement(temporalPattern, preparedStatement, MODE_INSERT);
			Log.debugMessage("TemporalPatternDatabase.insertTemporalPattern | Inserting temporal pattern " + tpIdCode, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		catch (UpdateObjectException uoe){
			String mesg = "TemporalPatternDatabase.insertTemporalPattern | Cannot insert temporal pattern '" + tpIdCode + "' -- " + uoe.getMessage();
			throw new CreateObjectException(mesg, uoe);
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

	
	public void update(StorableObject storableObject, int updateKind, Object arg) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
//		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, true);		
				return;
		}
	}
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
			return;
		}
	}
	
	public List retrieveAll() throws RetrieveObjectException {
		try{
			return retrieveByIds(null, null);
		}catch(IllegalDataException ide){
			throw new RetrieveObjectException(ide);
		}
	}
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retrieveByIdsOneQuery(null, condition);
		
		return retrieveByIdsOneQuery(ids, condition);
	}
	
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = this.retrieveButIds(ids, null);
		return list;
	}
	
}
