package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class PlanFrame extends JInternalFrame {

	ApplicationContext	aContext;
	PlanToolBar			toolBar;
	PlanPanel			mainPanel;

	public PlanFrame(ApplicationContext aContext) {
		this.aContext = aContext;

		setTitle(LangModelSchedule.getString("Plan.Title")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setDoubleBuffered(true);

		JScrollPane scroll = new JScrollPane();

		//		PlanLayeredPanel panel = new PlanLayeredPanel();
		mainPanel = new PlanPanel(scroll, aContext);
		toolBar = mainPanel.getToolBar();
		//		panel.setGraphPanel(mainPanel);
		scroll.getViewport().add(mainPanel);

		getContentPane().setBackground(SystemColor.window);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolBar, BorderLayout.NORTH);
		getContentPane().add(scroll, BorderLayout.CENTER);
		toolBar.applyChanges();
	}

}
