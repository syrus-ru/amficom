package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.Commandable;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;

public class TestRequestFrame extends JInternalFrame  implements Commandable {

	private TestRequestPanel	panel;
	private Command				command;

	public TestRequestFrame(ApplicationContext aContext) {
		//this.aContext = aContext;
		setTitle(LangModelSchedule.getString("TestOptions")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
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