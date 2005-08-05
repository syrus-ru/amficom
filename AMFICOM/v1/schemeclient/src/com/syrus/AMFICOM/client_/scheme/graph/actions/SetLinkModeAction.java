/*
 * $Id: SetLinkModeAction.java,v 1.4 2005/08/05 12:39:59 stas Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/08/05 12:39:59 $
 * @module schemeclient_v1
 */

public class SetLinkModeAction extends AbstractAction {
	private static final long serialVersionUID = 728678567741292211L;

	UgoTabbedPane pane;

	public SetLinkModeAction(UgoTabbedPane pane) {
		super(Constants.LINK_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = this.pane.getGraph();
		if (graph != null)
			graph.setMode(Constants.LINK_MODE);
	}
}