/*
 * $Id: CreateUgo.java,v 1.18.4.1 2006/05/18 17:50:00 bass Exp $
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
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.GraphConstants;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.18.4.1 $, $Date: 2006/05/18 17:50:00 $
 * @module schemeclient
 */

public class CreateUgo {
	private CreateUgo() {
		// empty
	}
	
	private static final int rackWidth = 11;
	private static final int rackHeight = 21;
	public static void createRackUgo(SchemeElement element, SchemeGraph graph) {
		// make net WxH
		int step = graph.getGridSize() * 2;
		List<Object> toInsert = new LinkedList<Object>();
		Map<DefaultGraphCell, Map> viewMap = new HashMap<DefaultGraphCell, Map>();
		ConnectionSet cs = new ConnectionSet();
				
		for (int i = 0; i < rackWidth; i++) {
			List<Point> list = new ArrayList<Point>();
			list.add(new Point(step * (i + 1), step));
			list.add(new Point(step * (i + 1), rackHeight * step));
			
			Map map = GraphConstants.createMap();
			map.put(Constants.SELECTABLE, new Boolean(false));
			GraphConstants.setLineColor(map, Color.LIGHT_GRAY);
			GraphConstants.setPoints(map, list);
			GraphConstants.setLineEnd(map, GraphConstants.ARROW_NONE);
			GraphConstants.setEndFill(map, true);
			
			DefaultEdge cell = new DefaultEdge(""); //$NON-NLS-1$
			viewMap.put(cell, map);
			toInsert.add(cell);
		}
		for (int i = 0; i < rackHeight; i++) {
			List<Point> list = new ArrayList<Point>();
			list.add(new Point(step, step * (i + 1)));
			list.add(new Point(rackWidth * step, step * (i + 1)));
			
			Map map = GraphConstants.createMap();
			map.put(Constants.SELECTABLE, new Boolean(false));
			GraphConstants.setLineColor(map, Color.LIGHT_GRAY);
			GraphConstants.setPoints(map, list);
			GraphConstants.setLineEnd(map, GraphConstants.ARROW_NONE);
			GraphConstants.setEndFill(map, true);
			
			DefaultEdge cell = new DefaultEdge(""); //$NON-NLS-1$
			viewMap.put(cell, map);
			toInsert.add(cell);
		}
		for (int i = 1; i < rackHeight; i++) {
			Map map = GraphConstants.createMap();
			String num = Integer.toString(rackHeight - i);
			Rectangle bounds = new Rectangle(0, (int)(step * i), step / 2, step / 2);
			map = GraphConstants.createMap();
			map.put(Constants.SELECTABLE, new Boolean(false));
			GraphConstants.setBounds(map, bounds);
			GraphConstants.setSizeable(map, false);
			GraphConstants.setAutoSize(map, true);
			DefaultGraphCell text = new DefaultGraphCell(num);
			viewMap.put(text, map);
			toInsert.add(text);
		}
		
		// remove old
		graph.setSelectionCells(new Object[0]);
		graph.getModel().remove(graph.getDescendants(graph.getAll()));
		
		Object[] cells = toInsert.toArray();
		graph.getModel().insert(cells, viewMap, cs, null, null);
		
		int lines = 0;
		try {
			for (SchemeElement se : element.getSchemeElements()) {
				lines = createGroup(lines, graph, step, se);
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
	
	private static int createGroup(int lines, SchemeGraph graph, int step, SchemeElement element) throws ApplicationException {
		List<Object> toInsert = new LinkedList<Object>();
		for (SchemeDevice dev : element.getSchemeDevices()) {
			int columns = 0;
			List<SchemePort> schemePorts = ClientUtils.getSortedPorts(dev.getSchemePorts());
			List<SchemeCablePort> schemeCablePorts = ClientUtils.getSortedCablePorts(dev.getSchemeCablePorts());
			
			Rectangle bounds = new Rectangle(step, step * (lines + 1), step * (schemePorts.size() + schemeCablePorts.size()), step);
			DeviceCell cell = SchemeActions.createDevice(graph, "", bounds, dev.getId());  //$NON-NLS-1$
			toInsert.add(cell);
			lines++;
			for (SchemePort port : schemePorts) {
				Point p = new Point((int)((columns + 1.5) * step), (int)((lines + .7) * step));
				DefaultGraphCell pcell = SchemeActions.createInternalPort(graph, p, port.getName(), port.getDirectionType(), false, Color.WHITE, port.getId());  
				toInsert.add(pcell);
				columns++;
			}
			for (SchemeCablePort port : schemeCablePorts) {
				Point p = new Point((int)((columns + 1.5) * step), (int)((lines + .7) * step));
				DefaultGraphCell pcell = SchemeActions.createInternalPort(graph, p, port.getName(), port.getDirectionType(), true, Color.WHITE, port.getId());  
				toInsert.add(pcell);
				columns++;
			}
		}
		Object[] objs = toInsert.toArray();
		if (objs.length != 0) {
			CreateGroup.createElementsGroup(graph, objs, element);
		}
		for (SchemeElement inner : element.getSchemeElements()) {
			lines = createGroup(lines, graph, step, inner);
		}
		return lines;
	}

	public static void createProtoUgo(SchemeProtoElement proto, SchemeGraph graph, ImageIcon symbol, String label, List<BlockPortCell> blockports_in, List<BlockPortCell> blockports_out) {
		create(graph, symbol, label, blockports_in, blockports_out);
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;
		CreateGroup.createGroup(graph, cells, proto.getId(), DeviceGroup.PROTO_ELEMENT);	
	}
	
	public static void createElementUgo(SchemeElement element, SchemeGraph graph, ImageIcon symbol, String label, List<BlockPortCell> blockports_in, List<BlockPortCell> blockports_out) {
		create(graph, symbol, label, blockports_in, blockports_out);
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;
		CreateGroup.createGroup(graph, cells, element.getId(), DeviceGroup.SCHEME_ELEMENT);	
	}
	
	public static void createSchemeUgo(Scheme scheme, SchemeGraph graph, ImageIcon symbol, String label, List<BlockPortCell> blockports_in, List<BlockPortCell> blockports_out) {
		create(graph, symbol, label, blockports_in, blockports_out);
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;
		CreateGroup.createGroup(graph, cells, scheme.getId(), DeviceGroup.SCHEME);
	}
	
	public static void createMuffUgo(SchemeElement element, SchemeGraph graph, ImageIcon symbol, String label, List<BlockPortCell> blockports_in, List<BlockPortCell> blockports_out) {
		createMuff(graph, blockports_in, blockports_out);
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;
		CreateGroup.createGroup(graph, cells, element.getId(), DeviceGroup.SCHEME_ELEMENT);
	}
	
	public static void createMuffUgo(SchemeProtoElement element, SchemeGraph graph, ImageIcon symbol, String label, List<BlockPortCell> blockports_in, List<BlockPortCell> blockports_out) {
//		try {
//			for (SchemeProtoElement proto : element.getSchemeProtoElements(false)) {
//				proto.setSchemeCell(null);
//			}
//		} catch (ApplicationException e) {
//			Log.errorMessage(e);
//		}
		
		createMuff(graph, blockports_in, blockports_out);
		Object[] cells = CreateGroup.getCellsToAdd(graph);
		if (cells.length == 0)
			return;
		CreateGroup.createGroup(graph, cells, element.getId(), DeviceGroup.PROTO_ELEMENT);
	}
	
	private static void createMuff(SchemeGraph graph, List<BlockPortCell> blockports_in, List<BlockPortCell> blockports_out) {
		//remove old cells
		graph.setSelectionCells(new Object[0]);
		graph.getModel().remove(graph.getDescendants(graph.getAll()));

		// create new element
		int grid = graph.getGridSize();
		int max = Math.max(1, Math.max(blockports_in.size(), blockports_out.size()));
		Point p1 = graph.snap(new Point(grid*4, grid*2)); 
		Rectangle deviceBounds = new Rectangle(
				new Point(p1.x, p1.y - grid / 2),//oldrect.x, oldrect.y
				graph.snap(new Dimension(grid * max, grid * max)));

		SchemeDevice dev;
		try {
			dev = SchemeObjectsFactory.createSchemeDevice(Long.toString(System.currentTimeMillis()));
		} catch (CreateObjectException e) {
			Log.errorMessage(e);
			return;
		}	

		Map viewMap = new HashMap();
		DeviceCell cell = DeviceCell.createInstance("", deviceBounds, viewMap, Color.GRAY);
		cell.setKind(DeviceCell.ROUNDED);
		cell.setSchemeDeviceId(dev.getId());
		graph.getGraphLayoutCache().insert(new Object[] { cell }, viewMap, null, null, null);
		graph.setSelectionCell(cell);
		
		List<DefaultGraphCell> insertedObjects = new ArrayList<DefaultGraphCell>(2 * (blockports_in.size() + blockports_out.size()) + 1);
		insertedObjects.add(cell);
		
		int counter = 0;
		Collections.sort(blockports_out, new BPCComparator());
		for (Iterator it = blockports_out.iterator(); it.hasNext();)
		{
			BlockPortCell b = (BlockPortCell)it.next();
			Point p = graph.snap(new Point(deviceBounds.x + deviceBounds.width + grid, (int)(deviceBounds.y + grid*(0.5 + (max - blockports_out.size()) / 2 + counter++))));
			
			AbstractSchemePort port = b.getAbstractSchemePort();
			Color color = SchemeActions.determinePortColor(port, port.getAbstractSchemeLink());
			if (b.isCablePort()) {
				CablePortCell portCell = SchemeActions.createCablePort(graph, cell, p, "", port.getDirectionType(), color, port.getId());
				insertedObjects.add(portCell);
			} else {
				PortCell portCell = SchemeActions.createPort(graph, cell, p, "", port.getDirectionType(), color, port.getId());
				insertedObjects.add(portCell);
			}
		}
		counter = 0;
		Collections.sort(blockports_in, new BPCComparator());
		for (Iterator it = blockports_in.iterator(); it.hasNext();)
		{
			BlockPortCell b = (BlockPortCell)it.next();
			Point p = graph.snap(new Point(deviceBounds.x - grid, (int)(deviceBounds.y + grid*(0.5 + (max - blockports_in.size()) / 2 + counter++))));
			
			AbstractSchemePort port = b.getAbstractSchemePort();
			Color color = SchemeActions.determinePortColor(port, port.getAbstractSchemeLink());
			if (b.isCablePort()) {
				CablePortCell portCell = SchemeActions.createCablePort(graph, cell, p, "", port.getDirectionType(), color, port.getId());
				insertedObjects.add(portCell);
			} else {
				PortCell portCell = SchemeActions.createPort(graph, cell, p, "", port.getDirectionType(), color, port.getId());
				insertedObjects.add(portCell);
			}
		}
		Object[] insertedCells = insertedObjects.toArray();
//		GraphActions.setObjectsBackColor(graph, insertedCells, Color.WHITE);
		graph.setSelectionCells(insertedCells);
	}
	
	private static void create(SchemeGraph graph, ImageIcon symbol, String label, List<BlockPortCell> blockports_in, List<BlockPortCell> blockports_out) {
		//remove old cells
		graph.setSelectionCells(new Object[0]);
		graph.getModel().remove(graph.getDescendants(graph.getAll()));

		// create new element
		int grid = graph.getGridSize();
		int max = Math.max(1, Math.max(blockports_in.size(), blockports_out.size()));
		int stringWidth = grid * (((int)graph.getFont().getStringBounds(label, new FontRenderContext(null, false, false)).getWidth() / grid + 1));
		Rectangle deviceBounds = new Rectangle(
				graph.snap(new Point(grid*4, grid*2)),//oldrect.x, oldrect.y
				graph.snap(new Dimension(Math.max(stringWidth, 2 * grid), grid*(1 + max))));

		SchemeDevice dev;
		try {
			dev = SchemeObjectsFactory.createSchemeDevice(Long.toString(System.currentTimeMillis()));
		} catch (CreateObjectException e) {
			Log.errorMessage(e);
			return;
		}	

		Map viewMap = new HashMap();
		DeviceCell cell = DeviceCell.createInstance(label, deviceBounds, viewMap);
		cell.setSchemeDeviceId(dev.getId());
		graph.getGraphLayoutCache().insert(new Object[] { cell }, viewMap, null, null, null);
		graph.setSelectionCell(cell);
		
		List<DefaultGraphCell> insertedObjects = new ArrayList<DefaultGraphCell>(2 * (blockports_in.size() + blockports_out.size()) + 1);
		insertedObjects.add(cell);
		
		int counter = 0;
		Collections.sort(blockports_out, new BPCComparator());
		for (Iterator it = blockports_out.iterator(); it.hasNext();)
		{
			BlockPortCell b = (BlockPortCell)it.next();
			String name = (String)b.getUserObject();
			Point p = graph.snap(new Point(deviceBounds.x + deviceBounds.width + grid, deviceBounds.y + grid*(1 + (max - blockports_out.size()) / 2 + counter++)));
			
			AbstractSchemePort port = b.getAbstractSchemePort();
			Color color = SchemeActions.determinePortColor(port, port.getAbstractSchemeLink());
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
		Collections.sort(blockports_in, new BPCComparator());
		for (Iterator it = blockports_in.iterator(); it.hasNext();)
		{
			BlockPortCell b = (BlockPortCell)it.next();
			String name = (String)b.getUserObject();
			Point p = graph.snap(new Point(deviceBounds.x - grid, deviceBounds.y + grid*(1 + (max - blockports_in.size()) / 2 + counter++)));
			
			AbstractSchemePort port = b.getAbstractSchemePort();
			Color color = SchemeActions.determinePortColor(port, port.getAbstractSchemeLink());
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
//		GraphActions.setObjectsBackColor(graph, insertedCells, Color.WHITE);
		graph.setSelectionCells(insertedCells);
		
		if (symbol != null) {
			GraphActions.setImage(graph, cell, symbol);
		}
	}
}

class BPCComparator implements Comparator<BlockPortCell> {
	public int compare(BlockPortCell o1, BlockPortCell o2) {
		return ((String)o1.getUserObject()).compareTo((String)o2.getUserObject());
	}
}