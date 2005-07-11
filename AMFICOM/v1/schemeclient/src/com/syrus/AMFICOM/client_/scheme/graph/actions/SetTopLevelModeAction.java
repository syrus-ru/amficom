/*
 * $Id: SetTopLevelModeAction.java,v 1.3 2005/07/11 12:31:38 stas Exp $
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
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class SetTopLevelModeAction extends AbstractAction {
	UgoTabbedPane pane;

	public SetTopLevelModeAction(UgoTabbedPane pane) {
		super(Constants.TOP_LEVEL_MODE);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		String topLevel = "Схематичное отображение";
		String normalLevel = "Точное отображение";
		AComboBox box = new AComboBox(new String[] { normalLevel, topLevel });
		int res = JOptionPane.showConfirmDialog(pane.getGraph(), box, "Режим работы схемы:",
				JOptionPane.OK_CANCEL_OPTION);

		if (res == JOptionPane.OK_OPTION)
			pane.getGraph().setTopLevelSchemeMode(box.getSelectedItem().equals(topLevel));
	}
}
