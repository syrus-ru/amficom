package com.syrus.AMFICOM.Client.Model;

import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ModelMenuBar extends JMenuBar
		implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenuItem menuSessionOpen = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionOptions = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	JMenu menuView = new JMenu();
	JMenuItem menuViewMapViewOpen = new JMenuItem();
	JMenuItem menuViewMapViewEdit = new JMenuItem();
	JMenuItem menuViewMapViewClose = new JMenuItem();
	JMenuItem menuViewSchemeOpen = new JMenuItem();
	JMenuItem menuViewSchemeEdit = new JMenuItem();
	JMenuItem menuViewSchemeClose = new JMenuItem();

	JMenu menuFile = new JMenu();
	JMenuItem menuFileOpen = new JMenuItem();
	JMenuItem menuFileOpenAs = new JMenu();
	JMenuItem menuFileOpenAsBellcore = new JMenuItem();
	JMenuItem menuFileOpenAsWavetek = new JMenuItem();
	JMenuItem menuFileSave = new JMenuItem();
	JMenuItem menuFileSaveAs = new JMenu();
	JMenuItem menuFileSaveAsText = new JMenuItem();
	JMenuItem menuFileClose = new JMenuItem();
	JMenuItem menuFileAddCompare = new JMenuItem();
	JMenuItem menuFileRemoveCompare = new JMenuItem();

	JMenu menuTrace = new JMenu();
	JMenuItem menuViewModelLoad = new JMenuItem();
	JMenuItem menuTraceAddCompare = new JMenuItem();
	JMenuItem menuTraceRemoveCompare = new JMenuItem();
	JMenuItem menuTraceClose = new JMenuItem();
	JMenuItem menuViewModelSave = new JMenuItem();

	JMenu menuReport = new JMenu();
	JMenuItem menuReportCreate = new JMenuItem();

	JMenu menuHelp = new JMenu();
	JMenuItem menuHelpContents = new JMenuItem();
	JMenuItem menuHelpFind = new JMenuItem();
	JMenuItem menuHelpTips = new JMenuItem();
	JMenuItem menuHelpStart = new JMenuItem();
	JMenuItem menuHelpCourse = new JMenuItem();
	JMenuItem menuHelpHelp = new JMenuItem();
	JMenuItem menuHelpSupport = new JMenuItem();
	JMenuItem menuHelpLicense = new JMenuItem();
	JMenuItem menuHelpAbout = new JMenuItem();

	JMenu menuWindow = new JMenu();
	JMenuItem menuWindowTraceSelector = new JMenuItem();
	JMenuItem menuWindowPrimaryParameters = new JMenuItem();
	JMenuItem menuWindowOverallStats = new JMenuItem();
	JMenuItem menuWindowNoiseFrame = new JMenuItem();
	JMenuItem menuWindowFilteredFrame = new JMenuItem();
	JMenuItem menuWindowEvents = new JMenuItem();
	JMenuItem menuWindowDetailedEvents = new JMenuItem();
	JMenuItem menuWindowAnalysis = new JMenuItem();
	JMenuItem menuWindowMarkersInfo = new JMenuItem();
	JMenuItem menuWindowAnalysisSelection = new JMenuItem();
	JMenuItem menuWindowDerivHistoFrame = new JMenuItem();
	JMenuItem menuWindowThresholdsSelection = new JMenuItem();
	JMenuItem menuWindowThresholds = new JMenuItem();

	public ModelMenuBar()
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

	public ModelMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		ModelMenuBar_this_actionAdapter actionAdapter =
				new ModelMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModel.getString("menuSession"));
		menuSession.setName("menuSession");
		menuSessionOpen.setText(LangModelModel.getString("menuSessionOpen"));
		menuSessionOpen.setName("menuSessionOpen");
		menuSessionOpen.addActionListener(actionAdapter);
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

		menuView.setText(LangModelModel.getString("menuView"));
		menuView.setName("menuView");
		menuViewMapViewOpen.setText(LangModelModel.getString("menuViewMapViewOpen"));
		menuViewMapViewOpen.setName("menuViewMapViewOpen");
		menuViewMapViewOpen.addActionListener(actionAdapter);
		menuViewMapViewEdit.setText(LangModelModel.getString("menuViewMapViewEdit"));
		menuViewMapViewEdit.setName("menuViewMapViewEdit");
		menuViewMapViewEdit.addActionListener(actionAdapter);
		menuViewMapViewClose.setText(LangModelModel.getString("menuViewMapViewClose"));
		menuViewMapViewClose.setName("menuViewMapViewClose");
		menuViewMapViewClose.addActionListener(actionAdapter);
		menuViewModelSave.setText(LangModelModel.getString("menuViewModelSave"));
		menuViewModelSave.setName("menuViewModelSave");
		menuViewModelSave.addActionListener(actionAdapter);
		menuViewModelLoad.setText(LangModelModel.getString("menuViewModelLoad"));
		menuViewModelLoad.setName("menuViewModelLoad");
		menuViewModelLoad.addActionListener(actionAdapter);

		menuViewSchemeOpen.setText(LangModelModel.getString("menuViewSchemeOpen"));
		menuViewSchemeOpen.setName("menuViewSchemeOpen");
		menuViewSchemeOpen.addActionListener(actionAdapter);
		menuViewSchemeEdit.setText(LangModelModel.getString("menuViewSchemeEdit"));
		menuViewSchemeEdit.setName("menuViewSchemeEdit");
		menuViewSchemeEdit.addActionListener(actionAdapter);
		menuViewSchemeClose.setText(LangModelModel.getString("menuViewSchemeClose"));
		menuViewSchemeClose.setName("menuViewSchemeClose");
		menuViewSchemeClose.addActionListener(actionAdapter);

		menuFile.setText(LangModelAnalyse.getString("menuFile"));
		menuFile.setName("menuFile");
		menuFileOpen.setText(LangModelAnalyse.getString("menuFileOpen"));
		menuFileOpen.setName("menuFileOpen");
		menuFileOpen.addActionListener(actionAdapter);
		menuFileOpenAs.setText(LangModelAnalyse.getString("menuFileOpenAs"));
		menuFileOpenAs.setName("menuFileOpenAs");
		menuFileOpenAs.addActionListener(actionAdapter);
		menuFileOpenAsBellcore.setText(LangModelAnalyse.getString("menuFileOpenAsBellcore"));
		menuFileOpenAsBellcore.setName("menuFileOpenAsBellcore");
		menuFileOpenAsBellcore.addActionListener(actionAdapter);
		menuFileOpenAsWavetek.setText(LangModelAnalyse.getString("menuFileOpenAsWavetek"));
		menuFileOpenAsWavetek.setName("menuFileOpenAsWavetek");
		menuFileOpenAsWavetek.addActionListener(actionAdapter);
		menuFileSave.setText(LangModelAnalyse.getString("menuFileSave"));
		menuFileSave.setName("menuFileSave");
		menuFileSave.addActionListener(actionAdapter);
		menuFileSaveAs.setText(LangModelAnalyse.getString("menuFileSaveAs"));
		menuFileSaveAs.setName("menuFileSaveAs");
		menuFileSaveAsText.setText(LangModelAnalyse.getString("menuFileSaveAsText"));
		menuFileSaveAsText.setName("menuFileSaveAsText");
		menuFileSaveAsText.addActionListener(actionAdapter);
		menuFileClose.setText(LangModelAnalyse.getString("menuFileClose"));
		menuFileClose.setName("menuFileClose");
		menuFileClose.addActionListener(actionAdapter);
		menuFileAddCompare.setText(LangModelAnalyse.getString("menuFileAddCompare"));
		menuFileAddCompare.setName("menuFileAddCompare");
		menuFileAddCompare.addActionListener(actionAdapter);
		menuFileRemoveCompare.setText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		menuFileRemoveCompare.setName("menuFileRemoveCompare");
		menuFileRemoveCompare.addActionListener(actionAdapter);
		menuTrace.setText(LangModelAnalyse.getString("menuTrace"));
		menuTrace.setName("menuTrace");
		menuTraceAddCompare.setText(LangModelAnalyse.getString("menuTraceAddCompare"));
		menuTraceAddCompare.setName("menuTraceAddCompare");
		menuTraceAddCompare.addActionListener(actionAdapter);
		menuTraceRemoveCompare.setText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		menuTraceRemoveCompare.setName("menuTraceRemoveCompare");
		menuTraceRemoveCompare.addActionListener(actionAdapter);
		menuTraceClose.setText(LangModelAnalyse.getString("menuFileClose"));
		menuTraceClose.setName("menuFileClose");
		menuTraceClose.addActionListener(actionAdapter);

		menuReport.setText(LangModelAnalyse.getString("menuReport"));
		menuReport.setName("menuReport");
		menuReportCreate.setText(LangModelAnalyse.getString("menuReportCreate"));
		menuReportCreate.setName("menuReportCreate");
		menuReportCreate.addActionListener(actionAdapter);
		menuReport.add(menuReportCreate);

		menuHelp.setText(LangModelModel.getString("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModelModel.getString("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModelModel.getString("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModelModel.getString("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpStart.setText(LangModelModel.getString("menuHelpStart"));
		menuHelpStart.setName("menuHelpStart");
		menuHelpStart.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModelModel.getString("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModelModel.getString("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpSupport.setText(LangModelModel.getString("menuHelpSupport"));
		menuHelpSupport.setName("menuHelpSupport");
		menuHelpSupport.addActionListener(actionAdapter);
		menuHelpLicense.setText(LangModelModel.getString("menuHelpLicense"));
		menuHelpLicense.setName("menuHelpLicense");
		menuHelpLicense.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModelModel.getString("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);

		menuSession.add(menuSessionOpen);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
//		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuView.add(menuViewMapViewOpen);
		menuView.add(menuViewMapViewEdit);
		menuView.add(menuViewMapViewClose);
		menuView.addSeparator();
		menuView.add(menuViewSchemeOpen);
		menuView.add(menuViewSchemeEdit);
		menuView.add(menuViewSchemeClose);

		menuFile.add(menuFileOpen);
		menuFile.add(menuFileOpenAs);
		menuFileOpenAs.add(menuFileOpenAsBellcore);
		menuFileOpenAs.add(menuFileOpenAsWavetek);
		menuFile.add(menuFileAddCompare);
		menuFile.addSeparator();
		menuFile.add(menuFileClose);
		menuFile.add(menuFileRemoveCompare);
		menuFile.addSeparator();
		menuFile.add(menuFileSave);
		menuFile.add(menuFileSaveAs);
		menuFileSaveAs.add(menuFileSaveAsText);

		menuTrace.add(menuViewModelLoad);
		menuTrace.add(menuTraceAddCompare);
		menuTrace.addSeparator();
		menuTrace.add(menuTraceClose);
		menuTrace.add(menuTraceRemoveCompare);
		menuTrace.addSeparator();
		menuTrace.add(menuViewModelSave);

		menuHelp.add(menuHelpContents);
		menuHelp.add(menuHelpFind);
		menuHelp.add(menuHelpTips);
		menuHelp.add(menuHelpStart);
		menuHelp.add(menuHelpCourse);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpHelp);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpSupport);
		menuHelp.add(menuHelpLicense);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpAbout);

		this.add(menuSession);
		this.add(menuView);
		this.add(menuFile);
		this.add(menuTrace);
		this.add(menuReport);
		this.add(menuHelp);
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
		int count = e.length;
		int i;

		menuSession.setVisible(aModel.isVisible("menuSession"));
		menuSession.setEnabled(aModel.isEnabled("menuSession"));
		menuSessionOpen.setVisible(aModel.isVisible("menuSessionOpen"));
		menuSessionOpen.setEnabled(aModel.isEnabled("menuSessionOpen"));
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
		menuExit.setVisible(aModel.isVisible("menuExit"));
		menuExit.setEnabled(aModel.isEnabled("menuExit"));

		menuView.setVisible(aModel.isVisible("menuView"));
		menuView.setEnabled(aModel.isEnabled("menuView"));
		menuViewMapViewOpen.setVisible(aModel.isVisible("menuViewMapViewOpen"));
		menuViewMapViewOpen.setEnabled(aModel.isEnabled("menuViewMapViewOpen"));
		menuViewMapViewEdit.setVisible(aModel.isVisible("menuViewMapViewEdit"));
		menuViewMapViewEdit.setEnabled(aModel.isEnabled("menuViewMapViewEdit"));
		menuViewMapViewClose.setVisible(aModel.isVisible("menuViewMapViewClose"));
		menuViewMapViewClose.setEnabled(aModel.isEnabled("menuViewMapViewClose"));
		menuViewSchemeOpen.setVisible(aModel.isVisible("menuViewSchemeOpen"));
		menuViewSchemeOpen.setEnabled(aModel.isEnabled("menuViewSchemeOpen"));
		menuViewSchemeEdit.setVisible(aModel.isVisible("menuViewSchemeEdit"));
		menuViewSchemeEdit.setEnabled(aModel.isEnabled("menuViewSchemeEdit"));
		menuViewSchemeClose.setVisible(aModel.isVisible("menuViewSchemeClose"));
		menuViewSchemeClose.setEnabled(aModel.isEnabled("menuViewSchemeClose"));

		menuViewModelSave.setVisible(aModel.isVisible("menuViewModelSave"));
		menuViewModelSave.setEnabled(aModel.isEnabled("menuViewModelSave"));
		menuViewModelLoad.setVisible(aModel.isVisible("menuViewModelLoad"));
		menuViewModelLoad.setEnabled(aModel.isEnabled("menuViewModelLoad"));
		menuFileOpen.setVisible(aModel.isVisible("menuFileOpen"));
		menuFileOpen.setEnabled(aModel.isEnabled("menuFileOpen"));
		menuFileOpenAs.setVisible(aModel.isVisible("menuFileOpenAs"));
		menuFileOpenAs.setEnabled(aModel.isEnabled("menuFileOpenAs"));
		menuFileOpenAsBellcore.setVisible(aModel.isVisible("menuFileOpenAsBellcore"));
		menuFileOpenAsBellcore.setEnabled(aModel.isEnabled("menuFileOpenAsBellcore"));
		menuFileOpenAsWavetek.setVisible(aModel.isVisible("menuFileOpenAsWavetek"));
		menuFileOpenAsWavetek.setEnabled(aModel.isEnabled("menuFileOpenAsWavetek"));
		menuFileSave.setVisible(aModel.isVisible("menuFileSave"));
		menuFileSave.setEnabled(aModel.isEnabled("menuFileSave"));
		menuFileSaveAs.setVisible(aModel.isVisible("menuFileSaveAs"));
		menuFileSaveAs.setEnabled(aModel.isEnabled("menuFileSaveAs"));
		menuFileSaveAsText.setVisible(aModel.isVisible("menuFileSaveAsText"));
		menuFileSaveAsText.setEnabled(aModel.isEnabled("menuFileSaveAsText"));
		menuFileClose.setVisible(aModel.isVisible("menuFileClose"));
		menuFileClose.setEnabled(aModel.isEnabled("menuFileClose"));
		menuFileAddCompare.setVisible(aModel.isVisible("menuFileAddCompare"));
		menuFileAddCompare.setEnabled(aModel.isEnabled("menuFileAddCompare"));
		menuFileRemoveCompare.setVisible(aModel.isVisible("menuFileRemoveCompare"));
		menuFileRemoveCompare.setEnabled(aModel.isEnabled("menuFileRemoveCompare"));

		menuTraceAddCompare.setVisible(aModel.isVisible("menuTraceAddCompare"));
		menuTraceAddCompare.setEnabled(aModel.isEnabled("menuTraceAddCompare"));
		menuTraceRemoveCompare.setVisible(aModel.isVisible("menuTraceRemoveCompare"));
		menuTraceRemoveCompare.setEnabled(aModel.isEnabled("menuTraceRemoveCompare"));
		menuTraceClose.setVisible(aModel.isVisible("menuTraceClose"));
		menuTraceClose.setEnabled(aModel.isEnabled("menuTraceClose"));

		menuReport.setVisible(aModel.isVisible("menuReport"));
		menuReport.setEnabled(aModel.isEnabled("menuReport"));
		menuReportCreate.setVisible(aModel.isVisible("menuReportCreate"));
		menuReportCreate.setEnabled(aModel.isEnabled("menuReportCreate"));

		menuHelp.setVisible(aModel.isVisible("menuHelp"));
		menuHelp.setEnabled(aModel.isEnabled("menuHelp"));
		menuHelpContents.setVisible(aModel.isVisible("menuHelpContents"));
		menuHelpContents.setEnabled(aModel.isEnabled("menuHelpContents"));
		menuHelpFind.setVisible(aModel.isVisible("menuHelpFind"));
		menuHelpFind.setEnabled(aModel.isEnabled("menuHelpFind"));
		menuHelpTips.setVisible(aModel.isVisible("menuHelpTips"));
		menuHelpTips.setEnabled(aModel.isEnabled("menuHelpTips"));
		menuHelpStart.setVisible(aModel.isVisible("menuHelpStart"));
		menuHelpStart.setEnabled(aModel.isEnabled("menuHelpStart"));
		menuHelpCourse.setVisible(aModel.isVisible("menuHelpCourse"));
		menuHelpCourse.setEnabled(aModel.isEnabled("menuHelpCourse"));
		menuHelpHelp.setVisible(aModel.isVisible("menuHelpHelp"));
		menuHelpHelp.setEnabled(aModel.isEnabled("menuHelpHelp"));
		menuHelpSupport.setVisible(aModel.isVisible("menuHelpSupport"));
		menuHelpSupport.setEnabled(aModel.isEnabled("menuHelpSupport"));
		menuHelpLicense.setVisible(aModel.isVisible("menuHelpLicense"));
		menuHelpLicense.setEnabled(aModel.isEnabled("menuHelpLicense"));
		menuHelpAbout.setVisible(aModel.isVisible("menuHelpAbout"));
		menuHelpAbout.setEnabled(aModel.isEnabled("menuHelpAbout"));
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

class ModelMenuBar_this_actionAdapter
		implements java.awt.event.ActionListener
{
	ModelMenuBar adaptee;

	ModelMenuBar_this_actionAdapter(ModelMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

