/*
 * $Id: SetTopLevelModeAction.java,v 1.5 2005/08/08 11:58:07 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:07 $
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
		String topLevel = "Схематичное отображение";
		String normalLevel = "Точное отображение";
		AComboBox box = new AComboBox(new String[] { normalLevel, topLevel });
		int res = JOptionPane.showConfirmDialog(this.pane.getGraph(), box, "Режим работы схемы:",
				JOptionPane.OK_CANCEL_OPTION);

		if (res == JOptionPane.OK_OPTION)
			this.pane.getGraph().setTopLevelSchemeMode(box.getSelectedItem().equals(topLevel));
	}
}
