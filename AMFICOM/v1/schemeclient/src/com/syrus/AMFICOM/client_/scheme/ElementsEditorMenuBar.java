/*-
 * $Id: ElementsEditorMenuBar.java,v 1.4 2005/06/22 10:16:05 stas Exp $
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

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/06/22 10:16:05 $
 * @module schemeclient_v1
 */

public class ElementsEditorMenuBar extends AbstractMainMenuBar
{

	public ElementsEditorMenuBar(ApplicationModel aModel)
	{
		super(aModel);
	}

	protected void addMenuItems()
	{
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
		menuComponentNew.addActionListener(actionAdapter);
		menuComponentSave.setText(LangModelSchematics.getString("menuComponentSave"));
		menuComponentSave.setName("menuComponentSave");
		menuComponentSave.addActionListener(actionAdapter);

		menuComponent.add(menuComponentNew);
		menuComponent.add(menuComponentSave);

		menuWindow.setText(LangModelSchematics.getString("menuWindow"));
		menuWindow.setName("menuWindow");
		menuWindowArrange.setText(LangModelSchematics.getString("menuWindowArrange"));
		menuWindowArrange.setName("menuWindowArrange");
		menuWindowArrange.addActionListener(actionAdapter);
		menuWindowTree.setText(LangModelSchematics.getString("menuWindowTree"));
		menuWindowTree.setName("menuWindowTree");
		menuWindowTree.addActionListener(actionAdapter);
		menuWindowScheme.setText(LangModelSchematics.getString("menuWindowElements"));
		menuWindowScheme.setName("menuWindowScheme");
		menuWindowScheme.addActionListener(actionAdapter);
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
		menuWindowArrange.setVisible(aModel.isVisible("menuWindowArrange"));
		menuWindowArrange.setEnabled(aModel.isEnabled("menuWindowArrange"));
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

