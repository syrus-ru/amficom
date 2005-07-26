/*-
 * $Id: MapDatabase.java,v 1.37 2005/07/26 11:41:05 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.37 $, $Date: 2005/07/26 11:41:05 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public final class MapDatabase extends StorableObjectDatabase {
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
	

    private static java.util.Map<String, String> dbTableColumnName;
	
	static {
		dbTableColumnName = new HashMap<String, String>();
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
				throw new IllegalDataException(this.getEntityName() + "Database.retrieveLinkedObjects | unknown linked table code:" + linkedTable);
		}
		return tableName;
	}

	private Map fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Map)
			return (Map) storableObject;
		throw new IllegalDataException(this.getEntityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final Map map = this.fromStorableObject(storableObject);
		this.retrieveEntity(map);
		final Set<Map> maps = Collections.singleton(map);
		
		{
			final java.util.Map<Identifier, Set<Identifier>> collectors = this.retrieveLinkedObjects(maps, _MAP_COLLECTOR);
			for (final Identifier id : collectors.keySet()) {
				final Set<Identifier> collectorIds = collectors.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<Collector> loadedCollectors = StorableObjectPool.getStorableObjects(collectorIds, true);
						map.setCollectors0(loadedCollectors);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> marks = this.retrieveLinkedObjects(maps, _MAP_MARK);
			for (final Identifier id : marks.keySet()) {
				final Set<Identifier> markIds = marks.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<Mark> loadedMarks = StorableObjectPool.getStorableObjects(markIds, true);
						map.setMarks0(loadedMarks);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> nodeLinks = this.retrieveLinkedObjects(maps, _MAP_NODE_LINK);
			for (final Identifier id : nodeLinks.keySet()) {
				final Set<Identifier> nodeLinkIds = nodeLinks.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<NodeLink> loadedNodeLinks = StorableObjectPool.getStorableObjects(nodeLinkIds, true);
						map.setNodeLinks0(loadedNodeLinks);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> physicalLinks = this.retrieveLinkedObjects(maps, _MAP_PHYSICAL_LINK);
			for (final Identifier id : physicalLinks.keySet()) {
				final Set<Identifier> physicalLinkIds = physicalLinks.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<PhysicalLink> loadedPhysicalLinks = StorableObjectPool.getStorableObjects(physicalLinkIds, true);
						map.setPhysicalLinks0(loadedPhysicalLinks);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> siteNodes = this.retrieveLinkedObjects(maps, _MAP_SITE_NODE);
			for (final Identifier id : siteNodes.keySet()) {
				final Set<Identifier> siteNodeIds = siteNodes.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<SiteNode> loadedSiteNodes = StorableObjectPool.getStorableObjects(siteNodeIds, true);
						map.setSiteNodes0(loadedSiteNodes);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> topologicalNodes = this.retrieveLinkedObjects(maps, _MAP_TOPOLOGICAL_NODE);
			for (final Identifier id : topologicalNodes.keySet()) {
				final Set<Identifier> topologicalNodeIds = topologicalNodes.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<TopologicalNode> loadedTopologicalNodes = StorableObjectPool.getStorableObjects(topologicalNodeIds, true);
						map.setTopologicalNodes0(loadedTopologicalNodes);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
	}	

	private java.util.Map<Identifier, Set<Identifier>> retrieveLinkedObjects(final Set<Map> maps, final int linkedTable)
			throws RetrieveObjectException,
				IllegalDataException {
		if (maps == null || maps.isEmpty())
			return Collections.emptyMap();

		final String tableName = this.getLinkedTableName(linkedTable);
		final String columnName = dbTableColumnName.get(tableName);

		return super.retrieveLinkedEntityIds(maps, tableName, MapWrapper.LINK_COLUMN_MAP_ID, columnName);
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MAP_CODE;
	}	

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MapWrapper.COLUMN_DOMAIN_ID;
		}
		return columns;
	}	

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		final Map map = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, map.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, map.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, map.getDomainId());
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		final Map map = fromStorableObject(storableObject);
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(map.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(map.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(map.getDomainId());
		return values;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final Map map = (storableObject == null)
				? new Map(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null)
					: fromStorableObject(storableObject);

		map.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, MapWrapper.COLUMN_DOMAIN_ID));
		return map;
	}

	@Override
	public void insert(final Set<? extends StorableObject> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);
		try {
			this.updateLinkedObjectIds(storableObjects, _MAP_COLLECTOR);
			this.updateLinkedObjectIds(storableObjects, _MAP_MARK);
			this.updateLinkedObjectIds(storableObjects, _MAP_NODE_LINK);
			this.updateLinkedObjectIds(storableObjects, _MAP_PHYSICAL_LINK);
			this.updateLinkedObjectIds(storableObjects, _MAP_SITE_NODE);
			this.updateLinkedObjectIds(storableObjects, _MAP_TOPOLOGICAL_NODE);
		} catch (final UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final Set<? extends StorableObject> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updateLinkedObjectIds(storableObjects, _MAP_COLLECTOR);
		this.updateLinkedObjectIds(storableObjects, _MAP_MARK);
		this.updateLinkedObjectIds(storableObjects, _MAP_NODE_LINK);
		this.updateLinkedObjectIds(storableObjects, _MAP_PHYSICAL_LINK);
		this.updateLinkedObjectIds(storableObjects, _MAP_SITE_NODE);
		this.updateLinkedObjectIds(storableObjects, _MAP_TOPOLOGICAL_NODE);
	}	

	private void updateLinkedObjectIds(final Set<? extends StorableObject> maps, final int linkedTable)
			throws UpdateObjectException {
		if (maps == null || maps.isEmpty())
			return;

		String tableName;
		try {
			tableName = getLinkedTableName(linkedTable);
		} catch (IllegalDataException e) {
			throw new UpdateObjectException(e);
		}
		final String columnName = dbTableColumnName.get(tableName);

		final java.util.Map<Identifier, Set<Identifier>> mapIdLinkedObjectIds = new HashMap<Identifier, Set<Identifier>>();

		for (final StorableObject storableObject : maps) {
			Map map;
			try {
				map = this.fromStorableObject(storableObject);
			} catch (IllegalDataException e) {
				throw new UpdateObjectException(e);
			}
			Set<? extends StorableObject> linkedObjectList;
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
					throw new UpdateObjectException(this.getEntityName()
							+ "Database.updateLinkedObjectIds | unknown linked table code:" + linkedTable);
			}

			final Set<Identifier> linkedObjectIds = new HashSet<Identifier>(linkedObjectList.size());
			for (final StorableObject storableObject2 : linkedObjectList) {
				linkedObjectIds.add(storableObject2.getId());
			}

			mapIdLinkedObjectIds.put(map.getId(), linkedObjectIds);
		}

		super.updateLinkedEntityIds(mapIdLinkedObjectIds, tableName, MapWrapper.LINK_COLUMN_MAP_ID, columnName);
	}

	@Override
	public void delete(final Identifier id) {
		this.delete(Collections.singleton(id));
	}

	@Override
	public void delete(final Set<? extends Identifiable> ids) {
		super.delete(ids);

		final java.util.Map<Map, Set<? extends StorableObject>> linkedObjectIds = new HashMap<Map, Set<? extends StorableObject>>();

		final java.util.Map<Identifier, Map> mapIds = new HashMap<Identifier, Map>();
		for (final Identifiable identifiable : ids) {
			final Identifier mapId = identifiable.getId();
			try {
				final Map map = (Map) StorableObjectPool.getStorableObject(mapId, true);
				mapIds.put(mapId, map);
			} catch (ApplicationException ae) {
				Log.errorMessage(this.getEntityName() + "Database.delete | Couldn't found map for " + mapId);
			}
		}

		for (final Identifiable identifiable : ids) {
			final Identifier mapId = identifiable.getId();
			final Map map = mapIds.get(mapId);
			linkedObjectIds.put(map, map.getCollectors());
		}

		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_COLLECTOR);

		linkedObjectIds.clear();
		for (final Identifiable identifiable : ids) {
			final Identifier mapId = identifiable.getId();
			final Map map = mapIds.get(mapId);
			linkedObjectIds.put(map, map.getMarks());
		}
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_MARK);

		linkedObjectIds.clear();
		for (final Identifiable identifiable : ids) {
			final Identifier mapId = identifiable.getId();
			final Map map = mapIds.get(mapId);
			linkedObjectIds.put(map, map.getNodeLinks());
		}
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_NODE_LINK);

		linkedObjectIds.clear();
		for (final Identifiable identifiable : ids) {
			final Identifier mapId = identifiable.getId();
			final Map map = mapIds.get(mapId);
			linkedObjectIds.put(map, map.getPhysicalLinks());
		}
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_PHYSICAL_LINK);

		linkedObjectIds.clear();
		for (final Identifiable identifiable : ids) {
			final Identifier mapId = identifiable.getId();
			final Map map = mapIds.get(mapId);
			linkedObjectIds.put(map, map.getSiteNodes());
		}
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_SITE_NODE);

		linkedObjectIds.clear();
		for (final Identifiable identifiable : ids) {
			final Identifier mapId = identifiable.getId();
			final Map map = mapIds.get(mapId);
			linkedObjectIds.put(map, map.getTopologicalNodes());
		}
		this.deleteLinkedObjectIds(linkedObjectIds, _MAP_TOPOLOGICAL_NODE);
	}	

	private void deleteLinkedObjectIds(final java.util.Map<Map, Set<? extends StorableObject>> linkedObjectIds, final int linkedTable) {
		String tableName;
		try {
			tableName = this.getLinkedTableName(linkedTable);
		} catch (IllegalDataException e) {
			Log.errorMessage(getEntityName() + "Database.deleteLinkedObjectIds | illegal linked database table id "
					+ linkedTable + " -- " + e.getMessage());
			return;
		}
		String columnName = dbTableColumnName.get(tableName);

		final StringBuffer linkBuffer = new StringBuffer(SQL_DELETE_FROM + tableName + SQL_WHERE);
		linkBuffer.append(idsEnumerationString(linkedObjectIds.keySet(), columnName, true));

		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(linkBuffer.toString());
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

	@Override
	protected Set retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Map> maps = super.retrieveByCondition(conditionQuery);
		
		final java.util.Map<Identifier, Map> mapIds = new HashMap<Identifier, Map>();
		for (final Map map : maps) {
			mapIds.put(map.getId(), map);
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> collectors = this.retrieveLinkedObjects(maps, _MAP_COLLECTOR);
			for (final Identifier id : collectors.keySet()) {
				final Map map = mapIds.get(id);
				final Set<Identifier> collectorIds = collectors.get(id);				
				if (id.equals(map.getId())) {
					try {
						final Set<Collector> loadedCollectors = StorableObjectPool.getStorableObjects(collectorIds, true);
						map.setCollectors0(loadedCollectors);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> marks = this.retrieveLinkedObjects(maps, _MAP_MARK);
			for (final Identifier id : marks.keySet()) {
				final Map map = mapIds.get(id);
				final Set<Identifier> markIds = marks.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<Mark> loadedMarks = StorableObjectPool.getStorableObjects(markIds, true);
						map.setMarks0(loadedMarks);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> nodeLinks = this.retrieveLinkedObjects(maps, _MAP_NODE_LINK);
			for (final Identifier id : nodeLinks.keySet()) {
				final Map map = mapIds.get(id);
				final Set<Identifier> nodeLinkIds = nodeLinks.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<NodeLink> loadedNodeLinkds = StorableObjectPool.getStorableObjects(nodeLinkIds, true);
						map.setNodeLinks0(loadedNodeLinkds);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> physicalLinks = this.retrieveLinkedObjects(maps, _MAP_PHYSICAL_LINK);
			for (final Identifier id :physicalLinks.keySet()) {
				final Map map = mapIds.get(id);
				final Set<Identifier> physicalLinkIds = physicalLinks.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<PhysicalLink> loadedPhysicalLinks = StorableObjectPool.getStorableObjects(physicalLinkIds, true);
						map.setPhysicalLinks0(loadedPhysicalLinks);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> siteNodes = this.retrieveLinkedObjects(maps, _MAP_SITE_NODE);
			for (final Identifier id : siteNodes.keySet()) {
				final Map map = mapIds.get(id);
				final Set<Identifier> siteNodeIds = siteNodes.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<SiteNode> loadedSiteNodes = StorableObjectPool.getStorableObjects(siteNodeIds, true);
						map.setSiteNodes0(loadedSiteNodes);
					} catch (ApplicationException e) {
						throw new RetrieveObjectException(e);
					}
				}
			}
		}
		
		{
			final java.util.Map<Identifier, Set<Identifier>> topologicalNodes = this.retrieveLinkedObjects(maps, _MAP_TOPOLOGICAL_NODE);
			for (final Identifier id : topologicalNodes.keySet()) {
				final Map map = mapIds.get(id);
				final Set<Identifier> topologicalNodeIds = topologicalNodes.get(id);
				if (id.equals(map.getId())) {
					try {
						final Set<TopologicalNode> loadedTopologicalNodes = StorableObjectPool.getStorableObjects(topologicalNodeIds, true);
						map.setTopologicalNodes0(loadedTopologicalNodes);
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
