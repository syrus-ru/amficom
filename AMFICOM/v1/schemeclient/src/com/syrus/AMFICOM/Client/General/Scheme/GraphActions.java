package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.tree.TreeNode;

import com.jgraph.graph.*;
import com.jgraph.graph.Port;
import com.jgraph.pad.ImageCell;
//import com.syrus.AMFICOM.Client.General.Command.Scheme.InsertToCatalogCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortPackage.DirectionType;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;

public class GraphActions
{
	public static DeviceGroup CreateTopLevelElementAction(SchemeGraph graph, Object userObject, Rectangle bounds,  boolean autosize, Color border)
	{
		Map viewMap = new HashMap();
		Map map;

		Object obj = (userObject instanceof String) ? userObject : "";
		DeviceGroup cell = new DeviceGroup(obj);

		map = GraphConstants.createMap();
		if (userObject instanceof ImageIcon)
			GraphConstants.setIcon(map, (ImageIcon) userObject);
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setBackground(map, Color.white);
		if (border != null)
			GraphConstants.setBorderColor(map, border);

		if (autosize)
			GraphConstants.setAutoSize(map, true);
		viewMap.put(cell, map);

		// Create Ports
//		int u = GraphConstants.PERCENT;
		DefaultPort port;

		// Floating Center Port (Child 0 is Default)
		port = new DefaultPort("Center");
		cell.add(port);

		Object[] insert = new Object[] { cell };
		graph.getGraphLayoutCache().insert(insert, viewMap, null, null, null);
		graph.setSelectionCells(insert);
		graph.setPortsVisible(false);
		return cell;
	}

	public static TopLevelCableLink CreateTopLevelCableLinkAction(SchemeGraph graph, Port firstPort,
			Port port, Point p, Point p2)
	{
		TopLevelCableLink cell = new TopLevelCableLink();
		Map map = GraphConstants.createMap();

//		GraphConstants.setRouting(map, GraphConstants.ROUTING_SIMPLE);

		ArrayList list = new ArrayList();
		list.add(p);
		list.add(p2);

		GraphConstants.setPoints(map, list);
		GraphConstants.setBendable(map, true);
		Map viewMap = new HashMap();

		viewMap.put(cell, map);
		Object[] insert = new Object[] {cell};
		ConnectionSet cs = new ConnectionSet();
		if (firstPort != null)
			cs.connect(cell, firstPort, true);
		if (port != null)
			cs.connect(cell, port, false);
		graph.getModel().insert(insert, viewMap, cs, null, null);

		graph.setSelectionCell(cell);
		return cell;
	}

	public static DefaultGraphCell CreateDeviceAction(SchemeGraph graph, Object userObject, Rectangle bounds,  boolean autosize, Color border)
	{
		Map viewMap = new HashMap();
		Map map;

		Object obj = (userObject instanceof String) ? userObject : "";
		DeviceCell cell = new DeviceCell(obj);
		SchemeDevice device = SchemeFactory.createSchemeDevice();
		cell.setSchemeDeviceId(device.id());
		try {
			SchemeStorableObjectPool.putStorableObject(device);
		}
		catch (IllegalObjectEntityException ex) {
			ex.printStackTrace();
		}

		map = GraphConstants.createMap();
		if (userObject instanceof ImageIcon)
			GraphConstants.setIcon(map, (ImageIcon) userObject);
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setBackground(map, Color.white);
		if (border != null)
			GraphConstants.setBorderColor(map, border);

		if (autosize)
			GraphConstants.setAutoSize(map, true);
		viewMap.put(cell, map);

		// Create Ports
//		int u = GraphConstants.PERCENT;
		DefaultPort port;

		// Floating Center Port (Child 0 is Default)
		port = new DefaultPort("Center");
		cell.add(port);

		Object[] insert = new Object[] { cell };
		graph.getGraphLayoutCache().insert(insert, viewMap, null, null, null);
		graph.setSelectionCells(insert);
		graph.setPortsVisible(false);
		return cell;
	}

	public static SchemeLink CreateLinkAction(SchemeGraph graph, PortView firstPort, PortView port, Point p, Point p2)
	{
		SchemeLink link;

		Map map = GraphConstants.createMap();
		DefaultLink cell;

		PortCell sPort_cell = null;
		PortCell tPort_cell = null;

		if (firstPort != null)
		{
			Object o = ((DefaultPort)firstPort.getCell()).getParent();
			if (o instanceof PortCell)
				sPort_cell = (PortCell)o;
		}
		if (port != null)
		{
			Object o = ((DefaultPort)port.getCell()).getParent();
			if (o instanceof PortCell)
				tPort_cell = (PortCell)o;
		}

		boolean b1 = false;
		if (firstPort == null)
			b1 = true;
		else if (sPort_cell != null)
			b1 = true;

		boolean b2 = false;
		if (port == null)
			b2 = true;
		else if (tPort_cell != null)
			b2 = true;

		if (b1 && b2)
		{
			Object[] cells = graph.getAll();
			int counter = 0;
			for (int i = 0; i < cells.length; i++)
				if (cells[i] instanceof DefaultLink)
					counter++;
			String name = "l" + String.valueOf(counter+1);

			cell = new DefaultLink(name);
			GraphConstants.setRouting(map, cell.getRouting());
			link = SchemeFactory.createSchemeLink();
			link.name(name);
			cell.setSchemeLinkId(link.id());
			try {
				SchemeStorableObjectPool.putStorableObject(link);
			}
			catch (IllegalObjectEntityException ex) {
				ex.printStackTrace();
			}
	}
		else
			return null;

		ArrayList list = new ArrayList();
		list.add(p);
		list.add(p2);

		GraphConstants.setPoints(map, list);
		//GraphConstants.setDisconnectable(map, false);
		GraphConstants.setBendable(map, true);
		Map viewMap = new HashMap();

		viewMap.put(cell, map);
		Object[] insert = new Object[] { cell };
		ConnectionSet cs = new ConnectionSet();
		if (sPort_cell != null)
			cs.connect(cell, firstPort.getCell(), true);
		if (tPort_cell != null)
			cs.connect(cell, port.getCell(), false);
		graph.getModel().insert(insert, viewMap, cs, null, null);

		//LinkView lv = (LinkView)graph.getGraphLayoutCache().getMapping(cell, true);
		//lv.getEdgeRenderer().setForeground(Color.blue);

		graph.setSelectionCell(cell);
		return link;
	}

	public static SchemeCableLink CreateCableLinkAction(SchemeGraph graph, PortView firstPort, PortView port, Point p, Point p2)
	{
		SchemeCableLink link;

		Map map = GraphConstants.createMap();
		DefaultCableLink cell;

		CablePortCell s_cablePort_cell = null;
		CablePortCell t_cablePort_cell = null;

		if (firstPort != null)
		{
			Object o = ((DefaultPort)firstPort.getCell()).getParent();
			if (o instanceof CablePortCell)
				s_cablePort_cell = (CablePortCell)o;
		}
		if (port != null)
		{
			Object o = ((DefaultPort)port.getCell()).getParent();
			if (o instanceof CablePortCell)
				t_cablePort_cell = (CablePortCell)o;
		}

		boolean b1 = false;
		if (firstPort == null)
			b1 = true;
		else if (s_cablePort_cell != null)
			b1 = true;

		boolean b2 = false;
		if (port == null)
			b2 = true;
		else if (t_cablePort_cell != null)
			b2 = true;

		if (b1 && b2)
		{
			Object[] cells = graph.getAll();
			int counter = 0;
			for (int i = 0; i < cells.length; i++)
				if (cells[i] instanceof DefaultCableLink)
					counter++;
			String name = "cl" + String.valueOf(counter+1);

			cell = new DefaultCableLink(name);
			GraphConstants.setRouting(map, cell.getRouting());
			link = SchemeFactory.createSchemeCableLink();
			cell.setSchemeCableLinkId(link.id());
			link.name(name);

			try {
				SchemeStorableObjectPool.putStorableObject(link);
			}
			catch (IllegalObjectEntityException ex) {
				ex.printStackTrace();
			}

			GraphConstants.setLineWidth(map, 2.49f);
		}
		else
			return null;

		ArrayList list = new ArrayList();
		list.add(p);
		list.add(p2);

		GraphConstants.setPoints(map, list);
		//GraphConstants.setDisconnectable(map, false);
		GraphConstants.setBendable(map, true);
		Map viewMap = new HashMap();

		viewMap.put(cell, map);
		Object[] insert = new Object[] { cell };
		ConnectionSet cs = new ConnectionSet();
		if (s_cablePort_cell != null)
			cs.connect(cell, firstPort.getCell(), true);
		if (t_cablePort_cell != null)
			cs.connect(cell, port.getCell(), false);
		graph.getModel().insert(insert, viewMap, cs, null, null);

		graph.setSelectionCell(cell);
		return link;
	}

	public static DefaultGraphCell CreateVisualPortAction(SchemeGraph graph, Point p, boolean isPort, String name)
	{
		Font f = graph.getFont();

		int counter = 0;
		Object[] cells = graph.getSelectionCells();
		DeviceCell dev = null;

		for  (int i = 0; i < cells.length; i++)
			if ( cells[i] instanceof DeviceCell)
			{
			dev = (DeviceCell)cells[i];
			counter++;
		}

		if (counter != 1)
			return null;
		if (hasGroupedParent(dev))
			return null;

		Map m = graph.getModel().getAttributes(dev);
		Rectangle dev_rect = GraphConstants.getBounds(m);
		if (name.equals(""))
			name = String.valueOf(((DeviceCell)cells[0]).getChildCount());

		if (dev_rect.y > p.y || dev_rect.y + dev_rect.height < p.y)
			return null;

		DefaultGraphCell visualPort;
		DefaultPort ellipsePort;
		Port devPort;
		Map edgemap = GraphConstants.createMap();
		int u = GraphConstants.PERCENT;

		Rectangle dev_bounds = GraphConstants.getBounds(dev.getAttributes());
		if (p.x >= dev_bounds.x + dev_bounds.width / 2)
		{
			if (isPort)
			{
				visualPort = addVisualPort(graph, "", new Rectangle(p.x - 6, p.y - 3, 7, 7), dev, DirectionType._OUT);
				Arrays.asList(dev.getSchemeDevice().schemePorts()).add(((PortCell)visualPort).getSchemePort());
				((PortCell)visualPort).getSchemePort().name(name);
			}
			else
			{
				visualPort = addVisualCablePort(graph, "", new Rectangle(p.x - 6, p.y - 3, 7, 7), dev, DirectionType._OUT);
				Arrays.asList(dev.getSchemeDevice().schemeCablePorts()).add(((CablePortCell)visualPort).getSchemeCablePort());
				((CablePortCell)visualPort).getSchemeCablePort().name(name);
			}
			devPort = addPort (graph, "", dev, new Point(u, (int)(u * ( (double)(p.y + 1 - dev_rect.y) / (double)dev_rect.height))));
			ellipsePort = addPort (graph, "", visualPort, new Point(0, u / 2));
			int distance = (p.x - (dev_bounds.x + dev_bounds.width)) / graph.getGridSize() + 1;
			GraphConstants.setLabelPosition(edgemap, new Point ( -u / distance, 0));//new Point ((int)(u + 4 * u / (distance * Math.sqrt(distance))), 0));
		}
		else
		{
			if (isPort)
			{
				visualPort = addVisualPort(graph, "", new Rectangle(p.x, p.y - 3, 7, 7), dev, DirectionType._IN);
				Arrays.asList(dev.getSchemeDevice().schemePorts()).add(((PortCell)visualPort).getSchemePort());
				((PortCell)visualPort).getSchemePort().name(name);
			}
			else
			{
				visualPort = addVisualCablePort(graph, "", new Rectangle(p.x, p.y - 3, 7, 7), dev, DirectionType._IN);
				Arrays.asList(dev.getSchemeDevice().schemeCablePorts()).add(((CablePortCell)visualPort).getSchemeCablePort());
				((CablePortCell)visualPort).getSchemeCablePort().name(name);
			}
			devPort = addPort(graph, "", dev, new Point(0, (int)(u * ( (double)(p.y + 1 - dev_rect.y) / (double)dev_rect.height))));
			ellipsePort = addPort(graph, "", visualPort, new Point(u, u / 2));
			int distance = (dev_bounds.x - p.x) / graph.getGridSize();
			GraphConstants.setLabelPosition(edgemap, new Point (u + (u / distance), 0));//new Point ((int)(- 2.5 * u / (distance * Math.sqrt(distance))), 0));
		}

		ArrayList list = new ArrayList();
		list.add(p);
		list.add(new Point(dev_rect.x + dev_rect.width, p.y));

		GraphConstants.setPoints(edgemap, list);
		//GraphConstants.setRouting(map, GraphConstants.ROUTING_SIMPLE);
		GraphConstants.setLineEnd(edgemap, GraphConstants.ARROW_NONE);
		GraphConstants.setEndFill(edgemap, false);
		GraphConstants.setDisconnectable(edgemap, false);

		GraphConstants.setFontName(edgemap, f.getName());
		GraphConstants.setFontSize(edgemap, f.getSize() - 2);
		GraphConstants.setFontStyle(edgemap, f.getStyle());

//				GraphConstants.setSizeable(map, false);
//				GraphConstants.setMoveable(map, false);
//				GraphConstants.setBendable(map, false);

		Map viewMap = new HashMap();
		PortEdge edge = new PortEdge(name);

		viewMap.put(edge, edgemap);
		Object[] insert = new Object[] { edge };
		ConnectionSet cs = new ConnectionSet();
		cs.connect(edge, ellipsePort, false);
		cs.connect(edge, devPort, true);

		graph.getModel().insert(insert, viewMap, cs, null, null);
		graph.addSelectionCells(new Object[] {visualPort});

		return visualPort;
	}

	static PortCell addVisualPort(SchemeGraph graph, Object userObject, Rectangle bounds, DeviceCell dev, DirectionType direction)
	{
		Map viewMap = new HashMap();
		Map map;

		// Create Vertex
		PortCell cell = new PortCell(userObject);
		SchemePort sp = SchemeFactory.createSchemePort();
		sp.directionType(direction);
		sp.schemeDevice(dev.getSchemeDevice());
		cell.setSchemePortId(sp.id());

		map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setBackground(map, Color.red);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBorderColor(map, graph.defaultBorderColor);
		viewMap.put(cell, map);

		// Create Ports
		int u = GraphConstants.PERCENT;
		DefaultPort port = new DefaultPort("Center");
		map = GraphConstants.createMap();
		if (direction.equals("in"))
			GraphConstants.setOffset(map, new Point(0, u / 2));
		else
			GraphConstants.setOffset(map, new Point(u, u / 2));
		viewMap.put(port, map);
		cell.add(port);

		graph.getGraphLayoutCache().insert(new Object[]{cell}, viewMap, null, null, null);
		return cell;
	}

	static CablePortCell addVisualCablePort(SchemeGraph graph, Object userObject, Rectangle bounds, DeviceCell dev, DirectionType direction)
	{
		Map viewMap = new HashMap();
		Map map;

		// Create Vertex
		CablePortCell cell = new CablePortCell(userObject);
		SchemeCablePort scp = SchemeFactory.createSchemeCablePort();
		scp.schemeDevice(dev.getSchemeDevice());
		scp.directionType(direction);
		cell.setSchemeCablePortId(scp.id());

		map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setBackground(map, Color.red);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBorderColor(map, graph.defaultBorderColor);
		viewMap.put(cell, map);

		// Create Ports
		int u = GraphConstants.PERCENT;
		DefaultPort port;

		port = new DefaultPort("Center");
		map = GraphConstants.createMap();
		GraphConstants.setOffset(map, new Point(u / 2, u / 2));
		viewMap.put(port, map);
		cell.add(port);

		graph.getGraphLayoutCache().insert(new Object[]{cell}, viewMap, null, null, null);
		return cell;
	}


	static DefaultPort addPort (SchemeGraph graph, Object userObject, DefaultGraphCell cell, Point p)
	{
		CellView[] cv = graph.getGraphLayoutCache().getMapping(cell.getChildren().toArray(), true);
		Map map = new HashMap();
		for (int i = 0; i < cv.length; i++)
			map.put(cv[i].getCell(), cv[0].getAllAttributes());

		DefaultPort port = addPort (graph, userObject, cell, p, map);
		cell.changeAttributes(map);
		graph.getGraphLayoutCache().edit(map, null, null, null);
		return port;
	}

	static DefaultPort addPort (SchemeGraph graph, Object userObject, DefaultGraphCell cell, Point p, Map viewMap)
	{
		DefaultPort port = new DefaultPort(userObject);
		cell.add(port);
		Map map = GraphConstants.createMap();
		GraphConstants.setOffset(map, p);
		GraphConstants.setConnectable(map, false);
		viewMap.put(port, map);
		return port;
	}

	static DefaultPort removePort (SchemeGraph graph, DefaultGraphCell cell, DefaultPort port, Map viewMap)
	{
		cell.remove(port);
		viewMap.remove(port);
		return port;
	}

	static DefaultGraphCell addVertex(SchemeGraph graph, Object userObject, Rectangle bounds, boolean autosize, boolean opaque, boolean sizable, Color border)
	{
		Map viewMap = new HashMap();
		Map map;

		// Create Vertex
		Object obj = (userObject instanceof String) ? userObject : "";
		DefaultGraphCell cell;
		if (userObject instanceof ImageIcon)
			cell = new ImageCell(obj);
		else
			cell = new DefaultGraphCell(obj);
		map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, opaque);
		GraphConstants.setSizeable(map, sizable);
		if (border != null)
			GraphConstants.setBorderColor(map, border);

		if (autosize)
			GraphConstants.setAutoSize(map, true);
		if (opaque)
			GraphConstants.setOpaque(map, true);
		viewMap.put(cell, map);

		// Create Ports
		int u = GraphConstants.PERCENT;
		DefaultPort port;

		// Floating Center Port (Child 0 is Default)
		port = new DefaultPort("Center");
		cell.add(port);

		if (userObject instanceof ImageIcon)
		{
			GraphConstants.setIcon(map, (ImageIcon) userObject);
			// Single non-floating central-port
			map = GraphConstants.createMap();
			GraphConstants.setOffset(
					map,
					new Point(u / 2, u / 2));
			viewMap.put(port, map);
		}
		else
		{
	/*		// Top Left
			port = new DefaultPort("Topleft");
			cell.add(port);
			map = GraphConstants.createMap();
			GraphConstants.setOffset(map, new Point(0, 0));
			viewMap.put(port, map);
*/
		}
		Object[] insert = new Object[] { cell };
		graph.getModel().insert(insert, viewMap, null, null, null);
		return cell;
	}

	public static void clearGraph(SchemeGraph graph)
	{
		SchemeGraph.skip_notify = true;
		graph.setSelectionCells(new Object[0]);
		Object[] cells = graph.getAll();
		graph.getModel().remove(cells);
		SchemeGraph.skip_notify = false;
	}

	public static void setObjectsBackColor(SchemeGraph graph, Object[] objs, Color color)
	{
		Map map = GraphConstants.createMap();
		GraphConstants.setBackground(map, color);
		GraphConstants.setOpaque(map, true);
		Map viewMap = new HashMap();

		for (int i = 0; i < objs.length; i++)
			viewMap.put(objs[i], GraphConstants.cloneMap(map));

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setObjectBackColor(SchemeGraph graph, Object obj, Color color)
	{
		Map map = GraphConstants.createMap();
		GraphConstants.setBackground(map, color);
		GraphConstants.setOpaque(map, true);
		Map viewMap = new HashMap();
		viewMap.put(obj, GraphConstants.cloneMap(map));
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setEdgeColor(SchemeGraph graph, Object[] edges, Color color)
	{
		Map map = GraphConstants.createMap();
		GraphConstants.setLineColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map viewMap = new HashMap();

		for (int i = 0; i < edges.length; i++)
			viewMap.put(edges[i], GraphConstants.cloneMap(map));

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setEdgeColor(SchemeGraph graph, Object edge, Color color)
	{
		Map map = GraphConstants.createMap();
		GraphConstants.setLineColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map viewMap = new HashMap();
		viewMap.put(edge, GraphConstants.cloneMap(map));
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setObjectsForeColor(SchemeGraph graph, Object[] objs, Color color)
	{
		Map map = GraphConstants.createMap();
		GraphConstants.setBorderColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map viewMap = new HashMap();

		for (int i = 0; i < objs.length; i++)
			viewMap.put(objs[i], GraphConstants.cloneMap(map));

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setObjectForeColor(SchemeGraph graph, Object obj, Color color)
	{
		Map map = GraphConstants.createMap();
		GraphConstants.setBorderColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map viewMap = new HashMap();
		viewMap.put(obj, GraphConstants.cloneMap(map));
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setText(SchemeGraph graph, Object obj, String text)
	{
		Map viewMap = new HashMap();
		if (obj instanceof CablePortCell || obj instanceof PortCell)
		{
			DefaultGraphCell cell = (DefaultGraphCell)obj;
			for (Enumeration enumeration = cell.children(); enumeration.hasMoreElements();)
			{
				Port p = (Port)enumeration.nextElement();
				for (Iterator i = p.edges(); i.hasNext();)
				{
					DefaultEdge edge = (DefaultEdge)i.next();
					if (edge instanceof PortEdge)
					{
						Map map = edge.getAttributes();
						edge.setUserObject(text);
						viewMap.put(edge, GraphConstants.cloneMap(map));
						break;
					}
				}
			}
		}
		else if (obj instanceof DefaultGraphCell)
		{
			DefaultGraphCell cell = (DefaultGraphCell)obj;
			Map map = cell.getAttributes();
			cell.setUserObject(text);
			viewMap.put(cell, GraphConstants.cloneMap(map));
		}

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setImage(SchemeGraph graph, DefaultGraphCell cell, ImageIcon icon)
	{
		if (icon == null)
		{
			Map map = cell.getAttributes();
			map.remove(GraphConstants.ICON);
			Map viewMap = new HashMap();
			viewMap.put(cell, GraphConstants.cloneMap(map));
			graph.getGraphLayoutCache().edit(viewMap, null, null, null);
		}
		else
		{
			if (icon.getIconHeight() > 20 || icon.getIconWidth() > 20)
				icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
			Map map = GraphConstants.createMap();
			GraphConstants.setIcon(map, icon);
			Map viewMap = new HashMap();
			viewMap.put(cell, GraphConstants.cloneMap(map));
			graph.getGraphLayoutCache().edit(viewMap, null, null, null);
		}
	}

	public static ImageIcon getImage(SchemeGraph graph, DefaultGraphCell cell)
	{
		Map map = cell.getAttributes();
		return GraphConstants.getIcon(map);
	}


	public static void setResizable(SchemeGraph graph, Object[] cells, boolean b)
	{
		Map viewMap = new HashMap();
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultGraphCell)
			{
				Map m = GraphConstants.createMap();
				GraphConstants.setSizeable(m, b);
				viewMap.put(cells[i], m);
			}
		}
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static boolean hasGroupedParent (Object obj)
	{
		if (obj instanceof TreeNode)
		{
			TreeNode node = (TreeNode)obj;
			if (node.getParent() == null)
				return false;
			if (node.getParent() instanceof DeviceGroup)
				return true;
			return hasGroupedParent(node.getParent());
		}
		return false;
	}

	static void alignToGrid(SchemeGraph graph, Object[] cells)
	{
		Map viewMap = new HashMap();
		int grid = graph.getGridSize();
		/*double delta_x = 0;
		double delta_y = 0;
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DeviceCell ||
					cells[i] instanceof DeviceGroup)
			{
				DefaultGraphCell cell = (DefaultGraphCell)cells[i];
				Map map = cell.getAttributes();
				Rectangle bounds = GraphConstants.getBounds(map);
				delta_x = bounds.x / grid - Math.round(bounds.x / grid);
				delta_y = bounds.y / grid - Math.round(bounds.y / grid);
			}
		}
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultGraphCell)
			{
				DefaultGraphCell cell = (DefaultGraphCell)cells[i];
				Map map = cell.getAttributes();
				Rectangle bounds = GraphConstants.getBounds(map);
				bounds = new Rectangle(bounds.x - delta_x);
			}
		}*/
		for (int i = 0; i < cells.length; i++)
		{
			if (cells[i] instanceof DefaultGraphCell)
			{
				DefaultGraphCell cell = (DefaultGraphCell)cells[i];
				Map map = cell.getAttributes();
				Rectangle bounds = GraphConstants.getBounds(map);

				if (cells[i] instanceof DeviceCell ||
						cells[i] instanceof DeviceGroup)
				{
					bounds.x = (bounds.x / grid) * grid;
					bounds.y = (bounds.y / grid) * grid;
				}
				else if (cells[i] instanceof BlockPortCell)
				{
					bounds.x = (bounds.x / grid) * grid;
					bounds.y = ((bounds.y / grid) + 1) * grid - 5;
				}
				else if (cells[i] instanceof PortCell)
				{
					if (((PortCell)cells[i]).getSchemePort().directionType().equals(DirectionType._OUT))
					{
						bounds.x = ((bounds.x / grid) + 1) * grid - 6;
						bounds.y = ((bounds.y / grid) + 1) * grid - 3;
					}
					else
					{
						bounds.x = (bounds.x / grid) * grid;
						bounds.y = ((bounds.y / grid) + 1) * grid - 3;
					}
				}
				else if (cells[i] instanceof CablePortCell)
				{
					if (((CablePortCell)cells[i]).getSchemeCablePort().directionType().equals(DirectionType._OUT))
					{
						bounds.x = ((bounds.x / grid) + 1) * grid - 6;
						bounds.y = ((bounds.y / grid) + 1) * grid - 3;
					}
					else
					{
						bounds.x = (bounds.x / grid) * grid;
						bounds.y = ((bounds.y / grid) + 1) * grid - 3;
					}
				}
				GraphConstants.setBounds(map, new Rectangle(bounds));
				viewMap.put(cell, GraphConstants.cloneMap(map));
			}
		}
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	static BlockPortCell getBlockPort (SchemeGraph graph, DefaultGraphCell port)
	{
		for (Enumeration enumeration = port.children(); enumeration.hasMoreElements();)
		{
			Object obj = enumeration.nextElement();
			if (obj instanceof Port)
			{
				Port p = (Port)obj;
				for (Iterator i = p.edges(); i.hasNext();)
				{
					DefaultEdge edge = (DefaultEdge)i.next();
					Object p2 = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
					if (p2 instanceof BlockPortCell)
						return (BlockPortCell)p2;
				}
			}
		}
		return null;
	}

	public static DefaultGraphCell[] findVisualPorts(SchemeGraph graph, DeviceCell cell)
	{
		ArrayList v = new ArrayList();
		for (Enumeration enumeration = cell.children(); enumeration.hasMoreElements();)
		{
			Object obj = enumeration.nextElement();
			if (obj instanceof Port)
			{
				Port p = (Port)obj;
				for (Iterator i = p.edges(); i.hasNext();)
				{
					DefaultEdge edge = (DefaultEdge)i.next();
					Object p2 = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
					if (p2 instanceof PortCell || p2 instanceof CablePortCell)
						v.add(p2);

				}
			}
		}
		return (DefaultGraphCell[])v.toArray(new DefaultGraphCell[v.size()]);
	}

	static Object[] findAllVertexEdges(SchemeGraph graph, DefaultGraphCell[] cells)
	{
		ArrayList edges = new ArrayList();
		for (int i = 0; i < cells.length; i++)
		{
			for (Enumeration enumeration = cells[i].children(); enumeration.hasMoreElements();)
			{
				Object obj = enumeration.nextElement();
				if (obj instanceof Port)
				{
					Port p = (Port)obj;
					for (Iterator j = p.edges(); j.hasNext();)
						edges.add(j.next());
				}
			}
		}
		return edges.toArray(new Object[edges.size()]);
	}

	public static BlockPortCell[] findTopLevelPorts(SchemeGraph graph, DeviceGroup group)
	{
		ArrayList v = new ArrayList();
		Object[] objs = graph.getDescendants(new Object[] {group});
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof DeviceCell)
			{
				DefaultGraphCell[] ports = findVisualPorts(graph, (DeviceCell)objs[i]);
				for (int j = 0; j < ports.length; j++)
				{
					BlockPortCell bpc = getBlockPort(graph, ports[j]);
					if (bpc != null)
						v.add(bpc);
				}
			}
		return (BlockPortCell[])v.toArray(new BlockPortCell[v.size()]);
	}

	public static DeviceGroup[] findTopLevelGroups(SchemeGraph graph, Object[] cells)
	{
		ArrayList v = new ArrayList();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof DeviceGroup && !hasGroupedParent(objs[i]))
				v.add(objs[i]);
		return (DeviceGroup[])v.toArray(new DeviceGroup[v.size()]);
	}

	public static DefaultLink[] findTopLevelLinks(SchemeGraph graph, Object[] cells)
	{
		ArrayList v = new ArrayList();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof DefaultLink && !hasGroupedParent(objs[i]))
				v.add(objs[i]);
		return (DefaultLink[])v.toArray(new DefaultLink[v.size()]);
	}

	public static BlockPortCell[] findTopLevelPorts(SchemeGraph graph, Object[] cells)
	{
		ArrayList v = new ArrayList();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof BlockPortCell)
				v.add(objs[i]);
		return (BlockPortCell[])v.toArray(new BlockPortCell[v.size()]);
	}

	static void connectEdge(SchemeGraph graph, DefaultEdge edge, DefaultPort port, boolean is_source)
	{
		Map viewMap = new HashMap();
		Map map = edge.getAttributes();
		viewMap.put(edge, GraphConstants.cloneMap(map));
		ConnectionSet cs = new ConnectionSet();
		cs.connect(edge, port, is_source);
		graph.getModel().edit(viewMap, cs, null, null);
	}

	static void disconnectEdge(SchemeGraph graph, DefaultEdge edge, DefaultPort port, boolean is_source)
	{
		Map viewMap = new HashMap();
		Map map = edge.getAttributes();
		viewMap.put(edge, GraphConstants.cloneMap(map));
		ConnectionSet cs = new ConnectionSet();
		graph.getModel().edit(viewMap, cs, null, null);
	}
}

	class SchemeActions
	{
		static JPopupMenu createElementPopup(final ApplicationContext aContext, final SchemeGraph graph, DeviceGroup group)
		{
			final SchemeElement se = group.getSchemeElement();

			JPopupMenu pop = new JPopupMenu();
			if (!group.getSchemeId().equals(""))
			{
				final Scheme sc = group.getScheme();

				if (sc != null)
				{
					JMenuItem menu1 = new JMenuItem(new AbstractAction()
					{
						public void actionPerformed(ActionEvent ev)
						{
							aContext.getDispatcher().notify(new OperationEvent(sc.id(), 0, "addschemeevent"));

							if (se != null && se.alarmed())
								aContext.getDispatcher().notify(new SchemeElementsEvent(this,
										se, SchemeElementsEvent.CREATE_ALARMED_LINK_EVENT));
						}
					});
					menu1.setText("Открыть схему");
					pop.add(menu1);
					pop.addSeparator();
				}
				else
				{
					JMenuItem menu1 = new JMenuItem("Ошибка! Схема не найдена");
					pop.add(menu1);
				}
			}

			if (!group.getSchemeElementId().equals(""))
			{
				List v = se.ugoCellImpl().getData();
				if (se.schemeElements().length != 0 ||
						(v != null && v.size() != 0 && ((Object[])v.get(0)).length != 0))
						{
							JMenuItem menu1 = new JMenuItem(new AbstractAction()
							{
							public void actionPerformed(ActionEvent ev)
							{
								aContext.getDispatcher().notify(new OperationEvent(se.id(), 0, "addschemeelementevent"));

								if (se.alarmed())
									aContext.getDispatcher().notify(new SchemeElementsEvent(this,
											se, SchemeElementsEvent.CREATE_ALARMED_LINK_EVENT));
							}
							});
							menu1.setText("Открыть компонент");
							pop.add(menu1);
				}
/*
				if (show_catalog && graph.isEditable() && graph.panel instanceof SchemePanel)
				{
					JMenuItem menu2 = new JMenuItem(new AbstractAction()
					{
						public void actionPerformed(ActionEvent ev)
						{
							new InsertToCatalogCommand(aContext, (SchemePanel)(graph.panel), null, false).execute();
						}
					});
					menu2.setText("Связать с каталогом");
					pop.add(menu2);
				}*/
			}
			return pop;
		}
/*
	static JPopupMenu createLinkPopup(final ApplicationContext aContext, final SchemeGraph graph, DefaultLink link)
	{
		JPopupMenu pop = new JPopupMenu();

		if (!link.getSchemeLinkId().equals("") && graph.isEditable() && graph.panel instanceof SchemePanel)
		{
			SchemeLink sl = link.getSchemeLink();
			final String slId = sl.getId();

			JMenuItem menu1 = new JMenuItem(new AbstractAction()
			{
				public void actionPerformed(ActionEvent ev)
				{
					new InsertToCatalogCommand(aContext, (SchemePanel)(graph.panel), null, false).execute();
				}
			});
			menu1.setText("Связать с каталогом");
			pop.add(menu1);
		}
		if (!link.getSchemePathId().equals(""))
		{
			if (pop.getSubElements().length != 0)
				pop.addSeparator();
			final SchemePath sp = link.getSchemePath();

			JMenuItem menu2 = new JMenuItem(new AbstractAction()
			{
				public void actionPerformed(ActionEvent ev)
				{
					aContext.getDispatcher().notify(new SchemeNavigateEvent(new SchemePath[]{sp},
							SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT, graph.isEditable()));
				}
			});
			menu2.setText("Показать маршрут");
			pop.add(menu2);
			if (graph.isEditable() && graph.panel instanceof SchemePanel)
			{
				JMenuItem menu1 = new JMenuItem(new AbstractAction()
				{
					public void actionPerformed(ActionEvent ev)
					{
						new InsertToCatalogCommand(aContext, (SchemePanel)(graph.panel), null, true).execute();
					}
				});
				menu1.setText("Связать маршрут с каталогом");
				pop.add(menu1);
			}
		}
		return pop;
	}

	static JPopupMenu createCableLinkPopup(final ApplicationContext aContext, final SchemeGraph graph, DefaultCableLink link)
	{
		JPopupMenu pop = new JPopupMenu();

		if (!link.getSchemeCableLinkId().equals("") && graph.isEditable() && graph.panel instanceof SchemePanel)
		{
			SchemeCableLink sl = link.getSchemeCableLink();

			JMenuItem menu1 = new JMenuItem(new AbstractAction()
			{
				public void actionPerformed(ActionEvent ev)
				{
					new InsertToCatalogCommand(aContext, (SchemePanel)(graph.panel), null, false).execute();
				}
			});
			menu1.setText("Связать с каталогом");
			pop.add(menu1);
		}
		if (!link.getSchemePathId().equals(""))
		{
			if (pop.getSubElements().length != 0)
				pop.addSeparator();
			final SchemePath sp = link.getSchemePath();

			JMenuItem menu2 = new JMenuItem(new AbstractAction()
			{
				public void actionPerformed(ActionEvent ev)
				{
					aContext.getDispatcher().notify(new SchemeNavigateEvent(new SchemePath[]{sp},
							SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT, graph.isEditable()));
					//java.util.Vector v = sp.checkLinks();
				}
			});
			menu2.setText("Показать маршрут");
			pop.add(menu2);
			if (graph.isEditable() && graph.panel instanceof SchemePanel)
			{
				JMenuItem menu1 = new JMenuItem(new AbstractAction()
				{
					public void actionPerformed(ActionEvent ev)
					{
						new InsertToCatalogCommand(aContext, (SchemePanel)(graph.panel), null, true).execute();
					}
				});
				menu1.setText("Связать маршрут с каталогом");
				pop.add(menu1);
			}
		}
		return pop;
	}
*/

	static DeviceGroup findSchemeElementById(SchemeGraph graph, Identifier scheme_elementId)
	{
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DeviceGroup)
				if (((DeviceGroup)cells[i]).getSchemeElementId().equals(scheme_elementId))
					return (DeviceGroup)cells[i];
		return null;
	}

	static DeviceGroup findProtoElementById(SchemeGraph graph, Identifier protoId)
	{
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DeviceGroup)
				if (((DeviceGroup)cells[i]).getProtoElementId().equals(protoId))
					return (DeviceGroup)cells[i];
		return null;
	}

	static PortCell findSchemePortById(SchemeGraph graph, Identifier schemePortId)
	{
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof PortCell)
				if (((PortCell)cells[i]).getSchemePortId().equals(schemePortId))
					return (PortCell)cells[i];
		return null;
	}

	static PortCell findAccessPortById(SchemeGraph graph, Identifier aportId)
	{
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof PortCell)
				if (!((PortCell)cells[i]).getSchemePortId().equals(""))
					if (((PortCell)cells[i]).getSchemePort().measurementPort() != null
						&& ((PortCell)cells[i]).getSchemePort().measurementPortImpl().getId().equals(aportId))
						return (PortCell)cells[i];
		return null;
	}

	static CablePortCell findSchemeCablePortById(SchemeGraph graph, Identifier scheme_cablePortId)
	{
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof CablePortCell)
				if (((CablePortCell)cells[i]).getSchemeCablePortId().equals(scheme_cablePortId))
					return (CablePortCell)cells[i];
		return null;
	}

	static DefaultLink findSchemeLinkById(SchemeGraph graph, Identifier schemeLinkId)
	{
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DefaultLink)
				if (((DefaultLink)cells[i]).getSchemeLinkId().equals(schemeLinkId))
					return (DefaultLink)cells[i];
		return null;
	}

	static DefaultCableLink findSchemeCableLinkById(SchemeGraph graph, Identifier scheme_cableLinkId)
	{
		Object[] cells = graph.getAll();
		for (int i = 0; i < cells.length; i++)
			if (cells[i] instanceof DefaultCableLink)
				if (((DefaultCableLink)cells[i]).getSchemeCableLinkId().equals(scheme_cableLinkId))
					return (DefaultCableLink)cells[i];
		return null;
	}

	static boolean connectSchemeLink(SchemeGraph graph, DefaultLink link, PortCell port, boolean is_source)
	{
		SchemePort sp = port.getSchemePort();
		if (sp == null)
		{
			System.err.println("GraphActions.connectSchemeLink() port not found " + port.getSchemePortId());
			return false;
		}
		SchemeLink sl = link.getSchemeLink();
		if (sl == null)
		{
			System.err.println("GraphActions.connectSchemeLink() link not found " + link.getSchemeLinkId());
			return false;
		}

		if (sp.schemeLink() != null && !sp.schemeLink().equals(sl))
		{
			String message = "К порту " + sp.name() + " уже подключена линия связи " + sp.schemeLink().name() + ".\n";
			message += "Изменить подключенную линию на " + sl.name() + "?";
				//int res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), message, "Подтверждение", JOptionPane.YES_NO_OPTION);
				//if (res == JOptionPane.NO_OPTION)
				//	return false;
		}

		sp.schemeLink(sl);
		if (is_source)
			sl.sourceSchemePort(sp);
		else
			sl.targetSchemePort(sp);

		PortType pt = sp.portTypeImpl();
		if (pt == null)
			GraphActions.setObjectBackColor(graph, port, Color.red);
		else if (pt.getSort().equals(PortTypeSort.PORTTYPESORT_THERMAL))
			GraphActions.setObjectBackColor(graph, port, Color.black);
		else
			GraphActions.setObjectBackColor(graph, port, Color.white);

		return true;
	}

	static void disconnectSchemeLink(SchemeGraph graph, DefaultLink link, boolean is_source)
	{
		SchemeLink sl = link.getSchemeLink();
		if (sl == null)
		{
			System.err.println("GraphActions.disconnectSchemeLink() link not found " + link.getSchemeLinkId());
			return;
		}
		SchemePort sp;
		if (is_source)
		{
			sp = sl.sourceSchemePort();
			sl.sourceSchemePort(null);
		}
		else
		{
			sp = sl.targetSchemePort();
			sl.targetSchemePort(null);
		}
		if (sp != null)
		{
			sp.schemeLink(null);
			PortType pt = sp.portTypeImpl();
			if (pt == null)
				GraphActions.setObjectBackColor(graph, sp, Color.red);
			else
				GraphActions.setObjectBackColor(graph, sp, Color.yellow);
		}
	}

	static boolean connectSchemeCableLink(SchemeGraph graph, DefaultCableLink link, CablePortCell port, boolean is_source)
	{
		SchemeCablePort sp = port.getSchemeCablePort();
		if (sp == null)
		{
			System.err.println("GraphActions.connectSchemeCableLink() port not found " + port.getSchemeCablePortId());
			return false;
		}
		SchemeCableLink sl = link.getSchemeCableLink();
		if (sl == null)
		{
			System.err.println("GraphActions.connectSchemeCableLink() link not found " + link.getSchemeCableLinkId());
			return false;
		}
		if (sp.schemeCableLink() != null && !sp.schemeCableLink().equals(sl))
		{
			String message = "К порту " + sp.name() + " уже подключена линия связи " + sp.schemeCableLink().name() + ".\n";
			message += "Изменить подключенную линию на " + sl.name() + "?";

				//int res = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), message, "Предупреждение", JOptionPane.YES_NO_OPTION);
				//if (res == JOptionPane.NO_OPTION)
					//return false;
		}

		sp.schemeCableLink(sl);
		if (is_source)
			sl.sourceSchemeCablePort(sp);
		else
			sl.targetSchemeCablePort(sp);

		PortType pt = sp.portTypeImpl();
		if (pt == null)
			GraphActions.setObjectBackColor(graph, port, Color.red);
		else
			GraphActions.setObjectBackColor(graph, port, Color.white);

		return true;
	}

	static void disconnectSchemeCableLink(SchemeGraph graph, DefaultCableLink link, boolean is_source)
	{
		SchemeCableLink sl = link.getSchemeCableLink();
		if (sl == null)
		{
			System.err.println("GraphActions.disconnectSchemeCableLink() link not found " + link.getSchemeCableLinkId());
			return;
		}
		SchemeCablePort sp;
		if (is_source)
		{
			sp = sl.sourceSchemeCablePort();
			sl.sourceSchemeCablePort(null);
		}
		else
		{
			sp = sl.targetSchemeCablePort();
			sl.targetSchemeCablePort(null);
		}

		sp.schemeCableLink(null);
		PortType pt = sp.portTypeImpl();
		if (pt == null)
				GraphActions.setObjectBackColor(graph, sp, Color.red);
			else
				GraphActions.setObjectBackColor(graph, sp, Color.yellow);
	}
}
