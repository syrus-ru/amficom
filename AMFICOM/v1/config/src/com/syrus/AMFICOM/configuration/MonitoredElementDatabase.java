/*
 * $Id: MonitoredElementDatabase.java,v 1.66 2005/05/23 18:45:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
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
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.66 $, $Date: 2005/05/23 18:45:19 $
 * @author $Author: bass $
 * @module config_v1
 */

public class MonitoredElementDatabase extends StorableObjectDatabase {

	public static final int		CHARACTER_NUMBER_OF_RECORDS	= 1;

	private static String		columns;
	private static String		updateMultipleSQLValues;

	private static final int	SIZE_LOCAL_ADDRESS_COLUMN	= 64;

	private MonitoredElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MonitoredElement)
			return (MonitoredElement) storableObject;
		throw new IllegalDataException("MonitoredElementDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.MONITORED_ELEMENT_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
					+ StorableObjectWrapper.COLUMN_NAME + COMMA + MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID
					+ COMMA + MonitoredElementWrapper.COLUMN_SORT + COMMA
					+ MonitoredElementWrapper.COLUMN_LOCAL_ADDRESS;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		String sql = DatabaseIdentifier.toSQLString(monitoredElement.getDomainId()) + COMMA + APOSTOPHE
				+ DatabaseString.toQuerySubString(monitoredElement.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(monitoredElement.getMeasurementPortId()) + COMMA
				+ monitoredElement.getSort().value() + COMMA + APOSTOPHE
				+ DatabaseString.toQuerySubString(monitoredElement.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN)
				+ APOSTOPHE;
		return sql;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject,
												PreparedStatement preparedStatement,
												int startParameterNumber) throws IllegalDataException, SQLException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, monitoredElement.getDomainId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, monitoredElement.getName(), SIZE_NAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, monitoredElement.getMeasurementPortId());
		preparedStatement.setInt(++startParameterNumber, monitoredElement.getSort().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, monitoredElement.getLocalAddress(),
			SIZE_LOCAL_ADDRESS_COLUMN);
		return startParameterNumber;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		super.retrieveEntity(monitoredElement);
		this.retrieveMonitoredDomainMemberIds(monitoredElement);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		MonitoredElement monitoredElement = (storableObject == null) ? null : this.fromStorableObject(storableObject);
		if (monitoredElement == null) {
			monitoredElement = new MonitoredElement(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, 0, null, null);
		}
		monitoredElement.setAttributes(
			DatabaseDate.fromQuerySubString(resultSet,StorableObjectWrapper.COLUMN_CREATED),
			DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
			DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
			DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
			resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
			DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
			DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
			DatabaseIdentifier.getIdentifier(resultSet, MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID),
			resultSet.getInt(MonitoredElementWrapper.COLUMN_SORT),
			DatabaseString.fromQuerySubString(resultSet.getString(MonitoredElementWrapper.COLUMN_LOCAL_ADDRESS)));
		return monitoredElement;
	}

	private void retrieveMonitoredDomainMemberIds(MonitoredElement monitoredElement) throws RetrieveObjectException {
		Set mdmIds = new HashSet();
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElement.getId());
		int meSort = monitoredElement.getSort().value();
		String column;
		String sql;
		{
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
				case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
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
			Log.debugMessage("MonitoredElementDatabase.retrieveMonitoredDomainMemberIds | Trying: " + sql,
				Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				mdmIds.add(DatabaseIdentifier.getIdentifier(resultSet, column));
			}
		} catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.retrieveMonitoredDomainMemberIds | Cannot retrieve monitored domain members for monitored element "
					+ meIdStr;
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		monitoredElement.setMonitoredDomainMemberIds0(mdmIds);
	}

	private void retrieveMonitoredDomainMemberIdsByOneQuery(Set monitoredElements) throws RetrieveObjectException {
		Map sortedMonitoredElements = new HashMap();
		Set monitoredElementsOneSort;
		Integer meSort;

		MonitoredElement monitoredElement;
		for (Iterator it = monitoredElements.iterator(); it.hasNext();) {
			monitoredElement = (MonitoredElement) it.next();
			meSort = new Integer(monitoredElement.getSort().value());
			monitoredElementsOneSort = (Set) sortedMonitoredElements.get(meSort);
			if (monitoredElementsOneSort == null) {
				monitoredElementsOneSort = new HashSet();
				sortedMonitoredElements.put(meSort, monitoredElementsOneSort);
			}
			monitoredElementsOneSort.add(monitoredElement);
		}

		for (Iterator it = sortedMonitoredElements.keySet().iterator(); it.hasNext();) {
			meSort = (Integer) it.next();
			monitoredElementsOneSort = (Set) sortedMonitoredElements.get(meSort);
			switch (meSort.intValue()) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
					this.retrieveMDMIdsByOneQuery(monitoredElementsOneSort,
							ObjectEntities.EQUIPMENTMELINK_ENTITY,
							MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID);
					break;
				case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					this.retrieveMDMIdsByOneQuery(monitoredElementsOneSort,
							ObjectEntities.TRANSPATHMELINK_ENTITY,
							MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID);
					break;
				default:
					String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new RetrieveObjectException(mesg);
			}
		}
	}

	private void retrieveMDMIdsByOneQuery(Set monitoredElements, String linkTable, String linkColumn)
			throws RetrieveObjectException {
		if (monitoredElements == null || monitoredElements.isEmpty())
			return;

		Map mdmIdsMap = null;
		mdmIdsMap = this.retrieveLinkedEntityIds(monitoredElements,
				linkTable,
				MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID,
				linkColumn);

		Identifier meId;
		Set mdmIds;
		MonitoredElement monitoredElement;
		for (Iterator it = monitoredElements.iterator(); it.hasNext();) {
			monitoredElement = (MonitoredElement) it.next();
			meId = monitoredElement.getId();
			mdmIds = (Set) mdmIdsMap.get(meId);

			monitoredElement.setMonitoredDomainMemberIds0(mdmIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  monitoredElement.getId() + "'; argument: " + arg);
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
			this.delete(monitoredElement.getId());
			throw coe;
		}
	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
			MonitoredElement monitoredElement = (MonitoredElement) iter.next();
			this.insertMonitoredDomainMemberIds(monitoredElement);
		}
	}

	private void insertMonitoredDomainMemberIds(MonitoredElement monitoredElement) throws CreateObjectException {
		Set mdmIds = monitoredElement.getMonitoredDomainMemberIds();
		Identifier meId = monitoredElement.getId();
		int meSort = monitoredElement.getSort().value();
		
		StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
		switch (meSort) {
			case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
				buffer.append(ObjectEntities.EQUIPMENTMELINK_ENTITY);
				break;
			case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
				buffer.append(ObjectEntities.TRANSPATHMELINK_ENTITY);
				break;
			default:
				String mesg = "MonitoredElementDatabase.insertMonitoredDomainMemberIds | ERROR: Unknown sort of monitoredelement: "
						+ meSort;
				throw new CreateObjectException(mesg);
		}
		buffer.append(OPEN_BRACKET);
		switch (meSort) {
			case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
				buffer.append(MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID);
				break;
			case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
				buffer.append(MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID);
				break;
			default:
				String mesg = "MonitoredElementDatabase.insertMonitoredDomainMemberIds | ERROR: Unknown sort of monitoredelement: "
						+ meSort;
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

		PreparedStatement preparedStatement = null;
		Identifier mdmId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(buffer.toString());
			for (Iterator it = mdmIds.iterator(); it.hasNext();) {
				mdmId = (Identifier) it.next();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, mdmId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, meId);
				Log.debugMessage("MonitoredElementDatabase.insertMonitoredDomainMemberIds | Inserting link for monitored element '"
						+ meId.getIdentifierString()
						+ "' and monitored domain member '"
						+ mdmId.getIdentifierString()
						+ "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElementDatabase.insertMonitoredDomainMemberIds | Cannot insert link for monitored element '"
					+ meId.getIdentifierString()
					+ "' and monitored domain member '"
					+ mdmId.getIdentifierString()
					+ "'";
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

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		this.updateMonitoredDomainMemberIds(Collections.singleton(storableObject));
	}

	public void update(Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		this.updateMonitoredDomainMemberIds(storableObjects);
	}

	private void updateMonitoredDomainMemberIds(Set monitoredElements) throws UpdateObjectException {
		if (monitoredElements == null || monitoredElements.isEmpty())
			return;

		Map sortedMonitoredElements = new HashMap();
		Set monitoredElementsOneSort;
		Integer meSort;

		MonitoredElement monitoredElement;
		for (Iterator it = monitoredElements.iterator(); it.hasNext();) {
			monitoredElement = (MonitoredElement) it.next();
			meSort = new Integer(monitoredElement.getSort().value());
			monitoredElementsOneSort = (Set) sortedMonitoredElements.get(meSort);
			if (monitoredElementsOneSort == null) {
				monitoredElementsOneSort = new HashSet();
				sortedMonitoredElements.put(meSort, monitoredElementsOneSort);
			}
			monitoredElementsOneSort.add(monitoredElement);
		}

		for (Iterator it = sortedMonitoredElements.keySet().iterator(); it.hasNext();) {
			meSort = (Integer) it.next();
			monitoredElementsOneSort = (Set) sortedMonitoredElements.get(meSort);
			switch (meSort.intValue()) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
					this.updateMDMIds(monitoredElementsOneSort,
							ObjectEntities.EQUIPMENTMELINK_ENTITY,
							MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID);
					break;
				case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					this.updateMDMIds(monitoredElementsOneSort,
							ObjectEntities.TRANSPATHMELINK_ENTITY,
							MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID);
					break;
				default:
					String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new UpdateObjectException(mesg);
			}
		}
	}

	private void updateMDMIds(Set monitoredElements, String linkTable, String linkColumn) throws UpdateObjectException {
		if (monitoredElements == null || monitoredElements.isEmpty())
			return;

		Map mdmIdsMap = new HashMap();
		try {
			for (Iterator it = monitoredElements.iterator(); it.hasNext();) {
				MonitoredElement monitoredElement = this.fromStorableObject((StorableObject) it.next());
				Set mdmIds = monitoredElement.getMonitoredDomainMemberIds();
				mdmIdsMap.put(monitoredElement.getId(), mdmIds);
			}

			super.updateLinkedEntityIds(mdmIdsMap, linkTable, MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID, linkColumn);
		}
		catch (IllegalDataException ide) {
			throw new UpdateObjectException("Cannot update monitored element domain members ids -- "  + ide.getMessage(), ide);
		}
	}

	public void delete(Identifier id) {
		assert (id.getMajor() == ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE) : "Illegal entity code: "
				+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		try {
			MonitoredElement monitoredElement = new MonitoredElement(id);
			String meIdStr = DatabaseIdentifier.toSQLString(monitoredElement.getId());
			int meSort = monitoredElement.getSort().value();

			StringBuffer sql1 = new StringBuffer(SQL_DELETE_FROM);
			switch (meSort) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
					sql1.append(ObjectEntities.EQUIPMENTMELINK_ENTITY);
					break;
				case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					sql1.append(ObjectEntities.TRANSPATHMELINK_ENTITY);
					break;
				default:
					String mesg = "MonitoredElementDatabase.delete | ERROR: Unknown sort of monitored element: "
							+ meSort;
					Log.errorMessage(mesg);
			}
			sql1.append(SQL_WHERE);
			sql1.append(MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
			sql1.append(EQUALS);
			sql1.append(meIdStr);

			String sql2 = SQL_DELETE_FROM + ObjectEntities.MONITORED_ELEMENT_ENTITY
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + meIdStr;

			Statement statement = null;
			Connection connection = DatabaseConnection.getConnection();
			try {
				statement = connection.createStatement();
				Log.debugMessage("MonitoredElementDatabase.delete | Trying: " + sql1, Log.DEBUGLEVEL09);
				statement.executeUpdate(sql1.toString());
				Log.debugMessage("MonitoredElementDatabase.delete | Trying: " + sql2, Log.DEBUGLEVEL09);
				statement.executeUpdate(sql2);
				connection.commit();
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
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
				finally {
					DatabaseConnection.releaseConnection(connection);
				}
			}
		}
		catch (RetrieveObjectException e) {
			Log.errorException(e);
		}
		catch (ObjectNotFoundException e) {
			Log.errorException(e);
		}
	}

	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Set collection = super.retrieveByCondition(conditionQuery);
		this.retrieveMonitoredDomainMemberIdsByOneQuery(collection);
		return collection;
	}

}
