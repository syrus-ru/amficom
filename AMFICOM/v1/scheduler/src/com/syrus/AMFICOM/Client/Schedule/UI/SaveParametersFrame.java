/*
 * SaveParametersFrame.java Created on 17.05.2004 18:23:26
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.ReturnTypeEditor;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;

/**
 * @author Vladimir Dolzhenko
 */
public class SaveParametersFrame extends JInternalFrame implements ReturnTypeEditor {

	private JPanel			panel;

	private JRadioButton	allResultsButton;
	private JRadioButton	recognizedEventsButton;
	private JRadioButton	measurementIdButton;
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
		// this.aContext = aContext;
		init();
		if (aContext != null) {
			SchedulerModel schedulerModel = (SchedulerModel) aContext.getApplicationModel();
			schedulerModel.setReturnTypeEditor(this);
		}
		this.command = new WindowCommand(this);
	}

	private void init() {
		setTitle(LangModelSchedule.getString("Saving_options")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
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
					// jButton1_actionPerformed(e);
				}
			});
			this.recognizedEventsButton = new JRadioButton(LangModelSchedule.getString("Only_recognized_events")); //$NON-NLS-1$
			this.recognizedEventsButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// jButton2_actionPerformed(e);
				}
			});
			this.measurementIdButton = new JRadioButton(LangModelSchedule.getString("Only_Measurement_Id")); //$NON-NLS-1$
			this.measurementIdButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// jButton3_actionPerformed(e);
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

	public TestReturnType getReturnType() {
		TestReturnType returnType = TestReturnType.TEST_RETURN_TYPE_WHOLE;
		if (this.recognizedEventsButton.isSelected())
			returnType = TestReturnType.TEST_RETURN_TYPE_EVENTS;
		else if (this.measurementIdButton.isSelected())
			returnType = TestReturnType.TEST_RETURN_TYPE_REFERENCE;
		return returnType;
	}

	public void setReturnType(TestReturnType returnType) {
		if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_WHOLE)) {
			this.allResultsButton.doClick();
		} else if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_EVENTS)) {
			this.recognizedEventsButton.doClick();
		} else if (returnType.equals(TestReturnType.TEST_RETURN_TYPE_REFERENCE)) {
			this.measurementIdButton.doClick();
		}
	}

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}
}