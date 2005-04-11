/*
 * $Id: CharacteristicTypeWrapper.java,v 1.7 2005/04/11 11:48:18 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @version $Revision: 1.7 $, $Date: 2005/04/11 11:48:18 $
 * @author $Author: bob $
 * @module general_v1
 */
public class CharacteristicTypeWrapper extends StorableObjectWrapper {

	public static final String					COLUMN_DATA_TYPE	= "data_type";
	public static final String					COLUMN_SORT			= "sort";

	protected static CharacteristicTypeWrapper	instance;

	protected List								keys;

	private CharacteristicTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_DATA_TYPE, COLUMN_SORT};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static CharacteristicTypeWrapper getInstance() {
		if (instance == null)
			instance = new CharacteristicTypeWrapper();
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
		if (object instanceof CharacteristicType) {
			CharacteristicType characteristicType = (CharacteristicType) object;
			if (key.equals(COLUMN_CODENAME))
				return characteristicType.getCodename();
			else if (key.equals(COLUMN_DESCRIPTION))
				return characteristicType.getDescription();
			else if (key.equals(COLUMN_DATA_TYPE))
				return new Integer(characteristicType.getDataType().value());
			else if (key.equals(COLUMN_SORT))
				return new Integer(characteristicType.getSort().value());
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof CharacteristicType) {
			CharacteristicType characteristic = (CharacteristicType) object;
			if (key.equals(COLUMN_CODENAME))
				characteristic.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				characteristic.setDescription((String) value);
			else if (key.equals(COLUMN_SORT))
				characteristic.setSort0(CharacteristicTypeSort.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_DATA_TYPE))
				characteristic.setDataType0(DataType.from_int(((Integer) value).intValue()));
		}
	}

}
