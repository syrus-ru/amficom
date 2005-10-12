/*
 * $Id: SetPathModeAction.java,v 1.7 2005/10/12 10:08:41 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/10/12 10:08:41 $
 * @module schemeclient
 */

public class SetPathModeAction extends AbstractAction {
	private static final long serialVersionUID = 2301332672752133162L;
	SchemeTabbedPane pane;

	public SetPathModeAction(SchemeTabbedPane pane) {
		super(Constants.PATH_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		for(ElementsPanel panel : this.pane.getAllPanels()) {
			SchemeGraph graph = panel.getGraph();
			graph.setMode(Constants.RACK_MODE);
		}
	}
}