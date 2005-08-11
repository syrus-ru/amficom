/*
 * $Id: SetLinkModeAction.java,v 1.6 2005/08/11 07:27:27 stas Exp $
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
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/08/11 07:27:27 $
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

		SchemeGraph graph = this.pane.getGraph();
		if (graph != null)
			graph.setMode(Constants.LINK_MODE);
	}
}