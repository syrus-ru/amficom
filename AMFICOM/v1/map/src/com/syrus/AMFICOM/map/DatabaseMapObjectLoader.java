/*-
 * $Id: DatabaseMapObjectLoader.java,v 1.14 2005/04/05 09:00:26 arseniy Exp $
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/04/05 09:00:26 $
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





	public Set loadCollectors(final Set ids) throws RetrieveObjectException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadCollectors | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadCollectors | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadMaps(final Set ids) throws RetrieveObjectException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMaps | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadMaps | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
		}
		return list;
	}

	public Set loadMarks(final Set ids) throws RetrieveObjectException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMarks | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadMarks | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadNodeLinks(final Set ids) throws RetrieveObjectException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadNodeLinks | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadNodeLinks | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadPhysicalLinks(final Set ids) throws RetrieveObjectException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinks | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadPhysicalLinks | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadPhysicalLinkTypes(final Set ids) throws RetrieveObjectException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinkTypes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadPhysicalLinkTypes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadSiteNodes(final Set ids) throws RetrieveObjectException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodes | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("loadSiteNodes.loadSiteNodes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadSiteNodeTypes(final Set ids) throws RetrieveObjectException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodeTypes | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("loadSiteNodes.loadSiteNodeTypes | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
		}
		return list;
	}	

	public Set loadTopologicalNodes(final Set ids) throws RetrieveObjectException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadTopologicalNodes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
			throw new RetrieveObjectException("loadSiteNodes.loadTopologicalNodes | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}





	public Set loadCollectorsButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadCollectorsButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadCollectorsButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadMapsButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		MapDatabase database = MapDatabaseContext.getMapDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMapsButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadMapsButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
		}
		return list;
	}

	public Set loadMarksButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		MarkDatabase database = MapDatabaseContext.getMarkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadMarksButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadMarksButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadNodeLinksButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		NodeLinkDatabase database = MapDatabaseContext.getNodeLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadNodeLinksButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadNodeLinksButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadPhysicalLinksButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		PhysicalLinkDatabase database = MapDatabaseContext.getPhysicalLinkDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinksButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadPhysicalLinksButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		PhysicalLinkTypeDatabase database = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadPhysicalLinkTypesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
			throw new RetrieveObjectException("DatabaseMapObjectLoader.loadPhysicalLinkTypesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}
	
	public Set loadSiteNodesButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		SiteNodeDatabase database = MapDatabaseContext.getSiteNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodesButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("loadSiteNodes.loadSiteNodesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
	}

	public Set loadSiteNodeTypesButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		SiteNodeTypeDatabase database = MapDatabaseContext.getSiteNodeTypeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadSiteNodeTypesButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
			throw new RetrieveObjectException("loadSiteNodes.loadSiteNodeTypesButIds | Illegal Storable Object: " + e.getMessage()); //$NON-NLS-1$
		}
		return list;
	}

	public Set loadTopologicalNodesButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		TopologicalNodeDatabase database = MapDatabaseContext.getTopologicalNodeDatabase();
		Set list = null;
		try {
			list = database.retrieveByIdsByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMapObjectLoader.loadTopologicalNodesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
			throw new RetrieveObjectException("loadSiteNodes.loadTopologicalNodesButIds | Illegal Storable Object: " //$NON-NLS-1$
					+ e.getMessage());
		}
		return list;
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





	public void delete(Identifier id) throws IllegalDataException {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = MapDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(Set objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		java.util.Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Set entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identifiable)
					identifier = ((Identifiable) object).getId();
				else
					throw new IllegalDataException("DatabaseMapObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identifiable");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Set) map.get(entityCode);
			storableObjectDatabase = MapDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
