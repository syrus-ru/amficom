/*
 * $Id: EquipmentWrapper.java,v 1.1 2005/01/26 13:18:49 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/26 13:18:49 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class EquipmentWrapper implements Wrapper {

	public static final String		COLUMN_NAME				= "name";
	public static final String		COLUMN_TYPE_ID			= "type_id";
	public static final String		COLUMN_IMAGE_ID			= "imageId";
	public static final String		COLUMN_LONGITUDE		= "longitude";
	public static final String		COLUMN_LATITUDE			= "latitude";
	public static final String		COLUMN_SUPPLIER			= "supplier";
	public static final String		COLUMN_SUPPLIER_CODE	= "supplierCode";
	public static final String		COLUMN_HW_SERIAL		= "hwSerial";
	public static final String		COLUMN_HW_VERSION		= "hwVersion";
	public static final String		COLUMN_SW_SERIAL		= "swSerial";
	public static final String		COLUMN_SW_VERSION		= "swVersion";
	public static final String		COLUMN_INVENTORY_NUMBER	= "inventoryNumber";
	public static final String		COLUMN_PORT_IDS			= "portIds";
	public static final String		COLUMN_CHARACTERISTICS	= "characteristics";

	private static EquipmentWrapper	instance;

	private List					keys;

	private EquipmentWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, StorableObjectType.COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_TYPE_ID, COLUMN_IMAGE_ID, COLUMN_LONGITUDE, COLUMN_LATITUDE, COLUMN_SUPPLIER,
				COLUMN_SUPPLIER_CODE, COLUMN_HW_SERIAL, COLUMN_HW_VERSION, COLUMN_SW_SERIAL, COLUMN_SW_VERSION,
				COLUMN_INVENTORY_NUMBER, COLUMN_PORT_IDS, COLUMN_CHARACTERISTICS};

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
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return equipment.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return equipment.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return equipment.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return equipment.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return equipment.getModifierId().getIdentifierString();
			if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				return equipment.getDescription();
			if (key.equals(COLUMN_NAME))
				return equipment.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return equipment.getType().getId().getIdentifierString();
			if (key.equals(COLUMN_IMAGE_ID))
				return equipment.getImageId().getIdentifierString();
			if (key.equals(COLUMN_LONGITUDE))
				return Float.toString(equipment.getLongitude());
			if (key.equals(COLUMN_LATITUDE))
				return Float.toString(equipment.getLatitude());
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
			else if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				equipment.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID)) {
				try {
					equipment.setType((EquipmentType) ConfigurationStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("EquipmentWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(COLUMN_IMAGE_ID))
				equipment.setImageId(new Identifier((String) value));
			else if (key.equals(COLUMN_LONGITUDE))
				equipment.setLongitude(Float.parseFloat((String) value));
			else if (key.equals(COLUMN_LATITUDE))
				equipment.setLatitude(Float.parseFloat((String) value));
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
			else if (key.equals(COLUMN_PORT_IDS)) {
				List portIds = new ArrayList(((List) value).size());
				for (Iterator it = ((List) value).iterator(); it.hasNext();) 
					portIds.add(new Identifier((String) it.next()));
				equipment.setPortIds0(portIds);
			} else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List charIdStr = (List) value;
				List characteristicIds = new ArrayList(charIdStr.size());
				for (Iterator it = charIdStr.iterator(); it.hasNext();)
					characteristicIds.add(new Identifier((String) it.next()));
				try {
					equipment.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("EquipmentWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			}
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
		if (key.equals(COLUMN_PORT_IDS) || key.equals(COLUMN_CHARACTERISTICS))
			return List.class;
		return String.class;
	}
}
