/*
 * $Id: ParameterTypeWrapper.java,v 1.2 2005/01/31 13:53:19 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/01/31 13:53:19 $
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
				return parameterType.getId();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return parameterType.getCreated();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return parameterType.getModified();
			else if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return parameterType.getCreatorId();
			else if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return parameterType.getModifierId();
			else if (key.equals(COLUMN_CODENAME))
				return parameterType.getCodename();
			else if (key.equals(COLUMN_DESCRIPTION))
				return parameterType.getDescription();
			else if (key.equals(COLUMN_NAME))
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
			if (key.equals(COLUMN_CODENAME))
				parameterType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				parameterType.setDescription((String) value);
			else if (key.equals(COLUMN_NAME))
				parameterType.setName((String) value);
			else if (key.equals(COLUMN_DATA_TYPE))
				parameterType.setDataType(DataType.from_int(((Integer)value).intValue()));
		}
	}

}
