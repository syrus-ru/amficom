package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Schedule.ScheduleMainFrame;
import com.syrus.AMFICOM.Client.Scheduler.General.I18N;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class TimeParametersFrame extends JInternalFrame implements
		OperationListener {

	//	private ApplicationContext aContext;

	private Dispatcher			dispatcher;

	private TimeParametersPanel	panel;

	public TimeParametersFrame(ApplicationContext aContext) {
		//		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		setTitle(I18N.getString("TemporalType.Title")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		panel = new TimeParametersPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.typ);
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		if (ScheduleMainFrame.DEBUG >= 3)
				System.out.println(getClass().getName() + " commandName: " //$NON-NLS-1$
						+ commandName);
		//		int id = ae.getID();
		//		Object obj = ae.getSource();

		if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if (tue.TEST_SELECTED) {
				TestRequest treq = (TestRequest) Pool.get(TestRequest.typ,
						test.getRequestId());
				if (treq != null) {
					panel.setTestRequest(treq);
				}
			} else {
				// nothing
			}
		}
	}

}