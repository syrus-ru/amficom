/*-
 * $Id: DatabaseMapObjectLoader.java,v 1.5 2005/05/24 13:24:58 bass Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/24 13:24:58 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class DatabaseMapObjectLoader extends DatabaseObjectLoader implements MapObjectLoader {
	public Set loadCollectors(final Set ids) throws ApplicationException {
		CollectorDatabase database = (CollectorDatabase) DatabaseContext.getDatabase(ObjectEntities.COLLECTOR_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMaps(final Set ids) throws ApplicationException {
		MapDatabase database = (MapDatabase) DatabaseContext.getDatabase(ObjectEntities.MAP_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMarks(final Set ids) throws ApplicationException {
		MarkDatabase database = (MarkDatabase) DatabaseContext.getDatabase(ObjectEntities.MARK_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadNodeLinks(final Set ids) throws ApplicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.NODE_LINK_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadPhysicalLinks(final Set ids) throws ApplicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadPhysicalLinkTypes(final Set ids) throws ApplicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadSiteNodes(final Set ids) throws ApplicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) DatabaseContext.getDatabase(ObjectEntities.SITE_NODE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadSiteNodeTypes(final Set ids) throws ApplicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}	

	public Set loadTopologicalNodes(final Set ids) throws ApplicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) DatabaseContext.getDatabase(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}





	public Set loadCollectorsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		CollectorDatabase database = (CollectorDatabase) DatabaseContext.getDatabase(ObjectEntities.COLLECTOR_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMapsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		MapDatabase database = (MapDatabase) DatabaseContext.getDatabase(ObjectEntities.MAP_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMarksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		MarkDatabase database = (MarkDatabase) DatabaseContext.getDatabase(ObjectEntities.MARK_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadNodeLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.NODE_LINK_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPhysicalLinksButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}
	
	public Set loadSiteNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) DatabaseContext.getDatabase(ObjectEntities.SITE_NODE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadSiteNodeTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadTopologicalNodesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) DatabaseContext.getDatabase(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public void saveCollectors(final Set list, final boolean force) throws ApplicationException {
		CollectorDatabase database = (CollectorDatabase) DatabaseContext.getDatabase(ObjectEntities.COLLECTOR_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMaps(final Set list, final boolean force) throws ApplicationException {
		MapDatabase database = (MapDatabase) DatabaseContext.getDatabase(ObjectEntities.MAP_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMarks(final Set list, final boolean force) throws ApplicationException {
		MarkDatabase database = (MarkDatabase) DatabaseContext.getDatabase(ObjectEntities.MARK_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveNodeLinks(final Set list, final boolean force) throws ApplicationException {
		NodeLinkDatabase database = (NodeLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.NODE_LINK_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePhysicalLinks(final Set list, final boolean force) throws ApplicationException {
		PhysicalLinkDatabase database = (PhysicalLinkDatabase) DatabaseContext.getDatabase(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePhysicalLinkTypes(final Set list, final boolean force) throws ApplicationException {
		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSiteNodes(final Set list, final boolean force) throws ApplicationException {
		SiteNodeDatabase database = (SiteNodeDatabase) DatabaseContext.getDatabase(ObjectEntities.SITE_NODE_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSiteNodeTypes(final Set list, final boolean force) throws ApplicationException {
		SiteNodeTypeDatabase database = (SiteNodeTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTopologicalNodes(final Set list, final boolean force) throws ApplicationException {
		TopologicalNodeDatabase database = (TopologicalNodeDatabase) DatabaseContext.getDatabase(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE);
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}





	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
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
			final StorableObjectDatabase storableObjectDatabase = DatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
