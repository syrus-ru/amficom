/*
 * $Id: DatabaseMapObjectLoader.java,v 1.11 2005/04/01 11:11:05 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.11 $, $Date: 2005/04/01 11:11:05 $
 * @author $Author: bob $
 * @module map_v1
 */
public class DatabaseMapObjectLoader implements MapObjectLoader {

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
	
	private void delete(Identifier id, Set ids) throws IllegalDataException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (ids.isEmpty())
				return;
			Object obj = ids.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier) obj).getMajor();
			else if (obj instanceof Identifiable)
				entityCode = ((Identifiable) obj).getId().getMajor();
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
			throw new IllegalDataException("DatabaseMapObjectLoader.delete | DatabaseException: " + e.getMessage());
		}
	}

	public void delete(Identifier id) throws IllegalDataException {
		delete(id, null);
	}

	public void delete(Set ids) throws IllegalDataException {
		delete(null, ids);
	}

	public Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException {
		return new Collector(id);
	}

	public Set loadCollectors(Set ids) throws DatabaseException, CommunicationException {
		CollectorDatabase database = (CollectorDatabase) MapDatabaseContext.getCollectorDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadCollectors | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadCollectors | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Set loadCollectorsButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		CollectorDatabase database = (CollectorDatabase) MapDatabaseContext.getCollectorDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
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

	public Set loadMaps(Set ids) throws DatabaseException, CommunicationException {
		MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMaps | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadMaps | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}
	
	public Set loadMapsButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMapsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadMapsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public Mark loadMark(Identifier id) throws DatabaseException, CommunicationException {
		return new Mark(id);
	}

	public Set loadMarks(Set ids) throws DatabaseException, CommunicationException {
		MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMarks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadMarks | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}
	
	public Set loadMarksButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
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

	public Set loadNodeLinks(Set ids) throws DatabaseException, CommunicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadNodeLinks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadNodeLinks | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Set loadNodeLinksButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
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

	public Set loadPhysicalLinks(Set ids) throws DatabaseException, CommunicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinks | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinks | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}
	
	public Set loadPhysicalLinksButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
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

	public Set loadPhysicalLinkTypes(Set ids) throws DatabaseException, CommunicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext.getPhysicalLinkTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinkTypes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinkTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}	

	public Set loadPhysicalLinkTypesButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext.getPhysicalLinkTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
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

	public Set loadSiteNodes(Set ids) throws DatabaseException, CommunicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadSiteNodes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}
	
	public Set loadSiteNodesButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
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

	public Set loadSiteNodeTypes(Set ids) throws DatabaseException, CommunicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodeTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadSiteNodeTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}	

	public Set loadSiteNodeTypesButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodeTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadSiteNodeTypesButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public TopologicalNode loadTopologicalNode(Identifier id) throws DatabaseException, CommunicationException {
		return new TopologicalNode(id);
	}

	public Set loadTopologicalNodes(Set ids) throws DatabaseException, CommunicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext.getTopologicalNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadTopologicalNodes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadTopologicalNodes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}
	
	public Set loadTopologicalNodesButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext.getTopologicalNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
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
			database.update(collector, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveCollector | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveCollector | UpdateObjectException: "
					+ e.getMessage());
		} 
	}

	public void saveMap(Map map, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
		try {
			database.update(map, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMap | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMap | UpdateObjectException: " + e.getMessage());
		}
	}

	public void saveMark(Mark mark, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
		try {
			database.update(mark, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMark | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMark | UpdateObjectException: " + e.getMessage());
		}
	}

	public void saveNodeLink(NodeLink nodeLink, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
		try {
			database.update(nodeLink, SessionContext.getAccessIdentity().getUserId(), 
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveNodeLink | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveNodeLink | UpdateObjectException: "
					+ e.getMessage());
		} 
	}

	public void savePhysicalLink(PhysicalLink physicalLink, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
		try {
			database.update(physicalLink, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLink | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLink | UpdateObjectException: "
					+ e.getMessage());
		} 
	}

	public void savePhysicalLinkType(PhysicalLinkType physicalLinkType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			database.update(physicalLinkType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinkType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinkType | UpdateObjectException: "
					+ e.getMessage());
		}
	}

	public void saveSiteNode(SiteNode siteNode, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
		try {
			database.update(siteNode, SessionContext.getAccessIdentity().getUserId(), 
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNode | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNode | UpdateObjectException: "
					+ e.getMessage());
		}
	}

	public void saveSiteNodeType(SiteNodeType siteNodeType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			database.update(siteNodeType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodeType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodeType | UpdateObjectException: "
					+ e.getMessage());
		}
	}

	public void saveTopologicalNode(TopologicalNode topologicalNode, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			database.update(topologicalNode, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveTopologicalNode | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveTopologicalNode | UpdateObjectException: "
					+ e.getMessage());
		} 
	}

	public void saveCollectors(Set list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CollectorDatabase database = (CollectorDatabase) MapDatabaseContext.getCollectorDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveCollectors | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveCollectors | UpdateObjectException: "
					+ e.getMessage());
		}
	}

	public void saveMaps(Set list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MapDatabase database = (MapDatabase) MapDatabaseContext.getMapDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMaps | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMaps | UpdateObjectException: " + e.getMessage());
		}
	}

	public void saveMarks(Set list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MarkDatabase database = (MarkDatabase) MapDatabaseContext.getMarkDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMarks | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveMarks | UpdateObjectException: " + e.getMessage());
		}
	}

	public void saveNodeLinks(Set list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) MapDatabaseContext.getNodeLinkDatabase();
		try {
			database.update(list,
				SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveNodeLinks | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveNodeLinks | UpdateObjectException: "
					+ e.getMessage());
		}
	}

	public void savePhysicalLinks(Set list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) MapDatabaseContext.getPhysicalLinkDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinks | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinks | UpdateObjectException: "
					+ e.getMessage());
		}
	}

	public void savePhysicalLinkTypes(Set list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinkTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinkTypes | UpdateObjectException: "
					+ e.getMessage());
		}
	}

	public void saveSiteNodes(Set list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) MapDatabaseContext.getSiteNodeDatabase();
		try {
			database.update(list,
				SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodes | UpdateObjectException: "
					+ e.getMessage());
		}
	}

	public void saveSiteNodeTypes(Set list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodeTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodeType | UpdateObjectException: "
					+ e.getMessage());
		}
	}

	public void saveTopologicalNodes(Set list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveTopologicalNodes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.saveTopologicalNodes | UpdateObjectException: "
					+ e.getMessage());
		}
	}

}
