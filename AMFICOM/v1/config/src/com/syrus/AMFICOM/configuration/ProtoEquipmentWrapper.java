/*-
 * $Id: ProtoEquipmentWrapper.java,v 1.2 2005/09/29 08:18:07 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/29 08:18:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class ProtoEquipmentWrapper extends StorableObjectWrapper<ProtoEquipment> {

	// manufacturer VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER = "manufacturer";

	// manufacturer_code VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER_CODE = "manufacturer_code";

	private static ProtoEquipmentWrapper instance;

	private List<String> keys;

	private ProtoEquipmentWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_TYPE_CODE,
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_MANUFACTURER,
				COLUMN_MANUFACTURER_CODE };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ProtoEquipmentWrapper getInstance() {
		if (instance == null) {
			instance = new ProtoEquipmentWrapper();
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
	public Object getValue(final ProtoEquipment equipmentType, final String key) {
		final Object value = super.getValue(equipmentType, key);
		if (value == null && equipmentType != null) {
			if (key.equals(COLUMN_TYPE_CODE)) {
				return equipmentType.getType();
			}
			if (key.equals(COLUMN_NAME)) {
				return equipmentType.getName();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return equipmentType.getDescription();
			}
			if (key.equals(COLUMN_MANUFACTURER)) {
				return equipmentType.getManufacturer();
			}
			if (key.equals(COLUMN_MANUFACTURER_CODE)) {
				return equipmentType.getManufacturerCode();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final ProtoEquipment protoEquipment, final String key, final Object value) {
		if (protoEquipment != null) {
			if (key.equals(COLUMN_TYPE_CODE)) {
				protoEquipment.setType((EquipmentType) value);
			}
			if (key.equals(COLUMN_NAME)) {
				protoEquipment.setName((String) value);
			}
			else if (key.equals(COLUMN_DESCRIPTION)) {
				protoEquipment.setDescription((String) value);
			}
			else if (key.equals(COLUMN_MANUFACTURER)) {
				protoEquipment.setManufacturer((String) value);
			}
			else if (key.equals(COLUMN_MANUFACTURER_CODE)) {
				protoEquipment.setManufacturerCode((String) value);
			}
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
		if (key.equals(COLUMN_TYPE_CODE)) {
			return EquipmentType.class;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_MANUFACTURER)
				|| key.equals(COLUMN_MANUFACTURER_CODE)) {
			return String.class;
		}
		return null;
	}
}
