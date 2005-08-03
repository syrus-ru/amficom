/*
 * $Id: ElementsTabbedPane.java,v 1.8 2005/08/03 09:29:41 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.actions.DeleteAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.RedoAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.UndoAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomActualAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomInAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.ZoomOutAction;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2005/08/03 09:29:41 $
 * @module schemeclient_v1
 */

public class ElementsTabbedPane extends UgoTabbedPane implements PropertyChangeListener {
	static JOptionPane optionPane;
	static JDialog dialog;
	int result;
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
		keyListener = new SchemeKeyListener();
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
		graph.addKeyListener(keyListener);
		JScrollPane graphView = new JScrollPane(graph);
		return graphView;
	}
	
	protected JComponent createToolBar() {
		this.toolBar = new ElementsToolBar(this, aContext);
		return toolBar;
	}
	
	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent) ae;
			if (see.isType(SchemeEvent.INSERT_PROTOELEMENT)) {
				SchemeProtoElement proto = (SchemeProtoElement) see.getObject();
				
				try {
					SchemeProtoElement newProto = proto.clone();
					Map<Identifier, Identifier>clonedIds = newProto.getClonedIdMap();
					Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = openSchemeCellContainer(proto, true);
					SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
					UgoPanel p = getCurrentPanel();
					SchemeGraph graph = p.getGraph();
					newProto.getSchemeCell().setData((List<Object>)graph.getArchiveableState());
				} catch (CloneNotSupportedException e) {
					Log.errorException(e);
				}
			}
		}
	}
	
	public Map<DefaultGraphCell, DefaultGraphCell> openSchemeCellContainer(SchemeCellContainer schemeCellContainer, boolean doClone) {
		Map<DefaultGraphCell, DefaultGraphCell> clones = Collections.emptyMap();
		UgoPanel p = getCurrentPanel();
		SchemeGraph graph = p.getGraph();
//		p.getSchemeResource().setSchemeProtoElement(proto);
//		GraphActions.clearGraph(p.getGraph());
		if (schemeCellContainer.getSchemeCell() != null) {
			clones = p.insertCell(schemeCellContainer.getSchemeCell().getData(), new Point(0, 0), doClone);
			fixImages(graph);
		}
		setGraphChanged(false);
		return clones;
	}
	
	boolean showConfirmDialog(String text) {
		if (optionPane == null) {
			JButton okButton = new JButton(LangModelGeneral.getString("Button.OK")); //$NON-NLS-1$
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					result = JOptionPane.OK_OPTION;
					dialog.dispose();
				}
			});
			JButton cancelButton = new JButton(LangModelGeneral.getString("Button.Cancel")); //$NON-NLS-1$
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					result = JOptionPane.CANCEL_OPTION;
					dialog.dispose();
				}
			});
			optionPane = new JOptionPane(text, JOptionPane.QUESTION_MESSAGE,
					JOptionPane.OK_CANCEL_OPTION, null, new Object[] { okButton,
							cancelButton}, null);
			
			dialog = optionPane.createDialog(Environment.getActiveWindow(), 
					LangModelScheme.getString("Message.confirmation.title")); //$NON-NLS-1$
			dialog.setModal(true);
		}
		dialog.setVisible(true);
		return result == JOptionPane.OK_OPTION;
	}
	
	/**
	 * @return selected ElementsPanel
	 */
	public ElementsPanel getCurrentPanel() {
		return (ElementsPanel)panel;
	}
	
	public boolean removePanel(UgoPanel p) {
		if (p instanceof ElementsPanel) {
			if (p.getGraph().isGraphChanged()) {
				String text = LangModelScheme.getString("Message.confirmation.object_changed");  //$NON-NLS-1$
				return showConfirmDialog(text);
			}
			return true;
		}
		return super.removePanel(p);
	}

	public boolean removeAllPanels() {
		return removePanel(panel);
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
				Log.debugMessage("aligning cells to grid", Level.FINEST);
				GraphActions.alignToGrid(graph, graph.getSelectionCells());
				SchemeActions.splitCableLink(graph, (DefaultCableLink)graph.getSelectionCells()[0]);
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
