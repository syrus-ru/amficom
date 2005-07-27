/*
 * $Id: MeasurementPortWrapper.java,v 1.12 2005/07/27 15:59:21 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.12 $, $Date: 2005/07/27 15:59:21 $
 * @author $Author: bass $
 * @module config
 */
public final class MeasurementPortWrapper extends StorableObjectWrapper {

	// type_id VARCHAR2(32) NOT NULL,
	// name VARCHAR2(64) NOT NULL,
	// description VARCHAR2(256),
	// kis_id VARCHAR2(32),
	public static final String COLUMN_KIS_ID = "kis_id";

	// port_id VARCHAR2(32),
	public static final String COLUMN_PORT_ID = "port_id";

	private static MeasurementPortWrapper instance;

	private List<String> keys;

	private MeasurementPortWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_TYPE_ID, COLUMN_KIS_ID, COLUMN_PORT_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static MeasurementPortWrapper getInstance() {
		if (instance == null)
			instance = new MeasurementPortWrapper();
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
	public Object getValue(final Object object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object instanceof MeasurementPort) {
			final MeasurementPort port = (MeasurementPort) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return port.getDescription();
			if (key.equals(COLUMN_NAME))
				return port.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return port.getType();
			if (key.equals(COLUMN_KIS_ID))
				return port.getKISId();
			if (key.equals(COLUMN_PORT_ID))
				return port.getPortId();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof MeasurementPort) {
			final MeasurementPort port = (MeasurementPort) object;
			if (key.equals(COLUMN_NAME))
				port.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				port.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID))
				port.setType((MeasurementPortType) value);
			else if (key.equals(COLUMN_KIS_ID))
				port.setKISId((Identifier) value);
			else if (key.equals(COLUMN_PORT_ID))
				port.setPortId((Identifier) value);
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
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_TYPE_ID)) {
			return MeasurementPortType.class;
		} else if (key.equals(COLUMN_KIS_ID)
				|| key.equals(COLUMN_PORT_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
