/*
 * $Id: MonitoredElementDatabase.java,v 1.41 2005/02/04 14:22:18 bob Exp $
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.41 $, $Date: 2005/02/04 14:22:18 $
 * @author $Author: bob $
 * @module config_v1
 */

public class MonitoredElementDatabase extends StorableObjectDatabase {
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

  private static String columns;
	private static String updateMultiplySQLValues;

	private static final int SIZE_LOCAL_ADDRESS_COLUMN = 64;

	private MonitoredElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MonitoredElement)
			return (MonitoredElement)storableObject;
		throw new IllegalDataException("MonitoredElementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.ME_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID + COMMA
				+ MonitoredElementWrapper.COLUMN_SORT + COMMA
				+ MonitoredElementWrapper.COLUMN_LOCAL_ADDRESS;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA 
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
    	}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(monitoredElement.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(monitoredElement.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(monitoredElement.getMeasurementPortId()) + COMMA
			+ monitoredElement.getSort().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(monitoredElement.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN) + APOSTOPHE;
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, monitoredElement.getDomainId());
			DatabaseString.setString(preparedStatement, ++i, monitoredElement.getName(), SIZE_NAME_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, monitoredElement.getMeasurementPortId());
			preparedStatement.setInt( ++i, monitoredElement.getSort().value());
			DatabaseString.setString(preparedStatement, ++i, monitoredElement.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("MeasurmentPortTypeDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		super.retrieveEntity(monitoredElement);
		this.retrieveMonitoredDomainMemberIds(monitoredElement);
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MonitoredElement monitoredElement = (storableObject == null) ? null : this.fromStorableObject(storableObject);
		if (monitoredElement == null) {
			monitoredElement = new MonitoredElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
																null,
																null,
																null,
																null,
																0,
																null,
																null);			
		}
		monitoredElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
											 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
											 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
											 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
											 DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
											 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
											 DatabaseIdentifier.getIdentifier(resultSet, MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID),
											 resultSet.getInt(MonitoredElementWrapper.COLUMN_SORT),
											 DatabaseString.fromQuerySubString(resultSet.getString(MonitoredElementWrapper.COLUMN_LOCAL_ADDRESS)));
		return monitoredElement;
	}

	private void retrieveMonitoredDomainMemberIds(MonitoredElement monitoredElement) throws RetrieveObjectException {
		List mdmIds = new ArrayList();
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElement.getId());
		int meSort = monitoredElement.getSort().value();
		String column;
		String sql;	{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			switch (meSort) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:	
					buffer.append(MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID);
					column = MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID;
					break;
				case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					buffer.append(MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID);
					column = MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID;
					break;
				default:
					String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new RetrieveObjectException(mesg);
			}
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
			buffer.append(MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
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
				mdmIds.add(DatabaseIdentifier.getIdentifier(resultSet, column));				
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		monitoredElement.setMonitoredDomainMemberIds(mdmIds);
	}

	private void retrieveMonitoredDomainMemberIdsByOneQuery(List monitoredElements) throws RetrieveObjectException {
		Map sortedMonitoredElements = new HashMap();
		List monitoredElementsOneSort;
		Integer meSort;

		MonitoredElement monitoredElement;
		for (Iterator it = monitoredElements.iterator(); it.hasNext();) {
			monitoredElement = (MonitoredElement) it.next();
			meSort = new Integer(monitoredElement.getSort().value());
			monitoredElementsOneSort = (List) sortedMonitoredElements.get(meSort);
			if (monitoredElementsOneSort == null) {
				monitoredElementsOneSort = new LinkedList();
				sortedMonitoredElements.put(meSort, monitoredElementsOneSort);
			}
			monitoredElementsOneSort.add(monitoredElement);
		}

		for (Iterator it = sortedMonitoredElements.keySet().iterator(); it.hasNext();) {
			meSort = (Integer) it.next();
			monitoredElementsOneSort = (List) sortedMonitoredElements.get(meSort);
			switch (meSort.intValue()) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
					this.retrieveMDMIdsByOneQuery(monitoredElementsOneSort,
							ObjectEntities.EQUIPMENTMELINK_ENTITY,
							MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID);
					break;
				case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					this.retrieveMDMIdsByOneQuery(monitoredElementsOneSort,
							ObjectEntities.TRANSPATHMELINK_ENTITY,
							MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
					break;
				default:
					String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new RetrieveObjectException(mesg);
			}
		}
	}

	private void retrieveMDMIdsByOneQuery(List monitoredElements, String linkTable, String linkColumn) throws RetrieveObjectException {
		if (monitoredElements == null || monitoredElements.isEmpty())
			return;
		
		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ linkColumn
				+ SQL_FROM + linkTable
				+ SQL_WHERE + MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET);
		int i = 1;
		for (Iterator it = monitoredElements.iterator(); it.hasNext(); i++) {
			MonitoredElement monitoredElement = (MonitoredElement) it.next();
			sql.append(DatabaseIdentifier.toSQLString(monitoredElement.getId()));
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER) != 0)
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}
			}
		}
		sql.append(CLOSE_BRACKET);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElementDatabase.retrieveMDMIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map mdmIdsMap = new HashMap();
			Identifier meId;
			List mdmIds;
			while (resultSet.next()) {
				meId = DatabaseIdentifier.getIdentifier(resultSet, MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
				mdmIds = (List) mdmIdsMap.get(meId);
				if (mdmIds == null) {
					mdmIds = new LinkedList();
					mdmIdsMap.put(meId, mdmIds);
				}
				mdmIds.add(DatabaseIdentifier.getIdentifier(resultSet, linkColumn));
			}

			MonitoredElement monitoredElement;
			for (Iterator it = monitoredElements.iterator(); it.hasNext();) {
				monitoredElement = (MonitoredElement) it.next();
				meId = monitoredElement.getId();
				mdmIds = (List) mdmIdsMap.get(meId);

				monitoredElement.setMonitoredDomainMemberIds(mdmIds);
			}
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.retrieveMDMIdsByOneQuery | Cannot retrieve parameters for result -- "
					+ sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException{
//		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
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

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
			MonitoredElement monitoredElement = (MonitoredElement) iter.next();
			insertMonitoredDomainMemberIds(monitoredElement);						
		}
	}

	private void insertMonitoredDomainMemberIds(MonitoredElement monitoredElement) throws CreateObjectException {
		List mdmIds = monitoredElement.getMonitoredDomainMemberIds();
		Identifier meId = monitoredElement.getId();
		int meSort = monitoredElement.getSort().value();

		String sql;
		{
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
					buffer.append(MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID);
					break;
				case	MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					buffer.append(MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID);
					break;
				default:
					String mesg = "MonitoredElementDatabase.insertMonitoredDomainMemberIds | ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new CreateObjectException(mesg);
			}
			buffer.append(COMMA);
			buffer.append(MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
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
		Identifier mdmId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator it = mdmIds.iterator(); it.hasNext();) {
				mdmId = (Identifier)it.next(); 
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, mdmId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, meId);
				Log.debugMessage("MonitoredElementDatabase.insertMonitoredDomainMemberIds | Inserting link for monitored element '" + meId.getIdentifierString() + "' and monitored domain member '" + mdmId.getIdentifierString() + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.insertMonitoredDomainMemberIds | Cannot insert link for monitored element '" + meId.getIdentifierString() + "' and monitored domain member '" + mdmId.getIdentifierString() + "'";
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
			finally {
                DatabaseConnection.releaseConnection(connection);
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
			list = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("MonitoredElementDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return list;
	}

	public void delete(StorableObject storableObject) throws IllegalDataException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElement.getId());
		int meSort = monitoredElement.getSort().value();

		String sql1;
		{
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
			buffer.append(MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
			buffer.append(EQUALS);
			buffer.append(meIdStr);
			sql1 = buffer.toString();
		}

		String sql2 = SQL_DELETE_FROM
			+ ObjectEntities.ME_ENTITY
			+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + meIdStr;

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
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public List retrieveByIds(List ids, String condition) 
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);

		this.retrieveMonitoredDomainMemberIdsByOneQuery(list);

		return list;
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;

		String condition = DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId());

		try {
			list = this.retrieveButIds(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("MonitoredElementDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}

		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list;
		if (condition instanceof DomainCondition) {
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		}
		else {
			Log.errorMessage("MonitoredElementDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}	
}
