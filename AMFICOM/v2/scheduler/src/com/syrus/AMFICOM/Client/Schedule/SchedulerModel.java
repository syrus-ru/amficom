/*
 * SchedulerModel.java
 * Created on 11.06.2004 10:42:43
 * 
 */
package com.syrus.AMFICOM.Client.Schedule;

import java.util.Date;
import java.util.HashMap;

import com.syrus.AMFICOM.CORBA.General.TestReturnType;
import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TestUpdateEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISM.MonitoredElement;
import com.syrus.AMFICOM.Client.Resource.Result.Analysis;
import com.syrus.AMFICOM.Client.Resource.Result.Evaluation;
import com.syrus.AMFICOM.Client.Resource.Result.Test;
import com.syrus.AMFICOM.Client.Resource.Result.TestArgumentSet;
import com.syrus.AMFICOM.Client.Resource.Result.TestRequest;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;
import com.syrus.AMFICOM.Client.Resource.Result.TimeStamp;
import com.syrus.AMFICOM.Client.Resource.Test.AnalysisType;
import com.syrus.AMFICOM.Client.Resource.Test.EvaluationType;
import com.syrus.AMFICOM.Client.Resource.Test.TestType;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

/**
 * @author Vladimir Dolzhenko
 */
public class SchedulerModel implements OperationListener {

	public static final String	COMMAND_APPLY_TEST				= "ApplyTest";		//$NON-NLS-1$

	public static final String	COMMAND_CREATE_TEST				= "CreateTest";	//$NON-NLS-1$
	public static final String	COMMAND_DATA_REQUEST			= "DataRequest";	//$NON-NLS-1$
	public static final String	COMMAND_SEND_DATA				= "SendData";		//$NON-NLS-1$
	public static final String	COMMAND_TEST_SAVED_OK			= "TestSavedOk";	//$NON-NLS-1$
	public static final String	COMMAND_CLEAN					= "Clean";
	public static final String	COMMAND_AVAILABLE_ME			= "AvailableMe";
	public static final String	COMMAND_CHANGE_STATUSBAR_STATE  = "ChangeStatusBarState";
	

	public static final int		DATA_ID_ELEMENTS				= 5;
	public static final int		DATA_ID_PARAMETERS				= 1;
	public static final int		DATA_ID_PARAMETERS_ANALYSIS		= 6;
	public static final int		DATA_ID_PARAMETERS_EVALUATION	= 7;
	public static final int		DATA_ID_PARAMETERS_PATTERN		= 2;
	public static final int		DATA_ID_RETURN_TYPE				= 8;
	public static final int		DATA_ID_TIMESTAMP				= 3;
	public static final int		DATA_ID_TYPE					= 4;

	//public static final int DEBUG_LEVEL = 3;

	private static final int	FLAG_APPLY						= 1 << 1;
	private static final int	FLAG_CREATE						= 1 << 0;
	private int					flag							= 0;
	private HashMap				receiveData;

	private int					receiveDataCount				= 0;
	private Test				receivedTest;
	private HashMap				receiveTreeElements;
	private TestReturnType		returnType;
	private Dispatcher			dispatcher;										//						=
	// new
	// Dispatcher();
	private ApplicationContext	aContext;

	public SchedulerModel(ApplicationContext aContext) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		this.dispatcher.register(this, SchedulerModel.COMMAND_CREATE_TEST);
		this.dispatcher.register(this, SchedulerModel.COMMAND_APPLY_TEST);
		this.dispatcher.register(this, SchedulerModel.COMMAND_SEND_DATA);
		this.dispatcher.register(this, TestUpdateEvent.TYPE);

	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		int id = ae.getID();
		Object obj = ae.getSource();

		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			receivedTest = tue.test;

			if (tue.testSelected) {
				//this.getContentPane().removeAll();
				//this.getContentPane().add(panel, BorderLayout.CENTER);
				//updateUI();

				TestRequest treq = (TestRequest) Pool.get(TestRequest.TYP,
						receivedTest.getRequestId());
				if (treq == null)
					System.out
							.println("TestRequestFrame.operationPerformed() treq not found id " //$NON-NLS-1$
									+ receivedTest.getRequestId());
				else {
					System.out.println(this.getClass().getName()
							+ " > treq.id: " + treq.getId()); //$NON-NLS-1$
					//panel.setTestRequest(treq);
					//panel.setTest(receivedTest);
				}

			}
		} else if (commandName.equalsIgnoreCase(COMMAND_CREATE_TEST)) {
			// creating test
			//if (flag == 0) {
			flag = FLAG_CREATE;
			if (receiveData == null)
				receiveData = new HashMap();
			else
				receiveData.clear();
			receiveTreeElements = null;

			receiveDataCount = 0;
			dispatcher.notify(new OperationEvent("", 0, //$NON-NLS-1$
					COMMAND_DATA_REQUEST));
			//}
		} else if (commandName.equalsIgnoreCase(COMMAND_APPLY_TEST)) {
			// apply test
			//if (flag == 0) {
			flag = FLAG_APPLY;
			receiveTreeElements = null;
			if (receiveData == null)
				receiveData = new HashMap();
			else
				receiveData.clear();
			receiveDataCount = 0;
			dispatcher.notify(new OperationEvent("", 0, //$NON-NLS-1$
					COMMAND_DATA_REQUEST));
			//}
		} else if (commandName.equalsIgnoreCase(COMMAND_SEND_DATA)) {
			receiveDataCount++;
			if (id == DATA_ID_PARAMETERS) {
				System.out.println("parameters id have got"); //$NON-NLS-1$
			} else if (id == DATA_ID_ELEMENTS) {
				System.out.println("elements id have got"); //$NON-NLS-1$
				receiveTreeElements = (HashMap) obj;
			} else if (id == DATA_ID_TIMESTAMP) {
				System.out.println("timestamp id have hot"); //$NON-NLS-1$
			}
			if (obj instanceof AnalysisType) {
				System.out.println("AnalysisType instanceof have got"); //$NON-NLS-1$
				receiveData.put(AnalysisType.typ, obj);
			} else if (obj instanceof EvaluationType) {
				System.out.println("EvaluationType instanceof have got"); //$NON-NLS-1$
				receiveData.put(EvaluationType.typ, obj);
			} else if (obj instanceof TestArgumentSet) {
				System.out.println("TestArgumentSet instanceof have got"); //$NON-NLS-1$
				receiveData.put(TestArgumentSet.typ, obj);
				receiveDataCount += 2;
				//receiveTestArgumentSet = (TestArgumentSet) obj;
			} else if (obj instanceof TestSetup) {
				System.out.println("TestSetup instanceof have got"); //$NON-NLS-1$
				//receiveTestSetup = (TestSetup) obj;
				System.out.println(((TestSetup) obj).getId());
				receiveData.put(TestSetup.typ, obj);
			} else if (obj instanceof TimeStamp) {
				System.out.println("timestamp instanceof have got"); //$NON-NLS-1$
				//receiveTimeStamp = (TimeStamp) obj;
				receiveData.put(TimeStamp.TYP, obj);
			} else if (obj instanceof TestReturnType) {
				returnType = (TestReturnType) obj;
			} else if (obj instanceof TestRequest) {
				receiveData.put(TestRequest.TYP, obj);
			}
			System.out.println("receiveDataCount:" + receiveDataCount); //$NON-NLS-1$
			if (7 == receiveDataCount) {
				if ((flag & FLAG_CREATE) != 0) {
					System.out.println("createTest"); //$NON-NLS-1$
					receivedTest = null;
					createTest();
				} else if ((flag & FLAG_APPLY) != 0) {
					System.out.println("applyTest"); //$NON-NLS-1$
					//					if (receivedTest==null)
					createTest();
					//					else applyTest();
				}
				flag = 0;
			}
		} else {
			//this.getContentPane().removeAll();
			//JList emptyList = new JList();
			//emptyList.setBorder(BorderFactory.createLoweredBevelBorder());
			//this.getContentPane().add(emptyList, BorderLayout.CENTER);
			//updateUI();
		}
	}
	private void createTest() {
		DataSourceInterface dsi = this.aContext.getDataSourceInterface();
		Test test = this.receivedTest;
		if (test == null) {
			test = new Test(dsi.GetUId(Test.TYPE)); //$NON-NLS-1$
			test.setStatus(TestStatus.TEST_STATUS_SCHEDULED);
			Pool.put(Test.TYPE, test.getId(), test);
		}

		TestType testType = (TestType) this.receiveTreeElements.get(TestType.typ);
		KIS kis = (KIS) this.receiveTreeElements.get(KIS.typ);
		MonitoredElement me = (MonitoredElement) this.receiveTreeElements
				.get(MonitoredElement.typ);
		test.setTestTypeId(testType.getId());
		test.setKisId(kis.id);
		test.setMonitoredElementId(me.id);

		test.setReturnType(this.returnType);
		test.setUserId(this.aContext.getSessionInterface().getUserId());
		test.setTimeStamp((TimeStamp) this.receiveData.get(TimeStamp.TYP));
		{

			TestArgumentSet testArgumentSet = (TestArgumentSet) this.receiveData
					.get(TestArgumentSet.typ);
			if (testArgumentSet != null) {
				testArgumentSet.setTestTypeId(testType.getId());
				testArgumentSet.setId(dsi.GetUId(TestArgumentSet.typ));
				testArgumentSet.setChanged(true);
				Pool.put(TestArgumentSet.typ, testArgumentSet.getId(),
						testArgumentSet);
				testArgumentSet.setName(testArgumentSet.getId());
				test.setTestArgumentSet(testArgumentSet);
				test.setTestArgumentSetId(testArgumentSet.getId());
			}
		}

		test.setTestSetup((TestSetup) receiveData.get(TestSetup.typ));
		if (test.getTestSetup() != null)
				test.setTestSetupId(test.getTestSetup().getId());

		{
			AnalysisType analysisType = (AnalysisType) receiveData
					.get(AnalysisType.typ);
			if (analysisType == null)
				test.setAnalysisId(""); //$NON-NLS-1$
			else {
				Analysis analysis = new Analysis(dsi.GetUId(Analysis.typ));
				analysis.setMonitoredElementId(me.getId());
				analysis.setTypeId(analysisType.getId());
				analysis.setCriteriaSetId(test.getTestSetup()
						.getCriteriaSetId());
				test.setAnalysisId(analysis.getId());
				analysis.setChanged(true);
				Pool.put(Analysis.typ, analysis.getId(), analysis);
				//System.err.println("test.analysis_id:" + test.analysis_id);
			}

			EvaluationType evaluationType = (EvaluationType) receiveData
					.get(EvaluationType.typ);
			if (evaluationType == null)
				test.setEvaluationId(""); //$NON-NLS-1$
			else {
				Evaluation evaluation = new Evaluation(dsi
						.GetUId(Evaluation.typ));
				evaluation.setMonitoredElementId(me.getId());
				evaluation.setTypeId(evaluationType.getId());
				evaluation.setThresholdSetId(test.getTestSetup()
						.getThresholdSetId());
				evaluation.setEthalonId(test.getTestSetup().getEthalonId());
				test.setEvaluationId(evaluation.getId());
				evaluation.setChanged(true);
				Pool.put(Evaluation.typ, evaluation.getId(), evaluation);
				//System.err.println("test.evaluation_id:" +
				// test.evaluation_id);
			}

		}
		TestRequest testRequest = (TestRequest) this.receiveData
				.get(TestRequest.TYP);

		testRequest.addTest(test);
		test.setRequestId(testRequest.getId());
		test.setChanged(true);
		
		test.setName(ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(test.getStartTime())));
		//testRequest.setName(test.getName());		
		this.dispatcher.notify(new TestUpdateEvent(this, test,
				TestUpdateEvent.TEST_SELECTED_EVENT));
	}
}
