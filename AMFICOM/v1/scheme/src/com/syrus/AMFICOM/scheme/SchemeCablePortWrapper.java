/*-
 * $Id: SchemeCablePortWrapper.java,v 1.13.2.1 2006/03/15 15:47:49 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.13.2.1 $, $Date: 2006/03/15 15:47:49 $
 * @author $Author: arseniy $
 * @module scheme
 */
public final class SchemeCablePortWrapper extends StorableObjectWrapper<SchemeCablePort> {
	
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

	private final List<String> keys;

	private SchemeCablePortWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] { COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_DIRECTION_TYPE,
				COLUMN_CABLE_PORT_TYPE_ID,
				COLUMN_CABLE_PORT_ID,
				COLUMN_MEASUREMENT_PORT_ID,
				COLUMN_PARENT_DEVICE_ID }));
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		return key;
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
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
	public Object getValue(final SchemeCablePort schemeCablePort, final String key) {
		final Object value = super.getValue(schemeCablePort, key);
		if (value != null) {
			return value;
		}
		if (schemeCablePort != null) {
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
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final SchemeCablePort schemeCablePort,
			final String key, final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (schemeCablePort != null) {
				if (key.equals(COLUMN_NAME)) {
					schemeCablePort.setName((String) value);
				} else if (key.equals(COLUMN_DESCRIPTION)) {
					schemeCablePort.setDescription((String) value);
				} else if (key.equals(COLUMN_DIRECTION_TYPE)) {
					schemeCablePort.setDirectionType(IdlDirectionType.from_int(((Integer) value).intValue()));
				} else if (key.equals(COLUMN_CABLE_PORT_TYPE_ID)) {
					schemeCablePort.setPortTypeId((Identifier) value);
				} else if (key.equals(COLUMN_CABLE_PORT_ID)) {
					schemeCablePort.setPortId((Identifier) value);
				} else if (key.equals(COLUMN_MEASUREMENT_PORT_ID)) {
					schemeCablePort.setMeasurementPortId((Identifier) value);
				} else if (key.equals(COLUMN_PARENT_DEVICE_ID)) {
					schemeCablePort.setParentSchemeDeviceId((Identifier) value, usePool);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static SchemeCablePortWrapper getInstance() {
		if (instance == null)
			instance = new SchemeCablePortWrapper();
		return instance;
	}
}
