/*
 * $Id: KISDatabase.java,v 1.15 2004/08/18 12:37:48 arseniy Exp $
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.15 $, $Date: 2004/08/18 12:37:48 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class KISDatabase extends StorableObjectDatabase {
	// table :: kis
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";	
	// mcm_id Identifier NOT NULL
	public static final String COLUMN_MCM_ID = "mcm_id";
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private KIS fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof KIS)
			return (KIS) storableObject;
		throw new IllegalDataException("KISDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		this.retrieveKIS(kis);	
	}

	private void retrieveKIS(KIS kis) throws ObjectNotFoundException, RetrieveObjectException {
		String kisIdStr = kis.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_CREATED) + COMMA
			+ DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_MCM_ID
			+ SQL_FROM + ObjectEntities.KIS_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + kisIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveKIS | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {				
				kis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
													resultSet.getString(COLUMN_NAME),
													resultSet.getString(COLUMN_DESCRIPTION),													
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(COLUMN_MCM_ID)));
			}
			else
				throw new ObjectNotFoundException("No such kis: " + kisIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveKIS | Cannot retrieve kis " + kisIdStr;
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
	
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			case KIS.RETRIEVE_MONITORED_ELEMENTS:
				return this.retrieveMonitoredElements(kis);
			default:
				return null;
		}
	}
	
	private List retrieveMonitoredElements(KIS kis) throws RetrieveObjectException {
		List monitoredElements = new ArrayList();

		String kisIdStr = kis.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.ME_ENTITY
			+ SQL_WHERE + MonitoredElementDatabase.COLUMN_MEASUREMENT_PORT_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
				+ SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID + EQUALS + kisIdStr
			+ CLOSE_BRACKET;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveMonitoredElements | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				monitoredElements.add((MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_ID)), true));
			}
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveMonitoredElements | Cannot retrieve monitored elements for kis " + kisIdStr;
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
		return monitoredElements;
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		try {
			this.insertKIS(kis);
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

	private void insertKIS(KIS kis) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String kisIdCode = kis.getId().getCode();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier domainId = kis.getDomainId();
		
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier mcmId = kis.getMCMId();
		
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.KIS_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_MCM_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
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
			preparedStatement.setString(1, kisIdCode);
			preparedStatement.setTimestamp(2, new Timestamp(kis.getCreated().getTime()));
			preparedStatement.setTimestamp(3, new Timestamp(kis.getModified().getTime()));
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(4, kis.getCreatorId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(5, kis.getModifierId().getCode());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(6, (domainId != null)?domainId.getCode():Identifier.getNullSQLString());

			preparedStatement.setString(7, kis.getName() );
			
			preparedStatement.setString(8, kis.getDescription());			
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(9, (mcmId != null)?mcmId.getCode():Identifier.getNullSQLString());
										
			Log.debugMessage("KISDatabase.insertKIS | Trying: " + sql, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.insertKIS | Cannot insert kis " + kisIdCode;
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
	
	public static void delete(KIS kis) {
		String kisIdStr = kis.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.KIS_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ kisIdStr;
			Log.debugMessage("KISDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL05);
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
	
	public static List retrieveAll() throws RetrieveObjectException {
		List kiss = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.KIS_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				kiss.add(new KIS(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveAll | Cannot retrieve kis";
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
		return kiss;
	}
}
