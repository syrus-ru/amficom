
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.TestEditor;
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

public class TestRequestPanel extends JPanel implements TestEditor {

	private JTextField	nameTextField	= new JTextField();

	private JTextField	ownerTextField	= new JTextField();

	private JTextField	typeTextField	= new JTextField();
	
	private JTextField	portTextField	= new JTextField();
	
	private SchedulerModel		schedulerModel;

	public TestRequestPanel(ApplicationContext aContext) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.schedulerModel.addTestEditor(this);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel panel = new JPanel(new GridLayout(0, 2));
		JLabel titleLabel = new JLabel(LangModelSchedule.getString("Title") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(titleLabel);
		panel.add(this.nameTextField);
		this.nameTextField.setEditable(false);
		// add(nameTextField);

		JLabel ownerLabel = new JLabel(LangModelSchedule.getString("Owner") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(ownerLabel);
		// add(ownerLabel);
		panel.add(this.ownerTextField);
		this.ownerTextField.setEditable(false);
		// add(ownerTextField);

		JLabel typeLabel = new JLabel(LangModelSchedule.getString("Type") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(typeLabel);
		// add(typeLabel);
		panel.add(this.typeTextField);
		this.typeTextField.setEditable(false);
		// add(typeTextField);

		JLabel objectLabel = new JLabel(LangModelSchedule.getString("Port") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(objectLabel);
		panel.add(this.portTextField);
		this.add(panel);
		this.add(Box.createVerticalGlue());

	}

	private void cleanAllFields() {
		this.nameTextField.setText("");
		this.typeTextField.setText("");
		this.ownerTextField.setText("");
		this.portTextField.setText("");
	}

	
	public void updateTest() {
		this.setTest(this.schedulerModel.getSelectedTest());
	}
	
	public void setTest(Test test) {
		if (test != null) {			
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
			this.cleanAllFields();
		}
	}
}
