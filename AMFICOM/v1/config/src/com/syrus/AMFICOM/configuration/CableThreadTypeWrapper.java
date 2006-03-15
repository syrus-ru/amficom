/*
 * $Id: CableThreadTypeWrapper.java,v 1.19 2006/03/15 16:50:53 bass Exp $
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
 * @version $Revision: 1.19 $, $Date: 2006/03/15 16:50:53 $
 * @author $Author: bass $
 * @module config
 */
public final class CableThreadTypeWrapper extends StorableObjectWrapper<CableThreadType> {

	// codename VARCHAR2(32) NOT NULL,
	// description VARCHAR2(256),
	// name VARCHAR2(64),
	// color NUMBER(38),
	public static final String COLUMN_COLOR = "color";

	// cable_link_type_id VARCHAR2(32),
	public static final String COLUMN_LINK_TYPE_ID = "link_type_id";

	// cable_link_type_id VARCHAR2(32),
	public static final String COLUMN_CABLE_LINK_TYPE_ID = "cable_link_type_id";

	private static CableThreadTypeWrapper instance;

	private List<String> keys;

	private CableThreadTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_COLOR, COLUMN_LINK_TYPE_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static CableThreadTypeWrapper getInstance() {
		if (instance == null)
			instance = new CableThreadTypeWrapper();
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
	public Object getValue(final CableThreadType cableThreadType, final String key) {
		final Object value = super.getValue(cableThreadType, key);
		if (value == null && cableThreadType != null) {
			if (key.equals(COLUMN_CODENAME))
				return cableThreadType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return cableThreadType.getDescription();
			if (key.equals(COLUMN_NAME))
				return cableThreadType.getName();
			if (key.equals(COLUMN_COLOR))
				return new Integer(cableThreadType.getColor());
			if (key.equals(COLUMN_LINK_TYPE_ID))
				return cableThreadType.getLinkTypeId();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final CableThreadType object, final String key, final Object value) {
		if (object != null) {
			if (key.equals(COLUMN_NAME))
				object.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				object.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				object.setCodename((String) value);
			else if (key.equals(COLUMN_COLOR))
				object.setColor(((Integer)value).intValue());
			else if (key.equals(COLUMN_LINK_TYPE_ID))
				object.setLinkTypeId((Identifier) value);
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
		if (key.equals(COLUMN_CODENAME)
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_NAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_COLOR)) {
			return Integer.class;
		}
		if (key.equals(COLUMN_LINK_TYPE_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
