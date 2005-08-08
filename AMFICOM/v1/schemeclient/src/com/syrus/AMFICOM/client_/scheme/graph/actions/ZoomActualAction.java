/*
 * $Id: ZoomActualAction.java,v 1.5 2005/08/08 11:58:07 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:07 $
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
		graph.setScale(1);
		graph.setPreferredSize(graph.getActualSize());
		graph.setGridVisible(graph.isGridVisibleAtActualSize());
		//graph.setLocation(0, 0);
	}
}