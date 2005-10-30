/*
 * $Id: ElementsPanel.java,v 1.18 2005/10/30 15:20:56 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2005/10/30 15:20:56 $
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
					assert Log.errorMessage(e);
				}
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent) ae;
			if (ev.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				SchemePath path = (SchemePath)ev.getSelectedObject();
				this.graph.setSelectionCells(this.schemeResource.getPathElements(path));
//				SchemeResource.setSchemePath(path, false);
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) {
					this.graph.insureSelectionVisible();
				}
			}
			// TODO разобраться с созданием пути 
//			else {
//				if (graph.path_creation_mode != Constants.CREATING_PATH_MODE)
//					schemeResource.setSchemePath(null);
//			}

//			if (ev.SCHEME_ALL_DESELECTED) {
//				getGraph().removeSelectionCells();
//			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME)) {
				Scheme scheme = (Scheme)ev.getSelectedObject();
				try {
					SchemeElement schemeElement = scheme.getParentSchemeElement();
					if (schemeElement != null) {
						this.graph.setSelectionCell(SchemeActions.findGroupById(this.graph, schemeElement.getId()));
						if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
							this.graph.insureSelectionVisible();
						}
					}
				} catch (ApplicationException e) {
					assert Log.errorMessage(e);
				}
			} else if (ev.isSelected(ObjectSelectedEvent.SCHEME_ELEMENT)) {
				SchemeElement element = (SchemeElement)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findGroupById(this.graph, element.getId()));
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_PROTOELEMENT)) {
				SchemeProtoElement proto = (SchemeProtoElement)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findGroupById(this.graph, proto.getId()));
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_PORT)) {
				// TODO multiple selection
				SchemePort port;
				if (ev.getSelectedObject() instanceof Set) {
					port = (SchemePort)((Set)ev.getSelectedObject()).iterator().next();
				} else {
					port = (SchemePort)ev.getSelectedObject();
				}
				this.graph.setSelectionCell(SchemeActions.findPortCellById(this.graph, port.getId()));
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_CABLEPORT)) {
				SchemeCablePort port = (SchemeCablePort)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findCablePortCellById(this.graph, port.getId()));
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} else if (ev.isSelected(ObjectSelectedEvent.SCHEME_LINK)) {
				SchemeLink link = (SchemeLink)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findSchemeLinkById(this.graph, link.getId()));
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_CABLELINK)) {
				SchemeCableLink link = (SchemeCableLink)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findSchemeCableLinkById(this.graph, link.getId()));
				if (ev.isSelected(ObjectSelectedEvent.INSURE_VISIBLE)) { 
					this.graph.insureSelectionVisible();
				}
			}
		} else if (ae.getPropertyName().equals(MarkerEvent.MARKER_EVENT_TYPE)) {
			MarkerEvent ev = (MarkerEvent)ae;
			if (ev.getMarkerEventType() == MarkerEvent.ALARMMARKER_CREATED_EVENT) {
				
			}
		}
		// TODO разобраться с созданием пути 
/*		
		else if (ae.getActionCommand().equals(CreatePathEvent.typ)) {
			CreatePathEvent cpe = (CreatePathEvent) ae;
			if (cpe.DELETE_PATH) {
			}
			if (cpe.EDIT_PATH) {
				if (graph.getCurrentPath() != null)
					editing_path = (SchemePath) graph.getCurrentPath().clone();
			}
		}
		if (ae.getActionCommand().equals(SchemeElementsEvent.type)) {
			SchemeElementsEvent see = (SchemeElementsEvent) ae;
			if (see.UGO_CREATE)
				return;
			if (see.SCHEME_UGO_CREATE)
				return;
		}*/
		super.propertyChange(ae);
	}
}
