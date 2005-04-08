/*-
 * $Id: MapStorableObjectFactory.java,v 1.1 2005/04/08 09:32:27 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;
import com.syrus.AMFICOM.map.corba.Map_Transferable;
import com.syrus.AMFICOM.map.corba.Mark_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNodeType_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/04/08 09:32:27 $
 * @module mshserver_v1
 */
public abstract class MapStorableObjectFactory {
	protected final SiteNode newSiteNode(final SiteNode_Transferable siteNode) throws CreateObjectException {
		return new SiteNode(siteNode);
	}

	protected final TopologicalNode newTopologicalNode(final TopologicalNode_Transferable topologicalNode) throws CreateObjectException {
		return new TopologicalNode(topologicalNode);
	}

	protected final NodeLink newNodeLink(final NodeLink_Transferable nodeLink) throws CreateObjectException {
		return new NodeLink(nodeLink);
	}

	protected final Mark newMark(final Mark_Transferable mark) throws CreateObjectException {
		return new Mark(mark);
	}

	protected final PhysicalLink newPhysicalLink(final PhysicalLink_Transferable physicalLink) throws CreateObjectException {
		return new PhysicalLink(physicalLink);
	}

	protected final Collector newCollector(final Collector_Transferable collector) throws CreateObjectException {
		return new Collector(collector);
	}

	protected final Map newMap(final Map_Transferable map) throws CreateObjectException {
		return new Map(map);
	}

	protected final SiteNodeType newSiteNodeType(final SiteNodeType_Transferable siteNodeType) throws CreateObjectException {
		return new SiteNodeType(siteNodeType);
	}

	protected final PhysicalLinkType newPhysicalLinkType(final PhysicalLinkType_Transferable physicalLinkType) throws CreateObjectException {
		return new PhysicalLinkType(physicalLinkType);
	}
}
