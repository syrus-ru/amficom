/*
 * SchedulerModel.java
 * Created on 11.06.2004 10:42:43
 * 
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Event.TestUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.RISDSurveyDataSource;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.TemporalCondition;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;

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

	/**
	 * TODO remove when will enable again
	 */
	//private ObjectResourceFilter filter = new TestFilter();
	private int						flag							= 0;
	private HashMap					receiveData;

	//private int receiveDataCount = 0;
	private Test					receivedTest;
	private HashMap					receiveTreeElements;
	private TestReturnType			returnType;
	private TestTemporalStamps		receiveTestTimeStamps;

	private List					tests;
	private List					allTests;

	private ObjectResourceTreeModel	treeModel;
	private List					unsavedTests;
	private List					allUnsavedTests;

	private DomainCondition			domainCondition;

	private DataSourceInterface		dataSourceInterface;

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

		add(ScheduleMainMenuBar.MENU_REPORT);
		add(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT);

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
		/**
		 * TODO remove when will enable again
		 */
		//		String connection = Environment.getConnectionType();
		//		if (connection.equals("RISD"))
		//			return new RISDSurveyDataSource(si);
		//		else if (connection.equals("Empty"))
		//			return new EmptySurveyDataSource(si);
		if (this.dataSourceInterface == null)
			this.dataSourceInterface = new RISDSurveyDataSource(si);
		return this.dataSourceInterface;
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
		//					System.out.println("TestRequestFrame.operationPerformed()
		// treq not
		// found id " //$NON-NLS-1$
		//							+ this.receivedTest.getRequestId());
		//				else {
		//					System.out.println(this.getClass().getName() + " > treq.id: "
		// +
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
			try {
				commitChanges();
			} catch (VersionCollisionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				//System.out.println("parameters id have got");
				// //$NON-NLS-1$
			} else if (id == DATA_ID_ELEMENTS) {
				System.out.println("elements id have got"); //$NON-NLS-1$
				this.receiveTreeElements = (HashMap) obj;
			} else if (id == DATA_ID_TIMESTAMP) {
				//System.out.println("timestamp id have hot");
				// //$NON-NLS-1$
			} else if (id == DATA_ID_PARAMETERS_ANALYSIS) {
				this.receiveData.put(ObjectEntities.SET_ENTITY, obj);
			} else if (id == DATA_ID_PARAMETERS_EVALUATION) {
				//this.receiveData.put(ObjectEntities.EVALUATION_ENTITY,
				// obj);
			}

			if (obj instanceof AnalysisType) {
				System.out.println("AnalysisType instanceof have got"); //$NON-NLS-1$
				this.receiveData.put(ObjectEntities.ANALYSISTYPE_ENTITY, obj);
			} else if (obj instanceof EvaluationType) {
				System.out.println("EvaluationType instanceof have got"); //$NON-NLS-1$
				this.receiveData.put(ObjectEntities.EVALUATIONTYPE_ENTITY, obj);
			} else if (obj instanceof Set) {
				System.out.println("Set instanceof have got"); //$NON-NLS-1$
				this.receiveData.put(ObjectEntities.SET_ENTITY, obj);
			} else if (obj instanceof List) {
				System.out.println("MeasurementSetup instanceof have got"); //$NON-NLS-1$				
				boolean isOnlySetups = true;
				List list = (List) obj;
				for (Iterator it = list.iterator(); it.hasNext();) {
					Object element = it.next();
					if (!(element instanceof MeasurementSetup)) {
						isOnlySetups = false;
						break;
					}
				}

				if (isOnlySetups)
					this.receiveData.put(ObjectEntities.MS_ENTITY, list);
				else {
					List measurementSetupIds = new LinkedList();
					for (Iterator it = list.iterator(); it.hasNext();) {
						Object element = it.next();
						if (element instanceof MeasurementSetup)
							measurementSetupIds.add(element);
					}
					this.receiveData.put(ObjectEntities.MS_ENTITY, measurementSetupIds);
				}
			} else if (obj instanceof TestTemporalStamps) {
				System.out.println("TestTemporalStamps instanceof have got"); //$NON-NLS-1$
				this.receiveTestTimeStamps = (TestTemporalStamps) obj;
				this.receiveData.put(ObjectEntities.TEMPORALPATTERN_ENTITY, obj);
			} else if (obj instanceof TestReturnType){
				this.returnType = (TestReturnType)obj;
			}
			
			if ((this.returnType != null)
					&& ((this.receiveData.get(ObjectEntities.SET_ENTITY) != null) || (((this.receiveData
							.get(ObjectEntities.MS_ENTITY) != null)
							&& (this.receiveData.get(ObjectEntities.ANALYSISTYPE_ENTITY) != null) && (this.receiveData
							.get(ObjectEntities.EVALUATIONTYPE_ENTITY) != null)))

					) && (this.receiveTreeElements != null)
					&& (this.receiveData.get(ObjectEntities.TEMPORALPATTERN_ENTITY) != null)) {
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
		try {
			//Environment.log(Environment.LOG_LEVEL_INFO, "updateTests",
			// getClass().getName()); //$NON-NLS-1$
			//		this.setCursor(UIStorage.WAIT_CURSOR);
			this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
					.getString("Updating_tests_from_BD"))); //$NON-NLS-1$
			DataSourceInterface dsi = this.aContext.getDataSource();
			if (dsi == null)
				return;

			if (this.allTests == null)
				this.allTests = new ArrayList();
			else
				this.allTests.clear();

			RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();

			this.allTests = MeasurementStorableObjectPool
					.getStorableObjectsByCondition(new TemporalCondition((Domain) ConfigurationStorableObjectPool
							.getStorableObject(sessionInterface.getDomainIdentifier(), true), new Date(startTime),
																			new Date(endTime)), true);

			if (this.tests == null)
				this.tests = new LinkedList();
			else
				this.tests.clear();
			/**
			 * TODO remove when will enable again
			 */
			// this.filter.filtrate(this.allTests, this.tests);
			this.tests.addAll(this.allTests);
			{
				List alarmsIds = null;
				List testArgumentSetIds = null;
				if (alarmsIds != null)
					dsi.GetAlarms((String[]) alarmsIds.toArray(new String[alarmsIds.size()]));
				if (testArgumentSetIds != null)
					dsi.LoadTestArgumentSets((String[]) testArgumentSetIds
							.toArray(new String[testArgumentSetIds.size()]));
			}

			this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
					.getString("Updating_tests_from_BD_finished"))); //$NON-NLS-1$
			this.dispatcher.notify(new OperationEvent(this.tests, 0, COMMAND_NAME_ALL_TESTS));
		} catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	private void commitChanges() throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {

		/**
		 * FIXME remove deleted tests
		 */
		// remove tests
		//		if (unsavedTest != null) {
		//			java.util.List deleteTests = new ArrayList();
		//			for (Iterator it = unsavedTest.keySet().iterator(); it.hasNext();) {
		//				Object key = it.next();
		//				Test test = (Test) unsavedTest.get(key);
		//				if (test.getDeleted() != 0) {
		//					Identifier testId = test.getId();
		//					if (test.getStatus().value() != TestStatus._TEST_STATUS_SCHEDULED)
		//						deleteTests.add(testId);
		//					TestRequest treq = (TestRequest) Pool
		//							.get(TestRequest.TYPE, test.getRequestId());
		//					//System.out.println("removing test:" +
		//					// testId + " from
		//					// testRequest:" + treq.getId());
		//					treq.removeTest(test);
		//					Pool.remove(Test.TYPE, testId);
		//					it.remove();
		//				}
		//
		//			}
		//			String[] deleteTestIds = (String[]) deleteTests.toArray(new
		// String[deleteTests.size()]);
		//			this.dataSource.RemoveTests(deleteTestIds);
		//
		//		}
		MeasurementStorableObjectPool.flush(true);
		this.aContext.getDispatcher().notify(new OperationEvent("", 0, SchedulerModel.COMMAND_TEST_SAVED_OK));
	}

	private void createTest() {
		Test test = this.receivedTest;

		MeasurementType measurementType = (MeasurementType) this.receiveTreeElements
				.get(ObjectEntities.MEASUREMENTTYPE_ENTITY);

		KIS kis = (KIS) this.receiveTreeElements.get(ObjectEntities.KIS_ENTITY);
		MonitoredElement me = (MonitoredElement) this.receiveTreeElements.get(ObjectEntities.ME_ENTITY);
		EvaluationType evaluationType = (EvaluationType) this.receiveData.get(ObjectEntities.EVALUATIONTYPE_ENTITY);
		AnalysisType analysisType = (AnalysisType) this.receiveData.get(ObjectEntities.ANALYSISTYPE_ENTITY);
		List measurementSetupIds = (List) this.receiveData.get(ObjectEntities.MS_ENTITY);
		if (measurementSetupIds == null) {
			Set set = (Set) this.receiveData.get(ObjectEntities.SET_ENTITY);
			RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();
			MeasurementSetup mSetup = MeasurementSetup.createInstance(IdentifierPool
					.generateId(ObjectEntities.MS_ENTITY_CODE), sessionInterface.getUserIdentifier(), set, null, null,
																		null, "created by Scheduler", 1000 * 60 * 10,
																		Collections.singletonList(me.getId()));
			measurementSetupIds = Collections.singletonList(mSetup.getId());
			try {
				MeasurementStorableObjectPool.putStorableObject(mSetup);
			} catch (IllegalObjectEntityException e) {
				Log.debugException(e, Log.DEBUGLEVEL05);
			}
		}

		//		test.setChanged(true);

		Identifier modifierId = ((RISDSessionInfo) this.aContext.getSessionInterface()).getUserIdentifier();

		Date startTime = this.receiveTestTimeStamps.getStartTime();
		Date endTime = this.receiveTestTimeStamps.getEndTime();
		TestTemporalType temporalType = this.receiveTestTimeStamps.getTestTemporalType();
		TemporalPattern temporalPattern = this.receiveTestTimeStamps.getTemporalPattern();

		if (test == null) {
			Identifier id = IdentifierPool.generateId(ObjectEntities.TEST_ENTITY_CODE);
			test = Test.createInstance(id, modifierId, startTime, endTime, temporalPattern, temporalType,
										measurementType, analysisType, evaluationType, me, this.returnType,
										ConstStorage.SIMPLE_DATE_FORMAT.format(startTime), measurementSetupIds);

			try {
				MeasurementStorableObjectPool.putStorableObject(test);
			} catch (IllegalObjectEntityException e) {
				Log.errorException(e);
			}

		} else {
			test.setAttributes(test.getCreated(), new Date(System.currentTimeMillis()), test.getCreatorId(),
								modifierId, temporalType.value(), startTime, endTime, temporalPattern, measurementType,
								analysisType, evaluationType, test.getStatus().value(), me, this.returnType.value(),
								ConstStorage.SIMPLE_DATE_FORMAT.format(startTime));
		}

		//testRequest.setName(test.getName());
		try {
			MeasurementStorableObjectPool.putStorableObject(test);
		} catch (IllegalObjectEntityException e) {
			Log.debugException(e, Log.DEBUGLEVEL05);
		}
		
		try {
			StorableObject storableObject = MeasurementStorableObjectPool.getStorableObject(test.getId(), false);
			System.out.println(storableObject);
			System.out.println(storableObject.getId());
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.dispatcher.notify(new TestUpdateEvent(this, test, TestUpdateEvent.TEST_SELECTED_EVENT));
	}

	/**
	 * @return Returns the filter.
	 */
	//public ObjectResourceFilter getFilter() {
	/**
	 * TODO remove when will enable again
	 */
	//	return this.filter;
	//}
	/**
	 * TODO remove when will enable again
	 * 
	 * @throws CommunicationException
	 * @throws DatabaseException
	 */
	//	/**
	//	 * @param filter
	//	 * The filter to set.
	//	 */
	//	public void setFilter(ObjectResourceFilter filter) {
	//		this.filter = filter;
	//		// Vector v = this.filter.getCriteria();
	//		// for(Iterator it=v.iterator();it.hasNext();){
	//		// FilterExpressionInterface fei =
	//		// (FilterExpressionInterface)it.next();
	//		// System.out.println("name:"+fei.getName()+"\tid:"+fei.getId());
	//		// Vector v2= fei.getVec();
	//		// for(Iterator it2=v2.iterator();it2.hasNext();){
	//		// Object obj = it2.next();
	//		// System.out.println("'"+obj+"'");
	//		// }
	//		// }
	//		if (this.allTests != null && !this.allTests.isEmpty()) {
	//			this.dispatcher.notify(new OperationEvent(this, 0,
	// SchedulerModel.COMMAND_CLEAN));
	//			for (Iterator it = this.tests.iterator(); it.hasNext();) {
	//				Object obj = it.next();
	//				if (!this.allTests.contains(obj))
	//					this.allTests.add(obj);
	//			}
	//
	//			// for (Iterator it = this.allTests.iterator();
	//			// it.hasNext();) {
	//			// Test test = (Test) it.next();
	//			// System.out.println("allTests > filter:" +
	//			// test.getId());
	//			// }
	//
	//			if (this.unsavedTests != null) {
	//				for (Iterator it = this.unsavedTests.iterator(); it.hasNext();) {
	//					Object obj = it.next();
	//					if (!this.allUnsavedTests.contains(obj))
	//						this.allUnsavedTests.add(obj);
	//				}
	//			}
	//
	//			// DataSet testSet = new DataSet(this.allTests);
	//			// testSet = this.filter.filter(testSet);
	//			// this.tests.clear();
	//			// for (Iterator it = testSet.iterator(); it.hasNext();)
	//			// {
	//			// Test test = (Test) it.next();
	//			//// System.out.println("filtered test:" +
	//			// test.getId());
	//			// this.tests.add(test);
	//			// }
	//			this.filter.filtrate(this.allTests, this.tests);
	//
	//			if (this.allUnsavedTests != null) {
	//				// testSet = new DataSet(this.allUnsavedTests);
	//				// testSet = this.filter.filter(testSet);
	//				// this.unsavedTests.clear();
	//				// for (Iterator it = testSet.iterator();
	//				// it.hasNext();) {
	//				// Test test = (Test) it.next();
	//				// this.unsavedTests.add(test);
	//				// }
	//				this.filter.filtrate(this.allUnsavedTests, this.unsavedTests);
	//			}
	//			// for (Iterator it = this.tests.iterator();
	//			// it.hasNext();) {
	//			// Test test = (Test) it.next();
	//			// System.out.println("filtered test2send:" +
	//			// test.getId());
	//			// }
	//			this.dispatcher.notify(new OperationEvent(this.tests, 0,
	// COMMAND_NAME_ALL_TESTS));
	//		}
	//	}
	public DomainCondition getDomainCondition(short entityCode) throws DatabaseException, CommunicationException {
		RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();
		Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(sessionInterface
				.getDomainIdentifier(), true);
		if (this.domainCondition == null)
			this.domainCondition = new DomainCondition(domain, entityCode);
		else {
			this.domainCondition.setDomain(domain);
			this.domainCondition.setEntityCode(new Short(entityCode));
		}
		return this.domainCondition;
	}

	public static void showErrorMessage(Component component, Exception exc) {
		JOptionPane.showMessageDialog(component, exc.getMessage(), LangModelSchedule.getString("Error"),
										JOptionPane.OK_OPTION);
	}
}