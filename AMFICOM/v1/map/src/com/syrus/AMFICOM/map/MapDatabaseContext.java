/*
 * $Id: MapDatabaseContext.java,v 1.3 2004/12/01 16:16:03 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/01 16:16:03 $
 * @author $Author: bob $
 * @module map_v1
 */
public class MapDatabaseContext {

	private static StorableObjectDatabase	siteNodeTypeDatabase;
	private static StorableObjectDatabase	physicalLinkTypeDatabase;

	private static StorableObjectDatabase	collectorDatabase;
	private static StorableObjectDatabase	mapDatabase;
	private static StorableObjectDatabase	markDatabase;
	private static StorableObjectDatabase	nodeLinkDatabase;
	private static StorableObjectDatabase	physicalLinkDatabase;
	private static StorableObjectDatabase	siteNodeDatabase;
	private static StorableObjectDatabase	topologicalNodeDatabase;

	private MapDatabaseContext() {
		// empty singleton constructor
	}

	public static void init(StorableObjectDatabase siteNodeTypeDatabase,
							StorableObjectDatabase physicalLinkTypeDatabase,
							StorableObjectDatabase collectorDatabase,
							StorableObjectDatabase mapDatabase,
							StorableObjectDatabase markDatabase,
							StorableObjectDatabase nodeLinkDatabase,
							StorableObjectDatabase physicalLinkDatabase,
							StorableObjectDatabase siteNodeDatabase,
							StorableObjectDatabase topologicalNodeDatabase) {
		MapDatabaseContext.siteNodeTypeDatabase = siteNodeTypeDatabase;
		MapDatabaseContext.physicalLinkTypeDatabase = physicalLinkTypeDatabase;

		MapDatabaseContext.collectorDatabase = collectorDatabase;
		MapDatabaseContext.mapDatabase = mapDatabase;
		MapDatabaseContext.markDatabase = markDatabase;
		MapDatabaseContext.nodeLinkDatabase = nodeLinkDatabase;
		MapDatabaseContext.physicalLinkDatabase = physicalLinkDatabase;
		MapDatabaseContext.siteNodeDatabase = siteNodeDatabase;
		MapDatabaseContext.topologicalNodeDatabase = topologicalNodeDatabase;

	}

	public static StorableObjectDatabase getCollectorDatabase() {
		return collectorDatabase;
	}

	public static StorableObjectDatabase getMapDatabase() {
		return mapDatabase;
	}

	public static StorableObjectDatabase getMarkDatabase() {
		return markDatabase;
	}

	public static StorableObjectDatabase getNodeLinkDatabase() {
		return nodeLinkDatabase;
	}

	public static StorableObjectDatabase getPhysicalLinkDatabase() {
		return physicalLinkDatabase;
	}

	public static StorableObjectDatabase getPhysicalLinkTypeDatabase() {
		return physicalLinkTypeDatabase;
	}

	public static StorableObjectDatabase getSiteNodeDatabase() {
		return siteNodeDatabase;
	}

	public static StorableObjectDatabase getSiteNodeTypeDatabase() {
		return siteNodeTypeDatabase;
	}

	public static StorableObjectDatabase getTopologicalNodeDatabase() {
		return topologicalNodeDatabase;
	}
}
