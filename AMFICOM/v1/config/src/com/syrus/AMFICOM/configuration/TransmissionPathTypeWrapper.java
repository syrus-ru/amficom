/*
 * $Id: TransmissionPathTypeWrapper.java,v 1.15 2005/09/14 18:42:07 arseniy Exp $
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
 * @version $Revision: 1.15 $, $Date: 2005/09/14 18:42:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class TransmissionPathTypeWrapper extends StorableObjectWrapper<TransmissionPathType> {

	private static TransmissionPathTypeWrapper instance;

	private List<String> keys;

	private TransmissionPathTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, 
				COLUMN_DESCRIPTION, 
				COLUMN_NAME };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static TransmissionPathTypeWrapper getInstance() {
		if (instance == null)
			instance = new TransmissionPathTypeWrapper();
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
	public Object getValue(final TransmissionPathType transmissionPathType, 
	                       final String key) {
		final Object value = super.getValue(transmissionPathType, key);
		if (value == null && transmissionPathType != null) {
			if (key.equals(COLUMN_CODENAME))
				return transmissionPathType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return transmissionPathType.getDescription();
			if (key.equals(COLUMN_NAME))
				return transmissionPathType.getName();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final TransmissionPathType transmissionPathType, final String key, final Object value) {
		if (transmissionPathType != null) {
			if (key.equals(COLUMN_NAME))
				transmissionPathType.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				transmissionPathType.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				transmissionPathType.setCodename((String) value);
		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME)
				|| key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		return null;
	}
}
