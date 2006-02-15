/*
 * $Id: ZoomInAction.java,v 1.8 2006/02/15 12:18:11 stas Exp $
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

public class ZoomInAction extends AbstractAction {
	private static final long serialVersionUID = -6803338845175386815L;
	UgoTabbedPane pane;

	public ZoomInAction(UgoTabbedPane pane) {
		super(Constants.ZOOM_IN);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = this.pane.getGraph();
		GraphActions.zoomToCenter(graph, 1.25);
	}
}