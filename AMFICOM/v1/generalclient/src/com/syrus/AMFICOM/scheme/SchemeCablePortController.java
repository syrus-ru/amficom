
package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort;

public final class SchemeCablePortController implements ObjectResourceController {

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
				result = port.id().toString();
			else if (key.equals(COLUMN_CREATED))
				result = Long.toString(port.created());
			else if (key.equals(COLUMN_CREATOR_ID))
				result = port.creatorId().identifierString();
			else if (key.equals(COLUMN_MODIFIED))
				result = Long.toString(port.modified());
			else if (key.equals(COLUMN_MODIFIER_ID))
				result = port.modifierId().identifierString();
			else if (key.equals(COLUMN_DESCRIPTION))
				result = port.description();
			else if (key.equals(COLUMN_NAME))
				result = port.name();
			else if (key.equals(COLUMN_DIRECTION))
				result = Integer.toString(port.directionType().value());
			else if (key.equals(COLUMN_MEASUREMENT_PORT_ID))
				result = port.measurementPortImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_MEASUREMENT_PORT_TYPE_ID))
				result = port.measurementPortTypeImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_PORT_ID))
				result = port.portImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_PORT_TYPE_ID))
				result = port.portTypeImpl().getId().getIdentifierString();
			else if (key.equals(COLUMN_SCHEME_CABLE_LINK_ID))
				result = port.schemeCableLink().id().identifierString();
			else if (key.equals(COLUMN_SCHEME_DEVICE_ID))
				result = port.schemeDevice().id().identifierString();
			else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List res = new ArrayList(port.characteristics().length);
				for (Iterator it = port.characteristicsImpl().getValue().iterator(); it.hasNext();) {
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
