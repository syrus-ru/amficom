
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;

public class TestParametersFrame extends JInternalFrame implements OperationListener {

	private ApplicationContext	aContext;
	private SchedulerModel		schedulerModel;
	private Dispatcher			dispatcher;
	private TestParametersPanel	panel;
	private Command				command;

	public TestParametersFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
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
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_PORT_TYPE);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CHANGE_PORT_TYPE);
		this.panel.unregisterDispatcher();
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		if (commandName.equals(SchedulerModel.COMMAND_CHANGE_PORT_TYPE)) {
			MeasurementPort port = (MeasurementPort) obj;
			if (((MeasurementPortType) port.getType()).getCodename().equals(
				ElementsTreeFrame.ACCESSPORT_NAME_REFLECTOMETER)) {
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