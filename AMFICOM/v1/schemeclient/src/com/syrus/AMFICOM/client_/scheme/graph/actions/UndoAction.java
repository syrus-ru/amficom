/*
 * $Id: UndoAction.java,v 1.1 2005/04/05 14:07:53 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.jgraph.graph.GraphUndoManager;
import com.syrus.AMFICOM.client_.scheme.graph.*;


/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class UndoAction extends AbstractAction {
	UgoTabbedPane pane;
	
	public UndoAction(UgoTabbedPane pane) {
		super(Constants.undoKey);
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
			((ShemeMarqueeHandler)graph.getMarqueeHandler()).updateHistoryButtons(undoManager);
		}
	}
}