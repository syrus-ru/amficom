/*
 * SchedulerModel.java
 * Created on 11.06.2004 10:42:43
 * 
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Color;
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
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.logic.IconPopulatableItem;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @author Vladimir Dolzhenko
 */
public class SchedulerModel extends ApplicationModel implements PropertyChangeListener {

	// public static final String COMMAND_ADD_PARAM_PANEL = "AddParamPanel";
	// //$NON-NLS-1$

	public static final String	COMMAND_CHANGE_KIS					= "ChangeKIS";
	public static final String	COMMAND_CHANGE_ME_TYPE				= "ChangeMEType";

	// //$NON-NLS-1$
	public static final String	COMMAND_CLEAN						= "Clean";

	// private static final boolean CREATE_ALLOW = true;

	private static final int	FLAG_APPLY							= 1 << 1;
	private static final int	FLAG_CREATE							= 1 << 2;
	Dispatcher					dispatcher;

	private int					flag								= 0;

	// private ObjectResourceTreeModel treeModel;
	private Set<Identifier>		testIds								= new HashSet<Identifier>();
	private Identifier			selectedFirstTestId;
	Set<Identifier>				selectedTestIds;
	Map<Set<Identifier>, Set<Identifier>>							measurementSetupIdMap;

	public static final String	COMMAND_GET_NAME					= "GetName";
	public static final String	COMMAND_SET_NAME					= "SetName";

	public static final String	COMMAND_GET_MEASUREMENT_TYPE		= "GetMeasurementType";
	public static final String	COMMAND_SET_MEASUREMENT_TYPE		= "SetMeasurementType";
	public static final String	COMMAND_SET_MEASUREMENT_TYPES		= "SetMeasurementTypes";

	public static final String	COMMAND_GET_MONITORED_ELEMENT		= "GetMonitoredElement";
	public static final String	COMMAND_SET_MONITORED_ELEMENT		= "SetMonitoredElement";

	public static final String	COMMAND_GET_ANALYSIS_TYPE			= "GetAnalysisType";
	public static final String	COMMAND_SET_ANALYSIS_TYPE			= "SetAnalysisType";
	public static final String	COMMAND_SET_ANALYSIS_TYPES			= "SetAnalysisTypes";

//	public static final String	COMMAND_GET_EVALUATION_TYPE			= "GetEvaluationType";
//	public static final String	COMMAND_SET_EVALUATION_TYPE			= "SetEvaluationType";
//	public static final String	COMMAND_SET_EVALUATION_TYPES		= "SetEvaluationTypes";

	public static final String	COMMAND_GET_SET						= "GetSet";
	public static final String	COMMAND_SET_SET						= "SetSet";

	public static final String	COMMAND_GET_MEASUREMENT_SETUP		= "GetMeasurementSetup";
	public static final String	COMMAND_SET_MEASUREMENT_SETUP		= "SetMeasurementSetup";
	public static final String	COMMAND_SET_MEASUREMENT_SETUPS		= "SetMeasurementSetups";

//	public static final String	COMMAND_GET_RETURN_TYPE				= "GetReturnType";
//	public static final String	COMMAND_SET_RETURN_TYPE				= "SetReturnType";

	public static final String	COMMAND_GET_TEMPORAL_STAMPS			= "GetTestTemporalStamps";
	public static final String	COMMAND_SET_TEMPORAL_STAMPS			= "SetTestTemporalStamps";

	public static final String	COMMAND_REFRESH_TIME_STAMPS			= "RefreshTimeStamp";
	public static final String	COMMAND_REFRESH_TEST				= "RefreshTest";
	public static final String	COMMAND_REFRESH_TESTS				= "RefreshTests";

	public static final String	COMMAND_SET_GROUP_TEST				= "GroupTest";
	public static final String	COMMAND_SET_START_GROUP_TIME		= "SetStartGroupTime";

	public static final String	COMMAND_ADD_NEW_MEASUREMENT_SETUP	= "AddNewMeasurementSetup";

	public static final String	COMMAND_DATE_OPERATION				= "DateOperation";

	MeasurementType				measurementType						= null;

	// private IntervalsEditor intervalsEditor;
	// private KIS kis = null;
	private String				name								= null;
	MonitoredElement			monitoredElement					= null;
	private Identifier			analysisTypeId						= null;
//	private Identifier			evaluationTypeId					= null;
	private ParameterSet		set									= null;
	private MeasurementSetup	measurementSetup					= null;
	private TestTemporalStamps	testTimeStamps						= null;

	private Date				startGroupDate;
	private long				interval;
	private boolean				aloneGroupTest;

	private Map<Identifier, Identifier>					meTestGroup;

	private boolean				groupTest							= false;

	public static final Color	COLOR_ABORDED						= Color.RED;

	public static final Color	COLOR_ABORDED_SELECTED				= Color.RED.darker();

	public static final Color	COLOR_ALARM							= Color.ORANGE.darker();

	public static final Color	COLOR_ALARM_SELECTED				= Color.ORANGE;

	public static final Color	COLOR_COMPLETED						= Color.GREEN.darker();

	public static final Color	COLOR_COMPLETED_SELECTED			= Color.GREEN;

	public static final Color	COLOR_PROCCESSING					= Color.CYAN.darker();

	public static final Color	COLOR_PROCCESSING_SELECTED			= Color.CYAN;

	public static final Color	COLOR_SCHEDULED						= Color.GRAY;

	public static final Color	COLOR_SCHEDULED_SELECTED			= Color.LIGHT_GRAY.brighter();

	public static final Color	COLOR_UNRECOGNIZED					= new Color(20, 20, 60);

	public static final Color	COLOR_WARNING						= Color.YELLOW.darker();

	public static final Color	COLOR_WARNING_SELECTED				= Color.YELLOW;

	public SchedulerModel(ApplicationContext aContext) {
		// this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();

		// this.dispatcher.register(this, TimeStampsEditor.DATE_OPERATION);
		this.dispatcher.addPropertyChangeListener(COMMAND_CLEAN, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_NAME, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_TEMPORAL_STAMPS, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_SET, this);
//		this.dispatcher.addPropertyChangeListener(COMMAND_SET_RETURN_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_MONITORED_ELEMENT, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_MEASUREMENT_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_MEASUREMENT_SETUP, this);
//		this.dispatcher.addPropertyChangeListener(COMMAND_SET_EVALUATION_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_ANALYSIS_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_REFRESH_TIME_STAMPS, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_GROUP_TEST, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_ADD_NEW_MEASUREMENT_SETUP, this);

		//
		this.add("menuSession");
		this.add("menuSessionNew");
		this.add("menuSessionClose");
		this.add("menuSessionOptions");
		this.add("menuSessionConnection");
		this.add("menuSessionChangePassword");
		this.add("menuSessionSave");
		this.add("menuSessionUndo");
		this.add("menuSessionDomain");
		this.add("menuExit");

		this.add(ScheduleMainMenuBar.MENU_VIEW);
		this.add(ScheduleMainMenuBar.MENU_VIEW_PLAN);
		this.add(ScheduleMainMenuBar.MENU_VIEW_TREE);
		this.add(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS);
		this.add(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS);
		this.add(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES);
		this.add(ScheduleMainMenuBar.MENU_VIEW_TIME);
		this.add(ScheduleMainMenuBar.MENU_VIEW_TABLE);
		this.add(ApplicationModel.MENU_VIEW_ARRANGE);

		this.add(ScheduleMainMenuBar.MENU_REPORT);
		this.add(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT);

		this.add(ApplicationModel.MENU_HELP);
		this.add(ApplicationModel.MENU_HELP_ABOUT);

		this.setVisible("menuSessionSave", false);
		this.setVisible("menuSessionUndo", false);
		this.setVisible("menuSessionOptions", false);

		this.setVisible(ScheduleMainMenuBar.MENU_VIEW, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_PLAN, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_TREE, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_TIME, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_TABLE, true);

		this.setVisible(ApplicationModel.MENU_HELP, true);
		this.setVisible(ApplicationModel.MENU_HELP_ABOUT, true);

	}

	/**
	 * @return tests
	 */
	public Set<Identifier> getTestIds() {
		return this.testIds;
	}

	// /**
	// * @return Returns the treeModel.
	// */
	// public ObjectResourceTreeModel getTreeModel() {
	// return this.treeModel;
	// }

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
			if (this.testIds != null)
				this.testIds.clear();

			try {
				this.refreshEditors();
			} catch (ApplicationException e) {
				AbstractMainFrame.showErrorMessage(Environment.getActiveWindow(), e);
			}
		} else if (propertyName.equals(COMMAND_SET_ANALYSIS_TYPE)) {
			this.analysisTypeId = (Identifier) evt.getNewValue();
			// this.generateTest();
		}
//		else if (propertyName.equals(COMMAND_SET_EVALUATION_TYPE)) {
//			this.evaluationTypeId = (Identifier) evt.getNewValue();
//			// this.generateTest();
//		} 
		else if (propertyName.equals(COMMAND_SET_MEASUREMENT_TYPE)) {
			this.measurementType = (MeasurementType) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_MONITORED_ELEMENT)) {
			this.monitoredElement = (MonitoredElement) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_SET)) {
			this.set = (ParameterSet) evt.getNewValue();
			// this.generateTest();
		} else if (propertyName.equals(COMMAND_SET_MEASUREMENT_SETUP)) {
			this.measurementSetup = (MeasurementSetup) evt.getNewValue();
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
			if (this.measurementSetupIdMap != null) {
				this.measurementSetupIdMap.clear();
			}
			this.refreshMeasurementSetups();
			this.refreshTests();
		}
	}

	public void removeTest(Test test) {
		
		int status = test.getStatus().value();
		if (status == TestStatus._TEST_STATUS_COMPLETED ||
				status == TestStatus._TEST_STATUS_ABORTED) {
			return;
		}
		
		Identifier groupTestId = test.getGroupTestId();
		if (groupTestId != null && !groupTestId.isVoid()) {
			try {
				Set testsByCondition = StorableObjectPool.getStorableObjectsByCondition(
					new LinkedIdsCondition(groupTestId, ObjectEntities.TEST_CODE), true, true);
				Set<Identifier> testIds1 = new HashSet<Identifier>(testsByCondition.size());
				for (Iterator iterator = testsByCondition.iterator(); iterator.hasNext();) {
					Identifier testId = ((Test) iterator.next()).getId();
					testIds1.add(testId);
					this.testIds.remove(testId);
				}
				StorableObjectPool.delete(testIds1);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Identifier testId = test.getId();
			this.testIds.remove(testId);
			StorableObjectPool.delete(testId);
		}
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
			this.selectedFirstTestId = null;
		}
		this.refreshTests();
	}

	private void refreshEditors() throws ApplicationException {
		// Collection temporalPatterns =
		// MeasurementStorableObjectPool.getStorableObjectsByCondition(
		// new
		// EquivalentCondition(ObjectEntities.CRONTEMPORALPATTERN_CODE),
		// true);
		// this.testTemporalStampsEditor.setTemporalPatterns(temporalPatterns);

		{
			final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_TYPE_CODE);
			final Set<MeasurementType> measurementTypes = StorableObjectPool.getStorableObjectsByCondition(ec, true);

			// LinkedIdsCondition domainCondition = new
			// LinkedIdsCondition(sessionInterface.getDomainIdentifier(),
			// ObjectEntities.ME_ENTITY_CODE);
			//
			// Collection monitoredElements =
			// ConfigurationStorableObjectPool.getStorableObjectsByCondition(
			// domainCondition, true);

			final Collection<IconPopulatableItem> measurementTypeItems = new ArrayList<IconPopulatableItem>(measurementTypes.size());

			final MeasurementTypeChildrenFactory childrenFactory = new MeasurementTypeChildrenFactory(LoginManager.getDomainId());
			for (Iterator iter = measurementTypes.iterator(); iter.hasNext();) {
				MeasurementType measurementType1 = (MeasurementType) iter.next();
				IconPopulatableItem measurementTypeItem = new IconPopulatableItem();
				measurementTypeItem.setChildrenFactory(childrenFactory);
				measurementTypeItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
				Log.debugMessage("SchedulerModel.refreshEditors | measurementType1: " + measurementType1.getName(), Log.DEBUGLEVEL07);
				measurementTypeItem.setName(measurementType1.getName());
				measurementTypeItem.setObject(measurementType1.getId());
				measurementTypeItems.add(measurementTypeItem);
			}

			// this.elementsViewer.setElements(measurementTypeItems);
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MEASUREMENT_TYPES, null, measurementTypeItems));

			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
					COMMAND_SET_ANALYSIS_TYPES,
					null,
					StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.ANALYSIS_TYPE_CODE), true, true)));

//			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
//					COMMAND_SET_EVALUATION_TYPES,
//					null,
//					StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.EVALUATION_TYPE_CODE),
//							true,
//							true)));

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
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
					COMMAND_SET_MEASUREMENT_TYPE,
					null,
					StorableObjectPool.getStorableObject(test.getMeasurementTypeId(), true)));
			MonitoredElement monitoredElement1 = test.getMonitoredElement();
			// MeasurementPort measurementPort = (MeasurementPort)
			// ConfigurationStorableObjectPool.getStorableObject(
			// monitoredElement.getMeasurementPortId(), true);
			// this.kisEditor.setKIS((KIS)
			// ConfigurationStorableObjectPool.getStorableObject(measurementPort.getKISId(),
			// true));
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MONITORED_ELEMENT, null, monitoredElement1));

			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_ANALYSIS_TYPE, this, test.getAnalysisTypeId()));
//			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
//					COMMAND_SET_EVALUATION_TYPE,
//					null,
//					test.getEvaluationTypeId()));

			final Set<Identifier> measurementSetupIds = test.getMeasurementSetupIds();
			if (!measurementSetupIds.isEmpty()) {
				final Identifier mainMeasurementSetupId = measurementSetupIds.iterator().next();
				final MeasurementSetup measurementSetup1 = (MeasurementSetup) StorableObjectPool.getStorableObject(mainMeasurementSetupId,
						true);
				if (measurementSetup1 != null) {
					// this.refreshMeasurementSetups();
					// this.dispatcher.firePropertyChange(new
					// PropertyChangeEvent(this, COMMAND_SET_SET, null,
					// measurementSetup1.getParameterSet()));
					// if (this.setEditor != null) {
					// this.setEditor.setSet(measurementSetup1.getParameterSet());
					// }
					// this.measurementSetupEditor.setMeasurementSetup(measurementSetup1);
					this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MEASUREMENT_SETUP,
																				null, measurementSetup1));
				}
			}

			if (test.getGroupTestId().isVoid()) {
				this.refreshTemporalStamps();
			} else {
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_START_GROUP_TIME, null, test.getStartTime()));
			}

		}
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TEST, null, null));
	}

	private void refreshTemporalStamps() {
		final Test test = this.getSelectedTest();

		if (test != null) {
			final Identifier temporalPatternId = test.getTemporalPatternId();
			AbstractTemporalPattern temporalPattern = null;
			if (temporalPatternId != null) {
				try {
					temporalPattern = (AbstractTemporalPattern) StorableObjectPool.getStorableObject(temporalPatternId,
						true);
				} catch (ApplicationException e) {
					Log.errorException(e);
				}
			}
			final TestTemporalStamps timeStamps = new TestTemporalStamps(test.getTemporalType(),
					test.getStartTime(),
					test.getEndTime(),
					temporalPattern);
			// this.testTemporalStampsEditor.setTestTemporalStamps(timeStamps);
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_TEMPORAL_STAMPS, null, timeStamps));
		}

	}

	private void refreshTests() {
		// for (int i = 0; i < this.testsEditors.length; i++) {
		// this.testsEditors[i].updateTests();
		// }

		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TESTS, null, null));
		try {
			this.refreshTest();
		} catch (ApplicationException e) {
			AbstractMainFrame.showErrorMessage(Environment.getActiveWindow(), e);
		}
	}

	// /**
	// * @param treeModel
	// * The treeModel to set.
	// */
	// public void setTreeModel(ObjectResourceTreeModel treeModel) {
	// this.treeModel = treeModel;
	// }

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
//		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
//			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_EVALUATION_TYPE, null, null));
//		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.set = null;
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_SET, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.measurementSetup = null;
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_MEASUREMENT_SETUP, null, null));
		}
//		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
//			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_RETURN_TYPE, null, null));
//		}
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
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.generateTest();
		}
	}

	public void setBreakData() {
		this.flag = 0;
	}

	public void updateTests(final long startTime, final long endTime) throws ApplicationException {
		// Environment.log(Environment.LOG_LEVEL_INFO, "updateTests",
		// getClass().getName()); //$NON-NLS-1$
		// this.setCursor(UIStorage.WAIT_CURSOR);
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelSchedule.getString("StatusMessage.UpdatingTests"))); //$NON-NLS-1$

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, true));

		if (this.measurementSetupIdMap != null) {
			this.measurementSetupIdMap.clear();
		}
		StorableObjectPool.refresh();

		final Date startDate = new Date(startTime);
		final Date endDate = new Date(endTime);

		TypicalCondition startTypicalCondition = new TypicalCondition(startDate,
				endDate,
				OperationSort.OPERATION_IN_RANGE,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_START_TIME);
		TypicalCondition endTypicalCondition = new TypicalCondition(startDate,
				endDate,
				OperationSort.OPERATION_IN_RANGE,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_END_TIME);
		TypicalCondition startTypicalCondition1 = new TypicalCondition(startDate,
				null,
				OperationSort.OPERATION_LESS_EQUALS,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_START_TIME);
		TypicalCondition endTypicalCondition2 = new TypicalCondition(endDate,
				null,
				OperationSort.OPERATION_GREAT_EQUALS,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_END_TIME);

		CompoundCondition compoundCondition1 = new CompoundCondition(startTypicalCondition,
				CompoundConditionSort.OR,
				endTypicalCondition);

		CompoundCondition compoundCondition2 = new CompoundCondition(startTypicalCondition1,
				CompoundConditionSort.AND,
				endTypicalCondition2);

		CompoundCondition compoundCondition = new CompoundCondition(compoundCondition1, CompoundConditionSort.OR, compoundCondition2);

		final Set<Test> tests = StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true, true);
		this.testIds.clear();
		for (final Test test : tests) {
			this.testIds.add(test.getId());
			System.out.println("SchedulerModel.updateTests() | " + test.getId() + ", " + test.getVersion() + "v, " + test.getStartTime() + ", " + test.getStatus().value());
		}

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelSchedule.getString("StatusMessage.TestsUpdated"))); //$NON-NLS-1$
		this.refreshTests();
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, false));
	}

	public Set<Identifier> getSelectedTestIds() {
		return this.selectedTestIds;
	}

	public Test getSelectedTest() {
		try {
			return this.selectedFirstTestId != null
					? (Test) StorableObjectPool.getStorableObject(this.selectedFirstTestId, true)
						: null;
		} catch (ApplicationException e) {
			return null;
		}
	}

	public void addSelectedTest(final Test selectedTest) throws ApplicationException {
		final Identifier selectedTestId = selectedTest.getId();
		if (this.selectedTestIds == null) {
			this.selectedTestIds = new HashSet<Identifier>();
		}

		if (selectedTest != null) {
			if (this.selectedTestIds.isEmpty()) {
				this.selectedFirstTestId = selectedTest.getId();
			}
			this.selectedTestIds.add(selectedTestId);
			this.refreshTest();
			// this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
			// COMMAND_REFRESH_TEST, null, null));
		} else {
			Log.debugMessage("SchedulerModel.setSelectedTest | selectedTest is " + selectedTest, Level.FINEST);
		}
	}

	public void unselectTests() {
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
		}
		this.selectedFirstTestId = null;
		this.measurementSetup = null;
		this.set = null;
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TEST, null, null));
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MEASUREMENT_SETUP,
			null, null));
		// this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
		// COMMAND_REFRESH_TEST, null, null));
	}

	public void setSelectedMeasurementType(final MeasurementType measurementType) {
		if (this.measurementType == null || measurementType == null
				|| !this.measurementType.getId().equals(measurementType.getId())) {
			this.measurementType = measurementType;
			this.refreshMeasurementSetups();
		}
	}

	public void setSelectedMonitoredElement(final MonitoredElement monitoredElement) {
		if (this.monitoredElement == null || monitoredElement == null
				|| !this.monitoredElement.getId().equals(monitoredElement.getId())) {
			this.monitoredElement = monitoredElement;
			this.refreshMeasurementSetups();
		}
	}

	public void setSelectedMonitoredElement(final MonitoredElement monitoredElement, final MeasurementType measurementType) {
		boolean changed = false;
		if (this.monitoredElement == null
				|| monitoredElement == null
				|| !this.monitoredElement.getId().equals(monitoredElement.getId())) {
			changed = true;
			this.monitoredElement = monitoredElement;
		}

		if (this.measurementType == null || measurementType == null || !this.measurementType.getId().equals(measurementType.getId())) {
			changed = true;
			this.measurementType = measurementType;
		}

		if (changed) {
			this.refreshMeasurementSetups();
		}
	}

	public void refreshMeasurementSetups() {
		// CommonUIUtilities.invokeAsynchronously(new Runnable() {
		//
		// public void run() {
		StorableObjectCondition condition = null;

		LinkedIdsCondition measurementTypeCondition = null;
		Identifier identifier = null;
		Set<Identifier> idSet = null;

		if (SchedulerModel.this.measurementType != null) {
			identifier = SchedulerModel.this.measurementType.getId();
			measurementTypeCondition = new LinkedIdsCondition(identifier, ObjectEntities.MEASUREMENTSETUP_CODE);
		}

		if (SchedulerModel.this.monitoredElement != null) {
			LinkedIdsCondition monitoredElementCondition = new LinkedIdsCondition(SchedulerModel.this.monitoredElement.getId(),
					ObjectEntities.MEASUREMENTSETUP_CODE);
			try {
				if (measurementTypeCondition != null) {
					idSet = new HashSet<Identifier>(2);
					idSet.add(identifier);
					identifier = SchedulerModel.this.monitoredElement.getId();
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
				return;
			}
		} else {
			if (identifier != null) {
				condition = measurementTypeCondition;
			}
		}

		try {
			if (condition != null) {
				final Set<Identifier> idSet1 = idSet;
				final StorableObjectCondition condition1 = condition;
				CommonUIUtilities.invokeAsynchronously(new Runnable() {

					public void run() {
						try {
							Set<Identifier> measurementSetupIds = SchedulerModel.this.measurementSetupIdMap != null
									? SchedulerModel.this.measurementSetupIdMap.get(idSet1)
										: null;
							Set<MeasurementSetup> measurementSetups = null;
							if (measurementSetupIds == null) {
								measurementSetups = StorableObjectPool.getStorableObjectsByCondition(condition1, true,
									true);
								if (SchedulerModel.this.measurementSetupIdMap == null) {
									SchedulerModel.this.measurementSetupIdMap = new HashMap<Set<Identifier>, Set<Identifier>>();
								}
								measurementSetupIds = new HashSet<Identifier>();
								for (final Identifiable identifiable : measurementSetups) {
									measurementSetupIds.add(identifiable.getId());
								}
								SchedulerModel.this.measurementSetupIdMap.put(idSet1, measurementSetupIds);
							} else {
								measurementSetups = StorableObjectPool.getStorableObjects(measurementSetupIds, true);
							}

//							for(final MeasurementSetup measurementSetup1 : measurementSetups) {
//								System.out.println("refreshMeasurementSetups | " + measurementSetup1.getId() + ", " + measurementSetup1.getDescription());
//							}

							SchedulerModel.this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
									COMMAND_SET_MEASUREMENT_SETUPS,
									null,
									measurementSetups));
						} catch (ApplicationException e) {
							AbstractMainFrame.showErrorMessage(Environment.getActiveWindow(), e);
						}

					}
				}, LangModelGeneral.getString("Message.Information.PlsWait"));
			}

			if (SchedulerModel.this.selectedTestIds != null && !SchedulerModel.this.selectedTestIds.isEmpty()) {
				final Collection<Identifier> measurementSetupIds = getSelectedTest().getMeasurementSetupIds();
				if (!measurementSetupIds.isEmpty()) {
					final Identifier mainMeasurementSetupId = measurementSetupIds.iterator().next();
					final MeasurementSetup measurementSetup1 = (MeasurementSetup) StorableObjectPool.getStorableObject(mainMeasurementSetupId,
							true);
					if (measurementSetup1 != null) {
						// SchedulerModel.this.dispatcher
						// .firePropertyChange(new PropertyChangeEvent(this,
						// COMMAND_SET_SET, null,
						// measurementSetup1.getParameterSet()));
						SchedulerModel.this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
								COMMAND_SET_MEASUREMENT_SETUP,
								null,
								measurementSetup1));

					}
				}
			}

		} catch (ApplicationException e) {
			AbstractMainFrame.showErrorMessage(Environment.getActiveWindow(), e);
		}
		// }
		// }, LangModelGeneral.getString("Message.Information.PlsWait"));
	}

	public void commitChanges() throws ApplicationException {
		StorableObjectPool.flush(ObjectEntities.TEST_CODE, LoginManager.getUserId(), true);
		if (this.meTestGroup != null) {
			this.meTestGroup.clear();
		}
		this.refreshMeasurementSetups();
		this.refreshTests();
		// this.refreshTest();
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TEST, null, null));
	}

	private void generateTest() {
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {

			Test test = null;
			test = (this.flag == FLAG_APPLY) ? this.getSelectedTest() : null;
			final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
			Set<Identifier> measurementSetupIds;
			if (this.measurementSetup == null) {
				if (this.set == null) {
					return;
				}
				try {
					this.measurementSetup = MeasurementSetup.createInstance(LoginManager.getUserId(),
							this.set,
							null,
							null,
							null,
							LangModelSchedule.getString("created by Scheduler") + " /" + sdf.format(new Date()) + "/",
							1000 * 60 * 10,
							Collections.singleton(this.monitoredElement.getId()),
							Collections.singleton(this.measurementType.getId()));
					if (this.measurementSetupIdMap != null) {
						this.measurementSetupIdMap.clear();
					}
				} catch (CreateObjectException e) {
					Log.debugException(e, Log.DEBUGLEVEL05);
				}
				this.refreshMeasurementSetups();

			}
			measurementSetupIds = Collections.singleton(this.measurementSetup.getId());

			final Date startTime = this.testTimeStamps.getStartTime();
			final Date endTime = this.testTimeStamps.getEndTime();
			final TestTemporalType temporalType = this.testTimeStamps.getTestTemporalType();
			final AbstractTemporalPattern temporalPattern = this.testTimeStamps.getTemporalPattern();
			if (test == null) {
				try {
					if (this.isValid(startTime, endTime, this.monitoredElement.getId())) {
						test = Test.createInstance(LoginManager.getUserId(),
								startTime,
								endTime,
								temporalPattern == null ? Identifier.VOID_IDENTIFIER : temporalPattern.getId(),
								temporalType,
								this.measurementType.getId(),
								this.analysisTypeId == null ? Identifier.VOID_IDENTIFIER : this.analysisTypeId,
//								this.evaluationTypeId == null ? 
										Identifier.VOID_IDENTIFIER 
//										: this.evaluationTypeId
										,
								Identifier.VOID_IDENTIFIER,
								this.monitoredElement,
								this.name != null && this.name.trim().length() > 0 ? this.name : sdf.format(startTime),
								measurementSetupIds);

						if (this.groupTest) {
							if (this.meTestGroup == null) {
								this.meTestGroup = new HashMap<Identifier, Identifier>();
							}
							final Identifier meId = this.monitoredElement.getId();
							Identifier testGroupId = this.meTestGroup.get(meId);
							if (testGroupId == null || testGroupId.isVoid()) {
								testGroupId = test.getId();
								this.meTestGroup.put(meId, testGroupId);
							}

							test.setGroupTestId(testGroupId);
						}

					} else {
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelSchedule.getString("Cannot add test"),
								LangModelSchedule.getString("Error"),
								JOptionPane.OK_OPTION);
					}
				} catch (CreateObjectException e) {
					Log.errorException(e);
				}
				this.testIds.add(test.getId());
			} else {
				if (this.isValid(startTime, endTime, this.monitoredElement.getId())) {
					test.setAttributes(test.getCreated(),
							new Date(System.currentTimeMillis()),
							test.getCreatorId(),
							LoginManager.getUserId(),
							test.getVersion(),
							temporalType.value(),
							startTime,
							endTime,
							temporalPattern == null ? Identifier.VOID_IDENTIFIER : temporalPattern.getId(),
							this.measurementType.getId(),
							this.analysisTypeId == null ? Identifier.VOID_IDENTIFIER : this.analysisTypeId,
							test.getGroupTestId(),
//							this.evaluationTypeId == null ? 
									Identifier.VOID_IDENTIFIER 
//									: this.evaluationTypeId
									,
							test.getStatus().value(),
							this.monitoredElement,
							this.name != null && this.name.trim().length() > 0 ? this.name : sdf.format(startTime),
							test.getNumberOfMeasurements());
				} else {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							LangModelSchedule.getString("Cannot update test"),
							LangModelSchedule.getString("Error"),
							JOptionPane.OK_OPTION);
				}
			}

			try {
				if (this.selectedTestIds != null) {
					this.selectedTestIds.clear();
				}
				this.addSelectedTest(test);
				// this.refreshTests();
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TESTS, null, null));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}

		}
	}

	public void addGroupTest(final Date date) {
		this.aloneGroupTest = true;
		this.startGroupDate = date;
		this.addGroupTests();
	}

	public void addGroupTests(final Date date, final long interval1) {
		this.aloneGroupTest = false;
		this.startGroupDate = date;
		this.interval = interval1;
		this.addGroupTests();
	}

	public void moveSelectedTests(final Date startDate) {
//		Log.debugMessage("SchedulerModel.moveSelectedTests | startDate is " + startDate, Level.FINEST);
//		final long t0 = System.currentTimeMillis();
		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
			try {
				final SortedSet<Test> selectedTests = new TreeSet<Test>(new WrapperComparator<Test>(TestWrapper.getInstance(),
						TestWrapper.COLUMN_START_TIME));
				final Set<Test> tests = StorableObjectPool.getStorableObjects(this.selectedTestIds, true);
				selectedTests.addAll(tests);

				final Test firstTest = selectedTests.first();
				final long offset = startDate.getTime() - firstTest.getStartTime().getTime();

				boolean correct = true;
				for (final Test selectedTest : selectedTests) {
					if (selectedTest.isChanged()) {
						final Date newStartDate = new Date(selectedTest.getStartTime().getTime() + offset);
						Date newEndDate = selectedTest.getEndTime();
						if (newEndDate != null) {
							newEndDate = new Date(newEndDate.getTime() + offset);
						}
						correct = this.isValid(newStartDate, newEndDate, selectedTest.getMonitoredElement().getId());
						if (!correct) {
							JOptionPane.showMessageDialog(Environment.getActiveWindow(),
									LangModelSchedule.getString("Cannot move tests"),
									LangModelSchedule.getString("Error"),
									JOptionPane.OK_OPTION);
							break;
						}
					}
				}

				if (correct) {
					boolean moved = false;
					for (final Test selectedTest : selectedTests) {
						if (selectedTest.isChanged()) {
							final Date newStartDate = new Date(selectedTest.getStartTime().getTime() + offset);
							Date newEndDate = selectedTest.getEndTime();
							if (newEndDate != null) {
								newEndDate = new Date(newEndDate.getTime() + offset);
							}
							selectedTest.setStartTime(newStartDate);
							selectedTest.setEndTime(newEndDate);
							moved = true;
						}
					}
					if (moved) {
						this.refreshTests();

						// XXX - regenerate time line !!!

						final Test selectedTest = this.getSelectedTest();
						if (selectedTest.getGroupTestId().isVoid()) {
							this.refreshTemporalStamps();
						} else {
							this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
									COMMAND_SET_START_GROUP_TIME,
									null,
									selectedTest.getStartTime()));
						}
					}
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}


//		final long t1 = System.currentTimeMillis();
//		Log.debugMessage("SchedulerModel.moveSelectedTests | time:" + (t1 - t0), Level.FINEST);
	}

	private void addGroupTests() {
		Log.debugMessage("SchedulerModel.addGroupTests | ", Level.FINEST);
		final Identifier meId = this.monitoredElement.getId();
		Identifier testGroupId = this.meTestGroup != null ? this.meTestGroup.get(meId) : null;
		if (testGroupId != null) {
			try {
				final Test testGroup = (Test) StorableObjectPool.getStorableObject(testGroupId, true);
				if (this.aloneGroupTest) {
					if (this.isValid(this.startGroupDate, null, testGroup.getMonitoredElement().getId())) {
						final Test test = Test.createInstance(LoginManager.getUserId(),
								this.startGroupDate,
								null,
								Identifier.VOID_IDENTIFIER,
								TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME,
								testGroup.getMeasurementTypeId(),
								testGroup.getAnalysisTypeId(),
								testGroup.getEvaluationTypeId(),
								testGroupId,
								testGroup.getMonitoredElement(),
								testGroup.getDescription(),
								testGroup.getMeasurementSetupIds());
						this.testIds.add(test.getId());
						if (this.selectedTestIds != null) {
							this.selectedTestIds.clear();
						} else {
							this.selectedTestIds = new HashSet<Identifier>();
						}
						this.selectedTestIds.add(test.getId());
					} else {
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelSchedule.getString("Cannot add tests"),
								LangModelSchedule.getString("Error"),
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

					final SortedSet<Test> selectedTests = new TreeSet<Test>(new WrapperComparator<Test>(TestWrapper.getInstance(),
							TestWrapper.COLUMN_START_TIME));
					final Set<Test> tests = StorableObjectPool.getStorableObjects(this.selectedTestIds, true);
					selectedTests.addAll(tests);
					final Test firstTest = selectedTests.first();
					final Test lastTest = selectedTests.last();

					final Date endTime = lastTest.getEndTime();
					final long firstTime = firstTest.getStartTime().getTime();
					final long offset = (endTime != null ? endTime : lastTest.getStartTime()).getTime() + this.interval - firstTime;

					assert Log.debugMessage("SchedulerModel.addGroupTests | firstTime is " + new Date(firstTime), Level.FINEST);
					assert Log.debugMessage("SchedulerModel.addGroupTests | offset is " + offset, Level.FINEST);

					this.selectedTestIds.clear();

					boolean correct = true;
					for (final Test selectedTest : selectedTests) {
						final Date startDate = new Date(selectedTest.getStartTime().getTime() + offset);
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
							JOptionPane.showMessageDialog(Environment.getActiveWindow(),
									LangModelSchedule.getString("Cannot add tests"),
									LangModelSchedule.getString("Error"),
									JOptionPane.OK_OPTION);
							break;
						}
					}

					if (correct) {
						for (Iterator iterator = selectedTests.iterator(); iterator.hasNext();) {
							final Test selectedTest = (Test) iterator.next();
							final Date startDate = new Date(selectedTest.getStartTime().getTime() + offset);
							Date endDate = selectedTest.getEndTime();
							if (endDate != null) {
								endDate = new Date(endDate.getTime() + offset);
							}

							// Log.debugMessage("SchedulerModel.addGroupTests
							// | new startDate " + startDate, Log.FINEST);
							// Log.debugMessage("SchedulerModel.addGroupTests
							// | new endDate " + endDate, Log.FINEST);
							final Test test = Test.createInstance(LoginManager.getUserId(),
									startDate,
									endDate,
									selectedTest.getTemporalPatternId(),
									selectedTest.getTemporalType(),
									selectedTest.getMeasurementTypeId(),
									selectedTest.getAnalysisTypeId(),
									selectedTest.getEvaluationTypeId(),
									testGroupId,
									selectedTest.getMonitoredElement(),
									selectedTest.getDescription(),
									selectedTest.getMeasurementSetupIds());
							final Identifier testId = test.getId();
							if (testGroupId == null || testGroupId.isVoid()) {
								testGroupId = testId;
								test.setGroupTestId(testGroupId);
							}
							this.testIds.add(testId.getId());
							this.selectedTestIds.add(testId);
							assert Log.debugMessage("SchedulerModel.addGroupTests | add test " + test.getId()
									+ " at " + startDate + "," + endDate, Level.FINEST);
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
//		Log.debugMessage("SchedulerModel.isValid | ", Level.FINEST);
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(this.testIds, true);
			for (final Test test : tests) {
				final Date startTime = test.getStartTime();
				Date endTime = test.getEndTime();
				if (endTime == null) {
					endTime = startTime;
				}
//				Log.debugMessage("SchedulerModel.isValid | startDate " + startDate + ", endDate " + endDate, Level.FINEST);

//				Log.debugMessage("SchedulerModel.isValid | startTime " + startTime + ", endTime " + endTime, Level.FINEST);
				if (test.getMonitoredElement().getId().equals(monitoredElementId)
						&& ((endDate != null && endDate.after(startTime) && endDate.before(endTime)) || (startDate.after(startTime) && startDate.before(endTime)))) {
					result = false;
					break;
				}
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Log.debugMessage("SchedulerModel.isValid | result is " + result, Level.FINEST);
		return result;
	}

	public static Color getColor(final TestStatus testStatus) {
		return getColor(testStatus, false);
	}

	public static Color getColor(final TestStatus testStatus, final boolean selected) {
		Color color;
		switch (testStatus.value()) {
			case TestStatus._TEST_STATUS_COMPLETED:
				color = selected ? COLOR_COMPLETED_SELECTED : COLOR_COMPLETED;
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

}
