/*
 * $Id: UndoAction.java,v 1.4 2005/07/11 12:31:38 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class UndoAction extends AbstractAction {
	UgoTabbedPane pane;
	
	public UndoAction(UgoTabbedPane pane) {
		super(Constants.UNDO);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		UgoPanel panel = pane.getCurrentPanel();
		SchemeGraph graph = panel.getGraph();
		GraphUndoManager undoManager = panel.getGraphUndoManager();
		try {
			undoManager.undo();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		} 
		finally {
			((SchemeMarqueeHandler)graph.getMarqueeHandler()).updateHistoryButtons(undoManager);
		}
	}
}