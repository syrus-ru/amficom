
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Test;

/**
 * @author ???
 * @author Vladimir Dolzhenko
 */

public class TestRequestPanel extends JPanel implements PropertyChangeListener {

	private static final long	serialVersionUID	= 3834032471820939824L;

	JTextField					nameTextField		= new JTextField();

	private JLabel				ownerTextField		= new JLabel();

	private JLabel				typeTextField		= new JLabel();

	private JLabel				portTextField		= new JLabel();

	SchedulerModel				schedulerModel;

	Dispatcher					dispatcher;

	public TestRequestPanel(ApplicationContext aContext) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();

		this.dispatcher = aContext.getDispatcher();
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEST, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_NAME, this);

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
					TestRequestPanel.this.dispatcher
							.firePropertyChange(new PropertyChangeEvent(TestRequestPanel.this,
																		SchedulerModel.COMMAND_REFRESH_TEST, null, null));
				}
			}
		});
		JLabel ownerLabel = new JLabel(LangModelSchedule.getString("Owner") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(ownerLabel);
		panel.add(this.ownerTextField);

		JLabel typeLabel = new JLabel(LangModelSchedule.getString("Type") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(typeLabel);
		panel.add(this.typeTextField);

		JLabel objectLabel = new JLabel(LangModelSchedule.getString("Port") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(objectLabel);
		panel.add(this.portTextField);
		this.add(panel);

		this.add(Box.createVerticalGlue());

	}

	public void propertyChange(PropertyChangeEvent evt) {

		String propertyName = evt.getPropertyName();
		if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
			Test test = this.schedulerModel.getSelectedTest();
			this.setTest(test);
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_NAME)) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_NAME, null,
																		TestRequestPanel.this.nameTextField.getText()));
		}

	}

	private void cleanAllFields() {
		this.nameTextField.setText("");
		this.typeTextField.setText("");
		this.ownerTextField.setText("");
		this.portTextField.setText("");
	}

	public void setTest(Test test) {
		if (test != null) {
			try {
				this.nameTextField.setText(test.getDescription());

				MeasurementType measurementType = (MeasurementType) StorableObjectPool.getStorableObject(
					test.getMeasurementTypeId(), true);
				this.typeTextField.setText(measurementType.getDescription());

				User user = (User) StorableObjectPool.getStorableObject(test.getCreatorId(), true);
				this.ownerTextField.setText(user.getName());

				MonitoredElement me = test.getMonitoredElement();
				this.portTextField.setText(me.getName());
			} catch (ApplicationException ae) {
				SchedulerModel.showErrorMessage(this, ae);
			}
		} else {
			this.cleanAllFields();
		}
	}
}
