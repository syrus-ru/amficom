/*-
 * $Id: EmptyClientMapObjectLoader.java,v 1.6 2005/04/04 14:05:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.6 $, $Date: 2005/04/04 14:05:37 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class EmptyClientMapObjectLoader implements MapObjectLoader {

	private java.util.Map	localHash	= new HashMap();

	public void delete(Identifier id) throws IllegalDataException {
		this.localHash.remove(id);
	}

	public void delete(Set ids) throws IllegalDataException {
		for (Iterator it = ids.iterator(); it.hasNext();) {
			this.localHash.remove(it.next());
		}
	}

	public Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Set loadCollectors(Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Map loadMap(Identifier id) throws DatabaseException, CommunicationException {
		return (Map) this.localHash.get(id);
	}

	public Set loadMaps(Set ids) throws DatabaseException, CommunicationException {
		return new HashSet(this.localHash.values());
	}

	public Mark loadMark(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Set loadMarks(Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public NodeLink loadNodeLink(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Set loadNodeLinks(Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public PhysicalLink loadPhysicalLink(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Set loadPhysicalLinks(Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public PhysicalLinkType loadPhysicalLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Set loadPhysicalLinkTypes(Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public SiteNode loadSiteNode(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Set loadSiteNodes(Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public SiteNodeType loadSiteNodeType(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Set loadSiteNodeTypes(Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public TopologicalNode loadTopologicalNode(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Set loadTopologicalNodes(Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadCollectorsButIds(StorableObjectCondition condition,
									Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadMapsButIds(	StorableObjectCondition condition,
								Set ids) throws DatabaseException, CommunicationException {
		Set objects = new HashSet();
		for (Iterator it = this.localHash.keySet().iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			if (!ids.contains(id))
				objects.add(this.localHash.get(id));
		}
		return objects;
	}

	public Set loadMarksButIds(	StorableObjectCondition condition,
								Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadNodeLinksButIds(	StorableObjectCondition condition,
									Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadPhysicalLinksButIds(	StorableObjectCondition condition,
										Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadPhysicalLinkTypesButIds(	StorableObjectCondition condition,
											Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadSiteNodesButIds(	StorableObjectCondition condition,
									Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadSiteNodeTypesButIds(	StorableObjectCondition condition,
										Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadTopologicalNodesButIds(	StorableObjectCondition condition,
											Set ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_SET;
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		return storableObjects;
	}

	public void saveCollector(	Collector collector,
								boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty
	}

	public void saveMap(Map map,
						boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		this.localHash.put(map.getId(), map);
	}

	public void saveMark(	Mark mark,
							boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		// empty

	}

	public void saveNodeLink(	NodeLink nodeLink,
								boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void savePhysicalLink(	PhysicalLink physicalLink,
									boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void savePhysicalLinkType(	PhysicalLinkType physicalLinkType,
										boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveSiteNode(	SiteNode siteNode,
								boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveSiteNodeType(	SiteNodeType siteNodeType,
									boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveTopologicalNode(TopologicalNode topologicalNode,
									boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveCollectors(	Set set,
								boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveMaps(	Set set,
							boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		for (Iterator it = set.iterator(); it.hasNext();) {
			Map map = (Map) it.next();
			this.localHash.put(map.getId(), map);
		}

	}

	public void saveMarks(	Set set,
							boolean force) throws VersionCollisionException, DatabaseException, CommunicationException {
		// empty

	}

	public void saveNodeLinks(	Set set,
								boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void savePhysicalLinks(	Set set,
									boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void savePhysicalLinkTypes(	Set set,
										boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveSiteNodes(	Set set,
								boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveSiteNodeTypes(	Set set,
									boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveTopologicalNodes(	Set set,
										boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

}
