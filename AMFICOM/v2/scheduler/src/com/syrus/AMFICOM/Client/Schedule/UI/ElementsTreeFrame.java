package com.syrus.AMFICOM.Client.Schedule.UI;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class ElementsTreeFrame extends JInternalFrame {

	private ApplicationContext	aContext;

	public ElementsTreeFrame(ApplicationContext aContext) {
		this.aContext = aContext;

		setTitle(LangModelSchedule.getString("Comonents_Tree")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
	}

	public void init() {
		setContentPane(new ElementsTreePanel(aContext));
	}
}