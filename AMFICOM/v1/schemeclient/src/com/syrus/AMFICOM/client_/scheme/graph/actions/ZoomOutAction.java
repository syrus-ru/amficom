/*
 * $Id: ZoomOutAction.java,v 1.8 2006/02/15 12:18:11 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
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
 * @version $Revision: 1.8 $, $Date: 2006/02/15 12:18:11 $
 * @module schemeclient
 */

public class ZoomOutAction extends AbstractAction {
	private static final long serialVersionUID = 6788842635936042798L;
	UgoTabbedPane pane;

	public ZoomOutAction(UgoTabbedPane pane) {
		super(Constants.ZOOM_OUT);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = this.pane.getGraph();
		GraphActions.zoomToCenter(graph, 0.8);
	}
}