/*
 * $Id: EventParameterDatabase.java,v 1.1 2005/02/28 15:31:36 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.syrus.AMFICOM.event.corba.EventParameter_TransferablePackage.EventParameterSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/28 15:31:36 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventParameterDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultiplySQLValues;

	private EventParameter fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EventParameter)
			return (EventParameter) storableObject;
		throw new IllegalDataException("EventParameterDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.EVENTPARAMETER_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ EventParameterWrapper.COLUMN_EVENT_ID + COMMA
				+ EventParameterWrapper.COLUMN_SORT + COMMA
				+ EventParameterWrapper.COLUMN_VALUE_NUMBER + COMMA
				+ EventParameterWrapper.COLUMN_VALUE_STRING + COMMA
				+ EventParameterWrapper.COLUMN_VALUE_RAW;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ SQL_FUNCTION_EMPTY_BLOB;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		EventParameter eventParameter = this.fromStorableObject(storableObject);
		int sort = eventParameter.getSort().value();
		StringBuffer values = new StringBuffer(super.getUpdateSingleSQLValues(storableObject) + COMMA
				+ DatabaseIdentifier.toSQLString(eventParameter.getType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(eventParameter.getEventId()) + COMMA
				+ Integer.toString(sort) + COMMA);
		switch (sort) {
			case EventParameterSort._PARAMETER_SORT_NUMBER:
				values.append(Integer.toString(eventParameter.getValueNumber()));
				values.append(COMMA);
				values.append("");
				break;
			case EventParameterSort._PARAMETER_SORT_STRING:
				values.append("0");
				values.append(COMMA);
				values.append(eventParameter.getValueString());
				break;
			case EventParameterSort._PARAMETER_SORT_RAW:
				values.append("0");
				values.append(COMMA);
				values.append("");
				break;
			default:
				throw new IllegalDataException("Illegal sort: " + sort + " of event parameter '" + eventParameter.getId()
						+ "' of event '" + eventParameter.getEventId() + "'");
		}
		values.append(COMMA);
		values.append(SQL_FUNCTION_EMPTY_BLOB);

		return values.toString();
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		EventParameter eventParameter = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, eventParameter.getType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, eventParameter.getEventId());
		int sort = eventParameter.getSort().value();
		preparedStatement.setInt(++i, sort);
		switch (sort) {
			case EventParameterSort._PARAMETER_SORT_NUMBER:
				preparedStatement.setInt(++i, eventParameter.getValueNumber());
				preparedStatement.setString(++i, "");
				break;
			case EventParameterSort._PARAMETER_SORT_STRING:
				preparedStatement.setInt(++i, 0);
				preparedStatement.setString(++i, eventParameter.getValueString());
				break;
			case EventParameterSort._PARAMETER_SORT_RAW:
				preparedStatement.setInt(++i, 0);
				preparedStatement.setString(++i, "");
				break;
			default:
				throw new IllegalDataException("Illegal sort: " + sort + " of event parameter '" + eventParameter.getId()
						+ "' of event '" + eventParameter.getEventId() + "'");
		}

		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		EventParameter eventParameter = (storableObject == null) ? new EventParameter(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, 0) : this.fromStorableObject(storableObject);

		Date created = DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED);
		Date modified = DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED);
		Identifier creatorId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID);
		Identifier modifierId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID);
		long version = resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION);

		ParameterType parameterType;
		try {
			parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		Identifier eventId = DatabaseIdentifier.getIdentifier(resultSet, EventParameterWrapper.COLUMN_EVENT_ID);
		int sort = resultSet.getInt(EventParameterWrapper.COLUMN_SORT);
		switch (sort) {
			case EventParameterSort._PARAMETER_SORT_NUMBER:
				eventParameter.setAttributes(created,
						modified,
						creatorId,
						modifierId,
						version,
						parameterType,
						eventId,
						resultSet.getInt(EventParameterWrapper.COLUMN_VALUE_NUMBER));
				break;
			case EventParameterSort._PARAMETER_SORT_STRING:
				eventParameter.setAttributes(created,
						modified,
						creatorId,
						modifierId,
					  version,
					  parameterType,
					  eventId,
					  resultSet.getString(EventParameterWrapper.COLUMN_VALUE_STRING));
				break;
			case EventParameterSort._PARAMETER_SORT_RAW:
				eventParameter.setAttributes(created,
						modified,
						creatorId,
						modifierId,
					  version,
					  parameterType,
					  eventId,
					  ByteArrayDatabase.toByteArray(resultSet.getBlob(EventParameterWrapper.COLUMN_VALUE_RAW)));
				break;
			default:
				Log.errorMessage("EventParameterDatabase.retrieveEventParameters | Unknown sort: " + sort
						+ " of parameter '" + eventParameter.getId() + "'");
		}
		return eventParameter;
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		super.retrieveEntity(storableObject);
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		Collection objects = null; 
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else
			objects = this.retrieveByIdsOneQuery(ids, condition);

		return objects;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		EventParameter eventParameter = this.fromStorableObject(storableObject);
		super.insertEntity(eventParameter);

		if (eventParameter.getSort().value() == EventParameterSort._PARAMETER_SORT_RAW)
			this.insertRawValue(eventParameter);
	}

	private void insertRawValue(EventParameter eventParameter) throws CreateObjectException {
		if (eventParameter.getSort().value() != EventParameterSort._PARAMETER_SORT_RAW) {
			Log.errorMessage("EventParameterDatabase.insertRawValue | Parameter '" + eventParameter.getId()
					+ "' of event '" + eventParameter.getEventId() + "' is not of sort RAW");
			return;
		}

		byte[] valueRaw = eventParameter.getValueRaw();
		try {
			ByteArrayDatabase.saveAsBlob(valueRaw,
					DatabaseConnection.getConnection(),
					this.getEnityName(),
					EventParameterWrapper.COLUMN_VALUE_RAW,
					StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(eventParameter.getId()));
		}
		catch (SQLException e) {
			throw new CreateObjectException("Cannot insert raw value of event parameter '" + eventParameter.getId()
					+ "' of event '" + eventParameter.getEventId() + "'");
		}
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;

		if (storableObjects.size() == 1) {
			this.insert((StorableObject) storableObjects.iterator().next());
			return;
		}

		String sql = SQL_INSERT_INTO + this.getEnityName() + OPEN_BRACKET
				+ this.getColumns(MODE_INSERT)
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ this.getInsertMultiplySQLValues()
				+ CLOSE_BRACKET;

		PreparedStatement preparedStatement = null;
		EventParameter eventParameter;
		String eventParameterIdStr = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				eventParameter = (EventParameter) it.next();
				eventParameterIdStr = eventParameter.getId().getIdentifierString();
				this.setEntityForPreparedStatement(eventParameter, preparedStatement, MODE_INSERT);
				Log.debugMessage("EventParameterDatabase.insert | Inserting event parameter '" + eventParameterIdStr
						+ "' of event '" + eventParameter.getId() + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();

				this.insertRawValue(eventParameter);
			}

			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Cannot insert event parameter '" + eventParameterIdStr + "' -- " + sqle.getMessage();
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

}
