/*
 * $Id: MapObjectLoader.java,v 1.7 2005/04/01 11:11:05 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.7 $, $Date: 2005/04/01 11:11:05 $
 * @author $Author: bob $
 * @module map_v1
 */
public interface MapObjectLoader {

	void delete(Identifier id) throws IllegalDataException;

	void delete(Set ids) throws IllegalDataException;

	Collector loadCollector(Identifier id) throws ApplicationException;

	Set loadCollectors(Set ids) throws ApplicationException;

	Map loadMap(Identifier id) throws ApplicationException;

	Set loadMaps(Set ids) throws ApplicationException;

	Mark loadMark(Identifier id) throws ApplicationException;

	Set loadMarks(Set ids) throws ApplicationException;

	NodeLink loadNodeLink(Identifier id) throws ApplicationException;

	Set loadNodeLinks(Set ids) throws ApplicationException;

	PhysicalLink loadPhysicalLink(Identifier id) throws ApplicationException;

	Set loadPhysicalLinks(Set ids) throws ApplicationException;

	PhysicalLinkType loadPhysicalLinkType(Identifier id) throws ApplicationException;

	Set loadPhysicalLinkTypes(Set ids) throws ApplicationException;

	SiteNode loadSiteNode(Identifier id) throws ApplicationException;

	Set loadSiteNodes(Set ids) throws ApplicationException;

	SiteNodeType loadSiteNodeType(Identifier id) throws ApplicationException;

	Set loadSiteNodeTypes(Set ids) throws ApplicationException;

	TopologicalNode loadTopologicalNode(Identifier id) throws ApplicationException;

	Set loadTopologicalNodes(Set ids) throws ApplicationException;

	Set loadCollectorsButIds(StorableObjectCondition condition,
									Set ids) throws ApplicationException;

	Set loadMapsButIds(	StorableObjectCondition condition,
								Set ids) throws ApplicationException;

	Set loadMarksButIds(	StorableObjectCondition condition,
								Set ids) throws ApplicationException;

	Set loadNodeLinksButIds(	StorableObjectCondition condition,
									Set ids) throws ApplicationException;

	Set loadPhysicalLinksButIds(	StorableObjectCondition condition,
										Set ids) throws ApplicationException;

	Set loadPhysicalLinkTypesButIds(	StorableObjectCondition condition,
											Set ids) throws ApplicationException;

	Set loadSiteNodesButIds(	StorableObjectCondition condition,
									Set ids) throws ApplicationException;

	Set loadSiteNodeTypesButIds(	StorableObjectCondition condition,
										Set ids) throws ApplicationException;

	Set loadTopologicalNodesButIds(	StorableObjectCondition condition,
											Set ids) throws ApplicationException;

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

	void saveCollectors(Set list,
						boolean force) throws ApplicationException;

	void saveMaps(	Set list,
					boolean force) throws ApplicationException;

	void saveMarks(	Set list,
					boolean force) throws ApplicationException;

	void saveNodeLinks(	Set list,
						boolean force) throws ApplicationException;

	void savePhysicalLinks(	Set list,
							boolean force) throws ApplicationException;

	void savePhysicalLinkTypes(	Set list,
								boolean force) throws ApplicationException;

	void saveSiteNodes(	Set list,
						boolean force) throws ApplicationException;

	void saveSiteNodeTypes(	Set list,
							boolean force) throws ApplicationException;

	void saveTopologicalNodes(	Set list,
								boolean force) throws ApplicationException;

}
