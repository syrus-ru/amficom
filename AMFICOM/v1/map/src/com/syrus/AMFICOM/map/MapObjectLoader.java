/*
 * $Id: MapObjectLoader.java,v 1.6 2005/02/24 15:47:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Collection;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/24 15:47:37 $
 * @author $Author: bob $
 * @module map_v1
 */
public interface MapObjectLoader {

	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection ids) throws IllegalDataException;

	Collector loadCollector(Identifier id) throws ApplicationException;

	Collection loadCollectors(Collection ids) throws ApplicationException;

	Map loadMap(Identifier id) throws ApplicationException;

	Collection loadMaps(Collection ids) throws ApplicationException;

	Mark loadMark(Identifier id) throws ApplicationException;

	Collection loadMarks(Collection ids) throws ApplicationException;

	NodeLink loadNodeLink(Identifier id) throws ApplicationException;

	Collection loadNodeLinks(Collection ids) throws ApplicationException;

	PhysicalLink loadPhysicalLink(Identifier id) throws ApplicationException;

	Collection loadPhysicalLinks(Collection ids) throws ApplicationException;

	PhysicalLinkType loadPhysicalLinkType(Identifier id) throws ApplicationException;

	Collection loadPhysicalLinkTypes(Collection ids) throws ApplicationException;

	SiteNode loadSiteNode(Identifier id) throws ApplicationException;

	Collection loadSiteNodes(Collection ids) throws ApplicationException;

	SiteNodeType loadSiteNodeType(Identifier id) throws ApplicationException;

	Collection loadSiteNodeTypes(Collection ids) throws ApplicationException;

	TopologicalNode loadTopologicalNode(Identifier id) throws ApplicationException;

	Collection loadTopologicalNodes(Collection ids) throws ApplicationException;

	Collection loadCollectorsButIds(StorableObjectCondition condition,
									Collection ids) throws ApplicationException;

	Collection loadMapsButIds(	StorableObjectCondition condition,
								Collection ids) throws ApplicationException;

	Collection loadMarksButIds(	StorableObjectCondition condition,
								Collection ids) throws ApplicationException;

	Collection loadNodeLinksButIds(	StorableObjectCondition condition,
									Collection ids) throws ApplicationException;

	Collection loadPhysicalLinksButIds(	StorableObjectCondition condition,
										Collection ids) throws ApplicationException;

	Collection loadPhysicalLinkTypesButIds(	StorableObjectCondition condition,
											Collection ids) throws ApplicationException;

	Collection loadSiteNodesButIds(	StorableObjectCondition condition,
									Collection ids) throws ApplicationException;

	Collection loadSiteNodeTypesButIds(	StorableObjectCondition condition,
										Collection ids) throws ApplicationException;

	Collection loadTopologicalNodesButIds(	StorableObjectCondition condition,
											Collection ids) throws ApplicationException;

	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;

	void saveCollector(	Collector collector,
						boolean force) throws ApplicationException;

	void saveMap(	Map map,
					boolean force) throws ApplicationException;

	void saveMark(	Mark mark,
					boolean force) throws ApplicationException;

	void saveNodeLink(	NodeLink nodeLink,
						boolean force) throws ApplicationException;

	void savePhysicalLink(	PhysicalLink physicalLink,
							boolean force) throws ApplicationException;

	void savePhysicalLinkType(	PhysicalLinkType physicalLinkType,
								boolean force) throws ApplicationException;

	void saveSiteNode(	SiteNode siteNode,
						boolean force) throws ApplicationException;

	void saveSiteNodeType(	SiteNodeType siteNodeType,
							boolean force) throws ApplicationException;

	void saveTopologicalNode(	TopologicalNode topologicalNode,
								boolean force) throws ApplicationException;

	void saveCollectors(Collection list,
						boolean force) throws ApplicationException;

	void saveMaps(	Collection list,
					boolean force) throws ApplicationException;

	void saveMarks(	Collection list,
					boolean force) throws ApplicationException;

	void saveNodeLinks(	Collection list,
						boolean force) throws ApplicationException;

	void savePhysicalLinks(	Collection list,
							boolean force) throws ApplicationException;

	void savePhysicalLinkTypes(	Collection list,
								boolean force) throws ApplicationException;

	void saveSiteNodes(	Collection list,
						boolean force) throws ApplicationException;

	void saveSiteNodeTypes(	Collection list,
							boolean force) throws ApplicationException;

	void saveTopologicalNodes(	Collection list,
								boolean force) throws ApplicationException;

}
