/*
 * $Id: ResultWrapper.java,v 1.3 2005/02/03 08:36:47 bob Exp $
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
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/03 08:36:47 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class ResultWrapper implements StorableObjectWrapper {

	// measurementId VARCHAR2(32) NOT NULL,
	public static final String		COLUMN_MEASUREMENT_ID	= "measurement_id";
	// analysisId VARCHAR2(32),
	public static final String		COLUMN_ANALYSIS_ID		= "analysis_id";
	// evaluationId VARCHAR2(32),
	public static final String		COLUMN_EVALUATION_ID	= "evaluation_id";
	// modelingId VARCHAR2(32),
	public static final String		COLUMN_MODELING_ID		= "modeling_id";
	// sort NUMBER(2, 0) NOT NULL,
	public static final String		COLUMN_SORT				= "sort";

	public static final String		COLUMN_ACTION_ID		= "action_id";

	private static ResultWrapper	instance;

	private List					keys;

	private ResultWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_ACTION_ID, COLUMN_SORT};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static ResultWrapper getInstance() {
		if (instance == null)
			instance = new ResultWrapper();
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
		if (object instanceof Result) {
			Result result = (Result) object;
			if (key.equals(COLUMN_ACTION_ID))
				return result.getAction().getId();
			if (key.equals(COLUMN_SORT))
				return Integer.toString(result.getSort().value());

		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Result) {
			Result result = (Result) object;
			if (key.equals(COLUMN_ACTION_ID))
				result.setAction((Action) value);
			else if (key.equals(COLUMN_SORT))
				result.setSort(ResultSort.from_int(((Integer) value).intValue()));
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
