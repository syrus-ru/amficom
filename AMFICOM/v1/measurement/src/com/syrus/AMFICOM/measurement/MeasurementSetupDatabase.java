/*
 * $Id: MeasurementSetupDatabase.java,v 1.21 2004/09/09 06:46:36 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.21 $, $Date: 2004/09/09 06:46:36 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementSetupDatabase extends StorableObjectDatabase {

	public static final String	COLUMN_CRITERIA_SET_ID				= "criteria_set_id";
	public static final String	COLUMN_DESCRIPTION					= "description";
	public static final String	COLUMN_ETALON_ID					= "etalon_id";
	public static final String	COLUMN_MEASUREMENT_DURAION			= "measurement_duration";
	public static final String	COLUMN_PARAMETER_SET_ID				= "parameter_set_id";
	public static final String	COLUMN_THRESHOLD_SET_ID				= "threshold_set_id";

	public static final String	LINK_COLUMN_ME_ID					= "monitored_element_id";
	public static final String	LINK_COLUMN_MEASUREMENT_SETUP_ID	= "measurement_setup_id";
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;	
	
	private String updateColumns;	
	private String updateMultiplySQLValues;	
	
	protected String getEnityName() {
		return "MeasurementSetup";
	}
	
	
	protected String getTableName() {		
		return ObjectEntities.MS_ENTITY;
	}	

	private MeasurementSetup fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementSetup)
			return (MeasurementSetup) storableObject;
		throw new IllegalDataException("MeasurementSetupDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurementSetup);
		this.retrieveMeasurementSetupMELinks(measurementSetup);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(measurementSetup);
			this.insertMeasurementSetupMELinks(measurementSetup);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			} catch (SQLException sqle) {
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
		insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
			this.insertMeasurementSetupMELinks(measurementSetup);
		}

	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		try {
			switch (updateKind) {
				case MeasurementSetup.UPDATE_ATTACH_ME:
					this.createMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);
					break;
				case MeasurementSetup.UPDATE_DETACH_ME:
					this.deleteMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);
					break;
				case UPDATE_CHECK:
					super.checkAndUpdateEntity(storableObject, false);
					break;
				case UPDATE_FORCE:					
				default:
					super.checkAndUpdateEntity(storableObject, true);					
					return;
			}
		}
		catch (UpdateObjectException e) {
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
	
	
	public void update(List storableObjects, int updateKind, Object obj) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case MeasurementSetup.UPDATE_ATTACH_ME:
				for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
					MeasurementSetup measurementSetup = (MeasurementSetup) iter.next();
					this.createMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);					
				}
				break;
			case MeasurementSetup.UPDATE_DETACH_ME:
				for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
					MeasurementSetup measurementSetup = (MeasurementSetup) iter.next();
					this.deleteMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);
				}
				break;
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);					
				return;
		}

	}
	

	private void createMEAttachment(MeasurementSetup measurementSetup, Identifier monitoredElementId) throws UpdateObjectException {
		String msIdStr = measurementSetup.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MSMELINK_ENTITY + OPEN_BRACKET
			+ LINK_COLUMN_MEASUREMENT_SETUP_ID + COMMA
			+ LINK_COLUMN_ME_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ msIdStr + COMMA
			+ meIdStr
			+ CLOSE_BRACKET;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.createMEAttachment | Cannot attach measurement setup '" + msIdStr + "' to monitored element '" + meIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	private void deleteMEAttachment(MeasurementSetup measurementSetup, Identifier monitoredElementId) throws IllegalDataException {
		String msIdStr = measurementSetup.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_DELETE_FROM 
			+ ObjectEntities.MSMELINK_ENTITY
			+ SQL_WHERE
			+ LINK_COLUMN_MEASUREMENT_SETUP_ID + EQUALS + msIdStr
			+ SQL_AND
				+ LINK_COLUMN_ME_ID + EQUALS + meIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.deleteMEAttachment | Cannot detach measurement setup '" + msIdStr + "' from monitored element '" + meIdStr + "' -- " + sqle.getMessage();
			throw new IllegalDataException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){			
			this.updateColumns = super.getUpdateColumns() + COMMA
			+ COLUMN_PARAMETER_SET_ID + COMMA
			+ COLUMN_CRITERIA_SET_ID + COMMA
			+ COLUMN_THRESHOLD_SET_ID + COMMA
			+ COLUMN_ETALON_ID + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_MEASUREMENT_DURAION;
		}
		
		return this.updateColumns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){			
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
		
		return this.updateMultiplySQLValues;
	}
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {		
		MeasurementSetup measurementSetup = fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		String criteriaSetIdSubstr = (criteriaSet != null) ? (criteriaSet.getId().toSQLString()) : Identifier.getNullSQLString();
		String thresholdSetIdSubstr = (thresholdSet != null) ? (thresholdSet.getId().toSQLString()) : Identifier.getNullSQLString();
		String etalonIdSubstr = (etalon != null) ? (etalon.getId().toSQLString()) : Identifier.getNullSQLString();
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ measurementSetup.getParameterSet().getId().toSQLString() + COMMA
			+ criteriaSetIdSubstr + COMMA
			+ thresholdSetIdSubstr + COMMA
			+ etalonIdSubstr + COMMA
			+ APOSTOPHE + measurementSetup.getDescription() + APOSTOPHE + COMMA
			+ Long.toString(measurementSetup.getMeasurementDuration());
		return values;
	}
	
	
	protected void setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		MeasurementSetup measurementSetup = fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		String criteriaSetIdSubstr = (criteriaSet != null) ? (criteriaSet.getId().getCode()) : "";
		String thresholdSetIdSubstr = (thresholdSet != null) ? (thresholdSet.getId().getCode()) : "";
		String etalonIdSubstr = (etalon != null) ? (etalon.getId().getCode()) : "";
		super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(6, measurementSetup.getParameterSet().getId().getCode()); 
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(7, criteriaSetIdSubstr); 
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(8, thresholdSetIdSubstr);
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(9, etalonIdSubstr);
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(10, measurementSetup.getId().getCode());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}

	}

	private void insertMeasurementSetupMELinks(MeasurementSetup measurementSetup) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String msIdCode = measurementSetup.getId().getCode();
		List meIds = measurementSetup.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
				+ ObjectEntities.MSMELINK_ENTITY + OPEN_BRACKET
				+ LINK_COLUMN_MEASUREMENT_SETUP_ID + COMMA 
				+ LINK_COLUMN_ME_ID
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String meIdCode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(1, msIdCode);
				meIdCode = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(2, meIdCode);
				Log.debugMessage("MeasurementSetupDatabase.insertMeasurementSetupMELinks | Inserting link for measurement setup "
										+ msIdCode
										+ " and monitored element "
										+ meIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.insertMeasurementSetupMELinks | Cannot insert link for monitored element '" + meIdCode + "' and Measurement Setup '" + msIdCode + "' -- " + sqle.getMessage();
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
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_PARAMETER_SET_ID + COMMA
			+ COLUMN_CRITERIA_SET_ID + COMMA
			+ COLUMN_THRESHOLD_SET_ID + COMMA
			+ COLUMN_ETALON_ID + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_MEASUREMENT_DURAION
			+ SQL_FROM + ObjectEntities.MS_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}	
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementSetup measurementSetup = (storableObject == null) ? 
				new MeasurementSetup(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 
									   null, null, null, 0, null) : 
					fromStorableObject(storableObject);	
		Set parameterSet;
		Set criteriaSet;
		Set thresholdSet;
		Set etalon;
		String idCode;
		try {
			parameterSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_PARAMETER_SET_ID)), true);
			/**
			 * @todo when change DB Identifier model ,change String to long
			 */
			/**
			 * @todo when change DB Identifier model ,change getString() to
			 *       getLong()
			 */
			idCode = resultSet.getString(COLUMN_CRITERIA_SET_ID);
			criteriaSet = (idCode != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(idCode), true) : null;
			/**
			 * @todo when change DB Identifier model ,change getString() to
			 *       getLong()
			 */
			idCode = resultSet.getString(COLUMN_THRESHOLD_SET_ID);
			thresholdSet = (idCode != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(idCode), true) : null;
			/**
			 * @todo when change DB Identifier model ,change getString() to
			 *       getLong()
			 */
			idCode = resultSet.getString(COLUMN_ETALON_ID);
			etalon = (idCode != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(idCode), true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String description = resultSet.getString(COLUMN_DESCRIPTION);
		measurementSetup.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																	 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
																	 /**
																		* @todo when change DB Identifier model ,change getString() to
																		*       getLong()
																		*/
																	 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
																	 /**
																		* @todo when change DB Identifier model ,change getString() to
																		*       getLong()
																		*/
																	 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																	 parameterSet,
																	 criteriaSet,
																	 thresholdSet,
																	 etalon,
																	 (description != null) ? description : "",
																	 resultSet.getLong(COLUMN_MEASUREMENT_DURAION));
		return measurementSetup;
	}

	private void retrieveMeasurementSetupMELinks(MeasurementSetup measurementSetup) throws RetrieveObjectException {
		List meIds = new ArrayList();

		String msIdStr = measurementSetup.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_ME_ID
			+ SQL_FROM + ObjectEntities.MSMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_SETUP_ID + EQUALS + msIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.retrieveMeasurementSetupMELinks | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				meIds.add(new Identifier(resultSet.getString(LINK_COLUMN_ME_ID)));
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.retrieveMeasurementSetupMELinks | Cannot retrieve monitored element ids for measurement setup '" + msIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
		measurementSetup.setMonitoredElementIds(meIds);
	}

	private void setModified(MeasurementSetup measurementSetup) throws UpdateObjectException {
		String msIdStr = measurementSetup.getId().toString();
		String sql = SQL_UPDATE
				+ ObjectEntities.MS_ENTITY
				+ SQL_SET
				+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(measurementSetup.getModified()) + COMMA
				+ COLUMN_MODIFIER_ID + EQUALS + measurementSetup.getModifierId().toString()
				+ SQL_WHERE + COLUMN_ID + msIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.setModified | Cannot set modified for measurement setup '"	+ msIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
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
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = retriveByIdsOneQuery(null, condition);
		else list = retriveByIdsOneQuery(ids, condition);
		
		for(Iterator it=list.iterator();it.hasNext();){
			MeasurementSetup measurementSetup = (MeasurementSetup)it.next();
			retrieveMeasurementSetupMELinks(measurementSetup);
		}
		
		return list;	
	}

}
