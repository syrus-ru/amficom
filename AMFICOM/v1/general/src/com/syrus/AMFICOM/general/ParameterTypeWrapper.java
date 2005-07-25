/*
 * $Id: ParameterTypeWrapper.java,v 1.12 2005/07/25 18:10:03 arseniy Exp $
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
 * @version $Revision: 1.12 $, $Date: 2005/07/25 18:10:03 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class ParameterTypeWrapper extends StorableObjectWrapper {

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
	public Object getValue(final Object object, final String key) {
		if (object instanceof ParameterType) {
			ParameterType parameterType = (ParameterType) object;
			if (key.equals(StorableObjectWrapper.COLUMN_CODENAME))
				return parameterType.getCodename();
			else if (key.equals(StorableObjectWrapper.COLUMN_DESCRIPTION))
				return parameterType.getDescription();
			else if (key.equals(StorableObjectWrapper.COLUMN_NAME))
				return parameterType.getName();
			else if (key.equals(COLUMN_DATA_TYPE_CODE))
				return parameterType.getDataType();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof ParameterType) {
			ParameterType parameterType = (ParameterType) object;
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
