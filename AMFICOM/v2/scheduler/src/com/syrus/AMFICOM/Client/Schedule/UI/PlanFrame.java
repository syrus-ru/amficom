package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class PlanFrame extends JInternalFrame {

	//private ApplicationContext aContext;
	private PlanToolBar	toolBar;
	private PlanPanel	mainPanel;
	private Command command;

	public PlanFrame(ApplicationContext aContext) {
		//this.aContext = aContext;

		setTitle(LangModelSchedule.getString("Plan.Title")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setDoubleBuffered(true);

		JScrollPane scroll = new JScrollPane();

		//		PlanLayeredPanel panel = new PlanLayeredPanel();
		this.mainPanel = new PlanPanel(scroll, aContext);
		this.toolBar = this.mainPanel.getToolBar();
		//		panel.setGraphPanel(mainPanel);
		scroll.getViewport().add(this.mainPanel);

		getContentPane().setBackground(SystemColor.window);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(this.toolBar, BorderLayout.NORTH);
		getContentPane().add(scroll, BorderLayout.CENTER);
		this.command = new WindowCommand(this);
	}
	

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}
}