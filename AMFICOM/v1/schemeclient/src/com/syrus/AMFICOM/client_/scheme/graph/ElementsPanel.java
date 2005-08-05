/*
 * $Id: ElementsPanel.java,v 1.7 2005/08/05 12:39:59 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.beans.PropertyChangeEvent;

import com.jgraph.graph.GraphModel;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/08/05 12:39:59 $
 * @module schemeclient_v1
 */

public class ElementsPanel extends UgoPanel {
	SchemeResource schemeResource;
	
	protected ElementsPanel(ApplicationContext aContext) {
		super(aContext);

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.schemeResource = new SchemeResource(this.graph);
	}

	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ObjectSelectedEvent.TYPE, this);
		}
		super.setContext(aContext);
		if (aContext != null) {
			this.aContext.getDispatcher().addPropertyChangeListener(ObjectSelectedEvent.TYPE, this);
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
		this.graph.make_notifications = true;
	}
	
	public SchemeResource getSchemeResource() {
		return this.schemeResource;
	}

	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent) ae;
			if (ev.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				SchemePath path = (SchemePath)ev.getSelectedObject();
				this.graph.setSelectionCells(this.schemeResource.getPathElements(path));
				this.schemeResource.setSchemePath(path);
			}
			// TODO разобраться с созданием пути 
//			else {
//				if (graph.path_creation_mode != Constants.CREATING_PATH_MODE)
//					schemeResource.setSchemePath(null);
//			}

//			if (ev.SCHEME_ALL_DESELECTED) {
//				getGraph().removeSelectionCells();
//			} 
			if (ev.isSelected(ObjectSelectedEvent.SCHEME_ELEMENT)) {
				SchemeElement element = (SchemeElement)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findGroupById(this.graph, element.getId()));
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_PROTOELEMENT)) {
				SchemeProtoElement proto = (SchemeProtoElement)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findGroupById(this.graph, proto.getId()));
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_PORT)) {
				SchemePort port = (SchemePort)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findPortCellById(this.graph, port.getId()));
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_CABLEPORT)) {
				SchemeCablePort port = (SchemeCablePort)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findCablePortCellById(this.graph, port.getId()));
			} else if (ev.isSelected(ObjectSelectedEvent.SCHEME_LINK)) {
				SchemeLink link = (SchemeLink)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findSchemeLinkById(this.graph, link.getId()));
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_CABLELINK)) {
				SchemeCableLink link = (SchemeCableLink)ev.getSelectedObject();
				this.graph.setSelectionCell(SchemeActions.findSchemeCableLinkById(this.graph, link.getId()));
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
