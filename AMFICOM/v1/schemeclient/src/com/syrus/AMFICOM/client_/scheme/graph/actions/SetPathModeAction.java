/*
 * $Id: SetPathModeAction.java,v 1.3 2005/08/05 12:39:59 stas Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/08/05 12:39:59 $
 * @module schemeclient_v1
 */

public class SetPathModeAction extends AbstractAction {
	private static final long serialVersionUID = 2301332672752133162L;

	UgoTabbedPane pane;

	SchemeGraph graph;

	public SetPathModeAction(UgoTabbedPane pane) {
		super(Constants.PATH_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		this.pane.getGraph().setMode(Constants.PATH_MODE);
	}
}