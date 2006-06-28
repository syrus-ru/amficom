/*-
 * $Id: SchemePanel.java,v 1.11 2006/02/15 12:18:10 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * @author $Author: stas $
 * @version $Revision: 1.11 $, $Date: 2006/02/15 12:18:10 $
 * @module schemeclient
 */

public class SchemePanel extends ElementsPanel {
		
	protected SchemePanel(ApplicationContext aContext) {
		super(aContext);
		this.graph.setBorderVisible(true);
		// graph.setPortsVisible(true);
		this.graph.setBendable(false);
		this.graph.setActualSize(Constants.A4);
		// graph.getSelectionModel().setChildrenSelectable(false);
	}

	@Override
	public String getReportTitle() {
		return LangModelSchematics.getString("schemeMainTitle");
	}
/*
	static Map copyFromArchivedState_virtual(List serializable) {
		DefaultGraphModel model = new DefaultGraphModel();
		SchemeGraph virtual_graph = new SchemeGraph(model, new ApplicationContext());
		Map clones = GraphActions.insertCell(virtual_graph, serializable, null, true);
		serializable = (List)virtual_graph.getArchiveableState(virtual_graph.getRoots());
		return clones;
	}*/
}
