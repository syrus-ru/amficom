/*
 * $Id: MapObjectLoader.java,v 1.12 2005/06/22 19:24:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.12 $, $Date: 2005/06/22 19:24:59 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public interface MapObjectLoader {
	Set loadCollectors(final Set<Identifier> ids) throws ApplicationException;

	Set loadMaps(final Set<Identifier> ids) throws ApplicationException;

	Set loadMarks(final Set<Identifier> ids) throws ApplicationException;

	Set loadNodeLinks(final Set<Identifier> ids) throws ApplicationException;

	Set loadPhysicalLinks(final Set<Identifier> ids) throws ApplicationException;

	Set loadPhysicalLinkTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadSiteNodes(final Set<Identifier> ids) throws ApplicationException;

	Set loadSiteNodeTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadTopologicalNodes(final Set<Identifier> ids) throws ApplicationException;

	Set loadCollectorsButIds(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws ApplicationException;

	Set loadMapsButIds(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws ApplicationException;

	Set loadMarksButIds(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws ApplicationException;

	Set loadNodeLinksButIds(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws ApplicationException;

	Set loadPhysicalLinksButIds(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws ApplicationException;

	Set loadPhysicalLinkTypesButIds(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws ApplicationException;

	Set loadSiteNodesButIds(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws ApplicationException;

	Set loadSiteNodeTypesButIds(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws ApplicationException;

	Set loadTopologicalNodesButIds(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws ApplicationException;

	Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException;

	void saveCollectors(final Set<Collector> objects,
			final boolean force) throws ApplicationException;

	void saveMaps(final Set<Map> objects,
			final boolean force) throws ApplicationException;

	void saveMarks(final Set<Mark> objects,
			final boolean force) throws ApplicationException;

	void saveNodeLinks(final Set<NodeLink> objects,
			final boolean force) throws ApplicationException;

	void savePhysicalLinks(final Set<PhysicalLink> objects,
			final boolean force) throws ApplicationException;

	void savePhysicalLinkTypes(final Set<PhysicalLinkType> objects,
			final boolean force) throws ApplicationException;

	void saveSiteNodes(final Set<SiteNode> objects,
			final boolean force) throws ApplicationException;

	void saveSiteNodeTypes(final Set<SiteNodeType> objects,
			final boolean force) throws ApplicationException;

	void saveTopologicalNodes(final Set<TopologicalNode> objects,
			final boolean force) throws ApplicationException;

	void delete(final Set<? extends Identifiable> objects);
}
