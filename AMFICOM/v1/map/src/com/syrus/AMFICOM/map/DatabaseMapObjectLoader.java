/*-
 * $Id: DatabaseMapObjectLoader.java,v 1.12 2005/04/01 11:27:33 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
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
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/04/01 11:27:33 $
 * @author $Author: bass $
 * @module map_v1
 */
public class DatabaseMapObjectLoader implements MapObjectLoader {
	private StorableObjectDatabase getDatabase(final short entityCode) {
		return MapDatabaseContext.getDatabase(entityCode);
	}
	
	private void delete(final Identifier id, final Set ids) throws IllegalDataException {
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
			Log.errorMessage("DatabaseMapObjectLoader.delete | DatabaseException: " + e.getMessage()); //$NON-NLS-1$
			throw new IllegalDataException("DatabaseMapObjectLoader.delete | DatabaseException: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	public void delete(final Identifier id) throws IllegalDataException {
		delete(id, Collections.EMPTY_SET);
	}

	public void delete(final Set ids) throws IllegalDataException {
		delete(null, ids);
	}

	public Collector loadCollector(final Identifier id) throws DatabaseException, CommunicationException {
		return new Collector(id);
	}

	public Set loadCollectors(final Set ids) throws DatabaseException, CommunicationException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadCollectors | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadCollectors | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadCollectorsButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException,
			CommunicationException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadCollectorsButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadCollectorsButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}
	
	public Map loadMap(final Identifier id) throws DatabaseException, CommunicationException {
		return new Map(id);
	}

	public Set loadMaps(final Set ids) throws DatabaseException, CommunicationException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMaps | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadMaps | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
		}
		return list;
	}
	
	public Set loadMapsButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException,
			CommunicationException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMapsButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadMapsButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
		}
		return list;
	}

	public Mark loadMark(final Identifier id) throws DatabaseException, CommunicationException {
		return new Mark(id);
	}

	public Set loadMarks(final Set ids) throws DatabaseException, CommunicationException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMarks | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadMarks | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}
	
	public Set loadMarksButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException,
			CommunicationException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMarksButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadMarksButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public NodeLink loadNodeLink(final Identifier id) throws DatabaseException, CommunicationException {
		return new NodeLink(id);
	}

	public Set loadNodeLinks(final Set ids) throws DatabaseException, CommunicationException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadNodeLinks | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadNodeLinks | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadNodeLinksButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException,
			CommunicationException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadNodeLinksButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadNodeLinksButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public PhysicalLink loadPhysicalLink(final Identifier id) throws DatabaseException, CommunicationException {
		return new PhysicalLink(id);
	}

	public Set loadPhysicalLinks(final Set ids) throws DatabaseException, CommunicationException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinks | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinks | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}
	
	public Set loadPhysicalLinksButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException,
			CommunicationException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinksButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinksButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public PhysicalLinkType loadPhysicalLinkType(final Identifier id) throws DatabaseException, CommunicationException {
		return new PhysicalLinkType(id);
	}

	public Set loadPhysicalLinkTypes(final Set ids) throws DatabaseException, CommunicationException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinkTypes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinkTypes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}	

	public Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException,
			CommunicationException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinkTypesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
			throw new DatabaseException("DatabaseMapObjectLoader.loadPhysicalLinkTypesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public SiteNode loadSiteNode(final Identifier id) throws DatabaseException, CommunicationException {
		return new SiteNode(id);
	}

	public Set loadSiteNodes(final Set ids) throws DatabaseException, CommunicationException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodes | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("loadSiteNodes.loadSiteNodes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}
	
	public Set loadSiteNodesButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException,
			CommunicationException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodesButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("loadSiteNodes.loadSiteNodesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public SiteNodeType loadSiteNodeType(final Identifier id) throws DatabaseException, CommunicationException {
		return new SiteNodeType(id);
	}

	public Set loadSiteNodeTypes(final Set ids) throws DatabaseException, CommunicationException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodeTypes | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("loadSiteNodes.loadSiteNodeTypes | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
		}
		return list;
	}	

	public Set loadSiteNodeTypesButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException,
			CommunicationException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodeTypesButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("loadSiteNodes.loadSiteNodeTypesButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
		}
		return list;
	}

	public TopologicalNode loadTopologicalNode(final Identifier id) throws DatabaseException, CommunicationException {
		return new TopologicalNode(id);
	}

	public Set loadTopologicalNodes(final Set ids) throws DatabaseException, CommunicationException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadTopologicalNodes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadTopologicalNodes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}
	
	public Set loadTopologicalNodesButIds(final StorableObjectCondition condition, final Set ids) throws DatabaseException,
			CommunicationException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadTopologicalNodesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
			throw new DatabaseException("loadSiteNodes.loadTopologicalNodesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set refresh(final Set storableObjects) throws CommunicationException, DatabaseException {
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
			Log.errorMessage("DatabaseConfigurationObjectLoader.refresh | DatabaseException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseConfigurationObjectLoader.refresh | DatabaseException: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	public void saveCollector(final Collector collector, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		try {
			database.update(collector, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveCollector | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveCollector | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		} 
	}

	public void saveMap(final Map map, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		try {
			database.update(map, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMap | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveMap | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	public void saveMark(final Mark mark, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		try {
			database.update(mark, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMark | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveMark | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	public void saveNodeLink(final NodeLink nodeLink, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		try {
			database.update(nodeLink, SessionContext.getAccessIdentity().getUserId(), 
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveNodeLink | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveNodeLink | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		} 
	}

	public void savePhysicalLink(final PhysicalLink physicalLink, final boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		try {
			database.update(physicalLink, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLink | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLink | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		} 
	}

	public void savePhysicalLinkType(final PhysicalLinkType physicalLinkType, final boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			database.update(physicalLinkType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinkType | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinkType | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}

	public void saveSiteNode(final SiteNode siteNode, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		try {
			database.update(siteNode, SessionContext.getAccessIdentity().getUserId(), 
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNode | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNode | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}

	public void saveSiteNodeType(final SiteNodeType siteNodeType, final boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			database.update(siteNodeType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodeType | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodeType | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}

	public void saveTopologicalNode(final TopologicalNode topologicalNode, final boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			database.update(topologicalNode, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveTopologicalNode | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveTopologicalNode | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		} 
	}

	public void saveCollectors(final Set list, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveCollectors | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveCollectors | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}

	public void saveMaps(final Set list, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMaps | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveMaps | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	public void saveMarks(final Set list, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveMarks | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveMarks | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	public void saveNodeLinks(final Set list, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		try {
			database.update(list,
				SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveNodeLinks | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveNodeLinks | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}

	public void savePhysicalLinks(final Set list, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinks | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinks | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}

	public void savePhysicalLinkTypes(final Set list, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.savePhysicalLinkTypes | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.savePhysicalLinkTypes | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}

	public void saveSiteNodes(final Set list, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		try {
			database.update(list,
				SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodes | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodes | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}

	public void saveSiteNodeTypes(final Set list, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveSiteNodeTypes | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveSiteNodeType | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}

	public void saveTopologicalNodes(final Set list, final boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMapObjectLoader.saveTopologicalNodes | UpdateObjectException: " + e.getMessage()); //$NON-NLS-1$
			throw new DatabaseException("DatabaseMapObjectLoader.saveTopologicalNodes | UpdateObjectException: " //$NON-NLS-1$
					+ e.getMessage());
		}
	}
}
