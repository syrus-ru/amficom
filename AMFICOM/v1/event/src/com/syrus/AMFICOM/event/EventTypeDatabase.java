/*
 * $Id: EventTypeDatabase.java,v 1.1 2004/12/29 15:26:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/29 15:26:31 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventTypeDatabase extends StorableObjectDatabase {

	public static final String	COLUMN_CODENAME = "codename";
	public static final String	COLUMN_DESCRIPTION = "description";	

	private static String columns;
	private static String updateMultiplySQLValues;

	private EventType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EventType)
			return (EventType) storableObject;
		throw new IllegalDataException("EventTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.EVENTTYPE_ENTITY;
	}	

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ COLUMN_CODENAME + COMMA 
				+ COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA 
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		EventType eventType = this.fromStorableObject(storableObject);		
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(eventType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(eventType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return sql;
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EventType eventType = this.fromStorableObject(storableObject);
		super.retrieveEntity(eventType);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws IllegalDataException, RetrieveObjectException, SQLException{
		EventType eventType = storableObject == null ? 
				new EventType(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null) : 
					this.fromStorableObject(storableObject);
		eventType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
								   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
								   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
								   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
								   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));
		return eventType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		EventType eventType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		EventType eventType = this.fromStorableObject(storableObject);
		this.insertEntity(eventType);
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
//		EventType eventType = this.fromStorableObject(storableObject);
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

	public EventType retrieveForCodename(String codename) throws ObjectNotFoundException, RetrieveObjectException {
		List list = null;
		try {
			list = this.retrieveByIds(null, COLUMN_CODENAME + EQUALS + APOSTOPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN) + APOSTOPHE);
		}
		catch (IllegalDataException ide) {				
			throw new RetrieveObjectException(ide);
		}

		if ((list == null) || (list.isEmpty()))
			throw new ObjectNotFoundException("No event  type with codename: '" + codename + "'");

		return (EventType) list.get(0);
	}

	public List retrieveAll() throws RetrieveObjectException {
		try {
			return this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);

		return list;		
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		EventType eventType = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseString.setString(preparedStatement, ++i, eventType.getCodename(), SIZE_CODENAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, eventType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(this.getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException, IllegalDataException {
		throw new UnsupportedOperationException("Not implemented");
	}
}
