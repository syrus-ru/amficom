/*-
 * $Id: SchemeTabbedPane.java,v 1.1 2005/04/05 14:07:53 stas Exp $
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

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class SchemeTabbedPane extends ElementsTabbedPane {
	JTabbedPane tabs;
	Map graphPanelsMap;

	public SchemeTabbedPane(ApplicationContext aContext) {
		super(aContext);
	}

	protected UgoPanel createPanel() {
		tabs = new JTabbedPane(SwingConstants.BOTTOM);
		tabs.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && tabs.getTabCount() > 1) {
					JPopupMenu popup = new JPopupMenu();
					JMenuItem close = new JMenuItem(new AbstractAction() {
						public void actionPerformed(ActionEvent ae) {
							SchemeResource res = getCurrentPanel().getSchemeResource();
							if (res.getScheme() != null)
								aContext.getDispatcher().notify(
										new SchemeEvent(this, res.getScheme(), null,
												SchemeEvent.CLOSE_SCHEME));
							else if (res.getSchemeElement() != null)
								aContext.getDispatcher().notify(
										new SchemeEvent(this, res.getSchemeElement(), null,
												SchemeEvent.CLOSE_SCHEMEELEMENT));
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
		SchemePanel p = new SchemePanel(aContext);
		addPanel(p);
		return p;
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

		SchemePanel newPanel = new SchemePanel(aContext);
		addPanel(newPanel);

		return newPanel;
	}
	
	public void addPanel(UgoPanel p) {
		SchemeGraph graph = p.getGraph();
		graph.setMarqueeHandler(marqueeHandler);
		tabs.addTab("", new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/close_unchanged.gif")), graph);
		tabs.setSelectedComponent(graph);
		graph.setGraphChanged(false);
		if (graphPanelsMap == null)
			graphPanelsMap = new HashMap();
		graphPanelsMap.put(graph, p);
	}
	
	public void selectPanel(UgoPanel p) {
		tabs.setSelectedComponent(p.getGraph());
	}

	public void updateTitle(String title) {
		tabs.setTitleAt(tabs.getSelectedIndex(), title);
	}

	public boolean removePanel(UgoPanel p) {
		if (super.removePanel(p)) {
			tabs.remove(p.getGraph());
			return true;
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
			UgoPanel panel = (UgoPanel)it.next(); 
			if (sch.equals(panel.getSchemeResource().getScheme())) {
				tabs.setSelectedComponent(panel.getGraph());
				if (panel instanceof SchemePanel
						&& panel.getGraph().isGraphChanged()) {
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
		tabs.setSelectedComponent(p.getGraph());
		super.openScheme(sch);
		updateTitle(sch.getName());
		setGraphChanged(false);
	}
	
	public void openSchemeElement(SchemeElement se) {
		Set panels = getAllPanels();
		for (Iterator it = panels.iterator(); it.hasNext();) {
			UgoPanel panel = (UgoPanel)it.next();
			if (se.equals(panel.getSchemeResource().getSchemeElement())) {
				tabs.setSelectedComponent(panel.getGraph());
				if (panel instanceof ElementsPanel
						&& panel.getGraph().isGraphChanged()) {
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
		tabs.setSelectedComponent(p.getGraph());
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
