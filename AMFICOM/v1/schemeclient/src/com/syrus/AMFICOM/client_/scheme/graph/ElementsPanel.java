/*
 * $Id: ElementsPanel.java,v 1.23 2006/06/06 12:46:58 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import com.jgraph.graph.GraphModel;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.23 $, $Date: 2006/06/06 12:46:58 $
 * @module schemeclient
 */

public class ElementsPanel extends UgoPanel {
	SchemeResource schemeResource;
	private boolean topLevelSchemeMode = false;
	
	protected ElementsPanel(ApplicationContext aContext) {
		super(aContext);

		GraphModel model = this.graph.getModel();
		model.addUndoableEditListener(this.undoManager);
		
		this.graph.setEditable(true);
		
		this.graph.setGridEnabled(true);
		this.graph.setGridVisible(true);
		this.graph.setGridVisibleAtActualSize(true);
		this.graph.setRequestFocusEnabled(true);
		this.graph.setBendable(true);
		this.graph.setEditable(true);
		this.graph.setEnabled(true);
		this.graph.setAntiAliased(false);
		this.graph.make_notifications = true;
		this.schemeResource = new SchemeResource(this.graph);
	}

	@Override
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
			this.aContext.getDispatcher().removePropertyChangeListener(MarkerEvent.MARKER_EVENT_TYPE, this);
		}
		super.setContext(aContext);
		if (aContext != null) {
			this.aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
			this.aContext.getDispatcher().addPropertyChangeListener(MarkerEvent.MARKER_EVENT_TYPE, this);
		}
	}

	public SchemeResource getSchemeResource() {
		return this.schemeResource;
	}
	
	public boolean isTopLevelSchemeMode() {
		return this.topLevelSchemeMode;
	}
	
	public void setTopLevelSchemeMode(boolean b) {
		if (this.topLevelSchemeMode == b) {
			return;
		}
		this.topLevelSchemeMode = b;
		if (b) {
			SchemeActions.generateTopLevelScheme(this.graph);
		} else {
			if (this.schemeResource.getCellContainerType() == SchemeResource.SCHEME) {
				GraphActions.clearGraph(this.graph);
				try {
					SchemeActions.openSchemeImageResource(this.graph, this.schemeResource.getScheme().getSchemeCell(), false);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent) ae;
			List<Object> cells = new ArrayList<Object>();
			
			if (ev.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				try {
					for (Identifiable id : ev.getIdentifiables()) {
						if (id.getId().getMajor() == ObjectEntities.SCHEMEPATH_CODE) {
							SchemePath path = StorableObjectPool.getStorableObject(id.getId(), false);
							cells.addAll(this.schemeResource.getPathElements(path));
						} else {
							cells.add(SchemeActions.findObjectById(this.graph, id.getId()));
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} else if (ev.isSelected(ObjectSelectedEvent.SCHEME)) {
				try {
					for (Identifiable id : ev.getIdentifiables()) {
						if (id.getId().getMajor() == ObjectEntities.SCHEME_CODE) {
							Scheme scheme = StorableObjectPool.getStorableObject(id.getId(), false);
							SchemeElement schemeElement = scheme.getParentSchemeElement();
							if (schemeElement != null) {
								cells.add(SchemeActions.findGroupById(this.graph, schemeElement.getId()));
							}
						} else {
							cells.add(SchemeActions.findObjectById(this.graph, id.getId()));
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} else if (ev.isSelected(ObjectSelectedEvent.SCHEME_ELEMENT)
					|| ev.isSelected(ObjectSelectedEvent.SCHEME_PROTOELEMENT)
					|| ev.isSelected(ObjectSelectedEvent.SCHEME_PORT)
					|| ev.isSelected(ObjectSelectedEvent.SCHEME_CABLEPORT)
					|| ev.isSelected(ObjectSelectedEvent.SCHEME_LINK)
					|| ev.isSelected(ObjectSelectedEvent.SCHEME_CABLELINK)) {
				for (Identifiable id : ev.getIdentifiables()) {
					cells.add(SchemeActions.findObjectById(this.graph, id.getId()));	
				}
			}
			this.graph.setSelectionCells(cells.toArray());
			if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
				this.graph.insureSelectionVisible();
			}
			
//			else if (ev.isSelected(ObjectSelectedEvent.MULTIPLE)) {
//				final Set< ? extends Object> selectedObjects = ev.getSelectedObjects();
//				final Set<Identifier> selectedIds = new HashSet<Identifier>();
//				for (Object obj : selectedObjects) {
//					if (obj instanceof Identifiable) {
//						selectedIds.add(((Identifiable)obj).getId());
//					}
//				}
//				if (!selectedIds.isEmpty()) {
//					this.graph.setSelectionCells(SchemeActions.findObjectsByIds(this.graph, selectedIds));
//				}
//			}
		} else if (ae.getPropertyName().equals(MarkerEvent.MARKER_EVENT_TYPE)) {
			MarkerEvent ev = (MarkerEvent)ae;
			if (ev.getMarkerEventType() == MarkerEvent.ALARMMARKER_CREATED_EVENT) {
				
			}
		}
		super.propertyChange(ae);
	}
}
