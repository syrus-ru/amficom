package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TestUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.Test;

public class TestParametersFrame extends JInternalFrame implements OperationListener {

	private ApplicationContext	aContext;
	private Dispatcher			dispatcher;
	private TestParametersPanel	panel;
	private Command				command;

	public TestParametersFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		setTitle(LangModelSchedule.getString("Measurement_options")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		this.panel = new TestParametersPanel(aContext);
		this.getContentPane().add(this.panel, BorderLayout.CENTER);
		initModule(aContext.getDispatcher());
		this.command = new WindowCommand(this);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_PORT_TYPE);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, TestUpdateEvent.TYPE);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CHANGE_PORT_TYPE);
		this.panel.unregisterDispatcher();
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		//		System.out.println(getClass().getName() + "\tcommandName:"
		//				+ commandName);
		//		System.out.println("obj:" + obj.getClass().getName() + "\t" + obj);
		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if (tue.testSelected) {				
				try{
					MeasurementPort port = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(test.getMonitoredElement().getMeasurementPortId(), true);
					if (((MeasurementPortType)port.getType()).getCodename().equals(ElementsTreePanel.ACCESSPORT_NAME_REFLECTOMETER)) {
						if (!this.panel.isParameterPanelExists(ReflectometryTestPanel.PANEL_NAME)) {
							this.dispatcher.notify(new OperationEvent(new ReflectometryTestPanel(this.aContext, port,
																									test), 0,
																		SchedulerModel.COMMAND_ADD_PARAM_PANEL));
						}
					}
				}catch(ApplicationException e){
					SchedulerModel.showErrorMessage(this.panel, e);
				}

				this.panel.setTest(test);
			} else {
				//				nothing
			}
		} else if (commandName.equals(SchedulerModel.COMMAND_CHANGE_PORT_TYPE)) {
			MeasurementPort port = (MeasurementPort) obj;
			if(((MeasurementPortType)port.getType()).getCodename().equals(ElementsTreePanel.ACCESSPORT_NAME_REFLECTOMETER)) {
				if (!this.panel.isParameterPanelExists(ReflectometryTestPanel.PANEL_NAME)) {
					this.dispatcher.notify(new OperationEvent(new ReflectometryTestPanel(this.aContext, port), 0,
																SchedulerModel.COMMAND_ADD_PARAM_PANEL));
				}
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