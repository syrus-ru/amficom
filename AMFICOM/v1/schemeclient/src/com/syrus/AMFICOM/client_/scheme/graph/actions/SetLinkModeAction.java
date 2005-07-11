/*
 * $Id: SetLinkModeAction.java,v 1.3 2005/07/11 12:31:38 stas Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class SetLinkModeAction extends AbstractAction {
	UgoTabbedPane pane;

	public SetLinkModeAction(UgoTabbedPane pane) {
		super(Constants.LINK_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = pane.getGraph();
		if (graph != null)
			graph.setMode(Constants.LINK_MODE);
	}
}