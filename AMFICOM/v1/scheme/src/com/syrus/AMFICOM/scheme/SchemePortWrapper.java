/*-
 * $Id: SchemePortWrapper.java,v 1.8 2005/07/24 16:58:44 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;

/**
 * @version $Revision: 1.8 $, $Date: 2005/07/24 16:58:44 $
 * @author $Author: arseniy $
 * @module scheme_v1
 */
public final class SchemePortWrapper extends StorableObjectWrapper {

//	schemeport.sql
//
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	direction_type NUMBER(1) NOT NULL,
//	port_type_id VARCHAR2(32 CHAR),
//	port_id VARCHAR2(32 CHAR),
//	measurement_port_id VARCHAR2(32 CHAR),
//	parent_device_id VARCHAR2(32 CHAR) NOT NULL,

	public static final String COLUMN_DIRECTION_TYPE = "direction_type";
	public static final String COLUMN_PORT_TYPE_ID = "port_type_id";
	public static final String COLUMN_PORT_ID = "port_id";
	public static final String COLUMN_MEASUREMENT_PORT_ID = "measurement_port_id";
	public static final String COLUMN_PARENT_DEVICE_ID = "parent_device_id";

	private static SchemePortWrapper instance;

	private List<String> keys;

	private SchemePortWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_DIRECTION_TYPE,
				COLUMN_PORT_TYPE_ID,
				COLUMN_PORT_ID,
				COLUMN_MEASUREMENT_PORT_ID,
				COLUMN_PARENT_DEVICE_ID}));
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
		} else if (key.equals(COLUMN_PORT_TYPE_ID)
				|| key.equals(COLUMN_PORT_ID)
				|| key.equals(COLUMN_MEASUREMENT_PORT_ID)
				|| key.equals(COLUMN_PARENT_DEVICE_ID)) {
			return Identifier.class;
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
		if (object instanceof SchemePort) {
			final SchemePort schemePort = (SchemePort) object;
			if (key.equals(COLUMN_NAME)) {
				return schemePort.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemePort.getDescription();
			} else if (key.equals(COLUMN_DIRECTION_TYPE)) {
				return new Integer(schemePort.getDirectionType().value());
			} else if (key.equals(COLUMN_PORT_TYPE_ID)) {
				return schemePort.getPortTypeId();
			} else if (key.equals(COLUMN_PORT_ID)) {
				return schemePort.getPortId();
			} else if (key.equals(COLUMN_MEASUREMENT_PORT_ID)) {
				return schemePort.getMeasurementPortId();
			} else if (key.equals(COLUMN_PARENT_DEVICE_ID)) {
				return schemePort.getParentSchemeDeviceId();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof SchemePort) {
			final SchemePort schemePort = (SchemePort) object;
			if (key.equals(COLUMN_NAME)) {
				schemePort.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				schemePort.setDescription((String) value);
			} else if (key.equals(COLUMN_DIRECTION_TYPE)) {
				schemePort.setDirectionType(IdlDirectionType.from_int(((Integer) value).intValue()));
			} else if (key.equals(COLUMN_PORT_TYPE_ID)) {
				/**
				 * @bug changed status is not updated.
				 */
				schemePort.portTypeId = (Identifier) value;
			} else if (key.equals(COLUMN_PORT_ID)) {
				schemePort.portId = (Identifier) value;
			} else if (key.equals(COLUMN_MEASUREMENT_PORT_ID)) {
				schemePort.measurementPortId = (Identifier) value;
			} else if (key.equals(COLUMN_PARENT_DEVICE_ID)) {
				schemePort.parentSchemeDeviceId = (Identifier) value;
			}
		}
	}

	public static SchemePortWrapper getInstance() {
		if (instance == null)
			instance = new SchemePortWrapper();
		return instance;
	}
}
