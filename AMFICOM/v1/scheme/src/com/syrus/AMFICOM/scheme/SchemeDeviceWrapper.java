/*-
 * $Id: SchemeDeviceWrapper.java,v 1.12 2006/03/15 15:49:10 arseniy Exp $
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
import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.12 $, $Date: 2006/03/15 15:49:10 $
 * @author $Author: arseniy $
 * @module scheme
 */
public final class SchemeDeviceWrapper extends StorableObjectWrapper<SchemeDevice> {
	
//	schemedevice.sql
//	
//	parent_scheme_proto_element_id VARCHAR2(32 CHAR),
//	parent_scheme_element_id VARCHAR2(32 CHAR),
	
	public static final String COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID = "parent_scheme_proto_element_id";
	public static final String COLUMN_PARENT_SCHEME_ELEMENT_ID = "parent_scheme_element_id";

	private static SchemeDeviceWrapper instance;
	
	private final List<String> keys;
	
	private SchemeDeviceWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID,
				COLUMN_PARENT_SCHEME_ELEMENT_ID}));
	}
	
	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		return key;
	}

	@Override
	public Class<?> getPropertyClass(String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		//there is no property
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		//there is no property
	}

	@Override
	public Object getValue(SchemeDevice schemeDevice, String key) {
		final Object value = super.getValue(schemeDevice, key);
		if (value != null) {
			return value;
		}
		if (schemeDevice != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeDevice.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeDevice.getDescription();
			} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID)) {
				return schemeDevice.getParentSchemeProtoElementId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
				return schemeDevice.getParentSchemeElementId();
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	@Override
	public void setValue(final SchemeDevice schemeDevice,
			final String key,
			final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (schemeDevice != null) {
				if (key.equals(COLUMN_NAME)) {
					schemeDevice.setName((String) value);
				} else if (key.equals(COLUMN_DESCRIPTION)) {
					schemeDevice.setDescription((String) value);
				} else if (key.equals(COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID)) {
					schemeDevice.setParentSchemeProtoElementId((Identifier) value, usePool);
				} else if (key.equals(COLUMN_PARENT_SCHEME_ELEMENT_ID)) {
					schemeDevice.setParentSchemeElementId((Identifier) value, usePool);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static SchemeDeviceWrapper getInstance() {
		if (instance == null)
			instance = new SchemeDeviceWrapper();
		return instance;
	}
}
