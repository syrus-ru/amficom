/*
 * $Id: TransmissionPathDatabase.java,v 1.13 2004/08/22 18:49:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.13 $, $Date: 2004/08/22 18:49:19 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class TransmissionPathDatabase extends StorableObjectDatabase {
    // table :: TransmissionPath
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
    // start_port_id VARCHAR2(32),
    public static final String COLUMN_START_PORT_ID = "start_port_id";
    // finish_port_id VARCHAR2(32),
    public static final String COLUMN_FINISH_PORT_ID        = "finish_port_id";


    // table :: TransmissionPathMELink
    // monitored_element_id Identifier,
    public static final String LINK_COLUMN_MONITORED_ELEMENT_ID  = "monitored_element_id";
    // transmission_path_id Identifier,
    public static final String LINK_COLUMN_TRANSMISSION_PATH_ID  = "transmission_path_id";    
    
    public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private TransmissionPath fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TransmissionPath)
			return (TransmissionPath)storableObject;
		throw new IllegalDataException("TransmissionPathDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		this.retrieveTransmissionPath(transmissionPath);
		this.retrieveTransmissionPathMELink(transmissionPath);
		transmissionPath.setCharacteristics(CharacteristicDatabase.retrieveCharacteristics(transmissionPath.getId(), CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATH));
	}

	private void retrieveTransmissionPath(TransmissionPath transmissionPath) throws ObjectNotFoundException, RetrieveObjectException {
		String tpIdStr = transmissionPath.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_START_PORT_ID + COMMA
			+ COLUMN_FINISH_PORT_ID
			+ SQL_FROM + ObjectEntities.TRANSPATH_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + tpIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.retrieveTransmissionPath | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String name = resultSet.getString(COLUMN_NAME);
				String description = resultSet.getString(COLUMN_DESCRIPTION);
				transmissionPath.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),								  
								  
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/												
								  new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),								  

								  (name != null) ? name : "",
								  (description != null) ? description : "",
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_START_PORT_ID)),
								  new Identifier(resultSet.getString(COLUMN_FINISH_PORT_ID)));
			}
			else
				throw new ObjectNotFoundException("No such transmission path: " + tpIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.retrieveTransmissionPath | Cannot retrieve transmission path " + tpIdStr;
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
	
	private void retrieveTransmissionPathMELink(TransmissionPath transmissionPath) throws RetrieveObjectException{
		String tpIdStr = transmissionPath.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_MONITORED_ELEMENT_ID
			+ SQL_FROM + ObjectEntities.TRANSPATHMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_TRANSMISSION_PATH_ID + EQUALS + tpIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.retrieveEquipmentMELink | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			List meLink = new ArrayList();
			while (resultSet.next()) {				
				/**
				* @todo when change DB Identifier model ,change getString() to getLong()
				*/
				Identifier meId = new Identifier(resultSet.getString(LINK_COLUMN_MONITORED_ELEMENT_ID));
				meLink.add(meId);				
			}
			transmissionPath.setMonitoredElementIds(meLink);
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.retrieveEquipmentMELink | Cannot retrieve transmission path " + tpIdStr;
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


	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException{
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		try {
			this.insertTransmissionPath(transmissionPath);
		}
		catch (CreateObjectException coe) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw coe;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertTransmissionPath(TransmissionPath transmissionPath) throws CreateObjectException {
		String tpIdStr = transmissionPath.getId().toSQLString();

		String sql = SQL_INSERT_INTO
			+ ObjectEntities.TRANSPATH_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_START_PORT_ID + COMMA
			+ COLUMN_FINISH_PORT_ID 
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ tpIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(transmissionPath.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(transmissionPath.getModified()) + COMMA
			+ transmissionPath.getCreatorId().toSQLString() + COMMA
			+ transmissionPath.getModifierId().toSQLString() + COMMA
			+ transmissionPath.getDomainId().toSQLString() + COMMA
			+ APOSTOPHE + transmissionPath.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + transmissionPath.getDescription() + APOSTOPHE + COMMA
			+ transmissionPath.getStartPortId().toSQLString() + COMMA
			+ transmissionPath.getFinishPortId().toSQLString()
			+ CLOSE_BRACKET;
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.insertTransmissionPath | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.insertTransmissionPath | Cannot insert transmission path " + tpIdStr;
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
//
//	private void insertTransmissionPathMELinks(TransmissionPath transmissionPath)	throws CreateObjectException {
//		/**
//		 * @todo when change DB Identifier model ,change String to long
//		 */
//		String tpIdCode = transmissionPath.getId().getCode();
//		List meIds = transmissionPath.getMonitoredElementIds();
//		String sql = SQL_INSERT_INTO 
//					+ ObjectEntities.TRANSPATHMELINK_ENTITY
//					+ OPEN_BRACKET
//					+ LINK_COLUMN_TRANSMISSION_PATH_ID 
//					+ COMMA 
//					+ LINK_COLUMN_MONITORED_ELEMENT_ID
//					+ CLOSE_BRACKET
//					+ SQL_VALUES
//					+ OPEN_BRACKET
//					+ QUESTION
//					+ COMMA
//					+ QUESTION
//					+ CLOSE_BRACKET;
//
//		PreparedStatement preparedStatement = null;
//		/**
//		 * @todo when change DB Identifier model ,change String to long
//		 */
//		String meIdCode = null;
//		try {
//			preparedStatement = connection.prepareStatement(sql);
//			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
//				/**
//				 * @todo when change DB Identifier model ,change setString() to
//				 *       setLong()
//				 */
//				preparedStatement.setString(1, tpIdCode);
//				meIdCode = ((Identifier) iterator.next()).getCode();
//				/**
//				 * @todo when change DB Identifier model ,change setString() to
//				 *       setLong()
//				 */
//				preparedStatement.setString(2, meIdCode);
//				Log.debugMessage("TransmissionPathDatabase.insertTransmissionPathMELinks | Inserting link for transmission path '"
//								+ tpIdCode
//								+ "' and monitored element '"
//								+ meIdCode + "'", Log.DEBUGLEVEL09);
//				preparedStatement.executeUpdate();
//			}
//		}
//		catch (SQLException sqle) {
//			String mesg = "TransmissionPathDatabase.insertTransmissionPathMELinks | Cannot insert link for monitored element '"
//							+ meIdCode + "' and transmission path '" + tpIdCode + "'";
//			throw new CreateObjectException(mesg, sqle);
//		}
//		finally {
//			try {
//				if (preparedStatement != null)
//					preparedStatement.close();
//				preparedStatement = null;
//			}
//			catch (SQLException sqle1) {
//				Log.errorException(sqle1);
//			}
//		}
//	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	private void setModified(TransmissionPath transmissionPath) throws UpdateObjectException {		
		String tpIdStr = transmissionPath.getId().toSQLString();
		String sql = SQL_UPDATE
			+ ObjectEntities.TRANSPATH_ENTITY
			+ SQL_SET
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(transmissionPath.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + transmissionPath.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS + tpIdStr;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.setModified | Cannot set modified for transmission path " + tpIdStr;
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
	
	public static List retrieveAll() throws RetrieveObjectException {
		List transPaths = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.TRANSPATH_ENTITY;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				transPaths.add(new TransmissionPath(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.retrieveAll | Cannot retrieve transmission path";
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
		return transPaths;
	}

	public static void delete(TransmissionPath transmissionPath) {
		String tpIdStr = transmissionPath.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
				+ ObjectEntities.TRANSPATH_ENTITY
				+ SQL_WHERE + COLUMN_ID + EQUALS + tpIdStr;
			Log.debugMessage("TransmissionPathDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
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
}
