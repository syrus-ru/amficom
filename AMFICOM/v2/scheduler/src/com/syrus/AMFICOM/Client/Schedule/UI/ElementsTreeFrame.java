package com.syrus.AMFICOM.Client.Schedule.UI;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.TestUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class ElementsTreeFrame extends JInternalFrame {

	private ApplicationContext	aContext;

	private Command				command;

	private ElementsTreePanel	panel;

	public ElementsTreeFrame(ApplicationContext aContext) {
		this.aContext = aContext;

		setTitle(LangModelSchedule.getString("Comonents_Tree")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.command = new WindowCommand(this);
	}

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}

	public void init() {
		if (this.panel == null)
			this.panel = new ElementsTreePanel(this.aContext);
		setContentPane(this.panel);
	}

	public void unregisterDispatcher() {
		this.panel.unregisterDispatcher();
	}
}