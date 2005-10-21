/*
 * $Id: GraphActions.java,v 1.20 2005/10/21 16:46:20 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import com.jgraph.graph.AbstractCellView;
import com.jgraph.graph.CellView;
import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Edge;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.Port;
import com.jgraph.pad.ImageCell;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceView;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortEdge;
import com.syrus.AMFICOM.client_.scheme.graph.objects.Rack;
import com.syrus.AMFICOM.client_.scheme.graph.objects.SchemeVertexView;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.20 $, $Date: 2005/10/21 16:46:20 $
 * @module schemeclient
 */

class ByteHolder {
		ByteHolder(byte[] b){
			this.b = b;
		}
		byte[] b;
	}

public class GraphActions {
	private GraphActions () {
		// empty
	}
	
	public static Map<DefaultGraphCell, DefaultGraphCell> insertCell(SchemeGraph graph, List serialized, boolean clone) {
		return insertCell(graph, serialized, clone, null, false);
	}
	
	public static Map<DefaultGraphCell, DefaultGraphCell> insertCell(SchemeGraph graph, List serialized, boolean clone, Point p, boolean isCenterPoint) {
		if (serialized != null) {
			if (clone) {
				Map<DefaultGraphCell, DefaultGraphCell> clones = graph.copyFromArchivedState(serialized, p, isCenterPoint);
				return clones;
			}
			return graph.setFromArchivedState(serialized);
		}
		return null;
	}
	
	public static DefaultPort addPort(SchemeGraph graph, Object userObject,
			DefaultGraphCell cell, Point p) {
		CellView[] cv = graph.getGraphLayoutCache().getMapping(
				cell.getChildren().toArray(), true);
		Map<Object, Map> map = new HashMap<Object, Map>();
		for (int i = 0; i < cv.length; i++)
			map.put(cv[i].getCell(), cv[i].getAllAttributes());

		DefaultPort port = addPort(userObject, cell, p, map);
		cell.changeAttributes(map);
		graph.getGraphLayoutCache().edit(map, null, null, null);
		return port;
	}

	private static DefaultPort addPort(Object userObject,
			DefaultGraphCell cell, Point p, Map<Object, Map> viewMap) {
		DefaultPort port = new DefaultPort(userObject);
		cell.add(port);
		Map map = GraphConstants.createMap();
		GraphConstants.setOffset(map, p);
		GraphConstants.setConnectable(map, false);
		viewMap.put(port, map);
		return port;
	}

	public static DefaultPort removePort(DefaultGraphCell cell, DefaultPort port, Map viewMap) {
		cell.remove(port);
		viewMap.remove(port);
		return port;
	}

	public static DefaultGraphCell addVertex(SchemeGraph graph,
			Object userObject, Rectangle bounds, boolean autosize, boolean opaque,
			boolean sizable, Color border) {
		Map<DefaultGraphCell, Map> viewMap = new HashMap<DefaultGraphCell, Map>();

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
		boolean b = graph.isMakeNotifications();
		graph.setMakeNotifications(false);
		graph.setSelectionCells(new Object[0]);
		Object[] cells = graph.getAll();
		if (cells.length != 0) {
			graph.getModel().remove(cells);
		}
		graph.setMakeNotifications(b);
	}
	
	public static void removeCells(SchemeGraph graph, Object[] cells) {
		boolean b = graph.isMakeNotifications();
		graph.setMakeNotifications(false);
		graph.setSelectionCells(new Object[0]);
		if (cells.length != 0) {
			graph.getModel().remove(cells);
		}
		graph.setMakeNotifications(b);
	}
	
	public static void move(SchemeGraph graph, Object[] cells, Point p, boolean isCenterPoint) {
		Rectangle rect;
		CellView[] cv = graph.getGraphLayoutCache().getMapping(cells);
		AbstractCellView topcv = (AbstractCellView)cv[0];
		for (int i = 0; i < cv.length; i++)
			if (cv[i] instanceof DeviceView) {
				topcv = (AbstractCellView)cv[i];
				break;
			}
		if (topcv instanceof SchemeVertexView)
			rect = ((SchemeVertexView)topcv).getPureBounds();
		else
			rect = topcv.getBounds();

		Point setpoint = graph.snap(graph.fromScreen(p));
		Point p0 = graph.snap(graph.fromScreen(rect.getLocation()));
		int x = graph.snap((setpoint.x - p0.x - (isCenterPoint ? rect.width / 2 : 0)) / 2);  
		int y = graph.snap((setpoint.y - p0.y - (isCenterPoint ? rect.height / 2 : 0)) / 2);
		translateViews(cv, x, y);
		graph.getGraphLayoutCache().refresh(cv, true);
	}
	
	private static void translateViews(CellView[] views, int dx, int dy) {
		views = AbstractCellView.getDescendantViews(views);
		for (int i = 0; i < views.length; i++) {
			if (views[i].isLeaf()) {
				translate(views[i].getAllAttributes(), dx, dy);
			}
		}
	}
	
	private static void translate(Map map, int dx, int dy) {
		// Translate Bounds
		if (GraphConstants.isMoveable(map)) {
			Rectangle bounds = GraphConstants.getBounds(map);
			if (bounds != null) {
				bounds.translate(dx, dy);
			}
			// Translate Points
			java.util.List points = GraphConstants.getPoints(map);
			if (points != null) {
				for (int i = 0; i < points.size(); i++) {
					Object obj = points.get(i);
					if (obj instanceof Point) {
						 ((Point) obj).translate(dx, dy);
					}
				}
			}
		}
	}
	
	public static Map<Object, Object> cloneMap(Map map) {
		Map<Object, Object> clone = new HashMap<Object, Object>(map);
		return clone;
	}
	
	public static void connect(SchemeGraph graph, DefaultCableLink link, CablePortCell port, boolean isSource) {
		connect(graph, (DefaultEdge)link, (DefaultGraphCell)port, isSource);
	}
	
	public static void connect(SchemeGraph graph, DefaultLink link, PortCell port, boolean isSource) {
		connect(graph, (DefaultEdge)link, (DefaultGraphCell)port, isSource);
	}
	
	private static void connect(SchemeGraph graph, DefaultEdge edge, DefaultGraphCell port, boolean isSource) {
		// find com.jgraph.graph.Port to connect
		DefaultPort p = null;
		for (Enumeration<DefaultPort> en = port.children(); en.hasMoreElements();) {
			p = en.nextElement();
			if (p.getUserObject().equals("Center")) {
				break;
			}
		}
		if (p != null) {
			CellView view = graph.getGraphLayoutCache().getMapping(edge, true);
			Map nested = GraphConstants.createAttributes(new CellView[] { view }, null);
			ConnectionSet cs = new ConnectionSet();
			cs.connect(edge, p, isSource);
			if (isSource) {
				edge.setSource(p);
			} else {
				edge.setTarget(p);
			}
			graph.getGraphLayoutCache().edit(nested, cs, null, null);
		}		
	}
	
	public static Rectangle getBounds(SchemeGraph graph, Collection<Object> cells) {
		Rectangle r = null;
		for (Object obj : cells) {
			if (obj instanceof DeviceGroup) {
				Rectangle r1 = getGroupBounds(graph, (DeviceGroup)obj);
				if (r == null) {
					r = r1;
				} else {
					r = r.union(r1);
				}
			}
		}
		return r;
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
	
	public static DeviceCell getMainCell(DeviceGroup group) {
		for (Iterator it = group.getChildren().iterator(); it.hasNext(); ) {
			Object child = it.next();
			if (child instanceof DeviceCell) {
				return (DeviceCell) child;
			}
		}
		return null;
	}

	public static void setObjectColor(SchemeGraph graph, Object obj, Color color) {
		if (obj instanceof Edge) {
			setEdgeColor(graph, obj, color);
		}
	}
	
	public static void setObjectsBackColor(SchemeGraph graph, Object[] objs,
			Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setBackground(map, color);
		GraphConstants.setOpaque(map, true);
		Map<Object, Map> viewMap = new HashMap<Object, Map>();

		for (int i = 0; i < objs.length; i++)
			viewMap.put(objs[i], GraphConstants.cloneMap(map));

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setObjectBackColor(SchemeGraph graph, Object obj,
			Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setBackground(map, color);
		GraphConstants.setOpaque(map, true);
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		viewMap.put(obj, GraphConstants.cloneMap(map));
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setEdgeColor(SchemeGraph graph, Object[] edges, Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setLineColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map<Object, Map> viewMap = new HashMap<Object, Map>();

		for (int i = 0; i < edges.length; i++)
			viewMap.put(edges[i], GraphConstants.cloneMap(map));

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setEdgeColor(SchemeGraph graph, Object edge, Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setLineColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		viewMap.put(edge, GraphConstants.cloneMap(map));
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setObjectsForeColor(SchemeGraph graph, Object[] objs,
			Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setBorderColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map<Object, Map> viewMap = new HashMap<Object, Map>();

		for (int i = 0; i < objs.length; i++)
			viewMap.put(objs[i], GraphConstants.cloneMap(map));

		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setObjectForeColor(SchemeGraph graph, Object obj,
			Color color) {
		Map map = GraphConstants.createMap();
		GraphConstants.setBorderColor(map, color);
		GraphConstants.setForeground(map, graph.getForeground());
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		viewMap.put(obj, GraphConstants.cloneMap(map));
		graph.getGraphLayoutCache().edit(viewMap, null, null, null);
	}

	public static void setText(SchemeGraph graph, Object obj, String text) {
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
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
	
	public static void updateGroup(SchemeGraph graph, DefaultGraphCell group, String text) {
		if (group.getChildCount() > 0) {
			for (Enumeration en = group.children(); en.hasMoreElements();) {
				Object child = en.nextElement();
				if (child instanceof DeviceCell) {
					setText(graph, child, text);
					break;
				}
			}
		} 
		else {
			setText(graph, group, text);
		}
	}
	
	public static void updateGroup(SchemeGraph graph, DefaultGraphCell group, ImageIcon icon) {
		if (group.getChildCount() > 0) {
			for (Enumeration en = group.children(); en.hasMoreElements();) {
				Object child = en.nextElement();
				if (child instanceof DeviceCell) {
					setImage(graph, ((DeviceCell) child), icon);
					break;
				}
			}
		} 
		else {
			setImage(graph, group, icon);
		}
	}

	public static void setImage(SchemeGraph graph, DefaultGraphCell cell, ImageIcon icon) {
		if (icon == null) {
			Map map = cell.getAttributes();
			map.remove(GraphConstants.ICON);
			Map<Object, Map> viewMap = new HashMap<Object, Map>();
			viewMap.put(cell, GraphConstants.cloneMap(map));
			graph.getGraphLayoutCache().edit(viewMap, null, null, null);
		} else {
			if (icon.getIconHeight() > 20 || icon.getIconWidth() > 20) {
				icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
			}
			Map map = GraphConstants.createMap();
			GraphConstants.setIcon(map, icon);
			Map<Object, Map> viewMap = new HashMap<Object, Map>();
			viewMap.put(cell, GraphConstants.cloneMap(map));
			graph.getGraphLayoutCache().edit(viewMap, null, null, null);
		}
	}

	public static ImageIcon getImage(DefaultGraphCell cell) {
		Map map = cell.getAttributes();
		return GraphConstants.getIcon(map);
	}

	public static void setResizable(SchemeGraph graph, Object[] cells, boolean b) {
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
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
			if (node.getParent() instanceof DeviceGroup || node.getParent() instanceof Rack)
				return true;
			return hasGroupedParent(node.getParent());
		}
		return false;
	}

	public static void alignToGrid(SchemeGraph graph, Object[] cells) {
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
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
					if (((PortCell) cells[i]).getSchemePort().getDirectionType() ==
							IdlDirectionType._OUT) {
						bounds.x = ((bounds.x / grid) + 1) * grid - 6;
						bounds.y = ((bounds.y / grid) + 1) * grid - 3;
					} else {
						bounds.x = (bounds.x / grid) * grid;
						bounds.y = ((bounds.y / grid) + 1) * grid - 3;
					}
				} else if (cells[i] instanceof CablePortCell) {
					if (((CablePortCell) cells[i]).getSchemeCablePort().getDirectionType()
							== IdlDirectionType._OUT) {
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

	public static DefaultGraphCell[] findVisualPorts(SchemeGraph graph, DeviceCell cell) {
		ArrayList<DefaultGraphCell> v = new ArrayList<DefaultGraphCell>();
		for (Enumeration enumeration = cell.children(); enumeration.hasMoreElements();) {
			Object obj = enumeration.nextElement();
			if (obj instanceof Port) {
				Port p = (Port) obj;
				for (Iterator i = p.edges(); i.hasNext();) {
					DefaultEdge edge = (DefaultEdge) i.next();
					Object p2 = DefaultGraphModel.getTargetVertex(graph.getModel(), edge);
					if (p2 instanceof PortCell || p2 instanceof CablePortCell)
						v.add((DefaultGraphCell)p2);

				}
			}
		}
		return v.toArray(new DefaultGraphCell[v.size()]);
	}

	public static Edge[] findAllVertexEdges(DefaultGraphCell[] cells) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < cells.length; i++) {
			for (Enumeration enumeration = cells[i].children(); enumeration
					.hasMoreElements();) {
				Object obj = enumeration.nextElement();
				if (obj instanceof Port) {
					Port p = (Port) obj;
					for (Iterator j = p.edges(); j.hasNext();)
						edges.add((Edge)j.next());
				}
			}
		}
		return edges.toArray(new Edge[edges.size()]);
	}

	public static BlockPortCell[] findTopLevelPorts(SchemeGraph graph, DeviceGroup group) {
		ArrayList<BlockPortCell> v = new ArrayList<BlockPortCell>();
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
		return v.toArray(new BlockPortCell[v.size()]);
	}

	public static DefaultGraphCell[] getTopLevelCells(Object[] cells) {
		Set<DefaultGraphCell> v = new HashSet<DefaultGraphCell>();

		for (int i = 0; i < cells.length; i++) {
			DefaultGraphCell node = (DefaultGraphCell)cells[i];
			while (node.getParent() != null) {
				node = (DefaultGraphCell)node.getParent();
			}
			v.add(node);
		}
		return  v.toArray(new DefaultGraphCell[v.size()]);
	}
	
	public static DeviceGroup[] findTopLevelGroups(SchemeGraph graph, Object[] cells) {
		ArrayList<DeviceGroup> v = new ArrayList<DeviceGroup>();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof DeviceGroup && !hasGroupedParent(objs[i]))
				v.add((DeviceGroup)objs[i]);
		return  v.toArray(new DeviceGroup[v.size()]);
	}
	
	public static DeviceGroup[] findAllGroups(SchemeGraph graph, Object[] cells) {
		ArrayList<DeviceGroup> v = new ArrayList<DeviceGroup>();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof DeviceGroup)
				v.add((DeviceGroup)objs[i]);
		return v.toArray(new DeviceGroup[v.size()]);
	}

	public static DefaultLink[] findTopLevelLinks(SchemeGraph graph, Object[] cells) {
		List<DefaultLink> v = new ArrayList<DefaultLink>();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof DefaultLink && !hasGroupedParent(objs[i]))
				v.add((DefaultLink)objs[i]);
		return v.toArray(new DefaultLink[v.size()]);
	}

	public static BlockPortCell[] findTopLevelPorts(SchemeGraph graph, Object[] cells) {
		ArrayList<BlockPortCell> v = new ArrayList<BlockPortCell>();
		Object[] objs = graph.getDescendants(cells);
		for (int i = 0; i < objs.length; i++)
			if (objs[i] instanceof BlockPortCell)
				v.add((BlockPortCell)objs[i]);
		return v.toArray(new BlockPortCell[v.size()]);
	}
/*
	static void connectEdge(SchemeGraph graph, DefaultEdge edge,
			DefaultPort port, boolean is_source) {
		Map viewMap = new HashMap();
		Map map = edge.getAttributes();
		viewMap.put(edge, GraphConstants.cloneMap(map));
		ConnectionSet cs = new ConnectionSet();
		cs.connect(edge, port, is_source);
		graph.getModel().edit(viewMap, cs, null, null);
	}
*/
	static void disconnectEdge(SchemeGraph graph, DefaultEdge edge, DefaultPort port, boolean is_source) {
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		Map map = edge.getAttributes();
		viewMap.put(edge, GraphConstants.cloneMap(map));
		ConnectionSet cs = new ConnectionSet();
		graph.getModel().edit(viewMap, cs, null, null);
	}
}