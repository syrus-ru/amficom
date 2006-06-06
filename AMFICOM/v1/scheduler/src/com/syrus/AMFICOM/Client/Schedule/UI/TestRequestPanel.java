
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

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Test;

/**
 * @author ???
 * @author Vladimir Dolzhenko
 */
final class TestRequestPanel extends JPanel implements PropertyChangeListener {

	private static final long	serialVersionUID	= 3834032471820939824L;

	JTextField					nameTextField;

	private JTextField			ownerTextField;

	private JTextField			typeTextField;

	private JTextField			portTextField;

	SchedulerModel				schedulerModel;

	Dispatcher					dispatcher;

	public TestRequestPanel(final ApplicationContext aContext) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();

		this.dispatcher = aContext.getDispatcher();
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEST, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_NAME, this);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		final JPanel panel = new JPanel(new GridLayout(0, 2));
		final JLabel titleLabel = new JLabel(I18N.getString("Scheduler.Text.Test.Field.Title") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(titleLabel);

		this.nameTextField = new JTextField();
		panel.add(this.nameTextField);
		this.nameTextField.setEditable(true);
		this.nameTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final JTextField textField = (JTextField) e.getSource();
				final Test selectedTest;
				try {
					selectedTest = TestRequestPanel.this.schedulerModel.getSelectedTest();
				} catch (final ApplicationException e1) {
					AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
					return;
				}
				if (selectedTest != null && selectedTest.isNew()) {
					selectedTest.setDescription(textField.getText());
					TestRequestPanel.this.dispatcher.firePropertyChange(
							new PropertyChangeEvent(TestRequestPanel.this,
									SchedulerModel.COMMAND_REFRESH_TEST, null, null));
				}
			}
		});

		final JLabel ownerLabel = new JLabel(I18N.getString("Scheduler.Text.Test.Field.Owner") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(ownerLabel);
		this.ownerTextField = new JTextField();
		this.ownerTextField.setEditable(false);
		panel.add(this.ownerTextField);

		final JLabel objectLabel = new JLabel(I18N.getString("Scheduler.Text.Test.Field.TestingLine") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(objectLabel);
		this.portTextField = new JTextField();
		this.portTextField.setEditable(false);
		panel.add(this.portTextField);
		this.add(panel);

		final JLabel typeLabel = new JLabel(I18N.getString("Scheduler.Text.Test.Field.MeasurementType") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(typeLabel);
		this.typeTextField = new JTextField();
		this.typeTextField.setEditable(false);
		panel.add(this.typeTextField);

		this.add(Box.createVerticalGlue());
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		final String propertyName = evt.getPropertyName();
		if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
			try {
				this.setTest(this.schedulerModel.getSelectedTest());
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
				return;
			}
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_NAME)) {
			this.dispatcher.firePropertyChange(
					new PropertyChangeEvent(
							this, SchedulerModel.COMMAND_SET_NAME, null,
							TestRequestPanel.this.nameTextField.getText()));
		}

	}

	private void cleanAllFields() {
		this.nameTextField.setText("");
		this.typeTextField.setText("");
		this.ownerTextField.setText("");
		this.portTextField.setText("");
	}

	public void setTest(final Test test) {
		if (test != null) {
			try {
				this.nameTextField.setText(test.getDescription());

				MeasurementType measurementType = test.getMeasurementType();
				this.typeTextField.setText(measurementType.getDescription());

				SystemUser user = (SystemUser) StorableObjectPool.getStorableObject(test.getCreatorId(), true);
				this.ownerTextField.setText(user.getName());

				MonitoredElement me = test.getMonitoredElement();
				this.portTextField.setText(me.getName());
			} catch (ApplicationException ae) {
				AbstractMainFrame.showErrorMessage(this, ae);
			}
		} else {
			this.cleanAllFields();
		}
	}
}
