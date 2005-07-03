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

		this.menuSession.setText(LangModel.getString("menuSession"));
		this.menuSession.setName("menuSession");
		this.menuSessionOpen.setText(LangModelModel.getString("menuSessionOpen"));
		this.menuSessionOpen.setName("menuSessionOpen");
		this.menuSessionOpen.addActionListener(actionAdapter);
		this.menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		this.menuSessionClose.setName("menuSessionClose");
		this.menuSessionClose.addActionListener(actionAdapter);
		this.menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
		this.menuSessionOptions.setName("menuSessionOptions");
		this.menuSessionOptions.addActionListener(actionAdapter);
		this.menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		this.menuSessionConnection.setName("menuSessionConnection");
		this.menuSessionConnection.addActionListener(actionAdapter);
		this.menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		this.menuSessionChangePassword.setName("menuSessionChangePassword");
		this.menuSessionChangePassword.addActionListener(actionAdapter);
		this.menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		this.menuSessionDomain.setName("menuSessionDomain");
		this.menuSessionDomain.addActionListener(actionAdapter);
		this.menuExit.setText(LangModel.getString("menuExit"));
		this.menuExit.setName("menuExit");
		this.menuExit.addActionListener(actionAdapter);

		this.menuView.setText(LangModelModel.getString("menuView"));
		this.menuView.setName("menuView");
		this.menuViewMapViewOpen.setText(LangModelModel.getString("menuViewMapViewOpen"));
		this.menuViewMapViewOpen.setName("menuViewMapViewOpen");
		this.menuViewMapViewOpen.addActionListener(actionAdapter);
		this.menuViewMapViewEdit.setText(LangModelModel.getString("menuViewMapViewEdit"));
		this.menuViewMapViewEdit.setName("menuViewMapViewEdit");
		this.menuViewMapViewEdit.addActionListener(actionAdapter);
		this.menuViewMapViewClose.setText(LangModelModel.getString("menuViewMapViewClose"));
		this.menuViewMapViewClose.setName("menuViewMapViewClose");
		this.menuViewMapViewClose.addActionListener(actionAdapter);
		this.menuViewModelSave.setText(LangModelModel.getString("menuViewModelSave"));
		this.menuViewModelSave.setName("menuViewModelSave");
		this.menuViewModelSave.addActionListener(actionAdapter);
		this.menuViewModelLoad.setText(LangModelModel.getString("menuViewModelLoad"));
		this.menuViewModelLoad.setName("menuViewModelLoad");
		this.menuViewModelLoad.addActionListener(actionAdapter);

		this.menuViewSchemeOpen.setText(LangModelModel.getString("menuViewSchemeOpen"));
		this.menuViewSchemeOpen.setName("menuViewSchemeOpen");
		this.menuViewSchemeOpen.addActionListener(actionAdapter);
		this.menuViewSchemeEdit.setText(LangModelModel.getString("menuViewSchemeEdit"));
		this.menuViewSchemeEdit.setName("menuViewSchemeEdit");
		this.menuViewSchemeEdit.addActionListener(actionAdapter);
		this.menuViewSchemeClose.setText(LangModelModel.getString("menuViewSchemeClose"));
		this.menuViewSchemeClose.setName("menuViewSchemeClose");
		this.menuViewSchemeClose.addActionListener(actionAdapter);

		this.menuFile.setText(LangModelAnalyse.getString("menuFile"));
		this.menuFile.setName("menuFile");
		this.menuFileOpen.setText(LangModelAnalyse.getString("menuFileOpen"));
		this.menuFileOpen.setName("menuFileOpen");
		this.menuFileOpen.addActionListener(actionAdapter);
		this.menuFileOpenAs.setText(LangModelAnalyse.getString("menuFileOpenAs"));
		this.menuFileOpenAs.setName("menuFileOpenAs");
		this.menuFileOpenAs.addActionListener(actionAdapter);
		this.menuFileOpenAsBellcore.setText(LangModelAnalyse.getString("menuFileOpenAsBellcore"));
		this.menuFileOpenAsBellcore.setName("menuFileOpenAsBellcore");
		this.menuFileOpenAsBellcore.addActionListener(actionAdapter);
		this.menuFileOpenAsWavetek.setText(LangModelAnalyse.getString("menuFileOpenAsWavetek"));
		this.menuFileOpenAsWavetek.setName("menuFileOpenAsWavetek");
		this.menuFileOpenAsWavetek.addActionListener(actionAdapter);
		this.menuFileSave.setText(LangModelAnalyse.getString("menuFileSave"));
		this.menuFileSave.setName("menuFileSave");
		this.menuFileSave.addActionListener(actionAdapter);
		this.menuFileSaveAs.setText(LangModelAnalyse.getString("menuFileSaveAs"));
		this.menuFileSaveAs.setName("menuFileSaveAs");
		this.menuFileSaveAsText.setText(LangModelAnalyse.getString("menuFileSaveAsText"));
		this.menuFileSaveAsText.setName("menuFileSaveAsText");
		this.menuFileSaveAsText.addActionListener(actionAdapter);
		this.menuFileClose.setText(LangModelAnalyse.getString("menuFileClose"));
		this.menuFileClose.setName("menuFileClose");
		this.menuFileClose.addActionListener(actionAdapter);
		this.menuFileAddCompare.setText(LangModelAnalyse.getString("menuFileAddCompare"));
		this.menuFileAddCompare.setName("menuFileAddCompare");
		this.menuFileAddCompare.addActionListener(actionAdapter);
		this.menuFileRemoveCompare.setText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		this.menuFileRemoveCompare.setName("menuFileRemoveCompare");
		this.menuFileRemoveCompare.addActionListener(actionAdapter);
		this.menuTrace.setText(LangModelAnalyse.getString("menuTrace"));
		this.menuTrace.setName("menuTrace");
		this.menuTraceAddCompare.setText(LangModelAnalyse.getString("menuTraceAddCompare"));
		this.menuTraceAddCompare.setName("menuTraceAddCompare");
		this.menuTraceAddCompare.addActionListener(actionAdapter);
		this.menuTraceRemoveCompare.setText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		this.menuTraceRemoveCompare.setName("menuTraceRemoveCompare");
		this.menuTraceRemoveCompare.addActionListener(actionAdapter);
		this.menuTraceClose.setText(LangModelAnalyse.getString("menuFileClose"));
		this.menuTraceClose.setName("menuFileClose");
		this.menuTraceClose.addActionListener(actionAdapter);

		this.menuReport.setText(LangModelAnalyse.getString("menuReport"));
		this.menuReport.setName("menuReport");
		this.menuReportCreate.setText(LangModelAnalyse.getString("menuReportCreate"));
		this.menuReportCreate.setName("menuReportCreate");
		this.menuReportCreate.addActionListener(actionAdapter);
		this.menuReport.add(this.menuReportCreate);

		this.menuHelp.setText(LangModelModel.getString("menuHelp"));
		this.menuHelp.setName("menuHelp");
		this.menuHelpContents.setText(LangModelModel.getString("menuHelpContents"));
		this.menuHelpContents.setName("menuHelpContents");
		this.menuHelpContents.addActionListener(actionAdapter);
		this.menuHelpFind.setText(LangModelModel.getString("menuHelpFind"));
		this.menuHelpFind.setName("menuHelpFind");
		this.menuHelpFind.addActionListener(actionAdapter);
		this.menuHelpTips.setText(LangModelModel.getString("menuHelpTips"));
		this.menuHelpTips.setName("menuHelpTips");
		this.menuHelpTips.addActionListener(actionAdapter);
		this.menuHelpStart.setText(LangModelModel.getString("menuHelpStart"));
		this.menuHelpStart.setName("menuHelpStart");
		this.menuHelpStart.addActionListener(actionAdapter);
		this.menuHelpCourse.setText(LangModelModel.getString("menuHelpCourse"));
		this.menuHelpCourse.setName("menuHelpCourse");
		this.menuHelpCourse.addActionListener(actionAdapter);
		this.menuHelpHelp.setText(LangModelModel.getString("menuHelpHelp"));
		this.menuHelpHelp.setName("menuHelpHelp");
		this.menuHelpHelp.addActionListener(actionAdapter);
		this.menuHelpSupport.setText(LangModelModel.getString("menuHelpSupport"));
		this.menuHelpSupport.setName("menuHelpSupport");
		this.menuHelpSupport.addActionListener(actionAdapter);
		this.menuHelpLicense.setText(LangModelModel.getString("menuHelpLicense"));
		this.menuHelpLicense.setName("menuHelpLicense");
		this.menuHelpLicense.addActionListener(actionAdapter);
		this.menuHelpAbout.setText(LangModelModel.getString("menuHelpAbout"));
		this.menuHelpAbout.setName("menuHelpAbout");
		this.menuHelpAbout.addActionListener(actionAdapter);

		this.menuSession.add(this.menuSessionOpen);
		this.menuSession.add(this.menuSessionClose);
		this.menuSession.add(this.menuSessionOptions);
		this.menuSession.add(this.menuSessionChangePassword);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuSessionConnection);
//		this.menuSession.addSeparator();
		this.menuSession.add(this.menuSessionDomain);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuExit);

		this.menuView.add(this.menuViewMapViewOpen);
		this.menuView.add(this.menuViewMapViewEdit);
		this.menuView.add(this.menuViewMapViewClose);
		this.menuView.addSeparator();
		this.menuView.add(this.menuViewSchemeOpen);
		this.menuView.add(this.menuViewSchemeEdit);
		this.menuView.add(this.menuViewSchemeClose);

		this.menuFile.add(this.menuFileOpen);
		this.menuFile.add(this.menuFileOpenAs);
		this.menuFileOpenAs.add(this.menuFileOpenAsBellcore);
		this.menuFileOpenAs.add(this.menuFileOpenAsWavetek);
		this.menuFile.add(this.menuFileAddCompare);
		this.menuFile.addSeparator();
		this.menuFile.add(this.menuFileClose);
		this.menuFile.add(this.menuFileRemoveCompare);
		this.menuFile.addSeparator();
		this.menuFile.add(this.menuFileSave);
		this.menuFile.add(this.menuFileSaveAs);
		this.menuFileSaveAs.add(this.menuFileSaveAsText);

		this.menuTrace.add(this.menuViewModelLoad);
		this.menuTrace.add(this.menuTraceAddCompare);
		this.menuTrace.addSeparator();
		this.menuTrace.add(this.menuTraceClose);
		this.menuTrace.add(this.menuTraceRemoveCompare);
		this.menuTrace.addSeparator();
		this.menuTrace.add(this.menuViewModelSave);

		this.menuHelp.add(this.menuHelpContents);
		this.menuHelp.add(this.menuHelpFind);
		this.menuHelp.add(this.menuHelpTips);
		this.menuHelp.add(this.menuHelpStart);
		this.menuHelp.add(this.menuHelpCourse);
		this.menuHelp.addSeparator();
		this.menuHelp.add(this.menuHelpHelp);
		this.menuHelp.addSeparator();
		this.menuHelp.add(this.menuHelpSupport);
		this.menuHelp.add(this.menuHelpLicense);
		this.menuHelp.addSeparator();
		this.menuHelp.add(this.menuHelpAbout);

		this.add(this.menuSession);
		this.add(this.menuView);
		this.add(this.menuFile);
		this.add(this.menuTrace);
		this.add(this.menuReport);
		this.add(this.menuHelp);
	}

	public void setModel(ApplicationModel a)
	{
		this.aModel = a;
	}

	public ApplicationModel getModel()
	{
		return this.aModel;
	}

	public void modelChanged(String e[])
	{
		this.menuSession.setVisible(this.aModel.isVisible("menuSession"));
		this.menuSession.setEnabled(this.aModel.isEnabled("menuSession"));
		this.menuSessionOpen.setVisible(this.aModel.isVisible("menuSessionOpen"));
		this.menuSessionOpen.setEnabled(this.aModel.isEnabled("menuSessionOpen"));
		this.menuSessionClose.setVisible(this.aModel.isVisible("menuSessionClose"));
		this.menuSessionClose.setEnabled(this.aModel.isEnabled("menuSessionClose"));
		this.menuSessionOptions.setVisible(this.aModel.isVisible("menuSessionOptions"));
		this.menuSessionOptions.setEnabled(this.aModel.isEnabled("menuSessionOptions"));
		this.menuSessionConnection.setVisible(this.aModel.isVisible("menuSessionConnection"));
		this.menuSessionConnection.setEnabled(this.aModel.isEnabled("menuSessionConnection"));
		this.menuSessionChangePassword.setVisible(this.aModel.isVisible("menuSessionChangePassword"));
		this.menuSessionChangePassword.setEnabled(this.aModel.isEnabled("menuSessionChangePassword"));
		this.menuSessionDomain.setVisible(this.aModel.isVisible("menuSessionDomain"));
		this.menuSessionDomain.setEnabled(this.aModel.isEnabled("menuSessionDomain"));
		this.menuExit.setVisible(this.aModel.isVisible("menuExit"));
		this.menuExit.setEnabled(this.aModel.isEnabled("menuExit"));

		this.menuView.setVisible(this.aModel.isVisible("menuView"));
		this.menuView.setEnabled(this.aModel.isEnabled("menuView"));
		this.menuViewMapViewOpen.setVisible(this.aModel.isVisible("menuViewMapViewOpen"));
		this.menuViewMapViewOpen.setEnabled(this.aModel.isEnabled("menuViewMapViewOpen"));
		this.menuViewMapViewEdit.setVisible(this.aModel.isVisible("menuViewMapViewEdit"));
		this.menuViewMapViewEdit.setEnabled(this.aModel.isEnabled("menuViewMapViewEdit"));
		this.menuViewMapViewClose.setVisible(this.aModel.isVisible("menuViewMapViewClose"));
		this.menuViewMapViewClose.setEnabled(this.aModel.isEnabled("menuViewMapViewClose"));
		this.menuViewSchemeOpen.setVisible(this.aModel.isVisible("menuViewSchemeOpen"));
		this.menuViewSchemeOpen.setEnabled(this.aModel.isEnabled("menuViewSchemeOpen"));
		this.menuViewSchemeEdit.setVisible(this.aModel.isVisible("menuViewSchemeEdit"));
		this.menuViewSchemeEdit.setEnabled(this.aModel.isEnabled("menuViewSchemeEdit"));
		this.menuViewSchemeClose.setVisible(this.aModel.isVisible("menuViewSchemeClose"));
		this.menuViewSchemeClose.setEnabled(this.aModel.isEnabled("menuViewSchemeClose"));

		this.menuViewModelSave.setVisible(this.aModel.isVisible("menuViewModelSave"));
		this.menuViewModelSave.setEnabled(this.aModel.isEnabled("menuViewModelSave"));
		this.menuViewModelLoad.setVisible(this.aModel.isVisible("menuViewModelLoad"));
		this.menuViewModelLoad.setEnabled(this.aModel.isEnabled("menuViewModelLoad"));
		this.menuFileOpen.setVisible(this.aModel.isVisible("menuFileOpen"));
		this.menuFileOpen.setEnabled(this.aModel.isEnabled("menuFileOpen"));
		this.menuFileOpenAs.setVisible(this.aModel.isVisible("menuFileOpenAs"));
		this.menuFileOpenAs.setEnabled(this.aModel.isEnabled("menuFileOpenAs"));
		this.menuFileOpenAsBellcore.setVisible(this.aModel.isVisible("menuFileOpenAsBellcore"));
		this.menuFileOpenAsBellcore.setEnabled(this.aModel.isEnabled("menuFileOpenAsBellcore"));
		this.menuFileOpenAsWavetek.setVisible(this.aModel.isVisible("menuFileOpenAsWavetek"));
		this.menuFileOpenAsWavetek.setEnabled(this.aModel.isEnabled("menuFileOpenAsWavetek"));
		this.menuFileSave.setVisible(this.aModel.isVisible("menuFileSave"));
		this.menuFileSave.setEnabled(this.aModel.isEnabled("menuFileSave"));
		this.menuFileSaveAs.setVisible(this.aModel.isVisible("menuFileSaveAs"));
		this.menuFileSaveAs.setEnabled(this.aModel.isEnabled("menuFileSaveAs"));
		this.menuFileSaveAsText.setVisible(this.aModel.isVisible("menuFileSaveAsText"));
		this.menuFileSaveAsText.setEnabled(this.aModel.isEnabled("menuFileSaveAsText"));
		this.menuFileClose.setVisible(this.aModel.isVisible("menuFileClose"));
		this.menuFileClose.setEnabled(this.aModel.isEnabled("menuFileClose"));
		this.menuFileAddCompare.setVisible(this.aModel.isVisible("menuFileAddCompare"));
		this.menuFileAddCompare.setEnabled(this.aModel.isEnabled("menuFileAddCompare"));
		this.menuFileRemoveCompare.setVisible(this.aModel.isVisible("menuFileRemoveCompare"));
		this.menuFileRemoveCompare.setEnabled(this.aModel.isEnabled("menuFileRemoveCompare"));

		this.menuTraceAddCompare.setVisible(this.aModel.isVisible("menuTraceAddCompare"));
		this.menuTraceAddCompare.setEnabled(this.aModel.isEnabled("menuTraceAddCompare"));
		this.menuTraceRemoveCompare.setVisible(this.aModel.isVisible("menuTraceRemoveCompare"));
		this.menuTraceRemoveCompare.setEnabled(this.aModel.isEnabled("menuTraceRemoveCompare"));
		this.menuTraceClose.setVisible(this.aModel.isVisible("menuTraceClose"));
		this.menuTraceClose.setEnabled(this.aModel.isEnabled("menuTraceClose"));

		this.menuReport.setVisible(this.aModel.isVisible("menuReport"));
		this.menuReport.setEnabled(this.aModel.isEnabled("menuReport"));
		this.menuReportCreate.setVisible(this.aModel.isVisible("menuReportCreate"));
		this.menuReportCreate.setEnabled(this.aModel.isEnabled("menuReportCreate"));

		this.menuHelp.setVisible(this.aModel.isVisible("menuHelp"));
		this.menuHelp.setEnabled(this.aModel.isEnabled("menuHelp"));
		this.menuHelpContents.setVisible(this.aModel.isVisible("menuHelpContents"));
		this.menuHelpContents.setEnabled(this.aModel.isEnabled("menuHelpContents"));
		this.menuHelpFind.setVisible(this.aModel.isVisible("menuHelpFind"));
		this.menuHelpFind.setEnabled(this.aModel.isEnabled("menuHelpFind"));
		this.menuHelpTips.setVisible(this.aModel.isVisible("menuHelpTips"));
		this.menuHelpTips.setEnabled(this.aModel.isEnabled("menuHelpTips"));
		this.menuHelpStart.setVisible(this.aModel.isVisible("menuHelpStart"));
		this.menuHelpStart.setEnabled(this.aModel.isEnabled("menuHelpStart"));
		this.menuHelpCourse.setVisible(this.aModel.isVisible("menuHelpCourse"));
		this.menuHelpCourse.setEnabled(this.aModel.isEnabled("menuHelpCourse"));
		this.menuHelpHelp.setVisible(this.aModel.isVisible("menuHelpHelp"));
		this.menuHelpHelp.setEnabled(this.aModel.isEnabled("menuHelpHelp"));
		this.menuHelpSupport.setVisible(this.aModel.isVisible("menuHelpSupport"));
		this.menuHelpSupport.setEnabled(this.aModel.isEnabled("menuHelpSupport"));
		this.menuHelpLicense.setVisible(this.aModel.isVisible("menuHelpLicense"));
		this.menuHelpLicense.setEnabled(this.aModel.isEnabled("menuHelpLicense"));
		this.menuHelpAbout.setVisible(this.aModel.isVisible("menuHelpAbout"));
		this.menuHelpAbout.setEnabled(this.aModel.isEnabled("menuHelpAbout"));
	}

	public void this_actionPerformed(ActionEvent e)
	{
		if(this.aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = this.aModel.getCommand(s);
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
		this.adaptee.this_actionPerformed(e);
	}
}

