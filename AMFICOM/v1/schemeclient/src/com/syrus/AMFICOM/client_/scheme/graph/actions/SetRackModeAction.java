/*-
 * $Id: SetRackModeAction.java,v 1.1 2005/10/17 15:08:38 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;

public class SetRackModeAction extends AbstractAction {
	private static final long serialVersionUID = 2301332672752133162L;
	SchemeTabbedPane pane;

	public SetRackModeAction(SchemeTabbedPane pane) {
		super(Constants.RACK_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
//		for(ElementsPanel panel : this.pane.getAllPanels()) {
//			SchemeGraph graph = panel.getGraph();
			SchemeGraph.setMode(Constants.RACK_MODE);
//		}
	}
}