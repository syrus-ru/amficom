package com.syrus.AMFICOM.Client.General.Model;

import java.awt.Toolkit;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.resource.ModelResourceKeys;

public class ModelApplicationModel extends ApplicationModel
{
	private static final String MENU = "Menu";
	public static final String MENU_APPEARANCE = MENU + ".Appearance";
	public static final String MENU_APPEARANCE_SCHEME = MENU_APPEARANCE + ".Scheme";
	public static final String MENU_APPEARANCE_MAP = MENU_APPEARANCE + ".Map";
	
	private static final String MENU_WINDOW = "Menu.window";
	public static final String MENU_WINDOW_TREE = MENU_WINDOW + ".tree";
	public static final String MENU_WINDOW_TRANS_DATA = MENU_WINDOW + ".trans_data";
	public static final String MENU_WINDOW_MODEL_PARAMETERS = MENU_WINDOW + ".model_parameters";
	public static final String MENU_WINDOW_SCHEME = MENU_WINDOW + ".scheme";
	public static final String MENU_WINDOW_MAP = MENU_WINDOW + ".map";
	public static final String MENU_WINDOW_GENERAL_PROPERTIES = MENU_WINDOW + ".general_properties";
	public static final String MENU_WINDOW_ADDITIONAL_PROPERTIES = MENU_WINDOW + ".additional_properties";
	public static final String MENU_WINDOW_CHARACTERISTICS = MENU_WINDOW + ".characteristics";
	
	public ModelApplicationModel()
	{
		super();

    add(ApplicationModel.MENU_SESSION);
    add(ApplicationModel.MENU_SESSION_NEW);
    add(ApplicationModel.MENU_SESSION_CLOSE);
    add(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD);
    add(ApplicationModel.MENU_EXIT);

    add(ApplicationModel.MENU_HELP);
    add(ApplicationModel.MENU_HELP_ABOUT);
    
    add(ApplicationModel.MENU_VIEW_ARRANGE);

		add("menuView");
		add("menuViewMapViewOpen");
		add("menuViewMapViewEdit");
		add("menuViewMapViewClose");
		add("menuViewSchemeOpen");
		add("menuViewSchemeEdit");
		add("menuViewSchemeClose");
		add("menuViewModelSave");
		add("menuViewModelLoad");

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

		add("menuTrace");
		add("menuTraceAddCompare");
		add("menuTraceRemoveCompare");
		add("menuTraceClose");

		add("menuReport");
		add("menuReportCreate");
		
		add(MENU_VIEW);
		add(MENU_WINDOW_ADDITIONAL_PROPERTIES);
		add(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS);
		add(MENU_WINDOW_CHARACTERISTICS);
		add(MENU_WINDOW_GENERAL_PROPERTIES);
		add(MENU_WINDOW_MAP);
		add(MENU_WINDOW_MODEL_PARAMETERS);
		add(MENU_WINDOW_TREE);
		add(MENU_WINDOW_SCHEME);
		add(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR);
		add(MENU_WINDOW_TRANS_DATA);
		
		initUIConstats();
		I18N.addResourceBundle("com.syrus.AMFICOM.resource.modeling");
		I18N.addResourceBundle("com.syrus.AMFICOM.resource.analysis");
		I18N.addResourceBundle("com.syrus.AMFICOM.client.resource.map");
		I18N.addResourceBundle("com.syrus.AMFICOM.client.report.report");
		I18N.addResourceBundle("com.syrus.AMFICOM.resource.SchemeMessages");
		I18N.addResourceBundle("com.syrus.AMFICOM.client_.scheme.graph.messages");
	}
	
	private void initUIConstats() {
		UIManager.put(ModelResourceKeys.ICON_MODEL_MAIN, Toolkit
				.getDefaultToolkit().getImage("images/main/model_mini.gif"));
	}

}