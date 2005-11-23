
package com.syrus.AMFICOM.Client.General.Model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;

public class AnalyseApplicationModel extends ApplicationModel {

	private static final String MENU = "Menu";
	
	public static final String MENU_FILE = MENU + ".File";
	public static final String MENU_FILE_OPEN = MENU_FILE + ".Open";
	public static final String MENU_FILE_OPEN_AS = MENU_FILE + ".OpenAs";
	public static final String MENU_FILE_OPEN_BELLCORE = MENU_FILE_OPEN + ".Bellcore";
	public static final String MENU_FILE_OPEN_WAVETEK = MENU_FILE_OPEN + ".Wavetek";
	public static final String MENU_FILE_SAVE = MENU_FILE + ".Save";
	public static final String MENU_FILE_SAVE_AS = MENU_FILE + ".SaveAs";
	public static final String MENU_FILE_SAVE_TEXT = MENU_FILE_SAVE + ".Text";
	public static final String MENU_FILE_SAVE_ALL = MENU_FILE_SAVE + ".All";
	public static final String MENU_FILE_CLOSE = MENU_FILE + ".Close";
	public static final String MENU_FILE_ADD_COMPARE = MENU_FILE + ".Add_compare";
	public static final String MENU_FILE_REMOVE_COMPARE = MENU_FILE + ".Remove_compare";
	
	public static final String MENU_TRACE = MENU + ".Trace";
	public static final String MENU_TRACE_DOWNLOAD = MENU_TRACE + ".Download";
	public static final String MENU_TRACE_ADD_COMPARE = MENU_TRACE + ".Add_compare";
	public static final String MENU_TRACE_REMOVE_COMPARE = MENU_TRACE + ".Remove_compare";
	public static final String MENU_TRACE_SAVE_PATHELEMENTS = MENU_TRACE + ".Save_PES";
	public static final String MENU_TRACE_CURRENT_MAKE_PRIMARY = MENU_TRACE + ".CurrentMakePrimary";
	public static final String MENU_TRACE_CHECK_MISMATCH = MENU_TRACE + ".CheckMismatch";
	
	public static final String MENU_MEASUREMENTSETUP = MENU + ".Measurementsetup";
	public static final String MENU_MEASUREMENTSETUP_CREATE = MENU_MEASUREMENTSETUP + ".Create";
	public static final String MENU_MEASUREMENTSETUP_SAVE = MENU_MEASUREMENTSETUP + ".Save";
	public static final String MENU_MEASUREMENTSETUP_SAVE_AS = MENU_MEASUREMENTSETUP + ".SaveAs";
	
	public static final String MENU_REPORT = MENU + ".Report";
	public static final String MENU_REPORT_CREATE = MENU_REPORT + ".Create";
	
	private static final String MENU_WINDOW = MENU + ".Window";
	public static final String MENU_WINDOW_TRACESELECTOR = MENU_WINDOW + ".TraceSelector";
	public static final String MENU_WINDOW_PRIMARYPARAMETERS = MENU_WINDOW + ".PrimaryParameters";
	public static final String MENU_WINDOW_OVERALLSTATS = MENU_WINDOW + ".OverallStats";
	public static final String MENU_WINDOW_NOISE = MENU_WINDOW + ".Noise";
	public static final String MENU_WINDOW_FILTERED = MENU_WINDOW + ".Filtered";
	public static final String MENU_WINDOW_EVENTS = MENU_WINDOW + ".Events";
	public static final String MENU_WINDOW_DETAILEDEVENTS = MENU_WINDOW + ".DetailedEvents";
	public static final String MENU_WINDOW_ANALYSIS = MENU_WINDOW + ".Analysis";
	public static final String MENU_WINDOW_MARKERSINFO = MENU_WINDOW + ".MarkersInfo";
	public static final String MENU_WINDOW_ANALYSISSELECTION = MENU_WINDOW + ".AnalysisSelection";
	public static final String MENU_WINDOW_HISTOGRAMM = MENU_WINDOW + ".Histogramm";
	public static final String MENU_WINDOW_THRESHOLDS = MENU_WINDOW + ".Thresholds";
	public static final String MENU_WINDOW_THRESHOLDSSELECTION = MENU_WINDOW + ".ThresholdsSelection";
	
	public AnalyseApplicationModel() {
		
	  add(MENU_SESSION);
	  add(MENU_SESSION_NEW);
	  add(MENU_SESSION_CLOSE);
	  add(MENU_SESSION_CHANGE_PASSWORD);
	  add(MENU_EXIT);

	  add(MENU_VIEW);
	  add(MENU_VIEW_ARRANGE);
	  
	  add(MENU_HELP);
	  add(MENU_HELP_ABOUT);

		add(MENU_FILE);
		add(MENU_FILE_OPEN);
		add(MENU_FILE_OPEN_AS);
		add(MENU_FILE_OPEN_BELLCORE);
		add(MENU_FILE_OPEN_WAVETEK);
		add(MENU_FILE_SAVE);
		add(MENU_FILE_SAVE_ALL);
		add(MENU_FILE_SAVE_AS);
		add(MENU_FILE_SAVE_TEXT);
		add(MENU_FILE_CLOSE);
		add(MENU_FILE_ADD_COMPARE);
		add(MENU_FILE_REMOVE_COMPARE);

		add(MENU_TRACE);
		add(MENU_TRACE_ADD_COMPARE);
		add(MENU_TRACE_REMOVE_COMPARE);
		add(MENU_TRACE_DOWNLOAD);
//		add("menuTraceDownloadEtalon"); // нет
//		add("menuTraceCloseEtalon"); // нет
//		add("menuTraceReferenceClose"); // нет
		add(MENU_TRACE_SAVE_PATHELEMENTS);

		add(MENU_MEASUREMENTSETUP);
		add(MENU_MEASUREMENTSETUP_CREATE);
		add(MENU_MEASUREMENTSETUP_SAVE);
		add(MENU_MEASUREMENTSETUP_SAVE_AS);
//		add("menuLoadTestSetup"); // нет
//		add("menuAnalyseUpload"); // нет
//		add("menuNetStudy"); // нет

//		add("menuOptions"); // нет
//		add("menuOptionsColor"); // нет

		add(MENU_REPORT);
		add(MENU_REPORT_CREATE);

		add(MENU_WINDOW_TRACESELECTOR);
		add(MENU_WINDOW_PRIMARYPARAMETERS);
		add(MENU_WINDOW_OVERALLSTATS);
		add(MENU_WINDOW_NOISE);
		add(MENU_WINDOW_FILTERED);
		add(MENU_WINDOW_EVENTS);
		add(MENU_WINDOW_DETAILEDEVENTS);
		add(MENU_WINDOW_ANALYSIS);
		add(MENU_WINDOW_MARKERSINFO);
		add(MENU_WINDOW_ANALYSISSELECTION);
		add(MENU_WINDOW_HISTOGRAMM);
		add(MENU_WINDOW_THRESHOLDS);
		add(MENU_WINDOW_THRESHOLDSSELECTION);


//		add("menuBar"); // нет
//		add("toolBar"); // нет
//		add("statusBar"); // нет
//		add("reflectometryFrame"); // нет

//	    add(ApplicationModel.MENU_WINDOW_ARRANGE); // ??
//	    add("menuHelpAbout"); // ??

		add(MENU_TRACE_CHECK_MISMATCH);
		add(MENU_TRACE_CURRENT_MAKE_PRIMARY);
		this.initUIConstats();
		I18N.addResourceBundle("com.syrus.AMFICOM.client.report.report");
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
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_CHECK_MISMATCH, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/check_mismatch.gif")));
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
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_CREATE_NEW, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/th_createNew.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INCREASE, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/increaseThresh.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DECREASE, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/decreaseThresh.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_EVENTS, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/events.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_MODELED, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		"images/modeled.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_TRACE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		"images/original_trace.gif")));
		
		UIManager.put(AnalysisResourceKeys.ICON_SHOW_PATH_ELEMENTS, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		"images/jdirectory.gif")));

		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_GAIN, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/gain.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_SPLICE_GAIN, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/splicegain.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_BEND_LOSS, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/bendloss.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_SPLICE_LOSS, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/spliceloss.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_SINGULARITY, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/singularity.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_REFLECTION, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/reflection.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_DEADZONE, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/deadzone.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_END, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/end.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANALYSIS_BREAK, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/break.gif")));
		UIManager.put(AnalysisResourceKeys.ICON_ANCHORED, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/opt_start.gif"))); // XXX: use own anchor icon (maybe use opt_start colored with red)

		UIDefaults defaults = UIManager.getLookAndFeelDefaults();

		defaults.put(AnalysisResourceKeys.COLOR_PRIMARY_TRACE, Color.BLUE);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_NEW_SELECTED, Color.MAGENTA);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_NEW, Color.RED);

		Color alarm = new Color(255,120,120);
		Color alarmSelection = new Color(255,0,0);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_ALARM, alarm);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_ALARM_SELECTED, alarmSelection);

		Color loss = new Color(0,135,0);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_LOST_SELECTED, loss);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_LOST, loss);

		Color lossChanged = new Color(224,120,40);
		Color lossChangedSelection = new Color(160,96,0);

		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED, lossChanged);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED_SELECTED, lossChangedSelection);

		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED, lossChanged);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED_SELECTED, lossChangedSelection); 

		defaults.put(AnalysisResourceKeys.COLOR_EVENTS, Color.BLACK);
		defaults.put(AnalysisResourceKeys.COLOR_EVENTS_SELECTED, Color.BLACK);
		
		
		defaults.put(AnalysisResourceKeys.COLOR_CONNECTOR, new Color (64, 180, 255));
		defaults.put(AnalysisResourceKeys.COLOR_END, new Color(160,32,255));
		defaults.put(AnalysisResourceKeys.COLOR_MARKER, Color.BLACK);
		
		defaults.put(AnalysisResourceKeys.COLOR_DEADZONE, new Color(192, 32, 255));
		defaults.put(AnalysisResourceKeys.COLOR_WELD, new Color(0, 220, 16));
		defaults.put(AnalysisResourceKeys.COLOR_LINEZONE, new Color(0, 0, 255));
		defaults.put(AnalysisResourceKeys.COLOR_NON_ID, new Color (200,9,0));
		defaults.put(AnalysisResourceKeys.COLOR_NOISE, new Color(160, 160, 160));
		
		defaults.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 0, new Color(0, 128, 128));
		defaults.put(Heap.PRIMARY_TRACE_KEY, Color.BLUE);
		defaults.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 1, new Color(128, 64, 0));
		defaults.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 2, new Color(128, 0, 128));
		defaults.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 3, new Color(0, 96, 0));
		defaults.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 4, new Color(0, 64, 128));
		defaults.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 5, new Color(128, 128, 0));
		
		defaults.put(AnalysisResourceKeys.COLOR_WARNING_THRESHOLD, new Color(255, 220, 0));
		defaults.put(AnalysisResourceKeys.COLOR_ALARM_THRESHOLD, new Color(255, 150, 60));
		
		defaults.put(AnalysisResourceKeys.COLOR_MODELED, new Color (0, 192, 0));
		defaults.put(AnalysisResourceKeys.COLOR_MIN_TRACE_LEVEL, new Color(255, 64, 64));
		defaults.put(AnalysisResourceKeys.COLOR_SCALE, Color.LIGHT_GRAY);
		defaults.put(AnalysisResourceKeys.COLOR_SCALE_DIGITS, Color.BLACK);
		defaults.put(AnalysisResourceKeys.COLOR_SELECT, Color.GRAY); 
		
		defaults.put(AnalysisResourceKeys.STROKE_NOISE_HISTOGRAMM, new BasicStroke(1.5f));
		defaults.put(AnalysisResourceKeys.STROKE_DEFAULT, new BasicStroke(1));
	}
	/*
	 * public DataSourceInterface getDataSource(SessionInterface si) { String
	 * connection = Environment.getConnectionType();
	 * if(connection.equals("RISD")) //return new RISDSurveyDataSource(si);
	 * return new RISDSurveyDataSource(si); else if(connection.equals("Empty"))
	 * return new EmptySurveyDataSource(si); return null; }
	 */
}
