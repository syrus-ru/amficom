package com.syrus.AMFICOM.client.observer;

import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.ObserverApplicationModel;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.resource.LangModelObserver;
import com.syrus.AMFICOM.resource.ObserverResourceKeys;

public class ObserverToolBar extends AbstractMainToolBar {

	public ObserverToolBar() {
		initItems();
	}

	private void initItems() {
		
		final JButton menuOpenMap = new JButton();
		final JButton menuOpenScheme = new JButton();
		
		final JButton menuViewNavigator = new JButton();
		final JButton menuViewAlarms = new JButton();
		final JButton menuViewResults = new JButton();
		
		final JButton menuStartScheduler = new JButton();
		final JButton menuStartAnalize = new JButton();
		final JButton menuStartAnalizeExt = new JButton();
		final JButton menuStartEvaluation = new JButton();
		final JButton menuStartPrognosis = new JButton();

		menuOpenMap.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_MAP_OPEN));
		menuOpenMap.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuOpenMap.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_OPEN_MAP));
		menuOpenMap.setName(ObserverApplicationModel.MENU_OPEN_MAP);
		menuOpenMap.addActionListener(super.actionListener);

		menuOpenScheme.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_SCHEME_OPEN));
		menuOpenScheme.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuOpenScheme.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_OPEN_SCHEME));
		menuOpenScheme.setName(ObserverApplicationModel.MENU_OPEN_SCHEME);
		menuOpenScheme.addActionListener(super.actionListener);

		menuViewNavigator.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_ARCHIVE));
		menuViewNavigator.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewNavigator.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_NAVIGATOR));
		menuViewNavigator.setName(ObserverApplicationModel.MENU_VIEW_NAVIGATOR);
		menuViewNavigator.addActionListener(super.actionListener);

		menuViewAlarms.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_ALARM));
		menuViewAlarms.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewAlarms.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_ALARMS));
		menuViewAlarms.setName(ObserverApplicationModel.MENU_VIEW_ALARMS);
		menuViewAlarms.addActionListener(super.actionListener);

		menuViewResults.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_RESULT));
		menuViewResults.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewResults.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_VIEW_RESULTS));
		menuViewResults.setName(ObserverApplicationModel.MENU_VIEW_RESULTS);
		menuViewResults.addActionListener(super.actionListener);

		menuStartScheduler.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_SCHEDULER));
		menuStartScheduler.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartScheduler.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_SCHEDULER));
		menuStartScheduler.setName(ObserverApplicationModel.MENU_START_SCHEDULER);
		menuStartScheduler.addActionListener(super.actionListener);

		menuStartAnalize.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_ANALYSIS));
		menuStartAnalize.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartAnalize.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_ANALIZE));
		menuStartAnalize.setName(ObserverApplicationModel.MENU_START_ANALIZE);
		menuStartAnalize.addActionListener(super.actionListener);

		menuStartAnalizeExt.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_SURVEY));
		menuStartAnalizeExt.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartAnalizeExt.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_ANALIZE_EXT));
		menuStartAnalizeExt.setName(ObserverApplicationModel.MENU_START_ANALIZE_EXT);
		menuStartAnalizeExt.addActionListener(super.actionListener);

		menuStartEvaluation.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_EVALUATION));
		menuStartEvaluation.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartEvaluation.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_EVALUATION));
		menuStartEvaluation.setName(ObserverApplicationModel.MENU_START_EVALUATION);
		menuStartEvaluation.addActionListener(super.actionListener);

		menuStartPrognosis.setIcon(UIManager.getIcon(ObserverResourceKeys.ICON_PREDICTION));
		menuStartPrognosis.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuStartPrognosis.setToolTipText(LangModelObserver.getString(ObserverApplicationModel.MENU_START_PROGNOSIS));
		menuStartPrognosis.setName(ObserverApplicationModel.MENU_START_PROGNOSIS);
		menuStartPrognosis.addActionListener(super.actionListener);

		addSeparator();
		add(menuOpenMap);
		add(menuOpenScheme);
		addSeparator();
		add(menuViewNavigator);
		add(menuViewAlarms);
		add(menuViewResults);
		addSeparator();
		add(menuStartScheduler);
		add(menuStartAnalize);
		add(menuStartAnalizeExt);
		add(menuStartEvaluation);
		add(menuStartPrognosis);
		addSeparator();

		addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e });
			}

			public void modelChanged(String e[]) {
				ApplicationModel aModel = ObserverToolBar.this.getApplicationModel();
				menuOpenMap.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_OPEN_MAP));
				menuOpenMap.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_OPEN_MAP));
				menuOpenScheme.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_OPEN_SCHEME));
				menuOpenScheme.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_OPEN_SCHEME));
				menuViewNavigator.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_NAVIGATOR));
				menuViewNavigator.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_NAVIGATOR));
				menuViewAlarms.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_ALARMS));
				menuViewAlarms.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_ALARMS));
				menuViewResults.setVisible(aModel.isVisible(ObserverApplicationModel.MENU_VIEW_RESULTS));
				menuViewResults.setEnabled(aModel.isEnabled(ObserverApplicationModel.MENU_VIEW_RESULTS));
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
			}
		});
	}
}
