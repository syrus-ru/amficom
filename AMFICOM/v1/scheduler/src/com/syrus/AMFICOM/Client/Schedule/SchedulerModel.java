/*
 * SchedulerModel.java
 * Created on 11.06.2004 10:42:43
 * 
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.RISDDataSource;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.item.MeasurementTypeChildrenFactory;
import com.syrus.AMFICOM.Client.Schedule.item.MeasurementTypeItem;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;

/**
 * @author Vladimir Dolzhenko
 */
public class SchedulerModel extends ApplicationModel implements OperationListener {

	public static final String			COMMAND_ADD_PARAM_PANEL			= "AddParamPanel";			//$NON-NLS-1$

	public static final String			COMMAND_CHANGE_KIS				= "ChangeKIS";				//$NON-NLS-1$
	public static final String			COMMAND_CHANGE_ME_TYPE			= "ChangeMEType";			//$NON-NLS-1$
	public static final String			COMMAND_CHANGE_PARAM_PANEL		= "ChangeParamPanel";		//$NON-NLS-1$
	public static final String			COMMAND_CHANGE_STATUSBAR_STATE	= "ChangeStatusBarState";

	// //$NON-NLS-1$
	public static final String			COMMAND_CLEAN					= "Clean";

	// private static final boolean CREATE_ALLOW = true;

	private static final int			FLAG_APPLY						= 1 << 1;
	private static final int			FLAG_CREATE						= 1 << 2;
	private ApplicationContext			aContext;
	private Dispatcher					dispatcher;
	
	private int							flag							= 0;

	private ObjectResourceTreeModel		treeModel;
	private Collection					tests							= new LinkedList();
	private Test						selectedTest;

	private MeasurementTypeEditor		measurementTypeEditor;
	private KISEditor					kisEditor;
	private MonitoredElementEditor		monitoredElementEditor;
	private AnalysisTypeEditor			analysisTypeEditor;
	private EvaluationTypeEditor		evaluationTypeEditor;
	private SetEditor					setEditor;
	private MeasurementSetupEditor		measurementSetupEditor;
	private ReturnTypeEditor			returnTypeEditor;
	private TestTemporalStampsEditor	testTemporalStampsEditor;
	private ElementsViewer				elementsViewer;
	private TestsEditor[]				testsEditors					= new TestsEditor[0];
	private TestEditor[]				testEditors						= new TestEditor[0];

	private MeasurementType				measurementType					= null;
	private KIS							kis								= null;
	private MonitoredElement			monitoredElement				= null;
	private AnalysisType				analysisType					= null;
	private EvaluationType				evaluationType					= null;
	private Set							set								= null;
	private MeasurementSetup			measurementSetup				= null;
	private TestReturnType				returnType						= null;
	private TestTemporalStamps			testTimeStamps					= null;

	private DataSourceInterface			dataSourceInterface;

	public SchedulerModel(ApplicationContext aContext) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		this.dispatcher.register(this, COMMAND_CLEAN);

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
		// String connection = Environment.getConnectionType();
		// if (connection.equals("RISD"))
		// return new RISDSurveyDataSource(si);
		// else if (connection.equals("Empty"))
		// return new EmptySurveyDataSource(si);
		// if (this.dataSourceInterface == null)
		this.dataSourceInterface = new RISDDataSource(si);
		return this.dataSourceInterface;
		// return null;
	}

	/**
	 * @return saved tests
	 */
	public Collection getTests() {		
		return this.tests;
	}

	/**
	 * @return Returns the treeModel.
	 */
	public ObjectResourceTreeModel getTreeModel() {
		return this.treeModel;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(COMMAND_CLEAN)) {
			if (!obj.equals(this)) {
				if (this.tests != null)
					this.tests.clear();
			}

			try {
				this.refreshEditors();
			} catch (ApplicationException e) {
				showErrorMessage(Environment.getActiveWindow(), e);
			}
		}
	}

	public void removeTest(Test test) throws ApplicationException {
		this.tests.remove(test);
		this.selectedTest = null;
		this.refreshTests();
	}

	private void refreshEditors() throws ApplicationException {
		RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();

		Collection temporalPatterns = MeasurementStorableObjectPool.getStorableObjectsByCondition(
			new EquivalentCondition(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), true);
		this.testTemporalStampsEditor.setTemporalPatterns(temporalPatterns);

		{
			Collection measurementTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(
				new EquivalentCondition(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

			LinkedIdsCondition domainCondition = new LinkedIdsCondition(sessionInterface.getDomainIdentifier(),
																		ObjectEntities.ME_ENTITY_CODE);			

			Collection monitoredElements = ConfigurationStorableObjectPool.getStorableObjectsByCondition(
				domainCondition, true);

			Collection measurementTypeItems = new ArrayList(measurementTypes.size());

			for (Iterator iter = measurementTypes.iterator(); iter.hasNext();) {
				MeasurementType measurementType = (MeasurementType) iter.next();
				MeasurementTypeItem measurementTypeItem = new MeasurementTypeItem(measurementType.getId());
				measurementTypeItems.add(measurementTypeItem);
				measurementTypeItem.setChildrenFactory(new MeasurementTypeChildrenFactory(sessionInterface.getDomainIdentifier()));
			}
			
			this.elementsViewer.setElements(measurementTypeItems);

			if (!monitoredElements.isEmpty()) {
				LinkedIdsCondition linkedIdsCondition;
				{
					java.util.Set meIdList = new HashSet(monitoredElements.size());
					for (Iterator it = monitoredElements.iterator(); it.hasNext();) {
						MonitoredElement me = (MonitoredElement) it.next();
						meIdList.add(me.getId());
					}
					linkedIdsCondition = new LinkedIdsCondition(meIdList, ObjectEntities.MS_ENTITY_CODE);
				}
				Collection measurementSetups = MeasurementStorableObjectPool.getStorableObjectsByCondition(
					linkedIdsCondition, true);

				this.measurementSetupEditor.setMeasurementSetups(measurementSetups);
			}
		}

	}

	private void refreshTest() throws ApplicationException {
		if (this.selectedTest != null) {
			this.measurementTypeEditor.setMeasurementType((MeasurementType) MeasurementStorableObjectPool
					.getStorableObject(this.selectedTest.getMeasurementTypeId(), true));
			MonitoredElement monitoredElement = this.selectedTest.getMonitoredElement();
			MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(
				monitoredElement.getMeasurementPortId(), true);
			this.kisEditor.setKIS((KIS) ConfigurationStorableObjectPool.getStorableObject(measurementPort.getKISId(),
				true));
			this.monitoredElementEditor.setMonitoredElement(monitoredElement);
			this.analysisTypeEditor.setAnalysisType((AnalysisType) MeasurementStorableObjectPool.getStorableObject(
				this.selectedTest.getAnalysisTypeId(), true));
			this.evaluationTypeEditor.setEvaluationType((EvaluationType) MeasurementStorableObjectPool
					.getStorableObject(this.selectedTest.getEvaluationTypeId(), true));
			Collection measurementSetupIds = this.selectedTest.getMeasurementSetupIds();
			if (!measurementSetupIds.isEmpty()) {
				Identifier mainMeasurementSetupId = (Identifier) measurementSetupIds.iterator().next();
				MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool.getStorableObject(
					mainMeasurementSetupId, true);
				this.setEditor.setSet(measurementSetup.getParameterSet());
				this.measurementSetupEditor.setMeasurementSetup(measurementSetup);
			}

			this.returnTypeEditor.setReturnType(this.selectedTest.getReturnType());
			{
				TestTemporalStamps timeStamps = new TestTemporalStamps(this.selectedTest.getTemporalType(),
																		this.selectedTest.getStartTime(),
																		this.selectedTest.getEndTime(),
																		(TemporalPattern) MeasurementStorableObjectPool
																				.getStorableObject(this.selectedTest
																						.getTemporalPatternId(), true));
				this.testTemporalStampsEditor.setTestTemporalStamps(timeStamps);
			}

			for (int i = 0; i < this.testEditors.length; i++) {
				this.testEditors[i].updateTest();
			}
		}
	}

	private void refreshTests() throws ApplicationException {
		for (int i = 0; i < this.testsEditors.length; i++) {
			this.testsEditors[i].updateTests();
		}

		this.refreshTest();
	}

	/**
	 * @param treeModel
	 *            The treeModel to set.
	 */
	public void setTreeModel(ObjectResourceTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public void applyTest() throws ApplicationException {
		this.flag = FLAG_APPLY;
		this.startGetData();
	}

	public void createTest() throws ApplicationException {
		this.flag = FLAG_CREATE;
		this.startGetData();
	}

	private void startGetData() throws ApplicationException {
		this.measurementType = this.measurementTypeEditor.getMeasurementType();
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
			this.kis = this.kisEditor.getKIS();
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
			this.monitoredElement = this.monitoredElementEditor.getMonitoredElement();
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
			this.analysisType = this.analysisTypeEditor.getAnalysisType();
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
			this.evaluationType = this.evaluationTypeEditor.getEvaluationType();
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
			this.set = this.setEditor.getSet();
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
			this.measurementSetup = this.measurementSetupEditor.getMeasurementSetup();
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
			this.returnType = this.returnTypeEditor.getReturnType();
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
			this.testTimeStamps = this.testTemporalStampsEditor.getTestTemporalStamps();
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
			this.generateTest();
	}

	public void setBreakData() {
		this.flag = 0;
	}

	public void updateTests(long startTime,
							long endTime) throws ApplicationException {
		// Environment.log(Environment.LOG_LEVEL_INFO, "updateTests",
		// getClass().getName()); //$NON-NLS-1$
		// this.setCursor(UIStorage.WAIT_CURSOR);
		this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
				.getString("Updating tests from DB"))); //$NON-NLS-1$
		DataSourceInterface dsi = this.aContext.getDataSource();
		if (dsi == null)
			return;

		ConfigurationStorableObjectPool.refresh();
		MeasurementStorableObjectPool.refresh();

		Date startDate = new Date(startTime);
		Date endDate = new Date(endTime);

		TypicalCondition startTypicalCondition = new TypicalCondition(startDate, endDate,
																		OperationSort.OPERATION_IN_RANGE,
																		ObjectEntities.TEST_ENTITY_CODE,
																		TestWrapper.COLUMN_START_TIME);
		TypicalCondition endTypicalCondition = new TypicalCondition(startDate, endDate,
																	OperationSort.OPERATION_IN_RANGE,
																	ObjectEntities.TEST_ENTITY_CODE,
																	TestWrapper.COLUMN_END_TIME);
		TypicalCondition startTypicalCondition1 = new TypicalCondition(startDate, null,
																		OperationSort.OPERATION_LESS_EQUALS,
																		ObjectEntities.TEST_ENTITY_CODE,
																		TestWrapper.COLUMN_START_TIME);
		TypicalCondition endTypicalCondition2 = new TypicalCondition(endDate, null,
																		OperationSort.OPERATION_GREAT_EQUALS,
																		ObjectEntities.TEST_ENTITY_CODE,
																		TestWrapper.COLUMN_END_TIME);

		
		CompoundCondition compoundCondition1 = new CompoundCondition(startTypicalCondition, CompoundConditionSort.OR,
																	endTypicalCondition);
		
		CompoundCondition compoundCondition2 = new CompoundCondition(startTypicalCondition1, CompoundConditionSort.AND,
			endTypicalCondition2);

		CompoundCondition compoundCondition = new CompoundCondition(compoundCondition1, CompoundConditionSort.OR,
			compoundCondition2);
		
		this.tests = MeasurementStorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);

		for (Iterator it = this.tests.iterator(); it.hasNext();) {
			Test test = (Test) it.next();
			System.out.println(">"+test.getId());
		}
		
//		{
//			List alarmsIds = null;
//			List testArgumentSetIds = null;
//			if (alarmsIds != null)
//				dsi.GetAlarms((String[]) alarmsIds.toArray(new String[alarmsIds.size()]));
//			if (testArgumentSetIds != null)
//				dsi.LoadTestArgumentSets((String[]) testArgumentSetIds.toArray(new String[testArgumentSetIds.size()]));
//		}

		this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
				.getString("Updating_tests_from_BD_finished"))); //$NON-NLS-1$
		this.refreshTests();
	}

	public Test getSelectedTest() {
		return this.selectedTest;
	}

	public void setSelectedTest(Test selectedTest) throws ApplicationException {
		if (this.selectedTest == null || selectedTest == null || !this.selectedTest.getId().equals(selectedTest.getId())) {
			this.selectedTest = selectedTest;
			this.refreshTest();
		}
	}

	public void commitChanges() throws ApplicationException {
		MeasurementStorableObjectPool.flush(true);
	}

	private void generateTest() throws ApplicationException {
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {

			Test test = (this.flag == FLAG_APPLY) ? this.selectedTest : null;

			java.util.Set measurementSetupIds;
			if (this.measurementSetup == null) {
				if (this.set == null)
					return;
				RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();
				try {
					this.measurementSetup = MeasurementSetup.createInstance(sessionInterface.getUserIdentifier(),
						this.set, null, null, null, "created by Scheduler", 1000 * 60 * 10, Collections
								.singleton(this.monitoredElement.getId()));
					MeasurementStorableObjectPool.putStorableObject(this.measurementSetup);
				} catch (IllegalObjectEntityException e) {
					Log.debugException(e, Log.DEBUGLEVEL05);
				} catch (CreateObjectException e) {
					Log.debugException(e, Log.DEBUGLEVEL05);
				}
			}
			measurementSetupIds = Collections.singleton(this.measurementSetup.getId());
			Identifier modifierId = ((RISDSessionInfo) this.aContext.getSessionInterface()).getUserIdentifier();

			Date startTime = this.testTimeStamps.getStartTime();
			Date endTime = this.testTimeStamps.getEndTime();
			TestTemporalType temporalType = this.testTimeStamps.getTestTemporalType();
			TemporalPattern temporalPattern = this.testTimeStamps.getTemporalPattern();
			SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
			if (test == null) {
				try {
					test = Test.createInstance(modifierId, startTime, endTime, temporalPattern == null ? null
							: temporalPattern.getId(), temporalType, this.measurementType.getId(),
						this.analysisType == null ? null : this.analysisType.getId(), this.evaluationType == null
								? null : this.evaluationType.getId(), this.monitoredElement, this.returnType,
						sdf.format(startTime), measurementSetupIds);

					MeasurementStorableObjectPool.putStorableObject(test);
				} catch (IllegalObjectEntityException e) {
					Log.errorException(e);
				} catch (CreateObjectException e) {
					Log.errorException(e);
				}
				this.tests.add(test);
			} else {
				test.setAttributes(test.getCreated(), new Date(System.currentTimeMillis()), test.getCreatorId(),
					modifierId, test.getVersion(), temporalType.value(), startTime, endTime, temporalPattern == null
							? null : temporalPattern.getId(), this.measurementType.getId(), this.analysisType == null
							? null : this.analysisType.getId(), this.evaluationType == null ? null
							: this.evaluationType.getId(), test.getStatus().value(), this.monitoredElement,
					this.returnType.value(), sdf.format(startTime));
			}
			try {
				MeasurementStorableObjectPool.putStorableObject(test);
			} catch (IllegalObjectEntityException e) {
				Log.debugException(e, Log.DEBUGLEVEL05);
			}

			this.selectedTest = test;
			this.refreshTests();
		}
	}

	public static void showErrorMessage(Component component,
										Exception exception) {
		exception.printStackTrace();
		JOptionPane.showMessageDialog(component, exception.getMessage(), LangModelSchedule.getString("Error"),
			JOptionPane.OK_OPTION);
	}

	public void addTestsEditor(TestsEditor testsEditor) {
		TestsEditor[] testEditors = new TestsEditor[this.testsEditors.length + 1];
		System.arraycopy(this.testsEditors, 0, testEditors, 1, this.testsEditors.length);
		testEditors[0] = testsEditor;
		this.testsEditors = testEditors;
	}

	public void removeTestsEditor(TestsEditor testsEditor) {
		int index = -1;
		for (int i = 0; i < this.testsEditors.length; i++) {
			if (this.testsEditors[i].equals(testsEditor)) {
				index = i;
				break;
			}
		}

		if (index >= 0) {
			TestsEditor[] testEditors = new TestsEditor[this.testsEditors.length - 1];
			System.arraycopy(this.testsEditors, 0, testEditors, 0, index + 1);
			System.arraycopy(this.testsEditors, index + 1, testEditors, index, this.testsEditors.length - index - 1);
			this.testsEditors = testEditors;
		}
	}

	public void addTestEditor(TestEditor testEditor) {
		TestEditor[] testEditors = new TestEditor[this.testEditors.length + 1];
		System.arraycopy(this.testEditors, 0, testEditors, 1, this.testEditors.length);
		testEditors[0] = testEditor;
		this.testEditors = testEditors;
	}

	public void removeTestEditor(TestEditor testEditor) {
		int index = -1;
		for (int i = 0; i < this.testEditors.length; i++) {
			if (this.testEditors[i].equals(testEditor)) {
				index = i;
				break;
			}
		}

		if (index >= 0) {
			TestEditor[] testEditors = new TestEditor[this.testEditors.length - 1];
			System.arraycopy(this.testEditors, 0, testEditors, 0, index + 1);
			System.arraycopy(this.testEditors, index + 1, testEditors, index, this.testEditors.length - index - 1);
			this.testEditors = testEditors;
		}
	}

	public void setAnalysisTypeEditor(AnalysisTypeEditor analysisTypeEditor) {
		this.analysisTypeEditor = analysisTypeEditor;
	}

	public void setEvaluationTypeEditor(EvaluationTypeEditor evaluationTypeEditor) {
		this.evaluationTypeEditor = evaluationTypeEditor;
	}

	public void setKisEditor(KISEditor kisEditor) {
		this.kisEditor = kisEditor;
	}

	public void setMeasurementSetupEditor(MeasurementSetupEditor measurementSetupEditor) {
		this.measurementSetupEditor = measurementSetupEditor;
	}

	public void setMeasurementTypeEditor(MeasurementTypeEditor measurementTypeEditor) {
		this.measurementTypeEditor = measurementTypeEditor;
	}

	public void setMonitoredElementEditor(MonitoredElementEditor monitoredElementEditor) {
		this.monitoredElementEditor = monitoredElementEditor;
	}

	public void setReturnTypeEditor(ReturnTypeEditor returnTypeEditor) {
		this.returnTypeEditor = returnTypeEditor;
	}

	public void setElementsViewer(ElementsViewer elementsViewer) {
		this.elementsViewer = elementsViewer;
	}

	public void setSetEditor(SetEditor setEditor) {
		this.setEditor = setEditor;
	}

	public void setTestTemporalStampsEditor(TestTemporalStampsEditor testTemporalStampsEditor) {
		this.testTemporalStampsEditor = testTemporalStampsEditor;
	}

}