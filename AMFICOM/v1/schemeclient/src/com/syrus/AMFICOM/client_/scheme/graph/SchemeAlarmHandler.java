/*-
 * $Id: SchemeAlarmHandler.java,v 1.9 2005/12/06 14:14:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.GraphCell;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2005/12/06 14:14:18 $
 * @module schemeclient_v1
 */

public final class SchemeAlarmHandler implements PropertyChangeListener {
	private static final long REPAINT_TIME = 1000;
	
	private ApplicationContext aContext;
	final SchemeTabbedPane pane;
	
	private Map<ElementsPanel, AlarmPainter> paintersMap;
	private Map<Identifier, ElementsPanel> panelsMap;
	private Map<Identifier, DefaultGraphCell> cellsMap;
	
	public SchemeAlarmHandler(final ApplicationContext aContext, final SchemeTabbedPane pane) {
		this.pane = pane;
		setContext(aContext);
		
		this.paintersMap = new HashMap<ElementsPanel, AlarmPainter>();
		this.panelsMap = new HashMap<Identifier, ElementsPanel>();
		this.cellsMap = new HashMap<Identifier, DefaultGraphCell>();
	}
	
	public void setContext(final ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(MarkerEvent.MARKER_EVENT_TYPE, this);
		}
		this.aContext = aContext;
		this.aContext.getDispatcher().addPropertyChangeListener(MarkerEvent.MARKER_EVENT_TYPE, this);
	}
	
	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MarkerEvent.MARKER_EVENT_TYPE)) {
			final MarkerEvent event = (MarkerEvent)evt;
			if (event.getMarkerEventType() == MarkerEvent.ALARMMARKER_CREATED_EVENT) {
				try {
					final PathElement pathElement = StorableObjectPool.getStorableObject(
							event.getSchemePathElementId(), false);
					
					openCorrespondingPanel(pathElement);
					ElementsPanel panel = this.pane.getCurrentPanel();
					
					final DefaultGraphCell cell = SchemeActions.findObjectById(panel.getGraph(),
							pathElement.getAbstractSchemeElement().getId());
					
					if (cell != null) {
						this.cellsMap.put(event.getMarkerId(), cell);
						this.panelsMap.put(event.getMarkerId(), panel);
						
						AlarmPainter painter = this.paintersMap.get(panel);
						if (painter == null || painter.getState() == State.TERMINATED) {
							painter = new AlarmPainter(panel.getGraph(), cell);
							this.paintersMap.put(panel, painter);
							painter.start();
						} else {
							synchronized (this) {
								painter.addAlarmedCell(cell);
							}
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} else if (event.getMarkerEventType() == MarkerEvent.MARKER_DELETED_EVENT) {
				ElementsPanel panel = this.panelsMap.get(event.getMarkerId());
				if (panel != null) {
					AlarmPainter painter = this.paintersMap.get(panel);
					if (painter != null) {
						DefaultGraphCell cell = this.cellsMap.get(event.getMarkerId());
						synchronized (this) {
							painter.removeAlarmedCell(cell);
						}
					}
				}
			} else if (event.getMarkerEventType() == MarkerEvent.MARKER_SELECTED_EVENT) {
				ElementsPanel panel = this.panelsMap.get(event.getMarkerId());
				if (panel != null) {
					AlarmPainter painter = this.paintersMap.get(panel);
					DefaultGraphCell cell = this.cellsMap.get(event.getMarkerId());
					if (painter == null || painter.getState() == State.TERMINATED) {
							painter = new AlarmPainter(panel.getGraph(), cell);
							this.paintersMap.put(panel, painter);
							painter.start();
						} else {
							synchronized (this) {
								painter.addAlarmedCell(cell);
							}
						}
				}
			}
		}
	}
	
	private void openCorrespondingPanel(PathElement pathElement) throws ApplicationException {
		// Scheme (or SE) with alarmed element
		if (pathElement.getKind().equals(IdlKind.SCHEME_CABLE_LINK)) { 
			final SchemeCableLink schemeCableLink = pathElement.getSchemeCableLink();
			this.pane.openScheme(schemeCableLink.getParentScheme());
		} else if (pathElement.getKind().equals(IdlKind.SCHEME_LINK)) { 
			final SchemeLink schemeLink = pathElement.getSchemeLink();
			Scheme parentScheme = schemeLink.getParentScheme();
			if (parentScheme != null) {
				this.pane.openScheme(parentScheme);
			} else {
				SchemeElement parentSchemeElement = schemeLink.getParentSchemeElement();
				if (parentSchemeElement != null ) {
					SchemeCellContainer cellContainer = getNonEmptyParent(parentSchemeElement);
					if (cellContainer instanceof Scheme) {
						this.pane.openScheme((Scheme)cellContainer);
					} else if (cellContainer instanceof SchemeElement) {
						this.pane.openSchemeElement((SchemeElement)cellContainer);
					}
				}
			}
		} else if (pathElement.getKind().equals(IdlKind.SCHEME_ELEMENT)) { 
			final SchemeElement schemeElement = pathElement.getSchemeElement();
			Scheme parentScheme = schemeElement.getParentScheme();
			if (parentScheme != null) {
				this.pane.openScheme(parentScheme);
			} else {
				SchemeElement parentSchemeElement = schemeElement.getParentSchemeElement();
				if (parentSchemeElement != null ) {
					SchemeCellContainer cellContainer = getNonEmptyParent(parentSchemeElement);
					if (cellContainer instanceof Scheme) {
						this.pane.openScheme((Scheme)cellContainer);
					} else if (cellContainer instanceof SchemeElement) {
						this.pane.openSchemeElement((SchemeElement)cellContainer);
					}
				}
			}
		}
	}
	
	private SchemeCellContainer getNonEmptyParent(SchemeElement se) {
		if (se.getSchemeCell() != null && !se.getSchemeCell().getData().isEmpty()) {
			return se;
		}
		SchemeElement parent = se.getParentSchemeElement();
		if (parent != null) {
			return getNonEmptyParent(parent);
		}
		return se.getParentScheme();
	}
	
	private class AlarmPainter extends Thread {
		Set<DefaultGraphCell> cellsSet;
		GraphCell[] cells;
		SchemeGraph graph;
		
		AlarmPainter(SchemeGraph graph, DefaultGraphCell cell) {
			this.cellsSet = new HashSet<DefaultGraphCell>();
			this.graph = graph;
			addAlarmedCell(cell);
		}
		
		synchronized void addAlarmedCell(DefaultGraphCell cell) {
			if (this.cellsSet.contains(cell)) {
				return;
			}
			this.cellsSet.add(cell);
			
			Object[] allChilds = this.graph.getDescendants(this.cellsSet.toArray(new DefaultGraphCell[this.cellsSet.size()])); 
			List<Object> childList = new ArrayList<Object>();
			
			for (Object obj : allChilds) {
//				if (!(obj instanceof DefaultPort || obj instanceof PortEdge || obj instanceof DeviceGroup)) {
					childList.add(obj);
//				}
			}
			this.cells = childList.toArray(new GraphCell[childList.size()]);
		}
		
		synchronized void removeAlarmedCell(DefaultGraphCell cell) {
			
			Object[] cells1 = this.cellsSet.toArray(new DefaultGraphCell[this.cellsSet.size()]);
			Set descendants1 = SchemeGraph.getDescendants1(cells1);
			GraphActions.setObjectsColor(this.graph, (GraphCell[])descendants1.toArray(new GraphCell[descendants1.size()]), Color.BLACK);
			this.cellsSet.remove(cell);
			Object[] allChilds = this.graph.getDescendants(this.cellsSet.toArray(new DefaultGraphCell[this.cellsSet.size()])); 
			List<Object> childList = new ArrayList<Object>();
			
			for (Object obj : allChilds) {
//				if (!(obj instanceof DefaultPort || obj instanceof DeviceGroup)) {
					childList.add(obj);
//				}
			}
			this.cells = childList.toArray(new GraphCell[childList.size()]);
		}
		
		@Override
		public void run() {
				while (true) {
					try {
						sleep(REPAINT_TIME);
					} catch (InterruptedException e) {
						Log.errorMessage(e);
					}
					synchronized (this) {
						if (this.cells.length == 0) {
							break;
						}
						GraphActions.setObjectsColor(this.graph, this.cells, Color.RED);
					}
					try {
						sleep(REPAINT_TIME);
					} catch (InterruptedException e) {
						Log.errorMessage(e);
					}
					
					synchronized (this) {
						GraphActions.setObjectsColor(this.graph, this.cells, Color.BLACK);
					}
				}
		}
	}
}
