/*
 * $Id: ModelingTypeWrapper.java,v 1.9 2005/06/22 10:22:59 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/22 10:22:59 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class ModelingTypeWrapper extends StorableObjectWrapper {

	public static final String MODE_IN = "IN";
	public static final String MODE_OUT = "OUT";
	public static final String LINK_COLUMN_MODELING_TYPE_ID = "modeling_type_id";

	private static ModelingTypeWrapper	instance;

	private List						keys;

	private ModelingTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, MODE_IN, MODE_OUT};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ModelingTypeWrapper getInstance() {
		if (instance == null)
			instance = new ModelingTypeWrapper();
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
		Object value = super.getValue(object, key);
		if (value == null && object instanceof ModelingType) {
			ModelingType modelingType = (ModelingType) object;
			if (key.equals(COLUMN_CODENAME))
				return modelingType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return modelingType.getDescription();
			if (key.equals(MODE_IN))
				return modelingType.getInParameterTypeIds();
			if (key.equals(MODE_OUT))
				return modelingType.getOutParameterTypeIds();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof ModelingType) {
			ModelingType modelingType = (ModelingType) object;
			if (key.equals(COLUMN_CODENAME))
				modelingType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				modelingType.setDescription((String) value);
			else if (key.equals(MODE_IN))
				modelingType.setInParameterTypeIds((Set) value);
			else if (key.equals(MODE_OUT))
				modelingType.setOutParameterTypeIds((Set) value);
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
		Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(MODE_IN) || key.equals(MODE_OUT))
			return Set.class;
		return null;
	}

}
