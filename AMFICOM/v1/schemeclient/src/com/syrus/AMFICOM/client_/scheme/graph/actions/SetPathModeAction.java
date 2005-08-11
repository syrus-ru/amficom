/*
 * $Id: SetPathModeAction.java,v 1.5 2005/08/11 07:27:27 stas Exp $
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
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/08/11 07:27:27 $
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
		SchemeGraph graph = this.pane.getGraph();
		if (graph != null)
			graph.setMode(Constants.PATH_MODE);
	}
}