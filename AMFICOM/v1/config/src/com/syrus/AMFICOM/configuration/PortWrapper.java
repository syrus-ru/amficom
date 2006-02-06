/*
 * $Id: PortWrapper.java,v 1.20 2005/09/14 18:42:07 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
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
 * @version $Revision: 1.20 $, $Date: 2005/09/14 18:42:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class PortWrapper extends StorableObjectWrapper<Port> {

	// type_id VARCHAR2(32) NOT NULL,
	// description VARCHAR2(256),
	// equipment_id VARCHAR2(32),
	public static final String COLUMN_EQUIPMENT_ID = "equipment_id";

	private static PortWrapper instance;

	private List<String> keys;

	private PortWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION, 
				COLUMN_TYPE_ID, 
				COLUMN_EQUIPMENT_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static PortWrapper getInstance() {
		if (instance == null)
			instance = new PortWrapper();
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
	public Object getValue(final Port port, final String key) {
		final Object value = super.getValue(port, key);
		if (value == null && port != null) {
			if (key.equals(COLUMN_DESCRIPTION))
				return port.getDescription();
			if (key.equals(COLUMN_TYPE_ID))
				return port.getType();
			if (key.equals(COLUMN_EQUIPMENT_ID))
				return port.getEquipmentId();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final Port port, final String key, final Object value) {
		if (port != null) {
			if (key.equals(COLUMN_DESCRIPTION))
				port.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID))
				port.setType((PortType) value);
			else if (key.equals(COLUMN_EQUIPMENT_ID))
				port.setEquipmentId((Identifier) value);
		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
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
		if (key.equals(COLUMN_DESCRIPTION))
			return String.class;
		else if (key.equals(COLUMN_TYPE_ID))
			return PortType.class;
		else if (key.equals(COLUMN_EQUIPMENT_ID))
			return Identifier.class;
		return null;
	}
}
