/*-
 * $Id: ElementsEditorMenuBar.java,v 1.9 2006/02/15 12:18:10 stas Exp $
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

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2006/02/15 12:18:10 $
 * @module schemeclient
 */

public class ElementsEditorMenuBar extends AbstractMainMenuBar {
	private static final long serialVersionUID = -6603705967256780208L;

	public ElementsEditorMenuBar(ApplicationModel aModel) {
		super(aModel);
	}

	@Override
	protected void addMenuItems() {
		final JMenu menuComponent = new JMenu();
		final JMenuItem menuComponentNew = new JMenuItem();
		final JMenuItem menuComponentSave = new JMenuItem();

		final JMenu menuWindow = new JMenu();
		final JMenuItem menuWindowArrange = new JMenuItem();
		final JMenuItem menuWindowTree = new JMenuItem();
		final JMenuItem menuWindowScheme = new JMenuItem();
		final JMenuItem menuWindowUgo = new JMenuItem();
		final JMenuItem menuWindowProps = new JMenuItem();
		final JMenuItem menuWindowList = new JMenuItem();
		
		menuComponent.setText(LangModelSchematics.getString("menuComponent"));
		menuComponent.setName("menuComponent");
		menuComponentNew.setText(LangModelSchematics.getString("menuComponentNew"));
		menuComponentNew.setName("menuComponentNew");
		menuComponentNew.addActionListener(this.actionAdapter);
		menuComponentSave.setText(LangModelSchematics.getString("menuComponentSave"));
		menuComponentSave.setName("menuComponentSave");
		menuComponentSave.addActionListener(this.actionAdapter);

		menuComponent.add(menuComponentNew);
		menuComponent.add(menuComponentSave);

		menuWindow.setText(LangModelSchematics.getString("menuWindow"));
		menuWindow.setName("menuWindow");
		menuWindowArrange.setText(I18N.getString("Menu.View.WindowArrange"));
		menuWindowArrange.setName(ApplicationModel.MENU_VIEW_ARRANGE);
		menuWindowArrange.addActionListener(this.actionAdapter);
		menuWindowTree.setText(LangModelSchematics.getString("menuWindowTree"));
		menuWindowTree.setName("menuWindowTree");
		menuWindowTree.addActionListener(this.actionAdapter);
		menuWindowScheme.setText(LangModelSchematics.getString("menuWindowElements"));
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

		this.add(menuComponent);
		this.add(menuWindow);

	
	this.addApplicationModelListener(new ApplicationModelListener() {
		public void modelChanged(String e) {
			modelChanged(new String[] { e});
		}

	public void modelChanged(String e[])
	{
		ApplicationModel aModel = ElementsEditorMenuBar.this.getApplicationModel();
		menuComponentSave.setVisible(aModel.isVisible("menuComponentSave"));
		menuComponentSave.setEnabled(aModel.isEnabled("menuComponentSave"));
		menuComponentNew.setVisible(aModel.isVisible("menuComponentNew"));
		menuComponentNew.setEnabled(aModel.isEnabled("menuComponentNew"));

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

