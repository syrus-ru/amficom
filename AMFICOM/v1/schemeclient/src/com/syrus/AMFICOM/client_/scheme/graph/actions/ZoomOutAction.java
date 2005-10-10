/*
 * $Id: ZoomOutAction.java,v 1.6 2005/10/10 11:07:38 stas Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/10/10 11:07:38 $
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
		
		Point oldLocation = graph.getLocation();
		Rectangle visibleRect = graph.getVisibleRect();
		
		graph.setScale(graph.getScale() * 0.8);
		Dimension size = graph.getPreferredSize();
		graph.setPreferredSize(new Dimension((int) (size.width * .8), (int) (size.height * .8)));
		
		graph.setLocation((int)(oldLocation.x * 0.8 + 0.1 * visibleRect.width), (int)(oldLocation.y * 0.8 + 0.1 * visibleRect.height));
		
		if (graph.getScale() < .5)
			graph.setGridVisible(false);
	}
}