/*-
 * $Id: SchemeTabbedPane.java,v 1.14 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.14 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class SchemeTabbedPane extends ElementsTabbedPane {
	private static final long serialVersionUID = 2083767734784404078L;
	JTabbedPane tabs;
	Map<JScrollPane, ElementsPanel> graphPanelsMap;

	public SchemeTabbedPane(ApplicationContext aContext) {
		super(aContext);
	}

	@Override
	protected JComponent createPanel() {
		this.tabs = new JTabbedPane(SwingConstants.BOTTOM);
		this.tabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				UgoPanel p = getCurrentPanel();
				if (p != null) {
					SchemeMarqueeHandler handler = getMarqueeHandler();
					if (!handler.s.isSelected())
						handler.s.setSelected(true);
					handler.updateButtonsState(p.getGraph().getSelectionCells());
				}
			}
		});
		
		this.tabs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && SchemeTabbedPane.this.tabs.getTabCount() > 1) {
					JPopupMenu popup = new JPopupMenu();
					JMenuItem close = new JMenuItem(new AbstractAction() {
						private static final long serialVersionUID = 3655699666572424829L;

						public void actionPerformed(ActionEvent ae) {
							removePanel(getCurrentPanel());
						}
					});
					close.setText(LangModelScheme.getString("Button.close") + " '" + SchemeTabbedPane.this.tabs.getTitleAt(SchemeTabbedPane.this.tabs.getSelectedIndex()) + "'");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
					popup.add(close);
					popup.show(SchemeTabbedPane.this.tabs, e.getX(), e.getY());
				}
			}
		});
		setLayout(new BorderLayout());
		add(this.tabs, BorderLayout.CENTER);
//		addPanel(new SchemePanel(aContext));
		return this.tabs;
	}
	
	@Override
	protected JComponent createToolBar() {
		this.toolBar = new SchemeToolBar(this, this.aContext);
		return this.toolBar;
	}

	
	@Override
	public Set<UgoPanel> getAllPanels() {
		Object[] comp = this.tabs.getComponents();
		Set<UgoPanel> panels = new HashSet<UgoPanel>(comp.length);
		for (int i = 0; i < comp.length; i++)
			panels.add(this.graphPanelsMap.get(comp[i]));
		return panels;
	}
	
	@Override
	public ElementsPanel getCurrentPanel() {
		if (this.tabs.getSelectedIndex() != -1)
			return this.graphPanelsMap.get(this.tabs.getComponentAt(this.tabs.getSelectedIndex()));
		return null;
//		SchemePanel newPanel = new SchemePanel(aContext);
//		addPanel(newPanel);
//		return newPanel;
	}
	
	public void addPanel(ElementsPanel p) {
		SchemeGraph graph = p.getGraph();
		graph.setMarqueeHandler(this.marqueeHandler);
		graph.addKeyListener(this.keyListener);
		JScrollPane graphView = new JScrollPane(graph);
		
		if (this.graphPanelsMap == null)
			this.graphPanelsMap = new HashMap<JScrollPane, ElementsPanel>();
		this.graphPanelsMap.put(graphView, p);
		
		this.tabs.addTab("", new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/close_unchanged.gif")), graphView);
		this.tabs.setSelectedComponent(graphView);
		setGraphChanged(false);
		graph.setEditable(this.editable);
	}
	
	public void selectPanel(ElementsPanel p) {
		Object[] comp = this.tabs.getComponents();
		for (int i = 0; i < comp.length; i++) {
			UgoPanel p1 = this.graphPanelsMap.get(comp[i]);
			if (p1.equals(p)) {
				this.tabs.setSelectedIndex(i);
				return;
			}
		}
	}

	public void updateTitle(String title) {
		this.tabs.setTitleAt(this.tabs.getSelectedIndex(), title);
	}

	@Override
	public boolean removePanel(UgoPanel p) {
		if (super.removePanel(p)) {
			Object[] comp = this.tabs.getComponents();
			for (int i = 0; i < comp.length; i++) {
				UgoPanel p1 = this.graphPanelsMap.get(comp[i]);
				if (p1.equals(p)) {
					this.tabs.removeTabAt(i);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean removeAllPanels() {
		Object[] comp = this.tabs.getComponents();
		// check for unsaved changes
		for (int i = 0; i < comp.length; i++) {
			UgoPanel p = this.graphPanelsMap.get(comp[i]);
			if (p instanceof ElementsPanel) {
				if (p.getGraph().isGraphChanged()) {
					return super.removePanel(p);
				}
			}
		}
		return true;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent) ae;
			if (see.isType(SchemeEvent.OPEN_SCHEME)) {
				Scheme scheme = (Scheme) see.getObject();
				openScheme(scheme);
			} else if (see.isType(SchemeEvent.OPEN_SCHEMEELEMENT)) {
				SchemeElement schemeElement = (SchemeElement) see.getObject();
				openSchemeElement(schemeElement);
			} else if (see.isType(SchemeEvent.INSERT_SCHEME)) {
				Scheme scheme = (Scheme) see.getObject();
				ElementsPanel panel1 = getCurrentPanel();
				try {
					SchemeElement schemeElement = null;
					if (panel1.getSchemeResource().getCellContainerType() == SchemeResource.SCHEME) {
						Scheme parentScheme = panel1.getSchemeResource().getScheme();
						schemeElement = SchemeObjectsFactory.createSchemeElement(parentScheme, scheme);
					} else {
						Log.debugMessage(getClass().getSimpleName() + " | Unsupported CellContainerType " + panel1.getSchemeResource().getCellContainerType(), Level.FINER);
						return;
					}
					Map<Identifier, Identifier>clonedIds = schemeElement.getClonedIdMap();
					SchemeImageResource res = scheme.getUgoCell();
					if (res == null)
						res = scheme.getSchemeCell();
					Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = super.openSchemeImageResource(res, true);
					SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
					SchemeGraph graph = panel1.getGraph();
					SchemeImageResource seRes = schemeElement.getUgoCell();
					if (seRes == null) {
						seRes = SchemeObjectsFactory.createSchemeImageResource();
						schemeElement.setUgoCell(seRes);
					}
					seRes.setData((List<Object>)graph.getArchiveableState());
					graph.selectionNotify();
				} catch (CreateObjectException e) {
					Log.errorException(e);
				}
			} else if (see.isType(SchemeEvent.INSERT_SCHEMEELEMENT)) {
				SchemeElement schemeElement = (SchemeElement) see.getObject();
				SchemeImageResource res = schemeElement.getUgoCell();
				if (res == null)
					res = schemeElement.getSchemeCell();
				super.openSchemeImageResource(res, true);
			} else if (see.isType(SchemeEvent.INSERT_PROTOELEMENT)) {
				SchemeProtoElement proto = (SchemeProtoElement) see.getObject();
				ElementsPanel panel1 = getCurrentPanel();
				try {
					SchemeElement schemeElement = null;
					if (panel1.getSchemeResource().getCellContainerType() == SchemeResource.SCHEME_ELEMENT) {
						SchemeElement se = panel1.getSchemeResource().getSchemeElement();
						schemeElement = SchemeObjectsFactory.createSchemeElement(se, proto);
					} else if (panel1.getSchemeResource().getCellContainerType() == SchemeResource.SCHEME) {
						Scheme scheme = panel1.getSchemeResource().getScheme();
						schemeElement = SchemeObjectsFactory.createSchemeElement(scheme, proto);
					} else {
						Log.debugMessage(getClass().getSimpleName() + " | Unsupported CellContainerType " + panel1.getSchemeResource().getCellContainerType(), Level.FINER);
						return;
					}
					Map<Identifier, Identifier>clonedIds = schemeElement.getClonedIdMap();
					SchemeImageResource res = schemeElement.getUgoCell();
					if (res == null)
						res = schemeElement.getSchemeCell();
					Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = super.openSchemeImageResource(res, true);
					SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
					SchemeGraph graph = panel1.getGraph();
					schemeElement.getSchemeCell().setData((List<Object>)graph.getArchiveableState());
					graph.selectionNotify();
				} catch (CreateObjectException e) {
					Log.errorException(e);
				}
				return;
			}	else if (see.isType(SchemeEvent.UPDATE_OBJECT)) {
				Object obj = see.getObject();
				if (obj instanceof Scheme) {
					Scheme scheme = (Scheme)obj;
					SchemeGraph graph = getGraph();
					graph.setActualSize(new Dimension(scheme.getWidth(), scheme.getHeight()));
				}
			}
		}
		super.propertyChange(ae);
	}
		
	public Map<DefaultGraphCell, DefaultGraphCell> openScheme(Scheme sch) {
		Map<DefaultGraphCell, DefaultGraphCell> clones = Collections.emptyMap();
		Set panels = getAllPanels();
		for (Iterator it = panels.iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next();
			if (p instanceof SchemePanel) {
				SchemePanel sp = (SchemePanel)p;
				if (sch.equals(sp.getSchemeResource().getScheme())) {
					selectPanel(sp);
					if (p.getGraph().isGraphChanged()) {
						int ret = JOptionPane.showConfirmDialog(
								Environment.getActiveWindow(), "Схема " + sch.getName()
									+ " уже открыта. Открыть сохраненную ранее версию?",
									"Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
						if (ret == JOptionPane.YES_OPTION) {
							sp.getSchemeResource().setScheme(sch);
							clones = super.openSchemeImageResource(sch.getSchemeCell(), false);
						}		
					}
					return clones;
				}
			}				
		}

		SchemePanel p = new SchemePanel(this.aContext);
		addPanel(p);
		p.getSchemeResource().setScheme(sch);
		updateTitle(sch.getName());
		clones = super.openSchemeImageResource(sch.getSchemeCell(), false);
		p.setGraphSize(new Dimension(sch.getWidth(), sch.getHeight()));
		return clones;
	}
	
	public Map<DefaultGraphCell, DefaultGraphCell> openSchemeElement(SchemeElement se) {
		Map<DefaultGraphCell, DefaultGraphCell> clones = Collections.emptyMap();
		Set panels = getAllPanels();
		for (Iterator it = panels.iterator(); it.hasNext();) {
			UgoPanel p1 = (UgoPanel)it.next();
			if (p1 instanceof ElementsPanel) {
				ElementsPanel p = (ElementsPanel)p1;
				if (se.equals(p.getSchemeResource().getSchemeElement())) {
					selectPanel(p);
					if (p.getGraph().isGraphChanged()) {
						int ret = JOptionPane.showConfirmDialog(
								Environment.getActiveWindow(), "Элемент " + se.getName()
								+ " уже открыт. Открыть сохраненную ранее версию?",
								"Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
						if (ret == JOptionPane.YES_OPTION) {
							p.getSchemeResource().setSchemeElement(se);
							clones = super.openSchemeImageResource(se.getSchemeCell(), false);
						}
					}
					return clones;
				}
			}
		}
		ElementsPanel p = new ElementsPanel(this.aContext);
		addPanel(p);
		p.getSchemeResource().setSchemeElement(se);
		updateTitle(se.getName());
		clones = super.openSchemeImageResource(se.getSchemeCell(), false);
		return clones;
	}
	/*
	public boolean removeScheme(Scheme sch) {
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getScheme() != null && sch != null
				&& sch.equals(graph.getScheme())) {
			removePanel(getPanel());
			if (tabs.getTabCount() == 0)
				setGraphChanged(false);
			return true;
		}
		return false;
	}

	public boolean removeSchemeElement(SchemeElement se) {
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getSchemeElement() != null && se != null
				&& se.equals(graph.getSchemeElement())) {
			removePanel(getPanel());
			if (tabs.getTabCount() == 0)
				setGraphChanged(false);
			return true;
		}
		return false;
	}*/
	
	public void setGraphChanged(SchemeGraph graph, boolean b) {
		for (int i = 0; i < this.tabs.getTabCount(); i++) {
			UgoPanel p = this.graphPanelsMap.get(this.tabs.getComponentAt(this.tabs.getSelectedIndex()));
			if (graph.equals(p.getGraph())) {
				graph.setGraphChanged(b);
				if (b)
					this.tabs.setIconAt(i, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
							"images/close_changed.gif")));
				else
					this.tabs.setIconAt(i, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
							"images/close_unchanged.gif")));
				return;
			}
		}
	}
	
	@Override
	public void setGraphChanged(boolean b) {
		if (getGraph().isGraphChanged() == b)
			return;

		super.setGraphChanged(b);
		if (b)
			this.tabs.setIconAt(this.tabs.getSelectedIndex(), new ImageIcon(Toolkit
					.getDefaultToolkit().getImage("images/close_changed.gif")));
		else
			this.tabs.setIconAt(this.tabs.getSelectedIndex(), new ImageIcon(Toolkit
					.getDefaultToolkit().getImage("images/close_unchanged.gif")));
	}
}
