/*
 * $Id: EquipmentWrapper.java,v 1.5 2005/02/01 06:15:29 bob Exp $
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

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/01 06:15:29 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class EquipmentWrapper implements Wrapper {

	// table :: Equipment
	// description VARCHAR2(256),
	public static final String		COLUMN_DESCRIPTION		= "description";
	// image_id Identifier,
	public static final String		COLUMN_IMAGE_ID			= "image_id";
	// name VARCHAR2(64) NOT NULL,
	public static final String		COLUMN_NAME				= "name";
	// type_id Identifier NOT NULL,
	public static final String		COLUMN_TYPE_ID			= "type_id";
	// supplier VARCHAR2(128)
	public static final String		COLUMN_SUPPLIER			= "supplier";
	// supplier_code VARCHAR2(128)
	public static final String		COLUMN_SUPPLIER_CODE	= "supplier_code";
	// latitude NUMBER
	public static final String		COLUMN_LATITUDE			= "latitude";
	// longitude NUMBER
	public static final String		COLUMN_LONGITUDE		= "longitude";
	// hwSerial VARCHAR2(64)
	public static final String		COLUMN_HW_SERIAL		= "hw_serial";
	// hwVersion VARCHAR2(64)
	public static final String		COLUMN_HW_VERSION		= "hw_version";
	// swSerial VARCHAR2(64)
	public static final String		COLUMN_SW_SERIAL		= "sw_serial";
	// swVersion VARCHAR2(64)
	public static final String		COLUMN_SW_VERSION		= "sw_version";
	// inventory_number VARCHAR2(64)
	public static final String		COLUMN_INVENTORY_NUMBER	= "inventory_number";

	public static final String		COLUMN_PORT_IDS			= "portIds";
	public static final String		COLUMN_CHARACTERISTICS	= "characteristics";

	private static EquipmentWrapper	instance;

	private List					keys;

	private EquipmentWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_TYPE_ID,
				COLUMN_IMAGE_ID, COLUMN_LONGITUDE, COLUMN_LATITUDE, COLUMN_SUPPLIER, COLUMN_SUPPLIER_CODE,
				COLUMN_HW_SERIAL, COLUMN_HW_VERSION, COLUMN_SW_SERIAL, COLUMN_SW_VERSION, COLUMN_INVENTORY_NUMBER,
				COLUMN_PORT_IDS, COLUMN_CHARACTERISTICS, ObjectEntities.EQUIPMENTMELINK_ENTITY};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static EquipmentWrapper getInstance() {
		if (instance == null)
			instance = new EquipmentWrapper();
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
		if (object instanceof Equipment) {
			Equipment equipment = (Equipment) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return equipment.getDescription();
			if (key.equals(COLUMN_NAME))
				return equipment.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return equipment.getType();
			if (key.equals(COLUMN_IMAGE_ID))
				return equipment.getImageId();
			if (key.equals(COLUMN_LONGITUDE))
				return new Float(equipment.getLongitude());
			if (key.equals(COLUMN_LATITUDE))
				return new Float(equipment.getLatitude());
			if (key.equals(COLUMN_SUPPLIER))
				return equipment.getSupplier();
			if (key.equals(COLUMN_SUPPLIER_CODE))
				return equipment.getSupplierCode();
			if (key.equals(COLUMN_HW_SERIAL))
				return equipment.getHwSerial();
			if (key.equals(COLUMN_HW_VERSION))
				return equipment.getHwVersion();
			if (key.equals(COLUMN_SW_SERIAL))
				return equipment.getSwSerial();
			if (key.equals(COLUMN_SW_VERSION))
				return equipment.getSwVersion();
			if (key.equals(COLUMN_INVENTORY_NUMBER))
				return equipment.getInventoryNumber();
			if (key.equals(COLUMN_PORT_IDS))
				return equipment.getCharacteristics();
			if (key.equals(COLUMN_CHARACTERISTICS))
				return equipment.getCharacteristics();
			if (key.equals(ObjectEntities.EQUIPMENTMELINK_ENTITY))
				return equipment.getMonitoredElementIds();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Equipment) {
			Equipment equipment = (Equipment) object;
			if (key.equals(COLUMN_NAME))
				equipment.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				equipment.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID)) 
				equipment.setType((EquipmentType)value);
			else if (key.equals(COLUMN_IMAGE_ID))
				equipment.setImageId((Identifier) value);
			else if (key.equals(COLUMN_LONGITUDE))
				equipment.setLongitude(((Float)value).floatValue());
			else if (key.equals(COLUMN_LATITUDE))
				equipment.setLatitude(((Float)value).floatValue());
			else if (key.equals(COLUMN_SUPPLIER))
				equipment.setSupplier((String) value);
			else if (key.equals(COLUMN_SUPPLIER_CODE))
				equipment.setSupplierCode((String) value);
			else if (key.equals(COLUMN_HW_SERIAL))
				equipment.setHwSerial((String) value);
			else if (key.equals(COLUMN_HW_VERSION))
				equipment.setHwVersion((String) value);
			else if (key.equals(COLUMN_SW_SERIAL))
				equipment.setSwSerial((String) value);
			else if (key.equals(COLUMN_SW_VERSION))
				equipment.setSwVersion((String) value);
			else if (key.equals(COLUMN_INVENTORY_NUMBER))
				equipment.setInventoryNumber((String) value);
			else if (key.equals(COLUMN_PORT_IDS)) 
				equipment.setPortIds((List)value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				equipment.setCharacteristics((List)value);
			else if (key.equals(ObjectEntities.EQUIPMENTMELINK_ENTITY))
				equipment.setMonitoredElementIds((List)value);			
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
		if (key.equals(COLUMN_PORT_IDS) || key.equals(COLUMN_CHARACTERISTICS) || key.equals(ObjectEntities.EQUIPMENTMELINK_ENTITY))
			return List.class;
		return String.class;
	}
}
