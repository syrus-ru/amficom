/*
 * $Id: TransmissionPathDatabase.java,v 1.2 2004/08/09 08:39:03 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/09 08:39:03 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class TransmissionPathDatabase extends StorableObjectDatabase {
    // table :: TransmissionPath
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // domain_id Identifier,
    public static final String COLUMN_DOMAIN_ID     = "domain_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";

    // table :: TransmissionPathMELink
    // monitored_element_id Identifier,
    public static final String LINK_COLUMN_MONITORED_ELEMENT_ID  = "monitored_element_id";
    // transmission_path_id Identifier,
    public static final String LINK_COLUMN_TRANSMISSION_PATH_ID  = "transmission_path_id";

	private TransmissionPath fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TransmissionPath)
			return (TransmissionPath)storableObject;
		throw new IllegalDataException("TransmissionPathDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		this.retrieveTransmissionPath(transmissionPath);
	}

	private void retrieveTransmissionPath(TransmissionPath transmissionPath) throws ObjectNotFoundException, RetrieveObjectException {
		String sql;
		String tpIdStr = transmissionPath.getId().toSQLString();
		{
		StringBuffer buffer = new StringBuffer();
		buffer.append(StorableObjectDatabase.SQL_SELECT);
		buffer.append(DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_CREATED));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_MODIFIED));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_CREATOR_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_MODIFIER_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DomainMember.COLUMN_DOMAIN_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_NAME);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_DESCRIPTION);
		buffer.append(StorableObjectDatabase.SQL_FROM);
		buffer.append(ObjectEntities.TRANSPATH_ENTITY);
		buffer.append(StorableObjectDatabase.SQL_WHERE);
		buffer.append(StorableObjectDatabase.COLUMN_ID);
		buffer.append(StorableObjectDatabase.EQUALS);
		buffer.append(tpIdStr);
		sql = buffer.toString();
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.retrieveTransmissionPath | Trying: " + sql, Log.DEBUGLEVEL05);
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
								  new Identifier(resultSet.getString(COLUMN_DOMAIN_ID)),								  

								  (name != null)?name:"",
								  (description != null)?description:"");
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
			this.insertTransmissionPathMELinks(transmissionPath);
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
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String trIdCode = transmissionPath.getId().getCode();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier domainId = transmissionPath.getDomainId();
		
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.SET_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(1, trIdCode);
			preparedStatement.setDate(2, new java.sql.Date(transmissionPath.getCreated().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(transmissionPath.getModified().getTime()));
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(4, transmissionPath.getCreatorId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(5, transmissionPath.getModifierId().getCode());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(6, (domainId != null)?domainId.getCode():Identifier.getNullSQLString());

			preparedStatement.setString(7, transmissionPath.getName());
			
			preparedStatement.setString(8, transmissionPath.getDescription());			
			
										
			Log.debugMessage("TransmissionPathDatabase.insertTransmissionPath | Trying: " + sql, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.insertTransmissionPath | Cannot insert transmission path " + trIdCode;
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

	private void insertTransmissionPathMELinks(TransmissionPath transmissionPath)	throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String tpIdCode = transmissionPath.getId().getCode();
		List meIds = transmissionPath.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
					+ ObjectEntities.TRANSPATHMELINK_ENTITY
					+ OPEN_BRACKET
					+ LINK_COLUMN_TRANSMISSION_PATH_ID 
					+ COMMA 
					+ LINK_COLUMN_MONITORED_ELEMENT_ID
					+ CLOSE_BRACKET
					+ SQL_VALUES
					+ OPEN_BRACKET
					+ QUESTION
					+ COMMA
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
				preparedStatement.setString(1, tpIdCode);
				meIdCode = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(2, meIdCode);
				Log.debugMessage("TransmissionPathDatabase.insertTransmissionPathMELinks | Inserting link for transmission path "
								+ tpIdCode
								+ " and monitored element "
								+ meIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.insertTransmissionPathMELinks | Cannot insert link for monitored element "
							+ meIdCode + " and transmission path " + tpIdCode;
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

	
	private void createMEAttachment(TransmissionPath transmissionPath,
									Identifier monitoredElementId) throws UpdateObjectException {
		String tpIdStr = transmissionPath.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_INSERT_INTO + ObjectEntities.TRANSPATHMELINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLUMN_TRANSMISSION_PATH_ID
			+ COMMA
			+ LINK_COLUMN_MONITORED_ELEMENT_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET				
			+ tpIdStr 
			+ COMMA 
			+ meIdStr 
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.createMEAttachment | Cannot attach transmission path "
				+ tpIdStr + " to monitored element " + meIdStr;
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

	private void deleteMEAttachment(TransmissionPath transmissionPath,
									Identifier monitoredElementId) throws IllegalDataException {
		String tpIdStr = transmissionPath.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_DELETE_FROM 
					+ ObjectEntities.TRANSPATHMELINK_ENTITY
					+ SQL_WHERE
					+ LINK_COLUMN_TRANSMISSION_PATH_ID
					+ EQUALS 
					+ tpIdStr
					+ SQL_AND
					+ LINK_COLUMN_MONITORED_ELEMENT_ID
					+ EQUALS 
					+ meIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.deleteMEAttachment | Cannot detach transmission path "
				+ tpIdStr + " from monitored element " + meIdStr;
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


	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		try {
			switch (updateKind) {
				case TransmissionPath.UPDATE_ATTACH_ME:
					this.createMEAttachment(transmissionPath, (Identifier) obj);
					this.setModified(transmissionPath);
					break;
				case TransmissionPath.UPDATE_DETACH_ME:
					this.deleteMEAttachment(transmissionPath, (Identifier) obj);
					this.setModified(transmissionPath);
					break;
				default:
					return;
			}
		} catch (UpdateObjectException e) {
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
		} catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void setModified(TransmissionPath transmissionPath) throws UpdateObjectException {		
		String tpIdStr = transmissionPath.getId().toSQLString();
		String sql = SQL_UPDATE
					+ ObjectEntities.TRANSPATH_ENTITY
					+ SQL_SET
					+ COLUMN_MODIFIED + EQUALS
					+ DatabaseDate.toUpdateSubString(transmissionPath.getModified()) + COMMA
					+ COLUMN_MODIFIER_ID + EQUALS + transmissionPath.getModifierId().toSQLString()
					+ SQL_WHERE + EQUALS + tpIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL05);
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

	public void delete(TransmissionPath transmissionPath) {
		/**
		 * FIXME delete cascade from TRANSPATHMELINK_ENTITY too
		 */
		String tpIdStr = transmissionPath.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();								
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.TRANSPATH_ENTITY
									+ SQL_WHERE
									+ COLUMN_ID + EQUALS
									+ tpIdStr);
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
