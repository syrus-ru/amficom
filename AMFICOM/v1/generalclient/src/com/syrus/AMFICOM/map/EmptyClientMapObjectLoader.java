/*
* $Id: EmptyClientMapObjectLoader.java,v 1.4 2005/03/01 16:12:42 krupenn Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.map;

import java.util.Collection;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * @version $Revision: 1.4 $, $Date: 2005/03/01 16:12:42 $
 * @author $Author: krupenn $
 * @module generalclient_v1
 */
public class EmptyClientMapObjectLoader implements MapObjectLoader {

	private java.util.Map localHash = new HashMap();

	public void delete(Identifier id) throws IllegalDataException {
		this.localHash.remove(id);
	}

	public void delete(Collection ids) throws IllegalDataException {
		for(Iterator it = ids.iterator(); it.hasNext();)
		{
			this.localHash.remove(it.next());
		}
	}

	public Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadCollectors(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Map loadMap(Identifier id) throws DatabaseException, CommunicationException {
		return (Map )this.localHash.get(id);
	}

	public Collection loadMaps(Collection ids) throws DatabaseException, CommunicationException {
		return Arrays.asList(this.localHash.values().toArray());
	}

	public Mark loadMark(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadMarks(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public NodeLink loadNodeLink(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadNodeLinks(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public PhysicalLink loadPhysicalLink(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadPhysicalLinks(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public PhysicalLinkType loadPhysicalLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadPhysicalLinkTypes(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public SiteNode loadSiteNode(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadSiteNodes(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public SiteNodeType loadSiteNodeType(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadSiteNodeTypes(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public TopologicalNode loadTopologicalNode(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadTopologicalNodes(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Collection loadCollectorsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Collection loadMapsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		Collection objects = new LinkedList();
		for(Iterator it = this.localHash.keySet().iterator(); it.hasNext();)
		{
			Identifier id = (Identifier)it.next();
			if(!ids.contains(id))
				objects.add(this.localHash.get(id));
		}
		return objects;
	}

	public Collection loadMarksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Collection loadNodeLinksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Collection loadPhysicalLinksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Collection loadPhysicalLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Collection loadSiteNodesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Collection loadSiteNodeTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Collection loadTopologicalNodesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		return storableObjects;
	}

	public void saveCollector(Collector collector, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty
	}

	public void saveMap(Map map, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.localHash.put(map.getId(), map);
	}

	public void saveMark(Mark mark, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveNodeLink(NodeLink nodeLink, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void savePhysicalLink(PhysicalLink physicalLink, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		// empty

	}
	
	public void savePhysicalLinkType(PhysicalLinkType physicalLinkType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		// empty

	}

	public void saveSiteNode(SiteNode siteNode, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveSiteNodeType(SiteNodeType siteNodeType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		// empty

	}

	public void saveTopologicalNode(TopologicalNode topologicalNode, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		// empty

	}

	public void saveCollectors(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveMaps(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		for(Iterator it = list.iterator(); it.hasNext();)
		{
			Map map = (Map )it.next();
			this.localHash.put(map.getId(), map);
		}

	}

	public void saveMarks(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveNodeLinks(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void savePhysicalLinks(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void savePhysicalLinkTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveSiteNodes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveSiteNodeTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveTopologicalNodes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

}
