package com.syrus.AMFICOM.Client.General.Model;

import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class AnalyseApplicationModel extends ApplicationModel
{

	public AnalyseApplicationModel()
	{
		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionDomain");
		add("menuExit");

		add("menuFileOpen");
		add("menuFileOpenAs");
		add("menuFileOpenAsBellcore");
		add("menuFileOpenAsWavetek");
		add("menuFileSave");
		add("menuFileSaveAll");
		add("menuFileSaveAs");
		add("menuFileSaveAsText");
		add("menuFileClose");
		add("menuFileAddCompare");
		add("menuFileRemoveCompare");
		add("menuExit");


		add("menuTrace");
		add("menuTraceAddCompare");
		add("menuTraceRemoveCompare");
		add("menuTraceDownload");
		add("menuTraceDownloadEtalon");
		add("menuTraceCloseEtalon");
		add("menuTraceClose");
		add("menuTraceReferenceClose");

		add("menuCreateTestSetup");
		add("menuSaveTestSetup");
		add("menuSaveTestSetupAs");
		add("menuAnalyseUpload");
		add("menuLoadTestSetup");
		add("menuTestSetup");
		add("menuNetStudy");

		add("menuOptions");
		add("menuOptionsColor");

		add("menuReport");
		add("menuReportCreate");

		add("menuWindow");
		add("menuWindowArrange");
		add("menuWindowTraceSelector");
		add("menuWindowPrimaryParameters");
		add("menuWindowOverallStats");
		add("menuWindowNoiseFrame");
		add("menuWindowFilteredFrame");
		add("menuWindowEvents");
		add("menuWindowDetailedEvents");
		add("menuWindowAnalysis");
		add("menuWindowMarkersInfo");
		add("menuWindowAnalysisSelection");
		add("menuWindowDerivHistoFrame");
		add("menuWindowThresholdsSelection");
		add("menuWindowThresholds");

		add("menuBar");
		add("toolBar");
		add("statusBar");
		add("reflectometryFrame");
		this.initUIConstats();
	}
	
	private void initUIConstats() {
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_MINI, Toolkit.getDefaultToolkit().getImage(
			"images/main/analyse_mini.gif"));
		UIManager.put(AnalysisResourceKeys.ICON_SURVEY_MINI, Toolkit.getDefaultToolkit().getImage(
			"images/main/survey_mini.gif"));
		UIManager.put(AnalysisResourceKeys.ICON_EVALUATE_MINI, Toolkit.getDefaultToolkit().getImage(
			"images/main/evaluate_mini.gif"));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_CENTER_A, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/centerA.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_CENTER_B, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/centerB.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_LOSS, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/loss.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_REFLECT, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/reflect.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_NOANALYSIS, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/noanalyse.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_PERFORM_ANALYSIS, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/perform_analysis.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_INITIAL_ANALYSIS, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/cs_initial.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_DEFAULT_ANALYSIS, 
		new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cs_default.gif")));
	}
/*
	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equals("RISD"))
			//return new RISDSurveyDataSource(si);
			return new RISDSurveyDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptySurveyDataSource(si);
		return null;
	}*/
}