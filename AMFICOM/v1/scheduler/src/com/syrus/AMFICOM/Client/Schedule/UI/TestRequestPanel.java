
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client_.general.ui_.ObjList;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestController;

/**
 * @author ???
 * @author Vladimir Dolzhenko
 */

public class TestRequestPanel extends JPanel implements OperationListener {

	/**
	 * TODO remove when testRequest'll enable again
	 */
	// private TestRequest testRequest;
	private JTextField	nameTextField	= new JTextField();

	private JTextField	ownerTextField	= new JTextField();

	private JTextField	typeTextField	= new JTextField();

	ObjList				testList		= new ObjList(TestController.getInstance(), null,
														TestController.KEY_TEST_OBJECT);

	Hashtable			tests;

	Dispatcher			dispatcher;

	SchedulerModel		schedulerModel;

	boolean				skip			= false;

	// private String kisId;
	// private String meId;

	// private boolean toCreate = true;

	public TestRequestPanel(ApplicationContext aContext) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		initModule(aContext.getDispatcher());
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
		// add(nameTextField);

		JLabel ownerLabel = new JLabel(LangModelSchedule.getString("Owner") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(ownerLabel, gbc);
		// add(ownerLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.ownerTextField, gbc);
		// add(ownerTextField);

		JLabel typeLabel = new JLabel(LangModelSchedule.getString("Type") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.weightx = 0.0;
		add(typeLabel, gbc);
		// add(typeLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.typeTextField, gbc);
		// add(typeTextField);

		JLabel objectLabel = new JLabel(LangModelSchedule.getString("TestObject") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(objectLabel, gbc);
		// add(objectLabel);
		gbc.weightx = 4.0;
		gbc.weighty = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		JScrollPane listPanel = new JScrollPane(this.testList);
		add(listPanel, gbc);
		// add(listPanel);

		this.testList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				// System.out.println("ListSelectionListener");
				if ((e.getValueIsAdjusting()) || TestRequestPanel.this.skip)
					return;
				MonitoredElement me = ((Test) TestRequestPanel.this.testList.getSelectedValue()).getMonitoredElement();
				if (me == null) {
					// TestRequestPanel.this.dispatcher.notify(new
					// TestUpdateEvent(this, null,
					// TestUpdateEvent.TEST_DESELECTED_EVENT));
					// System.out.println("me == null");
				} else {
					TestRequestPanel.this.skip = true;
					if (TestRequestPanel.this.tests != null) {
						Test test = (Test) TestRequestPanel.this.tests.get(me.getId());
						if (test != null)
							try {
								TestRequestPanel.this.schedulerModel.setSelectedTest(test);
							} catch (ApplicationException e1) {
								SchedulerModel.showErrorMessage(TestRequestPanel.this, e1);
							}
					}
					TestRequestPanel.this.skip = false;
				}
			}
		});
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CLEAN);
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		// Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
			cleanAllFields();
		} 
	}

	private void cleanAllFields() {
		this.nameTextField.setText("");
		this.typeTextField.setText("");
		this.ownerTextField.setText("");
		if (this.tests != null)
			this.tests.clear();
		/**
		 * TODO remove when will enable again
		 */
		// this.testRequest = null;
		this.testList.removeAll();
	}

	/**
	 * TODO remove when will enable again
	 */
	// public TestRequest getParameters() {
	// //System.out.println(getClass().getName() + "
	// // getParameters()"); //$NON-NLS-1$
	// DataSourceInterface dsi = this.aContext.getDataSourceInterface();
	// // if test request have saved tests , create new testRequest for
	// // new
	// // test
	// // if request contains only unsaved tests , add new test to it
	// boolean needNewReq = false;
	// if (this.testRequest == null)
	// needNewReq = true;
	// else {
	// // System.out.println("current TestRequest:" +
	// // this.testRequest.getId()); //$NON-NLS-1$
	// java.util.List list = this.testRequest.getTestIds();
	// for (Iterator it = list.iterator(); it.hasNext();) {
	// String testId = (String) it.next();
	// Test test = (Test) Pool.get(Test.TYPE, testId);
	// // System.out.println("testId:" + testId);
	// // //$NON-NLS-1$
	// if ((!test.isChanged()) && (testId.length() > 0)) {
	// // System.out.println("saved test
	// // exists,"); //$NON-NLS-1$
	// needNewReq = true;
	// break;
	// }
	// }
	// // if ((!needNewReq) && (toCreate)) {
	// // for (int i = 0; i < testRequest.unsavedTest.size();
	// // i++) {
	// // Test t = (Test) testRequest.unsavedTest.get(i);
	// // if ((t.getKisId().equals(kisId))
	// // && (t.getMonitoredElementId().equals(meId))) {
	// // System.out
	// // .println("test with the same KIS & ME exists,"
	// // //$NON-NLS-1$
	// // + t.getKisId() + "," + kisId + "/" //$NON-NLS-1$
	// // //$NON-NLS-2$
	// // + t.getMonitoredElementId() + "," + meId);
	// // //$NON-NLS-1$
	// // needNewReq = true;
	// // break;
	// // }
	// // }
	// // }
	// }
	//
	// if (needNewReq) {
	// // System.out.println("new TestRequest required");
	// this.testRequest = new TestRequest(dsi.GetUId(TestRequest.TYPE));
	// // System.out.println("set treqId:" +
	// // this.testRequest.getId());
	// this.testRequest.setName(LangModelSchedule.getString("Test created at") +
	// " "
	// + UIStorage.SDF.format(new Date(System.currentTimeMillis())));
	// //System.out.println("set name:" +
	// // this.testRequest.getName());
	// this.testRequest.setUserId(this.aContext.getSessionInterface().getUserId());
	// //testRequest = newTestRequest;
	// this.testRequest.setChanged(true);
	// Pool.put(TestRequest.TYPE, this.testRequest.getId(), this.testRequest);
	// this.tests = null;
	// }
	//
	// // TestRequest treq = new TestRequest("");
	// {
	// String s = this.nameTextField.getText();
	// if (s.length() > 0)
	// this.testRequest.setName(s);
	// }
	// {
	// String s = this.ownerTextField.getText();
	// if (s.length() > 0)
	// this.testRequest.setUserId(s);
	// }
	// this.testRequest.getTestIds().clear();
	// if (this.tests != null) {
	// for (Iterator it = this.tests.values().iterator(); it.hasNext();) {
	// Test test = (Test) it.next();
	// this.testRequest.addTest(test);
	// }
	// }
	// setTestRequest(this.testRequest);
	// return this.testRequest;
	// }
	public void setTest(Test test) {
		// System.out.println(getClass().getName() + " setTest");
		if (test != null) {
			/**
			 * TODO remove when will enable again
			 */
			// String treqId = test.getRequestId();
			// TestRequest treq = (TestRequest) Pool.get(TestRequest.TYPE,
			// treqId);
			// setTestRequest(treq);
			MonitoredElement me = test.getMonitoredElement();
			if (me != null) {
				this.testList.setSelectedValue(me, true);
				// System.out.println("setSelected:" +
				// me.getId());
			} // else System.out.println("tried setSelected
			// me==null");
			// ListModel lm = testList.getModel();
			// for (int i = 0; i < lm.getSize(); i++) {
			// MonitoredElement m = (MonitoredElement)
			// lm.getElementAt(i);
			// System.out.println(i + "\t" + m.id);
			// if (m.id.equals(me.id)) {
			// System.out.println("select:" + i);
			// testList.setSelectedIndex(i);
			// break;
			// }
			// }
		} else {
			Environment.log(Environment.LOG_LEVEL_WARNING, "test is null");
		}
	}

	/**
	 * TODO remove when will enable again
	 */
	// public void setTestRequest(TestRequest testRequest) {
	// if (this.skip)
	// return;
	// //System.out.println("setTestRequest");
	// this.skip = true;
	// this.testRequest = testRequest;
	// this.testList.removeAll();
	// this.nameTextField.setText(testRequest.getName());
	// this.ownerTextField.setText(testRequest.getUserId());
	// this.nameTextField.setCaretPosition(0);
	// this.ownerTextField.setCaretPosition(0);
	//
	// this.tests = new Hashtable();
	// Hashtable ht = new Hashtable();
	// String type = ""; //$NON-NLS-1$
	// java.util.List list = testRequest.getTestIds();
	// for (Iterator it = list.iterator(); it.hasNext();) {
	// Identifier testId = (Identifier) it.next();
	// Test tst = (Test) MeasurementStorableObjectPool.getStorableObject(testId,
	// true);
	// type = tst.getMeasurementType().getDescription();
	// MonitoredElement me = tst.getMonitoredElement();
	// ((ObjListModel) this.testList.getModel()).addElement(me);
	// this.tests.put(me.getId(), tst);
	// }
	// this.typeTextField.setText(type);
	// this.typeTextField.setCaretPosition(0);
	// this.skip = false;
	// }
}