/*
 * $Id: ZoomActualAction.java,v 1.3 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class ZoomActualAction extends AbstractAction {
	UgoTabbedPane pane;

	public ZoomActualAction(UgoTabbedPane pane) {
		super(Constants.ZOOM_ACTUAL);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = pane.getGraph();
		graph.setScale(1);
		graph.setPreferredSize(graph.getActualSize());
		graph.setGridVisible(graph.isGridVisibleAtActualSize());
		//graph.setLocation(0, 0);
	}
}