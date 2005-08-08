/*-
 * $Id: SchemeEditorToolBar.java,v 1.7 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class SchemeEditorToolBar extends AbstractMainToolBar {
	private static final long serialVersionUID = 7496638136912951178L;

	public SchemeEditorToolBar() {
		initItems();
	}

	private void initItems() {

		final JButton schemeNew = new JButton();
		schemeNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/new.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		schemeNew.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));		
		schemeNew.setToolTipText(LangModelSchematics.getString("menuSchemeNew"));
		schemeNew.setName("menuSchemeNew");
		schemeNew.addActionListener(super.actionListener);

		final JButton schemeSave = new JButton();
		schemeSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/save.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		schemeSave.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		schemeSave.setToolTipText(LangModelSchematics.getString("menuSchemeSave"));
		schemeSave.setName("menuSchemeSave");
		schemeSave.addActionListener(super.actionListener);

		final JButton schemeLoad = new JButton();
		schemeLoad.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/openfile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		schemeLoad.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		schemeLoad.setToolTipText(LangModelSchematics.getString("menuSchemeLoad"));
		schemeLoad.setName("menuSchemeLoad");
		schemeLoad.addActionListener(super.actionListener);

		addSeparator();
		add(schemeNew);
		add(schemeLoad);
		add(schemeSave);
		
		addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] {e});
			}
			
			public void modelChanged(String e[]) {
				ApplicationModel aModel = SchemeEditorToolBar.this.getApplicationModel();
				
				schemeNew.setVisible(aModel.isVisible("menuSchemeNew"));
				schemeNew.setEnabled(aModel.isEnabled("menuSchemeNew"));
				schemeSave.setVisible(aModel.isVisible("menuSchemeSave"));
				schemeSave.setEnabled(aModel.isEnabled("menuSchemeSave"));
				schemeLoad.setVisible(aModel.isVisible("menuSchemeLoad"));
				schemeLoad.setEnabled(aModel.isEnabled("menuSchemeLoad"));
			}
		});
	}
}
