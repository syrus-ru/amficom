/*-
 * $Id: SchemeTabbedPane.java,v 1.4 2005/06/22 10:16:06 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/06/22 10:16:06 $
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
	
	public UgoPanel getCurrentPanel() {
		if (tabs.getSelectedIndex() != -1)
			return (UgoPanel)graphPanelsMap.get(tabs.getComponentAt(tabs.getSelectedIndex()));
		return null;
//		SchemePanel newPanel = new SchemePanel(aContext);
//		addPanel(newPanel);
//		return newPanel;
	}
	
	public void addPanel(UgoPanel p) {
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
	
	public void selectPanel(UgoPanel p) {
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
	
	public void openScheme(Scheme sch) {
		Set panels = getAllPanels();
		for (Iterator it = panels.iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next(); 
			if (sch.equals(p.getSchemeResource().getScheme())) {
				selectPanel(p);
				if (p instanceof SchemePanel
						&& p.getGraph().isGraphChanged()) {
					int ret = JOptionPane.showConfirmDialog(
							Environment.getActiveWindow(), "Схема " + sch.getName()
									+ " уже открыта. Открыть сохраненную ранее версию?",
							"Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
					if (ret == JOptionPane.YES_OPTION) {
						super.openScheme(sch);
						setGraphChanged(false);
					}
				}
				updateTitle(sch.getName());
				return;
			}
		}

		SchemePanel p = new SchemePanel(aContext);
		addPanel(p);
		super.openScheme(sch);
		updateTitle(sch.getName());
		setGraphChanged(false);
	}
	
	public void openSchemeElement(SchemeElement se) {
		Set panels = getAllPanels();
		for (Iterator it = panels.iterator(); it.hasNext();) {
			UgoPanel p = (UgoPanel)it.next();
			if (se.equals(p.getSchemeResource().getSchemeElement())) {
				selectPanel(p);
				if (p instanceof ElementsPanel
						&& p.getGraph().isGraphChanged()) {
					int ret = JOptionPane.showConfirmDialog(
							Environment.getActiveWindow(), "Элемент " + se.getName()
									+ " уже открыт. Открыть сохраненную ранее версию?",
							"Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION);
					if (ret == JOptionPane.YES_OPTION) {
						super.openSchemeElement(se);
						setGraphChanged(false);
					}
				}
				return;
			}
		}
		SchemePanel p = new SchemePanel(aContext);
		addPanel(p);
		super.openSchemeElement(se);
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
