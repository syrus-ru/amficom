/*
 * $Id: ReportBuilderToolBar.java,v 1.6 2005/10/12 13:29:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class ReportBuilderToolBar extends AbstractMainToolBar {
	private static final long serialVersionUID = 7496638136912951178L;

	public ReportBuilderToolBar() {
		initItems();
	}

	private void initItems() {

		final JButton newTemplateButton = new JButton();
		newTemplateButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_NEW_TEMPLATE));
		newTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));		
		newTemplateButton.setToolTipText(I18N.getString("report.UI.Toolbar.createNewTemplate"));
		newTemplateButton.setName(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_NEW);
		newTemplateButton.addActionListener(super.actionListener);

		final JButton saveTemplateButton = new JButton();
		saveTemplateButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_SAVE_TEMPLATE));
		saveTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		saveTemplateButton.setToolTipText(I18N.getString("report.UI.Toolbar.saveTemplate"));
		saveTemplateButton.setName(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE);
		saveTemplateButton.addActionListener(super.actionListener);

		final JButton saveAsTemplateButton = new JButton();
		saveAsTemplateButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_SAVE_AS_TEMPLATE));
		saveAsTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		saveAsTemplateButton.setToolTipText(I18N.getString("report.UI.Toolbar.saveAsTemplate"));
		saveAsTemplateButton.setName(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE_AS);
		saveAsTemplateButton.addActionListener(super.actionListener);
		
		final JButton loadTemplateButton = new JButton();
		loadTemplateButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_LOAD_TEMPLATE));
		loadTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		loadTemplateButton.setToolTipText(I18N.getString("report.UI.Toolbar.loadTemplate"));
		loadTemplateButton.setName(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_LOAD);
		loadTemplateButton.addActionListener(super.actionListener);

		final JButton importTemplateButton = new JButton();
		importTemplateButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_IMPORT_TEMPLATES));
		importTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		importTemplateButton.setToolTipText(I18N.getString("report.UI.Toolbar.importTemplates"));
		importTemplateButton.setName(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_IMPORT);
		importTemplateButton.addActionListener(super.actionListener);

		final JButton exportTemplateButton = new JButton();
		exportTemplateButton.setIcon(UIManager.getIcon(ReportBuilderResourceKeys.ICON_EXPORT_TEMPLATES));
		exportTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		exportTemplateButton.setToolTipText(I18N.getString("report.UI.Toolbar.exportTemplates"));
		exportTemplateButton.setName(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_EXPORT);
		exportTemplateButton.addActionListener(super.actionListener);
		
		addSeparator();
		add(newTemplateButton);
		add(loadTemplateButton);
		add(saveTemplateButton);
		add(saveAsTemplateButton);		
		addSeparator();
		add(importTemplateButton);
		add(exportTemplateButton);

		
		addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] {e});
			}
			
			public void modelChanged(String e[]) {
				ApplicationModel aModel = ReportBuilderToolBar.this.getApplicationModel();
				
				newTemplateButton.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_NEW));
				newTemplateButton.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_NEW));
				saveTemplateButton.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE));
				saveTemplateButton.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE));
				saveAsTemplateButton.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE_AS));
				saveAsTemplateButton.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE_AS));
				loadTemplateButton.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_LOAD));
				loadTemplateButton.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_LOAD));
				exportTemplateButton.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_EXPORT));
				exportTemplateButton.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_EXPORT));
				importTemplateButton.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_IMPORT));
				importTemplateButton.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_IMPORT));
				
			}
		});
	}
}
