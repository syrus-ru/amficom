/*
 * $Id: CharacteristicWrapper.java,v 1.7 2005/02/07 14:15:18 bob Exp $
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

import com.syrus.AMFICOM.general.corba.CharacteristicSort;

/**
 * @version $Revision: 1.7 $, $Date: 2005/02/07 14:15:18 $
 * @author $Author: bob $
 * @module general_v1
 */
public class CharacteristicWrapper implements StorableObjectWrapper {

	// table :: Characteristic
	// type_id VARCHAR2(32) NOT NULL,
	// name VARCHAR2(64) NOT NULL,
	// description VARCHAR2(256),
	// sort NUMBER(2) NOT NULL,
	public static final String				COLUMN_SORT				= "sort";
	// value VARCHAR2(256),
	public static final String				COLUMN_VALUE			= "value";
	// characterized_id VARCHAR2(32),
	public static final String				COLUMN_CHARACTERIZED_ID	= "characterized_id";
	public static final String				COLUMN_EDITABLE			= "editable";
	public static final String				COLUMN_VISIBLE			= "visible";

	protected static CharacteristicWrapper	instance;

	protected List							keys;

	private CharacteristicWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_TYPE_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_SORT, COLUMN_VALUE,
				COLUMN_CHARACTERIZED_ID, COLUMN_EDITABLE, COLUMN_VISIBLE};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static CharacteristicWrapper getInstance() {
		if (instance == null)
			instance = new CharacteristicWrapper();
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
		if (object instanceof Characteristic) {
			Characteristic characteristic = (Characteristic) object;
			if (key.equals(COLUMN_TYPE_ID))
				return characteristic.getType();
			else if (key.equals(COLUMN_NAME))
				return characteristic.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return characteristic.getDescription();
			else if (key.equals(COLUMN_SORT))
				return new Integer(characteristic.getSort().value());
			else if (key.equals(COLUMN_CHARACTERIZED_ID))
				return characteristic.getCharacterizedId();
			else if (key.equals(COLUMN_EDITABLE))
				return new Boolean(characteristic.isEditable());
			else if (key.equals(COLUMN_VISIBLE))
				return new Boolean(characteristic.isVisible());
			else if (key.equals(COLUMN_VALUE))
				return characteristic.getValue();
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof Characteristic) {
			Characteristic characteristic = (Characteristic) object;
			if (key.equals(COLUMN_TYPE_ID))
				characteristic.setType((CharacteristicType) value);
			else if (key.equals(COLUMN_NAME))
				characteristic.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				characteristic.setDescription0((String) value);
			else if (key.equals(COLUMN_SORT))
				characteristic.setSort(CharacteristicSort.from_int(((Integer)value).intValue()));
			else if (key.equals(COLUMN_CHARACTERIZED_ID))
				characteristic.setCharacterizedId((Identifier) value);
			else if (key.equals(COLUMN_EDITABLE))
				characteristic.setEditable(((Boolean) value).booleanValue());
			else if (key.equals(COLUMN_VISIBLE))
				characteristic.setVisible(((Boolean) value).booleanValue());
			else if (key.equals(COLUMN_VALUE))
				characteristic.setValue((String)value);
		}
	}

}
