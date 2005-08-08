/*
 * $Id: RedoAction.java,v 1.6 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.jgraph.graph.GraphUndoManager;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeMarqueeHandler;
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class RedoAction extends AbstractAction {
	private static final long serialVersionUID = -1151561807147580569L;

	UgoTabbedPane pane;

	public RedoAction(UgoTabbedPane pane) {
		super(Constants.REDO);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		UgoPanel panel = this.pane.getCurrentPanel();
		SchemeGraph graph = panel.getGraph();
		GraphUndoManager undoManager = panel.getGraphUndoManager();
		try {
			undoManager.redo();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		} 
		finally {
			((SchemeMarqueeHandler)graph.getMarqueeHandler()).updateHistoryButtons(undoManager);
		}
	}
}