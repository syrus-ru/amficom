package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.TestType;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Schedule.ScheduleMainFrame;
import com.syrus.util.*;

public class TimeParametersFrame extends JInternalFrame implements
		OperationListener {

	private ApplicationContext	aContext;

	private Dispatcher			dispatcher;

	private TimeParametersPanel	panel;

	public TimeParametersFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		setTitle("Временные параметры");
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/general.gif")));
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
		if (ScheduleMainFrame.DEBUG)
				System.out.println(getClass().getName() + " commandName: "
						+ commandName);
		int id = ae.getID();
		Object obj = ae.getSource();

		if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if (tue.TEST_SELECTED) {
				TestRequest treq = (TestRequest) Pool.get(TestRequest.typ,
						test.request_id);
				if (treq != null) panel.setTestRequest(treq);
			} else {
			}
		}
	}	

}