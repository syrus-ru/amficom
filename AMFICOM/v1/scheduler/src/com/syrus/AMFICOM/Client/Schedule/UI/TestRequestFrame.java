package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class TestRequestFrame extends JInternalFrame {

	private TestRequestPanel	panel;
	private Command				command;

	public TestRequestFrame(ApplicationContext aContext) {
		//this.aContext = aContext;
		setTitle(LangModelSchedule.getString("TestOptions")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		this.panel = new TestRequestPanel(aContext);
		this.getContentPane().add(this.panel, BorderLayout.CENTER);
		this.command = new WindowCommand(this);

	}	

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}
}