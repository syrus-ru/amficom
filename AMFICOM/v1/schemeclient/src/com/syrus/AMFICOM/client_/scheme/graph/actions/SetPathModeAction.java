/*
 * $Id: SetPathModeAction.java,v 1.2 2005/07/11 12:31:38 stas Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class SetPathModeAction extends AbstractAction {
	UgoTabbedPane pane;

	SchemeGraph graph;

	public SetPathModeAction(UgoTabbedPane pane) {
		super(Constants.PATH_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		pane.getGraph().setMode(Constants.PATH_MODE);
	}
}