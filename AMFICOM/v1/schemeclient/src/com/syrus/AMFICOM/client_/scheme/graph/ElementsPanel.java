/*
 * $Id: ElementsPanel.java,v 1.2 2005/04/18 09:55:03 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import com.jgraph.graph.GraphModel;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 09:55:03 $
 * @module schemeclient_v1
 */

public class ElementsPanel extends UgoPanel {
	public ElementsPanel(ApplicationContext aContext) {
		super(aContext);

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().unregister(this, ObjectSelectedEvent.TYPE);
		}
		super.setContext(aContext);
		if (aContext != null) {
			this.aContext.getDispatcher().register(this, ObjectSelectedEvent.TYPE);
		}
	}

	private void jbInit() throws Exception {
		GraphModel model = graph.getModel();
		model.addUndoableEditListener(undoManager);

		graph.setEditable(true);
		
		graph.setGridEnabled(true);
		graph.setGridVisible(true);
		graph.setGridVisibleAtActualSize(true);
		graph.setRequestFocusEnabled(true);
		graph.setBendable(true);
		graph.setEditable(true);
		graph.setEnabled(true);
	}

	public void operationPerformed(OperationEvent ae) {
		if (ae.getActionCommand().equals(ObjectSelectedEvent.TYPE)) {
			ObjectSelectedEvent ev = (ObjectSelectedEvent) ae;
			if (ev.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				SchemePath path = (SchemePath)ev.getSelectedObject();
				graph.setSelectionCells(schemeResource.getPathElements(path));
				schemeResource.setSchemePath(path);
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
				graph.setSelectionCell(SchemeActions.findGroupById(graph, element.getId()));
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_PROTOELEMENT)) {
				SchemeProtoElement proto = (SchemeProtoElement)ev.getSelectedObject();
				graph.setSelectionCell(SchemeActions.findGroupById(graph, proto.getId()));
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_PORT)) {
				SchemePort port = (SchemePort)ev.getSelectedObject();
				graph.setSelectionCell(SchemeActions.findPortCellById(graph, port.getId()));
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_CABLEPORT)) {
				SchemeCablePort port = (SchemeCablePort)ev.getSelectedObject();
				graph.setSelectionCell(SchemeActions.findCablePortCellById(graph, port.getId()));
			}
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_LINK)) {
				SchemeLink link = (SchemeLink)ev.getSelectedObject();
				graph.setSelectionCell(SchemeActions.findSchemeLinkById(graph, link.getId()));
			} 
			else if (ev.isSelected(ObjectSelectedEvent.SCHEME_CABLELINK)) {
				SchemeCableLink link = (SchemeCableLink)ev.getSelectedObject();
				graph.setSelectionCell(SchemeActions.findSchemeCableLinkById(graph, link.getId()));
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
		super.operationPerformed(ae);
	}
}
