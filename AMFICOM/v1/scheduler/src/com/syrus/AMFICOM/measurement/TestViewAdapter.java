/*
 * $Id: TestViewAdapter.java,v 1.12 2006/03/13 15:54:25 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.UI.ComparableLabel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.12 $, $Date: 2006/03/13 15:54:25 $
 * @author $Author: bass $
 * @module module
 */
public class TestViewAdapter implements Wrapper<TestView> {

	public static final String KEY_TEMPORAL_TYPE = "TemporalType";
	public static final String KEY_TEMPORAL_TYPE_NAME = "TemporalTypeName";
	public static final String KEY_KIS = "RTU";
	public static final String KEY_MONITORED_ELEMENT = "MonitoredElement";
	public static final String KEY_PORT = "Port";
	public static final String KEY_MEASUREMENT_TYPE = "MeasurementType";
	public static final String KEY_START_TIME = "TestStartTime";
	public static final String KEY_END_TIME = "TestEndTime";
	public static final String KEY_STATUS = "Status";
	public static final String KEY_Q = "Q";
	public static final String KEY_D = "d";

	private List<String> keys;

	private static TestViewAdapter instance = null;

	private Map<Component, TestStatus> statusMap;

	private TestViewAdapter() {

		final String[] keysArray = new String[] { KEY_TEMPORAL_TYPE, 
				KEY_MONITORED_ELEMENT, 
				KEY_PORT,
				KEY_MEASUREMENT_TYPE,
				KEY_START_TIME,
				KEY_END_TIME,
				KEY_STATUS,
				KEY_D,
				KEY_Q};
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

		this.statusMap = new HashMap<Component, TestStatus>();
		this.addStatusItem(TestStatus.TEST_STATUS_ABORTED);
		this.addStatusItem(TestStatus.TEST_STATUS_COMPLETED);
		this.addStatusItem(TestStatus.TEST_STATUS_NEW);
		this.addStatusItem(TestStatus.TEST_STATUS_PROCESSING);
		this.addStatusItem(TestStatus.TEST_STATUS_SCHEDULED);
		this.addStatusItem(TestStatus.TEST_STATUS_STOPPING);
		this.addStatusItem(TestStatus.TEST_STATUS_STOPPED);
	}

	private Component getStatusComponent(final TestStatus testStatus) {
		final ComparableLabel label = 
			new ComparableLabel(SchedulerModel.getStatusName(testStatus));
		label.setOpaque(true);
		final Color color = SchedulerModel.getColor(testStatus);
		label.setBackground(color.brighter());
		return label;
	}

	private void addStatusItem(final TestStatus testStatus) {
		this.statusMap.put(this.getStatusComponent(testStatus), testStatus);
	}

	public static synchronized TestViewAdapter getInstance() {
		if (instance == null) {
			instance = new TestViewAdapter();
		}
		return instance;
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		String name = null;
		key = key != null ? key.intern() : null;
		if (key == KEY_TEMPORAL_TYPE) {
			name = I18N.getString("Scheduler.Text.Test.Field.TemporalType"); //$NON-NLS-1$
		} else if (key == KEY_KIS) {
			name = I18N.getString("Scheduler.Text.Test.Field.RTU"); //$NON-NLS-1$
		} else if (key == KEY_MONITORED_ELEMENT) {
			name = I18N.getString("Scheduler.Text.Test.Field.TestingLine"); //$NON-NLS-1$
		} else if (key == KEY_PORT) {
			name = I18N.getString("Scheduler.Text.Test.Field.Port"); //$NON-NLS-1$
		} else if (key == KEY_MEASUREMENT_TYPE) {
			name = I18N.getString("Scheduler.Text.Test.Field.MeasurementType"); //$NON-NLS-1$
		} else if (key == KEY_START_TIME) {
			name = I18N.getString("Scheduler.Text.Test.Field.TimeOfTheFirstMeasurement"); //$NON-NLS-1$
		} else if (key == KEY_END_TIME) {
			name = I18N.getString("Scheduler.Text.Test.Field.TimeOfTheFinishMeasurement"); //$NON-NLS-1$
		} else if (key == KEY_STATUS) {
			name = I18N.getString("Scheduler.Text.Test.Field.Status"); //$NON-NLS-1$
		} else if (key == KEY_D) {
			name = key + ", " + I18N.getString("Scheduler.Text.Table.dMeasurementUnit");
		} else if (key == KEY_Q) {
			name = I18N.getString("Scheduler.Text.Test.Field.LineQuality");
		}

		return name;
	}

	public Class<?> getPropertyClass(String key) {
		Class<?> clazz = String.class;
		key = key != null ? key.intern() : null;
		if (key == KEY_STATUS) {
			clazz = Map.class;
		} else if (key == KEY_START_TIME || key == KEY_END_TIME) {
			clazz = Date.class;
		}
		return clazz;
	}

	public Object getPropertyValue(String key) {
		Object value = null;
		key = key != null ? key.intern() : null;
		if (key == KEY_STATUS) {
			value = this.statusMap;
		}
		return value;
	}

	public void setPropertyValue(final String key, 
	                             final Object objectKey, 
	                             final Object objectValue) {
		// TODO Auto-generated method stub
	}

	public Object getValue(final TestView testView, 
	                       String key) {
		if (testView != null) {
			key = key != null ? key.intern() : null;
			final Test test = testView.getTest();
			if (key == KEY_TEMPORAL_TYPE) {
				return testView.getTemporalType();
			} else if (key == KEY_TEMPORAL_TYPE_NAME) {
				return testView.getTemporalName();
			} else if (key == KEY_KIS) {
				return testView.getKISName();
			} else if (key == KEY_MONITORED_ELEMENT) {
				return testView.getMonitoredElementName();
			} else if (key == KEY_PORT) {
				return testView.getPortName();
			} else if (key == KEY_MEASUREMENT_TYPE) {
				return test.getMeasurementType().getDescription();
			} else if (key == KEY_START_TIME) {
				return testView.getFirstDate();
			} else if (key == KEY_END_TIME) {
				return testView.getLastDate();
			} else if (key == KEY_STATUS) {
				return test.getStatus();
			} else if (key == KEY_D) {
				return testView.getTestD();
			} else if (key == KEY_Q) {
				return testView.getTestQ();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final TestView test, 
	                     final String key, 
	                     final Object value) {
		if (test != null) {
			//Nothing
		}
	}

}
