/*
 * $Id: MonitoredElementDatabase.java,v 1.13 2004/08/29 10:54:24 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
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
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.13 $, $Date: 2004/08/29 10:54:24 $
 * @author $Author: bob $
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
    
	private MonitoredElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MonitoredElement)
			return (MonitoredElement)storableObject;
		throw new IllegalDataException("MonitoredElementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		this.retrieveMonitoredElement(monitoredElement);
		this.retrieveMonitoredDomainMemberIds(monitoredElement);
	}

	private String retrieveMonitoredElementQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ DomainMember.COLUMN_DOMAIN_ID + COMMA		
		+ COLUMN_MEASUREMENT_PORT_ID + COMMA
		+ COLUMN_SORT + COMMA
		+ COLUMN_LOCAL_ADDRESS
		+ SQL_FROM + ObjectEntities.ME_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private MonitoredElement updateMonitoredElementFromResultSet(MonitoredElement monitoredElement, ResultSet resultSet) throws SQLException{
		MonitoredElement monitoredElement1 = monitoredElement;
		if (monitoredElement1 == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			monitoredElement1 = new MonitoredElement(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 0, null, null);			
		}
		monitoredElement1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
		return monitoredElement1;
	}

	private void retrieveMonitoredElement(MonitoredElement monitoredElement) throws ObjectNotFoundException, RetrieveObjectException {
		String meIdStr = monitoredElement.getId().toSQLString();
		String sql = retrieveMonitoredElementQuery(COLUMN_ID + EQUALS + meIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElementDatabase.retrieveMonitoredElement | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				updateMonitoredElementFromResultSet(monitoredElement, resultSet);
			else
				throw new ObjectNotFoundException("No such monitored element: " + meIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.retrieveMonitoredElement | Cannot retrieve monitored element " + meIdStr;
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
			this.insertMonitoredElement(monitoredElement);
			this.insertMonitoredDomainMemberIds(monitoredElement);
		}
		catch (CreateObjectException coe) {
			this.delete(monitoredElement);
			throw coe;
		}
	}

	private void insertMonitoredElement(MonitoredElement monitoredElement) throws CreateObjectException {
		String meIdStr = monitoredElement.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.ME_ENTITY + OPEN_BRACKET 
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_MEASUREMENT_PORT_ID + COMMA
			+ COLUMN_SORT + COMMA
			+ COLUMN_LOCAL_ADDRESS
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET			
			+ meIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(monitoredElement.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(monitoredElement.getModified()) + COMMA
			+ monitoredElement.getCreatorId().toSQLString() + COMMA
			+ monitoredElement.getModifierId().toSQLString() + COMMA
			+ monitoredElement.getDomainId().toSQLString() + COMMA
			+ monitoredElement.getMeasurementPortId().toSQLString() + COMMA
			+ monitoredElement.getSort().value() + COMMA
			+ APOSTOPHE + monitoredElement.getLocalAddress() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElementDatabase.insertMonitoredElement | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.insertMonitoredElement | Cannot insert monitored element " + meIdStr;
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
			}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws IllegalDataException, UpdateObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
	
	public List retrieveAll() throws RetrieveObjectException {
		return retriveByIdsOneQuery(null);
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
			sql = retrieveMonitoredElementQuery(condition);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElementDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateMonitoredElementFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			
			sql = retrieveMonitoredElementQuery(buffer.toString());
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
					result.add(updateMonitoredElementFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("MonitoredElementDatabase.retriveByIdsPreparedStatement | No such monitored element: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.retriveByIdsPreparedStatement | Cannot retrieve monitored element " + sqle.getMessage();
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
