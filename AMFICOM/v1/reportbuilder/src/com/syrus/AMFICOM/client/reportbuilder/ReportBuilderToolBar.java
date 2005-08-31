/*
 * $Id: ReportBuilderToolBar.java,v 1.1 2005/08/31 10:04:26 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class ReportBuilderToolBar extends AbstractMainToolBar {
	private static final long serialVersionUID = 7496638136912951178L;

	public ReportBuilderToolBar() {
		initItems();
	}

	private void initItems() {

		final JButton newTemplateButton = new JButton();
		newTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/new.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		newTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));		
		newTemplateButton.setToolTipText(LangModelReport.getString("report.UI.Toolbar.createNewTemplate"));
		newTemplateButton.setName("menuReportTemplateNew");
		newTemplateButton.addActionListener(super.actionListener);

		final JButton saveTemplateButton = new JButton();
		saveTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/save.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		saveTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		saveTemplateButton.setToolTipText(LangModelReport.getString("report.UI.Toolbar.saveTemplate"));
		saveTemplateButton.setName("menuReportTemplateSave");
		saveTemplateButton.addActionListener(super.actionListener);

		final JButton loadTemplateButton = new JButton();
		loadTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/openfile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		loadTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		loadTemplateButton.setToolTipText(LangModelReport.getString("report.UI.Toolbar.loadTemplate"));
		loadTemplateButton.setName("menuReportTemplateLoad");
		loadTemplateButton.addActionListener(super.actionListener);

		final JButton importTemplateButton = new JButton();
		importTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/load_template.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		importTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		importTemplateButton.setToolTipText(LangModelReport.getString("report.UI.Toolbar.importTemplates"));
		importTemplateButton.setName("menuReportTemplateImport");
		importTemplateButton.addActionListener(super.actionListener);

		final JButton exportTemplateButton = new JButton();
		exportTemplateButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/save_template.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		exportTemplateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		exportTemplateButton.setToolTipText(LangModelReport.getString("report.UI.Toolbar.exportTemplates"));
		exportTemplateButton.setName("menuReportTemplateExport");
		exportTemplateButton.addActionListener(super.actionListener);
		
		addSeparator();
		add(newTemplateButton);
		add(loadTemplateButton);
		add(saveTemplateButton);
		addSeparator();
		add(importTemplateButton);
		add(exportTemplateButton);

		
		addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] {e});
			}
			
			public void modelChanged(String e[]) {
				ApplicationModel aModel = ReportBuilderToolBar.this.getApplicationModel();
				
				newTemplateButton.setVisible(aModel.isVisible("menuReportTemplateNew"));
				newTemplateButton.setEnabled(aModel.isEnabled("menuReportTemplateNew"));
				saveTemplateButton.setVisible(aModel.isVisible("menuReportTemplateSave"));
				saveTemplateButton.setEnabled(aModel.isEnabled("menuReportTemplateSave"));
				loadTemplateButton.setVisible(aModel.isVisible("menuReportTemplateLoad"));
				loadTemplateButton.setEnabled(aModel.isEnabled("menuReportTemplateLoad"));
				exportTemplateButton.setVisible(aModel.isVisible("menuReportTemplateExport"));
				exportTemplateButton.setEnabled(aModel.isEnabled("menuReportTemplateExport"));
				importTemplateButton.setVisible(aModel.isVisible("menuReportTemplateImport"));
				importTemplateButton.setEnabled(aModel.isEnabled("menuReportTemplateImport"));
				
			}
		});
	}
}
