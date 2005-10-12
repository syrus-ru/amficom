/*
 * $Id: ReportBuilderMenuBar.java,v 1.5 2005/10/12 13:29:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.I18N;

public class ReportBuilderMenuBar extends AbstractMainMenuBar {
	private static final long serialVersionUID = -7099897258722684663L;

	public ReportBuilderMenuBar(ApplicationModel aModel) {
		super(aModel);
	}
	
	protected void addMenuItems() {
		final JMenu menuTemplate = new JMenu();
		final JMenuItem menuTemplateParameters = new JMenuItem();

		final JMenu menuWindow = new JMenu();
		final JMenuItem menuWindowTree = new JMenuItem();
		final JMenuItem menuWindowTemplateScheme = new JMenuItem();
		final JMenuItem menuViewArrange = new JMenuItem();
		
		menuTemplate.setText(I18N.getString("report.UI.Menubar.menuTemplate"));
		menuTemplate.setName(ReportBuilderApplicationModel.MENU_TEMPLATE);
		
		menuTemplateParameters.setText(I18N.getString("report.UI.Menubar.menuTemplateParameters"));
		menuTemplateParameters.setName(ReportBuilderApplicationModel.MENU_TEMPLATE_PARAMETERS);
		menuTemplateParameters.addActionListener(this.actionAdapter);
		
		menuTemplate.add(menuTemplateParameters);		
		
		menuWindow.setText(I18N.getString("report.UI.Menubar.menuWindow"));
		menuWindow.setName(ReportBuilderApplicationModel.MENU_WINDOW);
		
		menuWindowTree.setText(I18N.getString("report.UI.Menubar.menuTree"));
		menuWindowTree.setName(ReportBuilderApplicationModel.MENU_WINDOW_TREE);
		menuWindowTree.addActionListener(this.actionAdapter);
		
		menuWindowTemplateScheme.setText(I18N.getString("report.UI.Menubar.menuTemplateScheme"));
		menuWindowTemplateScheme.setName(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME);
		menuWindowTemplateScheme.addActionListener(this.actionAdapter);
		
		menuViewArrange.setText(I18N.getString("Menu.View.WindowArrange"));
		menuViewArrange.setName(ApplicationModel.MENU_VIEW_ARRANGE);
		menuViewArrange.addActionListener(this.actionAdapter);
		
		menuWindow.add(menuWindowTree);
		menuWindow.add(menuWindowTemplateScheme);
		menuWindow.add(menuViewArrange);
		
		add(menuTemplate);
		add(menuWindow);
		
		this.addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] {e});
			}
			
			public void modelChanged(String e[]) {
				ApplicationModel aModel = ReportBuilderMenuBar.this.getApplicationModel();
				
				menuTemplate.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_TEMPLATE));
				menuTemplate.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_TEMPLATE));
				menuTemplateParameters.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_TEMPLATE_PARAMETERS));
				menuTemplateParameters.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_TEMPLATE_PARAMETERS));
				
				menuWindow.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_WINDOW));
				menuWindow.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_WINDOW));
				menuWindowTree.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_WINDOW_TREE));
				menuWindowTree.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_WINDOW_TREE));
				menuWindowTemplateScheme.setVisible(aModel.isVisible(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME));
				menuWindowTemplateScheme.setEnabled(aModel.isEnabled(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME));
			}
		});
	}
}
