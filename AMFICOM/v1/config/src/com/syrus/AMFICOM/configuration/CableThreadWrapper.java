/*
 * $Id: CableThreadWrapper.java,v 1.15 2006/03/13 15:54:24 bass Exp $
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
 * @version $Revision: 1.15 $, $Date: 2006/03/13 15:54:24 $
 * @author $Author: bass $
 * @module config
 */

public final class CableThreadWrapper extends StorableObjectWrapper<CableThread> {

	private static CableThreadWrapper instance;

	private List<String> keys;

	private CableThreadWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_TYPE_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static CableThreadWrapper getInstance() {
		if (instance == null)
			instance = new CableThreadWrapper();
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
	public Object getValue(final CableThread cableThread, final String key) {
		final Object value = super.getValue(cableThread, key);
		if (value == null && cableThread != null) {
			if (key.equals(COLUMN_DESCRIPTION))
				return cableThread.getDescription();
			if (key.equals(COLUMN_NAME))
				return cableThread.getName();
			if (key.equals(StorableObjectWrapper.COLUMN_TYPE_ID))
				return cableThread.getType();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final CableThread cableThread, final String key, final Object value) {
		if (cableThread != null) {
			if (key.equals(COLUMN_NAME))
				cableThread.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				cableThread.setDescription((String) value);
			else if (key.equals(StorableObjectWrapper.COLUMN_TYPE_ID))
				cableThread.setType((CableThreadType)value);
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
		} else if (key.equals(StorableObjectWrapper.COLUMN_TYPE_ID)) {
			return CableThreadType.class;			
		}
		return null;
	}
}
