package com.syrus.AMFICOM.Client.Survey;

import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.resource.LangModelSurvey;
import com.syrus.AMFICOM.resource.SurveyResourceKeys;

public class SurveyToolBar  extends AbstractMainToolBar
{
	public SurveyToolBar()
	{
		initItems();
	}
	
	private void initItems() {
		
		final JButton menuViewMapOpen = new JButton();
		final JButton menuViewSchemeOpen = new JButton();
		
		final JButton menuViewMeasurements = new JButton();
		final JButton menuViewAlarms = new JButton();
		final JButton menuViewResults = new JButton();
		
		final JButton menuStartScheduler = new JButton();
		final JButton menuStartAnalize = new JButton();
		final JButton menuStartAnalizeExt = new JButton();
		final JButton menuStartEvaluation = new JButton();
		final JButton menuStartPrognosis = new JButton();
		
		
		menuViewMapOpen.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_MAP_OPEN));
		menuViewMapOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewMapOpen.setToolTipText(LangModelSurvey.getString("menu.view.map_open"));
		menuViewMapOpen.setName("menuViewMapOpen");
		menuViewMapOpen.addActionListener(super.actionListener);
		
		menuViewSchemeOpen.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_SCHEME_OPEN));
		menuViewSchemeOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewSchemeOpen.setToolTipText(LangModelSurvey.getString("menu.view.scheme_open"));
		menuViewSchemeOpen.setName("menuViewSchemeOpen");
		menuViewSchemeOpen.addActionListener(super.actionListener);
		
		menuViewMeasurements.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_ARCHIVE));
		menuViewMeasurements.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewMeasurements.setToolTipText(LangModelSurvey.getString("menu.view.measurement_archive"));
		menuViewMeasurements.setName("menuViewMeasurements");
		menuViewMeasurements.addActionListener(super.actionListener);
		
		menuViewAlarms.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_ALARM));
		menuViewAlarms.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewAlarms.setToolTipText(LangModelSurvey.getString("menu.view.alarm_signals"));
		menuViewAlarms.setName("menuViewAlarms");
		menuViewAlarms.addActionListener(super.actionListener);
		
		menuViewResults.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_RESULT));
		menuViewResults.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewResults.setToolTipText(LangModelSurvey.getString("menu.view.results_overview"));
		menuViewResults.setName("menuViewResults");
		menuViewResults.addActionListener(super.actionListener);
		
		menuStartScheduler.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_SCHEDULER));
		menuStartScheduler.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartScheduler.setToolTipText(LangModelSurvey.getString("menu.start.scheduling"));
		menuStartScheduler.setName("menuStartScheduler");
		menuStartScheduler.addActionListener(super.actionListener);
		
		menuStartAnalize.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_ANALYSIS));
		menuStartAnalize.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartAnalize.setToolTipText(LangModelSurvey.getString("menu.start.analyze"));
		menuStartAnalize.setName("menuStartAnalize");
		menuStartAnalize.addActionListener(super.actionListener);
		
		menuStartAnalizeExt.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_SURVEY));
		menuStartAnalizeExt.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartAnalizeExt.setToolTipText(LangModelSurvey.getString("menu.start.analyze_ext"));
		menuStartAnalizeExt.setName("menuStartAnalizeExt");
		menuStartAnalizeExt.addActionListener(super.actionListener);
		
		menuStartEvaluation.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_EVALUATION));
		menuStartEvaluation.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartEvaluation.setToolTipText(LangModelSurvey.getString("menu.start.evaluation"));
		menuStartEvaluation.setName("menuStartEvaluation");
		menuStartEvaluation.addActionListener(super.actionListener);
		
		menuStartPrognosis.setIcon(UIManager.getIcon(SurveyResourceKeys.ICON_PREDICTION));
		menuStartPrognosis.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartPrognosis.setToolTipText(LangModelSurvey.getString("menu.start.prediction"));
		menuStartPrognosis.setName("menuStartPrognosis");
		menuStartPrognosis.addActionListener(super.actionListener);
		
		add(menuViewMapOpen);
		add(menuViewSchemeOpen);
		addSeparator();
		add(menuViewMeasurements);
		add(menuViewAlarms);
		add(menuViewResults);
		addSeparator();
		add(menuStartScheduler);
		add(menuStartAnalize);
		add(menuStartAnalizeExt);
		add(menuStartEvaluation);
		add(menuStartPrognosis);
		
		addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e });
			}
			
			public void modelChanged(String e[]) {
				ApplicationModel aModel = SurveyToolBar.this.getApplicationModel();
				menuViewMapOpen.setVisible(aModel.isVisible("menuViewMapOpen"));
				menuViewMapOpen.setEnabled(aModel.isEnabled("menuViewMapOpen"));
				menuViewSchemeOpen.setVisible(aModel.isVisible("menuViewSchemeOpen"));
				menuViewSchemeOpen.setEnabled(aModel.isEnabled("menuViewSchemeOpen"));
				menuViewMeasurements.setVisible(aModel.isVisible("menuViewMeasurements"));
				menuViewMeasurements.setEnabled(aModel.isEnabled("menuViewMeasurements"));
				menuViewAlarms.setVisible(aModel.isVisible("menuViewAlarms"));
				menuViewAlarms.setEnabled(aModel.isEnabled("menuViewAlarms"));
				menuViewResults.setVisible(aModel.isVisible("menuViewResults"));
				menuViewResults.setEnabled(aModel.isEnabled("menuViewResults"));
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
			}
		});
	}
}
