/*
 * $Id: MeasurementSetupDatabase.java,v 1.18 2004/08/29 11:47:05 bob Exp $
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
import java.util.LinkedList;
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

/**
 * @version $Revision: 1.18 $, $Date: 2004/08/29 11:47:05 $
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

	private MeasurementSetup fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementSetup)
			return (MeasurementSetup) storableObject;
		throw new IllegalDataException("MeasurementSetupDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		this.retrieveMeasurementSetup(measurementSetup);
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
			this.insertMeasurementSetup(measurementSetup);
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
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
				default:
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

	private void insertMeasurementSetup(MeasurementSetup measurementSetup) throws CreateObjectException {
		String msIdStr = measurementSetup.getId().toSQLString();
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();		
		String criteriaSetIdSubstr = (criteriaSet != null) ? (criteriaSet.getId().toSQLString()) : Identifier.getNullSQLString();
		String thresholdSetIdSubstr = (thresholdSet != null) ? (thresholdSet.getId().toSQLString()) : Identifier.getNullSQLString();
		String etalonIdSubstr = (etalon != null) ? (etalon.getId().toSQLString()) : Identifier.getNullSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MS_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_PARAMETER_SET_ID + COMMA
			+ COLUMN_CRITERIA_SET_ID + COMMA
			+ COLUMN_THRESHOLD_SET_ID + COMMA
			+ COLUMN_ETALON_ID + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_MEASUREMENT_DURAION
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ msIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(measurementSetup.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(measurementSetup.getModified()) + COMMA
			+ measurementSetup.getCreatorId().toSQLString() + COMMA
			+ measurementSetup.getModifierId().toSQLString() + COMMA
			+ measurementSetup.getParameterSet().getId().toSQLString() + COMMA
			+ criteriaSetIdSubstr + COMMA
			+ thresholdSetIdSubstr + COMMA
			+ etalonIdSubstr + COMMA
			+ APOSTOPHE + measurementSetup.getDescription() + APOSTOPHE + COMMA
			+ Long.toString(measurementSetup.getMeasurementDuration())
			+ CLOSE_BRACKET;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.insertMeasurementSetup | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.insertMeasurementSetup | Cannot insert Measurement Setup '" + msIdStr + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
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
	
	private String retrieveMeasurementSetupQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_PARAMETER_SET_ID + COMMA
		+ COLUMN_CRITERIA_SET_ID + COMMA
		+ COLUMN_THRESHOLD_SET_ID + COMMA
		+ COLUMN_ETALON_ID + COMMA
		+ COLUMN_DESCRIPTION + COMMA
		+ COLUMN_MEASUREMENT_DURAION
		+ SQL_FROM + ObjectEntities.MS_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private MeasurementSetup updateMeasurementSetupFromResultSet(MeasurementSetup measurementSetup, ResultSet resultSet) throws RetrieveObjectException, SQLException{
		MeasurementSetup measurementSetup1 = measurementSetup;
		if (measurementSetup == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			measurementSetup1 = new MeasurementSetup(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 
												   null, null, null, 0, null);
		}
		/**
		 * @todo when change DB Identifier model ,change getString() to
		 *       getLong()
		 */
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
		measurementSetup1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
		return measurementSetup1;
	}


	private void retrieveMeasurementSetup(MeasurementSetup measurementSetup) throws ObjectNotFoundException, RetrieveObjectException {
		String msIdStr = measurementSetup.getId().toSQLString();
		String sql = retrieveMeasurementSetupQuery(COLUMN_ID + EQUALS + msIdStr);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.retrieveMeasurementSetup | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				updateMeasurementSetupFromResultSet(measurementSetup, resultSet);
			}
			else
				throw new ObjectNotFoundException("No such measurement setup: " + msIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.retrieveMeasurementSetup | Cannot retrieve measurement setup '" + msIdStr + "' -- " + sqle.getMessage();
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
		return retriveByIdsOneQuery(null);
	}

	public void delete(MeasurementSetup measurementSetup) {
		String msIdStr = measurementSetup.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.MS_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ msIdStr;
			Log.debugMessage("MeasurementSetupDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retriveByIdsOneQuery(null);
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			String condition = null;
			if (ids!=null){
				StringBuffer buffer = new StringBuffer(COLUMN_ID);
				int idsLength = ids.size();
				if (idsLength == 1){
					buffer.append(EQUALS);
					buffer.append(((Identifier)ids.iterator().next()).toSQLString());
				} else{
					buffer.append(SQL_IN);
					buffer.append(OPEN_BRACKET);
					
					int i = 1;
					for(Iterator it=ids.iterator();it.hasNext();i++){
						Identifier id = (Identifier)it.next();
						buffer.append(id.toSQLString());
						if (i < idsLength)
							buffer.append(COMMA);
					}
					
					buffer.append(CLOSE_BRACKET);
					condition = buffer.toString();
				}
			}
			sql = retrieveMeasurementSetupQuery(condition);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateMeasurementSetupFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
		return result;
	}
	
	private List retriveByIdsPreparedStatement(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			
			int idsLength = ids.size();
			if (idsLength == 1){
				return retriveByIdsOneQuery(ids);
			}
			StringBuffer buffer = new StringBuffer(COLUMN_ID);
			buffer.append(EQUALS);							
			buffer.append(QUESTION);
			
			sql =retrieveMeasurementSetupQuery(buffer.toString());
		}
			
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement(sql.toString());
			for(Iterator it = ids.iterator();it.hasNext();){
				Identifier id = (Identifier)it.next(); 
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				String idStr = id.getIdentifierString();
				stmt.setString(1, idStr);
				resultSet = stmt.executeQuery();
				if (resultSet.next()){
					result.add(updateMeasurementSetupFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("MeasurementSetupDatabase.retriveByIdsPreparedStatement | No such measurement setup: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.retriveByIdsPreparedStatement | Cannot retrieve measurement setup " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt != null)
					stmt.close();
				stmt = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}			
		
		return result;
	}

}
