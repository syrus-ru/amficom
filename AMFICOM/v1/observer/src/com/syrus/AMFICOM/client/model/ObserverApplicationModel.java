package com.syrus.AMFICOM.client.model;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.resource.ObserverResourceKeys;

public class ObserverApplicationModel extends ApplicationModel {
	public static final String MENU_OPEN = "menu.open";
	public static final String MENU_OPEN_SCHEME = "menu.open.scheme";
	public static final String MENU_OPEN_MAP = "menu.open.map";

	public static final String MENU_START = "menu.start";
	public static final String MENU_START_SCHEDULER = "menu.start.scheduling";
	public static final String MENU_START_ANALIZE = "menu.start.analyze";
	public static final String MENU_START_ANALIZE_EXT = "menu.start.analyze_ext";
	public static final String MENU_START_EVALUATION = "menu.start.evaluation";
	public static final String MENU_START_PROGNOSIS = "menu.start.prognosis";
	public static final String MENU_START_MAP_EDITOR = "menu.start.map_edit";
	public static final String MENU_START_SCHEME_EDITOR = "menu.start.scheme_edit";

	public static final String MENU_VIEW_SCHEME = "menu.view.scheme";
	public static final String MENU_VIEW_MAP = "menu.view.map";
	public static final String MENU_VIEW_GENERAL_PROPERTIES = "menu.view.general_properties";
	public static final String MENU_VIEW_ADDITIONAL_PROPERTIES = "menu.view.additional_properties";
	public static final String MENU_VIEW_CHARACTERISTICS = "menu.view.characteristics";
	public static final String MENU_VIEW_RESULTS = "menu.view.results_overview";
	public static final String MENU_VIEW_ALARMS = "menu.view.alarms";
	public static final String MENU_VIEW_NAVIGATOR = "menu.view.results_navigator";

	public static final String MENU_REPORT = "menu.report";
	public static final String MENU_REPORT_BY_TEMPLATE = "menu.report.template";

	public ObserverApplicationModel() {
		super();

		add(ApplicationModel.MENU_SESSION);
		add(ApplicationModel.MENU_SESSION_NEW);
		add(ApplicationModel.MENU_SESSION_CLOSE);
		add(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD);
		add(ApplicationModel.MENU_EXIT);
		
		add(ApplicationModel.MENU_VIEW_ARRANGE);

		add(ObserverApplicationModel.MENU_START);
		add(ObserverApplicationModel.MENU_START_SCHEDULER);
		add(ObserverApplicationModel.MENU_START_ANALIZE);
		add(ObserverApplicationModel.MENU_START_ANALIZE_EXT);
		add(ObserverApplicationModel.MENU_START_EVALUATION);
		add(ObserverApplicationModel.MENU_START_PROGNOSIS);
		add(ObserverApplicationModel.MENU_START_MAP_EDITOR);
		add(ObserverApplicationModel.MENU_START_SCHEME_EDITOR);

		add(ObserverApplicationModel.MENU_OPEN);
		add(ObserverApplicationModel.MENU_OPEN_MAP);
		add(ObserverApplicationModel.MENU_OPEN_SCHEME);

		add(ApplicationModel.MENU_VIEW);
		add(ObserverApplicationModel.MENU_VIEW_SCHEME);
		add(ObserverApplicationModel.MENU_VIEW_MAP);
		add(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES);
		add(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES);
		add(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS);
		add(ObserverApplicationModel.MENU_VIEW_RESULTS);
		add(ObserverApplicationModel.MENU_VIEW_ALARMS);
		add(ObserverApplicationModel.MENU_VIEW_NAVIGATOR);
		add(ApplicationModel.MENU_VIEW_ARRANGE);

		add(ObserverApplicationModel.MENU_REPORT);
		add(ObserverApplicationModel.MENU_REPORT_BY_TEMPLATE);

		add(ApplicationModel.MENU_HELP);
		add(ApplicationModel.MENU_HELP_ABOUT);
		
		this.initUIConstats();
		I18N.addResourceBundle("com.syrus.AMFICOM.client.resource.map");
		I18N.addResourceBundle("com.syrus.AMFICOM.client.report.report");
		I18N.addResourceBundle("com.syrus.AMFICOM.resource.SchemeMessages");
		I18N.addResourceBundle("com.syrus.AMFICOM.client_.scheme.graph.messages");
	}
	
	public static final int IMG_SIZE = 16;

	private void initUIConstats() {
		UIManager.put(ObserverResourceKeys.ICON_OBSERVE, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/main/observe_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_MAP_OPEN, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/main/map_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_SCHEME_OPEN, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/main/schematics_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_ARCHIVE, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/archive_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_ALARM, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/alarm_bell_red.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_RESULT, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/result.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_SCHEDULER, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/main/scheduling_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_ANALYSIS, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/main/analyse_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_SURVEY, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/main/survey_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_EVALUATION, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/main/evaluation_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		UIManager.put(ObserverResourceKeys.ICON_PREDICTION, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/main/prognosis_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
	}
}
