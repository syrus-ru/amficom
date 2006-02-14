package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

public final class EquipmentTypeWrapper extends StorableObjectWrapper<EquipmentType> {

	private static EquipmentTypeWrapper instance;

	private List<String> keys;

	private EquipmentTypeWrapper() {
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EquipmentTypeWrapper getInstance() {
		if (instance == null) {
			instance = new EquipmentTypeWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final EquipmentType object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_CODENAME)) {
				return object.getCodename();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return object.getDescription();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public final void setValue(final EquipmentType object, final String key, final Object value) throws PropertyChangeException {
		if (object != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				object.setDescription((String) value);
			} else if (key.equals(COLUMN_CODENAME)) {
				object.setCodename((String) value);
			}
		}
	}

	public final String getKey(final int index) {
		return this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public final Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION) || key.equals(COLUMN_CODENAME)) {
			return String.class;
		}
		return null;
	}

}
