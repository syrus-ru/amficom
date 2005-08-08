/*
 * $Id: TopLevelCableLink.java,v 1.5 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.Port;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class TopLevelCableLink extends DefaultEdge {
	private static final long serialVersionUID = 3904681565761255735L;

	public static TopLevelCableLink createInstance(Object userObject,
			Port firstPort, Port port, Point p, Point p2, Map<Object, Map> viewMap,
			ConnectionSet cs) {
		TopLevelCableLink cell = new TopLevelCableLink(userObject);
		
		Map map = GraphConstants.createMap();

		// GraphConstants.setRouting(map, GraphConstants.ROUTING_SIMPLE);

		ArrayList<Point> list = new ArrayList<Point>();
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

