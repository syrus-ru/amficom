/*
 * $Id: CharacteristicWrapper.java,v 1.26 2006/03/13 15:54:27 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.26 $, $Date: 2006/03/13 15:54:27 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class CharacteristicWrapper extends StorableObjectWrapper<Characteristic> {
	// table :: Characteristic
	// type_id VARCHAR2(32) NOT NULL,
	// name VARCHAR2(64) NOT NULL,
	// description VARCHAR2(256),
	// value VARCHAR2(256),
	public static final String COLUMN_VALUE = "value";
	// characterized_id VARCHAR2(32),
	public static final String COLUMN_CHARACTERIZABLE_ID = "characterizable_id";
	public static final String COLUMN_EDITABLE = "editable";
	public static final String COLUMN_VISIBLE = "visible";

	private static CharacteristicWrapper instance;

	private final List<String> keys;

	private CharacteristicWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_TYPE_ID,
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_VALUE,
				COLUMN_CHARACTERIZABLE_ID,
				COLUMN_EDITABLE,
				COLUMN_VISIBLE };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static CharacteristicWrapper getInstance() {
		if (instance == null) {
			instance = new CharacteristicWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
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
		if (key.equals(COLUMN_TYPE_ID)) {
			return CharacteristicType.class;
		} else if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_VALUE)) {
			return String.class;
		} else if (key.equals(COLUMN_CHARACTERIZABLE_ID)) {
			return Identifier.class;
		} else if (key.equals(COLUMN_EDITABLE)
				|| key.equals(COLUMN_VISIBLE)) {
			return Boolean.class;
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
	public Object getValue(final Characteristic characteristic, final String key) {
		final Object value = super.getValue(characteristic, key);
		if (value == null && characteristic != null) {
			if (key.equals(COLUMN_TYPE_ID))
				return characteristic.getType();
			else if (key.equals(COLUMN_NAME))
				return characteristic.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return characteristic.getDescription();
			else if (key.equals(COLUMN_CHARACTERIZABLE_ID))
				return characteristic.getParentCharacterizableId();
			else if (key.equals(COLUMN_EDITABLE))
				return new Boolean(characteristic.isEditable());
			else if (key.equals(COLUMN_VISIBLE))
				return new Boolean(characteristic.isVisible());
			else if (key.equals(COLUMN_VALUE))
				return characteristic.getValue();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final Characteristic characteristic, final String key, final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (characteristic != null) {
				if (key.equals(COLUMN_TYPE_ID))
					characteristic.setType((CharacteristicType) value);
				else if (key.equals(COLUMN_NAME))
					characteristic.setName((String) value);
				else if (key.equals(COLUMN_DESCRIPTION))
					characteristic.setDescription0((String) value);
				else if (key.equals(COLUMN_CHARACTERIZABLE_ID))
					characteristic.setParentCharacterizableId((Identifier) value, usePool);
				else if (key.equals(COLUMN_EDITABLE))
					characteristic.setEditable(((Boolean) value).booleanValue());
				else if (key.equals(COLUMN_VISIBLE))
					characteristic.setVisible(((Boolean) value).booleanValue());
				else if (key.equals(COLUMN_VALUE))
					characteristic.setValue((String) value);
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}
}
