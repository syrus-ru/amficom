/*
 * SaveParametersFrame.java Created on 17.05.2004 18:23:26
 *  
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;

/**
 * @author Vladimir Dolzhenko
 */
public class SaveParametersFrame extends JInternalFrame implements OperationListener {

	//	private ApplicationContext aContext;
	private Dispatcher		dispatcher;
	private JPanel			panel;

	private JRadioButton	allResultsButton;
	private JRadioButton	recognizedEventsButton;
	private JRadioButton	measurementIdButton;
	private Test			test;
	private Command			command;

	/**
	 * @todo only for testing mode
	 */
	public static void main(String[] args) {

		SaveParametersFrame frame = new SaveParametersFrame(null);
		JFrame mainFrame = new JFrame("SaveParametersFrame"); //$NON-NLS-1$
		mainFrame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		mainFrame.getContentPane().add(frame.getPanel());
		mainFrame.pack();
		mainFrame.setSize(new Dimension(250, 465));
		mainFrame.setVisible(true);
	}

	public SaveParametersFrame(ApplicationContext aContext) {
		//		this.aContext = aContext;
		init();
		if (aContext != null)
			initModule(aContext.getDispatcher());
		this.command = new WindowCommand(this);
	}

	private void init() {
		setTitle(LangModelSchedule.getString("Saving_options")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.panel = getPanel();
		setContentPane(this.panel);

	}

	private JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			this.allResultsButton = new JRadioButton(LangModelSchedule.getString("AllTestResults")); //$NON-NLS-1$
			this.allResultsButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//jButton1_actionPerformed(e);
				}
			});
			this.recognizedEventsButton = new JRadioButton(LangModelSchedule.getString("Only_recognized_events")); //$NON-NLS-1$
			this.recognizedEventsButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//jButton2_actionPerformed(e);
				}
			});
			this.measurementIdButton = new JRadioButton(LangModelSchedule.getString("Only_Measurement_Id")); //$NON-NLS-1$
			this.measurementIdButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//	jButton3_actionPerformed(e);
				}
			});
			ButtonGroup group = new ButtonGroup();
			group.add(this.allResultsButton);
			group.add(this.recognizedEventsButton);
			group.add(this.measurementIdButton);
			this.allResultsButton.setSelected(true);
			this.panel.add(this.allResultsButton, gbc);
			this.panel.add(this.recognizedEventsButton, gbc);
			this.panel.add(this.measurementIdButton, gbc);
			gbc.weighty = 1.0;
			this.panel.add(new JLabel(), gbc);
		}
		return this.panel;

	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_DATA_REQUEST);
	}
	
	public void unregisterDispatcher() {
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_DATA_REQUEST);
	}

	private TestReturnType getParameter() {
		TestReturnType ret = TestReturnType.TEST_RETURN_TYPE_WHOLE;
		if (this.recognizedEventsButton.isSelected())
			ret = TestReturnType.TEST_RETURN_TYPE_EVENTS;
		else if (this.measurementIdButton.isSelected())
			ret = TestReturnType.TEST_RETURN_TYPE_REFERENCE;
		return ret;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equalsIgnoreCase(SchedulerModel.COMMAND_DATA_REQUEST)) {
			TestReturnType returnType = this.getParameter();
			this.dispatcher.notify(new OperationEvent(returnType, SchedulerModel.DATA_ID_RETURN_TYPE,
														SchedulerModel.COMMAND_SEND_DATA));
		} else if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if ((this.test == null) || (!this.test.getId().equals(test.getId()))) {
				this.test = test;
				if (tue.testSelected) {
					TestReturnType returnType = test.getReturnType();
					if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_WHOLE)) {
						this.allResultsButton.doClick();
					} else if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_EVENTS)) {
						this.recognizedEventsButton.doClick();
					} else if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_REFERENCE)) {
						this.measurementIdButton.doClick();
					}
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