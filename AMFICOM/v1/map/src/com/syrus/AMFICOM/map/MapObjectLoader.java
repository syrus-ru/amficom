/*
 * $Id: MapObjectLoader.java,v 1.4 2005/02/14 10:30:56 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Collection;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/14 10:30:56 $
 * @author $Author: bob $
 * @module map_v1
 */
public interface MapObjectLoader {

	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(Collection ids) throws CommunicationException, DatabaseException;

	Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadCollectors(Collection ids) throws DatabaseException, CommunicationException;

	Map loadMap(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadMaps(Collection ids) throws DatabaseException, CommunicationException;

	Mark loadMark(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadMarks(Collection ids) throws DatabaseException, CommunicationException;

	NodeLink loadNodeLink(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadNodeLinks(Collection ids) throws DatabaseException, CommunicationException;

	PhysicalLink loadPhysicalLink(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadPhysicalLinks(Collection ids) throws DatabaseException, CommunicationException;

	PhysicalLinkType loadPhysicalLinkType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadPhysicalLinkTypes(Collection ids) throws DatabaseException, CommunicationException;

	SiteNode loadSiteNode(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadSiteNodes(Collection ids) throws DatabaseException, CommunicationException;

	SiteNodeType loadSiteNodeType(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadSiteNodeTypes(Collection ids) throws DatabaseException, CommunicationException;

	TopologicalNode loadTopologicalNode(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadTopologicalNodes(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadCollectorsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Collection loadMapsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	Collection loadMarksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	Collection loadNodeLinksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Collection loadPhysicalLinksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Collection loadPhysicalLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Collection loadSiteNodesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Collection loadSiteNodeTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	Collection loadTopologicalNodesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException;

	java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException;

	void saveCollector(Collector collector, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMap(Map map, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMark(Mark mark, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveNodeLink(NodeLink nodeLink, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePhysicalLink(PhysicalLink physicalLink, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void savePhysicalLinkType(PhysicalLinkType physicalLinkType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveSiteNode(SiteNode siteNode, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveSiteNodeType(SiteNodeType siteNodeType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveTopologicalNode(TopologicalNode topologicalNode, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException;

	void saveCollectors(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMaps(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMarks(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveNodeLinks(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePhysicalLinks(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePhysicalLinkTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveSiteNodes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveSiteNodeTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveTopologicalNodes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

}
