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
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.ScheduleMainFrame;

/**
 * @author Vladimir Dolzhenko
 */
public class SaveParametersFrame extends JInternalFrame implements
		OperationListener {

	private ApplicationContext	aContext;
	private Dispatcher			dispatcher;
	private JPanel				panel;

	private JRadioButton		allResultsButton;
	private JRadioButton		recognizedEventsButton;
	private JRadioButton		measurementIdButton;

	/** 
	 * @todo only for testing mode
	 */
	public static void main(String[] args) {

		SaveParametersFrame frame = new SaveParametersFrame(null);
		JFrame mainFrame = new JFrame("SaveParametersFrame");
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
		this.aContext = aContext;
		init();
		if (aContext != null) initModule(aContext.getDispatcher());
	}

	private void init() {
		setTitle("Параметры сохранения");
		setFrameIcon(UIUtil.GENERAL_ICON);
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
			allResultsButton = new JRadioButton(LangModelSchedule
					.getString("labelAllTestResults"));
			allResultsButton
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(ActionEvent e) {
							//jButton1_actionPerformed(e);
						}
					});
			recognizedEventsButton = new JRadioButton(LangModelSchedule
					.getString("labelKnownEvents"));
			recognizedEventsButton
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(ActionEvent e) {
							//jButton2_actionPerformed(e);
						}
					});
			measurementIdButton = new JRadioButton(LangModelSchedule
					.getString("labelIdIzmer"));
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
		this.dispatcher.register(this, TestRequestFrame.COMMAND_DATA_REQUEST);
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
		if (ScheduleMainFrame.DEBUG)
				System.out.println(getClass().getName() + " commandName: "
						+ commandName);
		if (commandName.equalsIgnoreCase(TestRequestFrame.COMMAND_DATA_REQUEST)) {
			/**
			 * @todo must send data edit in this form 
			 */
			TestReturnType returnType = this.getParameter();
			dispatcher.notify(new OperationEvent(returnType,
					TestRequestFrame.DATA_ID_RETURN_TYPE,
					TestRequestFrame.COMMAND_SEND_DATA));
		} else if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			TestReturnType returnType = tue.test.return_type;
			if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_WHOLE)) {
				allResultsButton.doClick();
			} else if (returnType
					.equals(TestReturnType.TEST_RETURN_TYPE_EVENTS)) {
				recognizedEventsButton.doClick();
			} else if (returnType
					.equals(TestReturnType.TEST_RETURN_TYPE_REFERENCE)) {
				measurementIdButton.doClick();
			}
			//this.test = tue.test;
		}
	}
}