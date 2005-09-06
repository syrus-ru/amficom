/*
 * $Id: TestController.java,v 1.22 2005/09/06 07:46:18 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.22 $, $Date: 2005/09/06 07:46:18 $
 * @author $Author: bob $
 * @module module
 */
public class TestController implements Wrapper<Test> {

	public static final String KEY_TEMPORAL_TYPE = "TemporalType";
	public static final String KEY_TEMPORAL_TYPE_NAME = "TemporalTypeName";
	public static final String KEY_KIS = "RTU";
	public static final String KEY_MONITORED_ELEMENT = "MonitoredElement";
	public static final String KEY_TEST_OBJECT = "TestObject";
	public static final String KEY_MEASUREMENT_TYPE = "MeasurementType";
	public static final String KEY_START_TIME = "TestStartTime";
	public static final String KEY_STATUS = "Status";

	private List<String> keys;

	private static boolean initialized = false;
	private static TestController instance = null;
	private static Object lock = new Object();

	private Map<Component, TestStatus> statusMap;

	private Map<TestTemporalType, String> temporalTypeMap;

	private class ComparableLabel extends JLabel implements Comparable {
		
		private static final long	serialVersionUID	= 3546642105390084920L;

		public ComparableLabel(String string) {
			super(string);
		}
		
		public int compareTo(Object o) {
			return ((ComparableLabel)o).getText().compareTo(this.getText());
		}
	}
	
	private TestController() {

		//		 empty private constructor
		final String[] keysArray = new String[] { KEY_TEMPORAL_TYPE, 
				KEY_MONITORED_ELEMENT, 
				KEY_TEST_OBJECT,
				KEY_MEASUREMENT_TYPE, 
				KEY_START_TIME, 
				KEY_STATUS};
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

		this.statusMap = new HashMap<Component, TestStatus>();
		this.addStatusItem(TestStatus.TEST_STATUS_ABORTED, 
			LangModelSchedule.getString("Text.Test.Status.Aborted"));
		this.addStatusItem(TestStatus.TEST_STATUS_COMPLETED, 
			LangModelSchedule.getString("Text.Test.Status.Completed"));
		this.addStatusItem(TestStatus.TEST_STATUS_NEW, 
			LangModelSchedule.getString("Text.Test.Status.New"));
		this.addStatusItem(TestStatus.TEST_STATUS_PROCESSING, 
			LangModelSchedule.getString("Text.Test.Status.Processing"));
		this.addStatusItem(TestStatus.TEST_STATUS_SCHEDULED, 
			LangModelSchedule.getString("Text.Test.Status.Scheduled"));
		this.addStatusItem(TestStatus.TEST_STATUS_STOPPING, 
			LangModelSchedule.getString("Text.Test.Status.Stopping"));
		this.addStatusItem(TestStatus.TEST_STATUS_STOPPED, 
			LangModelSchedule.getString("Text.Test.Status.Stopped"));
		
		this.temporalTypeMap = new HashMap<TestTemporalType, String>();
		this.temporalTypeMap.put(TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, 
			LangModelSchedule.getString("Text.Test.TemporalType.Onetime"));
		this.temporalTypeMap.put(TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL, 
			LangModelSchedule.getString("Text.Test.TemporalType.Periodical"));
		this.temporalTypeMap.put(TestTemporalType.TEST_TEMPORAL_TYPE_CONTINUOUS, 
			LangModelSchedule.getString("Text.Test.TemporalType.Continual"));
	}

	private Component getStatusComponent(final TestStatus testStatus, 
	                                     final String name) {
		final ComparableLabel label = new ComparableLabel(name);
		label.setOpaque(true);
		final Color color = SchedulerModel.getColor(testStatus);
		label.setBackground(color.brighter());
		return label;
	}
	
	private void addStatusItem(final TestStatus testStatus, 
	                           final String name) {
		this.statusMap.put(this.getStatusComponent(testStatus, name), testStatus);
	}
	
	public static TestController getInstance() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized && instance == null) {
					instance = new TestController();
				}
			}
			synchronized (lock) {
				initialized = true;
			}
		}
		return instance;
	}

	public String getKey(int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		String name = null;
		if (key.equals(KEY_TEMPORAL_TYPE))
			name = LangModelSchedule.getString("Text.Test.Field.TemporalType"); //$NON-NLS-1$
		else if (key.equals(KEY_KIS))
			name = LangModelSchedule.getString("Text.Test.Field.RTU"); //$NON-NLS-1$
		else if (key.equals(KEY_MONITORED_ELEMENT))
			name = LangModelSchedule.getString("Text.Test.Field.Port"); //$NON-NLS-1$
		else if (key.equals(KEY_TEST_OBJECT))
			name = LangModelSchedule.getString("Text.Test.Field.TestingObject"); //$NON-NLS-1$
		else if (key.equals(KEY_MEASUREMENT_TYPE))
			name = LangModelSchedule.getString("Text.Test.Field.MeasurementType"); //$NON-NLS-1$
		else if (key.equals(KEY_START_TIME))
			name = LangModelSchedule.getString("Text.Test.Field.TimeOfTheFirstMeasurement"); //$NON-NLS-1$
		else if (key.equals(KEY_STATUS))
			name = LangModelSchedule.getString("Text.Test.Field.Status"); //$NON-NLS-1$

		return name;
	}

	public Class getPropertyClass(final String key) {
		Class clazz = String.class;
		if ((key.equals(KEY_STATUS))) {
			clazz = Map.class;
		}
		return clazz;
	}

	public Object getPropertyValue(final String key) {
		Object value = null;
		if (key.equals(KEY_STATUS)) {
			value = this.statusMap;
		}
		return value;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// TODO Auto-generated method stub

	}

	public Object getValue(final Test test, final String key) {
		Object value = null;
		if (test != null) {
			if (key.equals(KEY_TEMPORAL_TYPE)) {
				if (test.getGroupTestId().isVoid()) {
					value = this.temporalTypeMap.get(test.getTemporalType());
				} else {
					value = LangModelSchedule.getString("Text.Test.TemporalType.Sectional");
				}
			} else if (key.equals(KEY_TEMPORAL_TYPE_NAME)) {
				if (test.getGroupTestId().isVoid()) {
					final TestTemporalType temporalType = test.getTemporalType();
					value = this.temporalTypeMap.get(test.getTemporalType());

					if (temporalType.value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL) {
						final Identifier temporalPatternId = test.getTemporalPatternId();
						if (!temporalPatternId.isVoid() && temporalPatternId.getMajor() == ObjectEntities.PERIODICALTEMPORALPATTERN_CODE) {
							try {
								final PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern) StorableObjectPool.getStorableObject(temporalPatternId, true);
								value = value + ", " + periodicalTemporalPattern.getPeriodDescription();
							} catch (ApplicationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} else {
					value = LangModelSchedule.getString("Text.Test.TemporalType.Sectional");
				}				
			}
			else if (key.equals(KEY_KIS)) {
				try {
					final KIS kis = (KIS) StorableObjectPool.getStorableObject(test.getKISId(), true);
					value = kis.getName();
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (key.equals(KEY_MONITORED_ELEMENT)) {
				value = test.getMonitoredElement().getName();
			}
			else if (key.equals(KEY_TEST_OBJECT)) {
				try {
					final MeasurementPort mp = (MeasurementPort) StorableObjectPool.getStorableObject(test.getMonitoredElement().getMeasurementPortId(),
							true);
					value = mp.getName();
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (key.equals(KEY_MEASUREMENT_TYPE)) {
				value = test.getMeasurementType().getDescription();
			} else if (key.equals(KEY_START_TIME)) {
				final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
				value = sdf.format(test.getStartTime());
			} else if (key.equals(KEY_STATUS)) {
				return test.getStatus();
			}
		}
		return value;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Test test, String key, Object value) {
		if (test != null) {
			//Nothing
		}
	}

}
