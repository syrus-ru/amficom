/*
 * $Id: ResultWrapper.java,v 1.1 2005/01/28 06:51:13 bob Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/28 06:51:13 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class ResultWrapper implements Wrapper {

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
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_ACTION_ID, COLUMN_SORT};

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
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return result.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return result.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return result.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return result.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return result.getModifierId().getIdentifierString();
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
				try {
					result.setAction((Action) MeasurementStorableObjectPool.getStorableObject(new Identifier((String)value),true));
				} catch (ApplicationException e) {
					Log.errorMessage("ResultWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_SORT))
				result.setSort(ResultSort.from_int(Integer.parseInt((String)value)));
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
