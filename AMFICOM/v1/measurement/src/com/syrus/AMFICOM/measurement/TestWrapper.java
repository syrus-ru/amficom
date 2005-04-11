/*
 * $Id: TestWrapper.java,v 1.10 2005/04/11 11:49:13 bob Exp $
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

import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;

/**
 * @version $Revision: 1.10 $, $Date: 2005/04/11 11:49:13 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class TestWrapper extends StorableObjectWrapper {

	public static final String COLUMN_ANALYSIS_TYPE_ID = "analysis_type_id";
	public static final String COLUMN_END_TIME = "end_time";
	public static final String COLUMN_EVALUATION_TYPE_ID = "evaluation_type_id";
	public static final String COLUMN_MEASUREMENT_TYPE_ID = "measurement_type_id";
	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_RETURN_TYPE = "return_type";
	public static final String COLUMN_START_TIME = "start_time";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_TEMPORAL_PATTERN_ID = "temporal_pattern_id";
	public static final String COLUMN_TEMPORAL_TYPE = "temporal_type";
	public static final String COLUMN_NUMBER_OF_MEASUREMENTS = "number_of_measurements";
	public static final String LINK_COLUMN_TEST_ID = "test_id";
	public static final String LINK_COLUMN_MEASUREMENT_SETUP_ID = "measurement_setup_id";

	private static TestWrapper instance;

	private List keys;

	private TestWrapper() {
		// empty private constructor
		String[] keysArray = new String[] {COLUMN_TEMPORAL_TYPE,
				COLUMN_START_TIME,
				COLUMN_END_TIME,
				COLUMN_TEMPORAL_PATTERN_ID,
				COLUMN_MEASUREMENT_TYPE_ID,
				COLUMN_ANALYSIS_TYPE_ID,
				COLUMN_EVALUATION_TYPE_ID,
				COLUMN_STATUS,
				COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_RETURN_TYPE,
				COLUMN_DESCRIPTION,
				COLUMN_NUMBER_OF_MEASUREMENTS,
				LINK_COLUMN_MEASUREMENT_SETUP_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static TestWrapper getInstance() {
		if (instance == null)
			instance = new TestWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	public Object getValue(final Object object, final String key) {
		if (object instanceof Test) {
			Test test = (Test) object;
			if (key.equals(COLUMN_TEMPORAL_TYPE))
				return new Integer(test.getTemporalType().value());
			if (key.equals(COLUMN_START_TIME))
				return test.getStartTime();
			if (key.equals(COLUMN_END_TIME)) {
				Date endTime = test.getEndTime();
				return endTime == null ? test.getStartTime() : endTime;
			}
			if (key.equals(COLUMN_TEMPORAL_PATTERN_ID))
				return test.getTemporalPatternId();
			if (key.equals(COLUMN_MEASUREMENT_TYPE_ID))
				return test.getMeasurementTypeId();
			if (key.equals(COLUMN_ANALYSIS_TYPE_ID))
				return test.getAnalysisTypeId();
			if (key.equals(COLUMN_EVALUATION_TYPE_ID))
				return test.getEvaluationTypeId();
			if (key.equals(COLUMN_STATUS))
				return new Integer(test.getStatus().value());
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return test.getMonitoredElement();
			if (key.equals(COLUMN_RETURN_TYPE))
				return new Integer(test.getReturnType().value());
			if (key.equals(COLUMN_DESCRIPTION))
				return test.getDescription();
			if (key.equals(COLUMN_NUMBER_OF_MEASUREMENTS))
				return new Integer(test.getNumberOfMeasurements());
			if (key.equals(LINK_COLUMN_MEASUREMENT_SETUP_ID))
				return test.getMeasurementSetupIds();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Test) {
			Test test = (Test) object;
			if (key.equals(COLUMN_TEMPORAL_TYPE))
				test.setTemporalType(TestTemporalType.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_START_TIME))
				test.setStartTime(new Date(Long.parseLong((String) value)));
			else if (key.equals(COLUMN_END_TIME))
				test.setEndTime(new Date(Long.parseLong((String) value)));
			else if (key.equals(COLUMN_TEMPORAL_PATTERN_ID))
				test.setTemporalPatternId((Identifier) value);
			else if (key.equals(COLUMN_MEASUREMENT_TYPE_ID))
				test.setMeasurementTypeId((Identifier) value);
			else if (key.equals(COLUMN_ANALYSIS_TYPE_ID))
				test.setAnalysisTypeId((Identifier) value);
			else if (key.equals(COLUMN_EVALUATION_TYPE_ID))
				test.setEvaluationTypeId((Identifier) value);
			else if (key.equals(COLUMN_STATUS))
				test.setStatus(TestStatus.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				test.setMonitoredElement((MonitoredElement) value);
			else if (key.equals(COLUMN_RETURN_TYPE))
				test.setReturnType(TestReturnType.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_DESCRIPTION))
				test.setDescription((String) value);
			else if (key.equals(LINK_COLUMN_MEASUREMENT_SETUP_ID))
				test.setMeasurementSetupIds((java.util.Set) value);
		}
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(String key) {
		return String.class;
	}

}
