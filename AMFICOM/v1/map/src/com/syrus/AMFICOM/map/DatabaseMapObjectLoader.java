/*-
 * $Id: DatabaseMapObjectLoader.java,v 1.17 2005/04/12 16:23:33 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.AbstractObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.17 $, $Date: 2005/04/12 16:23:33 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public class DatabaseMapObjectLoader extends AbstractObjectLoader implements MapObjectLoader {

	public Collector loadCollector(final Identifier id) throws ApplicationException {
		return new Collector(id);
	}

	public Map loadMap(final Identifier id) throws ApplicationException {
		return new Map(id);
	}

	public Mark loadMark(final Identifier id) throws ApplicationException {
		return new Mark(id);
	}

	public NodeLink loadNodeLink(final Identifier id) throws ApplicationException {
		return new NodeLink(id);
	}

	public PhysicalLink loadPhysicalLink(final Identifier id) throws ApplicationException {
		return new PhysicalLink(id);
	}

	public PhysicalLinkType loadPhysicalLinkType(final Identifier id) throws ApplicationException {
		return new PhysicalLinkType(id);
	}

	public SiteNode loadSiteNode(final Identifier id) throws ApplicationException {
		return new SiteNode(id);
	}

	public SiteNodeType loadSiteNodeType(final Identifier id) throws ApplicationException {
		return new SiteNodeType(id);
	}

	public TopologicalNode loadTopologicalNode(final Identifier id) throws ApplicationException {
		return new TopologicalNode(id);
	}





	public Set loadCollectors(final Set ids) throws ApplicationException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMaps(final Set ids) throws ApplicationException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMarks(final Set ids) throws ApplicationException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadNodeLinks(final Set ids) throws ApplicationException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadPhysicalLinks(final Set ids) throws ApplicationException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadPhysicalLinkTypes(final Set ids) throws ApplicationException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadSiteNodes(final Set ids) throws ApplicationException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadSiteNodeTypes(final Set ids) throws ApplicationException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}	

	public Set loadTopologicalNodes(final Set ids) throws ApplicationException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}





	public Set loadCollectorsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMapsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMarksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadNodeLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPhysicalLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}
	
	public Set loadSiteNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadSiteNodeTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadTopologicalNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}





	public void saveCollector(final Collector collector, final boolean force) throws ApplicationException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		database.update(collector, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMap(final Map map, final boolean force) throws ApplicationException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		database.update(map, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMark(final Mark mark, final boolean force) throws ApplicationException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		database.update(mark, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveNodeLink(final NodeLink nodeLink, final boolean force) throws ApplicationException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		database.update(nodeLink, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePhysicalLink(final PhysicalLink physicalLink, final boolean force) throws ApplicationException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		database.update(physicalLink, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePhysicalLinkType(final PhysicalLinkType physicalLinkType, final boolean force) throws ApplicationException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		database.update(physicalLinkType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSiteNode(final SiteNode siteNode, final boolean force) throws ApplicationException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		database.update(siteNode, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSiteNodeType(final SiteNodeType siteNodeType, final boolean force) throws ApplicationException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		database.update(siteNodeType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTopologicalNode(final TopologicalNode topologicalNode, final boolean force) throws ApplicationException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		database.update(topologicalNode, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}





	public void saveCollectors(final Set list, final boolean force) throws ApplicationException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMaps(final Set list, final boolean force) throws ApplicationException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMarks(final Set list, final boolean force) throws ApplicationException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveNodeLinks(final Set list, final boolean force) throws ApplicationException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePhysicalLinks(final Set list, final boolean force) throws ApplicationException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePhysicalLinkTypes(final Set list, final boolean force) throws ApplicationException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSiteNodes(final Set list, final boolean force) throws ApplicationException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSiteNodeTypes(final Set list, final boolean force) throws ApplicationException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTopologicalNodes(final Set list, final boolean force) throws ApplicationException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}





	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = MapDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}





	public void delete(Identifier id) {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = MapDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(final Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;
		/**
		 * @todo: use Trove collection instead java.util.Map
		 */
		final java.util.Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifiable identifiable = (Identifiable) identifiableIterator.next();
			final Short entityCode = new Short(identifiable.getId().getMajor());
			Set entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(identifiable);
		}

		for (final Iterator entityCodeIterator = map.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			final Set entityObjects = (Set) map.get(entityCode);
			final StorableObjectDatabase storableObjectDatabase = MapDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
