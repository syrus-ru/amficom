/*
 * $Id: MapDatabase.java,v 1.3 2004/12/06 17:36:54 bob Exp $
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
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identified;
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
 * @version $Revision: 1.3 $, $Date: 2004/12/06 17:36:54 $
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
		String sql;

		String tableName = this.getLinkedTableName(linkedTable);
		String columnName = (String)dbTableColumnName.get(tableName);
		
		
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(LINK_COLUMN_COLLECTOR_ID);
			buffer.append(COMMA);
			buffer.append(LINK_COLUMN_PHYSICAL_LINK_ID); 
			buffer.append(SQL_FROM);
			

			buffer.append(tableName);
			buffer.append(SQL_WHERE);
			buffer.append(LINK_COLUMN_MAP_ID);
			buffer.append(SQL_IN);
			buffer.append(OPEN_BRACKET);
			
			int i = 1;
			for (Iterator it = maps.iterator(); it.hasNext();i++) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier) object;
				else if (object instanceof Identified)
					id = ((Identified)object).getId();
				else throw new IllegalDataException(this.getEnityName() + "Database.retrieveLinkedObjects | Object " +
													object.getClass().getName()
													+ " isn't Identifier or Identified");

				if (id != null){
					buffer.append(DatabaseIdentifier.toSQLString(id));
					if (it.hasNext()) {
						if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0)){
							buffer.append(COMMA);
						}
						else {
							buffer.append(CLOSE_BRACKET);
							buffer.append(SQL_OR);
							buffer.append(LINK_COLUMN_COLLECTOR_ID);				
							buffer.append(SQL_IN);
							buffer.append(OPEN_BRACKET);
						}
					}
				}
			}
			buffer.append(CLOSE_BRACKET);
			
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.retrieveLinkedObjects | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);

			java.util.Map linkedObjMap = new HashMap();
			while (resultSet.next()){	
				Identifier dbLinkedObjId = DatabaseIdentifier.getIdentifier(resultSet, columnName);
				
				List physicalLinkIds = (List)linkedObjMap.get(dbLinkedObjId);
				if (physicalLinkIds == null){
					physicalLinkIds = new LinkedList();
					linkedObjMap.put(dbLinkedObjId, physicalLinkIds);
				}
				physicalLinkIds.add(DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PHYSICAL_LINK_ID));
			}
			
			return linkedObjMap;
			
		} catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.retrieveLinkedObjects | Cannot retrieve linked objects -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	protected String getEnityName() {		
		return ObjectEntities.MAP_ENTITY;
	}	
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_DOMAIN_ID;
		}
		return columns;
	}	
	
	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		Map map = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(map.getName()));
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(map.getDescription()));
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
			+ APOSTOPHE + DatabaseString.toQuerySubString(map.getName()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(map.getDescription()) + APOSTOPHE + COMMA
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
//		Collector collector = this.fromStorableObject(storableObject);
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
        StringBuffer buffer = new StringBuffer(SQL_SELECT);
        buffer.append(LINK_COLUMN_MAP_ID);
        buffer.append(COMMA);
        buffer.append(columnName);
        buffer.append(SQL_FROM);
        buffer.append(tableName);
        buffer.append(SQL_WHERE);
        buffer.append(LINK_COLUMN_MAP_ID);
        buffer.append(SQL_IN);
        buffer.append(OPEN_BRACKET);

        int i = 0;
		for (Iterator colIter = maps.iterator(); colIter.hasNext();i++) {
	        Map map = fromStorableObject((StorableObject)colIter.next());
	        buffer.append(map.getId());
	        if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
				buffer.append(COMMA);
			else {
				buffer.append(CLOSE_BRACKET);
				buffer.append(SQL_OR);
				buffer.append(LINK_COLUMN_MAP_ID);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
			}
		}
		buffer.append(CLOSE_BRACKET);
		
		java.util.Map dbPhysicalLinkIdsMap = new HashMap();
		
        String sql = buffer.toString();
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = DatabaseConnection.getConnection();
        try {
            statement = connection.createStatement();
            Log.debugMessage(this.getEnityName() + "Database.updateLinkedObjectIds | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
            	Identifier mapId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MAP_ID);
            	Identifier linkedObjectId = DatabaseIdentifier.getIdentifier(resultSet, columnName);
            	List linkedObjectIdsList = (List) dbPhysicalLinkIdsMap.get(mapId);
            	if (linkedObjectIdsList == null){
            		linkedObjectIdsList = new LinkedList();
            		dbPhysicalLinkIdsMap.put(mapId, linkedObjectIdsList);
            	}
            	linkedObjectIdsList.add(linkedObjectId);
            }	
        } catch (SQLException sqle) {
            String mesg = this.getEnityName() + "Database.updateLinkedObjectIds | SQLException: " + sqle.getMessage();
            throw new UpdateObjectException(mesg, sqle);
        }
		
		
		
		
		java.util.Map insertMap = new HashMap();
		java.util.Map deleteMap = new HashMap();
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
	        	PhysicalLink physicalLink = (PhysicalLink) it.next();
	            linkedObjectIds.add(physicalLink.getId());
			}
	        
            List dbLinkedObjectIds = (List) dbPhysicalLinkIdsMap.get(map.getId());

            List deleteList = null;
            List insertList = null;
            // prepare list for	deleting
            for (Iterator it = linkedObjectIds.iterator(); it.hasNext();) {
            	Identifier linkedObjectId = (Identifier) it.next();
            	if (!dbLinkedObjectIds.contains(linkedObjectId)){
            		if (insertList == null){
            			insertList = new LinkedList();
            			insertMap.put(map, insertList);
            		}
            		insertList.add(linkedObjectId);
            	}
            }
            
            //  prepare list for inserting
            for (Iterator it = dbLinkedObjectIds.iterator(); it.hasNext();) {
                Identifier dbLinkedObjectId = (Identifier) it.next();
    			if(!linkedObjectIds.contains(dbLinkedObjectId)) {
    				if (deleteList == null){
    					deleteList = new LinkedList();
    					deleteMap.put(map, insertList);
    				}
    				deleteList.add(dbLinkedObjectId);
                }    			
    		}
		}
		try {
			this.insertLinkedObjectIds(insertMap, tableName);
			this.deleteLinkedObjectIds(deleteMap, tableName);
		} catch (CreateObjectException e) {
			throw new UpdateObjectException(e);
		}
	}
	
	/**
	 * 
	 * @param mapIdLinkedObjectIdMap map of &lt;&lt;Identifier&gt; collectorId , List&lt;Identifier&gt; physicalLinkIds&gt; 
	 * @throws CreateObjectException
	 */
	private void insertLinkedObjectIds(java.util.Map mapIdLinkedObjectIdMap, String tableName) throws CreateObjectException{
		String columnName = (String)dbTableColumnName.get(tableName);
		
		String sql = SQL_INSERT_INTO 
		+ ObjectEntities.SETMELINK_ENTITY
		+ OPEN_BRACKET
		+ LINK_COLUMN_MAP_ID + COMMA 
		+ columnName
		+ CLOSE_BRACKET
		+ SQL_VALUES + OPEN_BRACKET
		+ QUESTION + COMMA
		+ QUESTION 
		+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier mapId = null;
		Identifier linkedObjectId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = mapIdLinkedObjectIdMap.keySet().iterator(); iterator.hasNext();) {
				mapId = (Identifier)iterator.next();
				linkedObjectId = (Identifier)mapIdLinkedObjectIdMap.get(mapId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, mapId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, linkedObjectId);
				Log.debugMessage(this.getEnityName() + "Database.insertLinkedObjectIds | Inserting linked object " + linkedObjectId + " for " + mapId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.insertLinkedObjectIds | Cannot insert linked object " + linkedObjectId + " for " + mapId + "' -- " + sqle.getMessage();
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
			}  finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	private void deleteLinkedObjectIds(java.util.Map collectorPhysicalLinkMap, String tableName) {
		String columnName = (String)dbTableColumnName.get(tableName);
		StringBuffer linkBuffer = new StringBuffer(columnName);
		
		linkBuffer.append(SQL_IN);
		linkBuffer.append(OPEN_BRACKET);
		
		int i = 0;
		for (Iterator colIter = collectorPhysicalLinkMap.keySet().iterator(); colIter.hasNext();) {
			Identifier collectorId = (Identifier) colIter.next();
			List physicalLinkIds = (List)collectorPhysicalLinkMap.get(collectorId);
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
