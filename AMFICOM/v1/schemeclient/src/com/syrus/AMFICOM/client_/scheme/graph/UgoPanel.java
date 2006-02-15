/*
 * $Id: UgoPanel.java,v 1.22 2006/02/15 12:18:10 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.event.UndoableEditEvent;

import com.jgraph.graph.CellView;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphUndoManager;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.22 $, $Date: 2006/02/15 12:18:10 $
 * @module schemeclient
 */

public class UgoPanel implements Printable, PropertyChangeListener {
	protected ApplicationContext aContext;
	protected SchemeGraph graph;
		
	protected GraphUndoManager undoManager = new GraphUndoManager() {
		private static final long serialVersionUID = 4110744288424174429L;

		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			super.undoableEditHappened(e);
			if (!UgoPanel.this.graph.make_notifications) {
				return;
			}
			((SchemeMarqueeHandler)UgoPanel.this.graph.getMarqueeHandler()).updateHistoryButtons(this);
//			UgoPanel.this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, UgoPanel.this.graph, SchemeEvent.SCHEME_CHANGED), false);
		}
	};
	
	protected UgoPanel(ApplicationContext aContext) {
		this.graph = new SchemeGraph(new DefaultGraphModel(), this.aContext);
		this.graph.setGridEnabled(true);
		this.graph.setGridVisible(false);
		this.graph.setGridVisibleAtActualSize(false);
		this.graph.setGridColor(Color.LIGHT_GRAY);
		this.graph.setBorderVisible(false);
		this.graph.setPortsVisible(false);
		this.graph.setRequestFocusEnabled(false);
		this.graph.setDragEnabled(false);
		this.graph.setDropEnabled(false);
		this.graph.setCloneable(false);
		this.graph.setAntiAliased(true);
		// this.graph.setEditable(false);
		// this.graph.setEnabled(false);
		this.graph.setBendable(true);
		this.graph.setDoubleBuffered(false);

		setContext(aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(SchemeEvent.TYPE, this);
		}
		if (aContext != null) {
			this.aContext = aContext;
			this.aContext.getDispatcher().addPropertyChangeListener(SchemeEvent.TYPE, this);
			this.graph.setContext(aContext);
		}
	}
	
	public String getReportTitle() {
		return LangModelSchematics.getString("elementsUGOTitle");
	}

	public SchemeGraph getGraph() {
		return this.graph;
	}
	
	public GraphUndoManager getGraphUndoManager() {
		return this.undoManager;
	}
	

	public void propertyChange(PropertyChangeEvent ev) {
		if (ev.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent)ev;
			if (see.isType(SchemeEvent.UPDATE_OBJECT)) {
				try {
					Identifier id = see.getIdentifier();
					if (id.getMajor() == ObjectEntities.SCHEMEELEMENT_CODE) {
						SchemeElement se = (SchemeElement)see.getStorableObject();
						DefaultGraphCell group = SchemeActions.findGroupById(this.graph, se.getId());
						if (group != null) {
							if (se.getLabel() != null)
								GraphActions.updateGroup(this.graph, group, se.getLabel());
							if (se.getSymbol() != null)
								GraphActions.updateGroup(this.graph, group, new ImageIcon(se.getSymbol().getImage()));
						}
					} else if (id.getMajor() == ObjectEntities.SCHEMEPROTOELEMENT_CODE) {
						SchemeProtoElement proto = (SchemeProtoElement)see.getStorableObject();
						DefaultGraphCell group = SchemeActions.findGroupById(this.graph, proto.getId());
						if (group != null) {
							if (proto.getLabel() != null)
								GraphActions.updateGroup(this.graph, group, proto.getLabel());
							if (proto.getSymbol() != null)
								GraphActions.updateGroup(this.graph, group, new ImageIcon(proto.getSymbol().getImage()));
						}
					} else if (id.getMajor() == ObjectEntities.SCHEMECABLELINK_CODE) {
						SchemeCableLink link = (SchemeCableLink)see.getStorableObject();
						DefaultGraphCell cell = SchemeActions.findSchemeCableLinkById(this.graph, link.getId());
						if (cell != null) {
							GraphActions.setText(this.graph, cell, link.getName());
						}
					} else if (id.getMajor() == ObjectEntities.SCHEMELINK_CODE) {
						SchemeLink link = (SchemeLink)see.getStorableObject();
						DefaultGraphCell cell = SchemeActions.findSchemeLinkById(this.graph, link.getId());
						if (cell != null) {
							GraphActions.setText(this.graph, cell, link.getName());
						}
					} else if (id.getMajor() == ObjectEntities.SCHEMEPORT_CODE) {
						SchemePort port = (SchemePort)see.getStorableObject();
						DefaultGraphCell cell = SchemeActions.findPortCellById(this.graph, port.getId());
						if (cell != null) {
							GraphActions.setText(this.graph, cell, port.getName());
							GraphActions.setObjectBackColor(this.graph, cell, SchemeActions.determinePortColor(port, port.getAbstractSchemeLink()));
						}
						cell = SchemeActions.findBlockPortCellById(this.graph, port.getId());
						if (cell != null) {
							GraphActions.setText(this.graph, cell, port.getName());
						}
					} else if (id.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
						SchemeCablePort port = (SchemeCablePort)see.getStorableObject();
						DefaultGraphCell cell = SchemeActions.findCablePortCellById(this.graph, port.getId());
						if (cell != null) {
							GraphActions.setText(this.graph, cell, port.getName());
							
							if (port.getAbstractSchemeLink() != null)
								GraphActions.setObjectBackColor(this.graph, cell, Color.WHITE);
							else
								GraphActions.setObjectBackColor(this.graph, cell, Color.YELLOW);
						}
						cell = SchemeActions.findBlockPortCellById(this.graph, port.getId());
						if (cell != null) {
							GraphActions.setText(this.graph, cell, port.getName());
						}
					} else if (id.getMajor() == ObjectEntities.SCHEMEPATH_CODE) {
//						if (this instanceof ElementsPanel) {
//							((ElementsPanel)this).getSchemeResource().setCashedPathMemberIds(null);
//						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
	}

	public int print(final Graphics g, final PageFormat pf, final int pi) {
		if (pi > 0) {
			return Printable.NO_SUCH_PAGE;
		}

		this.graph.printAll(g);
		return Printable.PAGE_EXISTS;
	}

	public void setSelectionAttributes(Map map) {
		final Object[] cells = DefaultGraphModel.getDescendants(this.graph.getModel(), this.graph.getSelectionCells()).toArray();
		map = GraphConstants.cloneMap(map);
		map.remove(GraphConstants.BOUNDS);
		map.remove(GraphConstants.POINTS);
		if (cells != null && cells.length > 0 && !map.isEmpty()) {
			final CellView[] views = this.graph.getGraphLayoutCache().getMapping(cells);
			final Map<CellView, Map> viewMap = new HashMap<CellView, Map>();
			for (int i = 0; i < views.length; i++) {
				viewMap.put(views[i], GraphConstants.cloneMap(map));
			}
			this.graph.getGraphLayoutCache().edit(viewMap, null, null, null);
		}
	}

	public void setGraphSize(final Dimension d) {
		this.graph.setActualSize(d);
	}
}
