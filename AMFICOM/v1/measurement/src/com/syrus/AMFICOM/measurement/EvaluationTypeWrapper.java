/*
 * $Id: EvaluationTypeWrapper.java,v 1.6 2005/04/01 08:43:32 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.6 $, $Date: 2005/04/01 08:43:32 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class EvaluationTypeWrapper implements StorableObjectWrapper {

	public static final String MODE_IN = "IN";
	public static final String MODE_THRESHOLD = "THS";
	public static final String MODE_ETALON = "ETA";
	public static final String MODE_OUT = "OUT";
	public static final String LINK_COLUMN_EVALUATION_TYPE_ID = "evaluation_type_id";

	private static EvaluationTypeWrapper	instance;

	private List							keys;

	private EvaluationTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] {COLUMN_CODENAME, COLUMN_DESCRIPTION, MODE_IN, MODE_OUT, MODE_THRESHOLD, MODE_ETALON};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EvaluationTypeWrapper getInstance() {
		if (instance == null)
			instance = new EvaluationTypeWrapper();
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
		if (object instanceof EvaluationType) {
			EvaluationType evaluationType = (EvaluationType) object;
			if (key.equals(COLUMN_CODENAME))
				return evaluationType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return evaluationType.getDescription();
			if (key.equals(MODE_IN))
				return evaluationType.getInParameterTypes();
			if (key.equals(MODE_OUT))
				return evaluationType.getOutParameterTypes();
			if (key.equals(MODE_THRESHOLD))
				return evaluationType.getThresholdParameterTypes();
			if (key.equals(MODE_ETALON))
				return evaluationType.getEtalonParameterTypes();

		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof EvaluationType) {
			EvaluationType evaluationType = (EvaluationType) object;
			if (key.equals(COLUMN_CODENAME))
				evaluationType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				evaluationType.setDescription((String) value);
			else if (key.equals(MODE_IN))
				evaluationType.setInParameterTypes((java.util.Set) value);
			else if (key.equals(MODE_OUT))
				evaluationType.setOutParameterTypes((java.util.Set) value);
			else if (key.equals(MODE_THRESHOLD))
				evaluationType.setThresholdParameterTypes((java.util.Set) value);
			else if (key.equals(MODE_ETALON))
				evaluationType.setEtalonParameterTypes((java.util.Set) value);
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
		if (key.equals(MODE_IN) || key.equals(MODE_OUT) || key.equals(MODE_THRESHOLD) || key.equals(MODE_ETALON))
			return java.util.Set.class;
		return String.class;
	}

}
