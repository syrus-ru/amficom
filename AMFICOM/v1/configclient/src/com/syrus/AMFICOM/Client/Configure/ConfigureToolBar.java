package com.syrus.AMFICOM.Client.Configure;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;


public class ConfigureToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton buttonOpenSession = new JButton();
	JButton buttonCloseSession = new JButton();

	JButton buttonNavigator = new JButton();

	JButton buttonDomains = new JButton();
	JButton buttonNetDirectory = new JButton();
	JButton buttonNetCatalogue = new JButton();
	JButton buttonJDirectory = new JButton();
	JButton buttonJCatalogue = new JButton();

	JButton buttonComponentEditor = new JButton();
	JButton buttonSchemeEditor = new JButton();
	JButton buttonMapEditor = new JButton();

	JButton buttonSchemeViewer = new JButton();
	JButton buttonMapViewer = new JButton();

	JButton buttonMaintenance = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public ConfigureToolBar()
	{
		super();

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		ConfigureToolBar_this_actionAdapter actionAdapter =
			new ConfigureToolBar_this_actionAdapter(this);
		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		buttonOpenSession = new JButton();
		buttonOpenSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonOpenSession.setText("");
		buttonOpenSession.setMaximumSize(buttonSize);
		buttonOpenSession.setPreferredSize(buttonSize);
		buttonOpenSession.setToolTipText(LangModel.ToolTip("menuSessionNew"));
		buttonOpenSession.setName("menuSessionNew");
		buttonOpenSession.addActionListener(actionAdapter);
		add(buttonOpenSession);

		buttonCloseSession = new JButton();
		buttonCloseSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonCloseSession.setText("");
		buttonCloseSession.setMaximumSize(buttonSize);
		buttonCloseSession.setPreferredSize(buttonSize);
		buttonCloseSession.setToolTipText(LangModel.ToolTip("menuSessionClose"));
		buttonCloseSession.setName("menuSessionClose");
		buttonCloseSession.addActionListener(actionAdapter);
//		add(buttonCloseSession);

		add(new JLabel("  "));

		buttonNavigator = new JButton();
		buttonNavigator.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/object_navigator.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonNavigator.setText("");
		buttonNavigator.setMaximumSize(buttonSize);
		buttonNavigator.setPreferredSize(buttonSize);
		buttonNavigator.setToolTipText(LangModelConfig.ToolTip("menuViewNavigator"));
		buttonNavigator.setName("menuViewNavigator");
		buttonNavigator.addActionListener(actionAdapter);
		add(buttonNavigator);

		buttonSchemeViewer = new JButton();
		buttonSchemeViewer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/schematics_mini.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonSchemeViewer.setText("");
		buttonSchemeViewer.setMaximumSize(buttonSize);
		buttonSchemeViewer.setPreferredSize(buttonSize);
		buttonSchemeViewer.setToolTipText(LangModelConfig.ToolTip("menuSchemeNetOpenScheme"));
		buttonSchemeViewer.setName("menuSchemeNetOpenScheme");
		buttonSchemeViewer.addActionListener(actionAdapter);
		add(buttonSchemeViewer);

		buttonMapViewer = new JButton();
		buttonMapViewer.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonMapViewer.setText("");
		buttonMapViewer.setMaximumSize(buttonSize);
		buttonMapViewer.setPreferredSize(buttonSize);
		buttonMapViewer.setToolTipText(LangModelConfig.ToolTip("menuSchemeNetOpen"));
		buttonMapViewer.setName("menuSchemeNetOpen");
		buttonMapViewer.addActionListener(actionAdapter);
		add(buttonMapViewer);

		add(new JLabel("  "));

		buttonDomains = new JButton();
		buttonDomains.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/domains.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonDomains.setText("");
		buttonDomains.setMaximumSize(buttonSize);
		buttonDomains.setPreferredSize(buttonSize);
		buttonDomains.setToolTipText(LangModelConfig.ToolTip("menuObjectDomain"));
		buttonDomains.setName("menuObjectDomain");
		buttonDomains.addActionListener(actionAdapter);
		add(buttonDomains);


		buttonNetDirectory = new JButton();
		buttonNetDirectory.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/netdirectory.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonNetDirectory.setText("");
		buttonNetDirectory.setMaximumSize(buttonSize);
		buttonNetDirectory.setPreferredSize(buttonSize);
		buttonNetDirectory.setToolTipText(LangModelConfig.ToolTip("menuNetDir"));
		buttonNetDirectory.setName("menuObjectNetDir");
		buttonNetDirectory.addActionListener(actionAdapter);
		add(buttonNetDirectory);


		buttonNetCatalogue = new JButton();
		buttonNetCatalogue.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/netcatalogue.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonNetCatalogue.setText("");
		buttonNetCatalogue.setMaximumSize(buttonSize);
		buttonNetCatalogue.setPreferredSize(buttonSize);
		buttonNetCatalogue.setToolTipText(LangModelConfig.ToolTip("menuNetCat"));
		buttonNetCatalogue.setName("menuObjectNetCat");
		buttonNetCatalogue.addActionListener(actionAdapter);
		add(buttonNetCatalogue);


		buttonJDirectory = new JButton();
		buttonJDirectory.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/jdirectory.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonJDirectory.setText("");
		buttonJDirectory.setMaximumSize(buttonSize);
		buttonJDirectory.setPreferredSize(buttonSize);
		buttonJDirectory.setToolTipText(LangModelConfig.ToolTip("menuJDir"));
		buttonJDirectory.setName("menuObjectJDir");
		buttonJDirectory.addActionListener(actionAdapter);
		add(buttonJDirectory);


		buttonJCatalogue = new JButton();
		buttonJCatalogue.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/jcatalogue.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonJCatalogue.setText("");
		buttonJCatalogue.setMaximumSize(buttonSize);
		buttonJCatalogue.setPreferredSize(buttonSize);
		buttonJCatalogue.setToolTipText(LangModelConfig.ToolTip("menuJCat"));
		buttonJCatalogue.setName("menuObjectJCat");
		buttonJCatalogue.addActionListener(actionAdapter);
		add(buttonJCatalogue);

		add(new JLabel("  "));

		buttonComponentEditor = new JButton();
		buttonComponentEditor.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/components.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonComponentEditor.setText("");
		buttonComponentEditor.setMaximumSize(buttonSize);
		buttonComponentEditor.setPreferredSize(buttonSize);
		buttonComponentEditor.setToolTipText(LangModelConfig.ToolTip("menuSchemeNetElement"));
		buttonComponentEditor.setName("menuSchemeNetElement");
		buttonComponentEditor.addActionListener(actionAdapter);
		add(buttonComponentEditor);

		buttonSchemeEditor = new JButton();
		buttonSchemeEditor.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/schematics.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonSchemeEditor.setText("");
		buttonSchemeEditor.setMaximumSize(buttonSize);
		buttonSchemeEditor.setPreferredSize(buttonSize);
		buttonSchemeEditor.setToolTipText(LangModelConfig.ToolTip("menuSchemeNetScheme"));
		buttonSchemeEditor.setName("menuSchemeNetScheme");
		buttonSchemeEditor.addActionListener(actionAdapter);
		add(buttonSchemeEditor);

		buttonMapEditor = new JButton();
		buttonMapEditor.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonMapEditor.setText("");
		buttonMapEditor.setMaximumSize(buttonSize);
		buttonMapEditor.setPreferredSize(buttonSize);
		buttonMapEditor.setToolTipText(LangModelConfig.ToolTip("menuSchemeNetView"));
		buttonMapEditor.setName("menuSchemeNetView");
		buttonMapEditor.addActionListener(actionAdapter);
		add(buttonMapEditor);

		add(new JLabel("  "));

		buttonMaintenance = new JButton();
		buttonMaintenance.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/maintain.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonMaintenance.setText("");
		buttonMaintenance.setMaximumSize(buttonSize);
		buttonMaintenance.setPreferredSize(buttonSize);
		buttonMaintenance.setToolTipText(LangModelConfig.ToolTip("menuMaintainAlertRule"));
		buttonMaintenance.setName("menuMaintainAlertRule");
		buttonMaintenance.addActionListener(actionAdapter);
		add(buttonMaintenance);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void modelChanged(String e[])
	{
		buttonOpenSession.setVisible(aModel.isVisible("menuSessionNew"));
		buttonOpenSession.setEnabled(aModel.isEnabled("menuSessionNew"));

		buttonCloseSession.setVisible(aModel.isVisible("menuSessionClose"));
		buttonCloseSession.setEnabled(aModel.isEnabled("menuSessionClose"));

		buttonNavigator.setVisible(aModel.isVisible("menuViewNavigator"));
		buttonNavigator.setEnabled(aModel.isEnabled("menuViewNavigator"));

		buttonDomains.setVisible(aModel.isVisible("menuObjectDomain"));
		buttonDomains.setEnabled(aModel.isEnabled("menuObjectDomain"));

		buttonNetDirectory.setVisible(aModel.isVisible("menuObjectNetDir"));
		buttonNetDirectory.setEnabled(aModel.isEnabled("menuObjectNetDir"));

		buttonNetCatalogue.setVisible(aModel.isVisible("menuObjectNetCat"));
		buttonNetCatalogue.setEnabled(aModel.isEnabled("menuObjectNetCat"));

		buttonJDirectory.setVisible(aModel.isVisible("menuObjectJDir"));
		buttonJDirectory.setEnabled(aModel.isEnabled("menuObjectJDir"));

		buttonJCatalogue.setVisible(aModel.isVisible("menuObjectJCat"));
		buttonJCatalogue.setEnabled(aModel.isEnabled("menuObjectJCat"));

		buttonComponentEditor.setVisible(aModel.isVisible("menuSchemeNetElement"));
		buttonComponentEditor.setEnabled(aModel.isEnabled("menuSchemeNetElement"));

		buttonSchemeEditor.setVisible(aModel.isVisible("menuSchemeNetScheme"));
		buttonSchemeEditor.setEnabled(aModel.isEnabled("menuSchemeNetScheme"));

		buttonMapEditor.setVisible(aModel.isVisible("menuSchemeNetView"));
		buttonMapEditor.setEnabled(aModel.isEnabled("menuSchemeNetView"));

		buttonSchemeViewer.setVisible(aModel.isVisible("menuSchemeNetOpenScheme"));
		buttonSchemeViewer.setEnabled(aModel.isEnabled("menuSchemeNetOpenScheme"));

		buttonMapViewer.setVisible(aModel.isVisible("menuSchemeNetOpen"));
		buttonMapViewer.setEnabled(aModel.isEnabled("menuSchemeNetOpen"));

		buttonMaintenance.setVisible(aModel.isVisible("menuMaintainAlertRule"));
		buttonMaintenance.setEnabled(aModel.isEnabled("menuMaintainAlertRule"));
	}

	public void this_actionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command = (Command )command.clone();
		command.execute();
	}
}

class ConfigureToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	ConfigureToolBar adaptee;

	ConfigureToolBar_this_actionAdapter(ConfigureToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}
