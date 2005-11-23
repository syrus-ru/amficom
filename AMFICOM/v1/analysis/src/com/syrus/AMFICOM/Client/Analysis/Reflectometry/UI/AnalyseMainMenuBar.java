
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;

public class AnalyseMainMenuBar extends AbstractMainMenuBar {
	private static final long serialVersionUID = 6525849494027083937L;

	public AnalyseMainMenuBar(ApplicationModel aModel) {
		super(aModel);
	}

	@Override
	protected void addMenuItems() {
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
//		final JMenuItem					menuTraceDownloadEtalon			= new JMenuItem();
//		final JMenuItem					menuTraceCloseEtalon			= new JMenuItem();
		final JMenuItem					menuTraceClose					= new JMenuItem();
		final JMenuItem					menuTraceSavePES				= new JMenuItem();

		final JMenuItem					menuMakeCurrentTracePrimary		= new JMenuItem();

		final JMenu						menuTestSetup					= new JMenu();
		final JMenuItem					menuCreateTestSetup				= new JMenuItem();
		final JMenuItem					menuSaveTestSetup				= new JMenuItem();
//		final JMenuItem					menuLoadTestSetup				= new JMenuItem();
		final JMenuItem					menuSaveTestSetupAs				= new JMenuItem();
//		final JMenuItem					menuNetStudy					= new JMenuItem();

//		final JMenuItem					menuAnalyseUpload				= new JMenuItem();

//		final JMenu						menuOptions						= new JMenu();
//		final JMenuItem					menuOptionsColor				= new JMenuItem();

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
		menuFile.setName(AnalyseApplicationModel.MENU_FILE);
		menuFileOpen.setText(LangModelAnalyse.getString("menuFileOpen"));
		menuFileOpen.setName(AnalyseApplicationModel.MENU_FILE_OPEN);
		menuFileOpen.addActionListener(this.actionAdapter);
		menuFileOpenAs.setText(LangModelAnalyse.getString("menuFileOpenAs"));
		menuFileOpenAs.setName(AnalyseApplicationModel.MENU_FILE_OPEN_AS);
		menuFileOpenAs.addActionListener(this.actionAdapter);
		menuFileOpenAsBellcore.setText(LangModelAnalyse.getString("menuFileOpenAsBellcore"));
		menuFileOpenAsBellcore.setName(AnalyseApplicationModel.MENU_FILE_OPEN_BELLCORE);
		menuFileOpenAsBellcore.addActionListener(this.actionAdapter);
		menuFileOpenAsWavetek.setText(LangModelAnalyse.getString("menuFileOpenAsWavetek"));
		menuFileOpenAsWavetek.setName(AnalyseApplicationModel.MENU_FILE_OPEN_WAVETEK);
		menuFileOpenAsWavetek.addActionListener(this.actionAdapter);
		menuFileSave.setText(LangModelAnalyse.getString("menuFileSave"));
		menuFileSave.setName(AnalyseApplicationModel.MENU_FILE_SAVE);
		menuFileSave.addActionListener(this.actionAdapter);
		menuFileSaveAll.setText(LangModelAnalyse.getString("menuFileSaveAll"));
		menuFileSaveAll.setName(AnalyseApplicationModel.MENU_FILE_SAVE_ALL);
		menuFileSaveAll.addActionListener(this.actionAdapter);
		menuFileSaveAs.setText(LangModelAnalyse.getString("menuFileSaveAs"));
		menuFileSaveAs.setName(AnalyseApplicationModel.MENU_FILE_SAVE_AS);
		menuFileSaveAsText.setText(LangModelAnalyse.getString("menuFileSaveAsTextFile"));
		menuFileSaveAsText.setName(AnalyseApplicationModel.MENU_FILE_SAVE_TEXT);
		menuFileSaveAsText.addActionListener(this.actionAdapter);
		menuFileClose.setText(LangModelAnalyse.getString("menuFileClose"));
		menuFileClose.setName(AnalyseApplicationModel.MENU_FILE_CLOSE);
		menuFileClose.addActionListener(this.actionAdapter);
		menuFileAddCompare.setText(LangModelAnalyse.getString("menuFileAddCompare"));
		menuFileAddCompare.setName(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE);
		menuFileAddCompare.addActionListener(this.actionAdapter);
		menuFileRemoveCompare.setText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		menuFileRemoveCompare.setName(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE);
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
		menuTrace.setName(AnalyseApplicationModel.MENU_TRACE);
		menuTraceAddCompare.setText(LangModelAnalyse.getString("menuTraceAddCompare"));
		menuTraceAddCompare.setName(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE);
		menuTraceAddCompare.addActionListener(this.actionAdapter);
		menuTraceRemoveCompare.setText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		menuTraceRemoveCompare.setName(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE);
		menuTraceRemoveCompare.addActionListener(this.actionAdapter);
		menuTraceDownload.setText(LangModelAnalyse.getString("menuTraceDownload"));
		menuTraceDownload.setName(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD);
		menuTraceDownload.addActionListener(this.actionAdapter);
//		menuTraceDownloadEtalon.setText(LangModelAnalyse.getString("menuTraceDownloadEtalon"));
//		menuTraceDownloadEtalon.setName("menuTraceDownloadEtalon");
//		menuTraceDownloadEtalon.addActionListener(this.actionAdapter);

		menuMakeCurrentTracePrimary.setText(LangModelAnalyse.getString("menuMakeCurrentTracePrimary"));
		menuMakeCurrentTracePrimary.setName(AnalyseApplicationModel.MENU_TRACE_CURRENT_MAKE_PRIMARY);
		menuMakeCurrentTracePrimary.addActionListener(this.actionAdapter);
		
		menuTraceClose.setText(LangModelAnalyse.getString("menuFileClose"));
		menuTraceClose.setName(AnalyseApplicationModel.MENU_FILE_CLOSE);
		menuTraceClose.addActionListener(this.actionAdapter);
//		menuTraceCloseEtalon.setText(LangModelAnalyse.getString("menuTraceCloseEtalon"));
//		menuTraceCloseEtalon.setName("menuTraceCloseEtalon");
//		menuTraceCloseEtalon.addActionListener(this.actionAdapter);
		menuTraceSavePES.setName(AnalyseApplicationModel.MENU_TRACE_SAVE_PATHELEMENTS);
		menuTraceSavePES.setText(LangModelAnalyse.getString("menuTraceSavePES"));
		menuTraceSavePES.addActionListener(this.actionAdapter);

		menuTrace.add(menuTraceDownload);
//		menuTrace.add(menuTraceDownloadEtalon);
		menuTrace.add(menuTraceAddCompare);
		menuTrace.addSeparator();
		menuTrace.add(menuTraceClose);
//		menuTrace.add(menuTraceCloseEtalon);
		menuTrace.add(menuTraceRemoveCompare);
//		menuTrace.add(menuAnalyseUpload);
//		menuTrace.add(menuNetStudy);
		menuTrace.addSeparator();
		menuTrace.add(menuMakeCurrentTracePrimary);
		menuTrace.add(menuTraceSavePES);

		menuTestSetup.setText(LangModelAnalyse.getString("menuTestSetup"));
		menuTestSetup.setName(AnalyseApplicationModel.MENU_MEASUREMENTSETUP);
//		menuAnalyseUpload.setText(LangModelAnalyse.getString("menuAnalyseUpload"));
//		menuAnalyseUpload.setName("menuAnalyseUpload");
//		menuAnalyseUpload.addActionListener(this.actionAdapter);
		menuCreateTestSetup.setText(LangModelAnalyse.getString("menuCreateTestSetup"));
		menuCreateTestSetup.setName(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_CREATE);
		menuCreateTestSetup.addActionListener(this.actionAdapter);
//		menuLoadTestSetup.setText(LangModelAnalyse.getString("menuLoadTestSetup"));
//		menuLoadTestSetup.setName("menuLoadTestSetup");
//		menuLoadTestSetup.addActionListener(this.actionAdapter);
		menuSaveTestSetup.setText(LangModelAnalyse.getString("menuSaveTestSetup"));
		menuSaveTestSetup.setName(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE);
		menuSaveTestSetup.addActionListener(this.actionAdapter);
		menuSaveTestSetupAs.setText(LangModelAnalyse.getString("menuSaveTestSetupAs"));
		menuSaveTestSetupAs.setName(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE_AS);
		menuSaveTestSetupAs.addActionListener(this.actionAdapter);
//		menuNetStudy.setText(LangModelAnalyse.getString("menuNetStudy"));
//		menuNetStudy.setName("menuNetStudy");
//		menuNetStudy.addActionListener(this.actionAdapter);
		menuTestSetup.add(menuCreateTestSetup);
//		menuTestSetup.add(menuLoadTestSetup);
		menuTestSetup.addSeparator();
		menuTestSetup.add(menuSaveTestSetup);
		menuTestSetup.add(menuSaveTestSetupAs);

//		menuOptions.setText(LangModelAnalyse.getString("menuOptions"));
//		menuOptions.setName("menuOptions");
//		menuOptionsColor.setText(LangModelAnalyse.getString("menuOptionsColor"));
//		menuOptionsColor.setName("menuOptionsColor");
//		menuOptionsColor.addActionListener(this.actionAdapter);
//		menuOptions.add(menuOptionsColor);

		menuReport.setText(LangModelAnalyse.getString("menuReport"));
		menuReport.setName(AnalyseApplicationModel.MENU_REPORT);
		menuReportCreate.setText(LangModelAnalyse.getString("menuReportCreate"));
		menuReportCreate.setName(AnalyseApplicationModel.MENU_REPORT_CREATE);
		menuReportCreate.addActionListener(this.actionAdapter);
		menuReport.add(menuReportCreate);

		menuWindow.setText(LangModelAnalyse.getString("menuWindow"));
		menuWindow.setName(ApplicationModel.MENU_VIEW);
		menuWindowArrange.setText(LangModelAnalyse.getString("menuWindowArrange"));
		menuWindowArrange.setName(ApplicationModel.MENU_VIEW_ARRANGE);
		menuWindowArrange.addActionListener(this.actionAdapter);
		menuWindowTraceSelector.setText(LangModelAnalyse.getString("menuWindowTraceSelector"));
		menuWindowTraceSelector.setName(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR);
		menuWindowTraceSelector.addActionListener(this.actionAdapter);
		menuWindowPrimaryParameters.setText(LangModelAnalyse.getString("menuWindowPrimaryParameters"));
		menuWindowPrimaryParameters.setName(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS);
		menuWindowPrimaryParameters.addActionListener(this.actionAdapter);
		menuWindowOverallStats.setText(LangModelAnalyse.getString("menuWindowOverallStats"));
		menuWindowOverallStats.setName(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS);
		menuWindowOverallStats.addActionListener(this.actionAdapter);
		menuWindowNoiseFrame.setText(LangModelAnalyse.getString("menuWindowNoiseFrame"));
		menuWindowNoiseFrame.setName(AnalyseApplicationModel.MENU_WINDOW_NOISE);
		menuWindowNoiseFrame.addActionListener(this.actionAdapter);
		menuWindowFilteredFrame.setText(LangModelAnalyse.getString("menuWindowFilteredFrame"));
		menuWindowFilteredFrame.setName(AnalyseApplicationModel.MENU_WINDOW_FILTERED);
		menuWindowFilteredFrame.addActionListener(this.actionAdapter);
		menuWindowEvents.setText(LangModelAnalyse.getString("menuWindowEvents"));
		menuWindowEvents.setName(AnalyseApplicationModel.MENU_WINDOW_EVENTS);
		menuWindowEvents.addActionListener(this.actionAdapter);
		menuWindowDetailedEvents.setText(LangModelAnalyse.getString("menuWindowDetailedEvents"));
		menuWindowDetailedEvents.setName(AnalyseApplicationModel.MENU_WINDOW_DETAILEDEVENTS);
		menuWindowDetailedEvents.addActionListener(this.actionAdapter);
		menuWindowAnalysis.setText(LangModelAnalyse.getString("menuWindowAnalysis"));
		menuWindowAnalysis.setName(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS);
		menuWindowAnalysis.addActionListener(this.actionAdapter);
		menuWindowMarkersInfo.setText(LangModelAnalyse.getString("menuWindowMarkersInfo"));
		menuWindowMarkersInfo.setName(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO);
		menuWindowMarkersInfo.addActionListener(this.actionAdapter);
		menuWindowAnalysisSelection.setText(LangModelAnalyse.getString("menuWindowAnalysisSelection"));
		menuWindowAnalysisSelection.setName(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION);
		menuWindowAnalysisSelection.addActionListener(this.actionAdapter);
		menuWindowDerivHistoFrame.setText(LangModelAnalyse.getString("menuWindowDerivHistoFrame"));
		menuWindowDerivHistoFrame.setName(AnalyseApplicationModel.MENU_WINDOW_HISTOGRAMM);
		menuWindowDerivHistoFrame.addActionListener(this.actionAdapter);
		menuWindowThresholdsSelection.setText(LangModelAnalyse.getString("menuWindowThresholdsSelection"));
		menuWindowThresholdsSelection.setName(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDSSELECTION);
		menuWindowThresholdsSelection.addActionListener(this.actionAdapter);
		menuWindowThresholds.setText(LangModelAnalyse.getString("menuWindowThresholds"));
		menuWindowThresholds.setName(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDS);
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

		this.add(menuFile);
		this.add(menuTrace);
		this.add(menuTestSetup);
		this.add(menuReport);
		this.add(menuWindow);
		
		this.addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e});
			}

			public void modelChanged(String e[]) {
				menuFileOpen.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_OPEN));
				menuFileOpen.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_OPEN));
				menuFileOpenAs.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_OPEN_AS));
				menuFileOpenAs.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_OPEN_AS));
				menuFileOpenAsBellcore.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_OPEN_BELLCORE));
				menuFileOpenAsBellcore.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_OPEN_BELLCORE));
				menuFileOpenAsWavetek.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_OPEN_WAVETEK));
				menuFileOpenAsWavetek.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_OPEN_WAVETEK));
				menuFileSave.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_SAVE));
				menuFileSave.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_SAVE));
				menuFileSaveAs.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_SAVE_AS));
				menuFileSaveAs.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_AS));
				menuFileSaveAsText.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_SAVE_TEXT));
				menuFileSaveAsText.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_TEXT));
				menuFileSaveAll.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_SAVE_ALL));
				menuFileSaveAll.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_SAVE_ALL));
				menuFileClose.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_CLOSE));
				menuFileClose.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_CLOSE));
				menuFileAddCompare.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE));
				menuFileAddCompare.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE));
				menuFileRemoveCompare.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE));
				menuFileRemoveCompare.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE));

				menuTraceAddCompare.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE));
				menuTraceAddCompare.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE));
				menuTraceRemoveCompare.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE));
				menuTraceRemoveCompare.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE));

//				menuAnalyseUpload.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuAnalyseUpload"));
//				menuAnalyseUpload.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuAnalyseUpload"));

				menuTraceDownload.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD));
				menuTraceDownload.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD));
//				menuTraceDownloadEtalon.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuTraceDownloadEtalon"));
//				menuTraceDownloadEtalon.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuTraceDownloadEtalon"));
				menuMakeCurrentTracePrimary.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_TRACE_CURRENT_MAKE_PRIMARY));
				menuMakeCurrentTracePrimary.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_TRACE_CURRENT_MAKE_PRIMARY));
				menuTraceClose.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_FILE_CLOSE));
				menuTraceClose.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_FILE_CLOSE));
//				menuTraceCloseEtalon.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuTraceCloseEtalon"));
//				menuTraceCloseEtalon.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuTraceCloseEtalon"));
				menuTraceSavePES.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_TRACE_SAVE_PATHELEMENTS));
				menuTraceSavePES.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_TRACE_SAVE_PATHELEMENTS));

				menuTestSetup.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_MEASUREMENTSETUP));
				menuTestSetup.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP));
				menuCreateTestSetup.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_CREATE));
				menuCreateTestSetup.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_CREATE));
//				menuLoadTestSetup.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuLoadTestSetup"));
//				menuLoadTestSetup.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuLoadTestSetup"));
				menuSaveTestSetup.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE));
				menuSaveTestSetup.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE));
				menuSaveTestSetupAs.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE_AS));
				menuSaveTestSetupAs.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_MEASUREMENTSETUP_SAVE_AS));
//				menuNetStudy.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible("menuNetStudy"));
//				menuNetStudy.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled("menuNetStudy"));

				menuReport.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_REPORT));
				menuReport.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_REPORT));
				menuReportCreate.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_REPORT_CREATE));
				menuReportCreate.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_REPORT_CREATE));

				menuWindow.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(ApplicationModel.MENU_VIEW));
				menuWindow.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(ApplicationModel.MENU_VIEW));
				menuWindowArrange.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(ApplicationModel.MENU_VIEW_ARRANGE));
				menuWindowArrange.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(ApplicationModel.MENU_VIEW_ARRANGE));
				menuWindowTraceSelector.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR));
				menuWindowTraceSelector.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR));
				menuWindowPrimaryParameters.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS));
				menuWindowPrimaryParameters.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS));
				menuWindowOverallStats.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS));
				menuWindowOverallStats.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_OVERALLSTATS));
				menuWindowNoiseFrame.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_NOISE));
				menuWindowNoiseFrame.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_NOISE));
				menuWindowFilteredFrame.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_FILTERED));
				menuWindowFilteredFrame.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_FILTERED));
				menuWindowEvents.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_EVENTS));
				menuWindowEvents.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_EVENTS));
				menuWindowDetailedEvents.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_DETAILEDEVENTS));
				menuWindowDetailedEvents.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_DETAILEDEVENTS));
				menuWindowAnalysis.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS));
				menuWindowAnalysis.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS));
				menuWindowMarkersInfo.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO));
				menuWindowMarkersInfo.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_MARKERSINFO));
				menuWindowAnalysisSelection.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION));
				menuWindowAnalysisSelection.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSISSELECTION));
				menuWindowDerivHistoFrame.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_HISTOGRAMM));
				menuWindowDerivHistoFrame.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_HISTOGRAMM));
				menuWindowThresholdsSelection.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDSSELECTION));
				menuWindowThresholdsSelection.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDSSELECTION));
				menuWindowThresholds.setVisible(AnalyseMainMenuBar.this.getApplicationModel().isVisible(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDS));
				menuWindowThresholds.setEnabled(AnalyseMainMenuBar.this.getApplicationModel().isEnabled(AnalyseApplicationModel.MENU_WINDOW_THRESHOLDS));
			}
		}
			);
	}

	
}
