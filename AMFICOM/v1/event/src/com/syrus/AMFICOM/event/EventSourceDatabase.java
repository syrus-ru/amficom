/*
 * $Id: EventSourceDatabase.java,v 1.27 2005/12/02 11:24:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.27 $, $Date: 2005/12/02 11:24:21 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventSourceDatabase extends StorableObjectDatabase<EventSource> {

	private static String columns;

	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.EVENTSOURCE_CODE;
	}

	@Override
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

	@Override
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

	@Override
	protected int setEntityForPreparedStatementTmpl(final EventSource storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Identifier sourceEntityId = storableObject.getSourceEntityId();
		final short sourceEntityCode = sourceEntityId.getMajor();
		preparedStatement.setShort(++startParameterNumber, sourceEntityCode);
		switch (sourceEntityCode) {
			case ObjectEntities.MCM_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				break;
			case ObjectEntities.PORT_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				break;
			case ObjectEntities.EQUIPMENT_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				break;
			case ObjectEntities.TRANSMISSIONPATH_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				break;
			case ObjectEntities.LINK_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				break;
			default:
				throw new IllegalDataException("Event source for entity code " + sourceEntityCode
						+ ", entity '" + ObjectEntities.codeToString(sourceEntityCode) + "' not implemented");
		}
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final EventSource storableObject) throws IllegalDataException {
		final Identifier sourceEntityId = storableObject.getSourceEntityId();
		final short sourceEntityCode = sourceEntityId.getMajor();
		final StringBuffer buffer = new StringBuffer(Short.toString(sourceEntityCode));
		buffer.append(COMMA);
		switch (sourceEntityCode) {
			case ObjectEntities.MCM_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				break;
			case ObjectEntities.PORT_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				break;
			case ObjectEntities.EQUIPMENT_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				break;
			case ObjectEntities.TRANSMISSIONPATH_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				break;
			case ObjectEntities.LINK_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				break;
			default:
				throw new IllegalDataException("Event source for entity code " + sourceEntityCode
						+ ", entity '" + ObjectEntities.codeToString(sourceEntityCode) + "' not implemented");
		}
		return buffer.toString();
	}

	@Override
	protected EventSource updateEntityFromResultSet(final EventSource storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final EventSource eventSource = (storableObject == null)
				? new EventSource(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null)
					: storableObject;
		final short sourceEntityCode = resultSet.getShort(EventSourceWrapper.COLUMN_SOURCE_ENTITY_CODE);
		Identifier sourceEntityId = null;
		switch (sourceEntityCode) {
			case ObjectEntities.MCM_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_MCM_ID);
				break;
			case ObjectEntities.PORT_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_PORT_ID);
				break;
			case ObjectEntities.EQUIPMENT_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_EQUIPMENT_ID);
				break;
			case ObjectEntities.TRANSMISSIONPATH_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_TRANSMISSION_PATH_ID);
				break;
			case ObjectEntities.LINK_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, EventSourceWrapper.COLUMN_LINK_ID);
				break;
			default:
				throw new IllegalDataException("Event source for entity code " + sourceEntityCode
						+ ", entity '" + ObjectEntities.codeToString(sourceEntityCode) + "' not implemented");
		}
		eventSource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				 StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				 sourceEntityId);

		return eventSource;
	}

}
