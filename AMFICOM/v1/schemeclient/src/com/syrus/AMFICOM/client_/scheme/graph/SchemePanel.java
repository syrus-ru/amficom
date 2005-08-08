/*-
 * $Id: SchemePanel.java,v 1.8 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import com.jgraph.graph.DefaultGraphModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.8 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class SchemePanel extends ElementsPanel {
		
	protected SchemePanel(ApplicationContext aContext) {
		super(aContext);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
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

	static Map copyFromArchivedState_virtual(List serializable) {
		DefaultGraphModel model = new DefaultGraphModel();
		SchemeGraph virtual_graph = new SchemeGraph(model, new ApplicationContext());
		Map clones = virtual_graph.copyFromArchivedState(serializable, new Point(0, 0));
		serializable = (List)virtual_graph.getArchiveableState(virtual_graph.getRoots());
		return clones;
	}
}
