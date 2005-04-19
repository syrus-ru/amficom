
package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;

public final class SchemeCablePortController extends ObjectResourceController {

	public static final String					COLUMN_SCHEME_CABLE_LINK_ID		= "scheme_cable_link_id";
	public static final String					COLUMN_DIRECTION				= "direction";
	public static final String					COLUMN_MEASUREMENT_PORT_ID		= "measurement_port_id";
	public static final String					COLUMN_MEASUREMENT_PORT_TYPE_ID	= "measurement_port_type_id";
	public static final String					COLUMN_PORT_ID					= "port_id";
	public static final String					COLUMN_PORT_TYPE_ID				= "port_type_id";
	public static final String					COLUMN_SCHEME_DEVICE_ID			= "scheme_device_id";

	private static SchemeCablePortController	instance;

	private List								keys;

	private SchemeCablePortController() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_ID, COLUMN_CREATED, COLUMN_CREATOR_ID, COLUMN_MODIFIED,
				COLUMN_MODIFIER_ID, COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_SCHEME_CABLE_LINK_ID,
				COLUMN_DIRECTION, COLUMN_MEASUREMENT_PORT_ID, COLUMN_MEASUREMENT_PORT_TYPE_ID, COLUMN_PORT_ID,
				COLUMN_PORT_TYPE_ID, COLUMN_SCHEME_DEVICE_ID, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SchemeCablePortController getInstance() {
		if (instance == null)
			instance = new SchemeCablePortController();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		String name = null;
		if (key.equals(COLUMN_NAME))
			name = "Название";
		return name;
	}

	public Object getValue(final Object object, final String key) {
		Object result = null;
		if (object instanceof SchemeCablePort) {
			SchemeCablePort port = (SchemeCablePort) object;
			if (key.equals(COLUMN_ID))
				result = port.getId().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(port.getCreated().getTime());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = port.getCreatorId().getIdentifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(port.getModified().getTime());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = port.getModifierId().getIdentifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = port.getDescription();
			else if (key.equals(COLUMN_NAME))
				result = port.getName();
			else if (key.equals(COLUMN_DIRECTION))
				result = Integer.toString(port.getDirectionType().value());
			else if (key.equals(COLUMN_MEASUREMENT_PORT_ID))
				result = port.getMeasurementPort().getId().getIdentifierString();
			else if (key.equals(COLUMN_MEASUREMENT_PORT_TYPE_ID))
				result = port.getMeasurementPortType().getId().getIdentifierString();
			else if (key.equals(COLUMN_PORT_ID))
				result = port.getPort().getId().getIdentifierString();
			else if (key.equals(COLUMN_PORT_TYPE_ID))
				result = port.getPortType().getId().getIdentifierString();
			else if (key.equals(COLUMN_SCHEME_CABLE_LINK_ID))
				result = port.getSchemeCableLink().getId().getIdentifierString();
			else if (key.equals(COLUMN_SCHEME_DEVICE_ID))
				result = port.getParentSchemeDevice().getId().getIdentifierString();
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(port.getCharacteristics().size());
				for (Iterator it = port.getCharacteristics().iterator(); it.hasNext();) {
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
