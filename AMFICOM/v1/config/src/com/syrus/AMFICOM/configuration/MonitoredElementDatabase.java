/*
 * $Id: MonitoredElementDatabase.java,v 1.19 2004/09/20 14:17:31 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.19 $, $Date: 2004/09/20 14:17:31 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class MonitoredElementDatabase extends StorableObjectDatabase {
    public static final String COLUMN_MEASUREMENT_PORT_ID = "measurement_port_id";
    // sort NUMBER(2) NOT NULL,
    public static final String COLUMN_SORT = "sort";
    public static final String COLUMN_LOCAL_ADDRESS = "local_address";

	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String LINK_COLUMN_EQUIPMENT_ID = "equipment_id";
	public static final String LINK_COLUMN_TRANSMISSION_PATH_ID = "transmission_path_id";

    public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
    
    private String updateColumns;
	private String updateMultiplySQLValues;
	
	private MonitoredElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MonitoredElement)
			return (MonitoredElement)storableObject;
		throw new IllegalDataException("MonitoredElementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return "MonitoredElement";
	}
	
	protected String getTableName() {
		return ObjectEntities.ME_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
    		this.updateColumns = super.getUpdateColumns() + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_MEASUREMENT_PORT_ID + COMMA
			+ COLUMN_SORT + COMMA
			+ COLUMN_LOCAL_ADDRESS;
		}
		return this.updateColumns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
    		this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA 
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
    	}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		MonitoredElement monitoredElement = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
				+ monitoredElement.getDomainId().toSQLString() + COMMA
				+ monitoredElement.getMeasurementPortId().toSQLString() + COMMA
				+ monitoredElement.getSort().value() + COMMA
				+ APOSTOPHE + monitoredElement.getLocalAddress() + APOSTOPHE;
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		MonitoredElement monitoredElement = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i, monitoredElement.getDomainId().toString());
			preparedStatement.setString( ++i, monitoredElement.getMeasurementPortId().toString());
			preparedStatement.setInt( ++i, monitoredElement.getSort().value());
			preparedStatement.setString( ++i, monitoredElement.getLocalAddress());
		}catch (SQLException sqle) {
			throw new UpdateObjectException("MeasurmentPortTypeDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
		
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		super.retrieveEntity(monitoredElement);
		this.retrieveMonitoredDomainMemberIds(monitoredElement);
	}

	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA		
				+ COLUMN_MEASUREMENT_PORT_ID + COMMA
				+ COLUMN_SORT + COMMA
				+ COLUMN_LOCAL_ADDRESS
				+ SQL_FROM + ObjectEntities.ME_ENTITY
				+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MonitoredElement monitoredElement = storableObject == null ? null : fromStorableObject(storableObject);
		if (monitoredElement == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			monitoredElement = new MonitoredElement(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 0, null, null);			
		}
		monitoredElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
									   /**
										 * @todo when change DB Identifier model ,change getString() to
										 *       getLong()
										 */
									   new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),
									   /**
										 * @todo when change DB Identifier model ,change getString() to
										 *       getLong()
										 */
									   new Identifier(resultSet.getString(COLUMN_MEASUREMENT_PORT_ID)),
									   resultSet.getInt(COLUMN_SORT),
									   resultSet.getString(COLUMN_LOCAL_ADDRESS));
		return monitoredElement;
	}

	private void retrieveMonitoredDomainMemberIds(MonitoredElement monitoredElement) throws RetrieveObjectException {
		List mdmIds = new ArrayList();
		String meIdStr = monitoredElement.getId().toSQLString();
		int meSort = monitoredElement.getSort().value();

		String sql;	{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			switch (meSort) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:	
					buffer.append(LINK_COLUMN_EQUIPMENT_ID);
					break;
				case	MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					buffer.append(LINK_COLUMN_TRANSMISSION_PATH_ID);
					break;
				default:
					String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new RetrieveObjectException(mesg);
			}
			buffer.append(" mdm_id ");
			buffer.append(SQL_FROM);
			switch (meSort) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:	
					buffer.append(ObjectEntities.EQUIPMENTMELINK_ENTITY);
					break;
				case	MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					buffer.append(ObjectEntities.TRANSPATHMELINK_ENTITY);
					break;
				default:
					String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new RetrieveObjectException(mesg);
			}
			buffer.append(SQL_WHERE);
			buffer.append(LINK_COLUMN_MONITORED_ELEMENT_ID);
			buffer.append(EQUALS);
			buffer.append(meIdStr);
			sql = buffer.toString();
		}

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElementDatabase.retrieveMonitoredDomainMemberIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				/**
				* @todo when change DB Identifier model ,change getString() to getLong()
				*/
				mdmIds.add(new Identifier(resultSet.getString("mdm_id")));				
			}
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.retrieveMonitoredDomainMemberIds | Cannot retrieve monitored domain members for monitored element " + meIdStr;
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
			} finally {
                DatabaseConnection.closeConnection(connection);
            }
		}
		monitoredElement.setMonitoredDomainMemberIds(mdmIds);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException{
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		try {
			super.insertEntity(monitoredElement);
			this.insertMonitoredDomainMemberIds(monitoredElement);
		}
		catch (CreateObjectException coe) {
			this.delete(monitoredElement);
			throw coe;
		}
	}
	
	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
		for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
			MonitoredElement monitoredElement = (MonitoredElement) iter.next();
			insertMonitoredDomainMemberIds(monitoredElement);						
		}
	}
	
	private void insertMonitoredDomainMemberIds(MonitoredElement monitoredElement) throws CreateObjectException {
		List mdmIds = monitoredElement.getMonitoredDomainMemberIds();
		String meIdCode = monitoredElement.getId().getCode();
		int meSort = monitoredElement.getSort().value();

		String sql; {
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			switch (meSort) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:	
					buffer.append(ObjectEntities.EQUIPMENTMELINK_ENTITY);
					break;
				case	MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					buffer.append(ObjectEntities.TRANSPATHMELINK_ENTITY);
					break;
				default:
					String mesg = "MonitoredElementDatabase.insertMonitoredDomainMemberIds | ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new CreateObjectException(mesg);
			}
			buffer.append(OPEN_BRACKET);
			switch (meSort) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:	
					buffer.append(LINK_COLUMN_EQUIPMENT_ID);
					break;
				case	MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					buffer.append(LINK_COLUMN_TRANSMISSION_PATH_ID);
					break;
				default:
					String mesg = "MonitoredElementDatabase.insertMonitoredDomainMemberIds | ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new CreateObjectException(mesg);
			}
			buffer.append(COMMA);
			buffer.append(LINK_COLUMN_MONITORED_ELEMENT_ID);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}

		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String mdmIdCode = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator it = mdmIds.iterator(); it.hasNext();) {
				mdmIdCode = ((Identifier)it.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(1, mdmIdCode);
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(2, meIdCode);
				Log.debugMessage("MonitoredElementDatabase.insertMonitoredDomainMemberIds | Inserting link for monitored element '" + meIdCode + "' and monitored domain member '" + mdmIdCode + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.insertMonitoredDomainMemberIds | Cannot insert link for monitored element '" + meIdCode + "' and monitored domain member '" + mdmIdCode + "'";
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
			} finally {
                DatabaseConnection.closeConnection(connection);
            }
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(storableObject, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(storableObject, false);
		break;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}
	}
	
	public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("MonitoredElementDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }

	public void delete(MonitoredElement monitoredElement) {
		String meIdStr = monitoredElement.getId().toSQLString();
		int meSort = monitoredElement.getSort().value();
		
		String sql1; {
			StringBuffer buffer = new StringBuffer(SQL_DELETE_FROM);
			switch (meSort) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:	
					buffer.append(ObjectEntities.EQUIPMENTMELINK_ENTITY);
					break;
				case	MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					buffer.append(ObjectEntities.TRANSPATHMELINK_ENTITY);
					break;
				default:
					String mesg = "MonitoredElementDatabase.delete | ERROR: Unknown sort of monitored element: " + meSort;
					Log.errorMessage(mesg);
			}
			buffer.append(SQL_WHERE);
			buffer.append(LINK_COLUMN_MONITORED_ELEMENT_ID);
			buffer.append(EQUALS);
			buffer.append(meIdStr);
			sql1 = buffer.toString();
		}

		String sql2 = SQL_DELETE_FROM
			+ ObjectEntities.ME_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + meIdStr;

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElementDatabase.delete | Trying: " + sql1, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql1);
			Log.debugMessage("MonitoredElementDatabase.delete | Trying: " + sql2, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql2);
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
			} finally {
                DatabaseConnection.closeConnection(connection);
            }
		}
	}

	public List retrieveByIds(List ids, String condition) 
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = super.retrieveByIdsOneQuery(null, condition);
		else list = super.retrieveByIdsOneQuery(ids, condition);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			MonitoredElement monitoredElement = (MonitoredElement) iter.next();
			this.retrieveMonitoredDomainMemberIds(monitoredElement);
		}
		return list;
	}

}
