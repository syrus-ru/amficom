
package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class EquipmentController implements ObjectResourceController {

	public static final String			COLUMN_IMAGE_ID			= "imageId";
	public static final String			COLUMN_LONGITUDE		= "longitude";
	public static final String			COLUMN_LATITUDE			= "latitude";
	public static final String			COLUMN_SUPPLIER			= "supplier";
	public static final String			COLUMN_SUPPLIER_CODE	= "supplierCode";
	public static final String			COLUMN_HW_SERIAL		= "hwSerial";
	public static final String			COLUMN_HW_VERSION		= "hwVersion";
	public static final String			COLUMN_SW_SERIAL		= "swSerial";
	public static final String			COLUMN_SW_VERSION		= "swVersion";
	public static final String			COLUMN_INVENTORY_NUMBER	= "inventoryNumber";
	public static final String			COLUMN_PORT_IDS			= "portIds";

	private static EquipmentController	instance;

	private List						keys;

	private EquipmentController() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_ID, COLUMN_CREATED,
				COLUMN_CREATOR_ID, COLUMN_MODIFIED,
				COLUMN_MODIFIER_ID, COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_TYPE_ID, COLUMN_IMAGE_ID, COLUMN_LONGITUDE, COLUMN_LATITUDE, COLUMN_SUPPLIER,
				COLUMN_SUPPLIER_CODE, COLUMN_HW_SERIAL, COLUMN_HW_VERSION, COLUMN_SW_SERIAL, COLUMN_SW_VERSION,
				COLUMN_INVENTORY_NUMBER, COLUMN_PORT_IDS, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static EquipmentController getInstance() {
		if (instance == null)
			instance = new EquipmentController();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		String name = null;
		if (key.equals(COLUMN_NAME))
			name = "Название";
		if (key.equals(COLUMN_DESCRIPTION))
			name = "Описание";
		return name;
	}

	public Object getValue(final Object object, final String key) {
		Object result = null;
		if (object instanceof Equipment) {
			Equipment eq = (Equipment) object;
			if (key.equals(COLUMN_ID))
				result = eq.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = eq.getCreated().toString();
			else if (key.equals(COLUMN_CREATOR_ID))
				result = eq.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = eq.getModified().toString();
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = eq.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = eq.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = eq.getName();
			else if (key.equals(COLUMN_TYPE_ID))
				result = eq.getType().getId().getIdentifierString();
			else if (key.equals(COLUMN_IMAGE_ID))
				result = eq.getImageId().getIdentifierString();
			else if (key.equals(COLUMN_LONGITUDE))
				result = Float.toString(eq.getLongitude());
			else if (key.equals(COLUMN_LATITUDE))
				result = Float.toString(eq.getLatitude());
			else if (key.equals(COLUMN_SUPPLIER))
				result = eq.getSupplier();
			else if (key.equals(COLUMN_SUPPLIER_CODE))
				result = eq.getSupplierCode();
			else if (key.equals(COLUMN_HW_SERIAL))
				result = eq.getHwSerial();
			else if (key.equals(COLUMN_HW_VERSION))
				result = eq.getHwVersion();
			else if (key.equals(COLUMN_SW_SERIAL))
				result = eq.getSwSerial();
			else if (key.equals(COLUMN_SW_VERSION))
				result = eq.getSwVersion();
			else if (key.equals(COLUMN_INVENTORY_NUMBER))
				result = eq.getInventoryNumber();
			else if (key.equals(COLUMN_PORT_IDS)) {
				List res = new ArrayList(eq.getPortIds().size());
				for (Iterator it = eq.getCharacteristics().iterator(); it.hasNext();) {
					res.add(((Identifier) it.next()).getIdentifierString());
				}
				result = res;
			} else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(eq.getCharacteristics().size());
				for (Iterator it = eq.getCharacteristics().iterator(); it.hasNext();) {
					Characteristic ch = (Characteristic) it.next();
					res.add(ch.getId().getIdentifierString());
				}
				result = res;
			}
		}
		return result;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		Object result = "";
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
	}

	public Class getPropertyClass(String key) {
		Class clazz = String.class;
		return clazz;
	}
}
