/*
 * $Id: PortEdge.java,v 1.9 2005/10/17 14:59:15 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.Port;

/**
 * used just for identification of link between PortCell and DeviceCell
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2005/10/17 14:59:15 $
 * @module schemeclient
 */

public class PortEdge extends DefaultEdge {
	private static final long serialVersionUID = 3257847662660628537L;

	public static PortEdge createInstance(Object userObject, Port devicePort,
			Port ellipsePort, Point p, Point p2, Point labelPosition, Map viewMap,
			ConnectionSet cs) {
		PortEdge cell = new PortEdge(userObject); 
		
//		UIDefaults defaults = UIManager.getDefaults();
//		Font f = defaults.getFont("Label.font");
//		if (f == null)
		Font f = new Font("Dialog", Font.PLAIN, 12);
		
		Map map = new HashMap();
		List list = new ArrayList();
		list.add(p);
		list.add(p2);
		
		GraphConstants.setPoints(map, list);
		//GraphConstants.setRouting(map, GraphConstants.ROUTING_SIMPLE);
		GraphConstants.setLineEnd(map, GraphConstants.ARROW_NONE);
		GraphConstants.setEndFill(map, false);
		GraphConstants.setDisconnectable(map, false);

		GraphConstants.setFontName(map, f.getName());
		GraphConstants.setFontSize(map, f.getSize() - 2);
		GraphConstants.setFontStyle(map, f.getStyle());
		
		int u = GraphConstants.PERCENT;
		
		GraphConstants.setLabelPosition(map, labelPosition);
		viewMap.put(cell, map);
		
		cs.connect(cell, ellipsePort, false);
		cs.connect(cell, devicePort, true);
		
		return cell;
	}
	
	private PortEdge(Object userObject) {
		super(userObject);
	}
}
