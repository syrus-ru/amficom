/*
* $Id: EmptyClientMapObjectLoader.java,v 1.1 2004/12/27 14:15:37 bob Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;


/**
 * @version $Revision: 1.1 $, $Date: 2004/12/27 14:15:37 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class EmptyClientMapObjectLoader implements MapObjectLoader {

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		// nothing
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
		//		 nothing
	}

	public Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public List loadCollectors(List ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Map loadMap(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public List loadMaps(List ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Mark loadMark(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public List loadMarks(List ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public NodeLink loadNodeLink(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public List loadNodeLinks(List ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public PhysicalLink loadPhysicalLink(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public List loadPhysicalLinks(List ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public PhysicalLinkType loadPhysicalLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public List loadPhysicalLinkTypes(List ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public SiteNode loadSiteNode(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public List loadSiteNodes(List ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public SiteNodeType loadSiteNodeType(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public List loadSiteNodeTypes(List ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public TopologicalNode loadTopologicalNode(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public List loadTopologicalNodes(List ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public List loadCollectorsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public List loadMapsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public List loadMarksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public List loadNodeLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public List loadPhysicalLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public List loadPhysicalLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public List loadSiteNodesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public List loadSiteNodeTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public List loadTopologicalNodesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		return Collections.EMPTY_SET;
	}

	public void saveCollector(Collector collector, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty
	}

	public void saveMap(Map map, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

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

	public void saveCollectors(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveMaps(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveMarks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveNodeLinks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void savePhysicalLinks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void savePhysicalLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveSiteNodes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveSiteNodeTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveTopologicalNodes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

}
