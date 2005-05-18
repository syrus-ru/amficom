/*
 * $Id: EventSourceDatabase.java,v 1.15 2005/05/18 11:16:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.15 $, $Date: 2005/05/18 11:16:58 $
 * @author $Author: bass $
 * @module event_v1
 */
public class EventSourceDatabase extends StorableObjectDatabase {

	private static String columns;

	private static String updateMultipleSQLValues;

	protected String getEnityName() {		
		return ObjectEntities.EVENTSOURCE_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = EventSourceWrapper.COLUMN_SOURCE_ENTITY_CODE + COMMA
				+ EventSourceWrapper.COLUMN_MCM_ID + COMMA
				+ EventSourceWrapper.COLUMN_PORT_ID + COMMA
				+ EventSourceWrapper.COLUMN_EQUIPMENT_ID + COMMA
				+ EventSourceWrapper.COLUMN_TRANSMISSION_PATH_ID + COMMA
				+ EventSourceWrapper.COLUMN_LINK_ID;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	private EventSource fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EventSource)
			return (EventSource) storableObject;
		throw new IllegalDataException("EventSourceDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		Identifier sourceEntityId = eventSource.getSourceEntityId();
		short sourceEntityCode = sourceEntityId.getMajor();
		preparedStatement.setShort(++startParameterNumber, sourceEntityCode);
		switch (sourceEntityCode) {
			case ObjectEntities.MCM_ENTITY_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				break;
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				break;
			case ObjectEntities.LINK_ENTITY_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				break;
			default:
				throw new IllegalDataException("Event source for entity code "
						+ sourceEntityCode
						+ ", entity '"
						+ ObjectEntities.codeToString(sourceEntityCode)
						+ "' not implemented");
		}
		return startParameterNumber;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		Identifier sourceEntityId = eventSource.getSourceEntityId();
		short sourceEntityCode = sourceEntityId.getMajor();
		StringBuffer buffer = new StringBuffer(Short.toString(sourceEntityCode));
		buffer.append(COMMA);
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
				SQLException {
		EventSource eventSource = (storableObject == null) ? new EventSource(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID),
				null,
				0L,
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
				 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				 sourceEntityId);

		return eventSource;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		this.retrieveEntity(eventSource);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  eventSource.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		EventSource eventSource = this.fromStorableObject(storableObject);
		this.insertEntity(eventSource);
	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

}
