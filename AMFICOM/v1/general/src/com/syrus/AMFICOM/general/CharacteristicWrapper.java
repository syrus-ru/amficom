/*
 * $Id: CharacteristicWrapper.java,v 1.13 2005/06/21 12:43:47 bass Exp $
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
 * @version $Revision: 1.13 $, $Date: 2005/06/21 12:43:47 $
 * @author $Author: bass $
 * @module general_v1
 */
public class CharacteristicWrapper extends StorableObjectWrapper {
	// table :: Characteristic
	// type_id VARCHAR2(32) NOT NULL,
	// name VARCHAR2(64) NOT NULL,
	// description VARCHAR2(256),
	// value VARCHAR2(256),
	public static final String				COLUMN_VALUE			= "value";
	// characterized_id VARCHAR2(32),
	public static final String				COLUMN_CHARACTERIZABLE_ID	= "characterizable_id";
	public static final String				COLUMN_EDITABLE			= "editable";
	public static final String				COLUMN_VISIBLE			= "visible";

	protected static CharacteristicWrapper	instance;

	protected List							keys;

	private CharacteristicWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_TYPE_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_VALUE,
				COLUMN_CHARACTERIZABLE_ID, COLUMN_EDITABLE, COLUMN_VISIBLE};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

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

	@Override
	public Object getValue(Object object, String key) {
		if (object instanceof Characteristic) {
			Characteristic characteristic = (Characteristic) object;
			if (key.equals(COLUMN_TYPE_ID))
				return characteristic.getType();
			else if (key.equals(COLUMN_NAME))
				return characteristic.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return characteristic.getDescription();
			else if (key.equals(COLUMN_CHARACTERIZABLE_ID))
				return characteristic.getCharacterizableId();
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
			else if (key.equals(COLUMN_CHARACTERIZABLE_ID))
				characteristic.setCharacterizableId((Identifier) value);
			else if (key.equals(COLUMN_EDITABLE))
				characteristic.setEditable(((Boolean) value).booleanValue());
			else if (key.equals(COLUMN_VISIBLE))
				characteristic.setVisible(((Boolean) value).booleanValue());
			else if (key.equals(COLUMN_VALUE))
				characteristic.setValue((String)value);
		}
	}
}
