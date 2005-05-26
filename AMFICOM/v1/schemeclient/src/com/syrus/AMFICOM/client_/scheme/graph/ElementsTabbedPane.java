/*
 * $Id: ElementsTabbedPane.java,v 1.3 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Point;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.JComponent;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.*;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/05/26 07:40:51 $
 * @module schemeclient_v1
 */

public class ElementsTabbedPane extends UgoTabbedPane {
	
	public ElementsTabbedPane(ApplicationContext aContext) {
		super(aContext);
	}
	
	protected JComponent createPanel() {
		panel = new ElementsPanel(aContext);
		SchemeGraph graph = panel.getGraph();
		graph.setMarqueeHandler(marqueeHandler);
		graph.addKeyListener(new SchemeKeyListener());
		JScrollPane graphView = new JScrollPane(graph);
		return graphView;
	}

	public void openScheme(Scheme sch) {
		UgoPanel p = getCurrentPanel();
		p.getSchemeResource().setScheme(sch);
		p.getSchemeResource().setSchemeElement(null);
		GraphActions.clearGraph(p.getGraph());
//		if (sch.getSchemeCell() != null)
//			p.insertCell(sch.getSchemeCell().getData(), new Point(0, 0), true);
	}

	public void openSchemeElement(SchemeElement se) {
		UgoPanel p = getCurrentPanel();
		p.getSchemeResource().setScheme(null);
		p.getSchemeResource().setSchemeElement(se);
		GraphActions.clearGraph(p.getGraph());
		if (se.getSchemeCell() != null)
			p.insertCell(se.getSchemeCell().getData(), new Point(0, 0), true);
	}

	/*
	public void removeScheme(Scheme sch) {
		SchemeResource res = getCurrentPanel().getSchemeResource();
		if (res.getSchemeElement() != null) {
			if (SchemeUtils.isSchemeContainsElement(sch, res.getSchemeElement())) {
				GraphActions.clearGraph(getCurrentPanel().getGraph());
			}
		}
	}*/
	
	class SchemeKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			ElementsTabbedPane pane = ElementsTabbedPane.this;
			UgoPanel p = pane.getCurrentPanel();
			SchemeGraph graph = p.getGraph();
			// Execute Remove Action on Delete Key Press
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				if (!graph.isSelectionEmpty())
					new DeleteAction(pane, aContext).actionPerformed(new ActionEvent(this, 0, ""));
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				GraphActions.alignToGrid(graph, graph.getSelectionCells());
			}
			// CTRL + ...
			if (e.getModifiers() == InputEvent.CTRL_MASK) {
				if (e.getKeyCode() == KeyEvent.VK_Z) {
					if (p.undoManager.canUndo(graph.getGraphLayoutCache()))
						new UndoAction(pane).actionPerformed(new ActionEvent(this, 0, ""));
				}
				if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
					new ZoomOutAction(pane).actionPerformed(new ActionEvent(this, 0, ""));
				}
				if (e.getKeyCode() == KeyEvent.VK_ADD) {
					new ZoomInAction(pane).actionPerformed(new ActionEvent(this, 0, ""));
				}
				if (e.getKeyCode() == KeyEvent.VK_MULTIPLY) {
					new ZoomActualAction(pane).actionPerformed(new ActionEvent(this, 0, ""));
				}
			}
			// CTRL + SHIFT + ...
			if (e.getModifiers() == InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK) {
				if (e.getKeyCode() == KeyEvent.VK_Z) {
					if (p.undoManager.canRedo(graph.getGraphLayoutCache()))
						new RedoAction(pane).actionPerformed(new ActionEvent(this, 0, ""));
				}
			}
		}
	}
}
