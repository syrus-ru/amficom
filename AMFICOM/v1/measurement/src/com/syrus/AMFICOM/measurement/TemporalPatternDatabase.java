/*
 * $Id: TemporalPatternDatabase.java,v 1.17 2004/09/09 06:46:36 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OraclePreparedStatement;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.ora.CronStringArray;

/**
 * @version $Revision: 1.17 $, $Date: 2004/09/09 06:46:36 $
 * @author $Author: bob $
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
	
	private String updateColumns;
	private String updateMultiplySQLValues;
	
	protected String getEnityName() {
		return "TemporalPattern";
	}
	
	protected String getTableName() {		
		return ObjectEntities.TEMPORALPATTERN_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_VALUE;
		}
		return this.updateColumns;
	}	

	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){	
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		throw new UnsupportedOperationException("Entity contain complex field");		
	}
	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.retrieveEntity(temporalPattern);
	}

	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_VALUE
			+ SQL_FROM + ObjectEntities.TEMPORALPATTERN_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		TemporalPattern temporalPattern = (storableObject == null) ?
				new TemporalPattern(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null):
				fromStorableObject(storableObject);
		String[] cronStrings = ((CronStringArray)(((OracleResultSet)resultSet).getORAData(COLUMN_VALUE, CronStringArray.getORADataFactory()))).getArray();
		temporalPattern.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																	DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
																	new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
																	new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																	resultSet.getString(COLUMN_DESCRIPTION),
																	cronStrings);
		return temporalPattern;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws ObjectNotFoundException, RetrieveObjectException, IllegalDataException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		try {
			this.insertTemporalPattern(temporalPattern);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}
	
	private PreparedStatement insertTemporalPatternPreparedStatement() throws SQLException{
		String sql = SQL_INSERT_INTO + ObjectEntities.TEMPORALPATTERN_ENTITY 
		+ OPEN_BRACKET
		+ this.getUpdateColumns()
		+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
		+ getUpdateMultiplySQLValues()
		+ CLOSE_BRACKET;
		return connection.prepareStatement(sql);
	}
	
	protected void setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		TemporalPattern temporalPattern = fromStorableObject(storableObject);
		super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			preparedStatement.setString(6, temporalPattern.getDescription());
			((OraclePreparedStatement)preparedStatement).setORAData(7, new CronStringArray(temporalPattern.getCronStrings()));
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(8, temporalPattern.getId().getCode());			
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
	}	
	
	private void insertTemporalPattern(TemporalPattern temporalPattern) throws CreateObjectException {
		String tpIdCode = temporalPattern.getId().toSQLString();
		
		PreparedStatement preparedStatement = null;
		try {			
			preparedStatement = insertTemporalPatternPreparedStatement();
			setEntityForPreparedStatement(temporalPattern, preparedStatement);
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
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
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
			return retriveByIdsOneQuery(null, condition);
		
		return retriveByIdsOneQuery(ids, condition);
	}
	
}
