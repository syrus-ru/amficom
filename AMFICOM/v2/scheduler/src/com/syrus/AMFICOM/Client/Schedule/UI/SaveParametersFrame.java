/*
 * SaveParametersFrame.java Created on 17.05.2004 18:23:26
 *  
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.CORBA.General.TestReturnType;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.I18N;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

/**
 * @author Vladimir Dolzhenko
 */
public class SaveParametersFrame extends JInternalFrame implements
		OperationListener {

	//	private ApplicationContext aContext;
	private Dispatcher		dispatcher;
	private JPanel			panel;

	private JRadioButton	allResultsButton;
	private JRadioButton	recognizedEventsButton;
	private JRadioButton	measurementIdButton;

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
		if (aContext != null) initModule(aContext.getDispatcher());
	}

	private void init() {
		setTitle(I18N.getString("Saving_options")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		panel = getPanel();
		setContentPane(panel);

	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			allResultsButton = new JRadioButton(I18N
					.getString("AllTestResults")); //$NON-NLS-1$
			allResultsButton
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(ActionEvent e) {
							//jButton1_actionPerformed(e);
						}
					});
			recognizedEventsButton = new JRadioButton(I18N
					.getString("Only_recognized_events")); //$NON-NLS-1$
			recognizedEventsButton
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(ActionEvent e) {
							//jButton2_actionPerformed(e);
						}
					});
			measurementIdButton = new JRadioButton(I18N
					.getString("Only_Measurement_Id")); //$NON-NLS-1$
			measurementIdButton
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(ActionEvent e) {
							//	jButton3_actionPerformed(e);
						}
					});
			ButtonGroup group = new ButtonGroup();
			group.add(allResultsButton);
			group.add(recognizedEventsButton);
			group.add(measurementIdButton);
			allResultsButton.setSelected(true);
			panel.add(allResultsButton, gbc);
			panel.add(recognizedEventsButton, gbc);
			panel.add(measurementIdButton, gbc);
			gbc.weighty = 1.0;
			panel.add(new JLabel(), gbc);
		}
		return panel;

	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.typ);
		this.dispatcher.register(this, SchedulerModel.COMMAND_DATA_REQUEST);
	}

	private TestReturnType getParameter() {
		TestReturnType ret = TestReturnType.TEST_RETURN_TYPE_WHOLE;
		if (recognizedEventsButton.isSelected())
			ret = TestReturnType.TEST_RETURN_TYPE_EVENTS;
		else if (measurementIdButton.isSelected())
				ret = TestReturnType.TEST_RETURN_TYPE_REFERENCE;
		return ret;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		if (SchedulerModel.DEBUG >= 5)
				System.out.println(getClass().getName() + " commandName: " //$NON-NLS-1$
						+ commandName);
		if (commandName.equalsIgnoreCase(SchedulerModel.COMMAND_DATA_REQUEST)) {
			/**
			 * @todo must send data edit in this form 
			 */
			TestReturnType returnType = this.getParameter();
			dispatcher.notify(new OperationEvent(returnType,
					SchedulerModel.DATA_ID_RETURN_TYPE,
					SchedulerModel.COMMAND_SEND_DATA));
		} else if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			if (tue.TEST_SELECTED) {
				TestReturnType returnType = tue.test.getReturnType();
				if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_WHOLE)) {
					allResultsButton.doClick();
				} else if (returnType
						.equals(TestReturnType.TEST_RETURN_TYPE_EVENTS)) {
					recognizedEventsButton.doClick();
				} else if (returnType
						.equals(TestReturnType.TEST_RETURN_TYPE_REFERENCE)) {
					measurementIdButton.doClick();
				}
			}
			//this.test = tue.test;
		}
	}
}