package com.syrus.AMFICOM.Client.Schematics.Scheme;

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

public class SchemeEditorMenuBar extends JMenuBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenuItem menuSessionNew = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionOptions = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
	JMenuItem menuSessionSave = new JMenuItem();
	JMenuItem menuSessionUndo = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	JMenu menuScheme = new JMenu();
	JMenuItem menuSchemeNew = new JMenuItem();
	JMenuItem menuSchemeLoad = new JMenuItem();
	JMenuItem menuSchemeSave = new JMenuItem();
	JMenuItem menuSchemeSaveAs = new JMenuItem();
	JMenuItem menuInsertToCatalog = new JMenuItem();
	JMenuItem menuSchemeImport = new JMenuItem();
	JMenuItem menuSchemeExport = new JMenuItem();

	JMenu menuPath = new JMenu();
	JMenuItem menuPathNew = new JMenuItem();
	JMenuItem menuPathEdit = new JMenuItem();
	JMenuItem menuPathSave = new JMenuItem();
	JMenuItem menuPathCancel = new JMenuItem();
	JMenuItem menuPathDelete = new JMenuItem();
	JMenuItem menuPathAddStart = new JMenuItem();
	JMenuItem menuPathAddEnd = new JMenuItem();
	JMenuItem menuPathAddLink = new JMenuItem();
	JMenuItem menuPathRemoveLink = new JMenuItem();
	JMenuItem menuPathUpdateLink = new JMenuItem();

	JMenu menuWindow = new JMenu();
	JMenuItem menuWindowArrange = new JMenuItem();
	JMenuItem menuWindowTree = new JMenuItem();
	JMenuItem menuWindowScheme = new JMenuItem();
	JMenuItem menuWindowCatalog = new JMenuItem();
	JMenuItem menuWindowUgo = new JMenuItem();
	JMenuItem menuWindowProps = new JMenuItem();
	JMenuItem menuWindowList = new JMenuItem();

	public SchemeEditorMenuBar()
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

	public SchemeEditorMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		SchemeEditorMenuBar_this_actionAdapter actionAdapter =
		new SchemeEditorMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModel.Text("menuSession"));
		menuSession.setName("menuSession");
		menuSessionNew.setText(LangModel.Text("menuSessionNew"));
		menuSessionNew.setName("menuSessionNew");
		menuSessionNew.addActionListener(actionAdapter);
		menuSessionClose.setText(LangModel.Text("menuSessionClose"));
		menuSessionClose.setName("menuSessionClose");
		menuSessionClose.addActionListener(actionAdapter);
		menuSessionOptions.setText(LangModel.Text("menuSessionOptions"));
		menuSessionOptions.setName("menuSessionOptions");
		menuSessionOptions.addActionListener(actionAdapter);
		menuSessionConnection.setText(LangModel.Text("menuSessionConnection"));
		menuSessionConnection.setName("menuSessionConnection");
		menuSessionConnection.addActionListener(actionAdapter);
		menuSessionChangePassword.setText(LangModel.Text("menuSessionChangePassword"));
		menuSessionChangePassword.setName("menuSessionChangePassword");
		menuSessionChangePassword.addActionListener(actionAdapter);
		menuSessionDomain.setText(LangModel.Text("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
		menuSessionDomain.addActionListener(actionAdapter);
		menuSessionSave.setText(LangModel.Text("menuSessionSave"));
		menuSessionSave.setName("menuSessionSave");
		menuSessionSave.addActionListener(actionAdapter);
		menuSessionUndo.setText(LangModel.Text("menuSessionUndo"));
		menuSessionUndo.setName("menuSessionUndo");
		menuSessionUndo.addActionListener(actionAdapter);
		menuExit.setText(LangModel.Text("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
		menuSession.addSeparator();
		menuSession.add(menuSessionSave);
		menuSession.add(menuSessionUndo);
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuScheme.setText(LangModelSchematics.Text("menuScheme"));
		menuScheme.setName("menuScheme");
		menuSchemeNew.setText(LangModelSchematics.Text("menuSchemeNew"));
		menuSchemeNew.setName("menuSchemeNew");
		menuSchemeNew.addActionListener(actionAdapter);
		menuSchemeSave.setText(LangModelSchematics.Text("menuSchemeSave"));
		menuSchemeSave.setName("menuSchemeSave");
		menuSchemeSave.addActionListener(actionAdapter);
		menuSchemeSaveAs.setText(LangModelSchematics.Text("menuSchemeSaveAs"));
		menuSchemeSaveAs.setName("menuSchemeSaveAs");
		menuSchemeSaveAs.addActionListener(actionAdapter);
		menuSchemeLoad.setText(LangModelSchematics.Text("menuSchemeLoad"));
		menuSchemeLoad.setName("menuSchemeLoad");
		menuSchemeLoad.addActionListener(actionAdapter);
		menuInsertToCatalog.setText(LangModelSchematics.Text("menuInsertToCatalog"));
		menuInsertToCatalog.setName("menuInsertToCatalog");
		menuInsertToCatalog.addActionListener(actionAdapter);
		menuSchemeImport.setText(LangModelSchematics.Text("menuSchemeImport"));
		menuSchemeImport.setName("menuSchemeImport");
		menuSchemeImport.addActionListener(actionAdapter);
		menuSchemeExport.setText(LangModelSchematics.Text("menuSchemeExport"));
		menuSchemeExport.setName("menuSchemeExport");
		menuSchemeExport.addActionListener(actionAdapter);

		menuScheme.add(menuSchemeNew);
		menuScheme.add(menuSchemeLoad);
		menuScheme.addSeparator();
		menuScheme.add(menuSchemeSave);
		menuScheme.add(menuSchemeSaveAs);
		menuScheme.addSeparator();
		menuScheme.add(menuInsertToCatalog);
		menuScheme.add(menuSchemeExport);
		menuScheme.add(menuSchemeImport);

		menuPath.setName("menuPath");
		menuPath.setText(LangModelSchematics.Text("menuPath"));
		menuPathNew.setName("menuPathNew");
		menuPathNew.setText(LangModelSchematics.Text("menuPathNew"));
		menuPathNew.addActionListener(actionAdapter);
		menuPathAddStart.setName("menuPathAddStart");
		menuPathAddStart.setText(LangModelSchematics.Text("menuPathAddStart"));
		menuPathAddStart.addActionListener(actionAdapter);
		menuPathAddEnd.setName("menuPathAddEnd");
		menuPathAddEnd.setText(LangModelSchematics.Text("menuPathAddEnd"));
		menuPathAddEnd.addActionListener(actionAdapter);
		menuPathAddLink.setName("menuPathAddLink");
		menuPathAddLink.setText(LangModelSchematics.Text("menuPathAddLink"));
		menuPathAddLink.addActionListener(actionAdapter);
		menuPathRemoveLink.setName("menuPathRemoveLink");
		menuPathRemoveLink.setText(LangModelSchematics.Text("menuPathRemoveLink"));
		menuPathRemoveLink.addActionListener(actionAdapter);
		menuPathUpdateLink.setName("menuPathUpdateLink");
		menuPathUpdateLink.setText(LangModelSchematics.Text("menuPathUpdateLink"));
		menuPathUpdateLink.addActionListener(actionAdapter);
		menuPathSave.setName("menuPathSave");
		menuPathSave.setText(LangModelSchematics.Text("menuPathSave"));
		menuPathSave.addActionListener(actionAdapter);
		menuPathCancel.setName("menuPathCancel");
		menuPathCancel.setText(LangModelSchematics.Text("menuPathCancel"));
		menuPathCancel.addActionListener(actionAdapter);
		menuPathDelete.setName("menuPathDelete");
		menuPathDelete.setText(LangModelSchematics.Text("menuPathDelete"));
		menuPathDelete.addActionListener(actionAdapter);
		menuPathEdit.setName("menuPathEdit");
		menuPathEdit.setText(LangModelSchematics.Text("menuPathEdit"));
		menuPathEdit.addActionListener(actionAdapter);

		menuPath.add(menuPathNew);
		menuPath.add(menuPathEdit);
		menuPath.add(menuPathSave);
		menuPath.add(menuPathDelete);
		menuPath.add(menuPathCancel);
		menuPath.addSeparator();
		menuPath.add(menuPathAddStart);
		menuPath.add(menuPathAddEnd);
		menuPath.addSeparator();
		menuPath.add(menuPathAddLink);
		menuPath.add(menuPathRemoveLink);
		menuPath.add(menuPathUpdateLink);

		menuWindow.setText(LangModelSchematics.Text("menuWindow"));
		menuWindow.setName("menuWindow");
		menuWindowArrange.setText(LangModelSchematics.Text("menuWindowArrange"));
		menuWindowArrange.setName("menuWindowArrange");
		menuWindowArrange.addActionListener(actionAdapter);
		menuWindowTree.setText(LangModelSchematics.Text("menuWindowTree"));
		menuWindowTree.setName("menuWindowTree");
		menuWindowTree.addActionListener(actionAdapter);
		menuWindowScheme.setText(LangModelSchematics.Text("menuWindowScheme"));
		menuWindowScheme.setName("menuWindowScheme");
		menuWindowScheme.addActionListener(actionAdapter);
		menuWindowCatalog.setText(LangModelSchematics.Text("menuWindowCatalog"));
		menuWindowCatalog.setName("menuWindowCatalog");
		menuWindowCatalog.addActionListener(actionAdapter);
		menuWindowUgo.setText(LangModelSchematics.Text("menuWindowUgo"));
		menuWindowUgo.setName("menuWindowUgo");
		menuWindowUgo.addActionListener(actionAdapter);
		menuWindowProps.setText(LangModelSchematics.Text("menuWindowProps"));
		menuWindowProps.setName("menuWindowProps");
		menuWindowProps.addActionListener(actionAdapter);
		menuWindowList.setText(LangModelSchematics.Text("menuWindowList"));
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

		add(menuSession);
		add(menuScheme);
		add(menuPath);
		add(menuWindow);
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
		menuSessionSave.setVisible(aModel.isVisible("menuSessionSave"));
		menuSessionSave.setEnabled(aModel.isEnabled("menuSessionSave"));
		menuSessionUndo.setVisible(aModel.isVisible("menuSessionUndo"));
		menuSessionUndo.setEnabled(aModel.isEnabled("menuSessionUndo"));

		menuScheme.setVisible(aModel.isVisible("menuScheme"));
		menuScheme.setEnabled(aModel.isEnabled("menuScheme"));
		menuSchemeNew.setVisible(aModel.isVisible("menuSchemeNew"));
		menuSchemeNew.setEnabled(aModel.isEnabled("menuSchemeNew"));
		menuSchemeSave.setVisible(aModel.isVisible("menuSchemeSave"));
		menuSchemeSave.setEnabled(aModel.isEnabled("menuSchemeSave"));
		menuSchemeSaveAs.setVisible(aModel.isVisible("menuSchemeSaveAs"));
		menuSchemeSaveAs.setEnabled(aModel.isEnabled("menuSchemeSaveAs"));
		menuSchemeLoad.setVisible(aModel.isVisible("menuSchemeLoad"));
		menuSchemeLoad.setEnabled(aModel.isEnabled("menuSchemeLoad"));
		menuInsertToCatalog.setVisible(aModel.isVisible("menuInsertToCatalog"));
		menuInsertToCatalog.setEnabled(aModel.isEnabled("menuInsertToCatalog"));
		menuSchemeImport.setVisible(aModel.isVisible("menuSchemeImport"));
		menuSchemeImport.setEnabled(aModel.isEnabled("menuSchemeImport"));
		menuSchemeExport.setVisible(aModel.isVisible("menuSchemeExport"));
		menuSchemeExport.setEnabled(aModel.isEnabled("menuSchemeExport"));

		menuPath.setVisible(aModel.isVisible("menuPath"));
		menuPath.setEnabled(aModel.isEnabled("menuPath"));
		menuPathNew.setVisible(aModel.isVisible("menuPathNew"));
		menuPathNew.setEnabled(aModel.isEnabled("menuPathNew"));
		menuPathAddStart.setVisible(aModel.isVisible("menuPathAddStart"));
		menuPathAddStart.setEnabled(aModel.isEnabled("menuPathAddStart"));
		menuPathAddEnd.setVisible(aModel.isVisible("menuPathAddEnd"));
		menuPathAddEnd.setEnabled(aModel.isEnabled("menuPathAddEnd"));
		menuPathAddLink.setVisible(aModel.isVisible("menuPathAddLink"));
		menuPathAddLink.setEnabled(aModel.isEnabled("menuPathAddLink"));
		menuPathRemoveLink.setVisible(aModel.isVisible("menuPathRemoveLink"));
		menuPathRemoveLink.setEnabled(aModel.isEnabled("menuPathRemoveLink"));
		menuPathUpdateLink.setVisible(aModel.isVisible("menuPathUpdateLink"));
		menuPathUpdateLink.setEnabled(aModel.isEnabled("menuPathUpdateLink"));
		menuPathSave.setVisible(aModel.isVisible("menuPathSave"));
		menuPathSave.setEnabled(aModel.isEnabled("menuPathSave"));
		menuPathCancel.setVisible(aModel.isVisible("menuPathCancel"));
		menuPathCancel.setEnabled(aModel.isEnabled("menuPathCancel"));
		menuPathDelete.setVisible(aModel.isVisible("menuPathDelete"));
		menuPathDelete.setEnabled(aModel.isEnabled("menuPathDelete"));
		menuPathEdit.setVisible(aModel.isVisible("menuPathEdit"));
		menuPathEdit.setEnabled(aModel.isEnabled("menuPathEdit"));

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

class SchemeEditorMenuBar_this_actionAdapter implements java.awt.event.ActionListener
{
	SchemeEditorMenuBar adaptee;

	SchemeEditorMenuBar_this_actionAdapter(SchemeEditorMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}
