/*
 * $Id: GraphActions.java,v 1.2 2005/04/19 09:01:50 bass Exp $
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
import javax.swing.tree.TreeNode;

import com.jgraph.graph.*;
import com.jgraph.pad.ImageCell;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/04/19 09:01:50 $
 * @module schemeclient_v1
 */

public class GraphActions {
	private GraphActions () {
		// empty
	}
	
	public static DefaultPort addPort(SchemeGraph graph, Object userObject,
			DefaultGraphCell cell, Point p) {
		CellView[] cv = graph.getGraphLayoutCache().getMapping(
				cell.getChildren().toArray(), true);
		Map map = new HashMap();
		for (int i = 0; i < cv.length; i++)
			map.put(cv[i].getCell(), cv[0].getAllAttributes());

		DefaultPort port = addPort(userObject, cell, p, map);
		cell.changeAttributes(map);
		graph.getGraphLayoutCache().edit(map, null, null, null);
		return port;
	}

	private static DefaultPort addPort(Object userObject,
			DefaultGraphCell cell, Point p, Map viewMap) {
		DefaultPort port = new DefaultPort(userObject);
		cell.add(port);
		Map map = GraphConstants.createMap();
		GraphConstants.setOffset(map, p);
		GraphConstants.setConnectable(map, false);
		viewMap.put(port, map);
		return port;
	}

	public static DefaultPort removePort(SchemeGraph graph, DefaultGraphCell cell,
			DefaultPort port, Map viewMap) {
		cell.remove(port);
		viewMap.remove(port);
		return port;
	}

	public static DefaultGraphCell addVertex(SchemeGraph graph,
			Object userObject, Rectangle bounds, boolean autosize, boolean opaque,
			boolean sizable, Color border) {
		Map viewMap = new HashMap();

		// Create Vertex
		Object obj = (userObject instanceof String) ? userObject : "";
		DefaultGraphCell cell;
		if (userObject instanceof ImageIcon)
			cell = new ImageCell(obj);
		else
			cell = new DefaultGraphCell(obj);
		Map map = GraphConstants.createMap();
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
		// Floating Center Port (Child 0 is Default)
		DefaultPort port = new DefaultPort("Center");
		cell.add(port);

		if (userObject instanceof ImageIcon) {
			GraphConstants.setIcon(map, (ImageIcon) userObject);
			// Single non-floating central-port
			map = GraphConstants.createMap();
			GraphConstants.setOffset(map, new Point(u / 2, u / 2));
			viewMap.put(port, map);
		}
		Object[] insert = new Object[] { cell };
		graph.getModel().insert(insert, viewMap, null, null, null);
		return cell;
	}

	public static void clearGraph(SchemeGraph graph) {
		// SchemeGraph.skip_notify = true;
		graph.setSelectionCells(new Object[0]);
		Object[] cells = graph.getAll();
		graph.getModel().remove(cells);
		// SchemeGraph.skip_notify = false;
	}
	
	public static Rectangle getGroupBounds(SchemeGraph graph, DeviceGroup group) {
		for (Iterator it = group.getChildren().iterator(); it.hasNext(); ) {
			Object child = it.next();
			if (child instanceof DeviceCell) {
				DeviceCell deviceCell = (DeviceCell) child;
				Map m = graph.getModel().getAttributes(deviceCell);
				return GraphConstants.getBounds(m);
			}
		}
		Map m = graph.getModel().getAttributes(group);
		return GraphConstants.getBounds(m);		
	}

	public static void setObjectsBackColor(SchemeGraph graph, Object[] objs,
			Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setBackground(map, color);
		GraphConstants.setOpaque(map, true);
		Map viewMap = new HashMap();

		for (int i = 0; i < objs.length; i++)
			viewMap.put(objs[i], GraphConstants.cloneMap(map));

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setObjectBackColor(SchemeGraph graph, Object obj,
			Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setBackground(map, color);
		GraphConstants.setOpaque(map, true);
		Map viewMap = new HashMap();
		viewMap.put(obj, GraphConstants.cloneMap(map));
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setEdgeColor(SchemeGraph graph, Object[] edges, Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setLineColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map viewMap = new HashMap();

		for (int i = 0; i < edges.length; i++)
			viewMap.put(edges[i], GraphConstants.cloneMap(map));

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setEdgeColor(SchemeGraph graph, Object edge, Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setLineColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map viewMap = new HashMap();
		viewMap.put(edge, GraphConstants.cloneMap(map));
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setObjectsForeColor(SchemeGraph graph, Object[] objs,
			Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setBorderColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map viewMap = new HashMap();

		for (int i = 0; i < objs.length; i++)
			viewMap.put(objs[i], GraphConstants.cloneMap(map));

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setObjectForeColor(SchemeGraph graph, Object obj,
			Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setBorderColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map viewMap = new HashMap();
		viewMap.put(obj, GraphConstants.cloneMap(map));
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setText(SchemeGraph graph, Object obj, String text) {
		Map viewMap = new HashMap();
		if (obj instanceof CablePortCell || obj instanceof PortCell) {
			DefaultGraphCell cell = (DefaultGraphCell) obj;
			for (Enumeration enumeration = cell.children(); enumeration
					.hasMoreElements();) {
				Port p = (Port) enumeration.nextElement();
				for (Iterator i = p.edges(); i.hasNext();) {
					DefaultEdge edge = (DefaultEdge) i.next();
					if (edge instanceof PortEdge) {
						Map map = edge.getAttributes();
						edge.setUserObject(text);
						viewMap.put(edge, GraphConstants.cloneMap(map));
						break;
					}
				}
			}
		} else if (obj instanceof DefaultGraphCell) {
			DefaultGraphCell cell = (DefaultGraphCell) obj;
			Map map = cell.getAttributes();
			cell.setUserObject(text);
			viewMap.put(cell, GraphConstants.cloneMap(map));
		}

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setImage(SchemeGraph graph, DefaultGraphCell cell,
			ImageIcon icon) {
		if (icon == null) {
			Map map = cell.getAttributes();
			map.remove(GraphConstants.ICON);
			Map viewMap = new HashMap();
			viewMap.put(cell, GraphConstants.cloneMap(map));
			graph.getGraphLayoutCache().edit(viewMap, null, null, null);
		} else {
			if (icon.getIconHeight() > 20 || icon.getIconWidth() > 20)
				icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20,
						Image.SCALE_SMOOTH));
			Map map = GraphConstants.createMap();
			GraphConstants.setIcon(map, icon);
			Map viewMap = new HashMap();
			viewMap.put(cell, GraphConstants.cloneMap(map));
			graph.getGraphLayoutCache().edit(viewMap, null, null, null);
		}
	}

	public static ImageIcon getImage(SchemeGraph graph, DefaultGraphCell cell) {
		Map map = cell.getAttributes();
		return GraphConstants.getIcon(map);
	}

	public static void setResizable(SchemeGraph graph, Object[] cells, boolean b) {
		Map viewMap = new HashMap();
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DefaultGraphCell) {
				Map m = GraphConstants.createMap();
				GraphConstants.setSizeable(m, b);
				viewMap.put(cells[i], m);
			}
		}
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static boolean hasGroupedParent(Object obj) {
		if (obj instanceof TreeNode) {
			TreeNode node = (TreeNode) obj;
			if (node.getParent() == null)
				return false;
			if (node.getParent() instanceof DeviceGroup)
				return true;
			return hasGroupedParent(node.getParent());
		}
		return false;
	}

	public static void alignToGrid(SchemeGraph graph, Object[] cells) {
		Map viewMap = new HashMap();
		int grid = graph.getGridSize();
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DefaultGraphCell) {
				DefaultGraphCell cell = (DefaultGraphCell) cells[i];
				Map map = cell.getAttributes();
				Rectangle bounds = GraphConstants.getBounds(map);

				if (cells[i] instanceof DeviceCell || cells[i] instanceof DeviceGroup) {
					bounds.x = (bounds.x / grid) * grid;
					bounds.y = (bounds.y / grid) * grid;
				} else if (cells[i] instanceof BlockPortCell) {
					bounds.x = (bounds.x / grid) * grid;
					bounds.y = ((bounds.y / grid) + 1) * grid - 5;
				} else if (cells[i] instanceof PortCell) {
					if (((PortCell) cells[i]).getSchemePort().getDirectionType().equals(
							AbstractSchemePortDirectionType._OUT)) {
						bounds.x = ((bounds.x / grid) + 1) * grid - 6;
						bounds.y = ((bounds.y / grid) + 1) * grid - 3;
					} else {
						bounds.x = (bounds.x / grid) * grid;
						bounds.y = ((bounds.y / grid) + 1) * grid - 3;
					}
				} else if (cells[i] instanceof CablePortCell) {
					if (((CablePortCell) cells[i]).getSchemeCablePort().getDirectionType()
							.equals(AbstractSchemePortDirectionType._OUT)) {
						bounds.x = ((bounds.x / grid) + 1) * grid - 6;
						bounds.y = ((bounds.y / grid) + 1) * grid - 3;
					} else {
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

	private static BlockPortCell getBlockPort(SchemeGraph graph, DefaultGraphCell port) {
		for (Enumeration enumeration = port.children(); enumeration
				.hasMoreElements();) {
			Object obj = enumeration.nextElement();
			if (obj instanceof Port) {
				Port p = (Port) obj;
				for (Iterator i = p.edges(); i.hasNext();) {
					DefaultEdge edge = (DefaultEdge) i.next();
					Object p2 = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
					if (p2 instanceof BlockPortCell)
						return (BlockPortCell) p2;
				}
			}
		}
		return null;
	}

	public static DefaultGraphCell[] findVisualPorts(SchemeGraph graph,
			DeviceCell cell) {
		ArrayList v = new ArrayList();
		for (Enumeration enumeration = cell.children(); enumeration
				.hasMoreElements();) {
			Object obj = enumeration.nextElement();
			if (obj instanceof Port) {
				Port p = (Port) obj;
				for (Iterator i = p.edges(); i.hasNext();) {
					DefaultEdge edge = (DefaultEdge) i.next();
					Object p2 = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
					if (p2 instanceof PortCell || p2 instanceof CablePortCell)
						v.add(p2);

				}
			}
		}
		return (DefaultGraphCell[]) v.toArray(new DefaultGraphCell[v.size()]);
	}

	public static Object[] findAllVertexEdges(SchemeGraph graph, DefaultGraphCell[] cells) {
		ArrayList edges = new ArrayList();
		for (int i = 0; i < cells.length; i++) {
			for (Enumeration enumeration = cells[i].children(); enumeration
					.hasMoreElements();) {
				Object obj = enumeration.nextElement();
				if (obj instanceof Port) {
					Port p = (Port) obj;
					for (Iterator j = p.edges(); j.hasNext();)
						edges.add(j.next());
				}
			}
		}
		return edges.toArray(new Object[edges.size()]);
	}

	public static BlockPortCell[] findTopLevelPorts(SchemeGraph graph,
			DeviceGroup group) {
		ArrayList v = new ArrayList();
		Object[] objs = graph.getDescendants(new Object[] { group });
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof DeviceCell) {
				DefaultGraphCell[] ports = findVisualPorts(graph, (DeviceCell) objs[i]);
				for (int j = 0; j < ports.length; j++) {
					BlockPortCell bpc = getBlockPort(graph, ports[j]);
					if (bpc != null)
						v.add(bpc);
				}
			}
		return (BlockPortCell[]) v.toArray(new BlockPortCell[v.size()]);
	}

	public static DeviceGroup[] findTopLevelGroups(SchemeGraph graph,
			Object[] cells) {
		ArrayList v = new ArrayList();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof DeviceGroup && !hasGroupedParent(objs[i]))
				v.add(objs[i]);
		return (DeviceGroup[]) v.toArray(new DeviceGroup[v.size()]);
	}

	public static DefaultLink[] findTopLevelLinks(SchemeGraph graph,
			Object[] cells) {
		List v = new ArrayList();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof DefaultLink && !hasGroupedParent(objs[i]))
				v.add(objs[i]);
		return (DefaultLink[])v.toArray(new DefaultLink[v.size()]);
	}

	public static BlockPortCell[] findTopLevelPorts(SchemeGraph graph,
			Object[] cells) {
		ArrayList v = new ArrayList();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof BlockPortCell)
				v.add(objs[i]);
		return (BlockPortCell[]) v.toArray(new BlockPortCell[v.size()]);
	}

	static void connectEdge(SchemeGraph graph, DefaultEdge edge,
			DefaultPort port, boolean is_source) {
		Map viewMap = new HashMap();
		Map map = edge.getAttributes();
		viewMap.put(edge, GraphConstants.cloneMap(map));
		ConnectionSet cs = new ConnectionSet();
		cs.connect(edge, port, is_source);
		graph.getModel().edit(viewMap, cs, null, null);
	}

	static void disconnectEdge(SchemeGraph graph, DefaultEdge edge,
			DefaultPort port, boolean is_source) {
		Map viewMap = new HashMap();
		Map map = edge.getAttributes();
		viewMap.put(edge, GraphConstants.cloneMap(map));
		ConnectionSet cs = new ConnectionSet();
		graph.getModel().edit(viewMap, cs, null, null);
	}
}