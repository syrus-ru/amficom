/*
 * $Id: MapDatabaseContext.java,v 1.2 2004/11/26 09:24:36 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.2 $, $Date: 2004/11/26 09:24:36 $
 * @author $Author: bob $
 * @module map_v1
 */
public class MapDatabaseContext {

	protected static StorableObjectDatabase	siteNodeTypeDatabase;
	protected static StorableObjectDatabase	physicalLinkTypeDatabase;

	protected static StorableObjectDatabase	collectorDatabase;
	protected static StorableObjectDatabase	mapDatabase;
	protected static StorableObjectDatabase	markDatabase;
	protected static StorableObjectDatabase	nodeLinkDatabase;
	protected static StorableObjectDatabase	physicalLinkDatabase;
	protected static StorableObjectDatabase	siteNodeDatabase;
	protected static StorableObjectDatabase	topologicalNodeDatabase;
}
