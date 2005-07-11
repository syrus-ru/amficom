/*-
 * $Id: SchemeTabbedPane.java,v 1.5 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class SchemeTabbedPane extends ElementsTabbedPane {
	JTabbedPane tabs;
	Map graphPanelsMap;

	public SchemeTabbedPane(ApplicationContext aContext) {
		super(aContext);
	}

	protected JComponent createPanel() {
		tabs = new JTabbedPane(SwingConstants.BOTTOM);
		tabs.addChangeListener(new ChangeListener() {
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
		
		tabs.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && tabs.getTabCount() > 1) {
					JPopupMenu popup = new JPopupMenu();
					JMenuItem close = new JMenuItem(new AbstractAction() {
						public void actionPerformed(ActionEvent ae) {
							removePanel(getCurrentPanel());
						}
					});
					close.setText("Закрыть \"" + tabs.getTitleAt(tabs.getSelectedIndex())
							+ "\"");
					popup.add(close);
					popup.show(tabs, e.getX(), e.getY());
				}
			}
		});
		setLayout(new BorderLayout());
		add(tabs, BorderLayout.CENTER);
//		addPanel(new SchemePanel(aContext));
		return tabs;
	}
	
	protected JComponent createToolBar() {
		return new SchemeToolBar(this, aContext);
	}

	
	public Set getAllPanels() {
		Object[] comp = tabs.getComponents();
		Set panels = new HashSet(comp.length);
		for (int i = 0; i < comp.length; i++)
			panels.add(graphPanelsMap.get(comp[i]));
		return panels;
	}
	
	public ElementsPanel getCurrentPanel() {
		if (tabs.getSelectedIndex() != -1)
			return (ElementsPanel)graphPanelsMap.get(tabs.getComponentAt(tabs.getSelectedIndex()));
		return null;
//		SchemePanel newPanel = new SchemePanel(aContext);
//		addPanel(newPanel);
//		return newPanel;
	}
	
	public void addPanel(ElementsPanel p) {
		SchemeGraph graph = p.getGraph();
		graph.setMarqueeHandler(marqueeHandler);
		graph.addKeyListener(new SchemeKeyListener());
		JScrollPane graphView = new JScrollPane(graph);
		
		if (graphPanelsMap == null)
			graphPanelsMap = new HashMap();
		graphPanelsMap.put(graphView, p);
		
		tabs.addTab("", new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/close_unchanged.gif")), graphView);
		tabs.setSelectedComponent(graphView);
		graph.setGraphChanged(false);
	}
	
	public void selectPanel(ElementsPanel p) {
		Object[] comp = tabs.getComponents();
		for (int i = 0; i < comp.length; i++) {
			UgoPanel p1 = (UgoPanel)graphPanelsMap.get(comp[i]);
			if (p1.equals(p)) {
				tabs.removeTabAt(i);
				return;
			}
		}
	}

	public void updateTitle(String title) {
		tabs.setTitleAt(tabs.getSelectedIndex(), title);
	}

	public boolean removePanel(UgoPanel p) {
		if (super.removePanel(p)) {
			Object[] comp = tabs.getComponents();
			for (int i = 0; i < comp.length; i++) {
				UgoPanel p1 = (UgoPanel)graphPanelsMap.get(comp[i]);
				if (p1.equals(p)) {
					tabs.removeTabAt(i);
					return true;
				}
			}
		}
		return false;
	}

	public void removeAllPanels() {
		Object[] comp = tabs.getComponents();
		for (int i = 0; i < comp.length; i++)
			removePanel((UgoPanel)graphPanelsMap.get(comp[i]));
	}
	
	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent see = (SchemeEvent) ae;
			if (see.isType(SchemeEvent.OPEN_SCHEME)) {
				Scheme scheme = (Scheme) see.getObject();
				openScheme(scheme);
			} else if (see.isType(SchemeEvent.OPEN_SCHEMEELEMENT)) {
				SchemeElement schemeElement = (SchemeElement) see.getObject();
				openSchemeElement(schemeElement);
			} else if (see.isType(SchemeEvent.UPDATE_OBJECT)) {
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
	
	public void openSchemeCellContainer(SchemeCellContainer schemeCellContainer) {
		if (schemeCellContainer instanceof Scheme) {
			openScheme((Scheme)schemeCellContainer);
		} else if (schemeCellContainer instanceof SchemeElement) {
			openSchemeElement((SchemeElement)schemeCellContainer);
		} else {
			Log.debugMessage("Error: try to open SchemeProtoElement in SchemeTabbedPane ", Level.FINER);
		}
	}
	
	public void openScheme(Scheme sch) {
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
							GraphActions.clearGraph(sp.getGraph());
							if (sch.getSchemeCell() != null)
								sp.insertCell(sch.getSchemeCell().getData(), new Point(0, 0), true);
							fixImages(getGraph());
							setGraphChanged(false);
						}		
					}
					updateTitle(sch.getName());
					return;
				}
			}				
		}

		SchemePanel p = new SchemePanel(aContext);
		addPanel(p);
		p.getSchemeResource().setScheme(sch);
		updateTitle(sch.getName());
		if (sch.getSchemeCell() != null)
			p.insertCell(sch.getSchemeCell().getData(), new Point(0, 0), true);
		fixImages(getGraph());
		setGraphChanged(false);
	}
	
	public void openSchemeElement(SchemeElement se) {
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
							GraphActions.clearGraph(p.getGraph());
							p.insertCell(se.getSchemeCell().getData(), new Point(0, 0), true);
							fixImages(getGraph());
							setGraphChanged(false);
						}
					}
					return;
				}
			}
		}
		ElementsPanel p = new ElementsPanel(aContext);
		addPanel(p);
		p.getSchemeResource().setSchemeElement(se);
		p.insertCell(se.getSchemeCell().getData(), new Point(0, 0), true);
		fixImages(getGraph());
		updateTitle(se.getName());
		setGraphChanged(false);
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
		for (int i = 0; i < tabs.getTabCount(); i++) {
			UgoPanel p = (UgoPanel)graphPanelsMap.get(tabs.getComponentAt(tabs.getSelectedIndex()));
			if (graph.equals(p.getGraph())) {
				graph.setGraphChanged(b);
				if (b)
					tabs.setIconAt(i, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
							"images/close_changed.gif")));
				else
					tabs.setIconAt(i, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
							"images/close_unchanged.gif")));
				return;
			}
		}
	}
	
	public void setGraphChanged(boolean b) {
		if (getGraph().isGraphChanged() == b)
			return;

		super.setGraphChanged(b);
		if (b)
			tabs.setIconAt(tabs.getSelectedIndex(), new ImageIcon(Toolkit
					.getDefaultToolkit().getImage("images/close_changed.gif")));
		else
			tabs.setIconAt(tabs.getSelectedIndex(), new ImageIcon(Toolkit
					.getDefaultToolkit().getImage("images/close_unchanged.gif")));
	}
}
