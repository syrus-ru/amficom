/*
* $Id: EmptyClientMapObjectLoader.java,v 1.3 2005/02/21 11:11:15 bob Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.map;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;


/**
 * @version $Revision: 1.3 $, $Date: 2005/02/21 11:11:15 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class EmptyClientMapObjectLoader implements MapObjectLoader {

	public void delete(Identifier id) throws IllegalDataException {
		// nothing
	}

	public void delete(Collection ids) throws IllegalDataException {
		//		 nothing
	}

	public Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadCollectors(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
	}

	public Map loadMap(Identifier id) throws DatabaseException, CommunicationException {
		return null;
	}

	public Collection loadMaps(Collection ids) throws DatabaseException, CommunicationException {
		return Collections.EMPTY_LIST;
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
		return Collections.EMPTY_LIST;
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

	public void saveCollectors(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

	}

	public void saveMaps(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		// empty

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
