/*
 * $Id: ModelingWrapper.java,v 1.9 2005/08/05 09:48:24 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.9 $, $Date: 2005/08/05 09:48:24 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class ModelingWrapper extends StorableObjectWrapper<Modeling> {

	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_ARGUMENT_SET_ID = "argument_set_id";

	private static ModelingWrapper instance;

	private List<String> keys;

	private ModelingWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_TYPE_ID, COLUMN_MONITORED_ELEMENT_ID, COLUMN_ARGUMENT_SET_ID,
				COLUMN_NAME};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ModelingWrapper getInstance() {
		if (instance == null)
			instance = new ModelingWrapper();
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
	public Object getValue(final Modeling modeling, final String key) {
		final Object value = super.getValue(modeling, key);
		if (value == null && modeling != null) {
			if (key.equals(COLUMN_TYPE_ID))
				return modeling.getType();
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return modeling.getMonitoredElementId();
			if (key.equals(COLUMN_ARGUMENT_SET_ID))
				return modeling.getArgumentSet();
			if (key.equals(COLUMN_NAME))
				return modeling.getName();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Modeling modeling, 
	                     final String key, 
	                     final Object value) {
		if (modeling != null) {
			if (key.equals(COLUMN_TYPE_ID))
				modeling.setType((ActionType) value);
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				modeling.setMonitoredElementId((Identifier) value);
			else if (key.equals(COLUMN_ARGUMENT_SET_ID))
				modeling.setArgumentSet((ParameterSet) value);
			else if (key.equals(COLUMN_NAME))
				modeling.setName((String) value);
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
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_TYPE_ID)) {
			return ModelingType.class;
		}
		if (key.equals(COLUMN_MONITORED_ELEMENT_ID)) {
			return Identifier.class;
		}
		if (key.equals(COLUMN_ARGUMENT_SET_ID)) {
			return ParameterSet.class;
		}
		if (key.equals(COLUMN_NAME)) {
			return String.class;
		}
		return null;
	}

}
