/*
 * $Id: ParameterTypeWrapper.java,v 1.4 2005/02/03 08:37:26 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/03 08:37:26 $
 * @author $Author: bob $
 * @module general_v1
 */
public class ParameterTypeWrapper implements StorableObjectWrapper {

	public static final String				COLUMN_DATA_TYPE	= "data_type";

	protected static ParameterTypeWrapper	instance;

	protected List							keys;

	private ParameterTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectWrapper.COLUMN_CODENAME,
				StorableObjectWrapper.COLUMN_DESCRIPTION, StorableObjectWrapper.COLUMN_NAME, COLUMN_DATA_TYPE};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static ParameterTypeWrapper getInstance() {
		if (instance == null)
			instance = new ParameterTypeWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	public String getName(String key) {
		/* there is no reason rename it */
		return key;
	}

	public Class getPropertyClass(String key) {
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Object getValue(Object object, String key) {
		if (object instanceof ParameterType) {
			ParameterType parameterType = (ParameterType) object;
			if (key.equals(StorableObjectWrapper.COLUMN_CODENAME))
				return parameterType.getCodename();
			else if (key.equals(StorableObjectWrapper.COLUMN_DESCRIPTION))
				return parameterType.getDescription();
			else if (key.equals(StorableObjectWrapper.COLUMN_NAME))
				return parameterType.getName();
			else if (key.equals(COLUMN_DATA_TYPE))
				return new Integer(parameterType.getDataType().value());
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof ParameterType) {
			ParameterType parameterType = (ParameterType) object;
			if (key.equals(StorableObjectWrapper.COLUMN_CODENAME))
				parameterType.setCodename((String) value);
			else if (key.equals(StorableObjectWrapper.COLUMN_DESCRIPTION))
				parameterType.setDescription((String) value);
			else if (key.equals(StorableObjectWrapper.COLUMN_NAME))
				parameterType.setName((String) value);
			else if (key.equals(COLUMN_DATA_TYPE))
				parameterType.setDataType(DataType.from_int(((Integer) value).intValue()));
		}
	}

}
