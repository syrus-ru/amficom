/*
 * $Id: DatabaseMapObjectLoader.java,v 1.3 2004/12/07 08:20:11 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/07 08:20:11 $
 * @author $Author: bob $
 * @module map_v1
 */
public final class DatabaseMapObjectLoader implements MapObjectLoader {

	private StorableObjectDatabase getDatabase(short entityCode){
		StorableObjectDatabase database = null;
		switch (entityCode) {
			case ObjectEntities.COLLECTOR_ENTITY_CODE:
				database = MapDatabaseContext.getCollectorDatabase();
				break;
			case ObjectEntities.MAP_ENTITY_CODE:
				database = MapDatabaseContext.getMapDatabase();
				break;
			case ObjectEntities.MARK_ENTITY_CODE:
				database = MapDatabaseContext.getMarkDatabase();
				break;
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				database = MapDatabaseContext.getNodeLinkDatabase();
				break;
			case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
				database = MapDatabaseContext.getPhysicalLinkDatabase();
				break;
			case ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE:
				database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
				break;
			case ObjectEntities.SITE_NODE_ENTITY_CODE:
				database = MapDatabaseContext.getSiteNodeDatabase();
				break;
			case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
				database = MapDatabaseContext.getSiteNodeTypeDatabase();
				break;
			case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
				database = MapDatabaseContext.getTopologicalNodeDatabase();
				break;
			default:
				Log.errorMessage("DatabaseMapObjectLoader.getDatabase | Unknown entity: " + entityCode);
		}
		return database;
	}
	
	private void delete(Identifier id, List ids) throws DatabaseException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (ids.isEmpty())
				return;
			Object obj = ids.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier) obj).getMajor();
			else if (obj instanceof Identified)
				entityCode = ((Identified) obj).getId().getMajor();
		}
		try {
			StorableObjectDatabase database = this.getDatabase(entityCode);
			if (database != null) {
				if (id != null)
					database.delete(id);
				else if (ids != null && !ids.isEmpty()) {
					database.delete(ids);
				}
			}
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.delete | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.delete | DatabaseException: " + e.getMessage());
		}
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		delete(id, null);
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
		delete(null, ids);
	}

	public Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException {
		return new Collector(id);
	}

	public List loadCollectors(List ids) throws DatabaseException, CommunicationException {
		CollectorDatabase database = (CollectorDatabase) MapDatabaseContext.getCollectorDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadCollectors | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadCollectors | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadCollectorsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		CollectorDatabase database = (CollectorDatabase) MapDatabaseContext.getCollectorDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadCollectorsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadCollectorsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}
	
	public Map loadMap(Identifier id) throws DatabaseException, CommunicationException {
		return new Map(id);
	}

	public List loadMaps(List ids) throws DatabaseException, CommunicationException {
		MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMaps | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadMaps | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}
	
	public List loadMapsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMapsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadMapsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public Mark loadMark(Identifier id) throws DatabaseException, CommunicationException {
		return new Mark(id);
	}

	public List loadMarks(List ids) throws DatabaseException, CommunicationException {
		MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMarks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadMarks | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}
	
	public List loadMarksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMarksButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadMarksButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public NodeLink loadNodeLink(Identifier id) throws DatabaseException, CommunicationException {
		return new NodeLink(id);
	}

	public List loadNodeLinks(List ids) throws DatabaseException, CommunicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadNodeLinks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadNodeLinks | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public List loadNodeLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadNodeLinksButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadNodeLinksButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public PhysicalLink loadPhysicalLink(Identifier id) throws DatabaseException, CommunicationException {
		return new PhysicalLink(id);
	}

	public List loadPhysicalLinks(List ids) throws DatabaseException, CommunicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinks | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}
	
	public List loadPhysicalLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinksButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinksButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public PhysicalLinkType loadPhysicalLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return new PhysicalLinkType(id);
	}

	public List loadPhysicalLinkTypes(List ids) throws DatabaseException, CommunicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext.getPhysicalLinkTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinkTypes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinkTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}	

	public List loadPhysicalLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext.getPhysicalLinkTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinkTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinkTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public SiteNode loadSiteNode(Identifier id) throws DatabaseException, CommunicationException {
		return new SiteNode(id);
	}

	public List loadSiteNodes(List ids) throws DatabaseException, CommunicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadSiteNodes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}
	
	public List loadSiteNodesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadSiteNodesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public SiteNodeType loadSiteNodeType(Identifier id) throws DatabaseException, CommunicationException {
		return new SiteNodeType(id);
	}

	public List loadSiteNodeTypes(List ids) throws DatabaseException, CommunicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodeTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadSiteNodeTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}	

	public List loadSiteNodeTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodeTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadSiteNodeTypesButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public TopologicalNode loadTopologicalNode(Identifier id) throws DatabaseException, CommunicationException {
		return new TopologicalNode(id);
	}

	public List loadTopologicalNodes(List ids) throws DatabaseException, CommunicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext.getTopologicalNodeDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadTopologicalNodes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadTopologicalNodes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}
	
	public List loadTopologicalNodesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext.getTopologicalNodeDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadTopologicalNodesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadTopologicalNodesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

    short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		try {
			StorableObjectDatabase database = this.getDatabase(entityCode);

			if (database != null)
				return database.refresh(storableObjects);

			return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("DatabaseConfigurationObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseConfigurationObjectLoader.refresh | DatabaseException: " + e.getMessage());
		}
	}

	public void saveCollector(Collector collector, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CollectorDatabase database = (CollectorDatabase) MapDatabaseContext.getCollectorDatabase();
		try {
			database.update(collector, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveCollector | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveCollector | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveCollector | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveCollector | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void saveMap(Map map, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
		try {
			database.update(map, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK,
				null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMap | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMap | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMap | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMap | Illegal Storable Object: " + e.getMessage());
		}
	}

	public void saveMark(Mark mark, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
		try {
			database.update(mark, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK,
				null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMark | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMark | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMark | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMark | Illegal Storable Object: " + e.getMessage());
		}
	}

	public void saveNodeLink(NodeLink nodeLink, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
		try {
			database.update(nodeLink,
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveNodeLink | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveNodeLink | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveNodeLink | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveNodeLink | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void savePhysicalLink(PhysicalLink physicalLink, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
		try {
			database.update(physicalLink, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLink | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLink | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLink | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLink | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void savePhysicalLinkType(PhysicalLinkType physicalLinkType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			database.update(physicalLinkType, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinkType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinkType | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinkType | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinkType | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void saveSiteNode(SiteNode siteNode, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
		try {
			database.update(siteNode,
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNode | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNode | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNode | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNode | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void saveSiteNodeType(SiteNodeType siteNodeType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			database.update(siteNodeType, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodeType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodeType | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodeType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodeType | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void saveTopologicalNode(TopologicalNode topologicalNode, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			database.update(topologicalNode, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveTopologicalNode | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveTopologicalNode | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log
					.errorMessage("DatabaseMapObjectLoader.saveTopologicalNode | Illegal Storable Object: "
							+ e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveTopologicalNode | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void saveCollectors(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CollectorDatabase database = (CollectorDatabase) MapDatabaseContext.getCollectorDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveCollectors | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveCollectors | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveCollectors | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveCollectors | Illegal Storable Object: "
					+ e.getMessage());
		}

	}

	public void saveMaps(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK,
				null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMaps | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMaps | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMaps | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMaps | Illegal Storable Object: " + e.getMessage());
		}

	}

	public void saveMarks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK,
				null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMarks | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMarks | UpdateObjectException: " + e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMarks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMarks | Illegal Storable Object: " + e.getMessage());
		}
	}

	public void saveNodeLinks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
		try {
			database.update(list,
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveNodeLinks | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveNodeLinks | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveNodeLinks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveNodeLinks | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void savePhysicalLinks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinks | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinks | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinks | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void savePhysicalLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinkTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinkTypes | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinkTypes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinkTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void saveSiteNodes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
		try {
			database.update(list,
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodes | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodes | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void saveSiteNodeTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodeTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodeType | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodeTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodeTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

	public void saveTopologicalNodes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK, null);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveTopologicalNodes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveTopologicalNodes | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log
					.errorMessage("DatabaseMapObjectLoader.saveTopologicalNodes | Illegal Storable Object: "
							+ e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveTopologicalNodes | Illegal Storable Object: "
					+ e.getMessage());
		}
	}

}
