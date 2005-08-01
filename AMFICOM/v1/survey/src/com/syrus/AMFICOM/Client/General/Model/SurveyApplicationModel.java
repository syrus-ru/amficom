package com.syrus.AMFICOM.Client.General.Model;

import java.awt.Toolkit;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.resource.SurveyResourceKeys;

public class SurveyApplicationModel extends ApplicationModel
{
	public SurveyApplicationModel()
	{
		super();

		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionChangePassword");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionDomain");
		add("menuExit");

		add("menuStart");
		add("menuStartScheduler");
		add("menuStartAnalize");
		add("menuStartAnalizeExt");
		add("menuStartEvaluation");
		add("menuStartPrognosis");
		add("menuStartMapEditor");
		add("menuStartSchemeEditor");

		add("menuView");
		add("menuViewMap");
		add("menuViewScheme");
		
		add("menuWindowScheme");
		add("menuWindowMap");
		add("menuWindowProperties");
		add("menuWindowCharacteristics");
		add("menuWindowResults");
		add("menuWindowAlarms");
		add("menuWindowMeasurements");

		add("menuReport");
		add("menuTemplateReport");

		add("menuHelp");
		add("menuHelpContents");
		add("menuHelpFind");
		add("menuHelpTips");
		add("menuHelpStart");
		add("menuHelpCourse");
		add("menuHelpHelp");
		add("menuHelpSupport");
		add("menuHelpLicense");
		add("menuHelpAbout");
		
		add(ApplicationModel.MENU_VIEW_ARRANGE);

		this.initUIConstats();
	}
	
	private void initUIConstats() {
		UIManager.put(SurveyResourceKeys.ICON_OBSERVE, Toolkit
				.getDefaultToolkit().getImage("images/main/observe_mini.gif"));
		UIManager.put(SurveyResourceKeys.ICON_MAP_OPEN, Toolkit
				.getDefaultToolkit().getImage("images/main/map_mini.gif"));
		UIManager.put(SurveyResourceKeys.ICON_SCHEME_OPEN, Toolkit
				.getDefaultToolkit().getImage("images/main/schematics_mini.gif"));
		UIManager.put(SurveyResourceKeys.ICON_ARCHIVE, Toolkit
				.getDefaultToolkit().getImage("images/archive_mini.gif"));
		UIManager.put(SurveyResourceKeys.ICON_ALARM, Toolkit
				.getDefaultToolkit().getImage("images/alarm_bell_red.gif"));
		UIManager.put(SurveyResourceKeys.ICON_RESULT, Toolkit
				.getDefaultToolkit().getImage("images/result.gif"));
		UIManager.put(SurveyResourceKeys.ICON_SCHEDULER, Toolkit
				.getDefaultToolkit().getImage("images/main/scheduling_mini.gif"));
		UIManager.put(SurveyResourceKeys.ICON_ANALYSIS, Toolkit
				.getDefaultToolkit().getImage("images/main/analyse_mini.gif"));
		UIManager.put(SurveyResourceKeys.ICON_SURVEY, Toolkit
				.getDefaultToolkit().getImage("images/main/survey_mini.gif"));
		UIManager.put(SurveyResourceKeys.ICON_EVALUATION, Toolkit
				.getDefaultToolkit().getImage("images/main/evaluation_mini.gif"));
		UIManager.put(SurveyResourceKeys.ICON_PREDICTION, Toolkit
				.getDefaultToolkit().getImage("images/main/prognosis_mini.gif"));
		
		
		
		
		
	}
}

