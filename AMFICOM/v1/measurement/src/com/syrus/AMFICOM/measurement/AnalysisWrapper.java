/*
 * $Id: AnalysisWrapper.java,v 1.10 2005/07/25 20:50:00 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.10 $, $Date: 2005/07/25 20:50:00 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class AnalysisWrapper extends StorableObjectWrapper {

	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_MEASUREMENT_ID = "measurement_id";
	public static final String COLUMN_CRITERIA_SET_ID = "criteria_set_id";

	private static AnalysisWrapper instance;

	private List<String> keys;

	private AnalysisWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] {COLUMN_TYPE_ID,
				COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_MEASUREMENT_ID,
				COLUMN_NAME,
				COLUMN_CRITERIA_SET_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static AnalysisWrapper getInstance() {
		if (instance == null)
			instance = new AnalysisWrapper();
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
	public Object getValue(final Object object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object instanceof Analysis) {
			final Analysis analysis = (Analysis) object;
			if (key.equals(COLUMN_TYPE_ID))
				return analysis.getType();
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return analysis.getMonitoredElementId();
			if (key.equals(COLUMN_MEASUREMENT_ID))
				return analysis.getMeasurement();
			if (key.equals(COLUMN_NAME))
				return analysis.getName();
			if (key.equals(COLUMN_CRITERIA_SET_ID))
				return analysis.getCriteriaSet();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof Analysis) {
			final Analysis analysis = (Analysis) object;
			if (key.equals(COLUMN_TYPE_ID))
				analysis.setType((ActionType) value);
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				analysis.setMonitoredElementId((Identifier) value);
			else if (key.equals(COLUMN_MEASUREMENT_ID))
				analysis.setMeasurement((Measurement) value);
			else if (key.equals(COLUMN_NAME))
				analysis.setName((String) value);
			else if (key.equals(COLUMN_CRITERIA_SET_ID))
				analysis.setCriteriaSet((ParameterSet) value);
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_TYPE_ID)) {
			return AnalysisType.class;
		}
		if (key.equals(COLUMN_MONITORED_ELEMENT_ID)) {
			return Identifier.class;
		}
		if (key.equals(COLUMN_MEASUREMENT_ID)) {
			return Measurement.class;
		}
		if (key.equals(COLUMN_NAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_CRITERIA_SET_ID)) {
			return Set.class;
		}
		return null;
	}

}
