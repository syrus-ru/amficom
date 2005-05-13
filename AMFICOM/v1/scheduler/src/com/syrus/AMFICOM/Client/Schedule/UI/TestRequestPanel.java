
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Test;

/**
 * @author ???
 * @author Vladimir Dolzhenko
 */

public class TestRequestPanel extends JPanel 
//implements TestEditor 
{

	private static final long	serialVersionUID	= 3834032471820939824L;

	JTextField	nameTextField	= new JTextField();

	private JLabel	ownerTextField	= new JLabel();

	private JLabel	typeTextField	= new JLabel();
	
	private JLabel	portTextField	= new JLabel();
	
	SchedulerModel		schedulerModel;
	
//	JButton						createButton;
//
//	JButton						applyButton;

	public TestRequestPanel(ApplicationContext aContext) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		
		final Dispatcher dispatcher = aContext.getDispatcher();
		OperationListener operationListener = new OperationListener() {
			public void operationPerformed(OperationEvent e) {
				String actionCommand = e.getActionCommand();
				if (actionCommand.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
					updateTest();
				} else if (actionCommand.equals(SchedulerModel.COMMAND_GET_NAME)) {
					dispatcher.notify(new OperationEvent(TestRequestPanel.this.nameTextField.getText(), 0, SchedulerModel.COMMAND_SET_NAME));
				}
			}
		};
		
		dispatcher.register(operationListener, SchedulerModel.COMMAND_REFRESH_TEST);
		dispatcher.register(operationListener, SchedulerModel.COMMAND_GET_NAME);
		
//		this.schedulerModel.addTestEditor(this);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel panel = new JPanel(new GridLayout(0, 2));
		JLabel titleLabel = new JLabel(LangModelSchedule.getString("Title") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(titleLabel);
		panel.add(this.nameTextField);
		this.nameTextField.setEditable(true);
		this.nameTextField.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JTextField textField = (JTextField) e.getSource();
				Test selectedTest = TestRequestPanel.this.schedulerModel.getSelectedTest();
				if (selectedTest != null && selectedTest.isChanged()) {
					selectedTest.setDescription(textField.getText());
					dispatcher.notify(new OperationEvent(this, 0, SchedulerModel.COMMAND_REFRESH_TEST));
				}
			}
		}
			);
		// add(nameTextField);

		JLabel ownerLabel = new JLabel(LangModelSchedule.getString("Owner") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(ownerLabel);
		// add(ownerLabel);
		panel.add(this.ownerTextField);
//		this.ownerTextField.setEditable(false);
		// add(ownerTextField);

		JLabel typeLabel = new JLabel(LangModelSchedule.getString("Type") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(typeLabel);
		// add(typeLabel);
		panel.add(this.typeTextField);
//		this.typeTextField.setEditable(false);
		// add(typeTextField);

		JLabel objectLabel = new JLabel(LangModelSchedule.getString("Port") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(objectLabel);
		panel.add(this.portTextField);
//		this.portTextField.setEditable(false);
		this.add(panel);
		
//		{
//			this.applyButton = new JButton(LangModelSchedule.getString("Apply"));
//			this.applyButton.setEnabled(false);
//			this.createButton = new JButton(LangModelSchedule.getString("Create"));
//
//			this.applyButton.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//					TestRequestPanel.this.createButton.setEnabled(false);
//					TestRequestPanel.this.applyButton.setEnabled(false);
//					TestRequestPanel.this.schedulerModel.applyTest();
//					TestRequestPanel.this.createButton.setEnabled(true);
//					TestRequestPanel.this.applyButton.setEnabled(true);
//
//				}
//			});
//
//			this.createButton.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//					TestRequestPanel.this.createButton.setEnabled(false);
//					TestRequestPanel.this.applyButton.setEnabled(false);
//					TestRequestPanel.this.schedulerModel.createTest();
//					TestRequestPanel.this.createButton.setEnabled(true);
//					TestRequestPanel.this.applyButton.setEnabled(true);
//				}
//			});
//
//			Box box = new Box(BoxLayout.X_AXIS);
//			this.createButton.setDefaultCapable(false);
//			this.createButton.setEnabled(true);
//			box.add(this.createButton);
//			box.add(Box.createGlue());
//			this.applyButton.setDefaultCapable(false);
//			this.applyButton.setEnabled(false);
//			box.add(this.applyButton);
//			this.add(box);
//		}

		this.add(Box.createVerticalGlue());

	}

	private void cleanAllFields() {
		this.nameTextField.setText("");
		this.typeTextField.setText("");
		this.ownerTextField.setText("");
		this.portTextField.setText("");
	}

	
	public void updateTest() {
		Test test = this.schedulerModel.getSelectedTest();
		this.setTest(test);
	}
	
	public void setTest(Test test) {
		if (test != null) {
//			this.applyButton.setEnabled(test.isChanged());
			try {
				this.nameTextField.setText(test.getDescription());
				
				MeasurementType measurementType = (MeasurementType) MeasurementStorableObjectPool.getStorableObject(
					test.getMeasurementTypeId(), true);
				this.typeTextField.setText(measurementType.getDescription());
				
				User user = (User)AdministrationStorableObjectPool.getStorableObject(test.getCreatorId(), true);
				this.ownerTextField.setText(user.getName());
				
				MonitoredElement me = test.getMonitoredElement();
				this.portTextField.setText(me.getName());
			} catch (ApplicationException ae) {
				SchedulerModel.showErrorMessage(this, ae);
			}			
		} else {
//			this.applyButton.setEnabled(false);
			this.cleanAllFields();
		}
	}
}
