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
import com.syrus.AMFICOM.Client.General.Lang.LangModelScheduler;
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
	//	private String meId;
	//	private boolean toCreate = true;

	public TestRequestPanel(ApplicationContext aContext) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.register(this, TestParametersPanel.COMMAND_CHANGE_KIS);
		this.dispatcher.register(this,
				TestParametersPanel.COMMAND_CHANGE_ME_TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CREATE_TEST);
		this.dispatcher.register(this, SchedulerModel.COMMAND_APPLY_TEST);
		//this.dispatcher.register(this, TestUpdateEvent.TYPE);

	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		//Object obj = ae.getSource();
		if (SchedulerModel.DEBUG_LEVEL >= 5)
				System.out.println(getClass().getName() + " commandName: " //$NON-NLS-1$
						+ commandName);
		if (commandName.equalsIgnoreCase(SchedulerModel.COMMAND_DATA_REQUEST)) {
			/**
			 * @todo must send data edit in this form 
			 */
			TestRequest testRequest = getParameters();

			dispatcher.notify(new OperationEvent(
					testRequest == null ? (Object) "" : (Object) testRequest, //$NON-NLS-1$
					0, SchedulerModel.COMMAND_SEND_DATA));
		} else if (commandName.equals(TestParametersPanel.COMMAND_CHANGE_KIS)) {
			//kisId = (String) obj;
			//System.out.println("kisId:" + kisId);
		} else if (commandName
				.equals(TestParametersPanel.COMMAND_CHANGE_ME_TYPE)) {
			//meId = (String) obj;
			//System.out.println("meId:" + meId);
		} else if (commandName.equals(SchedulerModel.COMMAND_CREATE_TEST)) {
			//toCreate = true;
		} else if (commandName.equals(SchedulerModel.COMMAND_APPLY_TEST)) {
			//toCreate = false;
		}
		//		else if (commandName.equals(TestUpdateEvent.TYPE)) {
		//			System.out.println(testList.getSelected().getClass().getName());
		//			TestUpdateEvent tue = (TestUpdateEvent) ae;
		//			Test test = tue.test;
		//			MonitoredElement me = (MonitoredElement) Pool.get(
		//					MonitoredElement.TYPE, test.monitored_element_id);
		//			if (tue.TEST_SELECTED) {
		//				testList.setSelected(me);
		//			}
		//		}
	}

	public TestRequest getParameters() {
		System.out.println(getClass().getName() + " getParameters()"); //$NON-NLS-1$
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		// if test request have saved tests , create new testRequest for new
		// test
		// if request contains only unsaved tests , add new test to it
		boolean needNewReq = false;
		if (testRequest == null)
			needNewReq = true;
		else {
			System.out.println("current TestRequest:" + testRequest.getId()); //$NON-NLS-1$
			java.util.List list = testRequest.getTestIds();
			for (Iterator it = list.iterator(); it.hasNext();) {
				String testId = (String) it.next();
				Test test = (Test) Pool.get(Test.TYPE, testId);
				System.out.println("testId:" + testId); //$NON-NLS-1$
				if ((!test.isChanged()) && (testId.length() > 0)) {
					System.out.println("saved test exists,"); //$NON-NLS-1$
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
			testRequest = new TestRequest(dsi.GetUId(TestRequest.typ));
			testRequest.setName(LangModelScheduler.getString("Test created at")
					+ " "
					+ UIStorage.SDF
							.format(new Date(System.currentTimeMillis())));
			testRequest.setUserId(aContext.getSessionInterface().getUserId());
			//testRequest = newTestRequest;
			testRequest.setChanged(true);
			Pool.put(TestRequest.typ, testRequest.getId(), testRequest);
			tests = null;
		}

		//		TestRequest treq = new TestRequest("");
		{
			String s = nameTextField.getText();
			if (s.length() > 0) testRequest.setName(s);
		}
		{
			String s = ownerTextField.getText();
			if (s.length() > 0) testRequest.setUserId(s);
		}
		testRequest.getTestIds().clear();
		if (tests != null) {
			for (Iterator it = tests.values().iterator(); it.hasNext();) {
				Test test = (Test) it.next();
				testRequest.addTest(test);
			}
		}
		setTestRequest(testRequest);
		return testRequest;
	}

	public void setTest(Test test) {
		//System.out.println(getClass().getName() + " setTest");
		if (test != null) {
			MonitoredElement me = (MonitoredElement) Pool.get(
					MonitoredElement.typ, test.getMonitoredElementId());
			if (me != null) {
				testList.setSelected(me);
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
			System.err.println("test is null"); //$NON-NLS-1$
		}
	}

	public void setTestRequest(TestRequest testRequest) {
		if (skip) return;
		skip = true;
		this.testRequest = testRequest;
		testList.removeAll();
		nameTextField.setText(testRequest.getName());
		ownerTextField.setText(testRequest.getUserId());
		nameTextField.setCaretPosition(0);
		ownerTextField.setCaretPosition(0);

		tests = new Hashtable();
		Hashtable ht = new Hashtable();
		String type = ""; //$NON-NLS-1$
		//for (Enumeration enum = treq.test_ids.elements();
		// enum.hasMoreElements();)
		java.util.List list = testRequest.getTestIds();
		for (Iterator it = list.iterator(); it.hasNext();) {
			String test_id = (String) it.next();
			Test tst = (Test) Pool.get(Test.TYPE, test_id);
			if (!tst.isChanged()) {
				System.out
						.println("test:" + tst.getId() + "\t" + tst.getName()); //$NON-NLS-1$ //$NON-NLS-2$
				MonitoredElement me = (MonitoredElement) Pool.get(
						MonitoredElement.typ, tst.getMonitoredElementId());
				//System.out.println("me:" + me.getId() + "\t" +
				// me.element_name);
				ht.put(me.getId(), me);
				tests.put(me.getId(), tst);

				type = Pool.getName(TestType.typ, tst.getTestTypeId());
			}
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
		testList.setContents(ht);
		//testList.setSelected(test);
		typeTextField.setText(type);
		typeTextField.setCaretPosition(0);
		skip = false;
	}

	private void jbInit() throws Exception {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		//		this.setLayout(new GridLayout(0,2));
		JLabel titleLabel = new JLabel(LangModelScheduler.getString("Title") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.weightx = 0.0;
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(titleLabel, gbc);
		//		add(titleLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(nameTextField, gbc);
		//		add(nameTextField);

		JLabel ownerLabel = new JLabel(LangModelScheduler.getString("Owner") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.weightx = 0.0;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		add(ownerLabel, gbc);
		//		add(ownerLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(ownerTextField, gbc);
		//		add(ownerTextField);

		JLabel typeLabel = new JLabel(LangModelScheduler.getString("Type") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.weightx = 0.0;
		add(typeLabel, gbc);
		//		add(typeLabel);
		gbc.weightx = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(typeTextField, gbc);
		//		add(typeTextField);

		JLabel objectLabel = new JLabel(LangModelScheduler.getString("TestObject") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(objectLabel, gbc);
		//		add(objectLabel);
		gbc.weightx = 4.0;
		gbc.weighty = 4.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		JScrollPane listPanel = new JScrollPane(testList);
		add(listPanel, gbc);
		//		add(listPanel);

		testList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				//System.out.println("ListSelectionListener");
				if ((e.getValueIsAdjusting()) || skip) return;
				MonitoredElement me = (MonitoredElement) testList
						.getSelectedObjectResource();
				if (me == null) {
					dispatcher.notify(new TestUpdateEvent(this, null,
							TestUpdateEvent.TEST_DESELECTED_EVENT));
					//System.out.println("me == null");
				} else {
					skip = true;
					Test test = (Test) tests.get(me.getId());
					if (test != null)
							dispatcher.notify(new TestUpdateEvent(this, test,
									TestUpdateEvent.TEST_SELECTED_EVENT));
					skip = false;
				}
			}
		});
	}
}