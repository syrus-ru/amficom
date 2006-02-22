/*
 * $Id: SchemeGraph.java,v 1.25 2006/02/22 07:08:11 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.tree.TreeNode;

import com.jgraph.graph.CellMapper;
import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.Edge;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphCell;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphLayoutCache;
import com.jgraph.graph.GraphModel;
import com.jgraph.graph.VertexView;
import com.jgraph.pad.EllipseCell;
import com.jgraph.pad.GPGraph;
import com.jgraph.plaf.GraphUI;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.LinkView;
import com.syrus.AMFICOM.client_.scheme.graph.objects.Rack;
import com.syrus.AMFICOM.client_.scheme.graph.objects.RackView;
import com.syrus.AMFICOM.client_.scheme.graph.objects.SchemeEllipseView;
import com.syrus.AMFICOM.client_.scheme.graph.objects.SchemeVertexView;
import com.syrus.AMFICOM.client_.scheme.graph.objects.TopLevelElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.util.Log;


/**
 * @author $Author: stas $
 * @version $Revision: 1.25 $, $Date: 2006/02/22 07:08:11 $
 * @module schemeclient
 */

public class SchemeGraph extends GPGraph {
	private static final long serialVersionUID = -1663840885231780078L;

	ApplicationContext aContext;
	 // variables for graph representation
	private Dimension actualSize = Constants.A4;
	private boolean isGridVisibleAtActualSize = false;
	private boolean isBorderVisible = false;
	private boolean graphChanged = false;	
		
	private boolean notifying = false;
	boolean make_notifications = false;
	/**
	 * trigger between path selection modes
	 */
	private static String mode = Constants.LINK_MODE;

	public SchemeGraph(ApplicationContext aContext) {
		this(null, aContext);
	}

	public SchemeGraph(GraphModel model, ApplicationContext aContext) {
		this(model, null, aContext);
	}

	public SchemeGraph(GraphModel model, GraphLayoutCache view,	ApplicationContext aContext) {
		super(model, view);

		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext) {
		if (aContext != null) {
			this.aContext = aContext;
		}
	}

	public void setActualSize(Dimension d) {
		int w = (int)(getScale() * d.width);
		int h = (int) (getScale() * d.height);
		Dimension d1 = getPreferredSize();
		if (d1.width != w || d1.height != h) {
			super.setPreferredSize(new Dimension(w, h));
			this.updateUI();
		}
		this.actualSize = d;
	}
	
	public Dimension getActualSize() {
		return this.actualSize;
	}

	// objects view substitution
	@Override
	public DefaultGraphCell addVertex(Object userObject, Rectangle bounds, boolean autosize, Color border) {
		return GraphActions.addVertex(this, userObject, bounds, autosize, false, true, border);
	}

	@Override
	protected VertexView createVertexView(Object v, CellMapper cm) {
		if (v instanceof DeviceCell) 		// handle size
			return ((DeviceCell)v).getView(this, cm);
		if (v instanceof EllipseCell || v instanceof TopLevelElement)	//	round view 
			return new SchemeEllipseView(v, this, cm);
		if (v instanceof Rack)
			return new RackView(v, this, cm);
		return new SchemeVertexView(v, this, cm);	// square view
	}
	
	@Override
	protected EdgeView createEdgeView(Edge e, CellMapper cm) {
		return new LinkView(e, this, cm);
	}
	
	public int snap(int coord) {
		if (this.gridEnabled) {
			coord = Math.round(((float)coord / (float)this.gridSize)) * this.gridSize;
		}
		return coord;
	}
	
	// correcting mapping between screen and logical points
	@Override
	public Point snap(Point p) {
		if (this.gridEnabled && p != null) {
			p.x = Math.round(((float)p.x / (float)this.gridSize)) * this.gridSize;
			p.y = Math.round(((float)p.y / (float)this.gridSize)) * this.gridSize;
		}
		return p;
		
		/*Point p2 = new Point(fromScreen(p));
		if (this.gridEnabled && p != null) {
			p2.x = p.x + this.gridSize / 2;
			p2.y = p.y + this.gridSize / 2;
			p2.x = Math.round(p2.x / this.gridSize) * this.gridSize;
			p2.y = Math.round(p2.y / this.gridSize) * this.gridSize;
		}
		return toScreen(p2);*/
	}

	@Override
	public Dimension snap(Dimension d) {
		return super.snap(d);
	}

	public Rectangle snap(Rectangle r) {
		r.setLocation(snap(r.getLocation()));
		r.width = snap(r.width);
		r.height = snap(r.height);
		return r;
	}
	
	@Override
	public Point toScreen(Point p) {
		if (p == null)
			return null;
		p.x = (int) Math.round(p.x * this.scale);
		p.y = (int) Math.round(p.y * this.scale);
		return p;
	}
	
	@Override
	public Point fromScreen(Point p) {
		if (p == null)
			return null;
		p.x = (int) Math.round(p.x / this.scale);
		p.y = (int) Math.round(p.y / this.scale);
		return p;
	}

	public int fromScreen(int coord) {
		coord = (int) Math.round(coord / this.scale);
		return coord;
	}
	
	public int toScreen(int coord) {
		coord = (int) Math.round(coord * this.scale);
		return coord;
	}
	
	@Override
	public Rectangle toScreen(Rectangle rect) {
		if (rect == null)
			return null;
		rect.x *= this.scale;
		rect.y *= this.scale;
		rect.width *= this.scale;
		rect.height *= this.scale;
		return rect;
	}

	@Override
	public Rectangle fromScreen(Rectangle rect) {
		if (rect == null)
			return null;
		rect.x /= this.scale;
		rect.y /= this.scale;
		rect.width /= this.scale;
		rect.height /= this.scale;
		return rect;
	}

	public void selectionNotify() {
		if (!this.make_notifications)
			return;
		
		SchemeMarqueeHandler marqee = (SchemeMarqueeHandler)getMarqueeHandler();
		Object[] selected = getSelectionCells();
		marqee.updateButtonsState(selected);
		
		this.notifying = true;
		if (selected.length == 0) {
			UgoPanel panel = marqee.pane.getCurrentPanel();
			if (panel instanceof ElementsPanel) {
				SchemeResource res = ((ElementsPanel)panel).getSchemeResource();
				if (SchemeResource.getSchemePath() != null) {
					this.notifying = false;
					return;
				}
				try {
					SchemeCellContainer cellContainer = res.getCellContainer(); 
					if (cellContainer != null) {
						Notifier.notify(this, this.aContext, cellContainer);
						this.notifying = false;
						return;
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
		Notifier.notify(this, this.aContext, selected);
		this.notifying = false;
	}
	
	@Override
	public void addSelectionCell(Object cell) {
		if (!this.notifying)
			super.addSelectionCell(cell);
	}

	@Override
	public void addSelectionCells(Object[] cells) {
		if (!this.notifying)
			super.addSelectionCells(cells);
	}

	@Override
	public void setSelectionCell(Object cell) {
		if (!this.notifying)
			super.setSelectionCell(cell);
	}
	
	public void insureCellVisible(Object cell) {
		scrollCellToVisible(cell);
	}
	
	public void insureSelectionVisible() {
		Object cell = getSelectionCell();
		if (cell != null) {
			insureCellVisible(cell);
		}
	}
	
	@Override
	public void setSelectionCells(Object[] cells) {
		if (!this.notifying)
			super.setSelectionCells(cells);
	}
	
	@Override
	public void removeSelectionCell(Object cell) {
		if (!this.notifying)
			super.removeSelectionCell(cell);
	}
	
	// select cell notification
	/*public void addSelectionCell(Object cell) {
		super.addSelectionCell(cell);
		selectionNotify();
	}

	public void addSelectionCells(Object[] cells) {
		super.addSelectionCells(cells);
		selectionNotify();
	}

	public void setSelectionCell(Object cell) {
		super.setSelectionCell(cell);
		selectionNotify();
	}

	public void setSelectionCells(Object[] cells) {
		super.setSelectionCells(cells);
		selectionNotify();
	}

	public void removeSelectionCell(Object cell) {
		super.removeSelectionCell(cell);
		selectionNotify();
	}

	public void removeSelectionCells() {
		setSelectionCells(new Object[0]);
		selectionNotify();
	}*/

	@Override
	public void removeAll() {
		getModel().remove(getDescendants(getAll()));
	}
	
	// cells serialization
	@Override
	public Serializable getArchiveableState() {
		return getArchiveableState(getRoots());
	}

	public static Set getDescendants(GraphModel model, Object[] cells) {
		if (cells != null) {
			Set result = new HashSet();
			for (Object cell : cells) {
				addObject(result, model, cell);
			}
			return result;
		}
		return null;
	}
	
	private static void addObject(Set objects, GraphModel model, Object cell) {
		if (!objects.contains(cell)) {
			objects.add(cell);
			for (int i = 0; i < model.getChildCount(cell); i++) {
				addObject(objects, model, model.getChild(cell, i));
			}
		}
	}

	public static Set getDescendants1(Object[] cells) {
		if (cells != null) {
			Set result = new HashSet();
			for (Object cell : cells) {
				addObject(result, cell);
			}
			return result;
		}
		return Collections.emptySet();
	}
	
	public static Set<GraphCell> getDescendants2(Collection<GraphCell> cells) {
		if (cells != null) {
			Set<GraphCell> result = new HashSet<GraphCell>();
			for (GraphCell cell : cells) {
				addObject2(result, cell);
			}
			return result;
		}
		return Collections.emptySet();
	}
	
	private static void addObject(Set objects, Object cell) {
		if (!objects.contains(cell)) {
			objects.add(cell);
			if (cell instanceof TreeNode) {
				TreeNode node = (TreeNode)cell;
				for (int i = 0; i < node.getChildCount(); i++) {
					addObject(objects, node.getChildAt(i)); 
				}
			}
		}
	}
	
	private static void addObject2(Set<GraphCell> objects, GraphCell cell) {
		if (!objects.contains(cell)) {
			objects.add(cell);
			if (cell instanceof TreeNode) {
				TreeNode node = (TreeNode)cell;
				for (int i = 0; i < node.getChildCount(); i++) {
					addObject2(objects, (GraphCell)node.getChildAt(i)); 
				}
			}
		}
	}

	
	public Serializable getArchiveableState(Object[] cells) {
		Object[] flat = getDescendants(getModel(), cells).toArray();
		ConnectionSet cs = ConnectionSet.create(getModel(), flat, false);
		Map viewAttributes = GraphConstants.createAttributes(cells, getGraphLayoutCache());
		ArrayList v = new ArrayList(3);
		v.add(cells);
		v.add(viewAttributes);
		v.add(cs);
		return v;
	}

	public Map<DefaultGraphCell, DefaultGraphCell> setFromArchivedState(Object s) {
		if (s instanceof List) {
			List v = (List) s;
			Object[] cells = (Object[]) v.get(0);
			Map viewAttributes = (Map) v.get(1);
			ConnectionSet cs = (ConnectionSet) v.get(2);
			getGraphLayoutCache().insert(cells, viewAttributes, cs, null, null);
		}
		return Collections.emptyMap();
	}
	
	public Map<DefaultGraphCell, DefaultGraphCell> copyFromArchivedState(Object s, Point p, boolean isCenterPoint) {
		if (s instanceof List) {
			List v = (List) s;
			if (!v.isEmpty()) {
				Object[] cells = (Object[]) v.get(0);
				Map viewAttributes = (Map) v.get(1);
				ConnectionSet cs = (ConnectionSet) v.get(2);
				
				List<DeviceGroup> newGroups = new ArrayList<DeviceGroup>();
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] instanceof DeviceGroup)
						newGroups.add((DeviceGroup)cells[i]);
				}
				DeviceGroup[] groups = newGroups.toArray(new DeviceGroup[newGroups.size()]);
				
				if (groups.length == 0) {
					getGraphLayoutCache().insert(cells, viewAttributes, cs, null, null);
					return Collections.emptyMap();
				}
				
				// клонируем селлы
				Map<DefaultGraphCell, DefaultGraphCell> clones = super.cloneCells(cells);
				// клонируем аттрубуты
				Map cell_attr = GraphConstants.createAttributes(cells,
						getGraphLayoutCache());
				Map new_attributes = GraphConstants.cloneMap(cell_attr);
				// клонируем коннекшены
				// Object[] flat = getDescendants(cells);
				// ConnectionSet cs =
				// ConnectionSet.create(getGraphLayoutCache().getModel(), flat, false);
				cs = cs.clone(clones);
				// устанавливаем аттрибуты для клона
				new_attributes = GraphConstants.replaceKeys(clones, new_attributes);
				// вставляем клонированные селлы
				Object[] cloned_cells = clones.values().toArray();
				
				try {
					getGraphLayoutCache().insert(cloned_cells, viewAttributes, cs, null, null);
				} catch (Exception e) {
					Log.errorMessage(e);
				}
				
				if (p != null) { // переносим вставленный объект в новую точку
					GraphActions.move(this, cloned_cells, p, isCenterPoint);
				}
				return clones;
			}
			Log.debugMessage("Try open empty cell", Level.FINER);
		}
		return null;
	}
	
	public void setGraphEditable(boolean b) {
		setEditable(b);
		setSizeable(b);
	}

	// SchemeUI substitution
	@Override
	public void updateUI() {
		setUI(new SchemeGraphUI());
		invalidate();
	}
	@Override
	public GraphUI getUI() {
		return (SchemeGraphUI)this.ui;
	}
	
	boolean isBorderVisible() {
		return this.isBorderVisible;
	}
	void setBorderVisible(boolean isBorderVisible) {
		this.isBorderVisible = isBorderVisible;
	}
	public void setGridVisibleAtActualSize(boolean isGridVisibleAtActualSize) {
		this.isGridVisibleAtActualSize = isGridVisibleAtActualSize;
	}
	public boolean isGridVisibleAtActualSize() {
		return this.isGridVisibleAtActualSize;
	}
	public void setGraphChanged(boolean b) {
		this.graphChanged = b;
	}
	public boolean isGraphChanged() {
		return this.graphChanged;
	}
	public static void setMode(String mode1) {
		mode = mode1;
	}
	public static String getMode() {
		return mode;
	}
	public void setMakeNotifications(boolean make_notifications) {
		this.make_notifications = make_notifications;
	}
	public boolean isMakeNotifications() {
		return this.make_notifications;
	}
}
