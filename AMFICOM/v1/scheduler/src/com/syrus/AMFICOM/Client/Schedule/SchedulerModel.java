/*-
 * $Id: SchedulerModel.java,v 1.137 2005/10/27 14:35:54 bob Exp $
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
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupWrapper;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.TestView;
import com.syrus.AMFICOM.measurement.TestViewAdapter;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.Log;
import com.syrus.util.WrapperComparator;

/**
 * @version $Revision: 1.137 $, $Date: 2005/10/27 14:35:54 $
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
	private Set<Identifier>		mainTestIds							= new HashSet<Identifier>();
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

	public static final String	COMMAND_ADD_TEST					= "AddTest";
	public static final String	COMMAND_REFRESH_TEST				= "RefreshTest";
	public static final String	COMMAND_REFRESH_TESTS				= "RefreshTests";
	public static final String	COMMAND_REMOVE_TEST					= "RemoveTest";
	
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
	
	public Set<Identifier> getMainTestIds() {
		assert Log
				.debugMessage("SchedulerModel.getMainTestIds | " + this.mainTestIds, Log.DEBUGLEVEL09);
		return this.mainTestIds;
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		final String propertyName = evt.getPropertyName().intern();
		if (propertyName == COMMAND_CLEAN) {
			if (this.testIds != null) {
				this.testIds.clear();
			}			
			if (this.mainTestIds != null) {
				this.mainTestIds.clear();
			}
			TestView.clearCache();
			this.refreshEditors();
		} else if (propertyName == COMMAND_SET_ANALYSIS_TYPE) {
			this.analysisType = (AnalysisType) evt.getNewValue();
		} else if (propertyName == COMMAND_SET_MEASUREMENT_TYPE) {
			this.setSelectedMeasurementType((MeasurementType) evt.getNewValue());
		} else if (propertyName == COMMAND_SET_MONITORED_ELEMENT) {
			this.setSelectedMonitoredElement((MonitoredElement) evt.getNewValue());
		} else if (propertyName == COMMAND_SET_MEASUREMENT_SETUP) {
			this.measurementSetup = (MeasurementSetup) evt.getNewValue();
		} else if (propertyName == COMMAND_SET_TEMPORAL_STAMPS) {
			this.testTimeStamps = (TestTemporalStamps) evt.getNewValue();
		} else if (propertyName == COMMAND_SET_NAME) {
			this.name = (String) evt.getNewValue();
		} else if (propertyName == COMMAND_SET_GROUP_TEST) {
			this.groupTest = true;
		} 
	}

	public final boolean isAddingGroupTestEnable() {
		if (this.monitoredElement != null && this.meTestGroup != null) {
			final Identifier groupTestId = this.meTestGroup.get(this.monitoredElement.getId());
			return groupTestId != null && !groupTestId.isVoid();
		}
		return false;
	}
	
	public void removeTest(final Test test) throws ApplicationException {
		
		int status = test.getStatus().value();
		if (status == TestStatus._TEST_STATUS_COMPLETED ||
				status == TestStatus._TEST_STATUS_ABORTED) {
			return;
		}
		
		final Identifier groupTestId = test.getGroupTestId();
		if (groupTestId != null && !groupTestId.isVoid()) {
			if (this.meTestGroup != null) {
				this.meTestGroup.remove(test.getMonitoredElementId());
			}
			try {
				final Set<Test> testsByCondition = StorableObjectPool.getStorableObjectsByCondition(
					new LinkedIdsCondition(groupTestId, ObjectEntities.TEST_CODE), true, true);
				final Set<Identifier> testIdsToRemove = Identifier.createIdentifiers(testsByCondition);
				this.mainTestIds.removeAll(testIdsToRemove);
				this.testIds.removeAll(testIdsToRemove);
				StorableObjectPool.delete(testIdsToRemove);
			} catch (final ApplicationException e) {
				throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
			}
		} else {
			Identifier testId = test.getId();
			this.mainTestIds.remove(testId);
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
		this.startGetData(true);
	}

	public void createTest() throws ApplicationException {
		this.flag = FLAG_CREATE;		
		this.startGetData(true);
	}

	private void startGetData(final boolean simple) throws ApplicationException {
		if (simple) {
			this.groupTest = false;
		}
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
			this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_NAME, null, null));
		}
		if (simple) {
			if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_GET_TEMPORAL_STAMPS, null, null));
			}
			if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {
				this.generateTest();
			}
		}
	}

	public void setBreakData() {
		this.flag = 0;
	}
	
	public void updateTests(final Date startDate, 
	                        final Date endDate) 
	throws ApplicationException {
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
				StatusMessageEvent.STATUS_MESSAGE,
				I18N.getString("Scheduler.StatusMessage.UpdatingTests"))); //$NON-NLS-1$

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, true));

		if (this.measurementSetupIdMap != null) {
			this.measurementSetupIdMap.clear();
		}
		
		try {
			final Map<Identifier, StorableObjectVersion> idVersion = new HashMap<Identifier, StorableObjectVersion>();
			{
				for (final Identifier testId : this.testIds) {
					final Test test = TestView.valueOf(testId).getTest();
					idVersion.put(test.getId(), test.getVersion());
				}
			}
			
			final long time0 = System.currentTimeMillis();
			StorableObjectPool.refresh();
			final long time1 = System.currentTimeMillis();
			final TypicalCondition startTypicalCondition = 
				new TypicalCondition(endDate,
					endDate,
					OperationSort.OPERATION_LESS_EQUALS,
					ObjectEntities.TEST_CODE,
					TestWrapper.COLUMN_START_TIME);
			final TypicalCondition endTypicalCondition = 
				new TypicalCondition(startDate,
					startDate,
					OperationSort.OPERATION_GREAT_EQUALS,
					ObjectEntities.TEST_CODE,
					TestWrapper.COLUMN_END_TIME);
			
			final CompoundCondition compoundCondition = 
				new CompoundCondition(startTypicalCondition,
					CompoundConditionSort.AND,
					endTypicalCondition);
			
			final Set<Test> tests = 
				StorableObjectPool.getStorableObjectsByCondition(compoundCondition, 
					true, 
					true);
			final long time2 = System.currentTimeMillis();

			final Set<Test> refreshTests = new HashSet<Test>();
			for (final Test test : tests) {
				final Identifier testId = test.getId();
				final StorableObjectVersion version = idVersion.get(testId);
				if (test.getVersion().equals(version)) {
					idVersion.remove(testId);
				} else {
					refreshTests.add(test);
				}
			}
			TestView.refreshCache(refreshTests, startDate, endDate);
			
			final long time3 = System.currentTimeMillis();

			for (final Test test : refreshTests) {
				TestView.addTest(test, startDate, endDate);
				this.addTest(test);
			}
			assert Log.debugMessage("SchedulerModel.updateTests | " 
						+ this.testIds 
						+ ", " 
						+ this.testIds.size(),
				Log.DEBUGLEVEL03);
			
			assert Log.debugMessage("SchedulerModel.updateTests | StorableObjectPool.refresh:" + (time1-time0),
				Log.DEBUGLEVEL03);
			assert Log.debugMessage("SchedulerModel.updateTests | StorableObjectPool.getStorableObjectsByCondition:" + (time2-time1),
				Log.DEBUGLEVEL03);
			assert Log.debugMessage("SchedulerModel.updateTests | TestView.refreshCache:" + (time3-time2),
				Log.DEBUGLEVEL03);
		} catch (final ApplicationException e) {
			throw new ApplicationException(I18N.getString("Scheduler.Error.CannotRefreshTests"));
		}

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
				StatusMessageEvent.STATUS_MESSAGE,
				I18N.getString("Scheduler.StatusMessage.TestsUpdated"))); //$NON-NLS-1$
		final long time0 = System.currentTimeMillis();
		this.refreshTests();
		final long time1 = System.currentTimeMillis();
		assert Log.debugMessage("SchedulerModel.updateTests | refreshTests:" + (time1-time0),
			Log.DEBUGLEVEL03);
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_PROGRESS_BAR, false));
	}

	public Set<Identifier> getSelectedTestIds() {
		if (this.selectedTestIds != null) {
			return this.selectedTestIds;
		}
		return Collections.emptySet();
	}
	
	public Set<Test> getSelectedTests(){
		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
			final Set<Test> selectedTests = new HashSet<Test>(this.selectedTestIds.size());
			for (final Identifier testId : this.selectedTestIds) {
				selectedTests.add(TestView.valueOf(testId).getTest());
			}			
			return selectedTests;
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

	public void addSelectedTest(final Object source, 
	                            final Test selectedTest) 
	throws ApplicationException {
		
		assert Log.debugMessage("SchedulerModel.addSelectedTest | " + selectedTest,
			Log.DEBUGLEVEL09);
		
		synchronized (this) {
			if (this.selectedTestIds == null) {
				this.selectedTestIds = new HashSet<Identifier>();
			}
			if (selectedTest != null) {
				if (this.selectedTestIds.isEmpty()) {
					this.selectedFirstTest = selectedTest;
					this.selectedFirstTestId = selectedTest.getId();
				}
				final Identifier groupTestId = selectedTest.getGroupTestId();
				if (!groupTestId.isVoid()) {
					final Set<Test> testsByCondition = StorableObjectPool.getStorableObjectsByCondition(
						new LinkedIdsCondition(groupTestId, ObjectEntities.TEST_CODE), true, true);
					for (final Test test : testsByCondition) {
						this.addSelectedSingleTest(test);
					}
				} else {
					this.addSelectedSingleTest(selectedTest);
				}
				this.refreshTest(source);
			} else {
				Log.debugMessage("SchedulerModel.setSelectedTest | selectedTest is " + selectedTest, Level.FINEST);
			}
		}
	}

	private void addSelectedSingleTest(final Test selectedTest) {
		final Identifier selectedTestId = selectedTest.getId();
		if (!this.selectedTestIds.contains(selectedTestId)) {
			this.selectedTestIds.add(selectedTestId);			
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
		final Set<Test> tests = this.getSelectedTests();
		
		for (final Test test : tests) {
			if (test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
				final String reason = this.isValid(test, measurementSetup);
				if (reason != null) {
					this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_MEASUREMENT_SETUP, null, null));
					throw new IllegalDataException(I18N.getString("Scheduler.Error.CannotApplyMeasurementSetup")
						+ "\n" 
						+ reason);
				}
			}
		}							
		
		final Set<Identifier> changedTestId = new HashSet<Identifier>(); 
		for (final Test test : tests) {
			if (test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
				changedTestId.add(test.getId());
				test.setMeasurementSetupIds(measurementSetupIdSet);
				final TestView testView = TestView.valueOf(test);
				TestView.addTest(test, testView.getStart(), testView.getEnd());
			}
		}
		
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_REFRESH_TESTS, null, changedTestId));
	}

	private void generateTest() throws ApplicationException {
		if (this.flag == FLAG_APPLY || this.flag == FLAG_CREATE) {

			final Set<Identifier> newTestIds = new HashSet<Identifier>();
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
				final String reason;
				if ((reason = this.isValid(this.monitoredElement.getId(), 
							startTime, 
							endTime, 
							temporalPattern, 
							this.measurementSetup))
						== null) {
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
						newTestIds.add(test.getId());
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
					this.addTest(test);
					TestView.addTest(test, test.getStartTime(), test.getEndTime());
				} else {
					throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest") 
						+ "\n" 
						+ reason);
				}								
			} else {
				final String reason;
				if ((reason = this.isValid(this.monitoredElement.getId(), 
							startTime, 
							endTime, 
							temporalPattern, 
							this.measurementSetup)) 
						== null) {
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
						+ "\n" 
						+ reason);
				}
			}

			if (this.selectedTestIds != null) {
				this.selectedTestIds.clear();
			}
			if (newTestIds.isEmpty()) {
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_ADD_TEST, null, newTestIds));
			} else {
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_REFRESH_TESTS, null, null));
			}
			this.addSelectedTest(this, test);
		}
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

			
			for (final Test selectedTest : selectedTests) {
				if (selectedTest.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
					final String reason = this.isValid(selectedTest, offset);
					if (reason != null) {
						throw new ApplicationException(I18N.getString("Scheduler.Error.CannotMoveTests")
							+ "\n" 
							+ reason);
					}
					
				}
			}
			
			for (final Test selectedTest : selectedTests) {
				if (selectedTest.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
					final Date newStartDate = new Date(selectedTest.getStartTime().getTime() + offset);
					Date newEndDate = selectedTest.getEndTime();
					if (newEndDate != null) {
						newEndDate = new Date(newEndDate.getTime() + offset);
					}
					selectedTest.setStartTime(newStartDate);
					selectedTest.setEndTime(newEndDate);
					final TestView testView = TestView.valueOf(selectedTest);
					TestView.addTest(selectedTest, testView.getStart(), testView.getEnd());
				}
			}
			this.refreshTests();			

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

	public void addGroupTest(final Date date) throws ApplicationException {
		this.aloneGroupTest = true;
		this.startGroupDate = date;
		this.addGroupTests(0L);
	}

	public void addGroupTests(final Date date, 
		final long interval) 
	throws ApplicationException {		
		this.aloneGroupTest = false;
		this.startGroupDate = date;
		this.addGroupTests(interval);
	}

	private void addGroupTests(final long interval) throws ApplicationException {
		Log.debugMessage("SchedulerModel.addGroupTests | ", Log.DEBUGLEVEL10);
		this.flag = FLAG_CREATE;
		this.startGetData(false);
		if (this.flag != FLAG_APPLY && this.flag != FLAG_CREATE) {
			return;
		}
		final Set<Identifier> newTestIds = new HashSet<Identifier>();
		final Identifier meId = this.monitoredElement.getId();
		final Identifier testGroupId = this.meTestGroup != null ? this.meTestGroup.get(meId) : null;
		if (testGroupId != null) {
			Test testGroup = null;
			try {
				testGroup = (Test) StorableObjectPool.getStorableObject(testGroupId, true);
			} catch (final ApplicationException e) {
				throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
			}
			if (this.aloneGroupTest) {					
				final String reason;
				if ((reason = this.isValid(this.monitoredElement.getId(), 
								this.startGroupDate, 
								this.startGroupDate, 
								Identifier.VOID_IDENTIFIER, 
								this.measurementSetup))
						== null) {
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
						newTestIds.add(test.getId());
						this.addTest(test);
						TestView.addTest(test, test.getStartTime(), test.getEndTime());
						if (this.selectedTestIds != null) {
							this.selectedTestIds.clear();
						} else {
							this.selectedTestIds = new HashSet<Identifier>();
						}
						this.selectedTestIds.add(test.getId());
						
						this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_ADD_TEST, null, newTestIds));
						return;
					} catch (final CreateObjectException coe) {
						throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest"));
					}					
				} else {
					throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest") + ':'
						+ "\n" 
						+ reason);
				}
			}
			
		}
		
		assert Log.debugMessage("SchedulerModel.addGroupTests | " + this.selectedTestIds, Log.DEBUGLEVEL09);
		
		if (this.selectedTestIds == null || this.selectedTestIds.isEmpty()) {
			if (testGroupId == null) {
				this.createTest();
			} else {
				this.startGroupDate = new Date(this.startGroupDate.getTime() + interval);
				this.aloneGroupTest = true;
				this.addGroupTests(interval);
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
			final long offset = (endTime != null ? endTime : lastTest.getStartTime()).getTime() + interval - firstTime;

			assert Log.debugMessage("SchedulerModel.addGroupTests | firstTime is " + new Date(firstTime), Level.FINEST);
			assert Log.debugMessage("SchedulerModel.addGroupTests | offset is " + offset, Level.FINEST);

			this.selectedTestIds.clear();
			
			for (final Test selectedTest : selectedTests) {
				final Date startDate = new Date(selectedTest.getStartTime().getTime() + offset);
				Date endDate = selectedTest.getEndTime();
				if (endDate != null) {
					endDate = new Date(endDate.getTime() + offset);
				}

				final String reason;
				if ((reason = this.isValid(this.monitoredElement.getId(), 
							startDate, 
							endDate, 
							selectedTest.getTemporalPatternId(), 
							this.measurementSetup)) 
						!= null) {
					throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest")
						+ ":\n"
						+ reason);
				}
			}

			for (final Test selectedTest : selectedTests) {
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
						test.setGroupTestId(testId);
					}
					newTestIds.add(test.getId());
					this.addTest(test);
					TestView.addTest(test, test.getStartTime(), test.getEndTime());
					this.selectedTestIds.add(testId);
					assert Log.debugMessage("SchedulerModel.addGroupTests | add test " + test.getId()
							+ " at " + startDate + "," + endDate, Level.FINEST);
				} catch (final CreateObjectException e) {
					throw new ApplicationException(I18N.getString("Scheduler.Error.CannotAddTest"));
				}
			}

		}
		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, COMMAND_ADD_TEST, null, newTestIds));
	}
	
	private void addTest(final Test test) {
		final Identifier testId = test.getId();
		this.testIds.add(testId);
		final Identifier groupTestId = test.getGroupTestId();
		if (groupTestId.isVoid() || groupTestId.equals(testId)) {
			this.mainTestIds.add(testId);
		}
	}
	
	public boolean isTestNewer(final Test test) {
		return test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION);
	}

	
	/**
	 * 
	 * @param monitoredElementId
	 * @param startDate
	 * @param endDate
	 * @param temporalPatternId
	 * @param measurementSetup
	 * @return intersection desctription or null if there is no intersection
	 * @throws ApplicationException
	 */
	public String isValid(final Identifier monitoredElementId,
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
	
	private String isValid0(final Identifier monitoredElementId,
	                       final SortedSet<Date> times,
	                       final MeasurementSetup measurementSetup,
	                       final Identifier testId) 
	throws ApplicationException {
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		
		final Map<Date, Date> localStartEndTimeMap = new HashMap<Date, Date>();
		
		String result = null;
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(this.testIds, true);
			for (final Test test : tests) {				
				
				// ignore sub group tests,   
				// since they are taken into account in the main group test 
				final Identifier groupTestId = test.getGroupTestId();
				
				if (test.getId().equals(testId) ||
						!groupTestId.isVoid() && !test.getId().equals(groupTestId)) {
					continue;
				}			
				
				{		
					final MonitoredElement monitoredElement = 
						StorableObjectPool.getStorableObject(monitoredElementId, true);
					
					final MeasurementPort measurementPort = 
						StorableObjectPool.getStorableObject(monitoredElement.getMeasurementPortId(), true);
					
					if (!test.getKISId().equals(measurementPort.getKISId())) {
						continue;
					}
				}
				
				final TestView view = TestView.valueOf(test);
				
				final Date firstDate = view.getFirstDate();
				final Date lastDate = view.getLastDate();
				
				// rough estimation of intersection
				final boolean before = times.last().before(firstDate);
				final boolean after = times.first().after(lastDate);
				if (before || after) {
					assert Log.debugMessage("SchedulerModel.isValid0 | rough estimation of intersection that inspected time is "
						+ (after ? "after" : "before") 
						+ " test " + test.getId(),
						Log.DEBUGLEVEL09);
					continue;
				}
				
				for(final Date stDate : times) {
					Date enDate = localStartEndTimeMap.get(stDate);
					if (enDate == null) {
						enDate = new Date(stDate.getTime() + measurementDuration);
						localStartEndTimeMap.put(stDate, enDate);
					}

					if ((result = this.isIntersect(test, stDate, enDate)) != null) {
						break;
					}
				}
				
				if (result != null) {
					break;
				}

			}
		} catch (final ApplicationException e) {
			throw new ApplicationException(I18N.getString("Error.CannotAcquireObject"));
		}
		Log.debugMessage("SchedulerModel.isValid0 | return " + result, Log.DEBUGLEVEL10);
		return result;
	}
	
	/**
	 * @param monitoredElementId
	 * @param startDate
	 * @param endDate
	 * @param temporalPattern
	 * @param measurementSetup
	 * @return intersection desctription or null if there is no intersection
	 * @throws ApplicationException
	 */
	public String isValid(final Identifier monitoredElementId,
	                       final Date startDate, 
	                       final Date endDate,
	                       final AbstractTemporalPattern temporalPattern,
	                       final MeasurementSetup measurementSetup) 
	throws ApplicationException {
		
		final SortedSet<Date> times = this.getTestTimes(temporalPattern, startDate, endDate, 0L); 
		
		final String result = this.isValid0(monitoredElementId, 
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
		
		final Date startTime0 = offset == 0 ? startDate : new Date(startDate.getTime() + offset);
		final Date endTime0 = offset == 0 ? endDate : new Date(endDate.getTime() + offset);
		
		final SortedSet<Date> times;
		if (temporalPattern != null) {			
			times = new TreeSet<Date>(temporalPattern.getTimes(startTime0, endTime0));
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
		
		assert Log.debugMessage("SchedulerModel.getTestTimes | testTimes:" + testTimes, Log.DEBUGLEVEL10);
		
		
		
		final TestStatus status = test.getStatus();
		if (status == TestStatus.TEST_STATUS_STOPPED ||  
				status == TestStatus.TEST_STATUS_STOPPING) {			
			final SortedMap<Date, String> stoppingMap = test.getStoppingMap();
			final Date stopDate = stoppingMap.lastKey();
			assert Log.debugMessage("SchedulerModel.getTestTimes | stopDate:" + stopDate,
				Log.DEBUGLEVEL10);
			final SortedSet<Date> tailSet2 = testTimes.tailSet(stopDate);
			assert Log.debugMessage("SchedulerModel.getTestTimes | clear tailSet from " + stopDate + " : " + tailSet2,
				Log.DEBUGLEVEL10);
			tailSet2.clear();			
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
	
	/**
	 * @param test
	 * @param measurementSetup
	 * @return intersection desctription or null if there is no intersection
	 * @throws ApplicationException
	 */
	public String isValid(final Test test,
	                       final  MeasurementSetup measurementSetup) 
	throws ApplicationException {
		
		final SortedSet<Date> times = this.getTestTimes(test, 0L);
		
		final String result = this.isValid0(test.getMonitoredElementId(), 
			times, 
			measurementSetup, 
			test.getId());
		Log.debugMessage("SchedulerModel.isValid (" + test + ", " + measurementSetup + ")  | return " + result, Log.DEBUGLEVEL10);
		return result;
	}
	
	/**
	 * @param test
	 * @param offset
	 * @return intersection desctription or null if there is no intersection
	 * @throws ApplicationException
	 */
	public String isValid(final Test test,
	                       final long offset) 
	throws ApplicationException {
		
		final SortedSet<Date> times = this.getTestTimes(test, offset);
		
		final MeasurementSetup measurementSetup = StorableObjectPool.getStorableObject(test.getMainMeasurementSetupId(), true);
		
		final String result = this.isValid0(test.getMonitoredElementId(), 
			times, 
			measurementSetup, 
			test.getId());
		Log.debugMessage("SchedulerModel.isValid (" + test + ", " + offset + ")  | return " + result, Log.DEBUGLEVEL10);
		return result;
	}
	
	/**
	 * @param test
	 * @param startDate
	 * @param endDate
	 * @return intersection desctription or null if there is no intersection
	 * @throws ApplicationException
	 */
	public String isValid(final Test test,
	                       final Date startDate,
	               		   final Date endDate) 
	throws ApplicationException {
		
		final SortedSet<Date> times = this.getTestTimes(test, startDate, endDate, 0L);
		
		final MeasurementSetup measurementSetup = StorableObjectPool.getStorableObject(test.getMainMeasurementSetupId(), true);
		
		final String result = this.isValid0(test.getMonitoredElementId(), 
			times, 
			measurementSetup, 
			test.getId());
		Log.debugMessage("SchedulerModel.isValid (" + test + ", " + startDate + ", " + endDate + ")  | return " + result, Log.DEBUGLEVEL10);
		return result;
	}
	
	private String isIntersect(final Test test, 
	                            final Date startDate0, 
	                            final Date endDate0) 
	throws ApplicationException {
		final Identifier groupTestId = test.getGroupTestId();
		if (groupTestId.isVoid() || test.getId().equals(groupTestId)) {
			return this.isIntersect0(test, startDate0, endDate0);
		} 

		String intersect = null;
		
		final Set<Test> groupTests = StorableObjectPool.getStorableObjectsByCondition(
			new LinkedIdsCondition(groupTestId, ObjectEntities.TEST_CODE), true, true);
		for(final Test groupTest : groupTests) {
			if ((intersect = this.isIntersect0(groupTest, startDate0, endDate0)) != null) {
				break;
			}
		}
		
		return intersect;
	}
	
	/**
	 * @param test
	 * @param startDate0
	 * @param endDate0
	 * @return intersection desctription or null if there is no intersection
	 * @throws ApplicationException
	 */
	private String isIntersect0(final Test test, 
	                            final Date startDate0, 
	                            final Date endDate0) 
	throws ApplicationException {
		final SortedSet<Date> testTimes = this.getTestTimes(test, 0);
		
		assert Log.debugMessage("SchedulerModel.isIntersect0 | " + testTimes, Log.DEBUGLEVEL10);
		
		final SortedSet<Date> tailSet = testTimes.tailSet(startDate0);
		
		boolean intersect = false;
		if (!tailSet.isEmpty()) {
			intersect = tailSet.first().before(endDate0);
		}
		
		if (!intersect) {
			final SortedSet<Date> headSet = testTimes.headSet(endDate0);			
			if (!headSet.isEmpty()) {
				final TestView view = TestView.valueOf(test);
				final MeasurementSetup measurementSetup = view.getMeasurementSetup();
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
			Log.DEBUGLEVEL10);
		if (intersect) {
			final String extendedTestDescription = this.getExtendedTestDescription(test);
			assert Log.debugMessage("SchedulerModel.isIntersect0 | " + extendedTestDescription,
				Log.DEBUGLEVEL10);
			return I18N.getString("Scheduler.Text.Scheduler.Model.TestIntersection") + "\n" + extendedTestDescription;
		}
		
		return null;
	}	
	
	public final String getExtendedTestDescription(final Test test) 
	throws ApplicationException {
		
		final MonitoredElement testMonitoredElement = 
			StorableObjectPool.getStorableObject(test.getMonitoredElementId(), true);
		
		final MeasurementPort testMeasurementPort = 
			StorableObjectPool.getStorableObject(testMonitoredElement.getMeasurementPortId(), true);
		
		final KIS kis = StorableObjectPool.getStorableObject(testMeasurementPort.getKISId(), true); 
		
		final TestViewAdapter testController = TestViewAdapter.getInstance();
		final TestView view = TestView.valueOf(test);
		return testController.getValue(view, TestViewAdapter.KEY_TEMPORAL_TYPE_NAME).toString()
			+ "\n" + testController.getName(TestViewAdapter.KEY_START_TIME) 
			+ " : " 
			+ testController.getValue(view, TestViewAdapter.KEY_START_TIME)
			+ "\n"
			+ testMonitoredElement.getName() 
			+ "\n"
			+ kis.getDescription(); 
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
				// too unlike
				Log.errorException(new Exception("SchedulerModel.getColor"));
				color = COLOR_UNRECOGNIZED;
				break;
		}
		return color;
	}

	public static String getStatusName(final TestStatus testStatus) {
		switch(testStatus.value()) {
		case TestStatus._TEST_STATUS_NEW: 
			return I18N.getString("Scheduler.Text.Test.Status.New");
		case TestStatus._TEST_STATUS_SCHEDULED: 
			return I18N.getString("Scheduler.Text.Test.Status.Scheduled");
		case TestStatus._TEST_STATUS_PROCESSING: 
			return I18N.getString("Scheduler.Text.Test.Status.Processing");
		case TestStatus._TEST_STATUS_COMPLETED: 
			return I18N.getString("Scheduler.Text.Test.Status.Completed");			
		case TestStatus._TEST_STATUS_STOPPING: 
			return I18N.getString("Scheduler.Text.Test.Status.Stopping");
		case TestStatus._TEST_STATUS_STOPPED: 
			return I18N.getString("Scheduler.Text.Test.Status.Stopped");
		case TestStatus._TEST_STATUS_ABORTED: 
			return I18N.getString("Scheduler.Text.Test.Status.Aborted");
		}
		// too unlike
		Log.errorException(new Exception("SchedulerModel.getStatusName"));
		return null;
	}
}
