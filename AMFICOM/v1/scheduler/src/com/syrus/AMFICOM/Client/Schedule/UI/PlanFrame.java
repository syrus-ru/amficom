package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.SystemColor;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.Commandable;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;

public class PlanFrame extends JInternalFrame implements Commandable {

	private JToolBar	toolBar;
	private PlanPanel	mainPanel;
	private Command command;

	public PlanFrame(ApplicationContext aContext) {
		setTitle(LangModelSchedule.getString("Plan.Title")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setDoubleBuffered(true);

		JScrollPane scroll = new JScrollPane();

		this.mainPanel = new PlanPanel(scroll, aContext);
		this.toolBar = this.mainPanel.getToolBar().getToolBar();
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
	
	public PlanPanel getPlanPanel(){
		return this.mainPanel;
	}
}