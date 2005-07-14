/*-
 * $Id: SchemeCablePortWrapper.java,v 1.6 2005/07/14 14:24:06 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;

/**
 * @version $Revision: 1.6 $, $Date: 2005/07/14 14:24:06 $
 * @author $Author: bass $
 * @module scheme_v1
 */
public final class SchemeCablePortWrapper extends StorableObjectWrapper {
	
//	schemecableport.sql
//	
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	direction_type NUMBER(1) NOT NULL,
//	cable_port_type_id VARCHAR2(32 CHAR),
//	cable_port_id VARCHAR2(32 CHAR),
//	measurement_port_id VARCHAR2(32 CHAR),
//	parent_device_id VARCHAR2(32 CHAR) NOT NULL,
	
	public static final String COLUMN_DIRECTION_TYPE = "direction_type";
	public static final String COLUMN_CABLE_PORT_TYPE_ID = "cable_port_type_id";
	public static final String COLUMN_CABLE_PORT_ID = "cable_port_id";
	public static final String COLUMN_MEASUREMENT_PORT_ID = "measurement_port_id";
	public static final String COLUMN_PARENT_DEVICE_ID = "parent_device_id";

	private static SchemeCablePortWrapper instance;

	private List<String> keys;

	private SchemeCablePortWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_DIRECTION_TYPE,
				COLUMN_CABLE_PORT_TYPE_ID,
				COLUMN_CABLE_PORT_ID,
				COLUMN_MEASUREMENT_PORT_ID,
				COLUMN_PARENT_DEVICE_ID,
				COLUMN_CHARACTERISTICS}));
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		return key;
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_DIRECTION_TYPE)) {
			return Integer.class;
		} else if (key.equals(COLUMN_CABLE_PORT_TYPE_ID)
				|| key.equals(COLUMN_CABLE_PORT_ID)
				|| key.equals(COLUMN_MEASUREMENT_PORT_ID)
				|| key.equals(COLUMN_PARENT_DEVICE_ID)) {
			return Identifier.class;
		} else if (key.equals(COLUMN_CHARACTERISTICS)) {
			return Set.class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// empty
	}

	@Override
	public Object getValue(final Object object, final String key) {
		final Object value = super.getValue(object, key);
		if (value != null) {
			return value;
		}
		if (object instanceof SchemeCablePort) {
			final SchemeCablePort schemeCablePort = (SchemeCablePort) object;
			if (key.equals(COLUMN_NAME)) {
				return schemeCablePort.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeCablePort.getDescription();
			} else if (key.equals(COLUMN_DIRECTION_TYPE)) {
				return new Integer(schemeCablePort.getDirectionType().value());
			} else if (key.equals(COLUMN_CABLE_PORT_TYPE_ID)) {
				return schemeCablePort.getPortTypeId();
			} else if (key.equals(COLUMN_CABLE_PORT_ID)) {
				return schemeCablePort.getPortId();
			} else if (key.equals(COLUMN_MEASUREMENT_PORT_ID)) {
				return schemeCablePort.getMeasurementPortId();
			} else if (key.equals(COLUMN_PARENT_DEVICE_ID)) {
				return schemeCablePort.getParentSchemeDeviceId();
			} else if (key.equals(COLUMN_CHARACTERISTICS)) {
				return schemeCablePort.getCharacteristics();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof SchemeCablePort) {
			final SchemeCablePort schemeCablePort = (SchemeCablePort) object;
			if (key.equals(COLUMN_NAME)) {
				schemeCablePort.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				schemeCablePort.getDescription();
			} else if (key.equals(COLUMN_DIRECTION_TYPE)) {
				schemeCablePort.setDirectionType(DirectionType.from_int(((Integer) value).intValue()));
			} else if (key.equals(COLUMN_CABLE_PORT_TYPE_ID)) {
				/**
				 * @bug changed status is not updated.
				 */
				schemeCablePort.portTypeId = (Identifier) value;
			} else if (key.equals(COLUMN_CABLE_PORT_ID)) {
				schemeCablePort.portId = (Identifier) value;
			} else if (key.equals(COLUMN_MEASUREMENT_PORT_ID)) {
				schemeCablePort.measurementPortId = (Identifier) value;
			} else if (key.equals(COLUMN_PARENT_DEVICE_ID)) {
				schemeCablePort.parentSchemeDeviceId = (Identifier) value;
			} else if (key.equals(COLUMN_CHARACTERISTICS)) {
				schemeCablePort.setCharacteristics((Set) value);
			}
		}
	}

	public static SchemeCablePortWrapper getInstance() {
		if (instance == null)
			instance = new SchemeCablePortWrapper();
		return instance;
	}
}
