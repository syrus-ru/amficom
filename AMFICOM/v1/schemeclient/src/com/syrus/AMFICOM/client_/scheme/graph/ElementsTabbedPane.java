/*
 * $Id: ElementsTabbedPane.java,v 1.24 2005/12/27 10:32:12 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.actions.DeleteAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.RedoAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.UndoAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomActualAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomInAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomOutAction;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.24 $, $Date: 2005/12/27 10:32:12 $
 * @module schemeclient
 */

public class ElementsTabbedPane extends UgoTabbedPane implements PropertyChangeListener {
	private static final long serialVersionUID = -1781981917301697387L;

	protected KeyListener keyListener;
		
	public ElementsTabbedPane() {
		super();
		jbInit();
	}
	
	public ElementsTabbedPane(ApplicationContext aContext) {
		super(aContext);
		jbInit();
	}
	
	private void jbInit() {
		this.keyListener = new SchemeKeyListener();
	}
	
	@Override
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
	
	@Override
	protected JComponent createPanel() {
		this.panel = new ElementsPanel(this.aContext);
		final SchemeGraph graph = this.panel.getGraph();
		SchemeGraph.setMode(Constants.PROTO_MODE);
		graph.setMarqueeHandler(this.marqueeHandler);
		graph.addMouseWheelListener(this.marqueeHandler);
		graph.addKeyListener(this.keyListener);
		return new JScrollPane(graph);
	}
	
	@Override
	protected JComponent createToolBar() {
		this.toolBar = new ElementsToolBar(this, this.aContext);
		return this.toolBar;
	}
	
	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent) ae;
			if (see.isType(SchemeEvent.INSERT_PROTOELEMENT) || 
					see.isType(SchemeEvent.OPEN_PROTOELEMENT_ASCOPY)) {
				
				try {
					SchemeProtoElement proto = (SchemeProtoElement) see.getStorableObject();
					SchemeProtoElement newProto = proto.clone();
					Map<Identifier, Identifier>clonedIds = newProto.getClonedIdMap();
					SchemeImageResource imageResource = see.isType(SchemeEvent.INSERT_PROTOELEMENT) ? 
							newProto.getUgoCell() : newProto.getSchemeCell();
					if (imageResource == null)
						imageResource = newProto.getSchemeCell();
					UgoPanel p = getCurrentPanel();
					SchemeGraph graph = p.getGraph();
					Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(graph, imageResource, true, see.getInsertionPoint(), false);
					SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
					
					ApplicationContext internalContext =  new ApplicationContext();
					internalContext.setDispatcher(new Dispatcher());
					final SchemeGraph invisibleGraph = new UgoTabbedPane(internalContext).getGraph();
					invisibleGraph.setMakeNotifications(false);
					clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, imageResource, true);
					SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
					newProto.getSchemeCell().setData((List<Object>)invisibleGraph.getArchiveableState());
				} catch (CloneNotSupportedException e) {
					Log.errorMessage(e);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} else if (see.isType(SchemeEvent.OPEN_PROTOELEMENT)) {
				try {
					SchemeProtoElement proto = (SchemeProtoElement) see.getStorableObject();
					ElementsPanel p = getCurrentPanel();
					SchemeGraph graph = p.getGraph();
					p.getSchemeResource().setSchemeProtoElement(proto);
					GraphActions.clearGraph(graph);
					graph.selectionNotify();
					
					SchemeImageResource imageResource = proto.getSchemeCell();
					SchemeActions.openSchemeImageResource(graph, imageResource, true, see.getInsertionPoint(), false);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				
				
			}
		}
	}
	
	/**
	 * @return selected ElementsPanel
	 */
	@Override
	public ElementsPanel getCurrentPanel() {
		return (ElementsPanel)this.panel;
	}
	
	@Override
	public boolean hasUnsavedChanges(UgoPanel p) {
		try {
			if (p instanceof ElementsPanel) {
				if (p.getGraph().isGraphChanged()) {
					return true;
				}
				SchemeResource res = ((ElementsPanel)p).getSchemeResource();
				boolean b = false;
				if (res.getCellContainerType() == SchemeResource.SCHEME) {
					Scheme scheme = res.getScheme(); 
					if (scheme != null && scheme.isChanged()) {
						b = true;
					} else {
						Log.debugMessage("CellContainerType is scheme, while scheme is null", Level.FINE);
					}
				} else if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) {
					SchemeElement schemeElement = res.getSchemeElement(); 
					if (schemeElement != null && schemeElement.isChanged()) {
						b = true;
					} else {
						Log.debugMessage("CellContainerType is schemeElement, while schemeElement is null", Level.FINE);
					}
				}
				return b;
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		return super.hasUnsavedChanges(p);
	}

	public boolean confirmUnsavedChanges(UgoPanel p) {
		if (hasUnsavedChanges(p)) {
			String text = LangModelScheme.getString("Message.confirmation.object_changed");  //$NON-NLS-1$
			return ClientUtils.showConfirmDialog(text);
		}
		return true;
	}
	
	public boolean confirmUnsavedChanges() {
		return confirmUnsavedChanges(this.panel);
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
		@Override
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
				Log.debugMessage("aligning cells to grid", Level.FINEST);
				GraphActions.alignToGrid(graph, graph.getSelectionCells());
			}
			// CTRL + ...
			if (e.getModifiers() == InputEvent.CTRL_MASK) {
				/*if (e.getKeyCode() == KeyEvent.VK_C) {
					Clipboard system = Toolkit.getDefaultToolkit().getSystemClipboard();
					getTransferHandler().exportToClipboard(graph, system, TransferHandler.COPY);
				} 
				else if (e.getKeyCode() == KeyEvent.VK_V) {
					Clipboard system = Toolkit.getDefaultToolkit().getSystemClipboard();
					getTransferHandler().importData(graph, system.getContents(graph));
				}*/
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
