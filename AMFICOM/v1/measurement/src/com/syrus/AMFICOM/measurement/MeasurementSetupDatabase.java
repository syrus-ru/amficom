/*
 * $Id: MeasurementSetupDatabase.java,v 1.72 2005/02/24 14:59:59 arseniy Exp $
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.72 $, $Date: 2005/02/24 14:59:59 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementSetupDatabase extends StorableObjectDatabase {

	public static final String  PARAMETER_TYPE_ID                   = "parameter_type_id";
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;	
	
	private static String columns;	
	private static String updateMultiplySQLValues;	
	
	protected String getEnityName() {
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
//		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		this.insertEntity(measurementSetup);
		this.insertMeasurementSetupMELinks(measurementSetup);
	}
	
	
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
			this.insertMeasurementSetupMELinks(measurementSetup);
		}

	}

	private void createMEAttachment(MeasurementSetup measurementSetup, Identifier monitoredElementId) throws UpdateObjectException {
		String msIdStr = DatabaseIdentifier.toSQLString(measurementSetup.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MSMELINK_ENTITY + OPEN_BRACKET
			+ MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + COMMA
			+ MeasurementSetupWrapper.LINK_COLUMN_ME_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ msIdStr + COMMA
			+ meIdStr
			+ CLOSE_BRACKET;

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
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
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void deleteMEAttachment(MeasurementSetup measurementSetup, Identifier monitoredElementId) throws IllegalDataException {
		String msIdStr = DatabaseIdentifier.toSQLString(measurementSetup.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_DELETE_FROM 
			+ ObjectEntities.MSMELINK_ENTITY
			+ SQL_WHERE
			+ MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + EQUALS + msIdStr
			+ SQL_AND
				+ MeasurementSetupWrapper.LINK_COLUMN_ME_ID + EQUALS + meIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
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
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	protected String getColumns(int mode) {
		if (columns == null){			
			columns = super.getColumns(mode) + COMMA
				+ MeasurementSetupWrapper.COLUMN_PARAMETER_SET_ID + COMMA
				+ MeasurementSetupWrapper.COLUMN_CRITERIA_SET_ID + COMMA
				+ MeasurementSetupWrapper.COLUMN_THRESHOLD_SET_ID + COMMA
				+ MeasurementSetupWrapper.COLUMN_ETALON_ID + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MeasurementSetupWrapper.COLUMN_MEASUREMENT_DURAION;
		}		
		return columns;
	}
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){			
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		
		return updateMultiplySQLValues;
	}	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {		
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(measurementSetup.getParameterSet().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString((criteriaSet != null) ? criteriaSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((thresholdSet != null) ? thresholdSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((etalon != null) ? etalon.getId() : null) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementSetup.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ Long.toString(measurementSetup.getMeasurementDuration());
		return values;
	}
	
	
	protected int  setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurementSetup.getParameterSet().getId()); 
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (criteriaSet != null) ? criteriaSet.getId() : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (thresholdSet != null) ? thresholdSet.getId() : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (etalon != null) ? etalon.getId() : null);
		DatabaseString.setString(preparedStatement, ++i, measurementSetup.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setLong(++i, measurementSetup.getMeasurementDuration());
		return i;
	}

	private void insertMeasurementSetupMELinks(MeasurementSetup measurementSetup) throws CreateObjectException {
		Identifier msId = measurementSetup.getId();
		Collection meIds = measurementSetup.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
				+ ObjectEntities.MSMELINK_ENTITY + OPEN_BRACKET
				+ MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + COMMA 
				+ MeasurementSetupWrapper.LINK_COLUMN_ME_ID
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier meId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
				meId = (Identifier) iterator.next();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, msId);				
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, meId);
				Log.debugMessage("MeasurementSetupDatabase.insertMeasurementSetupMELinks | Inserting link for measurement setup "
										+ msId
										+ " and monitored element "
										+ meId.getIdentifierString(), Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.insertMeasurementSetupMELinks | Cannot insert link for monitored element '" + meId.getIdentifierString() + "' and Measurement Setup '" + msId + "' -- " + sqle.getMessage();
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
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementSetup measurementSetup = (storableObject == null) ? 
				new MeasurementSetup(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, 
									   null, null, null, 0, null) : 
					this.fromStorableObject(storableObject);	
		Set parameterSet;
		Set criteriaSet;
		Set thresholdSet;
		Set etalon;
		Identifier id;
		try {
			parameterSet = (Set)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_PARAMETER_SET_ID), true);			
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_CRITERIA_SET_ID);
			criteriaSet = (id != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(id, true) : null;
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_THRESHOLD_SET_ID);
			thresholdSet = (id != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(id, true) : null;			
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_ETALON_ID);
			etalon = (id != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(id, true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		measurementSetup.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									   parameterSet,
									   criteriaSet,
									   thresholdSet,
									   etalon,
									   (description != null) ? description : "",
									   resultSet.getLong(MeasurementSetupWrapper.COLUMN_MEASUREMENT_DURAION));
		return measurementSetup;
	}

	private void retrieveMeasurementSetupMELinks(MeasurementSetup measurementSetup) throws RetrieveObjectException {
		List meIds = new ArrayList();

		String msIdStr = DatabaseIdentifier.toSQLString(measurementSetup.getId());
		String sql = SQL_SELECT
			+ MeasurementSetupWrapper.LINK_COLUMN_ME_ID
			+ SQL_FROM + ObjectEntities.MSMELINK_ENTITY
			+ SQL_WHERE + MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + EQUALS + msIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.retrieveMeasurementSetupMELinks | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				meIds.add(DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.LINK_COLUMN_ME_ID));
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
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
		measurementSetup.setMonitoredElementIds0(meIds);
	}
    
    private void retrieveMeasurementSetupMELinksByOneQuery(Collection measurementSetups) throws RetrieveObjectException {
		if ((measurementSetups == null) || (measurementSetups.isEmpty()))
			return;

		Map meIdsMap = null;
		try {
			meIdsMap = this.retrieveLinkedEntityIds(measurementSetups,
					ObjectEntities.MSMELINK_ENTITY,
					MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
					MeasurementSetupWrapper.LINK_COLUMN_ME_ID);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		MeasurementSetup measurementSetup;
		Identifier msId;
		Collection monitoredElementIds;
		for (Iterator it = measurementSetups.iterator(); it.hasNext();) {
			measurementSetup = (MeasurementSetup) it.next();
			msId = measurementSetup.getId();
			monitoredElementIds = (Collection) meIdsMap.get(msId);

			measurementSetup.setMonitoredElementIds0(monitoredElementIds);
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
		Collection objects = null; 
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else
			objects = this.retrieveByIdsOneQuery(ids, condition);
		
		this.retrieveMeasurementSetupMELinksByOneQuery(objects);
		
		
		return objects;	
	}
}
