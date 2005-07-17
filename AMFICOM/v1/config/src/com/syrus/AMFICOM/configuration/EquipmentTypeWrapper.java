/*
 * $Id: EquipmentTypeWrapper.java,v 1.13 2005/07/17 05:19:00 arseniy Exp $
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
 * @version $Revision: 1.13 $, $Date: 2005/07/17 05:19:00 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public final class EquipmentTypeWrapper extends StorableObjectWrapper {

	// manufacturer VARCHAR2(64),
	public static final String			COLUMN_MANUFACTURER			= "manufacturer";

	// manufacturer_code VARCHAR2(64),
	public static final String			COLUMN_MANUFACTURER_CODE	= "manufacturer_code";

	private static EquipmentTypeWrapper	instance;

	private List						keys;

	private EquipmentTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME,
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

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final Object object, final String key) {
		Object value = super.getValue(object, key);
		if (value == null && object instanceof EquipmentType) {
			EquipmentType type = (EquipmentType) object;			
			if (key.equals(COLUMN_CODENAME))
				return type.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return type.getDescription();
			if (key.equals(COLUMN_NAME))
				return type.getName();
			if (key.equals(COLUMN_MANUFACTURER))
				return type.getManufacturer();
			if (key.equals(COLUMN_MANUFACTURER_CODE))
				return type.getManufacturerCode();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof EquipmentType) {
			EquipmentType type = (EquipmentType) object;
			if (key.equals(COLUMN_NAME))
				type.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				type.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				type.setCodename((String) value);
			else if (key.equals(COLUMN_MANUFACTURER))
				type.setManufacturer((String) value);
			else if (key.equals(COLUMN_MANUFACTURER_CODE))
				type.setManufacturerCode((String) value);
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, 
	                             final Object objectKey, 
	                             final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		Class clazz = super.getPropertyClass(key); 
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
