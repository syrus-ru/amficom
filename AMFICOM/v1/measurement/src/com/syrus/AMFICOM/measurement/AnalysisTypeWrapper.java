/*
 * $Id: AnalysisTypeWrapper.java,v 1.4 2005/02/03 08:36:47 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/03 08:36:47 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class AnalysisTypeWrapper implements StorableObjectWrapper {

	public static final String			MODE_IN				= "IN";
	public static final String			MODE_CRITERION		= "CRI";
	public static final String			MODE_ETALON			= "ETA";
	public static final String			MODE_OUT			= "OUT";

	private static AnalysisTypeWrapper	instance;

	private List						keys;

	private AnalysisTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, MODE_IN, MODE_OUT, MODE_CRITERION,
				MODE_ETALON};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
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
		if (object instanceof AnalysisType) {
			AnalysisType analysisType = (AnalysisType) object;
			if (key.equals(COLUMN_CODENAME))
				return analysisType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return analysisType.getDescription();
			if (key.equals(MODE_IN))
				return analysisType.getInParameterTypes();
			if (key.equals(MODE_OUT))
				return analysisType.getOutParameterTypes();
			if (key.equals(MODE_CRITERION))
				return analysisType.getCriteriaParameterTypes();
			if (key.equals(MODE_ETALON))
				return analysisType.getEtalonParameterTypes();

		}
		return null;
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
				analysisType.setInParameterTypes((List) value);
			else if (key.equals(MODE_OUT))
				analysisType.setOutParameterTypes((List) value);
			else if (key.equals(MODE_CRITERION))
				analysisType.setCriteriaParameterTypes((List) value);
			else if (key.equals(MODE_ETALON))
				analysisType.setEtalonParameterTypes((List) value);
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
		if (key.equals(MODE_IN) || key.equals(MODE_OUT) || key.equals(MODE_CRITERION) || key.equals(MODE_ETALON))
			return List.class;
		return String.class;
	}

}
