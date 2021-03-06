/*-
 * $Id: ElementsEditorToolBar.java,v 1.6 2005/08/08 11:58:06 arseniy Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
 */

public class ElementsEditorToolBar extends AbstractMainToolBar {
	private static final long serialVersionUID = 1002579172632953867L;

	public ElementsEditorToolBar() {
		initItems();
	}

	private void initItems() {
		
		final JButton componentNew = new JButton();
		componentNew.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_NEW));
		componentNew.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		componentNew.setToolTipText(LangModelSchematics.getString("menuComponentNew"));
		componentNew.setName("menuComponentNew");
		componentNew.addActionListener(super.actionListener);

		final JButton componentSave = new JButton();
		componentSave.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_SAVE));
		componentSave.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		componentSave.setToolTipText(LangModelSchematics.getString("menuComponentSave"));
		componentSave.setName("menuComponentSave");
		componentSave.addActionListener(super.actionListener);
		
		addSeparator();
		add(componentNew);
		add(componentSave);

		addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e });
			}

			public void modelChanged(String e[]) {
				ApplicationModel aModel = ElementsEditorToolBar.this.getApplicationModel();

				componentNew.setVisible(aModel.isVisible("menuComponentNew"));
				componentNew.setEnabled(aModel.isEnabled("menuComponentNew"));
				componentSave.setVisible(aModel.isVisible("menuComponentSave"));
				componentSave.setEnabled(aModel.isEnabled("menuComponentSave"));
			}
		});
	}
}



