/*
 * $Id: ParameterSetWrapper.java,v 1.1 2005/06/16 10:34:03 bass Exp $
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
import com.syrus.AMFICOM.measurement.corba.ParameterSet_TransferablePackage.ParameterSetSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/16 10:34:03 $
 * @author $Author: bass $
 * @module measurement_v1
 */
public class ParameterSetWrapper extends StorableObjectWrapper {

	public static final String	COLUMN_SORT						= "sort";

	public static final String	LINK_COLUMN_SET_ID				= "set_id";
	public static final String	LINK_COLUMN_MONITORED_ELEMENT_ID				= "monitored_element_id";

	public static final String	LINK_FIELD_SET_PARAMETERS		= "set_parameters";
	public static final String	LINK_COLUMN_PARAMETER_VALUE		= "value";

	private static ParameterSetWrapper	instance;

	private List				keys;

	private ParameterSetWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_SORT, COLUMN_DESCRIPTION, LINK_COLUMN_MONITORED_ELEMENT_ID,
				LINK_FIELD_SET_PARAMETERS};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ParameterSetWrapper getInstance() {
		if (instance == null)
			instance = new ParameterSetWrapper();
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
		if (object instanceof ParameterSet) {
			ParameterSet set = (ParameterSet) object;
			if (key.equals(COLUMN_SORT))
				return new Integer(set.getSort().value());
			if (key.equals(COLUMN_DESCRIPTION))
				return set.getDescription();
			if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID))
				return set.getMonitoredElementIds();
			if (key.equals(LINK_FIELD_SET_PARAMETERS)) {
				Parameter[] parameters = set.getParameters();
				Map values = new HashMap(parameters.length * 3);
				for (int i = 0; i < parameters.length; i++) {
					values.put(COLUMN_ID + i, parameters[i].getId());
					values.put(COLUMN_TYPE_ID + i, parameters[i].getType());
					values.put(LINK_COLUMN_PARAMETER_VALUE + i, parameters[i].getValue());
				}
				return values;
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof ParameterSet) {
			ParameterSet set = (ParameterSet) object;
			if (key.equals(COLUMN_SORT))
				set.setSort(ParameterSetSort.from_int(((Integer) value).intValue()));
			else
				if (key.equals(COLUMN_DESCRIPTION))
					set.setDescription((String) value);
				else
					if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID)) {
						set.setMonitoredElementIds((java.util.Set) value);
					}
					else
						if (key.equals(LINK_FIELD_SET_PARAMETERS)) {
							Map setParameterMap = (Map) value;
							/* there are 3*N keys for N Parameter */
							Parameter[] setParameters = new Parameter[setParameterMap.size() / 3];
							for (int i = 0; i < setParameters.length; i++) {
								Identifier parameterId = (Identifier) setParameterMap.get(COLUMN_ID + i);
								ParameterType parameterType = (ParameterType) setParameterMap.get(COLUMN_TYPE_ID + i);
								byte[] setParameterValue = (byte[]) setParameterMap.get(LINK_COLUMN_PARAMETER_VALUE + i);

								setParameters[i] = new Parameter(parameterId, parameterType, setParameterValue);

							}
							set.setParameters(setParameters);
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

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_SORT))
			return Integer.class;
		if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID))
			return java.util.Set.class;
		if (key.equals(LINK_FIELD_SET_PARAMETERS))
			return Map.class;
		return String.class;
	}

}
