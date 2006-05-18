/*
 * $Id: MeasurementPortWrapper.java,v 1.4 2006/03/13 15:54:25 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2006/03/13 15:54:25 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementPortWrapper extends StorableObjectWrapper<MeasurementPort> {

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
	public Object getValue(final MeasurementPort measurementPort,
	                       final String key) {
		final Object value = super.getValue(measurementPort, key);
		if (value == null && measurementPort != null) {
			if (key.equals(COLUMN_DESCRIPTION))
				return measurementPort.getDescription();
			if (key.equals(COLUMN_NAME))
				return measurementPort.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return measurementPort.getType();
			if (key.equals(COLUMN_KIS_ID))
				return measurementPort.getKISId();
			if (key.equals(COLUMN_PORT_ID))
				return measurementPort.getPortId();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final MeasurementPort measurementPort, final String key, final Object value) {
		if (measurementPort != null) {
			if (key.equals(COLUMN_NAME))
				measurementPort.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				measurementPort.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID))
				measurementPort.setType((MeasurementPortType) value);
			else if (key.equals(COLUMN_KIS_ID))
				measurementPort.setKISId((Identifier) value);
			else if (key.equals(COLUMN_PORT_ID))
				measurementPort.setPortId((Identifier) value);
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
