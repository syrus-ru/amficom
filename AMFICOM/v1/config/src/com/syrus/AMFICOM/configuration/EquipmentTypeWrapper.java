/*
 * $Id: EquipmentTypeWrapper.java,v 1.18 2005/09/14 18:42:07 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.18 $, $Date: 2005/09/14 18:42:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */

public final class EquipmentTypeWrapper extends StorableObjectWrapper<EquipmentType> {

	// manufacturer VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER = "manufacturer";

	// manufacturer_code VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER_CODE = "manufacturer_code";

	private static EquipmentTypeWrapper instance;

	private List<String> keys;

	private EquipmentTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME,
				COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_MANUFACTURER,
				COLUMN_MANUFACTURER_CODE };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EquipmentTypeWrapper getInstance() {
		if (instance == null)
			instance = new EquipmentTypeWrapper();
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
	public Object getValue(final EquipmentType equipmentType, final String key) {
		final Object value = super.getValue(equipmentType, key);
		if (value == null && equipmentType != null) {
			if (key.equals(COLUMN_CODENAME))
				return equipmentType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return equipmentType.getDescription();
			if (key.equals(COLUMN_NAME))
				return equipmentType.getName();
			if (key.equals(COLUMN_MANUFACTURER))
				return equipmentType.getManufacturer();
			if (key.equals(COLUMN_MANUFACTURER_CODE))
				return equipmentType.getManufacturerCode();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final EquipmentType equipmentType, final String key, final Object value) {
		if (equipmentType != null) {
			if (key.equals(COLUMN_NAME))
				equipmentType.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				equipmentType.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				equipmentType.setCodename((String) value);
			else if (key.equals(COLUMN_MANUFACTURER))
				equipmentType.setManufacturer((String) value);
			else if (key.equals(COLUMN_MANUFACTURER_CODE))
				equipmentType.setManufacturerCode((String) value);
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_MANUFACTURER)
				|| key.equals(COLUMN_MANUFACTURER_CODE)) {
			return String.class;
		}
		return null;
	}
}
