/*
 * $Id: EventSourceDatabase.java,v 1.27.4.1 2006/03/28 09:52:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.event.EventSourceWrapper.COLUMN_EQUIPMENT_ID;
import static com.syrus.AMFICOM.event.EventSourceWrapper.COLUMN_LINK_ID;
import static com.syrus.AMFICOM.event.EventSourceWrapper.COLUMN_MCM_ID;
import static com.syrus.AMFICOM.event.EventSourceWrapper.COLUMN_PORT_ID;
import static com.syrus.AMFICOM.event.EventSourceWrapper.COLUMN_SOURCE_ENTITY_CODE;
import static com.syrus.AMFICOM.event.EventSourceWrapper.COLUMN_TRANSMISSION_PATH_ID;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENTSOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.27.4.1 $, $Date: 2006/03/28 09:52:57 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventSourceDatabase extends StorableObjectDatabase<EventSource> {

	private static String columns;

	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return EVENTSOURCE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_SOURCE_ENTITY_CODE + COMMA
				+ COLUMN_MCM_ID + COMMA
				+ COLUMN_PORT_ID + COMMA
				+ COLUMN_EQUIPMENT_ID + COMMA
				+ COLUMN_TRANSMISSION_PATH_ID + COMMA
				+ COLUMN_LINK_ID;
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
			case MCM_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				break;
			case PORT_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				break;
			case EQUIPMENT_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				break;
			case TRANSMISSIONPATH_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, sourceEntityId);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				break;
			case LINK_CODE:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
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
			case MCM_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				break;
			case PORT_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				break;
			case EQUIPMENT_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				break;
			case TRANSMISSIONPATH_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(sourceEntityId));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				break;
			case LINK_CODE:
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
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
				? new EventSource(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null)
					: storableObject;
		final short sourceEntityCode = resultSet.getShort(COLUMN_SOURCE_ENTITY_CODE);
		Identifier sourceEntityId = null;
		switch (sourceEntityCode) {
			case MCM_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MCM_ID);
				break;
			case PORT_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PORT_ID);
				break;
			case EQUIPMENT_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_EQUIPMENT_ID);
				break;
			case TRANSMISSIONPATH_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TRANSMISSION_PATH_ID);
				break;
			case LINK_CODE:
				sourceEntityId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_LINK_ID);
				break;
			default:
				throw new IllegalDataException("Event source for entity code " + sourceEntityCode
						+ ", entity '" + ObjectEntities.codeToString(sourceEntityCode) + "' not implemented");
		}
		eventSource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				 StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				 sourceEntityId);

		return eventSource;
	}

}
