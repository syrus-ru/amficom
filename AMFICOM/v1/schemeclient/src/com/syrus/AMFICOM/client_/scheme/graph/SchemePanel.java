/*-
 * $Id: SchemePanel.java,v 1.2 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Point;
import java.util.*;

import com.jgraph.graph.DefaultGraphModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/05/26 07:40:51 $
 * @module schemeclient_v1
 */

public class SchemePanel extends ElementsPanel {
	public SchemePanel(ApplicationContext aContext) {
		super(aContext);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		graph.setBorderVisible(true);
		// graph.setPortsVisible(true);
		graph.setBendable(false);
		graph.setActualSize(Constants.A4);
		// graph.getSelectionModel().setChildrenSelectable(false);
	}

	public String getReportTitle() {
		return LangModelSchematics.getString("schemeMainTitle");
	}

	public static Map copyFromArchivedState_virtual(List serializable) {
		DefaultGraphModel model = new DefaultGraphModel();
		SchemeGraph virtual_graph = new SchemeGraph(model, new ApplicationContext());
		Map clones = virtual_graph.copyFromArchivedState(serializable, new Point(0, 0));
		assignClonedIds(clones.values().toArray());
		serializable = (List)virtual_graph.getArchiveableState(virtual_graph.getRoots());
		return clones;
	}
}
