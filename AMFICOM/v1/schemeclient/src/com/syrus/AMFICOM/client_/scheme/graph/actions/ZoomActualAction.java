/*
 * $Id: ZoomActualAction.java,v 1.1 2005/04/05 14:07:53 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.client_.scheme.graph.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class ZoomActualAction extends AbstractAction {
	UgoTabbedPane pane;

	public ZoomActualAction(UgoTabbedPane pane) {
		super(Constants.zoomActualKey);
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