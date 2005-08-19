/*
 * $Id: SetLinkModeAction.java,v 1.7 2005/08/19 15:41:34 stas Exp $
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
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/08/19 15:41:34 $
 * @module schemeclient
 */

public class SetLinkModeAction extends AbstractAction {
	private static final long serialVersionUID = 728678567741292211L;

	SchemeTabbedPane pane;

	public SetLinkModeAction(SchemeTabbedPane pane) {
		super(Constants.LINK_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		if (this.pane.getCurrentPanel() != null) { 
			SchemeResource res = this.pane.getCurrentPanel().getSchemeResource();
			res.setSchemePath(null);
			res.setCashedPathMemberIds(null);
			res.setCashedPathStart(null);
			res.setCashedPathEnd(null);
		}

		for(ElementsPanel panel : this.pane.getAllPanels()) {
			SchemeGraph graph = panel.getGraph();
			graph.setMode(Constants.LINK_MODE);
		}
	}
}