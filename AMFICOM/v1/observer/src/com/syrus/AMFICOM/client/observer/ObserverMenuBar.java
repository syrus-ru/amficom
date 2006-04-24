package com.syrus.AMFICOM.client.observer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.ObserverApplicationModel;
import com.syrus.AMFICOM.resource.LangModelObserver;

public class ObserverMenuBar extends AbstractMainMenuBar {
	private static final long serialVersionUID = -481976816191208039L;

	public ObserverMenuBar(ApplicationModel aModel)
	{
		super(aModel);
	}

	@Override
	protected void addMenuItems() {
		
		final JMenu menuStart = new JMenu();
		final JMenuItem menuStartScheduler = new JMenuItem();
		final JMenuItem menuStartAnalize = new JMenuItem();
		final JMenuItem menuStartAnalizeExt = new JMenuItem();
		final JMenuItem menuStartEvaluation = new JMenuItem();
		final JMenuItem menuStartPrognosis = new JMenuItem();
		final JMenuItem menuStartMapEditor = new JMenuItem();
		final JMenuItem menuStartSchemeEditor = new JMenuItem();
		
		final JMenu menuOpen = new JMenu();
		final JMenuItem menuOpenMap = new JMenuItem();
		final JMenuItem menuOpenScheme = new JMenuItem();
		
		final JMenu menuView = new JMenu();
		final JMenuItem menuViewMap = new JMenuItem();
		final JMenuItem menuViewScheme = new JMenuItem();
		final JMenuItem menuViewGeneralProperties = new JMenuItem();
		final JMenuItem menuViewAdditionalProperties = new JMenuItem();
		final JMenuItem menuViewCharacteristics = new JMenuItem();
		final JMenuItem menuViewNavigator = new JMenuItem();
		final JMenuItem menuViewResults = new JMenuItem();
		final JMenuItem menuViewAlarms = new JMenuItem();
		final JMenuItem menuViewArrange = new JMenuItem();
		
		final JMenu menuReport = new JMenu();
		final JMenuItem menuReportByTemplate = new JMenuItem();

		menuOpen.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_OPEN));
		menuOpen.setName(ObserverApplicationModel.MENU_OPEN);
		menuOpenMap.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_OPEN_MAP));
		menuOpenMap.setName(ObserverApplicationModel.MENU_OPEN_MAP);
		menuOpenMap.addActionListener(this.actionAdapter);
		menuOpenScheme.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_OPEN_SCHEME));
		menuOpenScheme.setName(ObserverApplicationModel.MENU_OPEN_SCHEME);
		menuOpenScheme.addActionListener(this.actionAdapter);
		
		menuStart.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_START));
		menuStart.setName(ObserverApplicationModel.MENU_START);
		menuStartScheduler.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_SCHEDULER));
		menuStartScheduler.setName(ObserverApplicationModel.MENU_START_SCHEDULER);
		menuStartScheduler.addActionListener(this.actionAdapter);
		menuStartAnalize.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_ANALIZE));
		menuStartAnalize.setName(ObserverApplicationModel.MENU_START_ANALIZE);
		menuStartAnalize.addActionListener(this.actionAdapter);
		menuStartAnalizeExt.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_ANALIZE_EXT));
		menuStartAnalizeExt.setName(ObserverApplicationModel.MENU_START_ANALIZE_EXT);
		menuStartAnalizeExt.addActionListener(this.actionAdapter);
		menuStartEvaluation.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_EVALUATION));
		menuStartEvaluation.setName(ObserverApplicationModel.MENU_START_EVALUATION);
		menuStartEvaluation.addActionListener(this.actionAdapter);
		menuStartPrognosis.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_PROGNOSIS));
		menuStartPrognosis.setName(ObserverApplicationModel.MENU_START_PROGNOSIS);
		menuStartPrognosis.addActionListener(this.actionAdapter);
		menuStartMapEditor.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_MAP_EDITOR));
		menuStartMapEditor.setName(ObserverApplicationModel.MENU_START_MAP_EDITOR);
		menuStartMapEditor.addActionListener(this.actionAdapter);
		menuStartSchemeEditor.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_SCHEME_EDITOR));
		menuStartSchemeEditor.setName(ObserverApplicationModel.MENU_START_SCHEME_EDITOR);
		menuStartSchemeEditor.addActionListener(this.actionAdapter);

		menuView.setText(LangModelObserver.getString(ApplicationModel.MENU_VIEW));
		menuView.setName(ApplicationModel.MENU_VIEW);
		menuViewScheme.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_SCHEME));
		menuViewScheme.setName(ObserverApplicationModel.MENU_VIEW_SCHEME);
		menuViewScheme.addActionListener(this.actionAdapter);
		menuViewMap.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_MAP));
		menuViewMap.setName(ObserverApplicationModel.MENU_VIEW_MAP);
		menuViewMap.addActionListener(this.actionAdapter);
		menuViewGeneralProperties.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES));
		menuViewGeneralProperties.setName(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES);
		menuViewGeneralProperties.addActionListener(this.actionAdapter);
		menuViewAdditionalProperties.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES));
		menuViewAdditionalProperties.setName(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES);
		menuViewAdditionalProperties.addActionListener(this.actionAdapter);
		menuViewCharacteristics.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS));
		menuViewCharacteristics.setName(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS);
		menuViewCharacteristics.addActionListener(this.actionAdapter);
		menuViewNavigator.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_NAVIGATOR));
		menuViewNavigator.setName(ObserverApplicationModel.MENU_VIEW_NAVIGATOR);
		menuViewNavigator.addActionListener(this.actionAdapter);
		menuViewResults.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_RESULTS));
		menuViewResults.setName(ObserverApplicationModel.MENU_VIEW_RESULTS);
		menuViewResults.addActionListener(this.actionAdapter);
		menuViewAlarms.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_ALARMS));
		menuViewAlarms.setName(ObserverApplicationModel.MENU_VIEW_ALARMS);
		menuViewAlarms.addActionListener(this.actionAdapter);
		menuViewArrange.setText(LangModelObserver.getString(ApplicationModel.MENU_VIEW_ARRANGE));
		menuViewArrange.setName(ApplicationModel.MENU_VIEW_ARRANGE);
		menuViewArrange.addActionListener(this.actionAdapter);

		menuReport.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_REPORT));
		menuReport.setName(ObserverApplicationModel.MENU_REPORT);
		menuReportByTemplate.setText(LangModelObserver.getString(ObserverApplicationModel.MENU_REPORT_BY_TEMPLATE));
		menuReportByTemplate.setName(ObserverApplicationModel.MENU_REPORT_BY_TEMPLATE);
		menuReportByTemplate.addActionListener(this.actionAdapter);
    
		menuStart.add(menuStartScheduler);
		menuStart.add(menuStartAnalize);
		menuStart.add(menuStartAnalizeExt);
		menuStart.add(menuStartEvaluation);
		menuStart.add(menuStartPrognosis);
		menuStart.add(menuStartMapEditor);
		menuStart.add(menuStartSchemeEditor);

		menuOpen.add(menuOpenMap);
		menuOpen.add(menuOpenScheme);

		menuView.add(menuViewMap);
		menuView.add(menuViewScheme);
		menuView.add(menuViewGeneralProperties);
		menuView.add(menuViewAdditionalProperties);
		menuView.add(menuViewCharacteristics);
		menuView.addSeparator();
		menuView.add(menuViewNavigator);
		menuView.add(menuViewResults);
		menuView.add(menuViewAlarms);
		menuView.addSeparator();
		menuView.add(menuViewArrange);

		menuReport.add(menuReportByTemplate);

		this.add(menuOpen);
		this.add(menuStart);
		this.add(menuReport);
		this.add(menuView);

		this.addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e});
			}
			
			public void modelChanged(String e[])
			{
				ApplicationModel aModel = ObserverMenuBar.this.getApplicationModel();
				
				menuStart.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_START));
				menuStart.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_START));
				menuStartScheduler.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_START_SCHEDULER));
				menuStartScheduler.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_START_SCHEDULER));
				menuStartAnalize.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_START_ANALIZE));
				menuStartAnalize.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_START_ANALIZE));
				menuStartAnalizeExt.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_START_ANALIZE_EXT));
				menuStartAnalizeExt.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_START_ANALIZE_EXT));
				menuStartEvaluation.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_START_EVALUATION));
				menuStartEvaluation.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_START_EVALUATION));
				menuStartPrognosis.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_START_PROGNOSIS));
				menuStartPrognosis.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_START_PROGNOSIS));
				menuStartMapEditor.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_START_MAP_EDITOR));
				menuStartMapEditor.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_START_MAP_EDITOR));
				menuStartSchemeEditor.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_START_SCHEME_EDITOR));
				menuStartSchemeEditor.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_START_SCHEME_EDITOR));
				
				menuOpen.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_OPEN));
				menuOpen.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_OPEN));
				menuOpenMap.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_OPEN_MAP));
				menuOpenMap.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_OPEN_MAP));
				menuOpenScheme.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_OPEN_SCHEME));
				menuOpenScheme.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_OPEN_SCHEME));

				menuView.setVisible(aModel.isVisible(ApplicationModel.MENU_VIEW));
				menuView.setEnabled(aModel.isEnabled(ApplicationModel.MENU_VIEW));
				menuViewScheme.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_SCHEME));
				menuViewScheme.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_SCHEME));
				menuViewMap.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_MAP));
				menuViewMap.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_MAP));
				menuViewGeneralProperties.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES));
				menuViewGeneralProperties.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES));
				menuViewAdditionalProperties.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES));
				menuViewAdditionalProperties.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES));
				menuViewCharacteristics.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS));
				menuViewCharacteristics.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS));
				menuViewNavigator.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_NAVIGATOR));
				menuViewNavigator.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_NAVIGATOR));
				menuViewResults.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_RESULTS));
				menuViewResults.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_NAVIGATOR));
				menuViewAlarms.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_ALARMS));
				menuViewAlarms.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_ALARMS));
				menuViewArrange.setVisible(aModel.isVisible(ApplicationModel.MENU_VIEW_ARRANGE));
				menuViewArrange.setEnabled(aModel.isEnabled(ApplicationModel.MENU_VIEW_ARRANGE));
				
				menuReport.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_REPORT));
				menuReport.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_REPORT));
				menuReportByTemplate.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_REPORT_BY_TEMPLATE));
				menuReportByTemplate.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_REPORT_BY_TEMPLATE));
			}
		});
	}
}