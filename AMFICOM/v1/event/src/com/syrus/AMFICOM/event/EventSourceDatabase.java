/*
 * $Id: EventSourceDatabase.java,v 1.2 2005/02/08 20:31:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/08 20:31:06 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventSourceDatabase extends StorableObjectDatabase {

	private static String columns;

	private static String updateMultiplySQLValues;

	protected String getEnityName() {		
		return ObjectEntities.EVENTSOURCE_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ EventSourceWrapper.COLUMN_SOURCE_ENTITY_CODE
				+ EventSourceWrapper.COLUMN_MCM_ID
				+ EventSourceWrapper.COLUMN_PORT_ID
				+ EventSourceWrapper.COLUMN_EQUIPMENT_ID
				+ EventSourceWrapper.COLUMN_TRANSMISSION_PATH_ID
				+ EventSourceWrapper.COLUMN_LINK_ID;
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
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	private EventSource fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EventSource)
			return (EventSource) storableObject;
		throw new IllegalDataException("EventSourceDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException,
				UpdateObjectException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		Identifier sourceEntityId = eventSource.getSourceEntityId();
		short sourceEntityCode = sourceEntityId.getMajor();
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			preparedStatement.setShort(++i, sourceEntityCode);
			switch (sourceEntityCode) {
				case ObjectEntities.MCM_ENTITY_CODE:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, sourceEntityId);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					break;
				case ObjectEntities.PORT_ENTITY_CODE:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, sourceEntityId);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					break;
				case ObjectEntities.EQUIPMENT_ENTITY_CODE:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, sourceEntityId);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					break;
				case ObjectEntities.TRANSPATH_ENTITY_CODE:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, sourceEntityId);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					break;
				case ObjectEntities.LINK_ENTITY_CODE:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, sourceEntityId);
					break;
				default:
					throw new IllegalDataException("Event source for entity code "
							+ sourceEntityCode
							+ ", entity '"
							+ ObjectEntities.codeToString(sourceEntityCode)
							+ "' not implemented");
			}
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(),
					sqle);
		}
		return i;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		Identifier sourceEntityId = eventSource.getSourceEntityId();
		short sourceEntityCode = sourceEntityId.getMajor();
		StringBuffer buffer = new StringBuffer(super.getUpdateSingleSQLValues(storableObject)
				+ COMMA
				+ Short.toString(sourceEntityCode)
				+ COMMA);
		switch (sourceEntityCode) {
			case ObjectEntities.MCM_ENTITY_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				break;
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				break;
			case ObjectEntities.LINK_ENTITY_CODE:
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier) null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				break;
			default:
				throw new IllegalDataException("Event source for entity code "
						+ sourceEntityCode
						+ ", entity '"
						+ ObjectEntities.codeToString(sourceEntityCode)
						+ "' not implemented");
		}
		return buffer.toString();
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		EventSource eventSource = (storableObject == null) ? new EventSource(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID),
				null,
				null) : this.fromStorableObject(storableObject);
		short sourceEntityCode = resultSet.getShort(EventSourceWrapper.COLUMN_SOURCE_ENTITY_CODE);
		Identifier sourceEntityId = null;
		switch (sourceEntityCode) {
			case ObjectEntities.MCM_ENTITY_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_MCM_ID);
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_PORT_ID);
				break;
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_EQUIPMENT_ID);
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_TRANSMISSION_PATH_ID);
				break;
			case ObjectEntities.LINK_ENTITY_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_LINK_ID);
				break;
			default:
				throw new IllegalDataException("Event source for entity code "
						+ sourceEntityCode
						+ ", entity '"
						+ ObjectEntities.codeToString(sourceEntityCode)
						+ "' not implemented");
		}
		eventSource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED), 
				 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				 sourceEntityId);

		return eventSource;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		this.retrieveEntity(eventSource);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException,
				ObjectNotFoundException,
				RetrieveObjectException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		this.insertEntity(eventSource);
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);

		return this.retrieveByIdsOneQuery(ids, condition);
	}

	public void update(StorableObject storableObject, int updateKind, Object arg)
			throws IllegalDataException,
				VersionCollisionException,
				UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntity(storableObject, true);
				return;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException,
				VersionCollisionException,
				UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
			return;
		}
	}

}
