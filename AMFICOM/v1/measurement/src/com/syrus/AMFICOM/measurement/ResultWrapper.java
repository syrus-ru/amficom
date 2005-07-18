/*
 * $Id: ResultWrapper.java,v 1.12 2005/07/18 13:13:19 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;

/**
 * @version $Revision: 1.12 $, $Date: 2005/07/18 13:13:19 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class ResultWrapper extends StorableObjectWrapper {

	// measurementId VARCHAR2(32) NOT NULL,
	public static final String COLUMN_MEASUREMENT_ID = "measurement_id";
	// analysisId VARCHAR2(32),
	public static final String COLUMN_ANALYSIS_ID = "analysis_id";
	// evaluationId VARCHAR2(32),
	public static final String COLUMN_EVALUATION_ID = "evaluation_id";
	// modelingId VARCHAR2(32),
	public static final String COLUMN_MODELING_ID = "modeling_id";
	// sort NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_SORT = "sort";

	public static final String COLUMN_ACTION_ID = "action_id";

	public static final String LINK_COLUMN_RESULT_ID = "result_id";

	public static final String LINK_FIELD_RESULT_PARAMETERS = "result_parameters";
	public static final String LINK_COLUMN_PARAMETER_VALUE = "value";

	private static ResultWrapper instance;

	private List keys;

	private ResultWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_ACTION_ID, COLUMN_SORT, LINK_FIELD_RESULT_PARAMETERS};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
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

	@Override
	public Object getValue(final Object object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object instanceof Result) {
			final Result result = (Result) object;
			if (key.equals(COLUMN_ACTION_ID))
				return result.getAction();
			if (key.equals(COLUMN_SORT))
				return new Integer(result.getSort().value());
			if (key.equals(LINK_FIELD_RESULT_PARAMETERS)) {
				final Parameter[] parameters = result.getParameters();
				final Map<String, Object> values = new HashMap<String, Object>(parameters.length * 3);
				for (int i = 0; i < parameters.length; i++) {
					values.put(COLUMN_ID + i, parameters[i].getId());
					values.put(COLUMN_TYPE_ID + i, parameters[i].getType());
					values.put(LINK_COLUMN_PARAMETER_VALUE + i, parameters[i].getValue());
				}
				return values;
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Result) {
			final Result result = (Result) object;
			if (key.equals(COLUMN_ACTION_ID))
				result.setAction((Action) value);
			else
				if (key.equals(COLUMN_SORT))
					result.setSort(ResultSort.from_int(((Integer) value).intValue()));
				else
					if (key.equals(LINK_FIELD_RESULT_PARAMETERS)) {
						final Map resultParametersMap = (Map) value;
						/* there are 3*N keys for N Parameter */
						final Parameter[] resultParameters = new Parameter[resultParametersMap.size() / 3];
						for (int i = 0; i < resultParameters.length; i++) {
							final Identifier parameterId = (Identifier) resultParametersMap.get(COLUMN_ID + i);
							final ParameterType parameterType = (ParameterType) resultParametersMap.get(COLUMN_TYPE_ID + i);
							final byte[] resultParameterValue = (byte[]) resultParametersMap.get(LINK_COLUMN_PARAMETER_VALUE + i);

							resultParameters[i] = new Parameter(parameterId, parameterType, resultParameterValue);

						}
						result.setParameters(resultParameters);
					}
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

	@Override
	public Class getPropertyClass(String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_ACTION_ID))
			return Action.class;
		if (key.equals(COLUMN_SORT))
			return Integer.class;
		if (key.equals(LINK_FIELD_RESULT_PARAMETERS))
			return Map.class;
		return null;
	}

}
