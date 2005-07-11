/*
 * $Id: ZoomOutAction.java,v 1.3 2005/07/11 12:31:38 stas Exp $
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

public class ZoomOutAction extends AbstractAction {
	UgoTabbedPane pane;

	public ZoomOutAction(UgoTabbedPane pane) {
		super(Constants.ZOOM_OUT);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = pane.getGraph();
		graph.setScale(graph.getScale() * .8);
		Dimension size = graph.getPreferredSize();
		graph.setPreferredSize(new Dimension((int) (size.width * .8),
				(int) (size.height * .8)));
		Point loc = graph.getLocation();
		graph.setLocation(Math.min(0, loc.x + (int) (graph.getWidth() * 0.1)), Math
				.min(0, loc.y + (int) (graph.getHeight() * 0.1)));
		if (graph.getScale() < .5)
			graph.setGridVisible(false);
	}
}