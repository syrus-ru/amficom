/*
 * $Id: ZoomActualAction.java,v 1.6 2005/10/10 11:07:38 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

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

public class ZoomActualAction extends AbstractAction {
	private static final long serialVersionUID = -7922242665768967985L;
	UgoTabbedPane pane;

	public ZoomActualAction(UgoTabbedPane pane) {
		super(Constants.ZOOM_ACTUAL);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = this.pane.getGraph();
		Point oldLocation = graph.getLocation();
		double oldScale = graph.getScale();
		Rectangle visibleRect = graph.getVisibleRect();
		graph.setScale(1);
		graph.setPreferredSize(graph.getActualSize());
		graph.setGridVisible(graph.isGridVisibleAtActualSize());
		
		graph.setLocation((int)(oldLocation.x / oldScale - visibleRect.width / 2 * (1 / oldScale - 1)),
				(int)(oldLocation.y / oldScale - visibleRect.height / 2 * (1 / oldScale - 1)));
		
//		if (oldScale < 1) {
//			graph.setLocation((int)((oldLocation.x - visibleRect.width / 2) / oldScale), (int)((oldLocation.y - visibleRect.height / 2) / oldScale));
//		} else {
//			graph.setLocation((int)((oldLocation.x + visibleRect.width / 2) / oldScale), (int)((oldLocation.y + visibleRect.height / 2) / oldScale));
//		}
	}
}