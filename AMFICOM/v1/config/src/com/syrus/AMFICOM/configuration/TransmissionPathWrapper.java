/*
 * $Id: TransmissionPathWrapper.java,v 1.14 2005/07/25 20:49:36 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.14 $, $Date: 2005/07/25 20:49:36 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */
public final class TransmissionPathWrapper extends StorableObjectWrapper {

	// table :: TransmissionPath
	// description VARCHAR2(256),
	// name VARCHAR2(64) NOT NULL,
	// start_port_id VARCHAR2(32),
	public static final String COLUMN_START_PORT_ID = "start_port_id";

	// finish_port_id VARCHAR2(32),
	public static final String COLUMN_FINISH_PORT_ID = "finish_port_id";

	private static TransmissionPathWrapper instance;

	private List<String> keys;

	private TransmissionPathWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_NAME, ObjectEntities.TRANSPATHMELINK };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static TransmissionPathWrapper getInstance() {
		if (instance == null)
			instance = new TransmissionPathWrapper();
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
		if (value == null && object instanceof TransmissionPath) {
			final TransmissionPath path = (TransmissionPath) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return path.getDescription();
			if (key.equals(COLUMN_NAME))
				return path.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return path.getType();
			if (key.equals(COLUMN_START_PORT_ID))
				return path.getStartPortId();
			if (key.equals(COLUMN_FINISH_PORT_ID))
				return path.getFinishPortId();
			if (key.equals(ObjectEntities.TRANSPATHMELINK))
				return path.getMonitoredElementIds();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof TransmissionPath) {
			final TransmissionPath path = (TransmissionPath) object;
			if (key.equals(COLUMN_NAME))
				path.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				path.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID))
				path.setType((TransmissionPathType) value);
			else if (key.equals(COLUMN_START_PORT_ID))
				path.setStartPortId((Identifier) value);
			else if (key.equals(COLUMN_FINISH_PORT_ID))
				path.setFinishPortId((Identifier) value);
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
		}
		if (key.equals(COLUMN_TYPE_ID))
			return TransmissionPathType.class;
		else if (key.equals(COLUMN_START_PORT_ID)
				|| key.equals(COLUMN_FINISH_PORT_ID))
			return Identifier.class;
		if (key.equals(ObjectEntities.EQUIPMENTMELINK))
			return Set.class;
		return null;
	}
}
