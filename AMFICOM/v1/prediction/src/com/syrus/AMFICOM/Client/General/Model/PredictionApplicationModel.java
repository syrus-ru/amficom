package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;

public class PredictionApplicationModel extends ApplicationModel {

	private static final String MENU = "Menu";
	
//	public static final String MENU_FILE = MENU + ".File";
//	public static final String MENU_FILE_CLOSE = MENU_FILE + ".Close";
//	
//	public static final String MENU_TRACE = MENU + ".Trace";
//	public static final String MENU_TRACE_ADD_COMPARE = MENU_TRACE + ".Add_compare";
//	public static final String MENU_TRACE_REMOVE_COMPARE = MENU_TRACE + ".Remove_compare";
	
	public static final String MENU_VIEW_DATA_LOAD = MENU_VIEW + ".DataLoad";
	public static final String MENU_VIEW_COUNT_PREDICTION = MENU_VIEW + ".CountPrediction";
	public static final String MENU_VIEW_SAVE_PREDICTION = MENU_VIEW + ".SavePrediction";
	
	public static final String MENU_REPORT = MENU + ".Report";
	public static final String MENU_REPORT_CREATE = MENU_REPORT + ".Create";
	
	private static final String MENU_WINDOW = MENU + ".Window";
	public static final String MENU_WINDOW_TRACESELECTOR = MENU_WINDOW + ".TraceSelector";
	public static final String MENU_WINDOW_PRIMARYPARAMETERS = MENU_WINDOW + ".PrimaryParameters";
	public static final String MENU_WINDOW_OVERALLSTATS = MENU_WINDOW + ".OverallStats";
	public static final String MENU_WINDOW_EVENTS = MENU_WINDOW + ".Events";
	public static final String MENU_WINDOW_ANALYSIS = MENU_WINDOW + ".Analysis";
	public static final String MENU_WINDOW_MARKERSINFO = MENU_WINDOW + ".MarkersInfo";
	public static final String MENU_WINDOW_HISTOGRAMM = MENU_WINDOW + ".Histogramm";
	public static final String MENU_WINDOW_TD_FRAME = MENU_WINDOW + ".TimeDependenceFrame";
	public static final String MENU_WINDOW_TD_TABLE = MENU_WINDOW + ".TimeDependenceTable";
	
	public PredictionApplicationModel() {
		super();

		add(ApplicationModel.MENU_SESSION);
    add(ApplicationModel.MENU_SESSION_NEW);
    add(ApplicationModel.MENU_SESSION_CLOSE);
    add(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD);
    add(ApplicationModel.MENU_EXIT);

    add(ApplicationModel.MENU_HELP);
    add(ApplicationModel.MENU_HELP_ABOUT);
    
    add(ApplicationModel.MENU_VIEW_ARRANGE);

		add(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE);
		add(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE);
		add(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD);
		add(AnalyseApplicationModel.MENU_MODELING_DOWNLOAD);

		add(ApplicationModel.MENU_VIEW);
		add(MENU_VIEW_DATA_LOAD);
		add(MENU_VIEW_COUNT_PREDICTION);
		add(MENU_VIEW_SAVE_PREDICTION);
		
		add(MENU_REPORT);
		add(MENU_REPORT_CREATE);
		
		add(MENU_WINDOW);
		add(MENU_WINDOW_TRACESELECTOR);
		add(MENU_WINDOW_PRIMARYPARAMETERS);
		add(MENU_WINDOW_OVERALLSTATS);
		add(MENU_WINDOW_EVENTS);
		add(MENU_WINDOW_ANALYSIS);
		add(MENU_WINDOW_MARKERSINFO);
		add(MENU_WINDOW_HISTOGRAMM);
		add(MENU_WINDOW_TD_FRAME);
		add(MENU_WINDOW_TD_TABLE);
		
		I18N.addResourceBundle("com.syrus.AMFICOM.resource.predictionMessages");
	}
}