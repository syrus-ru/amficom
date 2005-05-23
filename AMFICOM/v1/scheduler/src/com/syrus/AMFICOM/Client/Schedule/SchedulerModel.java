/*
 * SchedulerModel.java
 * Created on 11.06.2004 10:42:43
 * 
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.item.MeasurementTypeChildrenFactory;
import com.syrus.AMFICOM.Client.Schedule.item.MeasurementTypeItem;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @author Vladimir Dolzhenko
 */
public class SchedulerModel extends ApplicationModel implements PropertyChangeListener {

//	public static final String			COMMAND_ADD_PARAM_PANEL			= "AddParamPanel";			//$NON-NLS-1$

	public static final String			COMMAND_CHANGE_KIS				= "ChangeKIS";				//$NON-NLS-1$
	public static final String			COMMAND_CHANGE_ME_TYPE			= "ChangeMEType";			//$NON-NLS-1$
//	public static final String			COMMAND_CHANGE_PARAM_PANEL		= "ChangeParamPanel";		//$NON-NLS-1$
	public static final String			COMMAND_CHANGE_STATUSBAR_STATE	= "ChangeStatusBarState";

	
	// //$NON-NLS-1$
	public static final String			COMMAND_CLEAN					= "Clean";

	// private static final boolean CREATE_ALLOW = true;

	private static final int			FLAG_APPLY						= 1 << 1;
	private static final int			FLAG_CREATE						= 1 << 2;
//	private ApplicationContext			aContext;
	private Dispatcher					dispatcher;

	private int							flag							= 0;

//	private ObjectResourceTreeModel		treeModel;
	private Collection					tests							= new LinkedList();
	private Test						selectedFirstTest;
	private java.util.Set				selectedTestIds;
	private Map				measurementSetupMap;
	
	public static final String COMMAND_GET_NAME = "GetName";
	public static final String COMMAND_SET_NAME = "SetName";
	
	public static final String COMMAND_GET_MEASUREMENT_TYPE = "GetMeasurementType";
	public static final String COMMAND_SET_MEASUREMENT_TYPE = "SetMeasurementType";
	public static final String COMMAND_SET_MEASUREMENT_TYPES = "SetMeasurementTypes";
	
	public static final String COMMAND_GET_MONITORED_ELEMENT = "GetMonitoredElement";
	public static final String COMMAND_SET_MONITORED_ELEMENT = "SetMonitoredElement";

	public static final String COMMAND_GET_ANALYSIS_TYPE = "GetAnalysisType";
	public static final String COMMAND_SET_ANALYSIS_TYPE = "SetAnalysisType";
	public static final String COMMAND_SET_ANALYSIS_TYPES = "SetAnalysisTypes";

	public static final String COMMAND_GET_EVALUATION_TYPE = "GetEvaluationType";
	public static final String COMMAND_SET_EVALUATION_TYPE = "SetEvaluationType";
	public static final String COMMAND_SET_EVALUATION_TYPES = "SetEvaluationTypes";

	public static final String COMMAND_GET_SET = "GetSet";
	public static final String COMMAND_SET_SET = "SetSet";

	public static final String COMMAND_GET_MEASUREMENT_SETUP = "GetMeasurementSetup";
	public static final String COMMAND_SET_MEASUREMENT_SETUP = "SetMeasurementSetup";
	public static final String COMMAND_SET_MEASUREMENT_SETUPS = "SetMeasurementSetups";

	public static final String COMMAND_GET_RETURN_TYPE = "GetReturnType";
	public static final String COMMAND_SET_RETURN_TYPE = "SetReturnType";

	public static final String COMMAND_GET_TEMPORAL_STAMPS = "GetTestTemporalStamps";
	public static final String COMMAND_SET_TEMPORAL_STAMPS = "SetTestTemporalStamps";
	
	public static final String	COMMAND_REFRESH_TIME_STAMPS	= "RefreshTimeStamp";
	public static final String	COMMAND_REFRESH_TEST	= "RefreshTest";
	public static final String	COMMAND_REFRESH_TESTS	= "RefreshTests";
	
	public static final String	COMMAND_SET_GROUP_TEST = "GroupTest";
	public static final String	COMMAND_SET_START_GROUP_TIME = "SetStartGroupTime";
	
	public static final String COMMAND_ADD_NEW_MEASUREMENT_SETUP = "AddNewMeasurementSetup";
	
	public static final String COMMAND_DATE_OPERATION = "DateOperation";

//	public static final String	COMMAND_ONE_TIME_S

//	private KISEditor					kisEditor;

	private MeasurementType				measurementType					= null;
	
//	private IntervalsEditor	intervalsEditor;
//	private KIS							kis								= null;
	private String name = null;
	private MonitoredElement			monitoredElement				= null;
	private Identifier				analysisTypeId					= null;
	private Identifier				evaluationTypeId					= null;
	private Set							set								= null;
	private MeasurementSetup			measurementSetup				= null;
	private TestReturnType				returnType						= null;
	private TestTemporalStamps			testTimeStamps					= null;
	
	private Date startGroupDate;
	private long interval;
	private boolean aloneGroupTest;
	
	private Map	meTestGroup;

	private boolean groupTest = false;
	
	public static final Color			COLOR_ABORDED					= Color.RED;

	public static final Color			COLOR_ABORDED_SELECTED			= Color.RED.darker();

	public static final Color			COLOR_ALARM						= Color.ORANGE.darker();

	public static final Color			COLOR_ALARM_SELECTED			= Color.ORANGE;

	public static final Color			COLOR_COMPLETED					= Color.GREEN.darker();

	public static final Color			COLOR_COMPLETED_SELECTED		= Color.GREEN;

	public static final Color			COLOR_PROCCESSING				= Color.CYAN.darker();

	public static final Color			COLOR_PROCCESSING_SELECTED		= Color.CYAN;

	public static final Color			COLOR_SCHEDULED					= Color.GRAY;

	public static final Color			COLOR_SCHEDULED_SELECTED		= Color.LIGHT_GRAY.brighter();

	public static final Color			COLOR_UNRECOGNIZED				= new Color(20, 20, 60);

	public static final Color			COLOR_WARNING					= Color.YELLOW.darker();

	public static final Color			COLOR_WARNING_SELECTED			= Color.YELLOW;

	public SchedulerModel(ApplicationContext aContext) {
//		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		
//		this.dispatcher.register(this, TimeStampsEditor.DATE_OPERATION);
		this.dispatcher.addPropertyChangeListener(COMMAND_CLEAN, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_NAME, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_TEMPORAL_STAMPS, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_SET, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_RETURN_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_MONITORED_ELEMENT, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_MEASUREMENT_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_MEASUREMENT_SETUP, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_EVALUATION_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_ANALYSIS_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_REFRESH_TIME_STAMPS, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_GROUP_TEST, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_ADD_NEW_MEASUREMENT_SETUP, this);
		
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
		add(ScheduleMainMenuBar.MENU_VIEW_ARRANGE);

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

	/**
	 * @return saved tests
	 */
	public Collection getTests() {
		return this.tests;
	}

//	/**
//	 * @return Returns the treeModel.
//	 */
//	public ObjectResourceTreeModel getTreeModel() {
//		return this.treeModel;
//	}

	
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		// Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" +
		// commandName, getClass().getName());
		// if (commandName.equals(TimeStampsEditor.DATE_OPERATION)) {
		// if (this.selectedTestIds != null && this.selectedTestIds.isChanged())
		// {
		// TestTemporalType temporalType =
		// this.selectedTestIds.getTemporalType();
		// switch(temporalType.value()) {
		// case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
		// Log.debugMessage("SchedulerModel.operationPerformed | selected test "
		// + this.selectedTestIds.getId(), Log.FINEST);
		// Log.debugMessage("SchedulerModel.operationPerformed | selected test
		// was " + this.selectedTestIds.getStartTime(), Log.FINEST);
		// this.selectedTestIds.setStartTime((Date) obj);
		// Log.debugMessage("SchedulerModel.operationPerformed | selected test
		// now " + this.selectedTestIds.getStartTime(), Log.FINEST);
		// this.refreshTests();
		// break;
		// }
		// }
		// }
		// else
		if (propertyName.equals(COMMAND_CLEAN)) {
			if (this.tests != null)
				this.tests.clear();

			try {
				this.refreshEditors();
			} catch (ApplicationException e) {
				showErrorMessage(Environment.getActiveWindow(), e);
			}
		} else if (propertyName.equals(COMMAND_SET_ANALYSIS_TYPE)) {
			this.analysisTypeId = (Identifier) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_EVALUATION_TYPE)) {
			this.evaluationTypeId = (Identifier) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_MEASUREMENT_TYPE)) {
			this.measurementType = (MeasurementType) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_MONITORED_ELEMENT)) {
			this.monitoredElement = (MonitoredElement) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_SET)) {
			this.set = (Set) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_MEASUREMENT_SETUP)) {
			this.measurementSetup = (MeasurementSetup) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_RETURN_TYPE)) {
			this.returnType = (TestReturnType) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_TEMPORAL_STAMPS)) {
			this.testTimeStamps = (TestTemporalStamps) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_NAME)) {
			this.name = (String) evt.getNewValue();
		} else if (propertyName.equals(COMMAND_REFRESH_TIME_STAMPS)) {
			this.refreshTemporalStamps();
		} else if (propertyName.equals(COMMAND_SET_GROUP_TEST)) {
			this.groupTest = true;
		} else if (propertyName.equals(COMMAND_ADD_NEW_MEASUREMENT_SETUP)) {
			if (this.measurementSetupMap != null) {
				this.measurementSetupMap.clear();
			}
			this.refreshMeasurementSetups();
			this.refreshTests();
		}
	}

	public void removeTest(Test test) {
		Identifier groupTestId = test.getGroupTestId();
		if (groupTestId != null) {
			try {
				java.util.Set testsByCondition = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(groupTestId, ObjectEntities.TEST_ENTITY_CODE), true);
				java.util.Set testIds = new HashSet(testsByCondition.size());
				for (Iterator iterator = testsByCondition.iterator(); iterator.hasNext();) {
					Test test2 = (Test) iterator.next();
					testIds.add(test2.getId());
					this.tests.remove(test2);
				}
				StorableObjectPool.delete(testIds);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			this.tests.remove(test);
			StorableObjectPool.delete(test.getId());
		}
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
			this.selectedFirstTest = null;
		}
		this.refreshTests();
	}

	private void refreshEditors() throws ApplicationException {
//		Collection temporalPatterns = MeasurementStorableObjectPool.getStorableObjectsByCondition(
//			new EquivalentCondition(ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE), true);
//		this.testTemporalStampsEditor.setTemporalPatterns(temporalPatterns);

		{
			Collection measurementTypes = StorableObjectPool.getStorableObjectsByCondition(
				new EquivalentCondition(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

			// LinkedIdsCondition domainCondition = new
			// LinkedIdsCondition(sessionInterface.getDomainIdentifier(),
			// ObjectEntities.ME_ENTITY_CODE);
			//
			// Collection monitoredElements =
			// ConfigurationStorableObjectPool.getStorableObjectsByCondition(
			// domainCondition, true);

			Collection measurementTypeItems = new ArrayList(measurementTypes.size());

			for (Iterator iter = measurementTypes.iterator(); iter.hasNext();) {
				MeasurementType measurementType1 = (MeasurementType) iter.next();
				MeasurementTypeItem measurementTypeItem = new MeasurementTypeItem(measurementType1.getId());
				measurementTypeItems.add(measurementTypeItem);
				measurementTypeItem.setChildrenFactory(new MeasurementTypeChildrenFactory(LoginManager.getDomainId()));
			}

//			this.elementsViewer.setElements(measurementTypeItems);
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this , COMMAND_SET_MEASUREMENT_TYPES, null, measurementTypeItems));
			
			this.dispatcher
					.firePropertyChange(new PropertyChangeEvent(
																this,
																COMMAND_SET_ANALYSIS_TYPES,
																null,
																StorableObjectPool
																		.getStorableObjectsByCondition(
																			new EquivalentCondition(
																									ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
																			true)));
			
			this.dispatcher
			.firePropertyChange(new PropertyChangeEvent(
														this,
														COMMAND_SET_EVALUATION_TYPES,
														null,
														StorableObjectPool
																.getStorableObjectsByCondition(
																	new EquivalentCondition(
																							ObjectEntities.EVALUATIONTYPE_ENTITY_CODE),
																	true)));

			// if (!monitoredElements.isEmpty()) {
			// LinkedIdsCondition linkedIdsCondition;
			// {
			// java.util.Set meIdList = new HashSet(monitoredElements.size());
			// for (Iterator it = monitoredElements.iterator(); it.hasNext();) {
			// MonitoredElement me = (MonitoredElement) it.next();
			// meIdList.add(me.getId());
			// }
			// linkedIdsCondition = new LinkedIdsCondition(meIdList,
			// ObjectEntities.MS_ENTITY_CODE);
			// }
			// java.util.Set measurementSetups =
			// MeasurementStorableObjectPool.getStorableObjectsByCondition(
			// linkedIdsCondition, true);
			//
			// this.measurementSetupEditor.setMeasurementSetups(measurementSetups);
			// }
			this.refreshMeasurementSetups();
		}

	}

	private void refreshTest() throws ApplicationException {
		Test test = this.getSelectedTest();

		if (test != null) {			
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MEASUREMENT_TYPE, null, StorableObjectPool
				.getStorableObject(test.getMeasurementTypeId(), true)));
			MonitoredElement monitoredElement1 = test.getMonitoredElement();
//			MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(
//				monitoredElement.getMeasurementPortId(), true);
//			this.kisEditor.setKIS((KIS) ConfigurationStorableObjectPool.getStorableObject(measurementPort.getKISId(),
//				true));
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MONITORED_ELEMENT, null, monitoredElement1));

			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_ANALYSIS_TYPE, this, test
					.getAnalysisTypeId()));
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_EVALUATION_TYPE, null, test
					.getEvaluationTypeId()));
			
			Collection measurementSetupIds = test.getMeasurementSetupIds();
			if (!measurementSetupIds.isEmpty()) {
				Identifier mainMeasurementSetupId = (Identifier) measurementSetupIds.iterator().next();
				MeasurementSetup measurementSetup1 = (MeasurementSetup) StorableObjectPool
						.getStorableObject(mainMeasurementSetupId, true);
				if (measurementSetup1 != null) {
					this.refreshMeasurementSetups();
					this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_SET, null, measurementSetup1.getParameterSet()));
//					if (this.setEditor != null) {
//						this.setEditor.setSet(measurementSetup1.getParameterSet());
//					}
//					this.measurementSetupEditor.setMeasurementSetup(measurementSetup1);
					this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MEASUREMENT_SETUP, null, measurementSetup1));
				}
			}

			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_RETURN_TYPE, null, test.getReturnType()));
//			this.returnTypeEditor.setReturnType(this.selectedTest.getReturnType());
			if (test.getGroupTestId() == null) {
				this.refreshTemporalStamps();
			} else {
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_START_GROUP_TIME, null, test.getStartTime()));
			}

		}
		
//		for (int i = 0; i < this.testEditors.length; i++) {
//			this.testEditors[i].updateTest();
//		}
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TEST, null, null));
	}
	
	private void refreshTemporalStamps() {
		Test test = this.getSelectedTest();

		if (test != null) {	
		Identifier temporalPatternId = test.getTemporalPatternId();
		AbstractTemporalPattern temporalPattern = null;
		if (temporalPatternId != null) {
			try {
				temporalPattern = (AbstractTemporalPattern) StorableObjectPool.getStorableObject(
					temporalPatternId, true);
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
		TestTemporalStamps timeStamps = new TestTemporalStamps(test.getTemporalType(),
			test.getStartTime(),
			test.getEndTime(), temporalPattern);
//			this.testTemporalStampsEditor.setTestTemporalStamps(timeStamps);
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_TEMPORAL_STAMPS, null, timeStamps));
		}
		
	}

	private void refreshTests() {
//		for (int i = 0; i < this.testsEditors.length; i++) {
//			this.testsEditors[i].updateTests();
//		}

		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TESTS, null, null));
		try {
			this.refreshTest();
		} catch (ApplicationException e) {
			SchedulerModel.showErrorMessage(Environment.getActiveWindow(), e);
		}
	}

//	/**
//	 * @param treeModel
//	 *            The treeModel to set.
//	 */
//	public void setTreeModel(ObjectResourceTreeModel treeModel) {
//		this.treeModel = treeModel;
//	}

	public void applyTest() {
		this.flag = FLAG_APPLY;
		this.startGetData();
	}

	public void createTest() {
		this.flag = FLAG_CREATE;
		this.startGetData();
	}

	private void startGetData() {
		this.groupTest = false;
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_MEASUREMENT_TYPE, null, null));
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_MONITORED_ELEMENT, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_ANALYSIS_TYPE, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_EVALUATION_TYPE, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_SET, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_MEASUREMENT_SETUP, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_RETURN_TYPE, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_TEMPORAL_STAMPS, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_NAME, null, null));
		}
		

		// this.measurementType =
		// this.measurementTypeEditor.getMeasurementType();
		// if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
		// this.kis = this.kisEditor.getKIS();
		// if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
		// this.monitoredElement =
		// this.monitoredElementEditor.getMonitoredElement();
		// if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
		// this.analysisType = this.analysisTypeEditor.getAnalysisType();
		// if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
		// this.evaluationType = this.evaluationTypeEditor.getEvaluationType();
		// if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
		// this.set = this.setEditor.getSet();
		// if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
		// this.measurementSetup =
		// this.measurementSetupEditor.getMeasurementSetup();
		// if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
		// this.returnType = this.returnTypeEditor.getReturnType();
		// if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE)
		// this.testTimeStamps =
		// this.testTemporalStampsEditor.getTestTemporalStamps();
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
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
				.getString("Updating tests from DB"))); //$NON-NLS-1$

		if (this.measurementSetupMap != null) {
			this.measurementSetupMap.clear();
		}
		StorableObjectPool.refresh();
		StorableObjectPool.refresh();

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

		this.tests = StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
				.getString("Updating_tests_from_BD_finished"))); //$NON-NLS-1$
		this.refreshTests();
	}

	public java.util.Set getSelectedTestIds() {
		return this.selectedTestIds;
	}
	
	public Test getSelectedTest() {
		return this.selectedFirstTest;
	}

	public void addSelectedTest(Test selectedTest) throws ApplicationException {
		Identifier selectedTestId = selectedTest.getId();
		if (this.selectedTestIds == null) {
			this.selectedTestIds = new HashSet();
		}
		
		if (selectedTest != null) {
			if (this.selectedTestIds.isEmpty()) {
				this.selectedFirstTest = selectedTest;
			}
			this.selectedTestIds.add(selectedTestId);
			this.refreshTest();
//			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TEST, null, null));
		} else {
			Log.debugMessage("SchedulerModel.setSelectedTest | selectedTest is " + selectedTest, Log.FINEST);
		}		
	}
	
	public void unselectTests() throws ApplicationException {
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
		}
		this.selectedFirstTest = null;
		this.refreshTest();
//		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TEST, null, null));
	}

	public void setSelectedMeasurementType(MeasurementType measurementType) {
		if (this.measurementType == null || measurementType == null
				|| !this.measurementType.getId().equals(measurementType.getId())) {
			this.measurementType = measurementType;
			this.refreshMeasurementSetups();
		}
	}

	public void setSelectedMonitoredElement(MonitoredElement monitoredElement) {
		if (this.monitoredElement == null || monitoredElement == null
				|| !this.monitoredElement.getId().equals(monitoredElement.getId())) {
			this.monitoredElement = monitoredElement;
			this.refreshMeasurementSetups();
		}
	}

	public void setSelectedMonitoredElement(MonitoredElement monitoredElement,
											MeasurementType measurementType) {
		boolean changed = false;
		if (this.monitoredElement == null || monitoredElement == null
				|| !this.monitoredElement.getId().equals(monitoredElement.getId())) {
			changed = true;
			this.monitoredElement = monitoredElement;
		}

		if (this.measurementType == null || measurementType == null
				|| !this.measurementType.getId().equals(measurementType.getId())) {
			changed = true;
			this.measurementType = measurementType;
		}

		if (changed) {
			this.refreshMeasurementSetups();
		}
	}

	public void refreshMeasurementSetups() {
		StorableObjectCondition condition = null;

		LinkedIdsCondition measurementTypeCondition = null;
		Identifier identifier = null;
		java.util.Set idSet = null;
		
		
		if (this.measurementType != null) {
			identifier = this.measurementType.getId();
			measurementTypeCondition = new LinkedIdsCondition(identifier,
																ObjectEntities.MS_ENTITY_CODE);
		}

		if (this.monitoredElement != null) {
			LinkedIdsCondition monitoredElementCondition = new LinkedIdsCondition(this.monitoredElement.getId(),
																			ObjectEntities.MS_ENTITY_CODE);
			try {
				if (measurementTypeCondition != null) {
					idSet = new HashSet(2);
					idSet.add(identifier);
					identifier = this.monitoredElement.getId();
					idSet.add(identifier);
					condition = new CompoundCondition(measurementTypeCondition, CompoundConditionSort.OR,
														monitoredElementCondition);
				} else {
					idSet = Collections.singleton(identifier);
					condition = monitoredElementCondition;
				}
			} catch (CreateObjectException e) {
				// never !
				e.printStackTrace();
			}
		} else {
			condition = measurementTypeCondition;
		}

		try {
			if (condition != null) {
				java.util.Set measurementSetups = (java.util.Set) (this.measurementSetupMap != null ? this.measurementSetupMap.get(idSet) : null);
				if (measurementSetups == null) {
					measurementSetups = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					if (this.measurementSetupMap == null) {
						this.measurementSetupMap = new HashMap();
					}
					this.measurementSetupMap.put(idSet, measurementSetups);
				}
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MEASUREMENT_SETUPS, null, measurementSetups));
			}

			if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
				Collection measurementSetupIds = this.getSelectedTest().getMeasurementSetupIds();
				if (!measurementSetupIds.isEmpty()) {
					Identifier mainMeasurementSetupId = (Identifier) measurementSetupIds.iterator().next();
					MeasurementSetup measurementSetup1 = (MeasurementSetup) StorableObjectPool
							.getStorableObject(mainMeasurementSetupId, true);
					if (measurementSetup1 != null) {
						this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_SET, null, measurementSetup1.getParameterSet()));
						this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MEASUREMENT_SETUP, null, measurementSetup1));

					}
				}
			}

		} catch (ApplicationException e) {
			showErrorMessage(Environment.getActiveWindow(), e);
		}
	}

	public void commitChanges() throws ApplicationException {		
		StorableObjectPool.flush(ObjectEntities.TEST_ENTITY_CODE, true);
		if (this.meTestGroup != null) {
			this.meTestGroup.clear();
		}
		this.refreshTests();
//		this.refreshTest();
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TEST, null, null));
	}
	
	private void generateTest() {
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {

			Test test = null;
			test = (this.flag == FLAG_APPLY) ? this.getSelectedTest() : null;
			SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
			java.util.Set measurementSetupIds;
			if (this.measurementSetup == null) {
				if (this.set == null)
					return;
				try {
					this.measurementSetup = MeasurementSetup.createInstance(LoginManager.getUserId(),
						this.set, null, null, null, LangModelSchedule.getString("created by Scheduler") + " /"
								+ sdf.format(new Date()) + "/", 1000 * 60 * 10, Collections
								.singleton(this.monitoredElement.getId()), Collections.singleton(this.measurementType
								.getId())); 
					StorableObjectPool.putStorableObject(this.measurementSetup);
					if (this.measurementSetupMap != null) {
						this.measurementSetupMap.clear();
					}
				} catch (IllegalObjectEntityException e) {
					Log.debugException(e, Log.DEBUGLEVEL05);
				} catch (CreateObjectException e) {
					Log.debugException(e, Log.DEBUGLEVEL05);
				}
				this.refreshMeasurementSetups();

			}
			measurementSetupIds = Collections.singleton(this.measurementSetup.getId());

			Date startTime = this.testTimeStamps.getStartTime();
			Date endTime = this.testTimeStamps.getEndTime();
			TestTemporalType temporalType = this.testTimeStamps.getTestTemporalType();
			AbstractTemporalPattern temporalPattern = this.testTimeStamps.getTemporalPattern();
			if (test == null) {
				try {
					if (this.isValid(startTime, endTime, this.monitoredElement.getId())) {
					test = Test.createInstance(LoginManager.getUserId(), startTime, endTime, temporalPattern == null
							? null : temporalPattern.getId(), temporalType, this.measurementType.getId(),
						this.analysisTypeId == null ? null : this.analysisTypeId, this.evaluationTypeId == null
								? null : this.evaluationTypeId, null, this.monitoredElement, this.returnType,
						this.name != null && this.name.trim().length() > 0 ? this.name : sdf.format(startTime),
						measurementSetupIds);
					
					
					
					if (this.groupTest) {
						if (this.meTestGroup == null) {
							this.meTestGroup = new HashMap();
						}
						Identifier meId = this.monitoredElement.getId();
						Identifier testGroupId = (Identifier) this.meTestGroup.get(meId);
						if (testGroupId == null) {
							testGroupId = test.getId();
							this.meTestGroup.put(meId, testGroupId);
						}
						
						test.setGroupTestId(testGroupId);
					}

					StorableObjectPool.putStorableObject(test);
					} else {
						JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelSchedule.getString("Cannot add test"), LangModelSchedule.getString("Error"),
							JOptionPane.OK_OPTION);
					}
				} catch (IllegalObjectEntityException e) {
					Log.errorException(e);
				} catch (CreateObjectException e) {
					Log.errorException(e);
				}
				this.tests.add(test);
			} else {
				if (this.isValid(startTime, endTime, this.monitoredElement.getId())) {
				test.setAttributes(test.getCreated(), new Date(System.currentTimeMillis()), test.getCreatorId(),
					LoginManager.getUserId(), test.getVersion(), temporalType.value(), startTime, endTime,
					temporalPattern == null ? null : temporalPattern.getId(), this.measurementType.getId(),
					this.analysisTypeId == null ? null : this.analysisTypeId, test.getGroupTestId(), this.evaluationTypeId == null ? null
							: this.evaluationTypeId, test.getStatus().value(), this.monitoredElement,
					this.returnType.value(), this.name != null && this.name.trim().length() > 0 ? this.name : sdf
							.format(startTime), test.getNumberOfMeasurements());
				} else {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelSchedule.getString("Cannot update test"), LangModelSchedule.getString("Error"),
						JOptionPane.OK_OPTION);
				}
			}
			try {
				StorableObjectPool.putStorableObject(test);
			} catch (IllegalObjectEntityException e) {
				Log.debugException(e, Log.DEBUGLEVEL05);
			}

		
			try {
				if (this.selectedTestIds != null) {
					this.selectedTestIds.clear();
				}
				this.addSelectedTest(test);
//				this.refreshTests();
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TESTS, null, null));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
			
		}
	}
	
	public void addGroupTest(Date date) {
		this.aloneGroupTest = true;
		this.startGroupDate = date;
		this.addGroupTests();
	}
	
	public void addGroupTests(Date date, long interval1) {
		this.aloneGroupTest = false;
		this.startGroupDate = date;
		this.interval = interval1;
		this.addGroupTests();
	}
	
	public void moveSelectedTests(Date startDate) {
		Log.debugMessage("SchedulerModel.moveSelectedTests | startDate is " + startDate, Log.FINEST);
		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
			try {
				SortedSet selectedTests = new TreeSet(new WrapperComparator(TestWrapper.getInstance(),
																			TestWrapper.COLUMN_START_TIME));
				selectedTests.addAll(StorableObjectPool.getStorableObjects(this.selectedTestIds, true));

				Test firstTest = ((Test) selectedTests.first());
				long offset = startDate.getTime() - firstTest.getStartTime().getTime();

				boolean correct = true;
				for (Iterator iterator = selectedTests.iterator(); iterator.hasNext();) {
					Test selectedTest = (Test) iterator.next();
					Date newStartDate = new Date(selectedTest.getStartTime().getTime() + offset);
					Date newEndDate = selectedTest.getEndTime();
					if (newEndDate != null) {
						newEndDate = new Date(newEndDate.getTime() + offset);
					}
					correct = this.isValid(newStartDate, newEndDate, selectedTest.getMonitoredElement().getId());
					if (!correct) {
						JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelSchedule.getString("Cannot move tests"), LangModelSchedule.getString("Error"),
							JOptionPane.OK_OPTION);
						break;
					}
				}

				if (correct) {
					for (Iterator iterator = selectedTests.iterator(); iterator.hasNext();) {
						Test selectedTest = (Test) iterator.next();
						Date newStartDate = new Date(selectedTest.getStartTime().getTime() + offset);
						Date newEndDate = selectedTest.getEndTime();
						if (newEndDate != null) {
							newEndDate = new Date(newEndDate.getTime() + offset);
						}
						selectedTest.setStartTime(newStartDate);
						selectedTest.setEndTime(newEndDate);
					}
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		this.refreshTests();
		Test selectedTest = this.getSelectedTest();
		if (selectedTest.getGroupTestId() == null) {
			this.refreshTemporalStamps();
		} else {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_START_GROUP_TIME, null, selectedTest.getStartTime()));
		}
	}
	
	private void addGroupTests() {
		Log.debugMessage("SchedulerModel.addGroupTests | ", Log.FINEST);
		Identifier meId = this.monitoredElement.getId();
		Identifier testGroupId = (Identifier) (this.meTestGroup != null ? this.meTestGroup.get(meId) : null);
		if (testGroupId != null) {
			try {
				Test testGroup = (Test) StorableObjectPool.getStorableObject(testGroupId, true);
				if (this.aloneGroupTest) {
					if (this.isValid(this.startGroupDate, null, testGroup
								.getMonitoredElement().getId())) {
					Test test = Test.createInstance(LoginManager.getUserId(), this.startGroupDate, null, null,
						TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, testGroup.getMeasurementTypeId(), testGroup
								.getAnalysisTypeId(), testGroup.getEvaluationTypeId(), testGroupId, testGroup
								.getMonitoredElement(), testGroup.getReturnType(), testGroup.getDescription(),
						testGroup.getMeasurementSetupIds());
					this.tests.add(test);
					StorableObjectPool.putStorableObject(test);
					if (this.selectedTestIds != null) {
						this.selectedTestIds.clear();
					} else {
						this.selectedTestIds = new HashSet();						
					}
					this.selectedTestIds.add(test.getId());
					} else {						
						JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelSchedule.getString("Cannot add tests"), LangModelSchedule.getString("Error"),
							JOptionPane.OK_OPTION);
					}
				} 
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		} 
		{
			try {

				if (this.selectedTestIds == null || (this.selectedTestIds.isEmpty())) {
					if (testGroupId == null) {
					this.createTest();
					} else {
						this.startGroupDate = new Date(this.startGroupDate.getTime() + this.interval);
						this.aloneGroupTest = true;
						this.addGroupTests();
					}
				} else {

						SortedSet selectedTests = new TreeSet(new WrapperComparator(TestWrapper.getInstance(),
																			TestWrapper.COLUMN_START_TIME));
						selectedTests.addAll(StorableObjectPool.getStorableObjects(this.selectedTestIds,
								true));
						Test firstTest = ((Test) selectedTests.first());
						Test lastTest = ((Test) selectedTests.last());

						Date endTime = lastTest.getEndTime();
						long firstTime = firstTest.getStartTime().getTime();
						long offset = (endTime != null ? endTime : lastTest.getStartTime()).getTime() + this.interval
								- firstTime;

						assert Log.debugMessage("SchedulerModel.addGroupTests | firstTime is " + new Date(firstTime),
							Log.FINEST);
						assert Log.debugMessage("SchedulerModel.addGroupTests | offset is " + offset, Log.FINEST);

						this.selectedTestIds.clear();

						boolean correct = true;
						for (Iterator iterator = selectedTests.iterator(); iterator.hasNext();) {
							Test selectedTest = (Test) iterator.next();
							Date startDate = new Date(selectedTest.getStartTime().getTime() + offset);
							Date endDate = selectedTest.getEndTime();
							if (endDate != null) {
								endDate = new Date(endDate.getTime() + offset);
							}

							// Log.debugMessage("SchedulerModel.addGroupTests |
							// new startDate " + startDate, Log.FINEST);
							// Log.debugMessage("SchedulerModel.addGroupTests |
							// new endDate " + endDate, Log.FINEST);
							correct = this.isValid(startDate, endDate, selectedTest.getMonitoredElement().getId());
							if (!correct) {
								JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelSchedule
										.getString("Cannot add tests"), LangModelSchedule.getString("Error"),
									JOptionPane.OK_OPTION);
								break;
							}
						}

						if (correct) {
							for (Iterator iterator = selectedTests.iterator(); iterator.hasNext();) {
								Test selectedTest = (Test) iterator.next();
								Date startDate = new Date(selectedTest.getStartTime().getTime() + offset);
								Date endDate = selectedTest.getEndTime();
								if (endDate != null) {
									endDate = new Date(endDate.getTime() + offset);
								}

								// Log.debugMessage("SchedulerModel.addGroupTests
								// | new startDate " + startDate, Log.FINEST);
								// Log.debugMessage("SchedulerModel.addGroupTests
								// | new endDate " + endDate, Log.FINEST);								
								Test test = Test.createInstance(LoginManager.getUserId(), startDate, endDate,
									selectedTest.getTemporalPatternId(), selectedTest.getTemporalType(), selectedTest
											.getMeasurementTypeId(), selectedTest.getAnalysisTypeId(), selectedTest
											.getEvaluationTypeId(), testGroupId, selectedTest.getMonitoredElement(),
									selectedTest.getReturnType(), selectedTest.getDescription(), selectedTest
											.getMeasurementSetupIds());
								if (testGroupId == null) {
									testGroupId = test.getId();
									test.setGroupTestId(testGroupId);
								}
								this.tests.add(test);
								StorableObjectPool.putStorableObject(test);
								this.selectedTestIds.add(test.getId());
								assert Log.debugMessage("SchedulerModel.addGroupTests | add test " + test.getId()
										+ " at " + startDate + "," + endDate, Log.FINEST);
							}
						}

					}				
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TESTS, null, null));
	}
	
	public boolean isValid(final Date startDate, final Date endDate, final Identifier monitoredElementId) {
		boolean result = true;
		Log.debugMessage("SchedulerModel.isValid | ", Log.FINEST);
		for (Iterator iterator = this.tests.iterator(); iterator.hasNext();) {
			Test test = (Test) iterator.next();
			Date startTime = test.getStartTime();
			Date endTime = test.getEndTime();
			if (endTime == null) {
				endTime = startTime;
			}
			Log.debugMessage("SchedulerModel.isValid | startDate " + startDate + ", endDate " + endDate, Log.FINEST);

			Log.debugMessage("SchedulerModel.isValid | startTime " + startTime + ", endTime " + endTime, Log.FINEST);
			if (test.getMonitoredElement().getId().equals(monitoredElementId) && 
					((endDate != null && endDate.after(startTime) && endDate.before(endTime))
					|| (startDate.after(startTime) && startDate.before(endTime)))){
				result = false;
				break;
			}
		}
		Log.debugMessage("SchedulerModel.isValid | result is " + result, Log.FINEST);
		return result;
	}

	public static void showErrorMessage(Component component,
										Exception exception) {
		exception.printStackTrace();
		JOptionPane.showMessageDialog(component, exception.getMessage(), LangModelSchedule.getString("Error"),
			JOptionPane.OK_OPTION);
	}

	public static Color getColor(TestStatus testStatus) {
		return getColor(testStatus, false);
	}
	
	public static Color getColor(TestStatus testStatus, boolean selected) {
		Color color;
		switch (testStatus.value()) {
			case TestStatus._TEST_STATUS_COMPLETED:
				color = selected ?  COLOR_COMPLETED_SELECTED : COLOR_COMPLETED;
				break;
			case TestStatus._TEST_STATUS_SCHEDULED:
			case TestStatus._TEST_STATUS_NEW:
				color = selected ? COLOR_SCHEDULED_SELECTED : COLOR_SCHEDULED;
				break;
			case TestStatus._TEST_STATUS_PROCESSING:
				color = selected ? COLOR_PROCCESSING_SELECTED : COLOR_PROCCESSING;
				break;
			case TestStatus._TEST_STATUS_ABORTED:
				color = selected ? COLOR_ABORDED_SELECTED : COLOR_ABORDED;
				break;
			default:
				color = COLOR_UNRECOGNIZED;
				break;
		}
		return color;
	}

//	public void addTestsEditor(TestsEditor testsEditor) {
//		TestsEditor[] testEditors1 = new TestsEditor[this.testsEditors.length + 1];
//		System.arraycopy(this.testsEditors, 0, testEditors1, 1, this.testsEditors.length);
//		testEditors1[0] = testsEditor;
//		this.testsEditors = testEditors1;
//	}
//
//	public void removeTestsEditor(TestsEditor testsEditor) {
//		int index = -1;
//		for (int i = 0; i < this.testsEditors.length; i++) {
//			if (this.testsEditors[i].equals(testsEditor)) {
//				index = i;
//				break;
//			}
//		}
//
//		if (index >= 0) {
//			TestsEditor[] testEditors1 = new TestsEditor[this.testsEditors.length - 1];
//			System.arraycopy(this.testsEditors, 0, testEditors1, 0, index + 1);
//			System.arraycopy(this.testsEditors, index + 1, testEditors1, index, this.testsEditors.length - index - 1);
//			this.testsEditors = testEditors1;
//		}
//	}
//
//	public void addTestEditor(TestEditor testEditor) {
//		TestEditor[] testEditors = new TestEditor[this.testEditors.length + 1];
//		System.arraycopy(this.testEditors, 0, testEditors, 1, this.testEditors.length);
//		testEditors[0] = testEditor;
//		this.testEditors = testEditors;
//	}
//
//	public void removeTestEditor(TestEditor testEditor) {
//		int index = -1;
//		for (int i = 0; i < this.testEditors.length; i++) {
//			if (this.testEditors[i].equals(testEditor)) {
//				index = i;
//				break;
//			}
//		}
//
//		if (index >= 0) {
//			TestEditor[] testEditors1 = new TestEditor[this.testEditors.length - 1];
//			System.arraycopy(this.testEditors, 0, testEditors1, 0, index + 1);
//			System.arraycopy(this.testEditors, index + 1, testEditors1, index, this.testEditors.length - index - 1);
//			this.testEditors = testEditors1;
//		}
//	}
//
//	public void setAnalysisTypeEditor(AnalysisTypeEditor analysisTypeEditor) {
//		this.analysisTypeEditor = analysisTypeEditor;
//	}
//
//	public void setEvaluationTypeEditor(EvaluationTypeEditor evaluationTypeEditor) {
//		this.evaluationTypeEditor = evaluationTypeEditor;
//	}
//
//	public void setKisEditor(KISEditor kisEditor) {
//		this.kisEditor = kisEditor;
//	}
//
//	public void setMeasurementSetupEditor(MeasurementSetupEditor measurementSetupEditor) {
//		this.measurementSetupEditor = measurementSetupEditor;
//	}
//
//	public void setMeasurementTypeEditor(MeasurementTypeEditor measurementTypeEditor) {
//		this.measurementTypeEditor = measurementTypeEditor;
//	}
//
//	public void setMonitoredElementEditor(MonitoredElementEditor monitoredElementEditor) {
//		this.monitoredElementEditor = monitoredElementEditor;
//	}
//
//	public void setReturnTypeEditor(ReturnTypeEditor returnTypeEditor) {
//		this.returnTypeEditor = returnTypeEditor;
//	}
//
//	public void setElementsViewer(ElementsViewer elementsViewer) {
//		this.elementsViewer = elementsViewer;
//	}
//
//	public void setSetEditor(SetEditor setEditor) {
//		this.setEditor = setEditor;
//	}
//
//	public void setTestTemporalStampsEditor(TestTemporalStampsEditor testTemporalStampsEditor) {
//		this.testTemporalStampsEditor = testTemporalStampsEditor;
//	}
//
//	
//	public void setIntervalsEditor(IntervalsEditor intervalsEditor) {
//		this.intervalsEditor = intervalsEditor;
//	}
//
//	public void refreshTestTemporalStamps() {
//		if (this.intervalsEditor != null) {
//			TestTemporalStamps testTemporalStamps = this.testTemporalStampsEditor.getTestTemporalStamps();
//			this.intervalsEditor.setIntervalsTemporalPattern(testTemporalStamps);
//		}
//	}
//	
//	

}
