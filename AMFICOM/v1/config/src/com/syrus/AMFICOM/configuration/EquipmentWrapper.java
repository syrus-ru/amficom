/*
 * $Id: EquipmentWrapper.java,v 1.27 2006/03/15 16:51:19 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TableNames;

/**
 * @version $Revision: 1.27 $, $Date: 2006/03/15 16:51:19 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class EquipmentWrapper extends StorableObjectWrapper<Equipment> {

	// table :: Equipment
	// description VARCHAR2(256),
	// image_id Identifier,
	public static final String COLUMN_IMAGE_ID = "image_id";
	// name VARCHAR2(64) NOT NULL,
	// proto_equipment_id Identifier NOT NULL,
	public static final String COLUMN_PROTO_EQUIPMENT_ID = "proto_equipment_id";
	// supplier VARCHAR2(128)
	public static final String COLUMN_SUPPLIER = "supplier";
	// supplier_code VARCHAR2(128)
	public static final String COLUMN_SUPPLIER_CODE = "supplier_code";
	// latitude NUMBER
	public static final String COLUMN_LATITUDE = "latitude";
	// longitude NUMBER
	public static final String COLUMN_LONGITUDE = "longitude";
	// hwSerial VARCHAR2(64)
	public static final String COLUMN_HW_SERIAL = "hw_serial";
	// hwVersion VARCHAR2(64)
	public static final String COLUMN_HW_VERSION = "hw_version";
	// swSerial VARCHAR2(64)
	public static final String COLUMN_SW_SERIAL = "sw_serial";
	// swVersion VARCHAR2(64)
	public static final String COLUMN_SW_VERSION = "sw_version";
	// inventory_number VARCHAR2(64)
	public static final String COLUMN_INVENTORY_NUMBER = "inventory_number";

	public static final String COLUMN_PORT_IDS = "portIds";

	private static EquipmentWrapper instance;

	private List<String> keys;

	private EquipmentWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_DOMAIN_ID,
				COLUMN_PROTO_EQUIPMENT_ID,
				COLUMN_IMAGE_ID,
				COLUMN_LONGITUDE,
				COLUMN_LATITUDE,
				COLUMN_SUPPLIER,
				COLUMN_SUPPLIER_CODE,
				COLUMN_HW_SERIAL,
				COLUMN_HW_VERSION,
				COLUMN_SW_SERIAL,
				COLUMN_SW_VERSION,
				COLUMN_INVENTORY_NUMBER,
				TableNames.EQUIPMENT_ME_LINK };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EquipmentWrapper getInstance() {
		if (instance == null) {
			instance = new EquipmentWrapper();
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
	public Object getValue(final Equipment equipment, final String key) {
		final Object value = super.getValue(equipment, key);
		if (value == null && equipment != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				return equipment.getDescription();
			}
			if (key.equals(COLUMN_NAME)) {
				return equipment.getName();
			} else if (key.equals(COLUMN_DOMAIN_ID)) {
				return equipment.getDomainId();
			}
			if (key.equals(COLUMN_PROTO_EQUIPMENT_ID)) {
				return equipment.getProtoEquipmentId();
			}
			if (key.equals(COLUMN_IMAGE_ID)) {
				return equipment.getImageId();
			}
			if (key.equals(COLUMN_LONGITUDE)) {
				return new Float(equipment.getLongitude());
			}
			if (key.equals(COLUMN_LATITUDE)) {
				return new Float(equipment.getLatitude());
			}
			if (key.equals(COLUMN_SUPPLIER)) {
				return equipment.getSupplier();
			}
			if (key.equals(COLUMN_SUPPLIER_CODE)) {
				return equipment.getSupplierCode();
			}
			if (key.equals(COLUMN_HW_SERIAL)) {
				return equipment.getHwSerial();
			}
			if (key.equals(COLUMN_HW_VERSION)) {
				return equipment.getHwVersion();
			}
			if (key.equals(COLUMN_SW_SERIAL)) {
				return equipment.getSwSerial();
			}
			if (key.equals(COLUMN_SW_VERSION)) {
				return equipment.getSwVersion();
			}
			if (key.equals(COLUMN_INVENTORY_NUMBER)) {
				return equipment.getInventoryNumber();
			}
			if (key.equals(TableNames.EQUIPMENT_ME_LINK)) {
				return equipment.getMonitoredElementIds();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final Equipment equipment, final String key, final Object value) {
		if (equipment != null) {
			if (key.equals(COLUMN_NAME)) {
				equipment.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				equipment.setDescription((String) value);
			} else if (key.equals(COLUMN_DOMAIN_ID)) {
				equipment.setDomainId((Identifier) value);
			} else if (key.equals(COLUMN_PROTO_EQUIPMENT_ID)) {
				equipment.setProtoEquipmentId((Identifier) value);
			} else if (key.equals(COLUMN_IMAGE_ID)) {
				equipment.setImageId((Identifier) value);
			} else if (key.equals(COLUMN_LONGITUDE)) {
				equipment.setLongitude(((Float) value).floatValue());
			} else if (key.equals(COLUMN_LATITUDE)) {
				equipment.setLatitude(((Float) value).floatValue());
			} else if (key.equals(COLUMN_SUPPLIER)) {
				equipment.setSupplier((String) value);
			} else if (key.equals(COLUMN_SUPPLIER_CODE)) {
				equipment.setSupplierCode((String) value);
			} else if (key.equals(COLUMN_HW_SERIAL)) {
				equipment.setHwSerial((String) value);
			} else if (key.equals(COLUMN_HW_VERSION)) {
				equipment.setHwVersion((String) value);
			} else if (key.equals(COLUMN_SW_SERIAL)) {
				equipment.setSwSerial((String) value);
			} else if (key.equals(COLUMN_SW_VERSION)) {
				equipment.setSwVersion((String) value);
			} else if (key.equals(COLUMN_INVENTORY_NUMBER)) {
				equipment.setInventoryNumber((String) value);
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
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_SUPPLIER)
				|| key.equals(COLUMN_SUPPLIER_CODE)
				|| key.equals(COLUMN_HW_SERIAL) 
				|| key.equals(COLUMN_HW_VERSION)
				|| key.equals(COLUMN_SW_SERIAL)
				|| key.equals(COLUMN_SW_VERSION)
				|| key.equals(COLUMN_INVENTORY_NUMBER)) {
			return String.class;
		} else if (key.equals(COLUMN_IMAGE_ID)
				|| key.equals(COLUMN_DOMAIN_ID)
				|| key.equals(COLUMN_PROTO_EQUIPMENT_ID)) {
			return Identifier.class;
		} else if (key.equals(COLUMN_LONGITUDE) 
				|| key.equals(COLUMN_LATITUDE)) {
			return Float.class;
		}
		return null;
	}
}
