/*-
 * $Id: SchemeEditorMenuBar.java,v 1.14 2006/03/28 10:22:59 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.resource.LangModelScheme;

/**
 * @author $Author: stas $
 * @version $Revision: 1.14 $, $Date: 2006/03/28 10:22:59 $
 * @module schemeclient
 */

public class SchemeEditorMenuBar extends AbstractMainMenuBar {
	private static final long serialVersionUID = -7099897258722684663L;

	public SchemeEditorMenuBar(ApplicationModel aModel) {
		super(aModel);
	}

	@Override
	protected void addMenuItems() {

		final JMenu menuScheme = new JMenu();
		final JMenuItem menuSchemeNew = new JMenuItem();
		final JMenuItem menuSchemeLoad = new JMenuItem();
		final JMenuItem menuSchemeSave = new JMenuItem();
		final JMenuItem menuSchemeSaveAs = new JMenuItem();
		final JMenuItem menuSchemeValidate = new JMenuItem();
		
		final JMenu menuImport = new JMenu();
		final JMenuItem menuSchemeImport = new JMenuItem();
		final JMenuItem menuConfigImport = new JMenuItem();
		final JMenuItem menuProtosImport = new JMenuItem();
		final JMenuItem menuSchemeImportCommit = new JMenuItem();
		final JMenu menuExport = new JMenu();
		final JMenuItem menuSchemeExport = new JMenuItem();
		final JMenuItem menuConfigExport = new JMenuItem();
		final JMenuItem menuProtosExport = new JMenuItem();

		final JMenu menuPath = new JMenu();
		final JMenuItem menuPathNew = new JMenuItem();
		final JMenuItem menuPathEdit = new JMenuItem();
		final JMenuItem menuPathSave = new JMenuItem();
		final JMenuItem menuPathCancel = new JMenuItem();

		final JMenu menuReport = new JMenu();
		final JMenuItem menuReportCreate = new JMenuItem();

		final JMenu menuWindow = new JMenu();
		final JMenuItem menuWindowArrange = new JMenuItem();
		final JMenuItem menuWindowTree = new JMenuItem();
		final JMenuItem menuWindowScheme = new JMenuItem();
		final JMenuItem menuWindowUgo = new JMenuItem();
		final JMenuItem menuWindowProps = new JMenuItem();
		final JMenuItem menuWindowList = new JMenuItem();

		menuScheme.setText(LangModelSchematics.getString("menuScheme"));
		menuScheme.setName("menuScheme");
		menuSchemeNew.setText(LangModelSchematics.getString("menuSchemeNew"));
		menuSchemeNew.setName("menuSchemeNew");
		menuSchemeNew.addActionListener(this.actionAdapter);
		menuSchemeSave.setText(LangModelSchematics.getString("menuSchemeSave"));
		menuSchemeSave.setName("menuSchemeSave");
		menuSchemeSave.addActionListener(this.actionAdapter);
		menuSchemeSaveAs.setText(LangModelSchematics.getString("menuSchemeSaveAs"));
		menuSchemeSaveAs.setName("menuSchemeSaveAs");
		menuSchemeSaveAs.addActionListener(this.actionAdapter);
		menuSchemeValidate.setText(LangModelSchematics.getString("menuSchemeValidate"));
		menuSchemeValidate.setName("menuSchemeValidate");
		menuSchemeValidate.addActionListener(this.actionAdapter);
		
		menuSchemeLoad.setText(LangModelSchematics.getString("menuSchemeLoad"));
		menuSchemeLoad.setName("menuSchemeLoad");
		menuSchemeLoad.addActionListener(this.actionAdapter);
		
		menuImport.setText(LangModelScheme.getString("Menu.import"));
		menuImport.setName("Menu.import");
		menuSchemeImport.setText(LangModelScheme.getString("Menu.import.scheme"));
		menuSchemeImport.setName("Menu.import.scheme");
		menuSchemeImport.addActionListener(this.actionAdapter);
		menuConfigImport.setText(LangModelScheme.getString("Menu.import.config"));
		menuConfigImport.setName("Menu.import.config");
		menuConfigImport.addActionListener(this.actionAdapter);
		menuProtosImport.setText(LangModelScheme.getString("Menu.import.protos"));
		menuProtosImport.setName("Menu.import.protos");
		menuProtosImport.addActionListener(this.actionAdapter);
		menuSchemeImportCommit.setText(LangModelScheme.getString("Menu.import.commit"));
		menuSchemeImportCommit.setName("Menu.import.commit");
		menuSchemeImportCommit.addActionListener(this.actionAdapter);
		
		menuExport.setText(LangModelScheme.getString("Menu.export"));
		menuExport.setName("Menu.export");
		menuSchemeExport.setText(LangModelScheme.getString("Menu.export.scheme"));
		menuSchemeExport.setName("Menu.export.scheme");
		menuSchemeExport.addActionListener(this.actionAdapter);
		menuConfigExport.setText(LangModelScheme.getString("Menu.export.config"));
		menuConfigExport.setName("Menu.export.config");
		menuConfigExport.addActionListener(this.actionAdapter);
		menuProtosExport.setText(LangModelScheme.getString("Menu.export.protos"));
		menuProtosExport.setName("Menu.export.protos");
		menuProtosExport.addActionListener(this.actionAdapter);

		menuScheme.add(menuSchemeNew);
		menuScheme.add(menuSchemeLoad);
		menuScheme.addSeparator();
		menuScheme.add(menuSchemeSave);
		menuScheme.add(menuSchemeSaveAs);
		menuScheme.addSeparator();
		menuScheme.add(menuSchemeValidate);
		menuScheme.addSeparator();
		menuScheme.add(menuExport);
		menuExport.add(menuConfigExport);
		menuExport.add(menuProtosExport);
		menuExport.add(menuSchemeExport);
		menuScheme.add(menuImport);
		menuImport.add(menuConfigImport);
		menuImport.add(menuProtosImport);
		menuImport.add(menuSchemeImport);
		menuImport.add(menuSchemeImportCommit);

		menuPath.setName("menuPath");
		menuPath.setText(LangModelSchematics.getString("menuPath"));
		menuPathNew.setName("menuPathNew");
		menuPathNew.setText(LangModelSchematics.getString("menuPathNew"));
		menuPathNew.addActionListener(this.actionAdapter);
		menuPathSave.setName("menuPathSave");
		menuPathSave.setText(LangModelSchematics.getString("menuPathSave"));
		menuPathSave.addActionListener(this.actionAdapter);
		menuPathCancel.setName("menuPathCancel");
		menuPathCancel.setText(LangModelSchematics.getString("menuPathCancel"));
		menuPathCancel.addActionListener(this.actionAdapter);
		menuPathEdit.setName("menuPathEdit");
		menuPathEdit.setText(LangModelSchematics.getString("menuPathEdit"));
		menuPathEdit.addActionListener(this.actionAdapter);

		menuPath.add(menuPathNew);
		menuPath.add(menuPathEdit);
		menuPath.addSeparator();
		menuPath.add(menuPathSave);
		menuPath.addSeparator();
		menuPath.add(menuPathCancel);

		menuReport.setName("menuReport");
		menuReport.setText(LangModelSchematics.getString("menuReport"));
		menuReportCreate.setName("menuReportCreate");
		menuReportCreate.setText(LangModelSchematics.getString("menuReportCreate"));
		menuReportCreate.addActionListener(this.actionAdapter);
		menuReport.add(menuReportCreate);

		menuWindow.setText(LangModelSchematics.getString("menuWindow"));
		menuWindow.setName("menuWindow");
		menuWindowArrange.setText(I18N.getString("Menu.View.WindowArrange"));
		menuWindowArrange.setName(ApplicationModel.MENU_VIEW_ARRANGE);
		menuWindowArrange.addActionListener(this.actionAdapter);
		menuWindowTree.setText(LangModelSchematics.getString("menuWindowTree"));
		menuWindowTree.setName("menuWindowTree");
		menuWindowTree.addActionListener(this.actionAdapter);
		menuWindowScheme.setText(LangModelSchematics.getString("menuWindowScheme"));
		menuWindowScheme.setName("menuWindowScheme");
		menuWindowScheme.addActionListener(this.actionAdapter);
		menuWindowUgo.setText(LangModelSchematics.getString("menuWindowUgo"));
		menuWindowUgo.setName("menuWindowUgo");
		menuWindowUgo.addActionListener(this.actionAdapter);
		menuWindowProps.setText(LangModelSchematics.getString("menuWindowProps"));
		menuWindowProps.setName("menuWindowProps");
		menuWindowProps.addActionListener(this.actionAdapter);
		menuWindowList.setText(LangModelSchematics.getString("menuWindowList"));
		menuWindowList.setName("menuWindowList");
		menuWindowList.addActionListener(this.actionAdapter);

		menuWindow.add(menuWindowArrange);
		menuWindow.addSeparator();
		menuWindow.add(menuWindowTree);
		menuWindow.add(menuWindowScheme);
		menuWindow.add(menuWindowUgo);
		menuWindow.add(menuWindowProps);
		menuWindow.add(menuWindowList);

		this.add(menuScheme);
		this.add(menuPath);
		this.add(menuReport);
		this.add(menuWindow);

		this.addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e });
			}

			public void modelChanged(String[] e) {
				ApplicationModel aModel = SchemeEditorMenuBar.this.getApplicationModel();

				menuScheme.setVisible(aModel.isVisible("menuScheme"));
				menuScheme.setEnabled(aModel.isEnabled("menuScheme"));
				menuSchemeNew.setVisible(aModel.isVisible("menuSchemeNew"));
				menuSchemeNew.setEnabled(aModel.isEnabled("menuSchemeNew"));
				menuSchemeSave.setVisible(aModel.isVisible("menuSchemeSave"));
				menuSchemeSave.setEnabled(aModel.isEnabled("menuSchemeSave"));
				menuSchemeSaveAs.setVisible(aModel.isVisible("menuSchemeSaveAs"));
				menuSchemeSaveAs.setEnabled(aModel.isEnabled("menuSchemeSaveAs"));
				menuSchemeValidate.setVisible(aModel.isVisible("menuSchemeValidate"));
				menuSchemeValidate.setEnabled(aModel.isEnabled("menuSchemeValidate"));
				
				menuSchemeLoad.setVisible(aModel.isVisible("menuSchemeLoad"));
				menuSchemeLoad.setEnabled(aModel.isEnabled("menuSchemeLoad"));
				menuImport.setVisible(aModel.isVisible("Menu.import"));
				menuImport.setEnabled(aModel.isEnabled("Menu.import"));
				menuSchemeImport.setVisible(aModel.isVisible("Menu.import.scheme"));
				menuSchemeImport.setEnabled(aModel.isEnabled("Menu.import.scheme"));
				menuConfigImport.setVisible(aModel.isVisible("Menu.import.config"));
				menuConfigImport.setEnabled(aModel.isEnabled("Menu.import.config"));
				menuProtosImport.setVisible(aModel.isVisible("Menu.import.protos"));
				menuProtosImport.setEnabled(aModel.isEnabled("Menu.import.protos"));
				menuSchemeImportCommit.setVisible(aModel.isVisible("Menu.import.commit"));
				menuSchemeImportCommit.setEnabled(aModel.isEnabled("Menu.import.commit"));
				menuExport.setVisible(aModel.isVisible("Menu.export"));
				menuExport.setEnabled(aModel.isEnabled("Menu.export"));
				menuSchemeExport.setVisible(aModel.isVisible("Menu.export.scheme"));
				menuSchemeExport.setEnabled(aModel.isEnabled("Menu.export.scheme"));
				menuConfigExport.setVisible(aModel.isVisible("Menu.export.config"));
				menuConfigExport.setEnabled(aModel.isEnabled("Menu.export.config"));
				menuProtosExport.setVisible(aModel.isVisible("Menu.export.protos"));
				menuProtosExport.setEnabled(aModel.isEnabled("Menu.export.protos"));

				menuPath.setVisible(aModel.isVisible("menuPath"));
				menuPath.setEnabled(aModel.isEnabled("menuPath"));
				menuPathNew.setVisible(aModel.isVisible("menuPathNew"));
				menuPathNew.setEnabled(aModel.isEnabled("menuPathNew"));
				menuPathSave.setVisible(aModel.isVisible("menuPathSave"));
				menuPathSave.setEnabled(aModel.isEnabled("menuPathSave"));
				menuPathCancel.setVisible(aModel.isVisible("menuPathCancel"));
				menuPathCancel.setEnabled(aModel.isEnabled("menuPathCancel"));
				menuPathEdit.setVisible(aModel.isVisible("menuPathEdit"));
				menuPathEdit.setEnabled(aModel.isEnabled("menuPathEdit"));

				menuReport.setVisible(aModel.isVisible("menuReport"));
				menuReport.setEnabled(aModel.isEnabled("menuReport"));
				menuReportCreate.setVisible(aModel.isVisible("menuReportCreate"));
				menuReportCreate.setEnabled(aModel.isEnabled("menuReportCreate"));

				menuWindow.setVisible(aModel.isVisible("menuWindow"));
				menuWindow.setEnabled(aModel.isEnabled("menuWindow"));
				menuWindowArrange.setVisible(aModel.isVisible(ApplicationModel.MENU_VIEW_ARRANGE));
				menuWindowArrange.setEnabled(aModel.isEnabled(ApplicationModel.MENU_VIEW_ARRANGE));
				menuWindowTree.setVisible(aModel.isVisible("menuWindowTree"));
				menuWindowTree.setEnabled(aModel.isEnabled("menuWindowTree"));
				menuWindowScheme.setVisible(aModel.isVisible("menuWindowScheme"));
				menuWindowScheme.setEnabled(aModel.isEnabled("menuWindowScheme"));
				menuWindowUgo.setVisible(aModel.isVisible("menuWindowUgo"));
				menuWindowUgo.setEnabled(aModel.isEnabled("menuWindowUgo"));
				menuWindowProps.setVisible(aModel.isVisible("menuWindowProps"));
				menuWindowProps.setEnabled(aModel.isEnabled("menuWindowProps"));
				menuWindowList.setVisible(aModel.isVisible("menuWindowList"));
				menuWindowList.setEnabled(aModel.isEnabled("menuWindowList"));
			}
		});
	}
}
