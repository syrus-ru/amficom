package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class TimeParametersFrame extends JInternalFrame implements
		OperationListener {

	//	private ApplicationContext aContext;

	private Dispatcher			dispatcher;

	private TimeParametersPanel	panel;

	public TimeParametersFrame(ApplicationContext aContext) {
		//		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		setTitle(LangModelSchedule.getString("TemporalType.Title")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		panel = new TimeParametersPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:"
				+ commandName, getClass().getName());
		//		int id = ae.getID();
		//		Object obj = ae.getSource();

		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if (tue.testSelected) {
				TestRequest treq = (TestRequest) Pool.get(TestRequest.TYP, test
						.getRequestId());
				if (treq != null) {
					panel.setTestRequest(treq);
				}
			} else {
				// nothing
			}
		}
	}

}
