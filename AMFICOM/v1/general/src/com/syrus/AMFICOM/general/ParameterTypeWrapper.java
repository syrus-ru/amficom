/*
 * $Id: ParameterTypeWrapper.java,v 1.1 2005/01/24 15:29:27 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/01/24 15:29:27 $
 * @author $Author: bob $
 * @module general_v1
 */
public class ParameterTypeWrapper implements Wrapper {

	public static final String				COLUMN_CODENAME		= "codename";
	public static final String				COLUMN_DESCRIPTION	= "description";
	public static final String				COLUMN_NAME			= "name";
	public static final String				COLUMN_DATA_TYPE	= "data_type";

	protected static ParameterTypeWrapper	instance;

	protected List							keys;

	private ParameterTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_MODIFIED, StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_DATA_TYPE};

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
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return parameterType.getId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(parameterType.getCreated().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(parameterType.getModified().getTime());
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return parameterType.getCreatorId().getIdentifierString();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return parameterType.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_CODENAME))
				return parameterType.getCodename();
			else if (key.equals(COLUMN_DESCRIPTION))
				return parameterType.getDescription();
			else if (key.equals(COLUMN_NAME))
				return parameterType.getName();
			else if (key.equals(COLUMN_DATA_TYPE))
				return Integer.toString(parameterType.getDataType().value());
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof ParameterType) {
			ParameterType parameterType = (ParameterType) object;
			if (key.equals(COLUMN_CODENAME))
				parameterType.setCodename0((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				parameterType.setDescription0((String) value);
			else if (key.equals(COLUMN_NAME))
				parameterType.setName((String) value);
			else if (key.equals(COLUMN_DATA_TYPE))
				parameterType.setDataType0(DataType.from_int(Integer.parseInt((String) value)));
		}
	}

}
