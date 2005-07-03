/*
 * $Id: CreateUgo.java,v 1.2 2005/04/22 07:32:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/22 07:32:50 $
 * @module schemeclient_v1
 */

public class CreateUgo {
	private CreateUgo() {
		// empty
	}

	public static void create(SchemeGraph graph, ImageIcon symbol, String label, List blockports_in, List blockports_out) {
		//remove old cells
		graph.setSelectionCells(new Object[0]);
		graph.getModel().remove(graph.getDescendants(graph.getAll()));

		// create new element
		int grid = graph.getGridSize();
		int max = Math.max(3, Math.max(blockports_in.size(), blockports_out.size()));
		Rectangle deviceBounds = new Rectangle(
				graph.snap(new Point(grid*2, grid*2)),//oldrect.x, oldrect.y
				graph.snap(new Dimension(grid*4, grid*(max+1))));

		List insertedObjects = new ArrayList(2 * (blockports_in.size() + blockports_out.size()) + 1);
		
		Map viewMap = new HashMap();
		DeviceCell cell = DeviceCell.createInstance(label, deviceBounds, viewMap);
		graph.getGraphLayoutCache().insert(new Object[] { cell }, viewMap, null, null, null);
		graph.setSelectionCell(cell);
		insertedObjects.add(cell);
		
		int counter = 0;
		for (Iterator it = blockports_out.iterator(); it.hasNext();)
		{
			BlockPortCell b = (BlockPortCell)it.next();
			String name = (String)b.getUserObject();
			Point p = graph.snap(new Point(grid*2 + grid*5, grid*2 + grid*((max - blockports_out.size()) / 2 + 1 + counter++)));
			
			AbstractSchemePort port = b.getAbstractSchemePort();
			port.setName(name);
			insertedObjects.add(SchemeActions.createAbstractPort(graph, cell, p, name, port.getDirectionType(), b.isCablePort()));
		}
		counter = 0;
		for (Iterator it = blockports_in.iterator(); it.hasNext();)
		{
			BlockPortCell b = (BlockPortCell)it.next();
			String name = (String)b.getUserObject();
			Point p = graph.snap(new Point(grid*2-grid, grid*2+ grid*((max - blockports_in.size()) / 2 + 1 + counter++)));
			
			AbstractSchemePort port = b.getAbstractSchemePort();
			port.setName(name);
			insertedObjects.add(SchemeActions.createAbstractPort(graph, cell, p, name, port.getDirectionType(), b.isCablePort()));
		}
		Object[] insertedCells = insertedObjects.toArray();
		GraphActions.setObjectsBackColor(graph, insertedCells, Color.WHITE);
		graph.setSelectionCells(insertedCells);
		
		if (symbol != null) {
			GraphActions.setImage(graph, cell, symbol);
		}
	}
}
