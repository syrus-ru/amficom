/*
 * $Id: AnalysisWrapper.java,v 1.2 2005/02/01 06:38:49 bob Exp $
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

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/01 06:38:49 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class AnalysisWrapper implements Wrapper {

	public static final String		COLUMN_TYPE_ID				= "type_id";

	public static final String		COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";

	public static final String		COLUMN_MEASUREMENT_ID		= "measurement_id";

	public static final String		COLUMN_CRITERIA_SET_ID		= "criteria_set_id";

	private static AnalysisWrapper	instance;

	private List					keys;

	private AnalysisWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_TYPE_ID, COLUMN_MONITORED_ELEMENT_ID, COLUMN_MEASUREMENT_ID,
				COLUMN_CRITERIA_SET_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static AnalysisWrapper getInstance() {
		if (instance == null)
			instance = new AnalysisWrapper();
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
		if (object instanceof Analysis) {
			Analysis analysis = (Analysis) object;
			if (key.equals(COLUMN_TYPE_ID))
				return analysis.getType();
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return analysis.getMonitoredElementId();
			if (key.equals(COLUMN_MEASUREMENT_ID))
				return analysis.getMeasurement();
			if (key.equals(COLUMN_CRITERIA_SET_ID))
				return analysis.getCriteriaSet();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Analysis) {
			Analysis analysis = (Analysis) object;
			if (key.equals(COLUMN_TYPE_ID))
				analysis.setType((ActionType) value);
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				analysis.setMonitoredElementId((Identifier) value);
			else if (key.equals(COLUMN_MEASUREMENT_ID))
				analysis.setMeasurement((Measurement) value);
			else if (key.equals(COLUMN_CRITERIA_SET_ID))
				analysis.setCriteriaSet((Set) value);
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
