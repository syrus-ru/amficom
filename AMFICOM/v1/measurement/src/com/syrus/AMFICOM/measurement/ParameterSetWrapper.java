/*
 * $Id: ParameterSetWrapper.java,v 1.15 2006/03/13 15:54:25 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;

/**
 * @version $Revision: 1.15 $, $Date: 2006/03/13 15:54:25 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ParameterSetWrapper extends StorableObjectWrapper<ParameterSet> {

	public static final String COLUMN_SORT = "sort";

	public static final String LINK_COLUMN_SET_ID = "set_id";
	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";

	public static final String LINK_FIELD_SET_PARAMETERS = "set_parameters";
	public static final String LINK_COLUMN_PARAMETER_VALUE = "value";

	private static ParameterSetWrapper instance;

	private List<String> keys;

	private ParameterSetWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_SORT,
				COLUMN_DESCRIPTION,
				LINK_COLUMN_MONITORED_ELEMENT_ID,
				LINK_FIELD_SET_PARAMETERS };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ParameterSetWrapper getInstance() {
		if (instance == null) {
			instance = new ParameterSetWrapper();
		}
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
	public Object getValue(final ParameterSet parameterSet, final String key) {
		final Object value = super.getValue(parameterSet, key);
		if (value == null && parameterSet != null) {
			if (key.equals(COLUMN_SORT)) {
				return new Integer(parameterSet.getSort().value());
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return parameterSet.getDescription();
			}
			if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID)) {
				return parameterSet.getMonitoredElementIds();
			}
			if (key.equals(LINK_FIELD_SET_PARAMETERS)) {
				final Parameter[] parameters = parameterSet.getParameters();
				final Map<String, Object> values = new HashMap<String, Object>(parameters.length * 3);
				for (int i = 0; i < parameters.length; i++) {
					values.put(COLUMN_ID + i, parameters[i].getId());
					values.put(COLUMN_TYPE_CODE + i, parameters[i].getType());
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

	@Override
	public void setValue(final ParameterSet parameterSet, final String key, final Object value) {
		if (parameterSet != null) {
			if (key.equals(COLUMN_SORT)) {
				parameterSet.setSort(ParameterSetSort.from_int(((Integer) value).intValue()));
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				parameterSet.setDescription((String) value);
			} else if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID)) {
				parameterSet.setMonitoredElementIds((Set) value);
			} else if (key.equals(LINK_FIELD_SET_PARAMETERS)) {
				Map setParameterMap = (Map) value;
				/* there are 3*N keys for N Parameter */
				final Parameter[] setParameters = new Parameter[setParameterMap.size() / 3];
				for (int i = 0; i < setParameters.length; i++) {
					final Identifier parameterId = (Identifier) setParameterMap.get(COLUMN_ID + i);
					final ParameterType parameterType = (ParameterType) setParameterMap.get(COLUMN_TYPE_CODE + i);
					final byte[] setParameterValue = (byte[]) setParameterMap.get(LINK_COLUMN_PARAMETER_VALUE + i);

					setParameters[i] = new Parameter(parameterId, parameterType, setParameterValue);

				}
				parameterSet.setParameters(setParameters);
			}
		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		if (key.equals(COLUMN_SORT)) {
			return Integer.class;
		}
		if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID)) {
			return Set.class;
		}
		if (key.equals(LINK_FIELD_SET_PARAMETERS)) {
			return Map.class;
		}
		return null;
	}

}
