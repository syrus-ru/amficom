/*-
 * $Id: SchemeEditorMenuBar.java,v 1.3 2005/06/09 10:53:52 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.model.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/06/09 10:53:52 $
 * @module schemeclient_v1
 */

public class SchemeEditorMenuBar extends AbstractMainMenuBar {

	public SchemeEditorMenuBar(ApplicationModel aModel) {
		super(aModel);
	}

	protected void addMenuItems() {
//		SchemeEditorMenuBar_this_actionAdapter actionAdapter = new SchemeEditorMenuBar_this_actionAdapter(
//				this);

		final JMenu menuScheme = new JMenu();
		final JMenuItem menuSchemeNew = new JMenuItem();
		final JMenuItem menuSchemeLoad = new JMenuItem();
		final JMenuItem menuSchemeSave = new JMenuItem();
		final JMenuItem menuSchemeSaveAs = new JMenuItem();
		final JMenuItem menuInsertToCatalog = new JMenuItem();
		final JMenuItem menuSchemeImport = new JMenuItem();
		final JMenuItem menuSchemeExport = new JMenuItem();

		final JMenu menuPath = new JMenu();
		final JMenuItem menuPathNew = new JMenuItem();
		final JMenuItem menuPathEdit = new JMenuItem();
		final JMenuItem menuPathSave = new JMenuItem();
		final JMenuItem menuPathCancel = new JMenuItem();
		final JMenuItem menuPathDelete = new JMenuItem();
		final JMenuItem menuPathAddStart = new JMenuItem();
		final JMenuItem menuPathAddEnd = new JMenuItem();
		final JMenuItem menuPathAddLink = new JMenuItem();
		final JMenuItem menuPathRemoveLink = new JMenuItem();
		final JMenuItem menuPathAutoCreate = new JMenuItem();

		final JMenu menuReport = new JMenu();
		final JMenuItem menuReportCreate = new JMenuItem();
		
		final JMenu menuWindow = new JMenu();
		final JMenuItem menuWindowArrange = new JMenuItem();
		final JMenuItem menuWindowTree = new JMenuItem();
		final JMenuItem menuWindowScheme = new JMenuItem();
		final JMenuItem menuWindowCatalog = new JMenuItem();
		final JMenuItem menuWindowUgo = new JMenuItem();
		final JMenuItem menuWindowProps = new JMenuItem();
		final JMenuItem menuWindowList = new JMenuItem();
		
		menuScheme.setText(LangModelSchematics.getString("menuScheme"));
		menuScheme.setName("menuScheme");
		menuSchemeNew.setText(LangModelSchematics.getString("menuSchemeNew"));
		menuSchemeNew.setName("menuSchemeNew");
		menuSchemeNew.addActionListener(actionAdapter);
		menuSchemeSave.setText(LangModelSchematics.getString("menuSchemeSave"));
		menuSchemeSave.setName("menuSchemeSave");
		menuSchemeSave.addActionListener(actionAdapter);
		menuSchemeSaveAs.setText(LangModelSchematics.getString("menuSchemeSaveAs"));
		menuSchemeSaveAs.setName("menuSchemeSaveAs");
		menuSchemeSaveAs.addActionListener(actionAdapter);
		menuSchemeLoad.setText(LangModelSchematics.getString("menuSchemeLoad"));
		menuSchemeLoad.setName("menuSchemeLoad");
		menuSchemeLoad.addActionListener(actionAdapter);
		menuInsertToCatalog.setText(LangModelSchematics
				.getString("menuInsertToCatalog"));
		menuInsertToCatalog.setName("menuInsertToCatalog");
		menuInsertToCatalog.addActionListener(actionAdapter);
		menuSchemeImport.setText(LangModelSchematics.getString("menuSchemeImport"));
		menuSchemeImport.setName("menuSchemeImport");
		menuSchemeImport.addActionListener(actionAdapter);
		menuSchemeExport.setText(LangModelSchematics.getString("menuSchemeExport"));
		menuSchemeExport.setName("menuSchemeExport");
		menuSchemeExport.addActionListener(actionAdapter);

		menuScheme.add(menuSchemeNew);
		menuScheme.add(menuSchemeLoad);
		menuScheme.addSeparator();
		menuScheme.add(menuSchemeSave);
		menuScheme.add(menuSchemeSaveAs);
		menuScheme.addSeparator();
		menuScheme.add(menuInsertToCatalog);
		menuScheme.add(menuSchemeExport);
		menuScheme.add(menuSchemeImport);

		menuPath.setName("menuPath");
		menuPath.setText(LangModelSchematics.getString("menuPath"));
		menuPathNew.setName("menuPathNew");
		menuPathNew.setText(LangModelSchematics.getString("menuPathNew"));
		menuPathNew.addActionListener(actionAdapter);
		menuPathAddStart.setName("menuPathAddStart");
		menuPathAddStart.setText(LangModelSchematics.getString("menuPathAddStart"));
		menuPathAddStart.addActionListener(actionAdapter);
		menuPathAddEnd.setName("menuPathAddEnd");
		menuPathAddEnd.setText(LangModelSchematics.getString("menuPathAddEnd"));
		menuPathAddEnd.addActionListener(actionAdapter);
		menuPathAddLink.setName("menuPathAddLink");
		menuPathAddLink.setText(LangModelSchematics.getString("menuPathAddLink"));
		menuPathAddLink.addActionListener(actionAdapter);
		menuPathRemoveLink.setName("menuPathRemoveLink");
		menuPathRemoveLink.setText(LangModelSchematics
				.getString("menuPathRemoveLink"));
		menuPathRemoveLink.addActionListener(actionAdapter);
		menuPathAutoCreate.setName("menuPathAutoCreate");
		menuPathAutoCreate.setText(LangModelSchematics
				.getString("menuPathAutoCreate"));
		menuPathAutoCreate.addActionListener(actionAdapter);
		menuPathSave.setName("menuPathSave");
		menuPathSave.setText(LangModelSchematics.getString("menuPathSave"));
		menuPathSave.addActionListener(actionAdapter);
		menuPathCancel.setName("menuPathCancel");
		menuPathCancel.setText(LangModelSchematics.getString("menuPathCancel"));
		menuPathCancel.addActionListener(actionAdapter);
		menuPathDelete.setName("menuPathDelete");
		menuPathDelete.setText(LangModelSchematics.getString("menuPathDelete"));
		menuPathDelete.addActionListener(actionAdapter);
		menuPathEdit.setName("menuPathEdit");
		menuPathEdit.setText(LangModelSchematics.getString("menuPathEdit"));
		menuPathEdit.addActionListener(actionAdapter);
		menuPath.add(menuPathNew);
		menuPath.add(menuPathEdit);
		menuPath.add(menuPathSave);
		menuPath.add(menuPathDelete);
		menuPath.add(menuPathCancel);
		menuPath.addSeparator();
		menuPath.add(menuPathAddStart);
		menuPath.add(menuPathAddEnd);
		menuPath.addSeparator();
		menuPath.add(menuPathAddLink);
		menuPath.add(menuPathRemoveLink);
		menuPath.addSeparator();
		menuPath.add(menuPathAutoCreate);

		menuReport.setName("menuReport");
		menuReport.setText(LangModelSchematics.getString("menuReport"));
		menuReportCreate.setName("menuReportCreate");
		menuReportCreate.setText(LangModelSchematics.getString("menuReportCreate"));
		menuReportCreate.addActionListener(actionAdapter);
		menuReport.add(menuReportCreate);

		menuWindow.setText(LangModelSchematics.getString("menuWindow"));
		menuWindow.setName("menuWindow");
		menuWindowArrange.setText(LangModelSchematics
				.getString("menuWindowArrange"));
		menuWindowArrange.setName("menuWindowArrange");
		menuWindowArrange.addActionListener(actionAdapter);
		menuWindowTree.setText(LangModelSchematics.getString("menuWindowTree"));
		menuWindowTree.setName("menuWindowTree");
		menuWindowTree.addActionListener(actionAdapter);
		menuWindowScheme.setText(LangModelSchematics.getString("menuWindowScheme"));
		menuWindowScheme.setName("menuWindowScheme");
		menuWindowScheme.addActionListener(actionAdapter);
		menuWindowCatalog.setText(LangModelSchematics
				.getString("menuWindowCatalog"));
		menuWindowCatalog.setName("menuWindowCatalog");
		menuWindowCatalog.addActionListener(actionAdapter);
		menuWindowUgo.setText(LangModelSchematics.getString("menuWindowUgo"));
		menuWindowUgo.setName("menuWindowUgo");
		menuWindowUgo.addActionListener(actionAdapter);
		menuWindowProps.setText(LangModelSchematics.getString("menuWindowProps"));
		menuWindowProps.setName("menuWindowProps");
		menuWindowProps.addActionListener(actionAdapter);
		menuWindowList.setText(LangModelSchematics.getString("menuWindowList"));
		menuWindowList.setName("menuWindowList");
		menuWindowList.addActionListener(actionAdapter);

		menuWindow.add(menuWindowArrange);
		menuWindow.addSeparator();
		menuWindow.add(menuWindowTree);
		menuWindow.add(menuWindowScheme);
		menuWindow.add(menuWindowUgo);
		menuWindow.add(menuWindowProps);
		menuWindow.add(menuWindowList);
		menuWindow.add(menuWindowCatalog);

		add(menuScheme);
		add(menuPath);
		add(menuReport);
		add(menuWindow);


		this.addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] {e});
			}
	
	public void modelChanged(String e[]) {
		ApplicationModel aModel = SchemeEditorMenuBar.this.getApplicationModel();
		
		menuScheme.setVisible(aModel.isVisible("menuScheme"));
		menuScheme.setEnabled(aModel.isEnabled("menuScheme"));
		menuSchemeNew.setVisible(aModel.isVisible("menuSchemeNew"));
		menuSchemeNew.setEnabled(aModel.isEnabled("menuSchemeNew"));
		menuSchemeSave.setVisible(aModel.isVisible("menuSchemeSave"));
		menuSchemeSave.setEnabled(aModel.isEnabled("menuSchemeSave"));
		menuSchemeSaveAs.setVisible(aModel.isVisible("menuSchemeSaveAs"));
		menuSchemeSaveAs.setEnabled(aModel.isEnabled("menuSchemeSaveAs"));
		menuSchemeLoad.setVisible(aModel.isVisible("menuSchemeLoad"));
		menuSchemeLoad.setEnabled(aModel.isEnabled("menuSchemeLoad"));
		menuInsertToCatalog.setVisible(aModel.isVisible("menuInsertToCatalog"));
		menuInsertToCatalog.setEnabled(aModel.isEnabled("menuInsertToCatalog"));
		menuSchemeImport.setVisible(aModel.isVisible("menuSchemeImport"));
		menuSchemeImport.setEnabled(aModel.isEnabled("menuSchemeImport"));
		menuSchemeExport.setVisible(aModel.isVisible("menuSchemeExport"));
		menuSchemeExport.setEnabled(aModel.isEnabled("menuSchemeExport"));

		menuPath.setVisible(aModel.isVisible("menuPath"));
		menuPath.setEnabled(aModel.isEnabled("menuPath"));
		menuPathNew.setVisible(aModel.isVisible("menuPathNew"));
		menuPathNew.setEnabled(aModel.isEnabled("menuPathNew"));
		menuPathAddStart.setVisible(aModel.isVisible("menuPathAddStart"));
		menuPathAddStart.setEnabled(aModel.isEnabled("menuPathAddStart"));
		menuPathAddEnd.setVisible(aModel.isVisible("menuPathAddEnd"));
		menuPathAddEnd.setEnabled(aModel.isEnabled("menuPathAddEnd"));
		menuPathAddLink.setVisible(aModel.isVisible("menuPathAddLink"));
		menuPathAddLink.setEnabled(aModel.isEnabled("menuPathAddLink"));
		menuPathRemoveLink.setVisible(aModel.isVisible("menuPathRemoveLink"));
		menuPathRemoveLink.setEnabled(aModel.isEnabled("menuPathRemoveLink"));
		menuPathAutoCreate.setVisible(aModel.isVisible("menuPathAutoCreate"));
		menuPathAutoCreate.setEnabled(aModel.isEnabled("menuPathAutoCreate"));
		menuPathSave.setVisible(aModel.isVisible("menuPathSave"));
		menuPathSave.setEnabled(aModel.isEnabled("menuPathSave"));
		menuPathCancel.setVisible(aModel.isVisible("menuPathCancel"));
		menuPathCancel.setEnabled(aModel.isEnabled("menuPathCancel"));
		menuPathDelete.setVisible(aModel.isVisible("menuPathDelete"));
		menuPathDelete.setEnabled(aModel.isEnabled("menuPathDelete"));
		menuPathEdit.setVisible(aModel.isVisible("menuPathEdit"));
		menuPathEdit.setEnabled(aModel.isEnabled("menuPathEdit"));

		menuReport.setVisible(aModel.isVisible("menuReport"));
		menuReport.setEnabled(aModel.isEnabled("menuReport"));
		menuReportCreate.setVisible(aModel.isVisible("menuReportCreate"));
		menuReportCreate.setEnabled(aModel.isEnabled("menuReportCreate"));

		menuWindow.setVisible(aModel.isVisible("menuWindow"));
		menuWindow.setEnabled(aModel.isEnabled("menuWindow"));
		menuWindowArrange.setVisible(aModel.isVisible("menuWindowArrange"));
		menuWindowArrange.setEnabled(aModel.isEnabled("menuWindowArrange"));
		menuWindowTree.setVisible(aModel.isVisible("menuWindowTree"));
		menuWindowTree.setEnabled(aModel.isEnabled("menuWindowTree"));
		menuWindowScheme.setVisible(aModel.isVisible("menuWindowScheme"));
		menuWindowScheme.setEnabled(aModel.isEnabled("menuWindowScheme"));
		menuWindowCatalog.setVisible(aModel.isVisible("menuWindowCatalog"));
		menuWindowCatalog.setEnabled(aModel.isEnabled("menuWindowCatalog"));
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
