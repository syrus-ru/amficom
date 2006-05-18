/*
 * $Id: CharacteristicTypeWrapper.java,v 1.23 2006/03/13 15:54:27 bass Exp $
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
 * @version $Revision: 1.23 $, $Date: 2006/03/13 15:54:27 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module general
 */
public final class CharacteristicTypeWrapper extends StorableObjectWrapper<CharacteristicType> {

	public static final String COLUMN_DATA_TYPE_CODE = "data_type_code";
	public static final String COLUMN_SORT = "sort";

	private static CharacteristicTypeWrapper instance;

	private List<String> keys;

	private CharacteristicTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_DATA_TYPE_CODE, COLUMN_SORT };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static CharacteristicTypeWrapper getInstance() {
		if (instance == null) {
			instance = new CharacteristicTypeWrapper();
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
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME)
				|| key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_DATA_TYPE_CODE)) {
			return DataType.class;	
		} else if (key.equals(COLUMN_SORT)) {
			return CharacteristicTypeSort.class;	
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Object getValue(final CharacteristicType characteristicType, final String key) {
		final Object value = super.getValue(characteristicType, key);
		if (value == null && characteristicType != null) {
			if (key.equals(COLUMN_CODENAME))
				return characteristicType.getCodename();
			else if (key.equals(COLUMN_DESCRIPTION))
				return characteristicType.getDescription();
			else if (key.equals(COLUMN_NAME))
				return characteristicType.getName();
			else if (key.equals(COLUMN_DATA_TYPE_CODE))
				return characteristicType.getDataType();
			else if (key.equals(COLUMN_SORT))
				return characteristicType.getSort();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final CharacteristicType characteristicType, final String key, final Object value) {
		if (characteristicType != null) {
			if (key.equals(COLUMN_CODENAME))
				characteristicType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				characteristicType.setDescription((String) value);
			else if (key.equals(COLUMN_NAME))
				characteristicType.setName((String) value);
			else if (key.equals(COLUMN_SORT))
				characteristicType.setSort((CharacteristicTypeSort) value);
			else if (key.equals(COLUMN_DATA_TYPE_CODE))
				characteristicType.setDataType((DataType) value);
		}
	}
}
