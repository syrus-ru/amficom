/*
 * $Id: EquipmentTypeWrapper.java,v 1.5 2005/02/03 08:37:00 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/03 08:37:00 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public final class EquipmentTypeWrapper implements StorableObjectWrapper {

	// manufacturer VARCHAR2(64),
	public static final String			COLUMN_MANUFACTURER			= "manufacturer";

	// manufacturer_code VARCHAR2(64),
	public static final String			COLUMN_MANUFACTURER_CODE	= "manufacturer_code";

	private static EquipmentTypeWrapper	instance;

	private List						keys;

	private EquipmentTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_MANUFACTURER, COLUMN_MANUFACTURER_CODE, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
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

	public Object getValue(final Object object, final String key) {
		if (object instanceof EquipmentType) {
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
			if (key.equals(COLUMN_CHARACTERISTICS))
				return type.getCharacteristics();
		}
		return null;
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
			else if (key.equals(COLUMN_CHARACTERISTICS)) 
				type.setCharacteristics((List)value);
		}
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_CHARACTERISTICS))
			return List.class;
		return String.class;
	}
}
