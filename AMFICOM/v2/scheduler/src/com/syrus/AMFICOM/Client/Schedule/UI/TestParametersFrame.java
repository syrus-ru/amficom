package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class TestParametersFrame extends JInternalFrame implements
		OperationListener {

	private ApplicationContext	aContext;
	private Dispatcher			dispatcher;
	private TestParametersPanel	panel;

	public TestParametersFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initModule(aContext.getDispatcher());
	}

	private void jbInit() throws Exception {
		setTitle(LangModelSchedule.getString("Measurement_options")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		panel = new TestParametersPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this,
				TestParametersPanel.COMMAND_CHANGE_PORT_TYPE);
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		System.out.println(getClass().getName() + "\tcommandName:"
				+ commandName);
		System.out.println("obj:" + obj.getClass().getName() + "\t" + obj);
		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if (tue.TEST_SELECTED) {
				KIS kis = (KIS) Pool.get(KIS.typ, test.getKisId());
				Vector ports = kis.access_ports;
				for (Enumeration e = ports.elements(); e.hasMoreElements();) {
					AccessPort port = (AccessPort) e.nextElement();
					//System.out.println("portId:" + port.type_id);
					if (port.type_id
							.equals(ElementsTreePanel.ACCESSPORT_NAME_REFLECTOMETER)) {
						if (!panel
								.isParameterPanelExists(ReflectometryTestPanel.PANEL_NAME)) {
							dispatcher
									.notify(new OperationEvent(
											new ReflectometryTestPanel(
													aContext, port, test),
											0,
											TestParametersPanel.COMMAND_ADD_PARAM_PANEL));
						}
					}
				}

				panel.setTest(test);
			} else {
//				nothing
			}
		} else if (commandName
				.equals(TestParametersPanel.COMMAND_CHANGE_PORT_TYPE)) {
			AccessPort port = (AccessPort) obj;
			if (port.type_id
					.equals(ElementsTreePanel.ACCESSPORT_NAME_REFLECTOMETER)) {
				if (!panel
						.isParameterPanelExists(ReflectometryTestPanel.PANEL_NAME)) {
					dispatcher.notify(new OperationEvent(
							new ReflectometryTestPanel(aContext, port), 0,
							TestParametersPanel.COMMAND_ADD_PARAM_PANEL));
				}
			}

		}
	}
}