package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;

/**
 * @author ??? , Vladimir Dolzhenko
 */

public class TestRequestPanel extends JPanel implements OperationListener {

	private TestRequest			testRequest;

	private JTextField			nameTextField	= new JTextField();

	private JTextField			ownerTextField	= new JTextField();

	private JTextField			typeTextField	= new JTextField();

	ObjectResourceListBox		testList		= new ObjectResourceListBox();

	Hashtable					tests;

	private ApplicationContext	aContext;

	Dispatcher					dispatcher;

	boolean						skip			= false;

	//	private String kisId;
	//private String meId;

	//	private boolean toCreate = true;

	public TestRequestPanel(ApplicationContext aContext) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		//		this.setLayout(new GridLayout(0,2));
		JLabel titleLabel = new JLabel(LangModelSchedule.getString("Title") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.weightx = 0.0;
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(titleLabel, gbc);
		//		add(titleLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.nameTextField, gbc);
		//		add(nameTextField);

		JLabel ownerLabel = new JLabel(LangModelSchedule.getString("Owner") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(ownerLabel, gbc);
		//		add(ownerLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.ownerTextField, gbc);
		//		add(ownerTextField);

		JLabel typeLabel = new JLabel(LangModelSchedule.getString("Type") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.weightx = 0.0;
		add(typeLabel, gbc);
		//		add(typeLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(this.typeTextField, gbc);
		//		add(typeTextField);

		JLabel objectLabel = new JLabel(LangModelSchedule.getString("TestObject") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(objectLabel, gbc);
		//		add(objectLabel);
		gbc.weightx = 4.0;
		gbc.weighty = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		JScrollPane listPanel = new JScrollPane(this.testList);
		add(listPanel, gbc);
		//		add(listPanel);

		this.testList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				//System.out.println("ListSelectionListener");
				if ((e.getValueIsAdjusting()) || TestRequestPanel.this.skip)
					return;
				MonitoredElement me = (MonitoredElement) TestRequestPanel.this.testList.getSelectedObjectResource();
				if (me == null) {
					//					TestRequestPanel.this.dispatcher.notify(new
					// TestUpdateEvent(this, null,
					//																				TestUpdateEvent.TEST_DESELECTED_EVENT));
					//System.out.println("me == null");
				} else {
					TestRequestPanel.this.skip = true;
					if (TestRequestPanel.this.tests != null) {
						Test test = (Test) TestRequestPanel.this.tests.get(me.getId());
						if (test != null)
							TestRequestPanel.this.dispatcher
									.notify(new TestUpdateEvent(this, test, TestUpdateEvent.TEST_SELECTED_EVENT));
					}
					TestRequestPanel.this.skip = false;
				}
			}
		});
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
		this.dispatcher.register(this, SchedulerModel.COMMAND_REMOVE_TEST);

	}
	
	public void unregisterDispatcher(){
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.unregister(this, TestUpdateEvent.TYPE);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CLEAN);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_REMOVE_TEST);		
	}
	

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		//		Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equalsIgnoreCase(SchedulerModel.COMMAND_DATA_REQUEST)) {
			TestRequest testRequest = getParameters();

			this.dispatcher.notify(new OperationEvent(testRequest == null ? (Object) "" : (Object) testRequest, //$NON-NLS-1$
														0, SchedulerModel.COMMAND_SEND_DATA));
		} else if (commandName.equals(SchedulerModel.COMMAND_CHANGE_KIS)) {
			//kisId = (String) obj;
			//System.out.println("kisId:" + kisId);
		} else if (commandName.equals(SchedulerModel.COMMAND_CHANGE_ME_TYPE)) {
			//			String meId = (String) obj;
			//			System.out.println("meId:" + meId);
			//			MonitoredElement me = (MonitoredElement)
			// Pool.get(MonitoredElement.typ, meId);
			//			this.testList.setSelected(me);
			//			if (this.testList.getSelectedIndex() < 0) {
			//				this.testList.add(me);
			//				this.testList.setSelected(me);
			//			}

		} else if (commandName.equals(SchedulerModel.COMMAND_CREATE_TEST)) {
			//toCreate = true;
		} else if (commandName.equals(SchedulerModel.COMMAND_APPLY_TEST)) {
			//toCreate = false;
		} else if (commandName.equals(TestUpdateEvent.TYPE)) {
			//System.out.println(testList.getSelected().getClass().getName());
//			TestUpdateEvent tue = (TestUpdateEvent) ae;
//			Test test = tue.test;
//			setTest(test);
		} else if (commandName.equals(SchedulerModel.COMMAND_CHANGE_TEST_TYPE)) {
			//			String testTypeId = (String) obj;
			//			TestType testType = (TestType) Pool.get(TestType.typ,
			// testTypeId);
			//			this.typeTextField.setText(testType.getName());
			//			this.typeTextField.setCaretPosition(0);
		} else if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
			cleanAllFields();
		} else if (commandName.equals(SchedulerModel.COMMAND_REMOVE_TEST)) {
			Test test = (Test) ae.getSource();
			MonitoredElement me = (MonitoredElement) Pool.get(MonitoredElement.typ, test.getMonitoredElementId());
			this.testList.remove(me);
			if (this.testList.getModel().getSize() == 0)
				cleanAllFields();

		}

	}

	private void cleanAllFields() {
		this.nameTextField.setText("");
		this.typeTextField.setText("");
		this.ownerTextField.setText("");
		if (this.tests != null)
			this.tests.clear();
		this.testRequest = null;
		this.testList.removeAll();
	}

	public TestRequest getParameters() {
		//System.out.println(getClass().getName() + " getParameters()"); //$NON-NLS-1$
		DataSourceInterface dsi = this.aContext.getDataSourceInterface();
		// if test request have saved tests , create new testRequest for new
		// test
		// if request contains only unsaved tests , add new test to it
		boolean needNewReq = false;
		if (this.testRequest == null)
			needNewReq = true;
		else {
		//	System.out.println("current TestRequest:" + this.testRequest.getId()); //$NON-NLS-1$
			java.util.List list = this.testRequest.getTestIds();
			for (Iterator it = list.iterator(); it.hasNext();) {
				String testId = (String) it.next();
				Test test = (Test) Pool.get(Test.TYPE, testId);
	//			System.out.println("testId:" + testId); //$NON-NLS-1$
				if ((!test.isChanged()) && (testId.length() > 0)) {
	//				System.out.println("saved test exists,"); //$NON-NLS-1$
					needNewReq = true;
					break;
				}
			}
			//			if ((!needNewReq) && (toCreate)) {
			//				for (int i = 0; i < testRequest.unsavedTest.size(); i++) {
			//					Test t = (Test) testRequest.unsavedTest.get(i);
			//					if ((t.getKisId().equals(kisId))
			//							&& (t.getMonitoredElementId().equals(meId))) {
			//						System.out
			//								.println("test with the same KIS & ME exists," //$NON-NLS-1$
			//										+ t.getKisId() + "," + kisId + "/" //$NON-NLS-1$ //$NON-NLS-2$
			//										+ t.getMonitoredElementId() + "," + meId); //$NON-NLS-1$
			//						needNewReq = true;
			//						break;
			//					}
			//				}
			//			}
		}

		if (needNewReq) {
		//	System.out.println("new TestRequest required");
			this.testRequest = new TestRequest(dsi.GetUId(TestRequest.TYPE));
		//	System.out.println("set treqId:" + this.testRequest.getId());
			this.testRequest.setName(LangModelSchedule.getString("Test created at") + " "
					+ UIStorage.SDF.format(new Date(System.currentTimeMillis())));
			//System.out.println("set name:" + this.testRequest.getName());
			this.testRequest.setUserId(this.aContext.getSessionInterface().getUserId());
			//testRequest = newTestRequest;
			this.testRequest.setChanged(true);
			Pool.put(TestRequest.TYPE, this.testRequest.getId(), this.testRequest);
			this.tests = null;
		}

		//		TestRequest treq = new TestRequest("");
		{
			String s = this.nameTextField.getText();
			if (s.length() > 0)
				this.testRequest.setName(s);
		}
		{
			String s = this.ownerTextField.getText();
			if (s.length() > 0)
				this.testRequest.setUserId(s);
		}
		this.testRequest.getTestIds().clear();
		if (this.tests != null) {
			for (Iterator it = this.tests.values().iterator(); it.hasNext();) {
				Test test = (Test) it.next();
				this.testRequest.addTest(test);
			}
		}
		setTestRequest(this.testRequest);
		return this.testRequest;
	}

	public void setTest(Test test) {
		//System.out.println(getClass().getName() + " setTest");
		if (test != null) {
			String treqId = test.getRequestId();
			TestRequest treq = (TestRequest) Pool.get(TestRequest.TYPE, treqId);
			setTestRequest(treq);
			MonitoredElement me = (MonitoredElement) Pool.get(MonitoredElement.typ, test.getMonitoredElementId());
			if (me != null) {
				this.testList.setSelected(me);
				//System.out.println("setSelected:" + me.getId());
			} // else System.out.println("tried setSelected me==null");
			//			ListModel lm = testList.getModel();
			//			for (int i = 0; i < lm.getSize(); i++) {
			//				MonitoredElement m = (MonitoredElement) lm.getElementAt(i);
			//				System.out.println(i + "\t" + m.id);
			//				if (m.id.equals(me.id)) {
			//					System.out.println("select:" + i);
			//					testList.setSelectedIndex(i);
			//					break;
			//				}
			//			}
		} else {
			Environment.log(Environment.LOG_LEVEL_WARNING, "test is null");
		}
	}

	public void setTestRequest(TestRequest testRequest) {
		if (this.skip)
			return;
		//System.out.println("setTestRequest");
		this.skip = true;
		this.testRequest = testRequest;
		this.testList.removeAll();
		this.nameTextField.setText(testRequest.getName());
		this.ownerTextField.setText(testRequest.getUserId());
		this.nameTextField.setCaretPosition(0);
		this.ownerTextField.setCaretPosition(0);

		this.tests = new Hashtable();
		Hashtable ht = new Hashtable();
		String type = ""; //$NON-NLS-1$
		//for (Enumeration enum = treq.test_ids.elements();
		// enum.hasMoreElements();)
		java.util.List list = testRequest.getTestIds();
		//System.out.println("list.size():" + list.size());
		for (Iterator it = list.iterator(); it.hasNext();) {
			String testId = (String) it.next();
			//System.out.println("testId:" + testId);
			Test tst = (Test) Pool.get(Test.TYPE, testId);
			//if (!tst.isChanged()) {
			//System.out.println("test:" + tst.getId() + "\t" + tst.getName());
			// //$NON-NLS-1$ //$NON-NLS-2$
			MonitoredElement me = (MonitoredElement) Pool.get(MonitoredElement.typ, tst.getMonitoredElementId());
			//System.out.println("me:" + me.getId() + "\t" +
			// me.element_name);
			ht.put(me.getId(), me);
			this.tests.put(me.getId(), tst);

			type = Pool.getName(TestType.typ, tst.getTestTypeId());
			//}
		}
		//		System.out.println("testRequest.unsavedTest.size():" //$NON-NLS-1$
		//				+ testRequest.unsavedTest.size());
		//		for (int i = 0; i < testRequest.unsavedTest.size(); i++) {
		//			Test tst = (Test) testRequest.unsavedTest.get(i);
		//			MonitoredElement me = (MonitoredElement) Pool.get(
		//					MonitoredElement.TYPE, tst.getMonitoredElementId());
		//			System.out.println("me:" + me.getId() + "\t" + me.element_name);
		// //$NON-NLS-1$ //$NON-NLS-2$
		//			ht.put(me.getId(), me);
		//			tests.put(me.getId(), tst);
		//			type = Pool.getName(TestType.TYPE, tst.getTestTypeId());
		//			System.out.println("type:" + type + "\t" + tst.getTestTypeId());
		// //$NON-NLS-1$ //$NON-NLS-2$
		//		}
		this.testList.setContents(ht);
		//testList.setSelected(test);
		this.typeTextField.setText(type);
		this.typeTextField.setCaretPosition(0);
		this.skip = false;
	}

}