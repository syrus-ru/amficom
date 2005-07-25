/*
 * $Id: AnalysisTypeWrapper.java,v 1.15 2005/07/25 20:50:00 arseniy Exp $
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
 * @version $Revision: 1.15 $, $Date: 2005/07/25 20:50:00 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class AnalysisTypeWrapper extends StorableObjectWrapper {

	public static final String LINK_COLUMN_ANALYSIS_TYPE_ID = "analysis_type_id";

	private static AnalysisTypeWrapper instance;

	private List<String> keys;

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
		if (value == null && object instanceof AnalysisType) {
			final AnalysisType analysisType = (AnalysisType) object;
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

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof AnalysisType) {
			final AnalysisType analysisType = (AnalysisType) object;
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

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
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
