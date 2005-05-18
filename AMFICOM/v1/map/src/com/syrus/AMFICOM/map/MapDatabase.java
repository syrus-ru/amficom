/*
 * $Id: MapDatabase.java,v 1.26 2005/05/18 11:48:20 bass Exp $
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.26 $, $Date: 2005/05/18 11:48:20 $
 * @author $Author: bass $
 * @module map_v1
 */
public class MapDatabase extends CharacterizableDatabase {
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
	

    private static java.util.Map dbTableColumnName;
	
	static {
		dbTableColumnName = new HashMap();
		dbTableColumnName.put(MAP_COLLECTOR, MapWrapper.LINK_COLUMN_COLLECTOR_ID);
		dbTableColumnName.put(MAP_MARK, MapWrapper.LINK_COLUMN_MARK_ID);
		dbTableColumnName.put(MAP_NODE_LINK, MapWrapper.LINK_COLUMN_NODE_LINK_ID);
		dbTableColumnName.put(MAP_PHYSICAL_LINK, MapWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
		dbTableColumnName.put(MAP_SITE_NODE, MapWrapper.LINK_COLUMN_SITE_NODE_ID);
		dbTableColumnName.put(MAP_TOPOLOGICAL_NODE, MapWrapper.LINK_COLUMN_TOPOLOGICAL_NODE_ID);
	}


	private static String columns;
	
	private static String updateMultipleSQLValues;
	
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
		Set maps = Collections.singleton(map);
		
		{
			java.util.Map collectors = this.retrieveLinkedObjects(maps, _MAP_COLLECTOR);
			for (Iterator it = collectors.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Set collectorIds = (Set)collectors.get(id);
				if (id.equals(map.getId())){
					try {
						map.setCollectors0(StorableObjectPool.getStorableObjects(collectorIds, true));
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map marks = this.retrieveLinkedObjects(maps, _MAP_MARK);
			for (Iterator it = marks.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Set markIds = (Set)marks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setMarks0(StorableObjectPool.getStorableObjects(markIds, true));
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map nodeLinks = this.retrieveLinkedObjects(maps, _MAP_NODE_LINK);
			for (Iterator it = nodeLinks.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Set nodeLinkIds = (Set)nodeLinks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setNodeLinks0(StorableObjectPool.getStorableObjects(nodeLinkIds, true));
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map physicalLinks = this.retrieveLinkedObjects(maps, _MAP_PHYSICAL_LINK);
			for (Iterator it = physicalLinks.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Set physicalLinkIds = (Set)physicalLinks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setPhysicalLinks0(StorableObjectPool.getStorableObjects(physicalLinkIds, true));
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map siteNodes = this.retrieveLinkedObjects(maps, _MAP_SITE_NODE);
			for (Iterator it = siteNodes.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Set siteNodeIds = (Set)siteNodes.get(id);
				if (id.equals(map.getId())){
					try {
						map.setSiteNodes0(StorableObjectPool.getStorableObjects(siteNodeIds, true));
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			java.util.Map topologicalNodes = this.retrieveLinkedObjects(maps, _MAP_TOPOLOGICAL_NODE);
			for (Iterator it = topologicalNodes.keySet().iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				Set topologicalNodeIds = (Set)topologicalNodes.get(id);
				if (id.equals(map.getId())){
					try {
						map.setTopologicalNodes0(StorableObjectPool.getStorableObjects(topologicalNodeIds, true));
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
	}	
	
	private java.util.Map retrieveLinkedObjects(Set maps, int linkedTable) throws RetrieveObjectException, IllegalDataException{
		if (maps == null || maps.isEmpty())
			return Collections.EMPTY_MAP;
		
		String tableName = this.getLinkedTableName(linkedTable);
		String columnName = (String)dbTableColumnName.get(tableName);
		
		return super.retrieveLinkedEntityIds(maps, tableName, MapWrapper.LINK_COLUMN_MAP_ID, columnName);
	}
	
	protected String getEnityName() {		
		return ObjectEntities.MAP_ENTITY;
	}	
	
	protected String getColumnsTmpl() {
		if (columns == null){
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MapWrapper.COLUMN_DOMAIN_ID;
		}
		return columns;
	}	
	
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		Map map = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, map.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, map.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, map.getDomainId());
		return startParameterNumber;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Map map = fromStorableObject(storableObject);
		String values = APOSTOPHE + DatabaseString.toQuerySubString(map.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(map.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(map.getDomainId());
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, SQLException {
		Map map = (storableObject == null) ?
				new Map(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null) :
					fromStorableObject(storableObject);				
		
		map.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
							   DatabaseIdentifier.getIdentifier(resultSet, MapWrapper.COLUMN_DOMAIN_ID));		
		return map;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException {
		Map map = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + map.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		Map map = this.fromStorableObject(storableObject);
		super.insertEntity(map);
		Set maps = Collections.singleton(map);
		try {
			this.updateLinkedObjectIds(maps, _MAP_COLLECTOR);
			this.updateLinkedObjectIds(maps, _MAP_MARK);
			this.updateLinkedObjectIds(maps, _MAP_NODE_LINK);
			this.updateLinkedObjectIds(maps, _MAP_PHYSICAL_LINK);
			this.updateLinkedObjectIds(maps, _MAP_SITE_NODE);
			this.updateLinkedObjectIds(maps, _MAP_TOPOLOGICAL_NODE);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			this.updateLinkedObjectIds(storableObjects, _MAP_COLLECTOR);
			this.updateLinkedObjectIds(storableObjects, _MAP_MARK);
			this.updateLinkedObjectIds(storableObjects, _MAP_NODE_LINK);
			this.updateLinkedObjectIds(storableObjects, _MAP_PHYSICAL_LINK);
			this.updateLinkedObjectIds(storableObjects, _MAP_SITE_NODE);
			this.updateLinkedObjectIds(storableObjects, _MAP_TOPOLOGICAL_NODE);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				return;
		}
		Set maps = Collections.singleton(storableObject);
		this.updateLinkedObjectIds(maps, _MAP_COLLECTOR);
		this.updateLinkedObjectIds(maps, _MAP_MARK);
		this.updateLinkedObjectIds(maps, _MAP_NODE_LINK);
		this.updateLinkedObjectIds(maps, _MAP_PHYSICAL_LINK);
		this.updateLinkedObjectIds(maps, _MAP_SITE_NODE);
		this.updateLinkedObjectIds(maps, _MAP_TOPOLOGICAL_NODE);
	}

	public void update(Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);
				return;
		}
		this.updateLinkedObjectIds(storableObjects, _MAP_COLLECTOR);
		this.updateLinkedObjectIds(storableObjects, _MAP_MARK);
		this.updateLinkedObjectIds(storableObjects, _MAP_NODE_LINK);
		this.updateLinkedObjectIds(storableObjects, _MAP_PHYSICAL_LINK);
		this.updateLinkedObjectIds(storableObjects, _MAP_SITE_NODE);
		this.updateLinkedObjectIds(storableObjects, _MAP_TOPOLOGICAL_NODE);
	}	

	private void updateLinkedObjectIds(Set maps, int linkedTable) throws UpdateObjectException {
		if (maps == null || maps.isEmpty())
			return;

		String tableName;
		try {
			tableName = getLinkedTableName(linkedTable);
		}
		catch (IllegalDataException e) {
			throw new UpdateObjectException(e);
		}
		String columnName = (String) dbTableColumnName.get(tableName);

		java.util.Map mapIdLinkedObjectIds = new HashMap();

		for (Iterator colIter = maps.iterator(); colIter.hasNext();) {
			StorableObject storableObject = (StorableObject) colIter.next();
			Map map;
			try {
				map = fromStorableObject(storableObject);
			}
			catch (IllegalDataException e) {
				throw new UpdateObjectException(e);
			}
			Set linkedObjectList;
			switch (linkedTable) {
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
					throw new UpdateObjectException(this.getEnityName()
							+ "Database.updateLinkedObjectIds | unknown linked table code:" + linkedTable);
			}

			Set linkedObjectIds = new HashSet(linkedObjectList.size());
			for (Iterator it = linkedObjectList.iterator(); it.hasNext();) {
				StorableObject storableObject2 = (StorableObject) it.next();
				linkedObjectIds.add(storableObject2.getId());
			}

			mapIdLinkedObjectIds.put(map.getId(), linkedObjectIds);
		}

		super.updateLinkedEntityIds(mapIdLinkedObjectIds, tableName, MapWrapper.LINK_COLUMN_MAP_ID, columnName);
	}

	public void delete(Identifier id) {
		this.delete(Collections.singleton(id));
	}

	public void delete(Set ids) {
		super.delete(ids);

		java.util.Map linkedObjectIds = new HashMap();

		java.util.Map mapIds = new HashMap();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			try {
				Map map = (Map) MapStorableObjectPool.getStorableObject(mapId, true);
				mapIds.put(mapId, map);
			}
			catch (ApplicationException ae) {
				Log.errorMessage(this.getEnityName() + "Database.delete | Couldn't found map for " + mapId);
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

	private void deleteLinkedObjectIds(java.util.Map linkedObjectIds, int linkedTable) {

		String tableName;
		try {
			tableName = this.getLinkedTableName(linkedTable);
		}
		catch (IllegalDataException e) {
			Log.errorMessage(getEnityName() + "Database.deleteLinkedObjectIds | illegal linked database table id "
					+ linkedTable + " -- " + e.getMessage());
			return;
		}
		String columnName = (String) dbTableColumnName.get(tableName);

		StringBuffer linkBuffer = new StringBuffer(SQL_DELETE_FROM + tableName + SQL_WHERE);
		linkBuffer.append(idsEnumerationString(linkedObjectIds.keySet(), columnName, true));

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(linkBuffer.toString());
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Set maps = super.retrieveByCondition(conditionQuery);
		
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
				Set collectorIds = (Set)collectors.get(id);				
				if (id.equals(map.getId())){
					try {
						map.setCollectors0(StorableObjectPool.getStorableObjects(collectorIds, true));
					} catch (ApplicationException e) {
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
				Set markIds = (Set)marks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setMarks0(StorableObjectPool.getStorableObjects(markIds, true));
					} catch (ApplicationException e) {
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
				Set nodeLinkIds = (Set)nodeLinks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setNodeLinks0(StorableObjectPool.getStorableObjects(nodeLinkIds, true));
					} catch (ApplicationException e) {
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
				Set physicalLinkIds = (Set)physicalLinks.get(id);
				if (id.equals(map.getId())){
					try {
						map.setPhysicalLinks0(StorableObjectPool.getStorableObjects(physicalLinkIds, true));
					} catch (ApplicationException e) {
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
				Set siteNodeIds = (Set)siteNodes.get(id);
				if (id.equals(map.getId())){
					try {
						map.setSiteNodes0(StorableObjectPool.getStorableObjects(siteNodeIds, true));
					} catch (ApplicationException e) {
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
				Set topologicalNodeIds = (Set)topologicalNodes.get(id);
				if (id.equals(map.getId())){
					try {
						map.setTopologicalNodes0(StorableObjectPool.getStorableObjects(topologicalNodeIds, true));
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		return maps;
		//return retriveByIdsPreparedStatement(ids, conditions);
	}	
	
}
