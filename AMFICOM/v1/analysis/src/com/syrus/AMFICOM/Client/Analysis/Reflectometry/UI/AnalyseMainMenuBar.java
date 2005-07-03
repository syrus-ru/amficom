
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;

public class AnalyseMainMenuBar extends AbstractMainMenuBar {

	// JMenu menuSession = new JMenu();
	// JMenuItem menuSessionNew = new JMenuItem();
	// JMenuItem menuSessionClose = new JMenuItem();
	// JMenuItem menuSessionOptions = new JMenuItem();
	// JMenuItem menuSessionConnection = new JMenuItem();
	// JMenuItem menuSessionChangePassword = new JMenuItem();
	// JMenuItem menuSessionDomain = new JMenuItem();
	// JMenuItem menuExit = new JMenuItem();
	//
	

	// public AnalyseMainMenuBar()
	// {
	// super();
	// try
	// {
	// jbInit();
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public AnalyseMainMenuBar(ApplicationModel aModel) {
		// this();
		super(aModel);
	}

	protected void addMenuItems() {
		// AnalyseMainMenuBar_this_actionAdapter actionAdapter =
		// new AnalyseMainMenuBar_this_actionAdapter(this);

		// menuSession.setText(LangModel.getString("menuSession"));
		// menuSession.setName("menuSession");
		// menuSessionNew.setText(LangModel.getString("menuSessionNew"));
		// menuSessionNew.setName("menuSessionNew");
		// menuSessionNew.addActionListener(actionAdapter);
		// menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		// menuSessionClose.setName("menuSessionClose");
		// menuSessionClose.addActionListener(actionAdapter);
		// menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
		// menuSessionOptions.setName("menuSessionOptions");
		// menuSessionOptions.addActionListener(actionAdapter);
		// menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		// menuSessionConnection.setName("menuSessionConnection");
		// menuSessionConnection.addActionListener(actionAdapter);
		// menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		// menuSessionChangePassword.setName("menuSessionChangePassword");
		// menuSessionChangePassword.addActionListener(actionAdapter);
		// menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		// menuSessionDomain.setName("menuSessionDomain");
		// menuSessionDomain.addActionListener(actionAdapter);
		// menuExit.setText(LangModel.getString("menuExit"));
		// menuExit.setName("menuExit");
		// menuExit.addActionListener(actionAdapter);
		//
		// menuSession.add(menuSessionNew);
		// menuSession.add(menuSessionClose);
		// menuSession.add(menuSessionOptions);
		// menuSession.add(menuSessionChangePassword);
		// menuSession.addSeparator();
		// menuSession.add(menuSessionConnection);
		// menuSession.addSeparator();
		//
		// menuSession.add(menuSessionDomain);
		// menuSession.addSeparator();
		// menuSession.add(menuExit);

		final JMenu						menuFile						= new JMenu();
		final JMenuItem					menuFileOpen					= new JMenuItem();
		final JMenuItem					menuFileOpenAs					= new JMenu();
		final JMenuItem					menuFileOpenAsBellcore			= new JMenuItem();
		final JMenuItem					menuFileOpenAsWavetek			= new JMenuItem();
		final JMenuItem					menuFileSave					= new JMenuItem();
		final JMenuItem					menuFileSaveAll					= new JMenuItem();
		final JMenuItem					menuFileSaveAs					= new JMenu();
		final JMenuItem					menuFileSaveAsText				= new JMenuItem();
		final JMenuItem					menuFileClose					= new JMenuItem();
		final JMenuItem					menuFileAddCompare				= new JMenuItem();
		final JMenuItem					menuFileRemoveCompare			= new JMenuItem();

		final JMenu						menuTrace						= new JMenu();
		final JMenuItem					menuTraceAddCompare				= new JMenuItem();
		final JMenuItem					menuTraceRemoveCompare			= new JMenuItem();
		final JMenuItem					menuTraceDownload				= new JMenuItem();
		final JMenuItem					menuTraceDownloadEtalon			= new JMenuItem();
		final JMenuItem					menuTraceCloseEtalon			= new JMenuItem();
		final JMenuItem					menuTraceClose					= new JMenuItem();

		final JMenu						menuTestSetup					= new JMenu();
		final JMenuItem					menuCreateTestSetup				= new JMenuItem();
		final JMenuItem					menuSaveTestSetup				= new JMenuItem();
		final JMenuItem					menuLoadTestSetup				= new JMenuItem();
		final JMenuItem					menuSaveTestSetupAs				= new JMenuItem();
		final JMenuItem					menuNetStudy					= new JMenuItem();

		final JMenuItem					menuAnalyseUpload				= new JMenuItem();

		final JMenu						menuOptions						= new JMenu();
		final JMenuItem					menuOptionsColor				= new JMenuItem();

		final JMenu						menuReport						= new JMenu();
		final JMenuItem					menuReportCreate				= new JMenuItem();

		final JMenu						menuWindow						= new JMenu();
		final JMenuItem					menuWindowArrange				= new JMenuItem();
		final JMenuItem					menuWindowTraceSelector			= new JMenuItem();
		final JMenuItem					menuWindowPrimaryParameters		= new JMenuItem();
		final JMenuItem					menuWindowOverallStats			= new JMenuItem();
		final JMenuItem					menuWindowNoiseFrame			= new JMenuItem();
		final JMenuItem					menuWindowFilteredFrame			= new JMenuItem();
		final JMenuItem					menuWindowEvents				= new JMenuItem();
		final JMenuItem					menuWindowDetailedEvents		= new JMenuItem();
		final JMenuItem					menuWindowAnalysis				= new JMenuItem();
		final JMenuItem					menuWindowMarkersInfo			= new JMenuItem();
		final JMenuItem					menuWindowAnalysisSelection		= new JMenuItem();
		final JMenuItem					menuWindowDerivHistoFrame		= new JMenuItem();
		final JMenuItem					menuWindowThresholdsSelection	= new JMenuItem();
		final JMenuItem					menuWindowThresholds			= new JMenuItem();
		
		menuFile.setText(LangModelAnalyse.getString("menuFile"));
		menuFile.setName("menuFile");
		menuFileOpen.setText(LangModelAnalyse.getString("menuFileOpen"));
		menuFileOpen.setName("menuFileOpen");
		menuFileOpen.addActionListener(this.actionAdapter);
		menuFileOpenAs.setText(LangModelAnalyse.getString("menuFileOpenAs"));
		menuFileOpenAs.setName("menuFileOpenAs");
		menuFileOpenAs.addActionListener(this.actionAdapter);
		menuFileOpenAsBellcore.setText(LangModelAnalyse.getString("menuFileOpenAsBellcore"));
		menuFileOpenAsBellcore.setName("menuFileOpenAsBellcore");
		menuFileOpenAsBellcore.addActionListener(this.actionAdapter);
		menuFileOpenAsWavetek.setText(LangModelAnalyse.getString("menuFileOpenAsWavetek"));
		menuFileOpenAsWavetek.setName("menuFileOpenAsWavetek");
		menuFileOpenAsWavetek.addActionListener(this.actionAdapter);
		menuFileSave.setText(LangModelAnalyse.getString("menuFileSave"));
		menuFileSave.setName("menuFileSave");
		menuFileSave.addActionListener(this.actionAdapter);
		menuFileSaveAll.setText(LangModelAnalyse.getString("menuFileSaveAll"));
		menuFileSaveAll.setName("menuFileSaveAll");
		menuFileSaveAll.addActionListener(this.actionAdapter);
		menuFileSaveAs.setText(LangModelAnalyse.getString("menuFileSaveAs"));
		menuFileSaveAs.setName("menuFileSaveAs");
		menuFileSaveAsText.setText(LangModelAnalyse.getString("menuFileSaveAsTextFile"));
		menuFileSaveAsText.setName("menuFileSaveAsText");
		menuFileSaveAsText.addActionListener(this.actionAdapter);
		menuFileClose.setText(LangModelAnalyse.getString("menuFileClose"));
		menuFileClose.setName("menuFileClose");
		menuFileClose.addActionListener(this.actionAdapter);
		menuFileAddCompare.setText(LangModelAnalyse.getString("menuFileAddCompare"));
		menuFileAddCompare.setName("menuFileAddCompare");
		menuFileAddCompare.addActionListener(this.actionAdapter);
		menuFileRemoveCompare.setText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		menuFileRemoveCompare.setName("menuFileRemoveCompare");
		menuFileRemoveCompare.addActionListener(this.actionAdapter);
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

		menuTrace.setText(LangModelAnalyse.getString("menuTrace"));
		menuTrace.setName("menuTrace");
		menuTraceAddCompare.setText(LangModelAnalyse.getString("menuTraceAddCompare"));
		menuTraceAddCompare.setName("menuTraceAddCompare");
		menuTraceAddCompare.addActionListener(this.actionAdapter);
		menuTraceRemoveCompare.setText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		menuTraceRemoveCompare.setName("menuTraceRemoveCompare");
		menuTraceRemoveCompare.addActionListener(this.actionAdapter);
		menuTraceDownload.setText(LangModelAnalyse.getString("menuTraceDownload"));
		menuTraceDownload.setName("menuTraceDownload");
		menuTraceDownload.addActionListener(this.actionAdapter);
		menuTraceDownloadEtalon.setText(LangModelAnalyse.getString("menuTraceDownloadEtalon"));
		menuTraceDownloadEtalon.setName("menuTraceDownloadEtalon");
		menuTraceDownloadEtalon.addActionListener(this.actionAdapter);
		menuTraceClose.setText(LangModelAnalyse.getString("menuFileClose"));
		menuTraceClose.setName("menuFileClose");
		menuTraceClose.addActionListener(this.actionAdapter);
		menuTraceCloseEtalon.setText(LangModelAnalyse.getString("menuTraceCloseEtalon"));
		menuTraceCloseEtalon.setName("menuTraceCloseEtalon");
		menuTraceCloseEtalon.addActionListener(this.actionAdapter);
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

		menuTestSetup.setText(LangModelAnalyse.getString("menuTestSetup"));
		menuTestSetup.setName("menuTestSetup");
		menuAnalyseUpload.setText(LangModelAnalyse.getString("menuAnalyseUpload"));
		menuAnalyseUpload.setName("menuAnalyseUpload");
		menuAnalyseUpload.addActionListener(this.actionAdapter);
		menuCreateTestSetup.setText(LangModelAnalyse.getString("menuCreateTestSetup"));
		menuCreateTestSetup.setName("menuCreateTestSetup");
		menuCreateTestSetup.addActionListener(this.actionAdapter);
		menuLoadTestSetup.setText(LangModelAnalyse.getString("menuLoadTestSetup"));
		menuLoadTestSetup.setName("menuLoadTestSetup");
		menuLoadTestSetup.addActionListener(this.actionAdapter);
		menuSaveTestSetup.setText(LangModelAnalyse.getString("menuSaveTestSetup"));
		menuSaveTestSetup.setName("menuSaveTestSetup");
		menuSaveTestSetup.addActionListener(this.actionAdapter);
		menuSaveTestSetupAs.setText(LangModelAnalyse.getString("menuSaveTestSetupAs"));
		menuSaveTestSetupAs.setName("menuSaveTestSetupAs");
		menuSaveTestSetupAs.addActionListener(this.actionAdapter);
		menuNetStudy.setText(LangModelAnalyse.getString("menuNetStudy"));
		menuNetStudy.setName("menuNetStudy");
		menuNetStudy.addActionListener(this.actionAdapter);
		menuTestSetup.add(menuCreateTestSetup);
		menuTestSetup.add(menuLoadTestSetup);
		menuTestSetup.addSeparator();
		menuTestSetup.add(menuSaveTestSetup);
		menuTestSetup.add(menuSaveTestSetupAs);

		menuOptions.setText(LangModelAnalyse.getString("menuOptions"));
		menuOptions.setName("menuOptions");
		menuOptionsColor.setText(LangModelAnalyse.getString("menuOptionsColor"));
		menuOptionsColor.setName("menuOptionsColor");
		menuOptionsColor.addActionListener(this.actionAdapter);
		menuOptions.add(menuOptionsColor);

		menuReport.setText(LangModelAnalyse.getString("menuReport"));
		menuReport.setName("menuReport");
		menuReportCreate.setText(LangModelAnalyse.getString("menuReportCreate"));
		menuReportCreate.setName("menuReportCreate");
		menuReportCreate.addActionListener(this.actionAdapter);
		menuReport.add(menuReportCreate);

		menuWindow.setText(LangModelAnalyse.getString("menuWindow"));
		menuWindow.setName("menuWindow");
		menuWindowArrange.setText(LangModelAnalyse.getString("menuWindowArrange"));
		menuWindowArrange.setName("menuWindowArrange");
		menuWindowArrange.addActionListener(this.actionAdapter);
		menuWindowTraceSelector.setText(LangModelAnalyse.getString("menuWindowTraceSelector"));
		menuWindowTraceSelector.setName("menuWindowTraceSelector");
		menuWindowTraceSelector.addActionListener(this.actionAdapter);
		menuWindowPrimaryParameters.setText(LangModelAnalyse.getString("menuWindowPrimaryParameters"));
		menuWindowPrimaryParameters.setName("menuWindowPrimaryParameters");
		menuWindowPrimaryParameters.addActionListener(this.actionAdapter);
		menuWindowOverallStats.setText(LangModelAnalyse.getString("menuWindowOverallStats"));
		menuWindowOverallStats.setName("menuWindowOverallStats");
		menuWindowOverallStats.addActionListener(this.actionAdapter);
		menuWindowNoiseFrame.setText(LangModelAnalyse.getString("menuWindowNoiseFrame"));
		menuWindowNoiseFrame.setName("menuWindowNoiseFrame");
		menuWindowNoiseFrame.addActionListener(this.actionAdapter);
		menuWindowFilteredFrame.setText(LangModelAnalyse.getString("menuWindowFilteredFrame"));
		menuWindowFilteredFrame.setName("menuWindowFilteredFrame");
		menuWindowFilteredFrame.addActionListener(this.actionAdapter);
		menuWindowEvents.setText(LangModelAnalyse.getString("menuWindowEvents"));
		menuWindowEvents.setName("menuWindowEvents");
		menuWindowEvents.addActionListener(this.actionAdapter);
		menuWindowDetailedEvents.setText(LangModelAnalyse.getString("menuWindowDetailedEvents"));
		menuWindowDetailedEvents.setName("menuWindowDetailedEvents");
		menuWindowDetailedEvents.addActionListener(this.actionAdapter);
		menuWindowAnalysis.setText(LangModelAnalyse.getString("menuWindowAnalysis"));
		menuWindowAnalysis.setName("menuWindowAnalysis");
		menuWindowAnalysis.addActionListener(this.actionAdapter);
		menuWindowMarkersInfo.setText(LangModelAnalyse.getString("menuWindowMarkersInfo"));
		menuWindowMarkersInfo.setName("menuWindowMarkersInfo");
		menuWindowMarkersInfo.addActionListener(this.actionAdapter);
		menuWindowAnalysisSelection.setText(LangModelAnalyse.getString("menuWindowAnalysisSelection"));
		menuWindowAnalysisSelection.setName("menuWindowAnalysisSelection");
		menuWindowAnalysisSelection.addActionListener(this.actionAdapter);
		menuWindowDerivHistoFrame.setText(LangModelAnalyse.getString("menuWindowDerivHistoFrame"));
		menuWindowDerivHistoFrame.setName("menuWindowDerivHistoFrame");
		menuWindowDerivHistoFrame.addActionListener(this.actionAdapter);
		menuWindowThresholdsSelection.setText(LangModelAnalyse.getString("menuWindowThresholdsSelection"));
		menuWindowThresholdsSelection.setName("menuWindowThresholdsSelection");
		menuWindowThresholdsSelection.addActionListener(this.actionAdapter);
		menuWindowThresholds.setText(LangModelAnalyse.getString("menuWindowThresholds"));
		menuWindowThresholds.setName("menuWindowThresholds");
		menuWindowThresholds.addActionListener(this.actionAdapter);

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

		// this.add(menuSession);
		this.add(menuFile);
		this.add(menuTrace);
		this.add(menuTestSetup);
		this.add(menuReport);
		this.add(menuWindow);
		// this.add(menuOptions);
		
		this.addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e});
			}

			public void modelChanged(String e[]) {
				// menuSession.setVisible(aModel.isVisible("menuSession"));
				// menuSession.setEnabled(aModel.isEnabled("menuSession"));
				// menuSessionNew.setVisible(aModel.isVisible("menuSessionNew"));
				// menuSessionNew.setEnabled(aModel.isEnabled("menuSessionNew"));
				// menuSessionClose.setVisible(aModel.isVisible("menuSessionClose"));
				// menuSessionClose.setEnabled(aModel.isEnabled("menuSessionClose"));
				// menuSessionOptions.setVisible(aModel.isVisible("menuSessionOptions"));
				// menuSessionOptions.setEnabled(aModel.isEnabled("menuSessionOptions"));
				// menuSessionConnection.setVisible(aModel.isVisible("menuSessionConnection"));
				// menuSessionConnection.setEnabled(aModel.isEnabled("menuSessionConnection"));
				// menuSessionChangePassword.setVisible(aModel.isVisible("menuSessionChangePassword"));
				// menuSessionChangePassword.setEnabled(aModel.isEnabled("menuSessionChangePassword"));
				// menuSessionDomain.setVisible(aModel.isVisible("menuSessionDomain"));
				// menuSessionDomain.setEnabled(aModel.isEnabled("menuSessionDomain"));

				menuFileOpen.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileOpen"));
				menuFileOpen.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileOpen"));
				menuFileOpenAs.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileOpenAs"));
				menuFileOpenAs.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileOpenAs"));
				menuFileOpenAsBellcore.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileOpenAsBellcore"));
				menuFileOpenAsBellcore.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileOpenAsBellcore"));
				menuFileOpenAsWavetek.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileOpenAsWavetek"));
				menuFileOpenAsWavetek.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileOpenAsWavetek"));
				menuFileSave.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileSave"));
				menuFileSave.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileSave"));
				menuFileSaveAs.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileSaveAs"));
				menuFileSaveAs.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileSaveAs"));
				menuFileSaveAsText.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileSaveAsText"));
				menuFileSaveAsText.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileSaveAsText"));
				menuFileSaveAll.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileSaveAll"));
				menuFileSaveAll.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileSaveAll"));
				menuFileClose.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileClose"));
				menuFileClose.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileClose"));
				menuFileAddCompare.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileAddCompare"));
				menuFileAddCompare.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileAddCompare"));
				menuFileRemoveCompare.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuFileRemoveCompare"));
				menuFileRemoveCompare.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuFileRemoveCompare"));

				menuTraceAddCompare.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuTraceAddCompare"));
				menuTraceAddCompare.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuTraceAddCompare"));
				menuTraceRemoveCompare.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuTraceRemoveCompare"));
				menuTraceRemoveCompare.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuTraceRemoveCompare"));

				menuAnalyseUpload.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuAnalyseUpload"));
				menuAnalyseUpload.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuAnalyseUpload"));

				menuTraceDownload.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuTraceDownload"));
				menuTraceDownload.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuTraceDownload"));
				menuTraceDownloadEtalon.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuTraceDownloadEtalon"));
				menuTraceDownloadEtalon.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuTraceDownloadEtalon"));
				menuTraceClose.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuTraceClose"));
				menuTraceClose.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuTraceClose"));
				menuTraceCloseEtalon.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuTraceCloseEtalon"));
				menuTraceCloseEtalon.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuTraceCloseEtalon"));
				menuTestSetup.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuTestSetup"));
				menuTestSetup.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuTestSetup"));
				menuCreateTestSetup.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuCreateTestSetup"));
				menuCreateTestSetup.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuCreateTestSetup"));
				menuLoadTestSetup.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuLoadTestSetup"));
				menuLoadTestSetup.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuLoadTestSetup"));
				menuSaveTestSetup.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuSaveTestSetup"));
				menuSaveTestSetup.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuSaveTestSetup"));
				menuSaveTestSetupAs.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuSaveTestSetupAs"));
				menuSaveTestSetupAs.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuSaveTestSetupAs"));
				menuNetStudy.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuNetStudy"));
				menuNetStudy.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuNetStudy"));

				menuReport.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuReport"));
				menuReport.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuReport"));
				menuReportCreate.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuReportCreate"));
				menuReportCreate.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuReportCreate"));

				menuWindow.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindow"));
				menuWindow.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindow"));
				menuWindowArrange.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowArrange"));
				menuWindowArrange.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowArrange"));
				menuWindowTraceSelector.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowTraceSelector"));
				menuWindowTraceSelector.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowTraceSelector"));
				menuWindowPrimaryParameters.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowPrimaryParameters"));
				menuWindowPrimaryParameters.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowPrimaryParameters"));
				menuWindowOverallStats.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowOverallStats"));
				menuWindowOverallStats.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowOverallStats"));
				menuWindowNoiseFrame.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowNoiseFrame"));
				menuWindowNoiseFrame.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowNoiseFrame"));
				menuWindowFilteredFrame.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowFilteredFrame"));
				menuWindowFilteredFrame.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowFilteredFrame"));
				menuWindowEvents.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowEvents"));
				menuWindowEvents.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowEvents"));
				menuWindowDetailedEvents.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowDetailedEvents"));
				menuWindowDetailedEvents.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowDetailedEvents"));
				menuWindowAnalysis.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowAnalysis"));
				menuWindowAnalysis.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowAnalysis"));
				menuWindowMarkersInfo.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowMarkersInfo"));
				menuWindowMarkersInfo.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowMarkersInfo"));
				menuWindowAnalysisSelection.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowAnalysisSelection"));
				menuWindowAnalysisSelection.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowAnalysisSelection"));
				menuWindowDerivHistoFrame.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowDerivHistoFrame"));
				menuWindowDerivHistoFrame.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowDerivHistoFrame"));
				menuWindowThresholdsSelection.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowThresholdsSelection"));
				menuWindowThresholdsSelection.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowThresholdsSelection"));
				menuWindowThresholds.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuWindowThresholds"));
				menuWindowThresholds.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuWindowThresholds"));
			}
		}
			);
	}

	
}
