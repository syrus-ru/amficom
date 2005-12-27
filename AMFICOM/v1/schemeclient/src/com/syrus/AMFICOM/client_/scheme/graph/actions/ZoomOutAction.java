/*
 * $Id: ZoomOutAction.java,v 1.7 2005/12/27 10:23:15 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/12/27 10:23:15 $
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