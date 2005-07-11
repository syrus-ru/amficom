/*
 * $Id: ElementsTabbedPane.java,v 1.5 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.actions.DeleteAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.RedoAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.UndoAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomActualAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomInAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomOutAction;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class ElementsTabbedPane extends UgoTabbedPane implements PropertyChangeListener {
	
	public ElementsTabbedPane(ApplicationContext aContext) {
		super(aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(SchemeEvent.TYPE, this);
		}
		if (aContext != null) {
			this.aContext = aContext;
			this.aContext.getDispatcher().addPropertyChangeListener(SchemeEvent.TYPE, this);
			for (Iterator it = getAllPanels().iterator(); it.hasNext();)
				((UgoPanel)it.next()).setContext(aContext);
		}
	}
	
	protected JComponent createPanel() {
		panel = new ElementsPanel(aContext);
		SchemeGraph graph = panel.getGraph();
		graph.setMarqueeHandler(marqueeHandler);
		graph.addKeyListener(new SchemeKeyListener());
		JScrollPane graphView = new JScrollPane(graph);
		return graphView;
	}
	
	protected JComponent createToolBar() {
		return new ElementsToolBar(this, aContext);
	}
	
	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent) ae;
			if (see.isType(SchemeEvent.OPEN_PROTOELEMENT)) {
				SchemeProtoElement proto = (SchemeProtoElement) see.getObject();
				openSchemeCellContainer(proto);
			}
		}
	}
	
	public void openSchemeCellContainer(SchemeCellContainer schemeCellContainer) {
		UgoPanel p = getCurrentPanel();
		SchemeGraph graph = p.getGraph();
//		p.getSchemeResource().setSchemeProtoElement(proto);
		GraphActions.clearGraph(p.getGraph());
		if (schemeCellContainer.getSchemeCell() != null) {
			p.insertCell(schemeCellContainer.getSchemeCell().getData(), new Point(0, 0), true);
			fixImages(graph);
		}
		graph.setGraphChanged(false);
	}
	
	/**
	 * @return selected ElementsPanel
	 */
	public ElementsPanel getCurrentPanel() {
		return (ElementsPanel)panel;
	}
	
	/*
	public void removeScheme(Scheme sch) {
		for (Iterator it = getAllPanels().iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next();
			SchemeResource res = p.getSchemeResource();
			if (sch.equals(res.getScheme())) {
				res.setScheme(null);
				SchemeGraph graph = p.getGraph();
				GraphActions.clearGraph(graph);
				graph.setGraphChanged(false);
			}
		}
	}
	
	public void removeSchemeElement(SchemeElement se) {
		for (Iterator it = getAllPanels().iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next();
			SchemeResource res = p.getSchemeResource();
			if (se.equals(res.getSchemeElement())) {
				res.setSchemeElement(null);
				SchemeGraph graph = p.getGraph();
				GraphActions.clearGraph(graph);
				graph.setGraphChanged(false);
			}
		}
	}
	
	public void removeSchemeProtoElement(SchemeProtoElement proto) {
		for (Iterator it = getAllPanels().iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next();
			SchemeResource res = p.getSchemeResource();
			if (proto.equals(res.getSchemeProtoElement())) {
				res.setSchemeProtoElement(null);
				SchemeGraph graph = p.getGraph();
				GraphActions.clearGraph(graph);
				graph.setGraphChanged(false);
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
					new DeleteAction(pane).actionPerformed(new ActionEvent(this, 0, ""));
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
