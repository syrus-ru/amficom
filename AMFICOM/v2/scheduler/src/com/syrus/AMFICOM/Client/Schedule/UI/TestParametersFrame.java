package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;

public class TestParametersFrame
	extends JInternalFrame
	implements OperationListener {
	private ApplicationContext aContext;
	private Dispatcher dispatcher;
	private TestParametersPanel panel;

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
		setTitle("Измерительные параметры");
		setFrameIcon(
			new ImageIcon(
				Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		panel = new TestParametersPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.typ);
		this.dispatcher.register(this, TestParametersPanel.COMMAND_CHANGE_PORT_TYPE);
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if (tue.TEST_SELECTED) {
				KIS kis = (KIS) Pool.get(KIS.typ, test.kis_id);
				Vector ports = kis.access_ports;
				for (Enumeration e = ports.elements(); e.hasMoreElements();) {
					AccessPort port = (AccessPort) e.nextElement();
					//System.out.println("portId:" + port.type_id);
					if (port
						.type_id
						.equals(
							ElementsTreePanel.ACCESSPORT_NAME_REFLECTOMETER)) {
						if (!panel
							.isParameterPanelExists(
								ReflectometryTestPanel.PANEL_NAME)) {
							dispatcher.notify(
								new OperationEvent(
									new ReflectometryTestPanel(aContext, test),
									0,
									TestParametersPanel
										.COMMAND_ADD_PARAM_PANEL));
						}
					}
				}

				panel.setTest(test);
			} else {}
		} else if (
			commandName.equals(TestParametersPanel.COMMAND_CHANGE_PORT_TYPE)) {
			String portTypeId = (String) obj;
			if (portTypeId
				.equals(ElementsTreePanel.ACCESSPORT_NAME_REFLECTOMETER)) {
				if (!panel
					.isParameterPanelExists(
						ReflectometryTestPanel.PANEL_NAME)) {
					dispatcher.notify(
						new OperationEvent(
							new ReflectometryTestPanel(aContext),
							0,
							TestParametersPanel.COMMAND_ADD_PARAM_PANEL));
				}
			}

		}		
	}
}
