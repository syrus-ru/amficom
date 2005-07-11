/*
 * $Id: ZoomInAction.java,v 1.3 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.Dimension;
import java.awt.Point;
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

public class ZoomInAction extends AbstractAction {
	UgoTabbedPane pane;

	public ZoomInAction(UgoTabbedPane pane) {
		super(Constants.ZOOM_IN);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = pane.getGraph();
		graph.setScale(graph.getScale() * 1.25);
		Dimension size = graph.getPreferredSize();
		graph.setPreferredSize(new Dimension((int) (size.width * 1.25),
				(int) (size.height * 1.25)));
		Point loc = graph.getLocation();
		graph.setLocation(loc.x - (int) (graph.getWidth() * 0.125), loc.y
				- (int) (graph.getHeight() * 0.125));
		if (graph.getScale() >= .5)
			graph.setGridVisible(graph.isGridVisibleAtActualSize());
	}
}