/*
 * $Id: TopLevelCableLink.java,v 1.1 2005/04/05 14:07:54 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Point;
import java.util.*;

import com.jgraph.graph.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:54 $
 * @module schemeclient_v1
 */

public class TopLevelCableLink extends DefaultEdge {
	public static TopLevelCableLink createInstance(Object userObject,
			Port firstPort, Port port, Point p, Point p2, Map viewMap,
			ConnectionSet cs) {
		TopLevelCableLink cell = new TopLevelCableLink(userObject);
		
		Map map = GraphConstants.createMap();

		// GraphConstants.setRouting(map, GraphConstants.ROUTING_SIMPLE);

		ArrayList list = new ArrayList();
		list.add(p);
		list.add(p2);

		GraphConstants.setPoints(map, list);
		GraphConstants.setBendable(map, true);

		viewMap.put(cell, map);
		if (firstPort != null)
			cs.connect(cell, firstPort, true);
		if (port != null)
			cs.connect(cell, port, false);
		
		return cell;
	}
	
	private TopLevelCableLink(Object userObject) {
		super(userObject);
	}
}

