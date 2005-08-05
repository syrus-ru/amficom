/*
 * $Id: EvaluationWrapper.java,v 1.11 2005/08/05 09:48:24 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.11 $, $Date: 2005/08/05 09:48:24 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class EvaluationWrapper extends StorableObjectWrapper<Evaluation> {

	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_MEASUREMENT_ID = "measurement_id";
	public static final String COLUMN_THRESHOLD_SET_ID = "threshold_set_id";

	private static EvaluationWrapper instance;

	private List<String> keys;

	private EvaluationWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_TYPE_ID,
				COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_MEASUREMENT_ID,
				COLUMN_THRESHOLD_SET_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EvaluationWrapper getInstance() {
		if (instance == null)
			instance = new EvaluationWrapper();
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
	public Object getValue(final Evaluation evaluation, final String key) {
		final Object value = super.getValue(evaluation, key);
		if (value == null && evaluation != null) {
			if (key.equals(COLUMN_TYPE_ID))
				return evaluation.getType();
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return evaluation.getMonitoredElementId();
			if (key.equals(COLUMN_MEASUREMENT_ID))
				return evaluation.getMeasurement();
			if (key.equals(COLUMN_THRESHOLD_SET_ID))
				return evaluation.getThresholdSet();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Evaluation evaluation, 
	                     final String key, 
	                     final Object value) {
		if (evaluation != null) {
			if (key.equals(COLUMN_TYPE_ID))
				evaluation.setType((ActionType) value);
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				evaluation.setMonitoredElementId(new Identifier((String) value));
			else if (key.equals(COLUMN_MEASUREMENT_ID))
				evaluation.setMeasurement((Measurement) value);
			else if (key.equals(COLUMN_THRESHOLD_SET_ID))
				evaluation.setThresholdSet((ParameterSet) value);
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
		if (key.equals(COLUMN_TYPE_ID))
			return EvaluationType.class;
		if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
			return Identifier.class;
		if (key.equals(COLUMN_MEASUREMENT_ID))
			return Measurement.class;
		if (key.equals(COLUMN_THRESHOLD_SET_ID))
			return ParameterSet.class;
		return null;
	}

}
