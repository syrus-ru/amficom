package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.Map;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class SchemeTabbedPane extends ElementsTabbedPane
{
	JTabbedPane tabs = new JTabbedPane();

	public SchemeTabbedPane(ApplicationContext aContext)
	{
		super(aContext);
		tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent e)
			{
				if (SwingUtilities.isRightMouseButton(e) && tabs.getTabCount() > 1)
				{
					JPopupMenu popup = new JPopupMenu();
					JMenuItem close = new JMenuItem(new AbstractAction()
					{
						public void actionPerformed(ActionEvent ae)
						{
							tabs.removeTabAt(tabs.getSelectedIndex());
						}
					});
					close.setText("Закрыть \"" + tabs.getTitleAt(tabs.getSelectedIndex()) + "\"");
					popup.add(close);
					popup.show(tabs, e.getX(), e.getY());
				}
			}
		});
		setLayout(new BorderLayout());
		add(tabs, BorderLayout.CENTER);
	}

	public UgoPanel getPanel()
	{
		return (UgoPanel)tabs.getComponentAt(tabs.getSelectedIndex());
	}

	public void addSchemeTab(SchemePanel panel)
	{
		Scheme scheme = panel.getGraph().getScheme();
		SchemeElement se = panel.getGraph().getSchemeElement();
		String name;
		name = se == null ? "" : se.getName();
		name = scheme == null ? name : scheme.getName();
		tabs.addTab(name, panel);
		tabs.setSelectedComponent(panel);
	}

	protected void openScheme(Scheme sch)
	{
		SchemePanel panel = new SchemePanel(aContext);
		panel.openScheme(sch);
		addSchemeTab(panel);
	}

	protected boolean removeScheme(Scheme sch)
	{
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getScheme() != null && sch != null && sch.getId().equals(graph.getScheme().getId()))
		{
			GraphActions.clearGraph(graph);
			return true;
		}
		return false;
	}


	protected void openSchemeElement(SchemeElement se)
	{
		SchemeGraph graph = getPanel().getGraph();
		graph.setScheme(null);
		super.openSchemeElement(se);
	}
}