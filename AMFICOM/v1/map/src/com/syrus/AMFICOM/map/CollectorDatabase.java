/*
 * $Id: CollectorDatabase.java,v 1.20 2005/03/09 14:49:53 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.20 $, $Date: 2005/03/09 14:49:53 $
 * @author $Author: bass $
 * @module map_v1
 */
public class CollectorDatabase extends CharacterizableDatabase {
	 private static String columns;
	
	private static String updateMultipleSQLValues;
	
	private static final String COLLECTOR_PHYSICAL_LINK = "CollPhLink";
	
	 private Collector fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Collector)
			return (Collector) storableObject;
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Collector collector = this.fromStorableObject(storableObject);
		this.retrieveEntity(collector);
		this.retrievePhysicalLinks(Collections.singletonList(collector));
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		collector.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(collector.getId(), CharacteristicSort.CHARACTERISTIC_SORT_COLLECTOR));
	}
	
	private void retrievePhysicalLinks(Collection collectors) throws RetrieveObjectException, IllegalDataException{
		if (collectors == null || collectors.isEmpty())
			return;
		java.util.Map map = super.retrieveLinkedEntityIds(collectors, COLLECTOR_PHYSICAL_LINK, CollectorWrapper.LINK_COLUMN_COLLECTOR_ID, CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Collector collector = (Collector) it.next();
			Collection physicalLinkIds = (Collection)map.get(collector);
			try {
				collector.setPhysicalLinks0(MapStorableObjectPool.getStorableObjects(physicalLinkIds, true));
			} catch (ApplicationException e) {
				throw new RetrieveObjectException(e);
			} 
		}
	}
	
	protected String getEnityName() {		
		return ObjectEntities.COLLECTOR_ENTITY;
	}	
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return super.getColumns(mode) + columns;
	}	
	
	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		Collector collector = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseString.setString(preparedStatement, ++i, collector.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, collector.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Collector collector = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(collector.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(collector.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		Collector collector = (storableObject == null) ? 
				new Collector(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null) : 
					fromStorableObject(storableObject);				
		
		collector.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));		
		return collector;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Collector collector = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Collector collector = this.fromStorableObject(storableObject);
		this.insertEntity(collector);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(collector);
			this.updatePhysicalLinks(Collections.singletonList(storableObject));
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
		
	}
	
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
			this.updatePhysicalLinks(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				characteristicDatabase.updateCharacteristics(storableObject);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				characteristicDatabase.updateCharacteristics(storableObject);
				return;
		}
		this.updatePhysicalLinks(Collections.singletonList(storableObject));
	}
	
	
	public void update(Collection storableObjects, Identifier modifierId, int updateKind) throws 
		VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				characteristicDatabase.updateCharacteristics(storableObjects);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);		
				characteristicDatabase.updateCharacteristics(storableObjects);
				return;
		}
		this.updatePhysicalLinks(storableObjects);
	}	
	
	private void updatePhysicalLinks(Collection collectors) throws UpdateObjectException {
		if (collectors == null || collectors.isEmpty())
			return;
		java.util.Map collectorIdPhysicalLinkIdsMap = new HashMap();
		for (Iterator it = collectors.iterator(); it.hasNext();) {
			Collector collector;
			try {
				collector = this.fromStorableObject((StorableObject)it.next());
			} catch (IllegalDataException e) {
				throw new UpdateObjectException("CollectorDatabase.updatePhysicalLinks | cannot get collector ", e);
			}
			Collection physicalLinks = collector.getPhysicalLinks();
			Collection physicalLinkIds = new ArrayList(physicalLinks.size());
			for (Iterator iter = physicalLinks.iterator(); iter.hasNext();) {
				PhysicalLink physicalLink = (PhysicalLink) iter.next();
				physicalLinkIds.add(physicalLink.getId());
			}
			collectorIdPhysicalLinkIdsMap.put(collector.getId(), physicalLinkIds);
		}		
		
		try {
			super.updateLinkedEntities(collectorIdPhysicalLinkIdsMap, COLLECTOR_PHYSICAL_LINK, CollectorWrapper.LINK_COLUMN_COLLECTOR_ID, CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
		} catch (IllegalDataException e) {
			throw new UpdateObjectException("CollectorDatabase.updatePhysicalLinks | cannot update physical links ", e);
		}
	}	
	
	public void delete(Identifier id) throws IllegalDataException {
		this.delete(Collections.singletonList(id));
	}
	
	public void delete(Collection ids) throws IllegalDataException {	
		StringBuffer linkBuffer = new StringBuffer(CollectorWrapper.LINK_COLUMN_COLLECTOR_ID);
		StringBuffer buffer = new StringBuffer(StorableObjectWrapper.COLUMN_ID);
		
		linkBuffer.append(SQL_IN);
		linkBuffer.append(OPEN_BRACKET);
		
		buffer.append(SQL_IN);
		buffer.append(OPEN_BRACKET);
		
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();

			linkBuffer.append(DatabaseIdentifier.toSQLString(id));
			buffer.append(DatabaseIdentifier.toSQLString(id));
			if (it.hasNext()) {
				if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0)){
					linkBuffer.append(COMMA);
					buffer.append(COMMA);
				}
				else {
					linkBuffer.append(CLOSE_BRACKET);
					linkBuffer.append(SQL_AND);
					linkBuffer.append(CollectorWrapper.LINK_COLUMN_COLLECTOR_ID);				
					linkBuffer.append(SQL_IN);
					linkBuffer.append(OPEN_BRACKET);
					
					buffer.append(CLOSE_BRACKET);
					buffer.append(SQL_AND);
					buffer.append(StorableObjectWrapper.COLUMN_ID);				
					buffer.append(SQL_IN);
					buffer.append(OPEN_BRACKET);
				}
			}
		
		}
		
		linkBuffer.append(CLOSE_BRACKET);
		buffer.append(CLOSE_BRACKET);
		
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + COLLECTOR_PHYSICAL_LINK + SQL_WHERE
					+ linkBuffer.toString());
			statement.executeUpdate(SQL_DELETE_FROM + this.getEnityName() + SQL_WHERE + StorableObjectWrapper.COLUMN_ID
					+ buffer.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		
		super.delete(ids);
	}	

	public void delete(StorableObject storableObject) throws IllegalDataException {
		Collector collector = this.fromStorableObject(storableObject);		
		this.delete(Collections.singletonList(collector.getId()));		
	}

	public Collection retrieveByIds(Collection ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		Collection collectors;
		if ((ids == null) || (ids.isEmpty()))
			collectors = retrieveByIdsOneQuery(null, conditions);
		else 
			collectors = retrieveByIdsOneQuery(ids, conditions);
		this.retrievePhysicalLinks(collectors);
		

        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
        Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(collectors, CharacteristicSort.CHARACTERISTIC_SORT_COLLECTOR);
        for (Iterator iter = collectors.iterator(); iter.hasNext();) {
            Collector collector = (Collector) iter.next();
            Collection characteristics = (Collection)characteristicMap.get(collector);
            collector.setCharacteristics0(characteristics);
        }
		
		return collectors;
		//return retriveByIdsPreparedStatement(ids, conditions);
	}	
	
}


