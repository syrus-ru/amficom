/*
 * $Id: AnalysisTypeWrapper.java,v 1.12 2005/06/22 10:22:59 bob Exp $
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

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.12 $, $Date: 2005/06/22 10:22:59 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class AnalysisTypeWrapper extends StorableObjectWrapper {

	public static final String MODE_IN = "IN";
	public static final String MODE_CRITERION = "CRI";
	public static final String MODE_ETALON = "ETA";
	public static final String MODE_OUT = "OUT";
	public static final String LINK_COLUMN_ANALYSIS_TYPE_ID = "analysis_type_id";

	private static AnalysisTypeWrapper	instance;

	private List						keys;

	private AnalysisTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] {COLUMN_CODENAME, COLUMN_DESCRIPTION, MODE_IN, MODE_OUT, MODE_CRITERION, MODE_ETALON};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static AnalysisTypeWrapper getInstance() {
		if (instance == null)
			instance = new AnalysisTypeWrapper();
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
		Object value = super.getValue(object, key);
		if (value == null && object instanceof AnalysisType) {
			AnalysisType analysisType = (AnalysisType) object;
			if (key.equals(COLUMN_CODENAME))
				return analysisType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return analysisType.getDescription();
			if (key.equals(MODE_IN))
				return analysisType.getInParameterTypeIds();
			if (key.equals(MODE_OUT))
				return analysisType.getOutParameterTypeIds();
			if (key.equals(MODE_CRITERION))
				return analysisType.getCriteriaParameterTypeIds();
			if (key.equals(MODE_ETALON))
				return analysisType.getEtalonParameterTypeIds();

		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof AnalysisType) {
			AnalysisType analysisType = (AnalysisType) object;
			if (key.equals(COLUMN_CODENAME))
				analysisType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				analysisType.setDescription((String) value);
			else if (key.equals(MODE_IN))
				analysisType.setInParameterTypeIds((Set) value);
			else if (key.equals(MODE_OUT))
				analysisType.setOutParameterTypeIds((Set) value);
			else if (key.equals(MODE_CRITERION))
				analysisType.setCriteriaParameterTypeIds((Set) value);
			else if (key.equals(MODE_ETALON))
				analysisType.setEtalonParameterTypeIds((Set) value);
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(String key) {
		Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(MODE_IN) 
				|| key.equals(MODE_OUT) 
				|| key.equals(MODE_CRITERION) 
				|| key.equals(MODE_ETALON)) {
			return Set.class;
		}
		return null;
	}
}
