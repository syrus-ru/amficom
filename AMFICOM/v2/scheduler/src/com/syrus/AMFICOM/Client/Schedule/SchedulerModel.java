/*
 * SchedulerModel.java
 * Created on 11.06.2004 10:42:43
 * 
 */
package com.syrus.AMFICOM.Client.Schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.CORBA.General.TestReturnType;
import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.Survey.ElementaryTestAlarm;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Event.TestUpdateEvent;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceDomainFilter;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptySurveyDataSource;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.RISDSurveyDataSource;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
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
import com.syrus.AMFICOM.Client.Schedule.Filter.TestFilter;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

/**
 * @author Vladimir Dolzhenko
 */
public class SchedulerModel extends ApplicationModel implements OperationListener {

	public static final String		COMMAND_ADD_PARAM_PANEL			= "AddParamPanel";			//$NON-NLS-1$
	public static final String		COMMAND_APPLY_TEST				= "ApplyTest";				//$NON-NLS-1$
	public static final String		COMMAND_AVAILABLE_ME			= "AvailableMe";
	public static final String		COMMAND_CHANGE_KIS				= "ChangeKIS";				//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_ME_TYPE			= "ChangeMEType";			//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_PARAM_PANEL		= "ChangeParamPanel";		//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_PORT_TYPE		= "ChangePortType";		//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_STATUSBAR_STATE	= "ChangeStatusBarState";
	public static final String		COMMAND_CHANGE_TEST_TYPE		= "ChangeTestType";		//$NON-NLS-1$
	public static final String		COMMAND_CLEAN					= "Clean";
	public static final String		COMMAND_COMMIT_CHANGES			= "CommitChanges";
	public static final String		COMMAND_CREATE_TEST				= "CreateTest";			//$NON-NLS-1$
	public static final String		COMMAND_DATA_REQUEST			= "DataRequest";			//$NON-NLS-1$
	public static final String		COMMAND_NAME_ALL_TESTS			= "AllTests";				//$NON-NLS-1$
	public static final String		COMMAND_REMOVE_TEST				= "RemoveTest";
	public static final String		COMMAND_SEND_DATA				= "SendData";				//$NON-NLS-1$
	public static final String		COMMAND_TEST_SAVED_OK			= "TestSavedOk";			//$NON-NLS-1$

	public static final int			DATA_ID_ELEMENTS				= 5;
	public static final int			DATA_ID_PARAMETERS				= 1;
	public static final int			DATA_ID_PARAMETERS_ANALYSIS		= 6;
	public static final int			DATA_ID_PARAMETERS_EVALUATION	= 7;
	public static final int			DATA_ID_PARAMETERS_PATTERN		= 2;
	public static final int			DATA_ID_RETURN_TYPE				= 8;
	public static final int			DATA_ID_TIMESTAMP				= 3;
	public static final int			DATA_ID_TYPE					= 4;

	private static final boolean	CREATE_ALLOW					= true;

	//public static final int DEBUG_LEVEL = 3;

	private static final int		FLAG_APPLY						= 1 << 1;
	private static final int		FLAG_CREATE						= 1 << 0;
	// new
	// Dispatcher();
	private ApplicationContext		aContext;
	private Dispatcher				dispatcher;												//						=

	private ObjectResourceFilter	filter							= new TestFilter();
	private int						flag							= 0;
	private HashMap					receiveData;

	//private int receiveDataCount = 0;
	private Test					receivedTest;
	private HashMap					receiveTreeElements;
	private TestReturnType			returnType;

	private List					tests;
	private List					allTests;

	private ObjectResourceTreeModel	treeModel;
	private List					unsavedTests;
	private List					allUnsavedTests;

	public SchedulerModel(ApplicationContext aContext) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		this.dispatcher.register(this, COMMAND_CREATE_TEST);
		this.dispatcher.register(this, COMMAND_APPLY_TEST);
		this.dispatcher.register(this, COMMAND_SEND_DATA);
		this.dispatcher.register(this, COMMAND_SEND_DATA);
		this.dispatcher.register(this, COMMAND_COMMIT_CHANGES);
		this.dispatcher.register(this, COMMAND_CLEAN);
		this.dispatcher.register(this, COMMAND_REMOVE_TEST);
		this.dispatcher.register(this, TestUpdateEvent.TYPE);

		//
		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionSave");
		add("menuSessionUndo");
		add("menuSessionDomain");
		add("menuExit");

		add(ScheduleMainMenuBar.MENU_VIEW);
		add(ScheduleMainMenuBar.MENU_VIEW_PLAN);
		add(ScheduleMainMenuBar.MENU_VIEW_TREE);
		add(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS);
		add(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS);
		add(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES);
		add(ScheduleMainMenuBar.MENU_VIEW_TIME);
		add(ScheduleMainMenuBar.MENU_VIEW_TABLE);

		add(ScheduleMainMenuBar.MENU_HELP);
		add(ScheduleMainMenuBar.MENU_HELP_ABOUT);

		setVisible("menuSessionSave", false);
		setVisible("menuSessionUndo", false);
		setVisible("menuSessionOptions", false);

		setVisible(ScheduleMainMenuBar.MENU_VIEW, true);
		setVisible(ScheduleMainMenuBar.MENU_VIEW_PLAN, true);
		setVisible(ScheduleMainMenuBar.MENU_VIEW_TREE, true);
		setVisible(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, true);
		setVisible(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, true);
		setVisible(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, true);
		setVisible(ScheduleMainMenuBar.MENU_VIEW_TIME, true);
		setVisible(ScheduleMainMenuBar.MENU_VIEW_TABLE, true);

		setVisible(ScheduleMainMenuBar.MENU_HELP, true);
		setVisible(ScheduleMainMenuBar.MENU_HELP_ABOUT, true);

	}

	public DataSourceInterface getDataSource(SessionInterface si) {
		String connection = Environment.getConnectionType();
		if (connection.equals("RISD"))
			return new RISDSurveyDataSource(si);
		else if (connection.equals("Empty"))
			return new EmptySurveyDataSource(si);
		return null;
	}

	public List getTests() {
		return this.tests;
	}

	/**
	 * @return Returns the treeModel.
	 */
	public ObjectResourceTreeModel getTreeModel() {
		return this.treeModel;
	}

	/**
	 * @return Returns the unsavedTests.
	 */
	public List getUnsavedTests() {
		return this.unsavedTests;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		int id = ae.getID();
		Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		//		if (commandName.equals(TestUpdateEvent.TYPE)) {
		//			TestUpdateEvent tue = (TestUpdateEvent) ae;
		//			this.receivedTest = tue.test;
		//
		//			if (tue.testSelected) {
		//				//this.getContentPane().removeAll();
		//				//this.getContentPane().add(panel, BorderLayout.CENTER);
		//				//updateUI();
		//
		//				TestRequest treq = (TestRequest) Pool.get(TestRequest.TYPE,
		// this.receivedTest.getRequestId());
		//				if (treq == null)
		//					System.out.println("TestRequestFrame.operationPerformed() treq not
		// found id " //$NON-NLS-1$
		//							+ this.receivedTest.getRequestId());
		//				else {
		//					System.out.println(this.getClass().getName() + " > treq.id: " +
		// treq.getId()); //$NON-NLS-1$
		//					//panel.setTestRequest(treq);
		//					//panel.setTest(receivedTest);
		//				}
		//
		//			}
		//		}
		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			this.receivedTest = tue.test;
			boolean found = false;
			if (this.tests != null) {
				if (this.tests.contains(this.receivedTest))
					found = true;
			}
			if (!found) {
				if (this.unsavedTests != null) {
					if (this.unsavedTests.contains(this.receivedTest))
						found = true;
				}
			}

			if (!found) {
				if (this.receivedTest.isChanged()) {
					if (this.unsavedTests == null)
						this.unsavedTests = new ArrayList();
					this.unsavedTests.add(this.receivedTest);
					if (this.allUnsavedTests == null)
						this.allUnsavedTests = new ArrayList();
					this.allUnsavedTests.add(this.receivedTest);
				} else {
					this.tests.add(this.receivedTest);
					this.allTests.add(this.receivedTest);
				}
			}
		} else if (commandName.equalsIgnoreCase(COMMAND_CREATE_TEST)) {
			// creating test
			//if (flag == 0) {
			this.flag = FLAG_CREATE;
			if (this.receiveData == null)
				this.receiveData = new HashMap();
			else
				this.receiveData.clear();
			this.receiveTreeElements = null;

			this.dispatcher.notify(new OperationEvent("", 0, //$NON-NLS-1$
														COMMAND_DATA_REQUEST));
		} else if (commandName.equals(COMMAND_COMMIT_CHANGES)) {
			commitChanges();
		} else if (commandName.equalsIgnoreCase(COMMAND_APPLY_TEST)) {
			// apply test
			//if (flag == 0) {
			this.flag = FLAG_APPLY;
			this.receiveTreeElements = null;
			if (this.receiveData == null)
				this.receiveData = new HashMap();
			else
				this.receiveData.clear();
			//receiveDataCount = 0;
			this.dispatcher.notify(new OperationEvent("", 0, //$NON-NLS-1$
														COMMAND_DATA_REQUEST));
			//}
		} else if (commandName.equalsIgnoreCase(COMMAND_SEND_DATA)) {
			//receiveDataCount++;
			if (id == DATA_ID_PARAMETERS) {
				//System.out.println("parameters id have got"); //$NON-NLS-1$
			} else if (id == DATA_ID_ELEMENTS) {
				System.out.println("elements id have got"); //$NON-NLS-1$
				this.receiveTreeElements = (HashMap) obj;
			} else if (id == DATA_ID_TIMESTAMP) {
				//System.out.println("timestamp id have hot"); //$NON-NLS-1$
			} else if (id == DATA_ID_PARAMETERS_ANALYSIS) {
				this.receiveData.put(AnalysisType.typ, obj);
			} else if (id == DATA_ID_PARAMETERS_EVALUATION) {
				this.receiveData.put(EvaluationType.TYPE, obj);
			}
			if (obj instanceof AnalysisType) {
				System.out.println("AnalysisType instanceof have got"); //$NON-NLS-1$
				this.receiveData.put(AnalysisType.typ, obj);
			} else if (obj instanceof EvaluationType) {
				System.out.println("EvaluationType instanceof have got"); //$NON-NLS-1$
				this.receiveData.put(EvaluationType.TYPE, obj);
			} else if (obj instanceof TestArgumentSet) {
				System.out.println("TestArgumentSet instanceof have got"); //$NON-NLS-1$
				this.receiveData.put(TestArgumentSet.TYPE, obj);
				//receiveTestArgumentSet = (TestArgumentSet) obj;
			} else if (obj instanceof TestSetup) {
				System.out.println("TestSetup instanceof have got"); //$NON-NLS-1$
				//receiveTestSetup = (TestSetup) obj;
				System.out.println(((TestSetup) obj).getId());
				this.receiveData.put(TestSetup.TYPE, obj);
			} else if (obj instanceof TimeStamp) {
				System.out.println("timestamp instanceof have got"); //$NON-NLS-1$
				//receiveTimeStamp = (TimeStamp) obj;
				this.receiveData.put(TimeStamp.TYPE, obj);
			} else if (obj instanceof TestReturnType) {
				System.out.println("TestReturnType instanceof have got");
				this.returnType = (TestReturnType) obj;
			} else if (obj instanceof TestRequest) {
				System.out.println("TestRequest instanceof have got");
				this.receiveData.put(TestRequest.TYPE, obj);
			}
			//System.out.println("receiveDataCount:" + receiveDataCount);
			// //$NON-NLS-1$

			//			System.err.println("this.receiveData.get(TestRequest.TYPE):"
			//					+ (this.receiveData.get(TestRequest.TYPE) != null));
			//			System.err.println("this.returnType != null):" + (this.returnType
			// != null));
			//			System.err.println("this.receiveData.get(TestSetup.typ):" +
			// (this.receiveData.get(TestSetup.typ) != null));
			//			System.err.println("this.receiveData.get(AnalysisType.typ):"
			//					+ (this.receiveData.get(AnalysisType.typ) != null));
			//			System.err.println("this.receiveData.get(EvaluationType.typ):"
			//					+ (this.receiveData.get(EvaluationType.typ) != null));
			//			System.err.println("this.receiveData.get(TestArgumentSet.typ):"
			//					+ (this.receiveData.get(TestArgumentSet.typ) != null));
			//			System.err.println("this.receiveTreeElements:" +
			// (this.receiveTreeElements != null));
			//			System.err.println("this.receiveData.get(TimeStamp.TYPE):" +
			// (this.receiveData.get(TimeStamp.TYPE) != null));
			//			System.err.println("this.receiveData.get(TestRequest.TYPE):"
			//					+ (this.receiveData.get(TestRequest.TYPE) != null));

			if ((this.receiveData.get(TestRequest.TYPE) != null)
					&& (this.returnType != null)
					&& (((this.receiveData.get(TestSetup.TYPE) != null)
							&& (this.receiveData.get(AnalysisType.typ) != null) && (this.receiveData
							.get(EvaluationType.TYPE) != null)) || (this.receiveData.get(TestArgumentSet.TYPE) != null)

					) && (this.receiveTreeElements != null) && (this.receiveData.get(TimeStamp.TYPE) != null)
					&& (this.receiveData.get(TestRequest.TYPE) != null)) {
				if ((this.flag & FLAG_CREATE) != 0) {
					System.out.println("createTest"); //$NON-NLS-1$
					this.receivedTest = null;
					createTest();
				} else if ((this.flag & FLAG_APPLY) != 0) {
					System.out.println("applyTest"); //$NON-NLS-1$
					createTest();
				}
				this.flag = 0;
			}
		} else if (commandName.equals(COMMAND_CLEAN)) {
			if (!obj.equals(this)) {
				if (this.tests != null)
					this.tests.clear();
				if (this.unsavedTests != null)
					this.unsavedTests.clear();
				if (this.allTests != null)
					this.allTests.clear();
				if (this.allUnsavedTests != null)
					this.allUnsavedTests.clear();
			}

		} else if (commandName.equals(COMMAND_REMOVE_TEST)) {
			Test test = (Test) ae.getSource();
			if (this.tests != null)
				this.tests.remove(test);
			if (this.unsavedTests != null)
				this.unsavedTests.remove(test);
			if (this.allTests != null)
				this.allTests.remove(test);
			if (this.allUnsavedTests != null)
				this.allUnsavedTests.remove(test);

		}
	}

	//	/**
	//	 * @param tests
	//	 * The tests to set.
	//	 */
	//	public void setTests(List tests) {
	//		this.tests = tests;
	//	}

	/**
	 * @param treeModel
	 *            The treeModel to set.
	 */
	public void setTreeModel(ObjectResourceTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	//	/**
	//	 * @param unsavedTests
	//	 * The unsavedTests to set.
	//	 */
	//	public void setUnsavedTests(List unsavedTests) {
	//		this.unsavedTests = unsavedTests;
	//	}

	public void updateTests(long startTime, long endTime) {
		//Environment.log(Environment.LOG_LEVEL_INFO, "updateTests",
		// getClass().getName()); //$NON-NLS-1$
		//		this.setCursor(UIStorage.WAIT_CURSOR);
		this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
				.getString("Updating_tests_from_BD"))); //$NON-NLS-1$
		DataSourceInterface dsi = this.aContext.getDataSourceInterface();
		if (dsi == null)
			return;

		//		dsi.GetTests(scaleStart.getTime(), scaleEnd.getTime());
		SurveyDataSourceImage sDataSrcImg = new SurveyDataSourceImage(dsi);
		String[] ids = sDataSrcImg.GetTests(startTime, endTime);

		//выбираем необходимые тесты из пула
		Hashtable hash = new Hashtable();
		for (int i = 0; i < ids.length; i++) {
			//Environment.log(Environment.LOG_LEVEL_INFO, "get test#" +
			// ids[i]); //$NON-NLS-1$
			Test test = (Test) Pool.get(Test.TYPE, ids[i]);
			if (test.getAnalysisId().length() > 0) //$NON-NLS-1$
				dsi.GetAnalysis(test.getAnalysisId());
			if (test.getEvaluationId().length() > 0) //$NON-NLS-1$
				dsi.GetEvaluation(test.getEvaluationId());

			if (test != null) {
				hash.put(test.getId(), test);
			} else {
				Environment.log(Environment.LOG_LEVEL_WARNING, "test " + ids[i] + " is null"); //$NON-NLS-1$ //$NON-NLS-2$
			}

		}
		ObjectResourceDomainFilter ordf = new ObjectResourceDomainFilter(this.aContext.getSessionInterface()
				.getDomainId());

		//DataSet testSet = new DataSet(hash);

		//testSet = ordf.filter(testSet);
		ordf.filtrate(hash);

		if (this.allTests == null)
			this.allTests = new ArrayList();
		else
			this.allTests.clear();

		for (Iterator it = hash.keySet().iterator(); it.hasNext();) {
			Test test = (Test) hash.get(it.next());
			//System.out.println("loaded ordf tests:" + test.getId());
			this.allTests.add(test);
		}

		//testSet = this.filter.filter(testSet);
		this.filter.filtrate(hash);

		//подгружаем тестреквесты и недостающие тесты
		HashSet treqs = new HashSet();
		for (Iterator it = hash.keySet().iterator(); it.hasNext();) {
			Test test = (Test) hash.get(it.next());
			treqs.add(test.getRequestId());
		}
		dsi.GetRequests();
		HashSet loadTests = new HashSet();
		for (Iterator it = treqs.iterator(); it.hasNext();) {
			TestRequest treq = (TestRequest) Pool.get(TestRequest.TYPE, (String) it.next());
			if (treq != null) {
				java.util.List testIds = treq.getTestIds();
				for (Iterator it2 = testIds.iterator(); it2.hasNext();) {
					String testId = (String) it2.next();
					//Environment.log(Environment.LOG_LEVEL_INFO, "test_id:" +
					// testId); //$NON-NLS-1$
					if (hash.get(testId) == null)
						loadTests.add(testId);
				}
			}
		}
		if (!loadTests.isEmpty())
			new SurveyDataSourceImage(dsi).GetTests((String[]) loadTests.toArray(new String[loadTests.size()]));

		if (this.tests == null)
			this.tests = new ArrayList();
		else
			this.tests.clear();

		for (Iterator it = hash.keySet().iterator(); it.hasNext();) {
			Test test = (Test) hash.get(it.next());
			this.tests.add(test);
		}

		{
			List alarmsIds = null;
			//List testSetupIds = null;
			List testArgumentSetIds = null;
			for (Iterator it = this.tests.iterator(); it.hasNext();) {
				Test test = (Test) it.next();

				TestSetup testSetup = (TestSetup) Pool.get(TestSetup.TYPE, test.getTestSetupId());
				if (testSetup == null) {
					//if (testSetupIds==null)
					//	testSetupIds=new ArrayList();
					//testSetupIds.add(test.getTestSetupId());
					String testSetupId = test.getTestSetupId();
					if ((testSetupId != null) && (testSetupId.length() > 0)) {
						Environment.log(Environment.LOG_LEVEL_INFO, "loading test setup:" + test.getTestSetupId()
								+ " for test:" + test.getId());
						dsi.loadTestSetup(testSetupId);
					}
				}

				TestArgumentSet testArgumentSet = (TestArgumentSet) Pool.get(TestArgumentSet.TYPE, test
						.getTestArgumentSetId());
				if (testArgumentSet == null) {
					if (testArgumentSetIds == null)
						testArgumentSetIds = new ArrayList();
					testArgumentSetIds.add(test.getTestArgumentSetId());
				}

				ElementaryTestAlarm[] testAlarms = test.getElementaryTestAlarms();
				if (testAlarms.length != 0) {
					//System.out.println("testAlarms.length:"+testAlarms.length);
					for (int i = 0; i < testAlarms.length; i++) {
						Alarm alarm = (Alarm) Pool.get(Alarm.typ, testAlarms[i].alarm_id);
						if (alarm == null) {
							if (alarmsIds == null)
								alarmsIds = new ArrayList();
							alarmsIds.add(testAlarms[i].alarm_id);
						}

					}
				}
			}
			if (alarmsIds != null)
				dsi.GetAlarms((String[]) alarmsIds.toArray(new String[alarmsIds.size()]));
			if (testArgumentSetIds != null)
				dsi.LoadTestArgumentSets((String[]) testArgumentSetIds.toArray(new String[testArgumentSetIds.size()]));
			//dsi.loadTestSetup((String[]) testArgumentSetIds.toArray(new
			// String[testArgumentSetIds.size()]));
			//dsi.LoadT
		}

		this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
				.getString("Updating_tests_from_BD_finished"))); //$NON-NLS-1$
		this.dispatcher.notify(new OperationEvent(this.tests, 0, COMMAND_NAME_ALL_TESTS));
	}

	private void commitChanges() {
		DataSourceInterface dataSource = this.aContext.getDataSourceInterface();
		Hashtable unsavedTestArgumentSet = Pool.getChangedHash(TestArgumentSet.TYPE);
		Hashtable unsavedAnalysis = Pool.getChangedHash(Analysis.TYPE);
		Hashtable unsavedEvaluation = Pool.getChangedHash(Evaluation.TYPE);
		Hashtable unsavedTestRequest = Pool.getChangedHash(TestRequest.TYPE);
		Hashtable unsavedTest = Pool.getChangedHash(Test.TYPE);

		// remove tests
		if (unsavedTest != null) {
			java.util.List deleteTests = new ArrayList();
			for (Iterator it = unsavedTest.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				Test test = (Test) unsavedTest.get(key);
				if (test.getDeleted() != 0) {
					String testId = test.getId();
					if (test.getStatus().value() != TestStatus._TEST_STATUS_SCHEDULED)
						deleteTests.add(testId);
					TestRequest treq = (TestRequest) Pool.get(TestRequest.TYPE, test.getRequestId());
					//System.out.println("removing test:" + testId + " from
					// testRequest:" + treq.getId());
					treq.removeTest(test);
					Pool.remove(Test.TYPE, testId);
					unsavedTest.remove(key);
				}

			}
			String[] deleteTestIds = (String[]) deleteTests.toArray(new String[deleteTests.size()]);
			dataSource.RemoveTests(deleteTestIds);

		}

		for (int i = 0; i < 5; i++) {
			Hashtable table;
			switch (i) {
				case 0:
					table = unsavedTestArgumentSet;
					break;
				case 1:
					table = unsavedAnalysis;
					break;
				case 2:
					table = unsavedEvaluation;
					break;
				case 3:
					table = unsavedTestRequest;
					break;
				case 4:
					table = unsavedTest;
					break;
				default:
					table = null;
					break;
			}
			if (table != null) {
				Set keys = table.keySet();
				for (Iterator it = keys.iterator(); it.hasNext();) {
					String key = (String) it.next();
					ObjectResource obj = (ObjectResource) table.get(key);
					if (obj instanceof TestArgumentSet) {
						TestArgumentSet tas = (TestArgumentSet) obj;
						Environment.log(Environment.LOG_LEVEL_INFO, "saveTestArgumentSet(" + tas.getId() + ")");
						if (CREATE_ALLOW) {
							dataSource.saveTestArgumentSet(tas.getId());
							tas.setChanged(false);
						}
					} else if (obj instanceof Analysis) {
						Analysis an = (Analysis) obj;
						Environment.log(Environment.LOG_LEVEL_INFO, "createAnalysis(" + an.getId() + ");");
						if (CREATE_ALLOW) {
							dataSource.createAnalysis(an.getId());
							an.setChanged(false);
						}
					} else if (obj instanceof Evaluation) {
						Evaluation ev = (Evaluation) obj;
						Environment.log(Environment.LOG_LEVEL_INFO, "createEvaluation(" + ev.getId() + ")");
						if (CREATE_ALLOW) {
							dataSource.createEvaluation(ev.getId());
							ev.setChanged(false);
						}
					} else if (obj instanceof TestRequest) {
						TestRequest testRequest = (TestRequest) obj;
						//						String[] ids = new
						// String[testRequest.test_ids.size()];
						//						for (int j = 0; j < ids.length; j++) {
						//							ids[j] = (String) testRequest.test_ids.get(j);
						//							System.out.println("ids[" + j + "]=" + ids[j]);
						//						}
						java.util.List list = testRequest.getTestIds();
						String[] ids = new String[list.size()];
						int j = 0;
						//System.out.println("list.size():" + list.size());
						for (Iterator it2 = list.iterator(); it2.hasNext();) {
							ids[j++] = (String) it2.next();
							Environment.log(Environment.LOG_LEVEL_INFO, "ids[" + (j - 1) + "]=" + ids[j - 1]);
						}
						//System.out.println("j:" + j);
						Environment.log(Environment.LOG_LEVEL_INFO, "RequestTest(" + testRequest.getId() + ")");
						if (CREATE_ALLOW) {
							testRequest.updateLocalFromTransferable();
							dataSource.RequestTest(testRequest.getId(), ids);
							testRequest.setChanged(false);
						}
					} else if (obj instanceof Test) {
						// nothing ???
						Test test = (Test) obj;
						Environment.log(Environment.LOG_LEVEL_INFO, "test:" + test.getId());
						test.setChanged(false);
					}
					Environment.log(Environment.LOG_LEVEL_INFO, "#" + i + " " + key + " " + obj.getClass().getName());
				}
			}
		}
		this.aContext.getDispatcher().notify(new OperationEvent("", 0, SchedulerModel.COMMAND_TEST_SAVED_OK));
	}

	private void createTest() {
		DataSourceInterface dsi = this.aContext.getDataSourceInterface();
		Test test = this.receivedTest;
		if (test == null) {
			test = new Test(dsi.GetUId(Test.TYPE)); //$NON-NLS-1$
			test.setStatus(TestStatus.TEST_STATUS_SCHEDULED);
			Pool.put(Test.TYPE, test.getId(), test);
			TestRequest testRequest = (TestRequest) this.receiveData.get(TestRequest.TYPE);
			testRequest.addTest(test);
			test.setRequestId(testRequest.getId());
		}

		System.out.println("createTest():" + test.getId());

		TestType testType = (TestType) this.receiveTreeElements.get(TestType.typ);
		KIS kis = (KIS) this.receiveTreeElements.get(KIS.typ);
		MonitoredElement me = (MonitoredElement) this.receiveTreeElements.get(MonitoredElement.typ);
		test.setTestTypeId(testType.getId());
		test.setKisId(kis.id);
		test.setMonitoredElementId(me.id);

		test.setReturnType(this.returnType);
		test.setUserId(this.aContext.getSessionInterface().getUserId());
		test.setTimeStamp((TimeStamp) this.receiveData.get(TimeStamp.TYPE));
		{

			TestArgumentSet testArgumentSet = (TestArgumentSet) this.receiveData.get(TestArgumentSet.TYPE);
			if (testArgumentSet != null) {
				testArgumentSet.setTestTypeId(testType.getId());
				testArgumentSet.setId(dsi.GetUId(TestArgumentSet.TYPE));
				testArgumentSet.setChanged(true);
				Pool.put(TestArgumentSet.TYPE, testArgumentSet.getId(), testArgumentSet);
				testArgumentSet.setName(testArgumentSet.getId());
				test.setTestArgumentSet(testArgumentSet);
				test.setTestArgumentSetId(testArgumentSet.getId());
			}
		}

		test.setTestSetup((TestSetup) this.receiveData.get(TestSetup.TYPE));
		if (test.getTestSetup() != null)
			test.setTestSetupId(test.getTestSetup().getId());

		{
			Object obj = this.receiveData.get(AnalysisType.typ);
			if (obj == null || obj instanceof String)
				test.setAnalysisId(""); //$NON-NLS-1$
			else {
				AnalysisType analysisType = (AnalysisType) obj;
				Analysis analysis = new Analysis(dsi.GetUId(Analysis.TYPE));
				analysis.setMonitoredElementId(me.getId());
				analysis.setTypeId(analysisType.getId());
				analysis.setCriteriaSetId(test.getTestSetup().getCriteriaSetId());
				test.setAnalysisId(analysis.getId());
				analysis.setChanged(true);
				Pool.put(Analysis.TYPE, analysis.getId(), analysis);
				//System.err.println("test.analysis_id:" + test.analysis_id);
			}

			obj = this.receiveData.get(EvaluationType.TYPE);
			//EvaluationType evaluationType = (EvaluationType)
			// this.receiveData.get(EvaluationType.typ);
			if (obj == null || obj instanceof String)
				test.setEvaluationId(""); //$NON-NLS-1$
			else {
				EvaluationType evaluationType = (EvaluationType) obj;
				Evaluation evaluation = new Evaluation(dsi.GetUId(Evaluation.TYPE));
				evaluation.setMonitoredElementId(me.getId());
				evaluation.setTypeId(evaluationType.getId());
				evaluation.setThresholdSetId(test.getTestSetup().getThresholdSetId());
				evaluation.setEthalonId(test.getTestSetup().getEthalonId());
				test.setEvaluationId(evaluation.getId());
				evaluation.setChanged(true);
				Pool.put(Evaluation.TYPE, evaluation.getId(), evaluation);
				//System.err.println("test.evaluation_id:" +
				// test.evaluation_id);
			}

		}

		//		test.setChanged(true);

		test.setName(ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(test.getStartTime())));
		//testRequest.setName(test.getName());
		this.dispatcher.notify(new TestUpdateEvent(this, test, TestUpdateEvent.TEST_SELECTED_EVENT));
	}

	/**
	 * @return Returns the filter.
	 */
	public ObjectResourceFilter getFilter() {
		return this.filter;
	}

	/**
	 * @param filter
	 *            The filter to set.
	 */
	public void setFilter(ObjectResourceFilter filter) {
		this.filter = filter;
		//		Vector v = this.filter.getCriteria();
		//		for(Iterator it=v.iterator();it.hasNext();){
		//			FilterExpressionInterface fei = (FilterExpressionInterface)it.next();
		//			System.out.println("name:"+fei.getName()+"\tid:"+fei.getId());
		//			Vector v2= fei.getVec();
		//			for(Iterator it2=v2.iterator();it2.hasNext();){
		//				Object obj = it2.next();
		//				System.out.println("'"+obj+"'");
		//			}
		//		}
		if (this.allTests != null && !this.allTests.isEmpty()) {
			this.dispatcher.notify(new OperationEvent(this, 0, SchedulerModel.COMMAND_CLEAN));
			for (Iterator it = this.tests.iterator(); it.hasNext();) {
				Object obj = it.next();
				if (!this.allTests.contains(obj))
					this.allTests.add(obj);
			}

			//			for (Iterator it = this.allTests.iterator(); it.hasNext();) {
			//				Test test = (Test) it.next();
			//								System.out.println("allTests > filter:" + test.getId());
			//			}

			if (this.unsavedTests != null) {
				for (Iterator it = this.unsavedTests.iterator(); it.hasNext();) {
					Object obj = it.next();
					if (!this.allUnsavedTests.contains(obj))
						this.allUnsavedTests.add(obj);
				}
			}

			//			DataSet testSet = new DataSet(this.allTests);
			//			testSet = this.filter.filter(testSet);
			//			this.tests.clear();
			//			for (Iterator it = testSet.iterator(); it.hasNext();) {
			//				Test test = (Test) it.next();
			//// System.out.println("filtered test:" + test.getId());
			//				this.tests.add(test);
			//			}
			this.filter.filtrate(this.allTests, this.tests);

			if (this.allUnsavedTests != null) {
				//				testSet = new DataSet(this.allUnsavedTests);
				//				testSet = this.filter.filter(testSet);
				//				this.unsavedTests.clear();
				//				for (Iterator it = testSet.iterator(); it.hasNext();) {
				//					Test test = (Test) it.next();
				//					this.unsavedTests.add(test);
				//				}
				this.filter.filtrate(this.allUnsavedTests, this.unsavedTests);
			}
			//						for (Iterator it = this.tests.iterator(); it.hasNext();) {
			//							Test test = (Test) it.next();
			//							System.out.println("filtered test2send:" + test.getId());
			//						}
			this.dispatcher.notify(new OperationEvent(this.tests, 0, COMMAND_NAME_ALL_TESTS));
		}
	}
}