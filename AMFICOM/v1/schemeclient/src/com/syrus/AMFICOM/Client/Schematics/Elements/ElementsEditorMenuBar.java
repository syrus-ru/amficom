package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

public class ElementsEditorMenuBar extends JMenuBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenuItem menuSessionNew = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionOptions = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	JMenu menuComponent = new JMenu();
	JMenuItem menuComponentNew = new JMenuItem();
	JMenuItem menuComponentSave = new JMenuItem();

	JMenu menuWindow = new JMenu();
	JMenuItem menuWindowArrange = new JMenuItem();
	JMenuItem menuWindowTree = new JMenuItem();
	JMenuItem menuWindowScheme = new JMenuItem();
	JMenuItem menuWindowCatalog = new JMenuItem();
	JMenuItem menuWindowUgo = new JMenuItem();
	JMenuItem menuWindowProps = new JMenuItem();
	JMenuItem menuWindowList = new JMenuItem();

	public ElementsEditorMenuBar()
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

	public ElementsEditorMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		ElementsEditorMenuBar_this_actionAdapter actionAdapter =
		new ElementsEditorMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModel.getString("menuSession"));
		menuSession.setName("menuSession");
		menuSessionNew.setText(LangModel.getString("menuSessionNew"));
		menuSessionNew.setName("menuSessionNew");
		menuSessionNew.addActionListener(actionAdapter);
		menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		menuSessionClose.setName("menuSessionClose");
		menuSessionClose.addActionListener(actionAdapter);
		menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
		menuSessionOptions.setName("menuSessionOptions");
		menuSessionOptions.addActionListener(actionAdapter);
		menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		menuSessionConnection.setName("menuSessionConnection");
		menuSessionConnection.addActionListener(actionAdapter);
		menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		menuSessionChangePassword.setName("menuSessionChangePassword");
		menuSessionChangePassword.addActionListener(actionAdapter);
		menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
		menuSessionDomain.addActionListener(actionAdapter);
		menuExit.setText(LangModel.getString("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuComponent.setText(LangModelSchematics.getString("menuComponent"));
		menuComponent.setName("menuComponent");
		menuComponentNew.setText(LangModelSchematics.getString("menuComponentNew"));
		menuComponentNew.setName("menuComponentNew");
		menuComponentNew.addActionListener(actionAdapter);
		menuComponentSave.setText(LangModelSchematics.getString("menuComponentSave"));
		menuComponentSave.setName("menuComponentSave");
		menuComponentSave.addActionListener(actionAdapter);

		menuComponent.add(menuComponentNew);
		menuComponent.add(menuComponentSave);

		menuWindow.setText(LangModelSchematics.getString("menuWindow"));
		menuWindow.setName("menuWindow");
		menuWindowArrange.setText(LangModelSchematics.getString("menuWindowArrange"));
		menuWindowArrange.setName("menuWindowArrange");
		menuWindowArrange.addActionListener(actionAdapter);
		menuWindowTree.setText(LangModelSchematics.getString("menuWindowTree"));
		menuWindowTree.setName("menuWindowTree");
		menuWindowTree.addActionListener(actionAdapter);
		menuWindowScheme.setText(LangModelSchematics.getString("menuWindowElements"));
		menuWindowScheme.setName("menuWindowScheme");
		menuWindowScheme.addActionListener(actionAdapter);
		menuWindowCatalog.setText(LangModelSchematics.getString("menuWindowCatalog"));
		menuWindowCatalog.setName("menuWindowCatalog");
		menuWindowCatalog.addActionListener(actionAdapter);
		menuWindowUgo.setText(LangModelSchematics.getString("menuWindowUgo"));
		menuWindowUgo.setName("menuWindowUgo");
		menuWindowUgo.addActionListener(actionAdapter);
		menuWindowProps.setText(LangModelSchematics.getString("menuWindowProps"));
		menuWindowProps.setName("menuWindowProps");
		menuWindowProps.addActionListener(actionAdapter);
		menuWindowList.setText(LangModelSchematics.getString("menuWindowList"));
		menuWindowList.setName("menuWindowList");
		menuWindowList.addActionListener(actionAdapter);

		menuWindow.add(menuWindowArrange);
		menuWindow.addSeparator();
		menuWindow.add(menuWindowTree);
		menuWindow.add(menuWindowScheme);
		menuWindow.add(menuWindowUgo);
		menuWindow.add(menuWindowProps);
		menuWindow.add(menuWindowList);
		menuWindow.add(menuWindowCatalog);


		this.add(menuSession);
		this.add(menuComponent);
		this.add(menuWindow);
	}

	public void modelChanged(String e[])
	{
		menuSession.setVisible(aModel.isVisible("menuSession"));
		menuSession.setEnabled(aModel.isEnabled("menuSession"));
		menuSessionNew.setVisible(aModel.isVisible("menuSessionNew"));
		menuSessionNew.setEnabled(aModel.isEnabled("menuSessionNew"));
		menuSessionClose.setVisible(aModel.isVisible("menuSessionClose"));
		menuSessionClose.setEnabled(aModel.isEnabled("menuSessionClose"));
		menuSessionOptions.setVisible(aModel.isVisible("menuSessionOptions"));
		menuSessionOptions.setEnabled(aModel.isEnabled("menuSessionOptions"));
		menuSessionConnection.setVisible(aModel.isVisible("menuSessionConnection"));
		menuSessionConnection.setEnabled(aModel.isEnabled("menuSessionConnection"));
		menuSessionChangePassword.setVisible(aModel.isVisible("menuSessionChangePassword"));
		menuSessionChangePassword.setEnabled(aModel.isEnabled("menuSessionChangePassword"));
		menuSessionDomain.setVisible(aModel.isVisible("menuSessionDomain"));
		menuSessionDomain.setEnabled(aModel.isEnabled("menuSessionDomain"));

		menuComponentSave.setVisible(aModel.isVisible("menuComponentSave"));
		menuComponentSave.setEnabled(aModel.isEnabled("menuComponentSave"));
		menuComponentNew.setVisible(aModel.isVisible("menuComponentNew"));
		menuComponentNew.setEnabled(aModel.isEnabled("menuComponentNew"));

		menuWindow.setVisible(aModel.isVisible("menuWindow"));
		menuWindow.setEnabled(aModel.isEnabled("menuWindow"));
		menuWindowArrange.setVisible(aModel.isVisible("menuWindowArrange"));
		menuWindowArrange.setEnabled(aModel.isEnabled("menuWindowArrange"));
		menuWindowTree.setVisible(aModel.isVisible("menuWindowTree"));
		menuWindowTree.setEnabled(aModel.isEnabled("menuWindowTree"));
		menuWindowScheme.setVisible(aModel.isVisible("menuWindowScheme"));
		menuWindowScheme.setEnabled(aModel.isEnabled("menuWindowScheme"));
		menuWindowCatalog.setVisible(aModel.isVisible("menuWindowCatalog"));
		menuWindowCatalog.setEnabled(aModel.isEnabled("menuWindowCatalog"));
		menuWindowUgo.setVisible(aModel.isVisible("menuWindowUgo"));
		menuWindowUgo.setEnabled(aModel.isEnabled("menuWindowUgo"));
		menuWindowProps.setVisible(aModel.isVisible("menuWindowProps"));
		menuWindowProps.setEnabled(aModel.isEnabled("menuWindowProps"));
		menuWindowList.setVisible(aModel.isVisible("menuWindowList"));
		menuWindowList.setEnabled(aModel.isEnabled("menuWindowList"));

	}

	public void setModel (ApplicationModel aModel)
	{
		this.aModel = aModel;
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

class ElementsEditorMenuBar_this_actionAdapter implements java.awt.event.ActionListener
{
	ElementsEditorMenuBar adaptee;

	ElementsEditorMenuBar_this_actionAdapter(ElementsEditorMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

