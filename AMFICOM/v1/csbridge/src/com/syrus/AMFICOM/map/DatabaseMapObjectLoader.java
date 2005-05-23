/*-
 * $Id: DatabaseMapObjectLoader.java,v 1.3 2005/05/23 13:51:17 bass Exp $
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
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/23 13:51:17 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class DatabaseMapObjectLoader extends DatabaseObjectLoader implements MapObjectLoader {
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

	public void saveCollectors(final Set list, final boolean force) throws ApplicationException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMaps(final Set list, final boolean force) throws ApplicationException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMarks(final Set list, final boolean force) throws ApplicationException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveNodeLinks(final Set list, final boolean force) throws ApplicationException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePhysicalLinks(final Set list, final boolean force) throws ApplicationException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void savePhysicalLinkTypes(final Set list, final boolean force) throws ApplicationException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSiteNodes(final Set list, final boolean force) throws ApplicationException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSiteNodeTypes(final Set list, final boolean force) throws ApplicationException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTopologicalNodes(final Set list, final boolean force) throws ApplicationException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		database.update(list, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
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
