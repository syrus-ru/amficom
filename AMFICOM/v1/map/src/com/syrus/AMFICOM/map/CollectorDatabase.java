/*
 * $Id: CollectorDatabase.java,v 1.12 2005/02/04 06:44:07 bob Exp $
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.12 $, $Date: 2005/02/04 06:44:07 $
 * @author $Author: bob $
 * @module map_v1
 */
public class CollectorDatabase extends StorableObjectDatabase {
	 private static String columns;
	
	private static String updateMultiplySQLValues;
	
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
	
	private void retrievePhysicalLinks(List collectors) throws RetrieveObjectException, IllegalDataException{
		if (collectors == null || collectors.isEmpty())
			return;
		java.util.Map map = super.retrieveLinkedEntityIds(collectors, COLLECTOR_PHYSICAL_LINK, CollectorWrapper.LINK_COLUMN_COLLECTOR_ID, CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Collector collector = (Collector) it.next();
			List physicalLinkIds = (List)map.get(collector);
			try {
				collector.setPhysicalLinks0(MapStorableObjectPool.getStorableObjects(physicalLinkIds, true));
			} catch (DatabaseException e) {
				throw new RetrieveObjectException(e);
			} catch (CommunicationException e) {
				throw new RetrieveObjectException(e);
			}
		}
	}
	
	protected String getEnityName() {		
		return ObjectEntities.COLLECTOR_ENTITY;
	}	
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}	
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Collector collector = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseString.setString(preparedStatement, ++i, collector.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, collector.getDescription(), SIZE_DESCRIPTION_COLUMN);
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		Collector collector = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(collector.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(collector.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		Collector collector = (storableObject == null) ? 
				new Collector(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, null, null) : 
					fromStorableObject(storableObject);				
		
		collector.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
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
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
			this.updatePhysicalLinks(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Collector collector = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(collector, false);
				characteristicDatabase.updateCharacteristics(collector);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(collector, true);
				characteristicDatabase.updateCharacteristics(collector);
				return;
		}
		this.updatePhysicalLinks(Collections.singletonList(storableObject));
	}
	
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				characteristicDatabase.updateCharacteristics(storableObjects);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				characteristicDatabase.updateCharacteristics(storableObjects);
				return;
		}
		this.updatePhysicalLinks(storableObjects);
	}	
	
	private void updatePhysicalLinks(List collectors) throws UpdateObjectException, IllegalDataException {
		if (collectors == null || collectors.isEmpty())
			return;
		java.util.Map collectorIdPhysicalLinkIdsMap = new HashMap();
		for (Iterator it = collectors.iterator(); it.hasNext();) {
			Collector collector = this.fromStorableObject((StorableObject)it.next());
			List physicalLinks = collector.getPhysicalLinks();
			List physicalLinkIds = new ArrayList(physicalLinks.size());
			for (Iterator iter = physicalLinks.iterator(); iter.hasNext();) {
				PhysicalLink physicalLink = (PhysicalLink) iter.next();
				physicalLinkIds.add(physicalLink.getId());
			}
			collectorIdPhysicalLinkIdsMap.put(collector.getId(), physicalLinkIds);
		}		
		super.updateLinkedEntities(collectorIdPhysicalLinkIdsMap, COLLECTOR_PHYSICAL_LINK, CollectorWrapper.LINK_COLUMN_COLLECTOR_ID, CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
	}	
	
	public void delete(Identifier id) throws IllegalDataException {
		this.delete(Collections.singletonList(id));
	}
	
	public void delete(List ids) throws IllegalDataException {	
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

	public List retrieveByIds(List ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		List collectors;
		if ((ids == null) || (ids.isEmpty()))
			collectors = retrieveByIdsOneQuery(null, conditions);
		else 
			collectors = retrieveByIdsOneQuery(ids, conditions);
		this.retrievePhysicalLinks(collectors);
		

        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
        Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(collectors, CharacteristicSort.CHARACTERISTIC_SORT_COLLECTOR);
        for (Iterator iter = collectors.iterator(); iter.hasNext();) {
            Collector collector = (Collector) iter.next();
            List characteristics = (List)characteristicMap.get(collector);
            collector.setCharacteristics0(characteristics);
        }
		
		return collectors;
		//return retriveByIdsPreparedStatement(ids, conditions);
	}	
	
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		{
			Log.errorMessage(getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}


