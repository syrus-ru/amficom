/*
 * $Id: ParameterTypeWrapper.java,v 1.13 2005/08/05 09:46:13 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @version $Revision: 1.13 $, $Date: 2005/08/05 09:46:13 $
 * @author $Author: bob $
 * @module general_v1
 */
public class ParameterTypeWrapper extends StorableObjectWrapper<ParameterType> {

	public static final String COLUMN_DATA_TYPE_CODE = "data_type_code";

	protected static ParameterTypeWrapper instance;

	protected List<String> keys;

	private ParameterTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { 
				StorableObjectWrapper.COLUMN_CODENAME,
				StorableObjectWrapper.COLUMN_DESCRIPTION, 
				StorableObjectWrapper.COLUMN_NAME, 
				COLUMN_DATA_TYPE_CODE};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static ParameterTypeWrapper getInstance() {
		if (instance == null) {
			instance = new ParameterTypeWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public String getName(final String key) {
		/* there is no reason rename it */
		return key;
	}

	@Override
	public Class getPropertyClass(final String key) {
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Object getValue(final ParameterType parameterType, final String key) {
		final Object value = super.getValue(parameterType, key);
		if (value == null && parameterType != null) {
			if (key.equals(StorableObjectWrapper.COLUMN_CODENAME))
				return parameterType.getCodename();
			else if (key.equals(StorableObjectWrapper.COLUMN_DESCRIPTION))
				return parameterType.getDescription();
			else if (key.equals(StorableObjectWrapper.COLUMN_NAME))
				return parameterType.getName();
			else if (key.equals(COLUMN_DATA_TYPE_CODE))
				return parameterType.getDataType();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final ParameterType parameterType, 
	                     final String key, 
	                     final Object value) {
		if (parameterType != null) {
			if (key.equals(StorableObjectWrapper.COLUMN_CODENAME))
				parameterType.setCodename((String) value);
			else if (key.equals(StorableObjectWrapper.COLUMN_DESCRIPTION))
				parameterType.setDescription((String) value);
			else if (key.equals(StorableObjectWrapper.COLUMN_NAME))
				parameterType.setName((String) value);
			else if (key.equals(COLUMN_DATA_TYPE_CODE))
				parameterType.setDataType((DataType) value);
		}
	}

}
