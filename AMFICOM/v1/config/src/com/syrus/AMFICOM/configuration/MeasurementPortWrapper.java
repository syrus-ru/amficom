/*
 * $Id: MeasurementPortWrapper.java,v 1.4 2005/02/01 06:15:29 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/01 06:15:29 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class MeasurementPortWrapper implements Wrapper {

	// type_id VARCHAR2(32) NOT NULL,
	public static final String				COLUMN_TYPE_ID			= "type_id";

	// name VARCHAR2(64) NOT NULL,
	public static final String				COLUMN_NAME				= "name";

	// description VARCHAR2(256),
	public static final String				COLUMN_DESCRIPTION		= "description";

	// kis_id VARCHAR2(32),
	public static final String				COLUMN_KIS_ID			= "kis_id";

	// port_id VARCHAR2(32),
	public static final String				COLUMN_PORT_ID			= "port_id";

	public static final String				COLUMN_CHARACTERISTICS	= "characteristics";

	private static MeasurementPortWrapper	instance;

	private List							keys;

	private MeasurementPortWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_TYPE_ID, COLUMN_KIS_ID,
				COLUMN_PORT_ID, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MeasurementPortWrapper getInstance() {
		if (instance == null)
			instance = new MeasurementPortWrapper();
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
		if (object instanceof MeasurementPort) {
			MeasurementPort port = (MeasurementPort) object;
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
			if (key.equals(COLUMN_CHARACTERISTICS))
				return port.getCharacteristics();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof MeasurementPort) {
			MeasurementPort port = (MeasurementPort) object;
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
			else if (key.equals(COLUMN_CHARACTERISTICS))
				port.setCharacteristics((List) value);
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
		Class clazz = String.class;
		return clazz;
	}
}
