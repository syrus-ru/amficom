
package com.syrus.AMFICOM.configuration;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class CableLinkTypeController implements ObjectResourceController {

	public static final String				COLUMN_SORT					= "sort";
	public static final String				COLUMN_MANUFACTURER			= "manufacturer";
	public static final String				COLUMN_MANUFACTURER_CODE	= "manufacturer_code";
	public static final String				COLUMN_IMAGE_ID				= "image_id";
	public static final String				COLUMN_CABLE_THREAD_TYPES	= "cable_thread_type_id";

	private static CableLinkTypeController	instance;

	private List							keys;

	private CableLinkTypeController() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME };

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static CableLinkTypeController getInstance() {
		if (instance == null)
			instance = new CableLinkTypeController();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		String name = null;
		if (key.equals(COLUMN_NAME))
			name = "��������";
		if (key.equals(COLUMN_DESCRIPTION))
			name = "��������";
		return name;
	}

	public Object getValue(final Object object, final String key) {
		Object result = null;
		if (object instanceof CableLinkType) {
			CableLinkType type = (CableLinkType) object;
			if (key.equals(COLUMN_ID))
				result = type.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = type.getCreated().toString();
			else if (key.equals(COLUMN_CREATOR_ID))
				result = type.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = type.getModified().toString();
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = type.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_CODENAME))
				result = type.getCodename();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = type.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = type.getName();
			else if (key.equals(COLUMN_SORT))
				result = Integer.toString(type.getSort().value());
			else if (key.equals(COLUMN_MANUFACTURER))
				result = type.getManufacturer();
			else if (key.equals(COLUMN_MANUFACTURER_CODE))
				result = type.getManufacturerCode();
			else if (key.equals(COLUMN_IMAGE_ID))
				result = type.getImageId().getIdentifierString();
			else if (key.equals(COLUMN_CABLE_THREAD_TYPES)) {
				List res = new ArrayList(type.getCableThreadTypes().size());
				for (Iterator it = type.getCableThreadTypes().iterator(); it.hasNext();) {
					CableThreadType ctt = (CableThreadType) it.next();
					res.add(ctt.getId().getIdentifierString());
				}
				result = res;
			} else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(type.getCharacteristics().size());
				for (Iterator it = type.getCharacteristics().iterator(); it.hasNext();) {
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

	public static String getPropertyPaneClassName() {
		return "com.syrus.AMFICOM.Client.Configure.UI.CableLinkTypePane";
	}
}
