
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	
	private DefaultListModel defaultListModel = new DefaultListModel();
	private JList testList = new JList(this.defaultListModel);

	private SchedulerModel		schedulerModel;

	boolean				skip			= false;

	public TestRequestPanel(ApplicationContext aContext) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.schedulerModel.addTestEditor(this);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		// this.setLayout(new GridLayout(0,2));
		JLabel titleLabel = new JLabel(LangModelSchedule.getString("Title") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.weightx = 0.0;
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(titleLabel, gbc);
		// add(titleLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.nameTextField, gbc);
		this.nameTextField.setEditable(false);
		// add(nameTextField);

		JLabel ownerLabel = new JLabel(LangModelSchedule.getString("Owner") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(ownerLabel, gbc);
		// add(ownerLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.ownerTextField, gbc);
		this.ownerTextField.setEditable(false);
		// add(ownerTextField);

		JLabel typeLabel = new JLabel(LangModelSchedule.getString("Type") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.weightx = 0.0;
		this.add(typeLabel, gbc);
		// add(typeLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(this.typeTextField, gbc);
		this.typeTextField.setEditable(false);
		// add(typeTextField);

		JLabel objectLabel = new JLabel(LangModelSchedule.getString("TestObject") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		this.add(objectLabel, gbc);
		// add(objectLabel);
		gbc.weightx = 4.0;
		gbc.weighty = 2.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
//		JScrollPane listPanel = new JScrollPane(this.testList);
		JScrollPane listPanel = new JScrollPane(this.testList);
		this.add(listPanel, gbc);

	}

	private void cleanAllFields() {
		this.nameTextField.setText("");
		this.typeTextField.setText("");
		this.ownerTextField.setText("");
		this.testList.removeAll();
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

				this.defaultListModel.removeAllElements();
				String meName = me.getName();
				this.defaultListModel.addElement(meName);
				this.testList.setSelectedValue(meName, true);
			} catch (ApplicationException ae) {
				SchedulerModel.showErrorMessage(this, ae);
			}			
		} else {
			this.cleanAllFields();
		}
	}
}