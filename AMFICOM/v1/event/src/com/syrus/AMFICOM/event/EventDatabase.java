/*
 * $Id: EventDatabase.java,v 1.3 2004/12/29 15:26:31 arseniy Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.database.DatabaseString;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/29 15:26:31 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventDatabase extends StorableObjectDatabase {

	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_DESCRIPTION = "description";

	private static String columns;

	private static String updateMultiplySQLValues;

	private Event fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Event)
			return (Event) storableObject;
		throw new IllegalDataException("EventDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Event event = this.fromStorableObject(storableObject);
		this.retrieveEntity(event);
	}	

	protected String getEnityName() {		
		return ObjectEntities.EVENT_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_STATUS + COMMA
				+ COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Event event = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, event.getType().getId());
			preparedStatement.setInt(++i, event.getStatus().value());
			DatabaseString.setString(preparedStatement, ++i, event.getDescription(), SIZE_DESCRIPTION_COLUMN);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Event event = this.fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(event.getType().getId()) + COMMA
			+ Integer.toString(event.getStatus().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(event.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return values;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Event event = (storableObject == null) ? 
				new Event(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null) : 
					this.fromStorableObject(storableObject);
		EventType eventType;
		try {
			eventType = (EventType)EventStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID), true);			
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		event.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
								eventType,
								resultSet.getInt(COLUMN_STATUS),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));		
		return event;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Event event = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Event event = this.fromStorableObject(storableObject);
		this.insertEntity(event);
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Event event = this.fromStorableObject(storableObject);
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
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
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

	public List retrieveByIds(List ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retrieveByIdsOneQuery(null, conditions);
		return retrieveByIdsOneQuery(ids, conditions);	
		//return retriveByIdsPreparedStatement(ids, conditions);
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		throw new UnsupportedOperationException("Not implemented");
	}
}
