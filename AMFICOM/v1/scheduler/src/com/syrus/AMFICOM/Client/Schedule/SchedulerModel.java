/*
 * SchedulerModel.java
 * Created on 11.06.2004 10:42:43
 * 
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.swing.JOptionPane;

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
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
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

	public static final String		COMMAND_ADD_PARAM_PANEL			= "AddParamPanel";			//$NON-NLS-1$

	public static final String		COMMAND_AVAILABLE_ME			= "AvailableMe";
	public static final String		COMMAND_CHANGE_KIS				= "ChangeKIS";				//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_ME_TYPE			= "ChangeMEType";			//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_PARAM_PANEL		= "ChangeParamPanel";		//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_PORT_TYPE		= "ChangePortType";		//$NON-NLS-1$
	public static final String		COMMAND_CHANGE_STATUSBAR_STATE	= "ChangeStatusBarState";
	public static final String		COMMAND_CHANGE_TEST_TYPE		= "ChangeTestType";		//$NON-NLS-1$
	public static final String		COMMAND_CLEAN					= "Clean";

	public static final String		COMMAND_DATA_REQUEST			= "DataRequest";			//$NON-NLS-1$

	public static final String		COMMAND_REFRESH_TESTS			= "RefreshTests";
	public static final String		COMMAND_REFRESH_TEST			= "RefreshTest";


//	private static final boolean	CREATE_ALLOW					= true;

	private static final int		FLAG_APPLY						= 1 << 1;
	private static final int		FLAG_CREATE						= 1 << 0;
	private ApplicationContext		aContext;
	private Dispatcher				dispatcher;												// =

	private int						flag							= 0;

	private ObjectResourceTreeModel	treeModel;

	private Collection				tests							= new HashSet();
	private Collection				unsavedTests					= new HashSet();
	private Test					selectedTest;

	private boolean					acquireMeasurementType			= false;
	private boolean					acquireKIS						= false;
	private boolean					acquireMonitoredElement			= false;
	private boolean					acquireAnalysisType				= false;
	private boolean					acquireEvaluationType			= false;
	private boolean					acquireSet						= false;
	private boolean					acquireMeasurementSetup			= false;
	private boolean					acquireReturnType				= false;
	private boolean					acquireTestTemporalStamps		= false;

	private MeasurementType			measurementType					= null;
	private KIS						kis								= null;
	private MonitoredElement		monitoredElement				= null;
	private AnalysisType			analysisType					= null;
	private EvaluationType			evaluationType					= null;
	private Set						set								= null;
	private Collection				measurementSetupIds				= null;
	private TestReturnType			returnType						= null;
	private TestTemporalStamps		testTimeStamps					= null;

	private DataSourceInterface		dataSourceInterface;

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

	/**
	 * @return Returns the unsavedTests.
	 */
	public Collection getUnsavedTests() {
		return this.unsavedTests;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());		
		if (commandName.equals(COMMAND_CLEAN)) {
			if (!obj.equals(this)) {
				if (this.tests != null)
					this.tests.clear();
				if (this.unsavedTests != null)
					this.unsavedTests.clear();
			}

		} 
	}
	
	public void removeTest(Test test) {
		this.tests.remove(test);
		this.unsavedTests.remove(test);
		this.selectedTest = null;
	}

	/**
	 * @param treeModel
	 *            The treeModel to set.
	 */
	public void setTreeModel(ObjectResourceTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public void applyTest() {
		this.flag = FLAG_APPLY;
		this.startGetData();
	}

	public void createTest() {
		this.flag = FLAG_CREATE;
		this.startGetData();
	}

	private void startGetData() {
		this.acquireMeasurementType = false;
		this.acquireKIS = false;
		this.acquireMonitoredElement = false;
		this.acquireAnalysisType = false;
		this.acquireEvaluationType = false;
		this.acquireSet = false;
		this.acquireMeasurementSetup = false;
		this.acquireReturnType = false;
		this.acquireTestTemporalStamps = false;

		this.measurementType = null;
		this.kis = null;
		this.monitoredElement = null;
		this.analysisType = null;
		this.evaluationType = null;
		this.set = null;
		this.measurementSetupIds = null;
		this.returnType = null;
		this.testTimeStamps = null;

		this.dispatcher.notify(new OperationEvent(this, 0, //$NON-NLS-1$
													COMMAND_DATA_REQUEST));
	}

	public void updateTests(long startTime,
							long endTime) {
		try {
			// Environment.log(Environment.LOG_LEVEL_INFO, "updateTests",
			// getClass().getName()); //$NON-NLS-1$
			// this.setCursor(UIStorage.WAIT_CURSOR);
			this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
					.getString("Updating_tests_from_BD"))); //$NON-NLS-1$
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
			CompoundCondition compoundCondition = new CompoundCondition(startTypicalCondition,
																		CompoundConditionSort.AND, endTypicalCondition);

			this.tests = MeasurementStorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);

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
			this.dispatcher.notify(new OperationEvent(this, 0, COMMAND_REFRESH_TESTS));
		} catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	public Test getSelectedTest() {
		return this.selectedTest;
	}

	public void setSelectedTest(Test selectedTest) {
		this.selectedTest = selectedTest;
		this.dispatcher.notify(new OperationEvent(this.selectedTest, 0, COMMAND_REFRESH_TEST));
	}

	public void commitChanges() throws ApplicationException {
		MeasurementStorableObjectPool.flush(true);
	}

	private void generateTest() {
		if (this.acquireMeasurementType && this.acquireKIS && this.acquireMonitoredElement && this.acquireAnalysisType
				&& this.acquireEvaluationType && this.acquireSet && this.acquireMeasurementSetup
				&& this.acquireReturnType && this.acquireTestTemporalStamps) {

			Test test = (this.flag == FLAG_APPLY) ? this.selectedTest : null;

			if (this.measurementSetupIds == null) {
				RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();
				try {
					MeasurementSetup measurementSetup = MeasurementSetup.createInstance(sessionInterface
							.getUserIdentifier(), this.set, null, null, null, "created by Scheduler", 1000 * 60 * 10,
						Collections.singletonList(this.monitoredElement.getId()));
					this.measurementSetupIds = Collections.singletonList(measurementSetup.getId());

					MeasurementStorableObjectPool.putStorableObject(measurementSetup);
				} catch (IllegalObjectEntityException e) {
					Log.debugException(e, Log.DEBUGLEVEL05);
				} catch (CreateObjectException e) {
					Log.debugException(e, Log.DEBUGLEVEL05);
				}
			}
			Identifier modifierId = ((RISDSessionInfo) this.aContext.getSessionInterface()).getUserIdentifier();

			Date startTime = this.testTimeStamps.getStartTime();
			Date endTime = this.testTimeStamps.getEndTime();
			TestTemporalType temporalType = this.testTimeStamps.getTestTemporalType();
			TemporalPattern temporalPattern = this.testTimeStamps.getTemporalPattern();

			if (test == null) {
				try {
					test = Test.createInstance(modifierId, startTime, endTime, temporalPattern, temporalType,
						this.measurementType, this.analysisType, this.evaluationType, this.monitoredElement,
						this.returnType, ConstStorage.SIMPLE_DATE_FORMAT.format(startTime), this.measurementSetupIds);

					MeasurementStorableObjectPool.putStorableObject(test);
				} catch (IllegalObjectEntityException e) {
					Log.errorException(e);
				} catch (CreateObjectException e) {
					Log.errorException(e);
				}
				this.unsavedTests.add(test);
				this.tests.add(test);
			} else {
				test.setAttributes(test.getCreated(), new Date(System.currentTimeMillis()), test.getCreatorId(),
					modifierId, test.getVersion(), temporalType.value(), startTime, endTime, temporalPattern,
					this.measurementType, this.analysisType, this.evaluationType, test.getStatus().value(),
					this.monitoredElement, this.returnType.value(), ConstStorage.SIMPLE_DATE_FORMAT.format(startTime));
			}
			try {
				MeasurementStorableObjectPool.putStorableObject(test);
			} catch (IllegalObjectEntityException e) {
				Log.debugException(e, Log.DEBUGLEVEL05);
			}
			
			this.selectedTest = test;
			this.dispatcher.notify(new OperationEvent(this, 0, COMMAND_REFRESH_TESTS));
		}
	}

	public static void showErrorMessage(Component component,
										Exception exc) {
		exc.printStackTrace();
		JOptionPane.showMessageDialog(component, exc.getMessage(), LangModelSchedule.getString("Error"),
			JOptionPane.OK_OPTION);
	}

	public void setAnalysisType(AnalysisType analysisType) {
		this.analysisType = analysisType;
		this.acquireAnalysisType = true;
		this.generateTest();
	}

	public void setEvaluationType(EvaluationType evaluationType) {
		this.evaluationType = evaluationType;
		this.acquireEvaluationType = true;
		this.generateTest();
	}

	public void setKis(KIS kis) {
		this.kis = kis;
		this.acquireKIS = true;
		this.generateTest();
	}

	public void setMeasurementSetupIds(Collection measurementSetupIds) {
		this.measurementSetupIds = measurementSetupIds;
		this.acquireMeasurementSetup = true;
		this.generateTest();
	}

	public void setMeasurementType(MeasurementType measurementType) {
		this.measurementType = measurementType;
		this.acquireMeasurementType = true;
		this.generateTest();
	}

	public void setMonitoredElement(MonitoredElement monitoredElement) {
		this.monitoredElement = monitoredElement;
		this.acquireMonitoredElement = true;
		this.generateTest();
	}

	public void setReturnType(TestReturnType returnType) {
		this.returnType = returnType;
		this.acquireReturnType = true;
		this.generateTest();
	}

	public void setSet(Set set) {
		this.set = set;
		this.acquireSet = true;
		this.generateTest();
	}

	public void setTestTimeStamps(TestTemporalStamps testTimeStamps) {
		this.testTimeStamps = testTimeStamps;
		this.acquireTestTemporalStamps = true;
		this.generateTest();
	}

}