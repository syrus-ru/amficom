package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class TestRequestFrame extends JInternalFrame implements
		OperationListener {

	//	private ApplicationContext aContext;
	private Dispatcher			dispatcher;
	private Test				receivedTest;
	private TestRequestPanel	panel;

	public TestRequestFrame(ApplicationContext aContext) {
		//this.aContext = aContext;
		setTitle(LangModelSchedule.getString("TestOptions")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		panel = new TestRequestPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.dispatcher = aContext.getDispatcher();
		this.dispatcher.register(this, TestUpdateEvent.TYPE);

	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:"
				+ commandName, getClass().getName());
		//		int id = ae.getID();
		//		Object obj = ae.getSource();

		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			receivedTest = tue.test;

			if (tue.testSelected) {
				TestRequest treq = (TestRequest) Pool.get(TestRequest.TYP,
						receivedTest.getRequestId());
				if (treq == null)
					System.out
							.println("TestRequestFrame.operationPerformed() treq not found id " //$NON-NLS-1$
									+ receivedTest.getRequestId());
				else {
					System.out.println(this.getClass().getName()
							+ " > treq.id: " + treq.getId()); //$NON-NLS-1$
					panel.setTestRequest(treq);
					panel.setTest(receivedTest);
				}

			}
		}
		//		else if (commandName
		//				.equalsIgnoreCase(SchedulerModel.COMMAND_CREATE_TEST)) {
		//			// creating test
		//			//if (flag == 0) {
		//			flag = FLAG_CREATE;
		//			if (receiveData == null)
		//				receiveData = new HashMap();
		//			else
		//				receiveData.clear();
		//			receiveTreeElements = null;
		//
		//			receiveDataCount = 0;
		//			dispatcher.notify(new OperationEvent("", 0, //$NON-NLS-1$
		//					SchedulerModel.COMMAND_DATA_REQUEST));
		//			//}
		//		} else if (commandName
		//				.equalsIgnoreCase(SchedulerModel.COMMAND_APPLY_TEST)) {
		//			// apply test
		//			//if (flag == 0) {
		//			flag = FLAG_APPLY;
		//			receiveTreeElements = null;
		//			if (receiveData == null)
		//				receiveData = new HashMap();
		//			else
		//				receiveData.clear();
		//			receiveDataCount = 0;
		//			dispatcher.notify(new OperationEvent("", 0, //$NON-NLS-1$
		//					SchedulerModel.COMMAND_DATA_REQUEST));
		//			//}
		//		} else if (commandName
		//				.equalsIgnoreCase(SchedulerModel.COMMAND_SEND_DATA)) {
		//			receiveDataCount++;
		//			if (id == SchedulerModel.DATA_ID_PARAMETERS) {
		//				System.out.println("parameters id have got"); //$NON-NLS-1$
		//			} else if (id == SchedulerModel.DATA_ID_ELEMENTS) {
		//				System.out.println("elements id have got"); //$NON-NLS-1$
		//				receiveTreeElements = (HashMap) obj;
		//			} else if (id == SchedulerModel.DATA_ID_TIMESTAMP) {
		//				System.out.println("timestamp id have hot"); //$NON-NLS-1$
		//			}
		//			if (obj instanceof AnalysisType) {
		//				System.out.println("AnalysisType instanceof have got"); //$NON-NLS-1$
		//				receiveData.put(AnalysisType.TYPE, obj);
		//			} else if (obj instanceof EvaluationType) {
		//				System.out.println("EvaluationType instanceof have got");
		// //$NON-NLS-1$
		//				receiveData.put(EvaluationType.TYPE, obj);
		//			} else if (obj instanceof TestArgumentSet) {
		//				System.out.println("TestArgumentSet instanceof have got");
		// //$NON-NLS-1$
		//				receiveData.put(TestArgumentSet.TYPE, obj);
		//				receiveDataCount += 2;
		//				//receiveTestArgumentSet = (TestArgumentSet) obj;
		//			} else if (obj instanceof TestSetup) {
		//				System.out.println("TestSetup instanceof have got"); //$NON-NLS-1$
		//				//receiveTestSetup = (TestSetup) obj;
		//				System.out.println(((TestSetup) obj).getId());
		//				receiveData.put(TestSetup.TYPE, obj);
		//			} else if (obj instanceof TimeStamp) {
		//				System.out.println("timestamp instanceof have got"); //$NON-NLS-1$
		//				//receiveTimeStamp = (TimeStamp) obj;
		//				receiveData.put(TimeStamp.TYPE, obj);
		//			} else if (obj instanceof TestReturnType) {
		//				returnType = (TestReturnType) obj;
		//			} else if (obj instanceof TestRequest) {
		//				receiveData.put(TestRequest.TYPE, obj);
		//			}
		//			System.out.println("receiveDataCount:" + receiveDataCount);
		// //$NON-NLS-1$
		//			if (7 == receiveDataCount) {
		//				if ((flag & FLAG_CREATE) != 0) {
		//					System.out.println("createTest"); //$NON-NLS-1$
		//					receivedTest = null;
		//					createTest();
		//				} else if ((flag & FLAG_APPLY) != 0) {
		//					System.out.println("applyTest"); //$NON-NLS-1$
		//					// if (receivedTest==null)
		//					createTest();
		//					// else applyTest();
		//				}
		//				flag = 0;
		//			}
		//		} else {
		//			//this.getContentPane().removeAll();
		//			//JList emptyList = new JList();
		//			//emptyList.setBorder(BorderFactory.createLoweredBevelBorder());
		//			//this.getContentPane().add(emptyList, BorderLayout.CENTER);
		//			//updateUI();
		//		}
	}

	//	private void initModule(Dispatcher dispatcher) {
	//		this.dispatcher = dispatcher;
	//		this.dispatcher.register(this, TestUpdateEvent.TYPE);
	//	}

}
