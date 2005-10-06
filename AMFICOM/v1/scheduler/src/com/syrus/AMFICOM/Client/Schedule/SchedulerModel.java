/*-
 * $Id: SchedulerModel.java,v 1.117 2005/10/06 13:18:02 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.logic.IconPopulatableItem;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @version $Revision: 1.117 $, $Date: 2005/10/06 13:18:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public final class SchedulerModel extends ApplicationModel implements PropertyChangeListener {

	public static final String	COMMAND_CHANGE_KIS					= "ChangeKIS";
	public static final String	COMMAND_CHANGE_ME_TYPE				= "ChangeMEType";

	public static final String	COMMAND_CLEAN						= "Clean";

	private static final int	FLAG_APPLY							= 1 << 1;
	private static final int	FLAG_CREATE							= 1 << 2;
	Dispatcher					dispatcher;

	private int					flag								= 0;

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

	public static final String	COMMAND_GET_MEASUREMENT_SETUP		= "GetMeasurementSetup";
	public static final String	COMMAND_SET_MEASUREMENT_SETUP		= "SetMeasurementSetup";
	public static final String	COMMAND_SET_MEASUREMENT_SETUPS		= "SetMeasurementSetups";

	public static final String	COMMAND_GET_TEMPORAL_STAMPS			= "GetTestTemporalStamps";
	public static final String	COMMAND_SET_TEMPORAL_STAMPS			= "SetTestTemporalStamps";

	public static final String	COMMAND_REFRESH_TEST				= "RefreshTest";
	public static final String	COMMAND_REFRESH_TESTS				= "RefreshTests";
	
	public static final String	COMMAND_REFRESH_TEMPORAL_STAMPS		= "RefreshTemporalStamps";
	public static final String	COMMAND_REFRESH_MEASUREMENT_SETUP	= "RefreshMeasurementSetup";

	public static final String	COMMAND_SET_GROUP_TEST				= "GroupTest";
	public static final String	COMMAND_SET_START_GROUP_TIME		= "SetStartGroupTime";

	public static final String	COMMAND_DATE_OPERATION				= "DateOperation";

	private MeasurementType				measurementType						= MeasurementType.UNKNOWN;

	private String				name								= null;
	private MonitoredElement			monitoredElement					= null;
	private AnalysisType		analysisType						= AnalysisType.UNKNOWN;
	private MeasurementSetup	measurementSetup					= null;
	private TestTemporalStamps	testTimeStamps						= null;

	private Date				startGroupDate;
	private long				interval;
	private boolean				aloneGroupTest;

	private Map<Identifier, Identifier>					meTestGroup;

	private boolean				groupTest							= false;
	private Test	selectedFirstTest;

	public static final Color	COLOR_STOPPED						= Color.MAGENTA.darker();

	public static final Color	COLOR_STOPPED_SELECTED				= Color.MAGENTA;
	
	public static final Color	COLOR_ABORDED						= Color.RED.darker();

	public static final Color	COLOR_ABORDED_SELECTED				= Color.RED;

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

	public SchedulerModel(final ApplicationContext aContext) {
		this.dispatcher = aContext.getDispatcher();

		this.dispatcher.addPropertyChangeListener(COMMAND_CLEAN, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_NAME, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_TEMPORAL_STAMPS, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_MONITORED_ELEMENT, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_MEASUREMENT_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_MEASUREMENT_SETUP, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_ANALYSIS_TYPE, this);
		this.dispatcher.addPropertyChangeListener(COMMAND_SET_GROUP_TEST, this);

		//
		this.add(MENU_SESSION);
		this.add(MENU_SESSION_NEW);
		this.add(MENU_SESSION_CLOSE);
		this.add(MENU_SESSION_OPTIONS);
		this.add(MENU_SESSION_CHANGE_PASSWORD);
		this.add(MENU_SESSION_DOMAIN);
		this.add(MENU_EXIT);

		this.add(ScheduleMainMenuBar.MENU_VIEW);
		this.add(ScheduleMainMenuBar.MENU_VIEW_PLAN);
		this.add(ScheduleMainMenuBar.MENU_VIEW_TREE);
		this.add(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS);
		this.add(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES);
		this.add(ScheduleMainMenuBar.MENU_VIEW_TIME);
		this.add(ScheduleMainMenuBar.MENU_VIEW_TABLE);
		this.add(ApplicationModel.MENU_VIEW_ARRANGE);

		this.add(ScheduleMainMenuBar.MENU_REPORT);
		this.add(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT);

		this.add(ApplicationModel.MENU_HELP);
		this.add(ApplicationModel.MENU_HELP_ABOUT);

		this.setVisible(ScheduleMainMenuBar.MENU_VIEW, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_PLAN, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_TREE, true);
		this.setVisible(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, true);
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

	public void propertyChange(final PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if (propertyName.equals(COMMAND_CLEAN)) {
			if (this.testIds != null) {
				this.testIds.clear();
			}
			this.refreshEditors();
		} else if (propertyName.equals(COMMAND_SET_ANALYSIS_TYPE)) {
			this.analysisType = (AnalysisType) evt.getNewValue();
		} else if (propertyName.equals(COMMAND_SET_MEASUREMENT_TYPE)) {
			this.setSelectedMeasurementType((MeasurementType) evt.getNewValue());
		} else if (propertyName.equals(COMMAND_SET_MONITORED_ELEMENT)) {
			this.setSelectedMonitoredElement((MonitoredElement) evt.getNewValue());
		} 
//		else if (propertyName.equals(COMMAND_SET_SET)) {
//			this.set = (ParameterSet) evt.getNewValue();
//		} 
		else if (propertyName.equals(COMMAND_SET_MEASUREMENT_SETUP)) {
			this.measurementSetup = (MeasurementSetup) evt.getNewValue();
		} else if (propertyName.equals(COMMAND_SET_TEMPORAL_STAMPS)) {
			this.testTimeStamps = (TestTemporalStamps) evt.getNewValue();
		} else if (propertyName.equals(COMMAND_SET_NAME)) {
			this.name = (String) evt.getNewValue();
		} else if (propertyName.equals(COMMAND_SET_GROUP_TEST)) {
			this.groupTest = true;
		} 
	}

	public void removeTest(final Test test) throws ApplicationException {
		
		int status = test.getStatus().value();
		if (status == TestStatus._TEST_STATUS_COMPLETED ||
				status == TestStatus._TEST_STATUS_ABORTED) {
			return;
		}
		
		final Identifier groupTestId = test.getGroupTestId();
		if (groupTestId != null && !groupTestId.isVoid()) {
				try {
					final Set<Test> testsByCondition = StorableObjectPool.getStorableObjectsByCondition(
						new LinkedIdsCondition(groupTestId, ObjectEntities.TEST_CODE), true, true);
					final Set<Identifier> testIdsToRemove = Identifier.createIdentifiers(testsByCondition);
					this.testIds.removeAll(testIdsToRemove);
					StorableObjectPool.delete(testIdsToRemove);
				} catch (final ApplicationException e) {
					throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
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

	private void refreshEditors() {
		final MeasurementType[] measurementTypes = MeasurementType.values();
		Arrays.sort(measurementTypes, new Comparator<MeasurementType>() {
			public int compare(	MeasurementType o1,
								MeasurementType o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});
		final Collection<IconPopulatableItem> measurementTypeItems = new ArrayList<IconPopulatableItem>(measurementTypes.length);

		final MeasurementTypeChildrenFactory childrenFactory = new MeasurementTypeChildrenFactory(LoginManager.getDomainId());
		
		for(final MeasurementType measurementType1 : measurementTypes) {
			if (measurementType1.getDescription().trim().length() == 0) {
				continue;
			}
			IconPopulatableItem measurementTypeItem = new IconPopulatableItem();
			measurementTypeItem.setChildrenFactory(childrenFactory);
			measurementTypeItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
			measurementTypeItem.setName(measurementType1.getDescription());
			measurementTypeItem.setObject(measurementType1);
			measurementTypeItems.add(measurementTypeItem);
		}

		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_SET_MEASUREMENT_TYPES, null, measurementTypeItems));

		this.refreshMeasurementSetups();
	}

	private void refreshTest(final Object source) {
		if (this.selectedFirstTest != null) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(source,
					COMMAND_SET_MEASUREMENT_TYPE,
					null,
					this.selectedFirstTest.getMeasurementType()));
			MonitoredElement monitoredElement1 = this.selectedFirstTest.getMonitoredElement();
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(source, COMMAND_SET_MONITORED_ELEMENT, null, monitoredElement1));

			this.dispatcher.firePropertyChange(new PropertyChangeEvent(source, COMMAND_SET_ANALYSIS_TYPE, this, this.selectedFirstTest.getAnalysisType()));
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(source, COMMAND_REFRESH_TEMPORAL_STAMPS, null, null));
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(source, COMMAND_REFRESH_MEASUREMENT_SETUP, null, null));

		}
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(source, COMMAND_REFRESH_TEST, null, null));
	}

	private void refreshTemporalStamps() {
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TEMPORAL_STAMPS, null, null));
	}

	private void refreshTests() {
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TESTS, null, null));
		this.refreshTest(this);
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
		this.groupTest = false;
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_MEASUREMENT_TYPE, null, null));
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_MONITORED_ELEMENT, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_ANALYSIS_TYPE, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.measurementSetup = null;
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_MEASUREMENT_SETUP, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_TEMPORAL_STAMPS, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_NAME, null, null));
		}
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
			this.generateTest();
		}
	}

	public void setBreakData() {
		this.flag = 0;
	}
	
	public void updateTests(final long startTime, final long endTime) throws ApplicationException {
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
				StatusMessageEvent.STATUS_MESSAGE,
				I18N.getString("Scheduler.StatusMessage.UpdatingTests"))); //$NON-NLS-1$

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, true));

		if (this.measurementSetupIdMap != null) {
			this.measurementSetupIdMap.clear();
		}
		try {
			StorableObjectPool.refresh();

			final Date startDate = new Date(startTime);
			final Date endDate = new Date(endTime);
			
			final TypicalCondition startTypicalCondition = new TypicalCondition(startDate,
					endDate,
					OperationSort.OPERATION_IN_RANGE,
					ObjectEntities.TEST_CODE,
					TestWrapper.COLUMN_START_TIME);
			final TypicalCondition endTypicalCondition = new TypicalCondition(startDate,
					endDate,
					OperationSort.OPERATION_IN_RANGE,
					ObjectEntities.TEST_CODE,
					TestWrapper.COLUMN_END_TIME);
			final TypicalCondition startTypicalCondition1 = new TypicalCondition(startDate,
					null,
					OperationSort.OPERATION_LESS_EQUALS,
					ObjectEntities.TEST_CODE,
					TestWrapper.COLUMN_START_TIME);
			final TypicalCondition endTypicalCondition2 = new TypicalCondition(endDate,
					null,
					OperationSort.OPERATION_GREAT_EQUALS,
					ObjectEntities.TEST_CODE,
					TestWrapper.COLUMN_END_TIME);
			
			// XXX rebuld using a <= d & c <= b
			// [a,b] intersect [c,d]

			final CompoundCondition compoundCondition1 = 
				new CompoundCondition(startTypicalCondition,
					CompoundConditionSort.OR,
					endTypicalCondition);

			final CompoundCondition compoundCondition2 = 
				new CompoundCondition(startTypicalCondition1,
					CompoundConditionSort.AND,
					endTypicalCondition2);

			final CompoundCondition compoundCondition = 
				new CompoundCondition(compoundCondition1, 
					CompoundConditionSort.OR, 
					compoundCondition2);

			this.testIds.clear();
			this.testIds.addAll(Identifier.createIdentifiers(StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true, true)));
		} catch (final ApplicationException e) {
			throw new ApplicationException(I18N.getString("Scheduler.Error.CannotRefreshTests"));
		}

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
				StatusMessageEvent.STATUS_MESSAGE,
				I18N.getString("Scheduler.StatusMessage.TestsUpdated"))); //$NON-NLS-1$
		this.refreshTests();
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, false));
	}

	public Set<Identifier> getSelectedTestIds() {
		if (this.selectedTestIds != null) {
			return this.selectedTestIds;
		}
		return Collections.emptySet();
	}
	
	public Set<Test> getSelectedTests() throws ApplicationException{
		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) { 
			return StorableObjectPool.getStorableObjects(this.selectedTestIds, true);
		}
		return Collections.emptySet();
	}	

	public Test getSelectedTest() throws ApplicationException {
		try {
			return this.selectedFirstTestId != null
					? (Test) StorableObjectPool.getStorableObject(this.selectedFirstTestId, true)
						: null;
		} catch (final ApplicationException e) {
			throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
		}
	}

	public void addSelectedTest(final Object source, final Test selectedTest) {
		final Identifier selectedTestId = selectedTest.getId();
		synchronized (this) {
			if (this.selectedTestIds == null) {
				this.selectedTestIds = new HashSet<Identifier>();
			}
			if (selectedTest != null) {
				if (this.selectedTestIds.isEmpty()) {
					this.selectedFirstTest = selectedTest;
					this.selectedFirstTestId = selectedTest.getId();
				}
				if (!this.selectedTestIds.contains(selectedTestId)) {
					this.selectedTestIds.add(selectedTestId);
					this.refreshTest(source);
				}
			} else {
				Log.debugMessage("SchedulerModel.setSelectedTest | selectedTest is " + selectedTest, Level.FINEST);
			}
		}
	}

	public void unselectTests(final Object source) {
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
		}
		this.selectedFirstTestId = null;
		this.measurementSetup = null;
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(source, COMMAND_REFRESH_TEST, null, null));
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(source, COMMAND_SET_MEASUREMENT_SETUP, null, null));
	}

	public void setSelectedMeasurementType(final MeasurementType measurementType) {
		assert Log.debugMessage("SchedulerModel.setSelectedMeasurementType | " + this.measurementType + " > " + measurementType,
			Log.DEBUGLEVEL10);
		if (this.measurementType == null || measurementType == null
				|| !this.measurementType.equals(measurementType)) {
			this.measurementType = measurementType;
			this.refreshMeasurementSetups();
		}
	}

	public void setSelectedMonitoredElement(final MonitoredElement monitoredElement) {
		assert Log.debugMessage("SchedulerModel.setSelectedMonitoredElement | " + this.monitoredElement + " > " + monitoredElement,
			Log.DEBUGLEVEL10);
		if (this.monitoredElement == null || monitoredElement == null
				|| !this.monitoredElement.equals(monitoredElement)) {
			this.monitoredElement = monitoredElement;
			this.refreshMeasurementSetups();
		}
	}

	public void setSelectedMonitoredElement(final MonitoredElement monitoredElement, final MeasurementType measurementType) {
		assert Log.debugMessage("SchedulerModel.setSelectedMonitoredElement | " + this.monitoredElement + " > " + monitoredElement
			+ "\n\t\t " + this.measurementType + " > " + measurementType,
			Log.DEBUGLEVEL10);
		boolean changed = false;
		if (this.monitoredElement == null
				|| monitoredElement == null
				|| !this.monitoredElement.equals(monitoredElement)) {
			changed = true;
			this.monitoredElement = monitoredElement;
		}

		if (this.measurementType == null || measurementType == null || !this.measurementType.equals(measurementType)) {
			changed = true;
			this.measurementType = measurementType;
		}

		if (changed) {
			this.refreshMeasurementSetups();
		}
	}

	public void refreshMeasurementSetups() {
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
			COMMAND_SET_MEASUREMENT_SETUPS,
			null,
			null));
	}
	
	public Set<MeasurementSetup> getMeasurementSetups() throws ApplicationException{
		StorableObjectCondition condition = null;

		TypicalCondition measurementTypeCondition = null;
		Identifier identifier = null;
		Set<Identifier> idSet = null;

		if (this.measurementType != null) {
			measurementTypeCondition = new TypicalCondition(this.measurementType,
				OperationSort.OPERATION_IN,
				ObjectEntities.MEASUREMENTSETUP_CODE,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE);
		}

		if (this.monitoredElement != null) {
			LinkedIdsCondition monitoredElementCondition = new LinkedIdsCondition(this.monitoredElement.getId(),
					ObjectEntities.MEASUREMENTSETUP_CODE);
			if (measurementTypeCondition != null) {
				idSet = new HashSet<Identifier>(2);
				idSet.add(identifier);
				identifier = this.monitoredElement.getId();
				idSet.add(identifier);
				condition = new CompoundCondition(measurementTypeCondition, CompoundConditionSort.AND,
													monitoredElementCondition);
			} else {
				idSet = Collections.singleton(identifier);
				condition = monitoredElementCondition;
			}
		} else {
			if (identifier != null) {
				condition = measurementTypeCondition;
			}
		}

		if (condition != null) {
			final Set<Identifier> idSet1 = idSet;
			final StorableObjectCondition condition1 = condition;
				
			try {
				Set<Identifier> measurementSetupIds = this.measurementSetupIdMap != null
						? this.measurementSetupIdMap.get(idSet1)
							: null;
				Set<MeasurementSetup> measurementSetups = null;
				if (measurementSetupIds == null) {
					measurementSetups = StorableObjectPool.getStorableObjectsByCondition(condition1, true,
						true);
					if (this.measurementSetupIdMap == null) {
						this.measurementSetupIdMap = new HashMap<Set<Identifier>, Set<Identifier>>();
					}
					measurementSetupIds = new HashSet<Identifier>();
					for (final Identifiable identifiable : measurementSetups) {
						measurementSetupIds.add(identifiable.getId());
					}
					this.measurementSetupIdMap.put(idSet1, measurementSetupIds);
				} else {
					measurementSetups = StorableObjectPool.getStorableObjects(measurementSetupIds, true);
				}
				return measurementSetups;
			} catch (final ApplicationException e) {
				throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
			}				
		}

		return Collections.emptySet();
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
	
	public MeasurementSetup createMeasurementSetup(final ParameterSet parameterSet,
			final String description) 
	throws CreateObjectException {
		final MeasurementSetup measurementSetup = 
			MeasurementSetup.createInstance(LoginManager.getUserId(),
				parameterSet,
				null,
				null,
				null,
				description,				
				0L,  
				Collections.singleton(this.monitoredElement.getId()),
				EnumSet.of(this.measurementType));
		if (this.measurementSetupIdMap != null) {
			this.measurementSetupIdMap.clear();
		}
		
		return measurementSetup;
	}
	
	public void changeMeasurementSetup(final MeasurementSetup measurementSetup) 
	throws ApplicationException {
		final Set<Identifier> measurementSetupIdSet = Collections.singleton(measurementSetup.getId());
		final Set<Test> tests = getSelectedTests();
		
		for (final Test test : tests) {
			if (test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
				boolean canBeApplied = this.isValid(test, measurementSetup);
				if (!canBeApplied) {
					this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_MEASUREMENT_SETUP, null, null));
					throw new IllegalDataException(I18N.getString("Scheduler.Error.CannotApplyMeasurementSetup"));
				}
			}
		}							
		
		for (final Test test : tests) {
			if (test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
				test.setMeasurementSetupIds(measurementSetupIdSet);
			}
		}
		
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_REFRESH_TESTS, null, null));
	}

	private void generateTest() throws ApplicationException {
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {

			Test test = null;
			test = (this.flag == FLAG_APPLY) ? this.getSelectedTest() : null;
			final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
			
			final Set<Identifier> measurementSetupIds = Collections.singleton(this.measurementSetup.getId());

			final Date startTime = this.testTimeStamps.getStartTime();
			final Date endTime = this.testTimeStamps.getEndTime() != null ?
					this.testTimeStamps.getEndTime() : this.testTimeStamps.getStartTime();
					
			final TestTemporalType temporalType = this.testTimeStamps.getTestTemporalType();
			final AbstractTemporalPattern temporalPattern = this.testTimeStamps.getTemporalPattern();
			if (test == null) {
//				if (this.isValid(startTime, new Date(endTime.getTime() + this.measurementSetup.getMeasurementDuration()), this.monitoredElement.getId())) {
				if (this.isValid(this.monitoredElement.getId(), startTime, endTime, temporalPattern, this.measurementSetup)) {
					try {
						test = Test.createInstance(LoginManager.getUserId(),
								startTime,
								endTime,
								temporalPattern == null ? Identifier.VOID_IDENTIFIER : temporalPattern.getId(),
								temporalType,
								this.measurementType,
								this.analysisType,
								Identifier.VOID_IDENTIFIER,
								this.monitoredElement,
								this.name != null && this.name.trim().length() > 0 ? this.name : sdf.format(startTime),
								measurementSetupIds);						
					} catch (final CreateObjectException e) {
						throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest"));
					}

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
					this.testIds.add(test.getId());
				} else {
					throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest") 
						+ ':'
						+ I18N.getString("Scheduler.Error.AddingTestIntersectWithOtherTest"));
				} 
				
			} else {
//				if (this.isValid(startTime, new Date(endTime.getTime() + this.measurementSetup.getMeasurementDuration()), this.monitoredElement.getId())) {
				if (this.isValid(this.monitoredElement.getId(), startTime, endTime, temporalPattern, this.measurementSetup)) {
					test.setAttributes(test.getCreated(),
							new Date(System.currentTimeMillis()),
							test.getCreatorId(),
							LoginManager.getUserId(),
							test.getVersion(),
							temporalType.value(),
							startTime,
							endTime,
							temporalPattern == null ? Identifier.VOID_IDENTIFIER : temporalPattern.getId(),
							this.measurementType,
							this.analysisType,
							test.getGroupTestId(),
							test.getStatus().value(),
							this.monitoredElement,
							this.name != null && this.name.trim().length() > 0 ? this.name : sdf.format(startTime),
							test.getNumberOfMeasurements());
				} else {
					throw new ApplicationException(I18N.getString("Scheduler.Error.CannotUpdateTest") + ':'
						+ I18N.getString("Scheduler.Error.AddingTestIntersectWithOtherTest"));
				}
			}

			if (this.selectedTestIds != null) {
				this.selectedTestIds.clear();
			}
			this.addSelectedTest(this, test);
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TESTS, null, null));

		}
	}

	public void addGroupTest(final Date date) throws ApplicationException {
		this.aloneGroupTest = true;
		this.startGroupDate = date;
		this.addGroupTests();
	}

	public void addGroupTests(final Date date, final long interval1) throws ApplicationException {
		this.aloneGroupTest = false;
		this.startGroupDate = date;
		this.interval = interval1;
		this.addGroupTests();
	}

	public void moveSelectedTests(final Date startDate) throws ApplicationException {
		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
			final Set<Test> tests;
			try {
				tests = StorableObjectPool.getStorableObjects(this.selectedTestIds, true);
			} catch (final ApplicationException e) {
				throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
			}

			Date firstStartDate = null;
			for(final Test test : tests) {
				final Date startTime = test.getStartTime();
				if (firstStartDate == null) {
					firstStartDate = startTime;
				} else {
					if (firstStartDate.after(startTime)) {
						firstStartDate = startTime;
					}
				}
			}
			
			if (firstStartDate != null) {
				this.moveSelectedTests(startDate.getTime() - firstStartDate.getTime());
			} else {
				assert Log.debugMessage("SchedulerModel.moveSelectedTests | firstStartDate == null ",
					Log.DEBUGLEVEL09);
			}
			
		}
	}

	public void moveSelectedTests(final long offset) throws ApplicationException {
		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
			final Set<Test> selectedTests;
			try {
				selectedTests = StorableObjectPool.getStorableObjects(this.selectedTestIds, true);
			} catch (final ApplicationException e) {
				throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
			}

			boolean correct = true;
			for (final Test selectedTest : selectedTests) {
				if (selectedTest.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
					correct = this.isValid(selectedTest, offset);
					if (!correct) {
						throw new ApplicationException(I18N.getString("Scheduler.Error.CannotMoveTests"));
					}
					
				}
			}

			if (correct) {
				boolean moved = false;
				for (final Test selectedTest : selectedTests) {
					if (selectedTest.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
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
		}
	}
	
	private void addGroupTests() throws ApplicationException {
		Log.debugMessage("SchedulerModel.addGroupTests | ", Level.FINEST);
		final Identifier meId = this.monitoredElement.getId();
		Identifier testGroupId = this.meTestGroup != null ? this.meTestGroup.get(meId) : null;
		if (testGroupId != null) {
			Test testGroup = null;
			try {
				testGroup = (Test) StorableObjectPool.getStorableObject(testGroupId, true);
			} catch (final ApplicationException e) {
				throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
			}
			if (this.aloneGroupTest) {					
//				if (this.isValid(this.startGroupDate, null, testGroup.getMonitoredElement().getId())) {
				if (this.isValid(this.monitoredElement.getId(), this.startGroupDate, this.startGroupDate, Identifier.VOID_IDENTIFIER, this.measurementSetup)) {
					try {
						final Test test = Test.createInstance(LoginManager.getUserId(),
								this.startGroupDate,
								this.startGroupDate,
								Identifier.VOID_IDENTIFIER,
								TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME,
								testGroup.getMeasurementType(),
								testGroup.getAnalysisType(),
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
					} catch (final CreateObjectException coe) {
						throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest"));
					}
				} else {
					throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest") + ':'
						+ I18N.getString("Scheduler.Error.AddingTestIntersectWithOtherTest"));
				}
			}
			
		}
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
			try {
				final Set<Test> tests = StorableObjectPool.getStorableObjects(this.selectedTestIds, true);
				selectedTests.addAll(tests);
			} catch (final ApplicationException e) {
				throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
			}
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
//				correct = this.isValid(startDate, endDate, selectedTest.getMonitoredElement().getId());
				correct = (this.isValid(this.monitoredElement.getId(), startDate, endDate, selectedTest.getTemporalPatternId(), this.measurementSetup)); 
				if (!correct) {
					throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest") + ':'
						+ I18N.getString("Scheduler.Error.AddingTestIntersectWithOtherTest"));
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

					try {
						final Test test = Test.createInstance(LoginManager.getUserId(),
								startDate,
								endDate,
								selectedTest.getTemporalPatternId(),
								selectedTest.getTemporalType(),
								selectedTest.getMeasurementType(),
								selectedTest.getAnalysisType(),
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
					} catch (final CreateObjectException e) {
						throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest"));
					}
				}
			}

		}
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TESTS, null, null));
	}
	
	public boolean isTestNewer(final Test test) {
		return test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION) 
			&& test.getStatus().value() == TestStatus._TEST_STATUS_NEW;
	}

	
	public boolean isValid(final Identifier monitoredElementId,
	                       final Date startDate, 
	                       final Date endDate,
	                       final Identifier temporalPatternId,
	                       final MeasurementSetup measurementSetup) 
	throws ApplicationException {
		final AbstractTemporalPattern temporalPattern;
		if (temporalPatternId == null || temporalPatternId.isVoid()) {
			temporalPattern = null;
		} else {
			temporalPattern = StorableObjectPool.getStorableObject(temporalPatternId, true);
		}
		return this.isValid(monitoredElementId, startDate, endDate, temporalPattern, measurementSetup);
	}
	
	private boolean isValid0(final Identifier monitoredElementId,
	                       final Set<Date> times,
	                       final MeasurementSetup measurementSetup,
	                       final Identifier testId) 
	throws ApplicationException {
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		
		final Map<Date, Date> localStartEndTimeMap = new HashMap<Date, Date>();
		
		boolean result = true;
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(this.testIds, true);
			for (final Test test : tests) {
				if (test.getId().equals(testId) || 
						!test.getMonitoredElementId().equals(monitoredElementId)) {
					continue;
				}				
				
				// ignore sub group tests,   
				// since they are taken into account in the main group test 
				final Identifier groupTestId = test.getGroupTestId();
				if (!groupTestId.isVoid() && !test.getId().equals(groupTestId)) {					
					continue;
				}
				
				for(final Date stDate : times) {
					Date enDate = localStartEndTimeMap.get(stDate);
					if (enDate == null) {
						enDate = new Date(stDate.getTime() + measurementDuration);
						localStartEndTimeMap.put(stDate, enDate);
					}
					
					result = !this.isIntersect(test, stDate, enDate);
					
					if (!result) {
						break;
					}
				}
			}
		} catch (final ApplicationException e) {
			throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
		}
		Log.debugMessage("SchedulerModel.isValid0 | return " + result, Log.DEBUGLEVEL10);
		return result;
	}
	
	public boolean isValid(final Identifier monitoredElementId,
	                       final Date startDate, 
	                       final Date endDate,
	                       final AbstractTemporalPattern temporalPattern,
	                       final MeasurementSetup measurementSetup) 
	throws ApplicationException {
		
		final Set<Date> times = this.getTestTimes(temporalPattern, startDate, endDate, 0L); 
		
		final boolean result = this.isValid0(monitoredElementId, 
			times, 
			measurementSetup, 
			Identifier.VOID_IDENTIFIER);
		
		Log.debugMessage("SchedulerModel.isValid | return " + result, Log.DEBUGLEVEL10);
		return result;
	}
	
	private SortedSet<Date> getTestTimes(final AbstractTemporalPattern temporalPattern,
		final Date startDate,
		final Date endDate,
		final long offset) {
		final SortedSet<Date> times; 
		
		final Date startTime0 = offset == 0 ? startDate : new Date(startDate.getTime() + offset);
		final Date endTime0 = offset == 0 ? endDate : new Date(endDate.getTime() + offset);
		
		if (temporalPattern != null) {
			times = temporalPattern.getTimes(startTime0, endTime0);
		} else {
			times = new TreeSet<Date>();
			times.add(startTime0);
		}
		return times;
	}
	
	private SortedSet<Date> getTestTimes(final Identifier temporalPatternId,
		final Date startDate,
		final Date endDate,
		final long offset) throws ApplicationException{
		
		if (temporalPatternId != null && !temporalPatternId.isVoid()) {
			final AbstractTemporalPattern temporalPattern = StorableObjectPool.getStorableObject(temporalPatternId, true);
			return this.getTestTimes(temporalPattern, startDate, endDate, offset);
		}
		return this.getTestTimes((AbstractTemporalPattern)null, startDate, endDate, offset);
	}
	
	private SortedSet<Date> getTestTimes(final Test test,
		final long offset) throws ApplicationException{
		return this.getTestTimes(test, test.getStartTime(), test.getEndTime(), offset);
	}
	
	private SortedSet<Date> getTestTimes(final Test test,
		final Date startTime,
		final Date endTime,
		final long offset) throws ApplicationException{		
		
		final SortedSet<Date> testTimes = this.getTestTimes(test.getTemporalPatternId(), startTime, endTime, 0L);
		
		final SortedMap<Date, String> stoppingMap = test.getStoppingMap();
		
		if (!stoppingMap.isEmpty()) {
			final SortedSet<Date> measurementDates;
			if (!this.isTestNewer(test)) {
				final LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(test.getId(), ObjectEntities.MEASUREMENT_CODE);	
				final Set<Measurement> testMeasurements = StorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition, true);
				measurementDates = new TreeSet<Date>();
				for (final Measurement measurement : testMeasurements) {
					measurementDates.add(measurement.getStartTime());
				}
			} else {
				measurementDates = null;
			}
			
			for(final Date stopDate : stoppingMap.keySet()) {
				final SortedSet<Date> tailSet = measurementDates != null ? 
						measurementDates.tailSet(stopDate) : null;
				if (tailSet != null && !tailSet.isEmpty()) {					
					final Date first = tailSet.first();
					testTimes.subSet(stopDate, first).clear();
					measurementDates.headSet(first).clear();
				} else {
					testTimes.tailSet(stopDate).clear();
					break;
				}
			}			
		}
		
		if (offset != 0 && !testTimes.isEmpty()) {
			final SortedSet<Date> testTimesOffsetted = new TreeSet<Date>();
			for (final Date date : testTimes) {
				testTimesOffsetted.add(new Date(date.getTime() + offset));
			}
			return testTimesOffsetted;
		}
		
		return testTimes;
	}
	
	public boolean isValid(final Test test,
	                       final  MeasurementSetup measurementSetup) 
	throws ApplicationException {
		
		final Set<Date> times = this.getTestTimes(test, 0L);
		
		final boolean result = this.isValid0(test.getMonitoredElementId(), 
			times, 
			measurementSetup, 
			test.getId());
		Log.debugMessage("SchedulerModel.isValid (" + test + ", " + measurementSetup + ")  | return " + result, Log.DEBUGLEVEL10);
		return result;
	}
	
	public boolean isValid(final Test test,
	                       final long offset) 
	throws ApplicationException {
		
		final Set<Date> times = this.getTestTimes(test, offset);
		
		final MeasurementSetup measurementSetup = StorableObjectPool.getStorableObject(test.getMainMeasurementSetupId(), true);
		
		final boolean result = this.isValid0(test.getMonitoredElementId(), 
			times, 
			measurementSetup, 
			test.getId());
		Log.debugMessage("SchedulerModel.isValid (" + test + ", " + offset + ")  | return " + result, Log.DEBUGLEVEL10);
		return result;
	}
	
	public boolean isValid(final Test test,
	                       final Date startDate,
	               		   final Date endDate) 
	throws ApplicationException {
		
		final Set<Date> times = this.getTestTimes(test, startDate, endDate, 0L);
		
		final MeasurementSetup measurementSetup = StorableObjectPool.getStorableObject(test.getMainMeasurementSetupId(), true);
		
		final boolean result = this.isValid0(test.getMonitoredElementId(), 
			times, 
			measurementSetup, 
			test.getId());
		Log.debugMessage("SchedulerModel.isValid (" + test + ", " + startDate + ", " + endDate + ")  | return " + result, Log.DEBUGLEVEL10);
		return result;
	}
	
	private boolean isIntersect(final Test test, 
	                            final Date startDate0, 
	                            final Date endDate0) 
	throws ApplicationException{
		final Identifier groupTestId = test.getGroupTestId();
		if (groupTestId.isVoid()) {
			return this.isIntersect0(test, startDate0, endDate0);
		} 

		boolean intersect = false;
		
		final Set<Test> groupTests = StorableObjectPool.getStorableObjectsByCondition(
			new LinkedIdsCondition(groupTestId, ObjectEntities.TEST_CODE), true, true);
		for(final Test groupTest : groupTests) {
			intersect = this.isIntersect0(groupTest, startDate0, endDate0);
			if (intersect) {
				break;
			}
		}
		
		return intersect;
	}
	
	private boolean isIntersect0(final Test test, 
	                            final Date startDate0, 
	                            final Date endDate0) 
	throws ApplicationException{	
		
		final SortedSet<Date> testTimes = this.getTestTimes(test, 0);
		
		final SortedSet<Date> tailSet = testTimes.tailSet(startDate0);
		
		boolean intersect = false;
		if (!tailSet.isEmpty()) {
			intersect = tailSet.first().before(endDate0);
		}
		
		if (!intersect) {
			final SortedSet<Date> headSet = testTimes.headSet(endDate0);			
			if (!headSet.isEmpty()) {
				final MeasurementSetup measurementSetup = 
					StorableObjectPool.getStorableObject(test.getMainMeasurementSetupId(), true);
				intersect = 
					headSet.last().getTime() + measurementSetup.getMeasurementDuration() > startDate0.getTime();
			}
		}
		
		assert Log.debugMessage("SchedulerModel.isIntersect0 | " 
				+ test 
				+ (intersect ? " intersects " : " doen't intersect ") 
				+ '[' 
				+ startDate0 
				+ ", " 
				+ endDate0 
				+ ']' , 
			Log.DEBUGLEVEL09);
		return intersect;
	}	
	
	public static Color getColor(final TestStatus testStatus) {
		return getColor(testStatus, false);
	}

	public static Color getColor(final TestStatus testStatus, 
	                             final boolean selected) {
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
			case TestStatus._TEST_STATUS_STOPPING:
			case TestStatus._TEST_STATUS_STOPPED:
				color = selected ? COLOR_STOPPED_SELECTED : COLOR_STOPPED;
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

	public static String getStatusName(final TestStatus testStatus) {
		switch(testStatus.value()) {
		case TestStatus._TEST_STATUS_ABORTED: 
			return I18N.getString("Scheduler.Text.Test.Status.Aborted");
		case TestStatus._TEST_STATUS_COMPLETED: 
			return I18N.getString("Scheduler.Text.Test.Status.Completed");
		case TestStatus._TEST_STATUS_NEW: 
			return I18N.getString("Scheduler.Text.Test.Status.New");
		case TestStatus._TEST_STATUS_PROCESSING: 
			return I18N.getString("Scheduler.Text.Test.Status.Processing");
		case TestStatus._TEST_STATUS_SCHEDULED: 
			return I18N.getString("Scheduler.Text.Test.Status.Scheduled");
		case TestStatus._TEST_STATUS_STOPPING: 
			return I18N.getString("Scheduler.Text.Test.Status.Stopping");
		case TestStatus._TEST_STATUS_STOPPED: 
			return I18N.getString("Scheduler.Text.Test.Status.Stopped");
		}
		return null;
	}
}
