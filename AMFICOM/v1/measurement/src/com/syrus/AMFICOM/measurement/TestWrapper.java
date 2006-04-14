/*
 * $Id: TestWrapper.java,v 1.27.2.6 2006/04/14 12:34:52 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.Test.TestStatus;
import com.syrus.AMFICOM.measurement.Test.TestTemporalType;

/**
 * @version $Revision: 1.27.2.6 $, $Date: 2006/04/14 12:34:52 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class TestWrapper extends StorableObjectWrapper<Test> {
	public static final String COLUMN_GROUP_TEST_ID = "group_test_id";
	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_TEMPORAL_TYPE = "temporal_type";
	public static final String COLUMN_START_TIME = "start_time";
	public static final String COLUMN_END_TIME = "end_time";
	public static final String COLUMN_TEMPORAL_PATTERN_ID = "temporal_pattern_id";
	public static final String COLUMN_MEASUREMENT_TYPE_ID = "measurement_type_id";
	public static final String LINK_COLUMN_MEASUREMENT_SETUP_ID = "measurement_setup_id";
	public static final String COLUMN_NUMBER_OF_MEASUREMENTS = "number_of_measurements";
	public static final String COLUMN_ANALYSIS_TYPE_ID = "analysis_type_id";

	public static final String LINK_COLUMN_TEST_ID = "test_id";

	private static TestWrapper instance;

	private List<String> keys;

	private TestWrapper() {
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION,
				COLUMN_GROUP_TEST_ID,
				COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_STATUS,
				COLUMN_TEMPORAL_TYPE,
				COLUMN_START_TIME,
				COLUMN_END_TIME,
				COLUMN_TEMPORAL_PATTERN_ID,
				COLUMN_MEASUREMENT_TYPE_ID,
				LINK_COLUMN_MEASUREMENT_SETUP_ID,
				COLUMN_NUMBER_OF_MEASUREMENTS,
				COLUMN_ANALYSIS_TYPE_ID };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static TestWrapper getInstance() {
		if (instance == null) {
			instance = new TestWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final Test test, final String key) {
		final Object value = super.getValue(test, key);
		if (value == null && test != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				return test.getDescription();
			}
			if (key.equals(COLUMN_GROUP_TEST_ID)) {
				return test.getGroupTestId();
			}
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID)) {
				return test.getMonitoredElementId();
			}
			if (key.equals(COLUMN_STATUS)) {
				return test.getStatus();
			}
			if (key.equals(COLUMN_TEMPORAL_TYPE)) {
				return test.getTemporalType();
			}
			if (key.equals(COLUMN_START_TIME)) {
				return test.getStartTime();
			}
			if (key.equals(COLUMN_END_TIME)) {
				return test.getEndTime();
			}
			if (key.equals(COLUMN_TEMPORAL_PATTERN_ID)) {
				return test.getTemporalPatternId();
			}
			if (key.equals(COLUMN_MEASUREMENT_TYPE_ID)) {
				return test.getMeasurementTypeId();
			}
			if (key.equals(LINK_COLUMN_MEASUREMENT_SETUP_ID)) {
				return test.getMeasurementSetupIds();
			}
			if (key.equals(COLUMN_NUMBER_OF_MEASUREMENTS)) {
				return new Integer(test.getNumberOfMeasurements());
			}
			if (key.equals(COLUMN_ANALYSIS_TYPE_ID)) {
				return test.getAnalysisTypeId();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final Test test, final String key, final Object value) {
		if (test != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				test.setDescription((String) value);
			} else if (key.equals(COLUMN_GROUP_TEST_ID)) {
				test.setGroupTestId((Identifier) value);
			} else if (key.equals(COLUMN_MONITORED_ELEMENT_ID)) {
				test.setMonitoredElementId((Identifier) value);
			} else if (key.equals(COLUMN_STATUS)) {
				test.setStatus((TestStatus) value);
			} else if (key.equals(COLUMN_START_TIME)) {
				test.setStartTime(new Date(Long.parseLong((String) value)));
			} else if (key.equals(COLUMN_END_TIME)) {
				test.setEndTime(new Date(Long.parseLong((String) value)));
			} else if (key.equals(COLUMN_TEMPORAL_PATTERN_ID)) {
				test.setTemporalPatternId((Identifier) value);
			} else if (key.equals(COLUMN_MEASUREMENT_TYPE_ID)) {
				test.setMeasurementType((Identifier) value);
			} else if (key.equals(COLUMN_ANALYSIS_TYPE_ID)) {
				test.setAnalysisTypeId((Identifier) value);
			}
		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		if (key.equals(COLUMN_GROUP_TEST_ID)
				|| key.equals(COLUMN_MONITORED_ELEMENT_ID)
				|| key.equals(COLUMN_TEMPORAL_PATTERN_ID)
				|| key.equals(COLUMN_MEASUREMENT_TYPE_ID)
				|| key.equals(COLUMN_ANALYSIS_TYPE_ID)) {
			return Identifier.class;
		}
		if (key.equals(LINK_COLUMN_MEASUREMENT_SETUP_ID)) {
			return Set.class;
		}
		if (key.equals(COLUMN_STATUS)) {
			return TestStatus.class;
		}
		if (key.equals(COLUMN_TEMPORAL_TYPE)) {
			return TestTemporalType.class;
		}
		if (key.equals(COLUMN_START_TIME)
				|| key.equals(COLUMN_END_TIME)) {
			return Date.class;
		}
		return null;
	}

}
