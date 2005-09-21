
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
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Test;

/**
 * @author ???
 * @author Vladimir Dolzhenko
 */

final class TestRequestPanel extends JPanel implements PropertyChangeListener {

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
		JLabel titleLabel = new JLabel(LangModelSchedule.getString("Text.Test.Field.Title") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(titleLabel);
		panel.add(this.nameTextField);
		this.nameTextField.setEditable(true);
		this.nameTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final JTextField textField = (JTextField) e.getSource();
				final Test selectedTest;
				try {
					selectedTest = TestRequestPanel.this.schedulerModel.getSelectedTest();
				} catch (final ApplicationException e1) {
					AbstractMainFrame.showErrorMessage(LangModelGeneral.getString("Error.CannotAcquireObject"));
					return; 
				}
				if (selectedTest != null && selectedTest.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
					selectedTest.setDescription(textField.getText());
					TestRequestPanel.this.dispatcher
							.firePropertyChange(new PropertyChangeEvent(TestRequestPanel.this,
																		SchedulerModel.COMMAND_REFRESH_TEST, null, null));
				}
			}
		});
		JLabel ownerLabel = new JLabel(LangModelSchedule.getString("Text.Test.Field.Owner") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(ownerLabel);
		panel.add(this.ownerTextField);

		JLabel typeLabel = new JLabel(LangModelSchedule.getString("Text.Test.Field.MeasurementType") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(typeLabel);
		panel.add(this.typeTextField);

		JLabel objectLabel = new JLabel(LangModelSchedule.getString("Text.Test.Field.Port") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(objectLabel);
		panel.add(this.portTextField);
		this.add(panel);

		this.add(Box.createVerticalGlue());

	}

	public void propertyChange(final PropertyChangeEvent evt) {
		final String propertyName = evt.getPropertyName();
		if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
			try {
				this.setTest(this.schedulerModel.getSelectedTest());
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(LangModelGeneral.getString("Error.CannotAcquireObject"));
				return;
			}
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
