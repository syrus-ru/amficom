package com.syrus.AMFICOM.Client.Schedule.UI;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;

public class ElementsTreeFrame extends JInternalFrame {

	private ApplicationContext	aContext;

	public ElementsTreeFrame(ApplicationContext aContext) {
		this.aContext = aContext;

		setTitle("Дерево компонентов");
		setFrameIcon(UIUtil.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
	}

	public void init() {
		setContentPane(new ElementsTreePanel(aContext));
	}
}