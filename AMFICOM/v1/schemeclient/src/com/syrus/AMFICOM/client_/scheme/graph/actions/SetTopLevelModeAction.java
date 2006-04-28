/*
 * $Id: SetTopLevelModeAction.java,v 1.8 2006/04/28 09:01:33 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.UI.AComboBox;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client_.scheme.ElementsPermissionManager;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2006/04/28 09:01:33 $
 * @module schemeclient
 */

public class SetTopLevelModeAction extends AbstractAction {
	private static final long serialVersionUID = -8573307466993168772L;

	UgoTabbedPane pane;

	public SetTopLevelModeAction(UgoTabbedPane pane) {
		super(Constants.TOP_LEVEL_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		if (this.pane instanceof SchemeTabbedPane) {
			
			String topLevel = "Схематичное отображение";
			String normalLevel = "Точное отображение";
			AComboBox box = new AComboBox(new String[] { normalLevel, topLevel });
			int res = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(), box, "Режим работы схемы:",
					JOptionPane.OK_CANCEL_OPTION);

			boolean isTop = box.getSelectedItem().equals(topLevel);
			
			if (isTop && SchemePermissionManager.isSavingAllowed()
					&& !((SchemeTabbedPane)this.pane).confirmUnsavedChanges(this.pane.getCurrentPanel())) {
				return;
			}
			
			if (res == JOptionPane.OK_OPTION) {
				((SchemeTabbedPane)this.pane).getCurrentPanel().setTopLevelSchemeMode(isTop);
				this.pane.setGraphChanged(false);
			}
		}
	}
}
