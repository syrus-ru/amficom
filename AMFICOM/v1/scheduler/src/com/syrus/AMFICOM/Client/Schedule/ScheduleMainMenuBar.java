/*-
* $Id: ScheduleMainMenuBar.java,v 1.14 2005/10/06 13:18:02 bob Exp $
*
* Copyright ¿ 2004-2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.I18N;

/**
 * @version $Revision: 1.14 $, $Date: 2005/10/06 13:18:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public class ScheduleMainMenuBar extends AbstractMainMenuBar {

	private static final long	serialVersionUID			= 3689065136446912305L;

	public static final String	MENU_VIEW					= "menuView";
	public static final String	MENU_VIEW_PLAN				= "menuViewPlan";
	public static final String	MENU_VIEW_TIME				= "menuViewTime";
	public static final String	MENU_VIEW_TREE				= "menuViewTree";
	public static final String	MENU_VIEW_PARAMETERS		= "menuViewParameters";
	public static final String	MENU_VIEW_PROPERTIES		= "menuViewProperties";
	public static final String	MENU_VIEW_TABLE				= "menuViewTable";
//	public static final String	MENU_VIEW_SAVE_PARAMETERS	= "menuViewSaveParameters";

	public static final String	MENU_REPORT					= "label_report";
	public static final String	MENU_TEMPLATE_REPORT		= "label_reportForTemplate";

	private JMenu				menuView;
	// private JMenu menuReport;

	public ScheduleMainMenuBar(ApplicationModel aModel) {
		super(aModel);

		// this.menuReport = new JMenu(LangModelReport.getString(MENU_REPORT));
		// this.menuReport.setName(MENU_REPORT);
		//
		// final JMenuItem menuTemplateReportItem = new
		// JMenuItem(LangModelReport.getString(MENU_TEMPLATE_REPORT));
		// menuTemplateReportItem.setName(MENU_TEMPLATE_REPORT);
		// menuTemplateReportItem.addActionListener(actionAdapter);
		// this.menuReport.add(menuTemplateReportItem);

		// this.add(this.menuReport);

	}

	@Override
	protected void addMenuItems() {
		this.menuView = new JMenu(I18N.getString(ApplicationModel.MENU_VIEW));
		this.menuView.setName(MENU_VIEW);

		final JMenuItem menuViewPlanItem = new JMenuItem(I18N.getString("Scheduler.Text.Plan.Title"));
		menuViewPlanItem.setName(MENU_VIEW_PLAN);
		menuViewPlanItem.addActionListener(this.actionAdapter);
		this.menuView.add(menuViewPlanItem);

		final JMenuItem menuViewTimeItem = new JMenuItem(I18N.getString("Scheduler.Text.TimePanel.Title"));
		menuViewTimeItem.setName(MENU_VIEW_TIME);
		menuViewTimeItem.addActionListener(this.actionAdapter);
		this.menuView.add(menuViewTimeItem);

		final JMenuItem menuViewTreeItem = new JMenuItem(I18N.getString("Scheduler.Text.ElementsTree.Title"));
		menuViewTreeItem.setName(MENU_VIEW_TREE);
		menuViewTreeItem.addActionListener(this.actionAdapter);
		this.menuView.add(menuViewTreeItem);

		final JMenuItem menuViewParametersItem = new JMenuItem(I18N.getString("Scheduler.Text.MeasurementParameter.Title"));
		menuViewParametersItem.setName(MENU_VIEW_PARAMETERS);
		menuViewParametersItem.addActionListener(this.actionAdapter);
		this.menuView.add(menuViewParametersItem);

		final JMenuItem menuViewPropertiesItem = new JMenuItem(I18N.getString("Scheduler.Text.TestOptions.Title"));
		menuViewPropertiesItem.setName(MENU_VIEW_PROPERTIES);
		menuViewPropertiesItem.addActionListener(this.actionAdapter);
		this.menuView.add(menuViewPropertiesItem);

		final JMenuItem menuViewTableItem = new JMenuItem(I18N.getString("Scheduler.Text.Table.Title"));
		menuViewTableItem.setName(MENU_VIEW_TABLE);
		menuViewTableItem.addActionListener(this.actionAdapter);
		this.menuView.add(menuViewTableItem);

		this.menuView.addSeparator();

		final JMenuItem menuViewArrangeItem = new JMenuItem(I18N.getString(ApplicationModel.MENU_VIEW_ARRANGE));
		menuViewArrangeItem.setName(ApplicationModel.MENU_VIEW_ARRANGE);
		menuViewArrangeItem.addActionListener(this.actionAdapter);
		this.menuView.add(menuViewArrangeItem);

		this.add(this.menuView);

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(String elementName) {
				this.modelChanged();
			}

			public void modelChanged(String[] elementNames) {
				this.modelChanged();
			}

			private void modelChanged() {
				menuViewPlanItem.setVisible(ScheduleMainMenuBar.this.getApplicationModel().isVisible(MENU_VIEW_PLAN));
				menuViewPlanItem.setEnabled(ScheduleMainMenuBar.this.getApplicationModel().isEnabled(MENU_VIEW_PLAN));

				menuViewTimeItem.setVisible(ScheduleMainMenuBar.this.getApplicationModel().isVisible(MENU_VIEW_TIME));
				menuViewTimeItem.setEnabled(ScheduleMainMenuBar.this.getApplicationModel().isEnabled(MENU_VIEW_TIME));

				menuViewTreeItem.setVisible(ScheduleMainMenuBar.this.getApplicationModel().isVisible(MENU_VIEW_TREE));
				menuViewTreeItem.setEnabled(ScheduleMainMenuBar.this.getApplicationModel().isEnabled(MENU_VIEW_TREE));

				menuViewParametersItem.setVisible(ScheduleMainMenuBar.this.getApplicationModel().isVisible(
					MENU_VIEW_PARAMETERS));
				menuViewParametersItem.setEnabled(ScheduleMainMenuBar.this.getApplicationModel().isEnabled(
					MENU_VIEW_PARAMETERS));

				menuViewPropertiesItem.setVisible(ScheduleMainMenuBar.this.getApplicationModel().isVisible(
					MENU_VIEW_PROPERTIES));
				menuViewPropertiesItem.setEnabled(ScheduleMainMenuBar.this.getApplicationModel().isEnabled(
					MENU_VIEW_PROPERTIES));

				menuViewTableItem.setVisible(ScheduleMainMenuBar.this.getApplicationModel().isVisible(MENU_VIEW_TABLE));
				menuViewTableItem.setEnabled(ScheduleMainMenuBar.this.getApplicationModel().isEnabled(MENU_VIEW_TABLE));

				// menuTemplateReportItem.setVisible(ScheduleMainMenuBar.this.getApplicationModel().isVisible(MENU_TEMPLATE_REPORT));
				// menuTemplateReportItem.setEnabled(ScheduleMainMenuBar.this.getApplicationModel().isEnabled(MENU_TEMPLATE_REPORT));
//				menuViewSaveParametersItem.setVisible(ScheduleMainMenuBar.this.getApplicationModel().isVisible(
//					MENU_VIEW_SAVE_PARAMETERS));
//				menuViewSaveParametersItem.setEnabled(ScheduleMainMenuBar.this.getApplicationModel().isEnabled(
//					MENU_VIEW_SAVE_PARAMETERS));

				
				
				menuViewArrangeItem.setVisible(ScheduleMainMenuBar.this.getApplicationModel().isVisible(
					ApplicationModel.MENU_VIEW_ARRANGE));
				menuViewArrangeItem.setEnabled(ScheduleMainMenuBar.this.getApplicationModel().isEnabled(
					ApplicationModel.MENU_VIEW_ARRANGE));

			}

		});
	}

}
