package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.General.TestTimeStampsPackage.PeriodicalTestParameters;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Schedule.ScheduleMainFrame;

public class TestRequestFrame extends JInternalFrame implements
		OperationListener {

	public static final String	COMMAND_CREATE_TEST				= "CreateTest";
	public static final String	COMMAND_APPLY_TEST				= "ApplyTest";
	public static final String	COMMAND_DATA_REQUEST			= "DataRequest";
	public static final String	COMMAND_SEND_DATA				= "SendData";

	public static final int		DATA_ID_PARAMETERS				= 1;
	public static final int		DATA_ID_PARAMETERS_PATTERN		= 2;
	public static final int		DATA_ID_TIMESTAMP				= 3;
	public static final int		DATA_ID_TYPE					= 4;
	public static final int		DATA_ID_ELEMENTS				= 5;
	public static final int		DATA_ID_PARAMETERS_ANALYSIS		= 6;
	public static final int		DATA_ID_PARAMETERS_EVALUATION	= 7;
	public static final int		DATA_ID_RETURN_TYPE				= 8;

	private int					receiveDataCount				= 0;
	private static final int	FLAG_CREATE						= 1 << 0;
	private static final int	FLAG_APPLY						= 1 << 1;
	private int					flag							= 0;

	private ApplicationContext	aContext;
	private Dispatcher			dispatcher;
	private TestRequestPanel	panel;

	//	private ArrayList unsavedTestList;

	private HashMap				receiveTreeElements;
	private HashMap				receiveData;
	private TestReturnType		returnType;

	//	private TimeStamp receiveTimeStamp;
	//	private TestSetup receiveTestSetup;
	//	private TestArgumentSet receiveTestArgumentSet;

	public TestRequestFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initModule(aContext.getDispatcher());
	}

	private void jbInit() throws Exception {
		setTitle("Свойства теста");
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"images/general.gif")));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		panel = new TestRequestPanel(aContext);
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.typ);
		this.dispatcher.register(this, COMMAND_CREATE_TEST);
		this.dispatcher.register(this, COMMAND_APPLY_TEST);
		this.dispatcher.register(this, COMMAND_SEND_DATA);

	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		if (ScheduleMainFrame.DEBUG)
				System.out.println(getClass().getName() + " commandName: "
						+ commandName);
		int id = ae.getID();
		Object obj = ae.getSource();

		if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if (tue.TEST_SELECTED) {
				//this.getContentPane().removeAll();
				//this.getContentPane().add(panel, BorderLayout.CENTER);
				//updateUI();

				TestRequest treq = (TestRequest) Pool.get(TestRequest.typ,
						test.request_id);
				if (treq == null)
					System.out
							.println("TestRequestFrame.operationPerformed() treq not found id "
									+ test.request_id);
				else {
					System.out.println(this.getClass().getName()
							+ " > treq.id: " + treq.id);
					panel.setTestRequest(treq);

				}

			}
		} else if (commandName.equalsIgnoreCase(COMMAND_CREATE_TEST)) {
			// creating test
			if (flag == 0) {
				flag = FLAG_CREATE;
				if (receiveData == null)
					receiveData = new HashMap();
				else
					receiveData.clear();
				receiveTreeElements = null;

				receiveDataCount = 0;
				dispatcher.notify(new OperationEvent("", 0,
						TestRequestFrame.COMMAND_DATA_REQUEST));
			}
		} else if (commandName.equalsIgnoreCase(COMMAND_APPLY_TEST)) {
			// apply test
			if (flag == 0) {
				flag = FLAG_APPLY;
				receiveTreeElements = null;
				if (receiveData == null)
					receiveData = new HashMap();
				else
					receiveData.clear();
				receiveDataCount = 0;
				dispatcher.notify(new OperationEvent("", 0,
						TestRequestFrame.COMMAND_DATA_REQUEST));
			}
		} else if (commandName.equalsIgnoreCase(COMMAND_SEND_DATA)) {
			receiveDataCount++;
			if (id == DATA_ID_PARAMETERS) {
				System.out.println("parameters id have got");
			} else if (id == DATA_ID_ELEMENTS) {
				System.out.println("elements id have got");
				receiveTreeElements = (HashMap) obj;
			} else if (id == DATA_ID_TIMESTAMP) {
				System.out.println("timestamp id have hot");
			}
			if (obj instanceof AnalysisType) {
				System.out.println("AnalysisType instanceof have got");
				receiveData.put(AnalysisType.typ, obj);
			} else if (obj instanceof EvaluationType) {
				System.out.println("EvaluationType instanceof have got");
				receiveData.put(EvaluationType.typ, obj);
			} else if (obj instanceof TestArgumentSet) {
				System.out.println("TestArgumentSet instanceof have got");
				receiveData.put(TestArgumentSet.typ, obj);
				receiveDataCount+=2;
				//receiveTestArgumentSet = (TestArgumentSet) obj;
			} else if (obj instanceof TestSetup) {
				System.out.println("TestSetup instanceof have got");
				//receiveTestSetup = (TestSetup) obj;
				System.out.println(((TestSetup) obj).id);
				receiveData.put(TestSetup.typ, obj);
			} else if (obj instanceof TimeStamp) {
				System.out.println("timestamp instanceof have got");
				//receiveTimeStamp = (TimeStamp) obj;
				receiveData.put(TimeStamp.typ, obj);
			} else if (obj instanceof TestReturnType) {
				returnType = (TestReturnType) obj;
			}
			System.out.println("receiveDataCount:" + receiveDataCount);
			if (7 == receiveDataCount) {
				if ((flag & FLAG_CREATE) != 0) {
					System.out.println("createTest");
					createTest();
				} else if ((flag & FLAG_APPLY) != 0) {
					System.out.println("applyTest");
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
		Test test = new Test(""); //текущий тест
		test.id = null; // id of unsaved test , will set directly before
		// saving
		test.status = TestStatus.TEST_STATUS_SCHEDULED;
		TestType testType = (TestType) receiveTreeElements.get(TestType.typ);
		KIS kis = (KIS) receiveTreeElements.get(KIS.typ);
		MonitoredElement me = (MonitoredElement) receiveTreeElements
				.get(MonitoredElement.typ);
		test.test_type_id = testType.id;
		test.kis_id = kis.id;
		test.monitored_element_id = me.id;

		test.return_type = returnType;

		TestTimeStamps test_time_stamps = new TestTimeStamps();
		test_time_stamps._default();

		// this is new TimeStamp model
		test.timeStamp = (TimeStamp) receiveData.get(TimeStamp.typ);
		TimeStamp timeStamp = test.timeStamp;

		if (test.timeStamp.getType() == TimeStamp.TIMESTAMPTYPE_ONETIME) {
			test.temporal_type = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
			test.start_time = test.timeStamp.getPeriodStart();
			//			tempTest.duration = receiveTimeStamp.getPeriodEnd() -
			// receiveTimeStamp.getPeriodStart();
			test.duration = 0;
			timeStamp.setPeriodEnd(timeStamp.getPeriodStart());

		} else if (test.timeStamp.getType() == TimeStamp.TIMESTAMPTYPE_PERIODIC) {
			test.temporal_type = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;
			test.start_time = test.timeStamp.getPeriodStart();
			/**
			 * @TODO periodical time
			 */
			Time period = test.timeStamp.getPeriod();
			long interval = 0;
			int scale = period.getScale();
			int value = period.getValue();
			switch (scale) {
				case Calendar.MINUTE:
					interval = 1000 * 60 * value;
					break;
				case Calendar.HOUR:
					interval = 1000 * 60 * 60 * value;
					break;

			/**
			 * @todo other period scales
			 */

			}
			PeriodicalTestParameters ptp = new PeriodicalTestParameters();
			ptp.dt = interval;
			ptp.end_time = test.timeStamp.getPeriodEnd();
			test_time_stamps.ptpars(ptp);
		}
		// this is only for backward compatability
		test.time_stamps = test_time_stamps;

		test.test_argument_set_id = "";
		test.testArgumentSet = (TestArgumentSet) receiveData
				.get(TestArgumentSet.typ);
		
		test.testSetup = (TestSetup) receiveData.get(TestSetup.typ);
		if (test.testSetup!=null)
			test.test_setup_id = test.testSetup.id;

		{
			DataSourceInterface dsi = aContext.getDataSourceInterface();
			AnalysisType analysisType = (AnalysisType) receiveData
					.get(AnalysisType.typ);
			if (analysisType == null)
				test.analysis_id = "";
			else {
				Analysis anal = new Analysis(dsi.GetUId(Analysis.typ));
				anal.monitored_element_id = me.id;
				anal.type_id = analysisType.id;
				anal.criteria_set_id = test.testSetup.criteria_set_id;
				test.analysis_id = anal.id;
				Pool.put(Analysis.typ, anal.getId(), anal);
				System.err.println("test.analysis_id:" + test.analysis_id);
			}

			EvaluationType evaluationType = (EvaluationType) receiveData
					.get(EvaluationType.typ);
			if (evaluationType == null)
				test.evaluation_id = "";
			else {
				Evaluation eval = new Evaluation(dsi.GetUId(Evaluation.typ));
				eval.monitored_element_id = me.id;
				eval.type_id = evaluationType.id;
				eval.threshold_set_id = test.testSetup.threshold_set_id;
				eval.etalon_id = test.testSetup.etalon_id;
				test.evaluation_id = eval.getId();
				Pool.put(Evaluation.typ, eval.getId(), eval);
				System.err.println("test.evaluation_id:" + test.evaluation_id);
			}

		}

		this.dispatcher.notify(new TestUpdateEvent(this, test,
				TestUpdateEvent.TEST_SELECTED_EVENT));
		//unsavedTestList.add(tempTest);

		//		String test_type_id = ""; //id текущего типа теста
		//		String kis_id = ""; //id текущего КИСа
		//		String port_id = ""; //id текущего порта
		//		String me_id = ""; //id текущего MonitoredElement
		//		String test_setup_id = ""; //id текущего TestSetup
		//		String analysis_type_id = ""; //id текущего типа анализа
		//		String evaluation_type_id = ""; //id текущего типа оценки
		//
		//		DataSourceInterface dsi = aContext.getDataSourceInterface();
		//		tempTest.test_setup_id = test_setup_id;
		//		TestArgumentSet as = new TestArgumentSet();
		//		if (!test_setup_id.equals("")) {
		//			TestSetup ts = (TestSetup) Pool.get(TestSetup.typ, test_setup_id);
		//			tempTest.test_argument_set_id = ts.test_argument_set_id;
		//			as = (TestArgumentSet) Pool.get(TestArgumentSet.typ,
		//					ts.test_argument_set_id);
		//			if (!analysis_type_id.equals("")) {
		//				Analysis analysis = new Analysis(dsi.GetUId(Analysis.typ));
		//				analysis.monitored_element_id = meId;
		//				analysis.type_id = analysis_type_id;
		//				analysis.criteria_set_id = ts.criteria_set_id;
		//				tempTest.analysis_id = analysis.getId();
		//				Pool.put(Analysis.typ, analysis.getId(), analysis);
		//			} else {
		//				tempTest.analysis_id = "";
		//			}
		//
		//			if (!evaluation_type_id.equals("")) {
		//				Evaluation eval = new Evaluation(dsi.GetUId(Evaluation.typ));
		//				eval.monitored_element_id = meId;
		//				eval.type_id = evaluation_type_id;
		//				eval.threshold_set_id = ts.threshold_set_id;
		//				eval.etalon_id = ts.etalon_id;
		//				tempTest.evaluation_id = eval.getId();
		//				Pool.put(Evaluation.typ, eval.getId(), eval);
		//			} else {
		//				tempTest.evaluation_id = "";
		//			}
		//		}
		//		tempTest.kis_id = kisId;
		//		tempTest.monitored_element_id = meId;
		//		tempTest.test_type_id = test_type_id;
		//		//temp_test.temporal_type = ttt;
		//		//String aa = tempTest.temporal_type.toString();
		//		tempTest.status = TestStatus.TEST_STATUS_SCHEDULED;
		//
		//		//---------------------------------------------------------------------
		//		/*
		//		 * temp_test.start_time = start_time; temp_test.return_type =
		//		 * returntype; TestTimeStamps tts = new TestTimeStamps(); switch
		//		 * (temp_test.temporal_type.value()) { case
		//		 * TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME : tts._default();
		// break;
		//		 * case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL : int temp_ti
		// =
		//		 * (int) ((end_time - start_time) / interval);
		// PeriodicalTestParameters
		//		 * ptp = new PeriodicalTestParameters( interval, start_time + temp_ti
		// *
		//		 * interval); tts.ptpars(ptp); break; case
		//		 * TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE : long[] times = new
		//		 * long[temp_times.size()]; for (int i = 0; i < temp_times.size();
		// i++)
		//		 * times[i] = Long.parseLong(temp_times.elementAt(i).toString());
		//		 * tts.ti(times); break; default : System.out.println( "ERROR: Unknown
		//		 * temporal type: " + temp_test.temporal_type.value()); }
		//		 * temp_test.time_stamps = tts;
		//		 */
		//
		//		if (test_setup_id.equals("")) {
		//			as.id = dsi.GetUId(TestArgumentSet.typ);
		//			Pool.put(TestArgumentSet.typ, as.getId(), as);
		//			as.name = as.id;
		//			as.created = 0;
		//			as.created_by = "";
		//			as.test_type_id = test_type_id;
		//
		//			Parameter par_trans;
		//			
		//			dsi.saveTestArgumentSet(as.getId());
		//			tempTest.test_argument_set_id = as.getId();
		//		}
		//
		//		//Test clone_test = temp_test.myclone();
		//		//baza_test_new.add(clone_test);
	}

}