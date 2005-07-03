/*
 * $Id: CableThreadTypeWrapper.java,v 1.10 2005/06/22 07:43:48 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/22 07:43:48 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class CableThreadTypeWrapper extends StorableObjectWrapper {

	// codename VARCHAR2(32) NOT NULL,
	// description VARCHAR2(256),
	// name VARCHAR2(64),
	// color NUMBER(38),
	public static final String				COLUMN_COLOR		= "color";

	// cable_link_type_id VARCHAR2(32),
	public static final String				COLUMN_LINK_TYPE_ID	= "link_type_id";

	// cable_link_type_id VARCHAR2(32),
	public static final String				COLUMN_CABLE_LINK_TYPE_ID	= "cable_link_type_id";

	private static CableThreadTypeWrapper	instance;

	private List							keys;

	private CableThreadTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_COLOR, COLUMN_LINK_TYPE_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static CableThreadTypeWrapper getInstance() {
		if (instance == null)
			instance = new CableThreadTypeWrapper();
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
		if (value == null && object instanceof CableThreadType) {
			CableThreadType type = (CableThreadType) object;
			if (key.equals(COLUMN_CODENAME))
				return type.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return type.getDescription();
			if (key.equals(COLUMN_NAME))
				return type.getName();
			if (key.equals(COLUMN_COLOR))
				return new Integer(type.getColor());
			if (key.equals(COLUMN_LINK_TYPE_ID))
				return type.getLinkType();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof CableThreadType) {
			CableThreadType type = (CableThreadType) object;
			if (key.equals(COLUMN_NAME))
				type.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				type.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				type.setCodename((String) value);
			else if (key.equals(COLUMN_COLOR))
				type.setColor(((Integer)value).intValue());
			else if (key.equals(COLUMN_LINK_TYPE_ID))
				type.setLinkType((LinkType)value);
		}
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
		if (key.equals(COLUMN_CODENAME)
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_NAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_COLOR)) {
			return Integer.class;
		}
		if (key.equals(COLUMN_LINK_TYPE_ID)) {
			return LinkType.class;
		}
		return null;
	}
}
