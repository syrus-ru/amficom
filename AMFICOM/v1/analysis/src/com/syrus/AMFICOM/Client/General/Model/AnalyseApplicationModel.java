
package com.syrus.AMFICOM.Client.General.Model;

import java.awt.BasicStroke;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.extensions.ExtensionLauncher;
import com.syrus.AMFICOM.resources.ResourceHandler;

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
		final ExtensionLauncher extensionLauncher = ExtensionLauncher.getInstance();
		final ClassLoader classLoader = AnalyseApplicationModel.class.getClassLoader();
		extensionLauncher.addExtensions(classLoader.getResource("xml/resources.xml"));
		extensionLauncher.getExtensionHandler(ResourceHandler.class.getName()); 
		// XXX: use own anchor icon (maybe use opt_start colored with red)

		UIDefaults defaults = UIManager.getLookAndFeelDefaults();

		defaults.put(AnalysisResourceKeys.STROKE_NOISE_HISTOGRAMM, new BasicStroke(1.5f));
		defaults.put(AnalysisResourceKeys.STROKE_DEFAULT, new BasicStroke(1));
	}
}
