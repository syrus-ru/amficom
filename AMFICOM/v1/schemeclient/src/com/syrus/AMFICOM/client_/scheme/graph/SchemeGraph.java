/*
 * $Id: SchemeGraph.java,v 1.1 2005/04/05 14:07:53 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

import com.jgraph.graph.*;
import com.jgraph.pad.GPGraph;
import com.jgraph.plaf.GraphUI;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.*;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;


/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class SchemeGraph extends GPGraph {
	ApplicationContext aContext;
	 // variables for graph representation
	private Dimension actualSize = Constants.A4;
	private boolean isGridVisibleAtActualSize = false;
	private boolean isBorderVisible = false;
	private boolean graphChanged = false;	
	private boolean topLevelSchemeMode = false;
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
		topLevelSchemeMode = b;
		if (b)
			SchemeActions.generateTopLevelScheme(this);
	}

	public boolean isTopLevelSchemeMode() {
		return topLevelSchemeMode;
	}

	public void setActualSize(Dimension d) {
		super.setPreferredSize(new Dimension((int)(getScale() * d.width), (int) (getScale() * d.height)));
		actualSize = d;
	}
	
	public Dimension getActualSize() {
		return actualSize;
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
		return new LinkView(e, this, cm, 0);
	}
	
	// correcting mapping between screen and logical points
	public Point snap(Point p) {
		Point p2 = new Point(fromScreen(p));
		if (gridEnabled && p != null) {
			p2.x = p.x + gridSize / 2;
			p2.y = p.y + gridSize / 2;
			p2.x = Math.round(p2.x / gridSize) * gridSize;
			p2.y = Math.round(p2.y / gridSize) * gridSize;
		}
		return toScreen(p2);
	}

	public Dimension snap(Dimension d) {
		return super.snap(d);
	}

	public Point toScreen(Point p) {
		if (p == null)
			return null;
		p.x = (int) Math.round(p.x * scale);
		p.y = (int) Math.round(p.y * scale);
		return p;
	}

	public Point fromScreen(Point p) {
		if (p == null)
			return null;
		p.x = (int) Math.round(p.x / scale);
		p.y = (int) Math.round(p.y / scale);
		return p;
	}

	public Rectangle toScreen(Rectangle rect) {
		if (rect == null)
			return null;
		rect.x *= scale;
		rect.y *= scale;
		rect.width *= scale;
		rect.height *= scale;
		return rect;
	}

	public Rectangle fromScreen(Rectangle rect) {
		if (rect == null)
			return null;
		rect.x /= scale;
		rect.y /= scale;
		rect.width /= scale;
		rect.height /= scale;
		return rect;
	}

	public void selectionNotify() {
		// TODO make select notifications 
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
		ArrayList v = new ArrayList(3);
		v.add(cells);
		v.add(viewAttributes);
		v.add(cs);
		return v;
	}

	public Map setFromArchivedState(Object s) {
		if (s instanceof List) {
			List v = (List) s;
			Object[] cells = (Object[]) v.get(0);
			Map viewAttributes = (Map) v.get(1);
			ConnectionSet cs = (ConnectionSet) v.get(2);
			getGraphLayoutCache().insert(cells, viewAttributes, cs, null, null);
		}
		return Collections.EMPTY_MAP;
	}
	
	public Map copyFromArchivedState(Object s, Point p) {
		if (s instanceof List) {
			List v = (List) s;
			Object[] cells = (Object[]) v.get(0);
			Map viewAttributes = (Map) v.get(1);
			ConnectionSet cs = (ConnectionSet) v.get(2);

			ArrayList new_cells = new ArrayList();
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] instanceof DeviceGroup)
					new_cells.add(cells[i]);
			}
			DeviceGroup[] groups = (DeviceGroup[]) new_cells.toArray(new DeviceGroup[new_cells.size()]);

			if (groups.length == 0) {
				getGraphLayoutCache().insert(cells, viewAttributes, cs, null, null);
				return new HashMap();
			}

			// клонируем селлы
			Map clones = cloneCells(cells);
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
		return (SchemeGraphUI)ui;
	}
	
	boolean isBorderVisible() {
		return isBorderVisible;
	}
	void setBorderVisible(boolean isBorderVisible) {
		this.isBorderVisible = isBorderVisible;
	}
	public void setGridVisibleAtActualSize(boolean isGridVisibleAtActualSize) {
		this.isGridVisibleAtActualSize = isGridVisibleAtActualSize;
	}
	public boolean isGridVisibleAtActualSize() {
		return isGridVisibleAtActualSize;
	}
	void setGraphChanged(boolean b) {
		this.graphChanged = b;
	}
	public boolean isGraphChanged() {
		return graphChanged;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
}
