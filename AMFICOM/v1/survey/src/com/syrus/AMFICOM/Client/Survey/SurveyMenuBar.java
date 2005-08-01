package com.syrus.AMFICOM.Client.Survey;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.resource.LangModelSurvey;

public class SurveyMenuBar extends AbstractMainMenuBar
{
	public SurveyMenuBar(ApplicationModel aModel)
	{
		super(aModel);
	}

	protected void addMenuItems() {
		
		final JMenu menuStart = new JMenu();
		final JMenuItem menuStartScheduler = new JMenuItem();
		final JMenuItem menuStartAnalize = new JMenuItem();
		final JMenuItem menuStartAnalizeExt = new JMenuItem();
		final JMenuItem menuStartEvaluation = new JMenuItem();
		final JMenuItem menuStartPrognosis = new JMenuItem();
		final JMenuItem menuStartMapEditor = new JMenuItem();
		final JMenuItem menuStartSchemeEditor = new JMenuItem();
		
		final JMenu menuView = new JMenu();
		final JMenuItem menuViewMap = new JMenuItem();
		final JMenuItem menuViewScheme = new JMenuItem();
		
		final JMenuItem menuWindowMap = new JMenuItem();
		final JMenuItem menuWindowScheme = new JMenuItem();
		final JMenuItem menuWindowMeasurements = new JMenuItem();
		final JMenuItem menuWindowCharacteristics = new JMenuItem();
		final JMenuItem menuWindowProperties = new JMenuItem();
		final JMenuItem menuWindowResults = new JMenuItem();
		final JMenuItem menuWindowAlarms = new JMenuItem();
		
		final JMenu menuReport = new JMenu();
		final JMenuItem menuTemplateReport = new JMenuItem();

		menuStart.setText(LangModelSurvey.getString("menu.start"));
		menuStart.setName("menuStart");
		menuStartScheduler.setText(LangModelSurvey.getString("menu.start.scheduling"));
		menuStartScheduler.setName("menuStartScheduler");
		menuStartScheduler.addActionListener(actionAdapter);
		menuStartAnalize.setText(LangModelSurvey.getString("menu.start.analyze"));
		menuStartAnalize.setName("menuStartAnalize");
		menuStartAnalize.addActionListener(actionAdapter);
		menuStartAnalizeExt.setText(LangModelSurvey.getString("menu.start.analyze_ext"));
		menuStartAnalizeExt.setName("menuStartAnalizeExt");
		menuStartAnalizeExt.addActionListener(actionAdapter);
		menuStartEvaluation.setText(LangModelSurvey.getString("menu.start.evaluation"));
		menuStartEvaluation.setName("menuStartEvaluation");
		menuStartEvaluation.addActionListener(actionAdapter);
		menuStartPrognosis.setText(LangModelSurvey.getString("menu.start.prediction"));
		menuStartPrognosis.setName("menuStartPrognosis");
		menuStartPrognosis.addActionListener(actionAdapter);
		menuStartMapEditor.setText(LangModelSurvey.getString("menu.view.map_edit"));
		menuStartMapEditor.setName("menuStartMapEditor");
		menuStartMapEditor.addActionListener(actionAdapter);
		menuStartSchemeEditor.setText(LangModelSurvey.getString("menu.view.scheme_edit"));
		menuStartSchemeEditor.setName("menuStartSchemeEditor");
		menuStartSchemeEditor.addActionListener(actionAdapter);

		menuView.setText(LangModelSurvey.getString("menu.view"));
		menuView.setName("menuView");
		menuViewMap.setText(LangModelSurvey.getString("menu.view.map_open"));
		menuViewMap.setName("menuViewMap");
		menuViewMap.addActionListener(actionAdapter);
		menuViewScheme.setText(LangModelSurvey.getString("menu.view.scheme_open"));
		menuViewScheme.setName("menuViewScheme");
		menuViewScheme.addActionListener(actionAdapter);
		
		
		menuWindowScheme.setText(LangModelSurvey.getString("menu.view.scheme"));
		menuWindowScheme.setName("menuWindowScheme");
		menuWindowScheme.addActionListener(actionAdapter);
		menuWindowMap.setText(LangModelSurvey.getString("menu.view.map"));
		menuWindowMap.setName("menuWindowMap");
		menuWindowMap.addActionListener(actionAdapter);
		menuWindowProperties.setText(LangModelSurvey.getString("menu.view.properties"));
		menuWindowProperties.setName("menuWindowProperties");
		menuWindowProperties.addActionListener(actionAdapter);
		menuWindowCharacteristics.setText(LangModelSurvey.getString("menu.view.characteristics"));
		menuWindowCharacteristics.setName("menuWindowCharacteristics");
		menuWindowCharacteristics.addActionListener(actionAdapter);
		menuWindowMeasurements.setText(LangModelSurvey.getString("menu.view.measurement_archive"));
		menuWindowMeasurements.setName("menuWindowMeasurements");
		menuWindowMeasurements.addActionListener(actionAdapter);
		menuWindowResults.setText(LangModelSurvey.getString("menu.view.results_overview"));
		menuWindowResults.setName("menuWindowResults");
		menuWindowResults.addActionListener(actionAdapter);
		menuWindowAlarms.setText(LangModelSurvey.getString("menu.view.alarm_signals"));
		menuWindowAlarms.setName("menuWindowAlarms");
		menuWindowAlarms.addActionListener(actionAdapter);

		menuReport.setText(LangModelSurvey.getString("menu.report"));
		menuReport.setName("menuReport");
		menuTemplateReport.setText(LangModelSurvey.getString("menu.report.template"));
		menuTemplateReport.setName("menuTemplateReport");
		menuTemplateReport.addActionListener(actionAdapter);
    
		menuStart.add(menuStartScheduler);
		menuStart.add(menuStartAnalize);
		menuStart.add(menuStartAnalizeExt);
		menuStart.add(menuStartEvaluation);
		menuStart.add(menuStartPrognosis);
		menuStart.add(menuStartMapEditor);
		menuStart.add(menuStartSchemeEditor);

		menuView.add(menuViewMap);
		menuView.add(menuViewScheme);
		menuView.addSeparator();
		menuView.add(menuWindowMap);
		menuView.add(menuWindowScheme);
		menuView.add(menuWindowProperties);
		menuView.add(menuWindowCharacteristics);
		menuView.addSeparator();
		menuView.add(menuWindowMeasurements);
		menuView.add(menuWindowResults);
		menuView.add(menuWindowAlarms);

		menuReport.add(menuTemplateReport);

		this.add(menuStart);
		this.add(menuView);
		this.add(menuReport);
		this.addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e});
			}
			
			public void modelChanged(String e[])
			{
				ApplicationModel aModel = SurveyMenuBar.this.getApplicationModel();
				
				menuStart.setVisible(aModel.isVisible("menuStart"));
				menuStart.setEnabled(aModel.isEnabled("menuStart"));
				menuStartScheduler.setVisible(aModel.isVisible("menuStartScheduler"));
				menuStartScheduler.setEnabled(aModel.isEnabled("menuStartScheduler"));
				menuStartAnalize.setVisible(aModel.isVisible("menuStartAnalize"));
				menuStartAnalize.setEnabled(aModel.isEnabled("menuStartAnalize"));
				menuStartAnalizeExt.setVisible(aModel.isVisible("menuStartAnalizeExt"));
				menuStartAnalizeExt.setEnabled(aModel.isEnabled("menuStartAnalizeExt"));
				menuStartEvaluation.setVisible(aModel.isVisible("menuStartEvaluation"));
				menuStartEvaluation.setEnabled(aModel.isEnabled("menuStartEvaluation"));
				menuStartPrognosis.setVisible(aModel.isVisible("menuStartPrognosis"));
				menuStartPrognosis.setEnabled(aModel.isEnabled("menuStartPrognosis"));
				
				menuView.setVisible(aModel.isVisible("menuView"));
				menuView.setEnabled(aModel.isEnabled("menuView"));
				menuViewMap.setVisible(aModel.isVisible("menuViewMap"));
				menuViewMap.setEnabled(aModel.isEnabled("menuViewMap"));
				menuStartMapEditor.setVisible(aModel.isVisible("menuStartMapEditor"));
				menuStartMapEditor.setEnabled(aModel.isEnabled("menuStartMapEditor"));
				menuViewScheme.setVisible(aModel.isVisible("menuViewScheme"));
				menuViewScheme.setEnabled(aModel.isEnabled("menuViewScheme"));
				menuStartSchemeEditor.setVisible(aModel.isVisible("menuStartSchemeEditor"));
				menuStartSchemeEditor.setEnabled(aModel.isEnabled("menuStartSchemeEditor"));

				menuWindowCharacteristics.setVisible(aModel.isVisible("menuWindowCharacteristics"));
				menuWindowCharacteristics.setEnabled(aModel.isEnabled("menuWindowCharacteristics"));
				menuWindowProperties.setVisible(aModel.isVisible("menuWindowProperties"));
				menuWindowProperties.setEnabled(aModel.isEnabled("menuWindowProperties"));
				menuWindowScheme.setVisible(aModel.isVisible("menuWindowScheme"));
				menuWindowScheme.setEnabled(aModel.isEnabled("menuWindowScheme"));
				menuWindowMap.setVisible(aModel.isVisible("menuWindowMap"));
				menuWindowMap.setEnabled(aModel.isEnabled("menuWindowMap"));
				
				menuWindowMeasurements.setVisible(aModel.isVisible("menuWindowMeasurements"));
				menuWindowMeasurements.setEnabled(aModel.isEnabled("menuWindowMeasurements"));
				menuWindowResults.setVisible(aModel.isVisible("menuWindowResults"));
				menuWindowResults.setEnabled(aModel.isEnabled("menuWindowResults"));
				menuWindowAlarms.setVisible(aModel.isVisible("menuWindowAlarms"));
				menuWindowAlarms.setEnabled(aModel.isEnabled("menuWindowAlarms"));
				
				menuReport.setVisible(aModel.isVisible("menuReport"));
				menuReport.setEnabled(aModel.isEnabled("menuReport"));
				menuTemplateReport.setVisible(aModel.isVisible("menuTemplateReport"));
				menuTemplateReport.setEnabled(aModel.isEnabled("menuTemplateReport"));
			}
		});
	}
}