package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

public class AnalyseMainMenuBar extends JMenuBar implements ApplicationModelListener
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

	JMenu menuFile = new JMenu();
	JMenuItem menuFileOpen = new JMenuItem();
	JMenuItem menuFileOpenAs = new JMenu();
	JMenuItem menuFileOpenAsBellcore = new JMenuItem();
	JMenuItem menuFileOpenAsWavetek = new JMenuItem();
	JMenuItem menuFileSave = new JMenuItem();
	JMenuItem menuFileSaveAll = new JMenuItem();
	JMenuItem menuFileSaveAs = new JMenu();
	JMenuItem menuFileSaveAsText = new JMenuItem();
	JMenuItem menuFileClose = new JMenuItem();
	JMenuItem menuFileAddCompare = new JMenuItem();
	JMenuItem menuFileRemoveCompare = new JMenuItem();

	JMenu menuTrace = new JMenu();
	JMenuItem menuTraceAddCompare = new JMenuItem();
	JMenuItem menuTraceRemoveCompare = new JMenuItem();
	JMenuItem menuTraceDownload = new JMenuItem();
	JMenuItem menuTraceDownloadEtalon = new JMenuItem();
	JMenuItem menuTraceCloseEtalon = new JMenuItem();
	JMenuItem menuTraceClose = new JMenuItem();

	JMenu menuTestSetup = new JMenu();
	JMenuItem menuCreateTestSetup = new JMenuItem();
	JMenuItem menuSaveTestSetup = new JMenuItem();
	JMenuItem menuLoadTestSetup = new JMenuItem();
	JMenuItem menuSaveTestSetupAs = new JMenuItem();
	JMenuItem menuNetStudy = new JMenuItem();

	JMenuItem menuAnalyseUpload = new JMenuItem();

	JMenu menuOptions = new JMenu();
	JMenuItem menuOptionsColor = new JMenuItem();

	JMenu menuReport = new JMenu();
	JMenuItem menuReportCreate = new JMenuItem();

	JMenu menuWindow = new JMenu();
	JMenuItem menuWindowArrange = new JMenuItem();
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


	public AnalyseMainMenuBar()
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

	public AnalyseMainMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
				AnalyseMainMenuBar_this_actionAdapter actionAdapter =
		new AnalyseMainMenuBar_this_actionAdapter(this);

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

		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuFile.setText(LangModelAnalyse.Text("menuFile"));
		menuFile.setName("menuFile");
		menuFileOpen.setText(LangModelAnalyse.Text("menuFileOpen"));
		menuFileOpen.setName("menuFileOpen");
		menuFileOpen.addActionListener(actionAdapter);
		menuFileOpenAs.setText(LangModelAnalyse.Text("menuFileOpenAs"));
		menuFileOpenAs.setName("menuFileOpenAs");
		menuFileOpenAs.addActionListener(actionAdapter);
		menuFileOpenAsBellcore.setText(LangModelAnalyse.Text("menuFileOpenAsBellcore"));
		menuFileOpenAsBellcore.setName("menuFileOpenAsBellcore");
		menuFileOpenAsBellcore.addActionListener(actionAdapter);
		menuFileOpenAsWavetek.setText(LangModelAnalyse.Text("menuFileOpenAsWavetek"));
		menuFileOpenAsWavetek.setName("menuFileOpenAsWavetek");
		menuFileOpenAsWavetek.addActionListener(actionAdapter);
		menuFileSave.setText(LangModelAnalyse.Text("menuFileSave"));
		menuFileSave.setName("menuFileSave");
		menuFileSave.addActionListener(actionAdapter);
		menuFileSaveAll.setText(LangModelAnalyse.Text("menuFileSaveAll"));
		menuFileSaveAll.setName("menuFileSaveAll");
		menuFileSaveAll.addActionListener(actionAdapter);
		menuFileSaveAs.setText(LangModelAnalyse.Text("menuFileSaveAs"));
		menuFileSaveAs.setName("menuFileSaveAs");
		menuFileSaveAsText.setText(LangModelAnalyse.Text("menuFileSaveAsText"));
		menuFileSaveAsText.setName("menuFileSaveAsText");
		menuFileSaveAsText.addActionListener(actionAdapter);
		menuFileClose.setText(LangModelAnalyse.Text("menuFileClose"));
		menuFileClose.setName("menuFileClose");
		menuFileClose.addActionListener(actionAdapter);
		menuFileAddCompare.setText(LangModelAnalyse.Text("menuFileAddCompare"));
		menuFileAddCompare.setName("menuFileAddCompare");
		menuFileAddCompare.addActionListener(actionAdapter);
		menuFileRemoveCompare.setText(LangModelAnalyse.Text("menuFileRemoveCompare"));
		menuFileRemoveCompare.setName("menuFileRemoveCompare");
		menuFileRemoveCompare.addActionListener(actionAdapter);
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
		menuFile.add(menuFileSaveAll);
		menuFile.add(menuFileSaveAs);
		menuFileSaveAs.add(menuFileSaveAsText);

		menuTrace.setText(LangModelAnalyse.Text("menuTrace"));
		menuTrace.setName("menuTrace");
		menuTraceAddCompare.setText(LangModelAnalyse.Text("menuTraceAddCompare"));
		menuTraceAddCompare.setName("menuTraceAddCompare");
		menuTraceAddCompare.addActionListener(actionAdapter);
		menuTraceRemoveCompare.setText(LangModelAnalyse.Text("menuTraceRemoveCompare"));
		menuTraceRemoveCompare.setName("menuTraceRemoveCompare");
		menuTraceRemoveCompare.addActionListener(actionAdapter);
		menuTraceDownload.setText(LangModelAnalyse.Text("menuTraceDownload"));
		menuTraceDownload.setName("menuTraceDownload");
		menuTraceDownload.addActionListener(actionAdapter);
		menuTraceDownloadEtalon.setText(LangModelAnalyse.Text("menuTraceDownloadEtalon"));
		menuTraceDownloadEtalon.setName("menuTraceDownloadEtalon");
		menuTraceDownloadEtalon.addActionListener(actionAdapter);
		menuTraceClose.setText(LangModelAnalyse.Text("menuFileClose"));
		menuTraceClose.setName("menuFileClose");
		menuTraceClose.addActionListener(actionAdapter);
		menuTraceCloseEtalon.setText(LangModelAnalyse.Text("menuTraceCloseEtalon"));
		menuTraceCloseEtalon.setName("menuTraceCloseEtalon");
		menuTraceCloseEtalon.addActionListener(actionAdapter);
		menuTrace.add(menuTraceDownload);
		menuTrace.add(menuTraceDownloadEtalon);
		menuTrace.add(menuTraceAddCompare);
		menuTrace.addSeparator();
		menuTrace.add(menuTraceClose);
		menuTrace.add(menuTraceCloseEtalon);
		menuTrace.add(menuTraceRemoveCompare);
		menuTrace.addSeparator();
		menuTrace.add(menuAnalyseUpload);
		menuTrace.add(menuNetStudy);

		menuTestSetup.setText(LangModelAnalyse.Text("menuTestSetup"));
		menuTestSetup.setName("menuTestSetup");
		menuAnalyseUpload.setText(LangModelAnalyse.Text("menuAnalyseUpload"));
		menuAnalyseUpload.setName("menuAnalyseUpload");
		menuAnalyseUpload.addActionListener(actionAdapter);
		menuCreateTestSetup.setText(LangModelAnalyse.Text("menuCreateTestSetup"));
		menuCreateTestSetup.setName("menuCreateTestSetup");
		menuCreateTestSetup.addActionListener(actionAdapter);
		menuLoadTestSetup.setText(LangModelAnalyse.Text("menuLoadTestSetup"));
		menuLoadTestSetup.setName("menuLoadTestSetup");
		menuLoadTestSetup.addActionListener(actionAdapter);
		menuSaveTestSetup.setText(LangModelAnalyse.Text("menuSaveTestSetup"));
		menuSaveTestSetup.setName("menuSaveTestSetup");
		menuSaveTestSetup.addActionListener(actionAdapter);
		menuSaveTestSetupAs.setText(LangModelAnalyse.Text("menuSaveTestSetupAs"));
		menuSaveTestSetupAs.setName("menuSaveTestSetupAs");
		menuSaveTestSetupAs.addActionListener(actionAdapter);
		menuNetStudy.setText(LangModelAnalyse.Text("menuNetStudy"));
		menuNetStudy.setName("menuNetStudy");
		menuNetStudy.addActionListener(actionAdapter);
		menuTestSetup.add(menuCreateTestSetup);
		menuTestSetup.add(menuLoadTestSetup);
		menuTestSetup.addSeparator();
		menuTestSetup.add(menuSaveTestSetup);
		menuTestSetup.add(menuSaveTestSetupAs);

		menuOptions.setText(LangModelAnalyse.Text("menuOptions"));
		menuOptions.setName("menuOptions");
		menuOptionsColor.setText(LangModelAnalyse.Text("menuOptionsColor"));
		menuOptionsColor.setName("menuOptionsColor");
		menuOptionsColor.addActionListener(actionAdapter);
		menuOptions.add(menuOptionsColor);

		menuReport.setText(LangModelAnalyse.Text("menuReport"));
		menuReport.setName("menuReport");
		menuReportCreate.setText(LangModelAnalyse.Text("menuReportCreate"));
		menuReportCreate.setName("menuReportCreate");
		menuReportCreate.addActionListener(actionAdapter);
		menuReport.add(menuReportCreate);

		menuWindow.setText(LangModelAnalyse.Text("menuWindow"));
		menuWindow.setName("menuWindow");
		menuWindowArrange.setText(LangModelAnalyse.Text("menuWindowArrange"));
		menuWindowArrange.setName("menuWindowArrange");
		menuWindowArrange.addActionListener(actionAdapter);
		menuWindowTraceSelector.setText(LangModelAnalyse.Text("menuWindowTraceSelector"));
		menuWindowTraceSelector.setName("menuWindowTraceSelector");
		menuWindowTraceSelector.addActionListener(actionAdapter);
		menuWindowPrimaryParameters.setText(LangModelAnalyse.Text("menuWindowPrimaryParameters"));
		menuWindowPrimaryParameters.setName("menuWindowPrimaryParameters");
		menuWindowPrimaryParameters.addActionListener(actionAdapter);
		menuWindowOverallStats.setText(LangModelAnalyse.Text("menuWindowOverallStats"));
		menuWindowOverallStats.setName("menuWindowOverallStats");
		menuWindowOverallStats.addActionListener(actionAdapter);
		menuWindowNoiseFrame.setText(LangModelAnalyse.Text("menuWindowNoiseFrame"));
		menuWindowNoiseFrame.setName("menuWindowNoiseFrame");
		menuWindowNoiseFrame.addActionListener(actionAdapter);
		menuWindowFilteredFrame.setText(LangModelAnalyse.Text("menuWindowFilteredFrame"));
		menuWindowFilteredFrame.setName("menuWindowFilteredFrame");
		menuWindowFilteredFrame.addActionListener(actionAdapter);
		menuWindowEvents.setText(LangModelAnalyse.Text("menuWindowEvents"));
		menuWindowEvents.setName("menuWindowEvents");
		menuWindowEvents.addActionListener(actionAdapter);
		menuWindowDetailedEvents.setText(LangModelAnalyse.Text("menuWindowDetailedEvents"));
		menuWindowDetailedEvents.setName("menuWindowDetailedEvents");
		menuWindowDetailedEvents.addActionListener(actionAdapter);
		menuWindowAnalysis.setText(LangModelAnalyse.Text("menuWindowAnalysis"));
		menuWindowAnalysis.setName("menuWindowAnalysis");
		menuWindowAnalysis.addActionListener(actionAdapter);
		menuWindowMarkersInfo.setText(LangModelAnalyse.Text("menuWindowMarkersInfo"));
		menuWindowMarkersInfo.setName("menuWindowMarkersInfo");
		menuWindowMarkersInfo.addActionListener(actionAdapter);
		menuWindowAnalysisSelection.setText(LangModelAnalyse.Text("menuWindowAnalysisSelection"));
		menuWindowAnalysisSelection.setName("menuWindowAnalysisSelection");
		menuWindowAnalysisSelection.addActionListener(actionAdapter);
		menuWindowDerivHistoFrame.setText(LangModelAnalyse.Text("menuWindowDerivHistoFrame"));
		menuWindowDerivHistoFrame.setName("menuWindowDerivHistoFrame");
		menuWindowDerivHistoFrame.addActionListener(actionAdapter);
		menuWindowThresholdsSelection.setText(LangModelAnalyse.Text("menuWindowThresholdsSelection"));
		menuWindowThresholdsSelection.setName("menuWindowThresholdsSelection");
		menuWindowThresholdsSelection.addActionListener(actionAdapter);
		menuWindowThresholds.setText(LangModelAnalyse.Text("menuWindowThresholds"));
		menuWindowThresholds.setName("menuWindowThresholds");
		menuWindowThresholds.addActionListener(actionAdapter);

		menuWindow.add(menuWindowArrange);
		menuWindow.addSeparator();
		menuWindow.add(menuWindowTraceSelector);
		menuWindow.add(menuWindowPrimaryParameters);
		menuWindow.add(menuWindowOverallStats);
		menuWindow.add(menuWindowMarkersInfo);
		menuWindow.add(menuWindowEvents);
		menuWindow.add(menuWindowDetailedEvents);
		menuWindow.add(menuWindowAnalysisSelection);
		menuWindow.add(menuWindowAnalysis);
		menuWindow.add(menuWindowNoiseFrame);
		menuWindow.add(menuWindowFilteredFrame);
		menuWindow.add(menuWindowDerivHistoFrame);
		menuWindow.add(menuWindowThresholdsSelection);
		menuWindow.add(menuWindowThresholds);

		this.add(menuSession);
		this.add(menuFile);
		this.add(menuTrace);
		this.add(menuTestSetup);
		this.add(menuReport);
		this.add(menuWindow);
		//this.add(menuOptions);
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
		menuFileSaveAll.setVisible(aModel.isVisible("menuFileSaveAll"));
		menuFileSaveAll.setEnabled(aModel.isEnabled("menuFileSaveAll"));
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

		menuAnalyseUpload.setVisible(aModel.isVisible("menuAnalyseUpload"));
		menuAnalyseUpload.setEnabled(aModel.isEnabled("menuAnalyseUpload"));


		menuTraceDownload.setVisible(aModel.isVisible("menuTraceDownload"));
		menuTraceDownload.setEnabled(aModel.isEnabled("menuTraceDownload"));
		menuTraceDownloadEtalon.setVisible(aModel.isVisible("menuTraceDownloadEtalon"));
		menuTraceDownloadEtalon.setEnabled(aModel.isEnabled("menuTraceDownloadEtalon"));
		menuTraceClose.setVisible(aModel.isVisible("menuTraceClose"));
		menuTraceClose.setEnabled(aModel.isEnabled("menuTraceClose"));
		menuTraceCloseEtalon.setVisible(aModel.isVisible("menuTraceCloseEtalon"));
		menuTraceCloseEtalon.setEnabled(aModel.isEnabled("menuTraceCloseEtalon"));
		menuTestSetup.setVisible(aModel.isVisible("menuTestSetup"));
		menuTestSetup.setEnabled(aModel.isEnabled("menuTestSetup"));
		menuCreateTestSetup.setVisible(aModel.isVisible("menuCreateTestSetup"));
		menuCreateTestSetup.setEnabled(aModel.isEnabled("menuCreateTestSetup"));
		menuLoadTestSetup.setVisible(aModel.isVisible("menuLoadTestSetup"));
		menuLoadTestSetup.setEnabled(aModel.isEnabled("menuLoadTestSetup"));
		menuSaveTestSetup.setVisible(aModel.isVisible("menuSaveTestSetup"));
		menuSaveTestSetup.setEnabled(aModel.isEnabled("menuSaveTestSetup"));
		menuSaveTestSetupAs.setVisible(aModel.isVisible("menuSaveTestSetupAs"));
		menuSaveTestSetupAs.setEnabled(aModel.isEnabled("menuSaveTestSetupAs"));
		menuNetStudy.setVisible(aModel.isVisible("menuNetStudy"));
		menuNetStudy.setEnabled(aModel.isEnabled("menuNetStudy"));

		menuReport.setVisible(aModel.isVisible("menuReport"));
		menuReport.setEnabled(aModel.isEnabled("menuReport"));
		menuReportCreate.setVisible(aModel.isVisible("menuReportCreate"));
		menuReportCreate.setEnabled(aModel.isEnabled("menuReportCreate"));

		menuWindow.setVisible(aModel.isVisible("menuWindow"));
		menuWindow.setEnabled(aModel.isEnabled("menuWindow"));
		menuWindowArrange.setVisible(aModel.isVisible("menuWindowArrange"));
		menuWindowArrange.setEnabled(aModel.isEnabled("menuWindowArrange"));
		menuWindowTraceSelector.setVisible(aModel.isVisible("menuWindowTraceSelector"));
		menuWindowTraceSelector.setEnabled(aModel.isEnabled("menuWindowTraceSelector"));
		menuWindowPrimaryParameters.setVisible(aModel.isVisible("menuWindowPrimaryParameters"));
		menuWindowPrimaryParameters.setEnabled(aModel.isEnabled("menuWindowPrimaryParameters"));
		menuWindowOverallStats.setVisible(aModel.isVisible("menuWindowOverallStats"));
		menuWindowOverallStats.setEnabled(aModel.isEnabled("menuWindowOverallStats"));
		menuWindowNoiseFrame.setVisible(aModel.isVisible("menuWindowNoiseFrame"));
		menuWindowNoiseFrame.setEnabled(aModel.isEnabled("menuWindowNoiseFrame"));
		menuWindowFilteredFrame.setVisible(aModel.isVisible("menuWindowFilteredFrame"));
		menuWindowFilteredFrame.setEnabled(aModel.isEnabled("menuWindowFilteredFrame"));
		menuWindowEvents.setVisible(aModel.isVisible("menuWindowEvents"));
		menuWindowEvents.setEnabled(aModel.isEnabled("menuWindowEvents"));
		menuWindowDetailedEvents.setVisible(aModel.isVisible("menuWindowDetailedEvents"));
		menuWindowDetailedEvents.setEnabled(aModel.isEnabled("menuWindowDetailedEvents"));
		menuWindowAnalysis.setVisible(aModel.isVisible("menuWindowAnalysis"));
		menuWindowAnalysis.setEnabled(aModel.isEnabled("menuWindowAnalysis"));
		menuWindowMarkersInfo.setVisible(aModel.isVisible("menuWindowMarkersInfo"));
		menuWindowMarkersInfo.setEnabled(aModel.isEnabled("menuWindowMarkersInfo"));
		menuWindowAnalysisSelection.setVisible(aModel.isVisible("menuWindowAnalysisSelection"));
		menuWindowAnalysisSelection.setEnabled(aModel.isEnabled("menuWindowAnalysisSelection"));
		menuWindowDerivHistoFrame.setVisible(aModel.isVisible("menuWindowDerivHistoFrame"));
		menuWindowDerivHistoFrame.setEnabled(aModel.isEnabled("menuWindowDerivHistoFrame"));
		menuWindowThresholdsSelection.setVisible(aModel.isVisible("menuWindowThresholdsSelection"));
		menuWindowThresholdsSelection.setEnabled(aModel.isEnabled("menuWindowThresholdsSelection"));
		menuWindowThresholds.setVisible(aModel.isVisible("menuWindowThresholds"));
		menuWindowThresholds.setEnabled(aModel.isEnabled("menuWindowThresholds"));
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

class AnalyseMainMenuBar_this_actionAdapter implements java.awt.event.ActionListener
{
	AnalyseMainMenuBar adaptee;

	AnalyseMainMenuBar_this_actionAdapter(AnalyseMainMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}