package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class TimeParametersFrame extends JInternalFrame implements OperationListener {

	//	private ApplicationContext aContext;

	private Dispatcher			dispatcher;

	private TimeParametersPanel	panel;

	private Command				command;

	public TimeParametersFrame(ApplicationContext aContext) {
		//		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		setTitle(LangModelSchedule.getString("TemporalType.Title")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.panel = new TimeParametersPanel(aContext);
		this.getContentPane().add(this.panel, BorderLayout.CENTER);
		this.command = new WindowCommand(this);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
	}
	
	public void unregisterDispatcher(){
		this.dispatcher.unregister(this, TestUpdateEvent.TYPE);
		this.panel.unregisterDispatcher();
	}
	

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		//		int id = ae.getID();
		//		Object obj = ae.getSource();

		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if (tue.testSelected) {
				TestRequest treq = (TestRequest) Pool.get(TestRequest.TYPE, test.getRequestId());
				if (treq != null) {
					this.panel.setTestRequest(treq);
				}
			} else {
				// nothing
			}
		}
	}

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}
}