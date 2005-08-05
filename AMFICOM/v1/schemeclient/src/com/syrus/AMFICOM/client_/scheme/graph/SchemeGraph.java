/*
 * $Id: SchemeGraph.java,v 1.7 2005/08/05 12:39:59 stas Exp $
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.jgraph.graph.CellMapper;
import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.Edge;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphLayoutCache;
import com.jgraph.graph.GraphModel;
import com.jgraph.graph.VertexView;
import com.jgraph.pad.GPGraph;
import com.jgraph.plaf.GraphUI;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultPortView;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceView;
import com.syrus.AMFICOM.client_.scheme.graph.objects.LinkView;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.SchemeVertexView;


/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/08/05 12:39:59 $
 * @module schemeclient_v1
 */

public class SchemeGraph extends GPGraph {
	private static final long serialVersionUID = -1663840885231780078L;

	ApplicationContext aContext;
	 // variables for graph representation
	private Dimension actualSize = Constants.A4;
	private boolean isGridVisibleAtActualSize = false;
	private boolean isBorderVisible = false;
	private boolean graphChanged = false;	
	private boolean topLevelSchemeMode = false;
	
	private boolean notify = false;
	boolean make_notifications = false;
	/**
	 * trigger between path selection modes
	 */
	private String mode = Constants.LINK_MODE;

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

	public void setTopLevelSchemeMode(boolean b) {
		this.topLevelSchemeMode = b;
		if (b)
			SchemeActions.generateTopLevelScheme(this);
	}

	public boolean isTopLevelSchemeMode() {
		return this.topLevelSchemeMode;
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
	public DefaultGraphCell addVertex(Object userObject, Rectangle bounds, boolean autosize, Color border) {
		return GraphActions.addVertex(this, userObject, bounds, autosize, false, true, border);
	}

	protected VertexView createVertexView(Object v, CellMapper cm) {
		if (v instanceof DeviceCell)
			return new DeviceView(v, this, cm);
		else if (v instanceof PortCell)
			return new DefaultPortView(v, this, cm);
		else if (v instanceof DeviceGroup)
			return new SchemeVertexView(v, this, cm);
		return super.createVertexView(v, cm);
	}
	
	protected EdgeView createEdgeView(Edge e, CellMapper cm) {
		return new LinkView(e, this, cm);
	}
	
	// correcting mapping between screen and logical points
	public Point snap(Point p) {
		Point p2 = new Point(fromScreen(p));
		if (this.gridEnabled && p != null) {
			p2.x = p.x + this.gridSize / 2;
			p2.y = p.y + this.gridSize / 2;
			p2.x = Math.round(p2.x / this.gridSize) * this.gridSize;
			p2.y = Math.round(p2.y / this.gridSize) * this.gridSize;
		}
		return toScreen(p2);
	}

	public Dimension snap(Dimension d) {
		return super.snap(d);
	}

	public Point toScreen(Point p) {
		if (p == null)
			return null;
		p.x = (int) Math.round(p.x * this.scale);
		p.y = (int) Math.round(p.y * this.scale);
		return p;
	}

	public Point fromScreen(Point p) {
		if (p == null)
			return null;
		p.x = (int) Math.round(p.x / this.scale);
		p.y = (int) Math.round(p.y / this.scale);
		return p;
	}

	public Rectangle toScreen(Rectangle rect) {
		if (rect == null)
			return null;
		rect.x *= this.scale;
		rect.y *= this.scale;
		rect.width *= this.scale;
		rect.height *= this.scale;
		return rect;
	}

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
		marqee.updateButtonsState(getSelectionCells());
		
		this.notify = true;
		if (getSelectionCount() == 0) {
			UgoPanel panel = marqee.pane.getCurrentPanel();
			if (panel instanceof ElementsPanel) {
				SchemeResource res = ((ElementsPanel)panel).getSchemeResource();
				if (res.getCellContainer() != null) {
					Notifier.notify(this, this.aContext, res.getCellContainer());
					this.notify = false;
					return;
				}
			}
		}
		Notifier.notify(this, this.aContext, getSelectionCells());
		this.notify = false;
	}
	
	public void addSelectionCell(Object cell) {
		if (!this.notify)
			super.addSelectionCell(cell);
	}

	public void addSelectionCells(Object[] cells) {
		if (!this.notify)
			super.addSelectionCells(cells);
	}

	public void setSelectionCell(Object cell) {
		if (!this.notify)
			super.setSelectionCell(cell);
	}
	
	public void setSelectionCells(Object[] cells) {
		if (!this.notify)
			super.setSelectionCells(cells);
	}
	
	public void removeSelectionCell(Object cell) {
		if (!this.notify)
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

	public void removeAll() {
		getModel().remove(getDescendants(getAll()));
	}
	
	// cells serialization
	public Serializable getArchiveableState() {
		return getArchiveableState(getRoots());
	}

	public Serializable getArchiveableState(Object[] cells) {
		Object[] flat = DefaultGraphModel.getDescendants(getModel(), cells).toArray();
		ConnectionSet cs = ConnectionSet.create(getModel(), flat, false);
		Map viewAttributes = GraphConstants.createAttributes(cells, getGraphLayoutCache());
		ArrayList<Object> v = new ArrayList<Object>(3);
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
	
	public Map<DefaultGraphCell, DefaultGraphCell> copyFromArchivedState(Object s, Point p) {
		if (s instanceof List) {
			List v = (List) s;
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
			Map<DefaultGraphCell, DefaultGraphCell> clones = cloneCells(cells);
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
			getGraphLayoutCache().insert(cloned_cells, viewAttributes, cs, null, null);

			if (p != null) { // переносим вставленный объект в новую точку
				// Point setpoint = snap(p);
				// Rectangle rect;
				// CellView[] cv = getGraphLayoutCache().getMapping(cloned_cells);
				// //CellView[] cv2 = getGraphLayoutCache().getAllDescendants(cv);
				// CellView topcv = cv[0];
				// for (int i = 0; i < cv.length; i++)
				// if (cv[i] instanceof DeviceView)
				// {
				// topcv = cv[i];
				// break;
				// }
				// if (topcv instanceof SchemeVertexView)
				// rect = ((SchemeVertexView)topcv).getPureBounds();
				// else
				// rect = topcv.getBounds();
				//
				// p = snap(new Point((int)(p.x / (2 * getScale()) - rect.x / 2),
				// (int)(p.y / (2 * getScale()) - rect.y / 2)));
				// //getGraphLayoutCache().update(getGraphLayoutCache().getAllDescendants(cv));
				// getGraphLayoutCache().setRememberCellViews(false);
				// getGraphLayoutCache().translateViews(cv, p.x, p.y);
				// getGraphLayoutCache().update(cv);
			}
			return clones;
		}
		return null;
	}
	
	public void setGraphEditable(boolean b) {
		setEditable(b);
		setSizeable(b);
	}

	// SchemeUI substitution
	public void updateUI() {
		setUI(new SchemeGraphUI());
		invalidate();
	}
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
	void setGraphChanged(boolean b) {
		this.graphChanged = b;
	}
	public boolean isGraphChanged() {
		return this.graphChanged;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setMakeNotifications(boolean make_notifications) {
		this.make_notifications = make_notifications;
	}
}
