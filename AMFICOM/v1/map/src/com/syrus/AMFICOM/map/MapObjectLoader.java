/*
 * $Id: MapObjectLoader.java,v 1.3 2004/12/07 08:20:30 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.List;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/07 08:20:30 $
 * @author $Author: bob $
 * @module map_v1
 */
public interface MapObjectLoader {

	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(List ids) throws CommunicationException, DatabaseException;

	Collector loadCollector(Identifier id) throws DatabaseException, CommunicationException;

	List loadCollectors(List ids) throws DatabaseException, CommunicationException;

	Map loadMap(Identifier id) throws DatabaseException, CommunicationException;

	List loadMaps(List ids) throws DatabaseException, CommunicationException;

	Mark loadMark(Identifier id) throws DatabaseException, CommunicationException;

	List loadMarks(List ids) throws DatabaseException, CommunicationException;

	NodeLink loadNodeLink(Identifier id) throws DatabaseException, CommunicationException;

	List loadNodeLinks(List ids) throws DatabaseException, CommunicationException;

	PhysicalLink loadPhysicalLink(Identifier id) throws DatabaseException, CommunicationException;

	List loadPhysicalLinks(List ids) throws DatabaseException, CommunicationException;

	PhysicalLinkType loadPhysicalLinkType(Identifier id) throws DatabaseException, CommunicationException;

	List loadPhysicalLinkTypes(List ids) throws DatabaseException, CommunicationException;

	SiteNode loadSiteNode(Identifier id) throws DatabaseException, CommunicationException;

	List loadSiteNodes(List ids) throws DatabaseException, CommunicationException;

	SiteNodeType loadSiteNodeType(Identifier id) throws DatabaseException, CommunicationException;

	List loadSiteNodeTypes(List ids) throws DatabaseException, CommunicationException;

	TopologicalNode loadTopologicalNode(Identifier id) throws DatabaseException, CommunicationException;

	List loadTopologicalNodes(List ids) throws DatabaseException, CommunicationException;

	List loadCollectorsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	List loadMapsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadMarksButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadNodeLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	List loadPhysicalLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	List loadPhysicalLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	List loadSiteNodesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	List loadSiteNodeTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException;

	List loadTopologicalNodesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
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

	void saveCollectors(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveMaps(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMarks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveNodeLinks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePhysicalLinks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void savePhysicalLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveSiteNodes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveSiteNodeTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

	void saveTopologicalNodes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException;

}
