/*
 * $Id: ElementsPanel.java,v 1.20 2006/01/25 12:58:22 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.beans.PropertyChangeEvent;
import java.util.Set;

import com.jgraph.graph.GraphModel;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.event.MarkerEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.20 $, $Date: 2006/01/25 12:58:22 $
 * @module schemeclient
 */

public class ElementsPanel extends UgoPanel {
	SchemeResource schemeResource;
	private boolean topLevelSchemeMode = false;
	
	protected ElementsPanel(ApplicationContext aContext) {
		super(aContext);

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private void jbInit() throws Exception {
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
			if (ev.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				try {
					SchemePath path = StorableObjectPool.getStorableObject(((Identifiable)ev.getSelectedObject()).getId(), false);
					this.graph.setSelectionCells(this.schemeResource.getPathElements(path));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) {
					this.graph.insureSelectionVisible();
				}
			} else if (ev.isSelected(ObjectSelectedEvent.SCHEME)) {
				try {
					Scheme scheme = StorableObjectPool.getStorableObject(((Identifiable)ev.getSelectedObject()).getId(), false);
					SchemeElement schemeElement = scheme.getParentSchemeElement();
					if (schemeElement != null) {
						this.graph.setSelectionCell(SchemeActions.findGroupById(this.graph, schemeElement.getId()));
						if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
							this.graph.insureSelectionVisible();
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} else if (ev.isSelected(ObjectSelectedEvent.SCHEME_ELEMENT)) {
				try {
					SchemeElement element = StorableObjectPool.getStorableObject(((Identifiable)ev.getSelectedObject()).getId(), false);
					this.graph.setSelectionCell(SchemeActions.findGroupById(this.graph, element.getId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_PROTOELEMENT)) {
				try {
					SchemeProtoElement proto = StorableObjectPool.getStorableObject(((Identifiable)ev.getSelectedObject()).getId(), false);
					this.graph.setSelectionCell(SchemeActions.findGroupById(this.graph, proto.getId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_PORT)) {
				// TODO multiple selection
				Identifier portId;
				if (ev.getSelectedObject() instanceof Set) {
					portId = ((Identifiable)((Set)ev.getSelectedObject()).iterator().next()).getId();
				} else {
					portId = ((Identifiable)ev.getSelectedObject()).getId();
				}
				try {
					SchemePort port = StorableObjectPool.getStorableObject(portId, false);
					this.graph.setSelectionCell(SchemeActions.findPortCellById(this.graph, port.getId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_CABLEPORT)) {
				try {
					SchemeCablePort port = StorableObjectPool.getStorableObject(((Identifiable)ev.getSelectedObject()).getId(), false);
					this.graph.setSelectionCell(SchemeActions.findCablePortCellById(this.graph, port.getId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} else if (ev.isSelected(ObjectSelectedEvent.SCHEME_LINK)) {
				try {
					SchemeLink link = StorableObjectPool.getStorableObject(((Identifiable)ev.getSelectedObject()).getId(), false);
					this.graph.setSelectionCell(SchemeActions.findSchemeLinkById(this.graph, link.getId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_CABLELINK)) {
				try {
					SchemeCableLink link = StorableObjectPool.getStorableObject(((Identifiable)ev.getSelectedObject()).getId(), false);
					this.graph.setSelectionCell(SchemeActions.findSchemeCableLinkById(this.graph, link.getId()));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			}
		} else if (ae.getPropertyName().equals(MarkerEvent.MARKER_EVENT_TYPE)) {
			MarkerEvent ev = (MarkerEvent)ae;
			if (ev.getMarkerEventType() == MarkerEvent.ALARMMARKER_CREATED_EVENT) {
				
			}
		}
		super.propertyChange(ae);
	}
}
