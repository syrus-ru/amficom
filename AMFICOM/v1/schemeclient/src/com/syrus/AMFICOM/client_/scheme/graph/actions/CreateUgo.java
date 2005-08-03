/*
 * $Id: CreateUgo.java,v 1.5 2005/08/03 09:29:41 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/08/03 09:29:41 $
 * @module schemeclient_v1
 */

public class CreateUgo {
	private CreateUgo() {
		// empty
	}

	public static void createProtoUgo(SchemeProtoElement proto, SchemeGraph graph, ImageIcon symbol, String label, List blockports_in, List blockports_out) {
		create(graph, symbol, label, blockports_in, blockports_out);
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;
		CreateGroup.createGroup(graph, cells, proto.getId(), DeviceGroup.PROTO_ELEMENT);	
	}
	
	public static void createElementUgo(SchemeElement element, SchemeGraph graph, ImageIcon symbol, String label, List blockports_in, List blockports_out) {
		create(graph, symbol, label, blockports_in, blockports_out);
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;
		CreateGroup.createGroup(graph, cells, element.getId(), DeviceGroup.SCHEME_ELEMENT);	
	}
	
	public static void createSchemeUgo(Scheme scheme, SchemeGraph graph, ImageIcon symbol, String label, List blockports_in, List blockports_out) {
		create(graph, symbol, label, blockports_in, blockports_out);
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;
		CreateGroup.createGroup(graph, cells, scheme.getId(), DeviceGroup.SCHEME);
	}
	 
	
	private static void create(SchemeGraph graph, ImageIcon symbol, String label, List blockports_in, List blockports_out) {
		//remove old cells
		graph.setSelectionCells(new Object[0]);
		graph.getModel().remove(graph.getDescendants(graph.getAll()));

		// create new element
		int grid = graph.getGridSize();
		int max = Math.max(3, Math.max(blockports_in.size(), blockports_out.size()));
		Rectangle deviceBounds = new Rectangle(
				graph.snap(new Point(grid*2, grid*2)),//oldrect.x, oldrect.y
				graph.snap(new Dimension(grid*4, grid*(max+1))));

		List<DefaultGraphCell> insertedObjects = new ArrayList<DefaultGraphCell>(2 * (blockports_in.size() + blockports_out.size()) + 1);
		
		SchemeDevice dev;
		try {
			dev = SchemeObjectsFactory.createSchemeDevice("SchemeDevice"+System.currentTimeMillis());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			return;
		}	
		
		Map viewMap = new HashMap();
		DeviceCell cell = DeviceCell.createInstance(label, deviceBounds, viewMap);
		cell.setSchemeDeviceId(dev.getId());
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
			Color color = SchemeActions.determinePortColor(port);
			port.setName(name);
			if (b.isCablePort()) {
				CablePortCell portCell = SchemeActions.createCablePort(graph, cell, p, name, port.getDirectionType(), color, port.getId());
				insertedObjects.add(portCell);
			} else {
				PortCell portCell = SchemeActions.createPort(graph, cell, p, name, port.getDirectionType(), color, port.getId());
				insertedObjects.add(portCell);
			}
		}
		counter = 0;
		for (Iterator it = blockports_in.iterator(); it.hasNext();)
		{
			BlockPortCell b = (BlockPortCell)it.next();
			String name = (String)b.getUserObject();
			Point p = graph.snap(new Point(grid*2-grid, grid*2+ grid*((max - blockports_in.size()) / 2 + 1 + counter++)));
			
			AbstractSchemePort port = b.getAbstractSchemePort();
			Color color = SchemeActions.determinePortColor(port);
			port.setName(name);
			if (b.isCablePort()) {
				CablePortCell portCell = SchemeActions.createCablePort(graph, cell, p, name, port.getDirectionType(), color, port.getId());
				insertedObjects.add(portCell);
			} else {
				PortCell portCell = SchemeActions.createPort(graph, cell, p, name, port.getDirectionType(), color, port.getId());
				insertedObjects.add(portCell);
			}
		}
		Object[] insertedCells = insertedObjects.toArray();
		GraphActions.setObjectsBackColor(graph, insertedCells, Color.WHITE);
		graph.setSelectionCells(insertedCells);
		
		if (symbol != null) {
			GraphActions.setImage(graph, cell, symbol);
		}
	}
}
