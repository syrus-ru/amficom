/*
 * $Id: MapDatabase.java,v 1.7 2004/12/22 09:51:06 bob Exp $
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

import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.7 $, $Date: 2004/12/22 09:51:06 $
 * @author $Author: bob $
 * @module map_v1
 */
public class MapDatabase extends StorableObjectDatabase {
	 // name VARCHAR2(128),
    public static final String COLUMN_NAME  		= "name";
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // domain_id VARCHAR2(32),
    public static final String COLUMN_DOMAIN_ID     = "domain_id";
    
    // linked tables :: 
    private static final String MAP_COLLECTOR 			= "MapCollector";    
    private static final String MAP_MARK 				= "MapMark";
	private static final String MAP_NODE_LINK 			= "MapNodeLink";
	private static final String MAP_PHYSICAL_LINK 		= "MapPhysicalLink";
	private static final String MAP_SITE_NODE 			=  "MapSiteNode";
	private static final String MAP_TOPOLOGICAL_NODE 	= "MapTopologicalNode";
	
    private static final int _MAP_COLLECTOR 			= 0;    
    private static final int _MAP_MARK 					= 1;
	private static final int _MAP_NODE_LINK 			= 2;
	private static final int _MAP_PHYSICAL_LINK 		= 3;
	private static final int _MAP_SITE_NODE 			= 4;
	private static final int _MAP_TOPOLOGICAL_NODE 		= 5;
	

    // map_id VARCHAR2(32),   
    private static final String LINK_COLUMN_MAP_ID					= "map_id";
    // collector_id VARCHAR2(32),
    private static final String LINK_COLUMN_COLLECTOR_ID			= "collector_id";
    // mark_id VARCHAR2(32),
    private static final String LINK_COLUMN_MARK_ID					= "mark_id";
    // node_link_id VARCHAR2(32),
    private static final String LINK_COLUMN_NODE_LINK_ID			= "node_link_id";
    // physical_link_id VARCHAR2(32),
    private static final String LINK_COLUMN_PHYSICAL_LINK_ID		= "physical_link_id";
    // site_node_id VARCHAR2(32),
    private static final String LINK_COLUMN_SITE_NODE_ID  			= "site_node_id";
    // topological_node_id VARCHAR2(32),
    private static final String LINK_COLUMN_TOPOLOGICAL_NODE_ID		= "topological_node_id";


	private static java.util.Map dbTableColumnName;
	
	static {
		dbTableColumnName = new HashMap();
		dbTableColumnName.put(MAP_COLLECTOR, LINK_COLUMN_COLLECTOR_ID);
		dbTableColumnName.put(MAP_MARK, LINK_COLUMN_MARK_ID);
		dbTableColumnName.put(MAP_NODE_LINK, LINK_COLUMN_NODE_LINK_ID);
		dbTableColumnName.put(MAP_PHYSICAL_LINK, LINK_COLUMN_PHYSICAL_LINK_ID);
		dbTableColumnName.put(MAP_SITE_NODE, LINK_COLUMN_SITE_NODE_ID);
		dbTableColumnName.put(MAP_TOPOLOGICAL_NODE, LINK_COLUMN_TOPOLOGICAL_NODE_ID);
	}


	private static String columns;
	
	private static String updateMultiplySQLValues;
	
	private String getLinkedTableName(int linkedTable) throws IllegalDataException{
		String tableName;
		switch(linkedTable){
			case _MAP_COLLECTOR:
				tableName = MAP_COLLECTOR;
				break;
			case _MAP_MARK:
				tableName = MAP_MARK;
				break;
			case _MAP_NODE_LINK:
				tableName = MAP_NODE_LINK;
				break;
			case _MAP_PHYSICAL_LINK:
				tableName = MAP_PHYSICAL_LINK;
				break;
			case _MAP_SITE_NODE:
				tableName = MAP_SITE_NODE;
				break;
			case _MAP_TOPOLOGICAL_NODE:
				tableName = MAP_TOPOLOGICAL_NODE;
				break;
			default:
				throw new IllegalDataException(this.getEnityName() + "Database.retrieveLinkedObjects | unknown linked table code:" + linkedTable);
		}
		return tableName;
	}

	private Map fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Map)
			return (Map) storableObject;
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Map map = this.fromStorableObject(storableObject);
		this.retrieveEntity(map);
		List maps = Collections.singletonList(map);
		
		{
			java.util.Map collectors = this.retrieveLinkedObjects(maps, _MAP_COLLECTOR);
			for (Iterator it = collectors.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				List collectorIds = (List)collectors.get(id);
				if (id.equals(map.getId())){
					try {
						map.setCollectors0(MapStorableObjectPool.getStorableObjects(collectorIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map marks = this.retrieveLinkedObjects(maps, _MAP_MARK);
			for (Iterator it = marks.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				List markIds = (List)marks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setMarks0(MapStorableObjectPool.getStorableObjects(markIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map nodeLinks = this.retrieveLinkedObjects(maps, _MAP_NODE_LINK);
			for (Iterator it = nodeLinks.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				List nodeLinkIds = (List)nodeLinks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setNodeLinks0(MapStorableObjectPool.getStorableObjects(nodeLinkIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map physicalLinks = this.retrieveLinkedObjects(maps, _MAP_PHYSICAL_LINK);
			for (Iterator it = physicalLinks.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				List physicalLinkIds = (List)physicalLinks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setPhysicalLinks0(MapStorableObjectPool.getStorableObjects(physicalLinkIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map siteNodes = this.retrieveLinkedObjects(maps, _MAP_SITE_NODE);
			for (Iterator it = siteNodes.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				List siteNodeIds = (List)siteNodes.get(id);
				if (id.equals(map.getId())){
					try {
						map.setSiteNodes0(MapStorableObjectPool.getStorableObjects(siteNodeIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map topologicalNodes = this.retrieveLinkedObjects(maps, _MAP_TOPOLOGICAL_NODE);
			for (Iterator it = topologicalNodes.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				List topologicalNodeIds = (List)topologicalNodes.get(id);
				if (id.equals(map.getId())){
					try {
						map.setTopologicalNodes0(MapStorableObjectPool.getStorableObjects(topologicalNodeIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
	}	
	
	private java.util.Map retrieveLinkedObjects(List maps, int linkedTable) throws RetrieveObjectException, IllegalDataException{
		if (maps == null || maps.isEmpty())
			return Collections.EMPTY_MAP;
		
		String tableName = this.getLinkedTableName(linkedTable);
		String columnName = (String)dbTableColumnName.get(tableName);
		
		return super.retrieveLinkedEntities(maps, tableName, LINK_COLUMN_MAP_ID, columnName);
	}
	
	protected String getEnityName() {		
		return ObjectEntities.MAP_ENTITY;
	}	
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_DOMAIN_ID;
		}
		return columns;
	}	
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Map map = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseString.setString(preparedStatement, ++i, map.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, map.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, map.getDomainId());		
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		Map map = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(map.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(map.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(map.getDomainId());
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		Map map = (storableObject == null) ? 
				new Map(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null) : 
					fromStorableObject(storableObject);				
		
		map.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_DOMAIN_ID));		
		return map;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Map map = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Map map = this.fromStorableObject(storableObject);
		super.insertEntity(map);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		List maps = Collections.singletonList(map);
		try {
			characteristicDatabase.updateCharacteristics(map);
			this.updateLinkedObjectIds(maps, _MAP_COLLECTOR);
			this.updateLinkedObjectIds(maps, _MAP_MARK);
			this.updateLinkedObjectIds(maps, _MAP_NODE_LINK);
			this.updateLinkedObjectIds(maps, _MAP_PHYSICAL_LINK);
			this.updateLinkedObjectIds(maps, _MAP_SITE_NODE);
			this.updateLinkedObjectIds(maps, _MAP_TOPOLOGICAL_NODE);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
			this.updateLinkedObjectIds(storableObjects, _MAP_COLLECTOR);
			this.updateLinkedObjectIds(storableObjects, _MAP_MARK);
			this.updateLinkedObjectIds(storableObjects, _MAP_NODE_LINK);
			this.updateLinkedObjectIds(storableObjects, _MAP_PHYSICAL_LINK);
			this.updateLinkedObjectIds(storableObjects, _MAP_SITE_NODE);
			this.updateLinkedObjectIds(storableObjects, _MAP_TOPOLOGICAL_NODE);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Map map = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(map, false);
				characteristicDatabase.updateCharacteristics(map);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(map, true);
				characteristicDatabase.updateCharacteristics(map);
				return;
		}
		List maps = Collections.singletonList(map);
		this.updateLinkedObjectIds(maps, _MAP_COLLECTOR);
		this.updateLinkedObjectIds(maps, _MAP_MARK);
		this.updateLinkedObjectIds(maps, _MAP_NODE_LINK);
		this.updateLinkedObjectIds(maps, _MAP_PHYSICAL_LINK);
		this.updateLinkedObjectIds(maps, _MAP_SITE_NODE);
		this.updateLinkedObjectIds(maps, _MAP_TOPOLOGICAL_NODE);
	}
	
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
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
		this.updateLinkedObjectIds(storableObjects, _MAP_COLLECTOR);
		this.updateLinkedObjectIds(storableObjects, _MAP_MARK);
		this.updateLinkedObjectIds(storableObjects, _MAP_NODE_LINK);
		this.updateLinkedObjectIds(storableObjects, _MAP_PHYSICAL_LINK);
		this.updateLinkedObjectIds(storableObjects, _MAP_SITE_NODE);
		this.updateLinkedObjectIds(storableObjects, _MAP_TOPOLOGICAL_NODE);
	}	
	
	private void updateLinkedObjectIds(List maps, int linkedTable) throws UpdateObjectException, IllegalDataException {
		if (maps == null || maps.isEmpty())
			return;

		String tableName = getLinkedTableName(linkedTable);
		String columnName = (String) dbTableColumnName.get(tableName);
		
		java.util.Map mapIdLinkedObjectIds = new HashMap();
		
		for (Iterator colIter = maps.iterator(); colIter.hasNext();) {
			StorableObject storableObject = (StorableObject) colIter.next();
	        Map map = fromStorableObject(storableObject);
	        List linkedObjectList ;
	        switch(linkedTable){
				case _MAP_COLLECTOR:
					linkedObjectList = map.getCollectors();
					break;
				case _MAP_MARK:
					linkedObjectList = map.getMarks();
					break;
				case _MAP_NODE_LINK:
					linkedObjectList = map.getNodeLinks();
					break;
				case _MAP_PHYSICAL_LINK:
					linkedObjectList = map.getPhysicalLinks();
					break;
				case _MAP_SITE_NODE:
					linkedObjectList = map.getSiteNodes();
					break;
				case _MAP_TOPOLOGICAL_NODE:
					linkedObjectList = map.getTopologicalNodes();
					break;
				default:
					throw new IllegalDataException(this.getEnityName() + "Database.updateLinkedObjectIds | unknown linked table code:" + linkedTable);
			}
	
	        List linkedObjectIds = new ArrayList(linkedObjectList.size());
	        for (Iterator it = linkedObjectList.iterator(); it.hasNext();) {
	        	StorableObject storableObject2 = (StorableObject) it.next();
	            linkedObjectIds.add(storableObject2.getId());
			}
	        
	        mapIdLinkedObjectIds.put(map.getId(), linkedObjectIds);
		}
		
		super.updateLinkedEntities(mapIdLinkedObjectIds, tableName, LINK_COLUMN_MAP_ID, columnName);

	}
	
	public void delete(Identifier id) throws IllegalDataException {
		this.delete(Collections.singletonList(id));
	}
	
	public void delete(List ids) throws IllegalDataException {
		super.delete(ids);
		
		java.util.Map linkedObjectIds = new HashMap();
		
		java.util.Map mapIds = new HashMap();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			try{
				Map map = (Map)MapStorableObjectPool.getStorableObject(mapId, true);
				mapIds.put(mapId, map);
			}catch(ApplicationException ae){
				throw new IllegalDataException(this.getEnityName()+"Database.delete | Couldn't found map for " + mapId);
			} 
		}
		
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			Map map = (Map) mapIds.get(mapId);
			linkedObjectIds.put(map, map.getCollectors());
		}
		
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_COLLECTOR);
		
		linkedObjectIds.clear();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			Map map = (Map) mapIds.get(mapId);
			linkedObjectIds.put(map, map.getMarks());
		}
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_MARK);
		
		linkedObjectIds.clear();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			Map map = (Map) mapIds.get(mapId);
			linkedObjectIds.put(map, map.getNodeLinks());
		}		
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_NODE_LINK);
		
		linkedObjectIds.clear();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			Map map = (Map) mapIds.get(mapId);
			linkedObjectIds.put(map, map.getPhysicalLinks());
		}			
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_PHYSICAL_LINK);
		
		linkedObjectIds.clear();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			Map map = (Map) mapIds.get(mapId);
			linkedObjectIds.put(map, map.getSiteNodes());
		}		
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_SITE_NODE);
		
		linkedObjectIds.clear();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			Map map = (Map) mapIds.get(mapId);
			linkedObjectIds.put(map, map.getTopologicalNodes());
		}		
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_TOPOLOGICAL_NODE);
	}	

	public void delete(StorableObject storableObject) throws IllegalDataException {
		Map map = fromStorableObject(storableObject);
		this.delete(Collections.singletonList(map.getId()));
	}
	
	private void deleteLinkedObjectIds(java.util.Map linkedObjectIds, int linkedTable) throws IllegalDataException {	
		String tableName = this.getLinkedTableName(linkedTable);
		String columnName = (String)dbTableColumnName.get(tableName);

		StringBuffer linkBuffer = new StringBuffer(columnName);
		
		linkBuffer.append(SQL_IN);
		linkBuffer.append(OPEN_BRACKET);
		
		int i = 0;
		for (Iterator colIter = linkedObjectIds.keySet().iterator(); colIter.hasNext();) {
			Identifier collectorId = (Identifier) colIter.next();
			List physicalLinkIds = (List)linkedObjectIds.get(collectorId);
			for (Iterator it = physicalLinkIds.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
	
				linkBuffer.append(DatabaseIdentifier.toSQLString(id));
				if (it.hasNext()) {
					if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0)){
						linkBuffer.append(COMMA);
					}
					else {
						linkBuffer.append(CLOSE_BRACKET);
						linkBuffer.append(SQL_AND);
						linkBuffer.append(columnName);				
						linkBuffer.append(SQL_IN);
						linkBuffer.append(OPEN_BRACKET);
					}
				}
			
			}
		}
		
		linkBuffer.append(CLOSE_BRACKET);
		
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + tableName + SQL_WHERE
					+ linkBuffer.toString());
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
	}

	public List retrieveByIds(List ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		List maps;
		if ((ids == null) || (ids.isEmpty()))
			maps = retrieveByIdsOneQuery(null, conditions);
		else maps = retrieveByIdsOneQuery(ids, conditions);	
		
		java.util.Map mapIds = new HashMap();
		for (Iterator it = maps.iterator(); it.hasNext();) {
			Map map = (Map) it.next();
			mapIds.put(map.getId(), map);
		}
		
		{
			java.util.Map collectors = this.retrieveLinkedObjects(maps, _MAP_COLLECTOR);
			for (Iterator it = collectors.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Map map = (Map) mapIds.get(id);
				List collectorIds = (List)collectors.get(id);				
				if (id.equals(map.getId())){
					try {
						map.setCollectors0(MapStorableObjectPool.getStorableObjects(collectorIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map marks = this.retrieveLinkedObjects(maps, _MAP_MARK);
			for (Iterator it = marks.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Map map = (Map) mapIds.get(id);
				List markIds = (List)marks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setMarks0(MapStorableObjectPool.getStorableObjects(markIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map nodeLinks = this.retrieveLinkedObjects(maps, _MAP_NODE_LINK);
			for (Iterator it = nodeLinks.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Map map = (Map) mapIds.get(id);
				List nodeLinkIds = (List)nodeLinks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setNodeLinks0(MapStorableObjectPool.getStorableObjects(nodeLinkIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map physicalLinks = this.retrieveLinkedObjects(maps, _MAP_PHYSICAL_LINK);
			for (Iterator it = physicalLinks.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Map map = (Map) mapIds.get(id);
				List physicalLinkIds = (List)physicalLinks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setPhysicalLinks0(MapStorableObjectPool.getStorableObjects(physicalLinkIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map siteNodes = this.retrieveLinkedObjects(maps, _MAP_SITE_NODE);
			for (Iterator it = siteNodes.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Map map = (Map) mapIds.get(id);
				List siteNodeIds = (List)siteNodes.get(id);
				if (id.equals(map.getId())){
					try {
						map.setSiteNodes0(MapStorableObjectPool.getStorableObjects(siteNodeIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map topologicalNodes = this.retrieveLinkedObjects(maps, _MAP_TOPOLOGICAL_NODE);
			for (Iterator it = topologicalNodes.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Map map = (Map) mapIds.get(id);
				List topologicalNodeIds = (List)topologicalNodes.get(id);
				if (id.equals(map.getId())){
					try {
						map.setTopologicalNodes0(MapStorableObjectPool.getStorableObjects(topologicalNodeIds, true));
					} catch (DatabaseException e) {
						throw new RetrieveObjectException(e);
					} catch (CommunicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		return maps;
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
