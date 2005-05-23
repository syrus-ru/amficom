/*-
 * $Id: MapDatabaseContext.java,v 1.7 2005/05/23 18:45:17 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/05/23 18:45:17 $
 * @author Vladimir ``Bob'' Dolzhenko
 * @author $Author: bass $
 * @module map_v1
 */
public final class MapDatabaseContext {
	private static SiteNodeTypeDatabase	siteNodeTypeDatabase;
	private static PhysicalLinkTypeDatabase	physicalLinkTypeDatabase;

	private static CollectorDatabase	collectorDatabase;
	private static MapDatabase		mapDatabase;
	private static MarkDatabase		markDatabase;
	private static NodeLinkDatabase		nodeLinkDatabase;
	private static PhysicalLinkDatabase	physicalLinkDatabase;
	private static SiteNodeDatabase		siteNodeDatabase;
	private static TopologicalNodeDatabase	topologicalNodeDatabase;

	private MapDatabaseContext() {
		assert false;
	}

	public static void init(
			final SiteNodeTypeDatabase	siteNodeTypeDatabase1,
			final PhysicalLinkTypeDatabase	physicalLinkTypeDatabase1,
			final CollectorDatabase		collectorDatabase1,
			final MapDatabase		mapDatabase1,
			final MarkDatabase		markDatabase1,
			final NodeLinkDatabase		nodeLinkDatabase1,
			final PhysicalLinkDatabase	physicalLinkDatabase1,
			final SiteNodeDatabase		siteNodeDatabase1,
			final TopologicalNodeDatabase	topologicalNodeDatabase1) {
		if (siteNodeTypeDatabase1 != null)
			siteNodeTypeDatabase = siteNodeTypeDatabase1;
		if (physicalLinkTypeDatabase1 != null)
			physicalLinkTypeDatabase = physicalLinkTypeDatabase1;

		if (collectorDatabase1 != null)
			collectorDatabase = collectorDatabase1;
		if (mapDatabase1 != null)
			mapDatabase = mapDatabase1;
		if (markDatabase1 != null)
			markDatabase = markDatabase1;
		if (nodeLinkDatabase1 != null)
			nodeLinkDatabase = nodeLinkDatabase1;
		if (physicalLinkDatabase1 != null)
			physicalLinkDatabase = physicalLinkDatabase1;
		if (siteNodeDatabase1 != null)
			siteNodeDatabase = siteNodeDatabase1;
		if (topologicalNodeDatabase1 != null)
			topologicalNodeDatabase = topologicalNodeDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode) {
		switch (entityCode) {
			case ObjectEntities.COLLECTOR_ENTITY_CODE:
				return collectorDatabase;
			case ObjectEntities.MAP_ENTITY_CODE:
				return mapDatabase;
			case ObjectEntities.MARK_ENTITY_CODE:
				return markDatabase;
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				return nodeLinkDatabase;
			case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
				return physicalLinkDatabase;
			case ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE:
				return physicalLinkTypeDatabase;
			case ObjectEntities.SITE_NODE_ENTITY_CODE:
				return siteNodeDatabase;
			case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
				return siteNodeTypeDatabase;
			case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
				return topologicalNodeDatabase;
			default:
				Log.errorMessage("MapDatabaseContext.getDatabase | Unknown entity: " + entityCode);
				return null;
		}
	}
}
