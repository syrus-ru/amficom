package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.scheme.corba.*;

public class SchemeTabbedPane extends ElementsTabbedPane
{
	JTabbedPane tabs;

	public SchemeTabbedPane(ApplicationContext aContext)
	{
		super(aContext);
	}

	protected void init_panel()
	{
		tabs = new JTabbedPane(SwingConstants.TOP);
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
							if (getPanel().getGraph().getScheme() != null)
								aContext.getDispatcher().notify(new SchemeElementsEvent(
										this, getPanel().getGraph().getScheme(), SchemeElementsEvent.CLOSE_SCHEME_EVENT));
							else if (getPanel().getGraph().getSchemeElement() != null)
								aContext.getDispatcher().notify(new SchemeElementsEvent(
										this, getPanel().getGraph().getSchemeElement(), SchemeElementsEvent.CLOSE_SCHEME_EVENT));
//							removePanel(getPanel());
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

	public UgoPanel[] getAllPanels()
	{
		Object[] comp = tabs.getComponents();
		UgoPanel[] p = new UgoPanel[comp.length];
		for (int i = 0; i < p.length; i++)
			p[i] = (UgoPanel)comp[i];
		return p;
	}

	public UgoPanel getPanel()
	{
		if (tabs.getSelectedIndex() != -1)
			return (UgoPanel)tabs.getComponentAt(tabs.getSelectedIndex());

		SchemePanel newPanel = new SchemePanel(aContext);
//		Scheme scheme = SchemeFactory.createScheme();
//		scheme.name("Новая схема");
//		panel.getGraph().setScheme(scheme);

		addPanel(newPanel);
//		updateTitle(scheme.name());

		return newPanel;
	}

	public void addPanel(UgoPanel p)
	{
//		Scheme scheme = panel.getGraph().getScheme();
//		SchemeElement se = panel.getGraph().getSchemeElement();
		tabs.addTab(
				"",
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_unchanged.gif")),
				p);
		tabs.setSelectedComponent(p);
		setGraphChanged(false);
	}

	public void selectPanel(UgoPanel p)
	{
		tabs.setSelectedComponent(p);
	}

	public void updateTitle(String title)
	{
		tabs.setTitleAt(tabs.getSelectedIndex(), title);
	}

	public boolean removePanel(UgoPanel p)
	{
		if (super.removePanel(p))
		{
			tabs.remove(p);
			return true;
		}
		return false;
	}

	public void removeAllPanels()
	{
		Object[] comp = tabs.getComponents();
		for (int i = 0; i < comp.length; i++)
			remove((UgoPanel)comp[i]);
	}

	public void setGraphChanged(SchemeGraph graph, boolean b)
	{
		for (int i = 0; i < tabs.getTabCount(); i++)
		{
			UgoPanel p = (UgoPanel)tabs.getComponentAt(i);
			if (graph.equals(p.getGraph()))
			{
				graph.setGraphChanged(b);
				if (b)
					tabs.setIconAt(i,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_changed.gif")));
				else
					tabs.setIconAt(i,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_unchanged.gif")));

				return;
			}
		}
	}


	public void setGraphChanged(boolean b)
	{
		if (getPanel().getGraph().isGraphChanged() == b)
			return;

		super.setGraphChanged(b);
		if (b)
			tabs.setIconAt(tabs.getSelectedIndex(),
					new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_changed.gif")));
		else
			tabs.setIconAt(tabs.getSelectedIndex(),
					new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_unchanged.gif")));
	}

	public void openScheme(Scheme sch)
	{
		UgoPanel[] panels = getAllPanels();
		for (int i = 0; i < panels.length; i++)
		{
			if (sch.equals(panels[i].getGraph().getScheme()))
			{
				tabs.setSelectedComponent(panels[i]);
				if (panels[i] instanceof SchemePanel && panels[i].getGraph().isGraphChanged())
				{
					int ret = JOptionPane.showConfirmDialog(
							Environment.getActiveWindow(),
							"Схема " + sch.name() + " уже открыта. Открыть сохраненную ранее версию?",
							"Подтверждение",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (ret == JOptionPane.YES_OPTION)
					{
						GraphActions.clearGraph(panels[i].getGraph());
						((SchemePanel)panels[i]).openScheme(sch);
						setGraphChanged(false);
					}
				}
				updateTitle(sch.name());
				return;
			}
		}

		SchemePanel p = new SchemePanel(aContext);
		addPanel(p);
		p.openScheme(sch);
		updateTitle(sch.name());
		setGraphChanged(false);
	}

	public void openSchemeElement(SchemeElement se)
	{
		UgoPanel[] panels = getAllPanels();
		for (int i = 0; i < panels.length; i++)
		{
			if (se.equals(panels[i].getGraph().getSchemeElement()))
			{
				tabs.setSelectedComponent(panels[i]);
				if (panels[i] instanceof ElementsPanel && panels[i].getGraph().isGraphChanged())
				{
					int ret = JOptionPane.showConfirmDialog(
							Environment.getActiveWindow(),
							"Элемент " + se.name() + " уже открыт. Открыть сохраненную ранее версию?",
							"Подтверждение",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (ret == JOptionPane.YES_OPTION)
					{
						GraphActions.clearGraph(panels[i].getGraph());
						((ElementsPanel) panels[i]).openSchemeElement(se);
						setGraphChanged(false);
					}
				}
				return;
			}
		}
		SchemePanel p = new SchemePanel(aContext);
		addPanel(p);
		p.openSchemeElement(se);
		updateTitle(se.name());
		setGraphChanged(false);
	}

	public boolean removeScheme(Scheme sch)
	{
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getScheme() != null && sch != null && sch.equals(graph.getScheme()))
		{
			removePanel(getPanel());
			if (tabs.getTabCount() == 0)
				setGraphChanged(false);
			return true;
		}
		return false;
	}

	public boolean removeSchemeElement(SchemeElement se)
	{
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getSchemeElement() != null && se != null && se.equals(graph.getSchemeElement()))
		{
			removePanel(getPanel());
			if (tabs.getTabCount() == 0)
				setGraphChanged(false);
			return true;
		}
		return false;
	}

}