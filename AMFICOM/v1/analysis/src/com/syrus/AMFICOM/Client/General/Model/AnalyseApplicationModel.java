
package com.syrus.AMFICOM.Client.General.Model;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;

public class AnalyseApplicationModel extends ApplicationModel {

	public static final String SELECT_PREVIOUS_EVENT = "SelectPreviousEvent";
	public static final String SELECT_NEXT_EVENT = "SelectNextEvent";
	
	public AnalyseApplicationModel() {
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
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_INITIAL_ANALYSIS, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/cs_initial.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_DEFAULT_ANALYSIS, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/cs_default.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_TRACE, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/download_trace.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_ADD, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/download_add.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_REMOVE, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/download_remove.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_MARKER, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/marker.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_DELETE_MARKER, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/marker_delete.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/threshold.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_ENLARGE_X, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/enlargex.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_ENLARGE_Y, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/enlargey.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_REDUCE_X, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/reducex.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_REDUCE_Y, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/reducey.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_FIT, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/fit.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_ZOOM_BOX, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/zoom_box.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INITIAL, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/th_initial.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DEFAULT, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/th_default.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INCREASE, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/increaseThresh.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DECREASE, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/decreaseThresh.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_EVENTS, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/events.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_MODELED, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/modeled.gif")));

		UIDefaults defaults = UIManager.getLookAndFeelDefaults();

		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_NEW_SELECTED, Color.MAGENTA);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_NEW, Color.RED);
		
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED_SELECTED, Color.ORANGE);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED, Color.CYAN);		
		// maybe yellow is better that cyan?
		
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED, Color.ORANGE);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED_SELECTED, Color.CYAN); 
		// maybe yellow is better that cyan?
		
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS, Color.BLACK);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_SELECTED, Color.BLACK);
		
		
		defaults.put(AnalysisResourceKeys.COLOR_CONNECTOR, new Color (64, 180, 255));
		defaults.put(AnalysisResourceKeys.COLOR_END, new Color(160,32,255));
		defaults.put(AnalysisResourceKeys.COLOR_MARKER, Color.BLACK);
		
		defaults.put(RefUpdateEvent.PRIMARY_TRACE, Color.BLUE);		
	}
	/*
	 * public DataSourceInterface getDataSource(SessionInterface si) { String
	 * connection = Environment.getConnectionType();
	 * if(connection.equals("RISD")) //return new RISDSurveyDataSource(si);
	 * return new RISDSurveyDataSource(si); else if(connection.equals("Empty"))
	 * return new EmptySurveyDataSource(si); return null; }
	 */
}