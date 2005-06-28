/*
 * $Id: PortWrapper.java,v 1.13 2005/06/28 11:15:24 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.configuration.corba.IdlPortPackage.PortSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.13 $, $Date: 2005/06/28 11:15:24 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */
public final class PortWrapper extends StorableObjectWrapper {

	// type_id VARCHAR2(32) NOT NULL,
	// description VARCHAR2(256),
	// equipment_id VARCHAR2(32),
	public static final String	COLUMN_EQUIPMENT_ID		= "equipment_id";

	// sort NUMBER(2) NOT NULL,
	public static final String	COLUMN_SORT				= "sort";

	private static PortWrapper	instance;

	private List				keys;

	private PortWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_TYPE_ID, COLUMN_SORT, COLUMN_EQUIPMENT_ID,
				COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static PortWrapper getInstance() {
		if (instance == null)
			instance = new PortWrapper();
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
		Object value = super.getValue(object, key);
		if (value == null && object instanceof Port) {
			Port port = (Port) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return port.getDescription();
			if (key.equals(COLUMN_TYPE_ID))
				return port.getType();
			if (key.equals(COLUMN_SORT))
				return new Integer(port.getSort().value());
			if (key.equals(COLUMN_EQUIPMENT_ID))
				return port.getEquipmentId();
			if (key.equals(COLUMN_CHARACTERISTICS))
				return port.getCharacteristics();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Port) {
			Port port = (Port) object;
			if (key.equals(COLUMN_DESCRIPTION))
				port.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID))
				port.setType((PortType) value);
			else if (key.equals(COLUMN_SORT))
				port.setSort(PortSort.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_EQUIPMENT_ID))
				port.setEquipmentId((Identifier) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				port.setCharacteristics((Set) value);
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
		Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION))
			return String.class;
		else if (key.equals(COLUMN_TYPE_ID))
			return PortType.class;
		else if (key.equals(COLUMN_SORT))
			return Integer.class;
		else if (key.equals(COLUMN_EQUIPMENT_ID))
			return Identifier.class;
		if (key.equals(COLUMN_CHARACTERISTICS))
			return Set.class;
		return null;
	}
}
