/*
 * $Id: AnalysisTypeWrapper.java,v 1.14 2005/07/18 13:13:19 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.14 $, $Date: 2005/07/18 13:13:19 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class AnalysisTypeWrapper extends StorableObjectWrapper {

	public static final String LINK_COLUMN_ANALYSIS_TYPE_ID = "analysis_type_id";

	private static AnalysisTypeWrapper instance;

	private List keys;

	private AnalysisTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION };

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

	@Override
	public Object getValue(final Object object, final String key) {
		Object value = super.getValue(object, key);
		if (value == null && object instanceof AnalysisType) {
			AnalysisType analysisType = (AnalysisType) object;
			if (key.equals(COLUMN_CODENAME))
				return analysisType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return analysisType.getDescription();
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
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(String key) {
		Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		return null;
	}
}
