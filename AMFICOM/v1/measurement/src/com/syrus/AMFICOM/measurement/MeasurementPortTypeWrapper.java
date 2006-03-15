/*
 * $Id: MeasurementPortTypeWrapper.java,v 1.8.2.2 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.8.2.2 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementPortTypeWrapper extends StorableObjectWrapper<MeasurementPortType> {

	private static MeasurementPortTypeWrapper instance;

	private List<String> keys;

	private MeasurementPortTypeWrapper() {
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static MeasurementPortTypeWrapper getInstance() {
		if (instance == null) {
			instance = new MeasurementPortTypeWrapper();
		}
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
	public Object getValue(final MeasurementPortType object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_CODENAME)) {
				return object.getCodename();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return object.getDescription();
			}
			if (key.equals(COLUMN_NAME)) {
				return object.getName();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final MeasurementPortType object, final String key, final Object value) {
		if (object != null) {
			if (key.equals(COLUMN_NAME)) {
				object.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				object.setDescription((String) value);
			} else if (key.equals(COLUMN_CODENAME)) {
				object.setCodename((String) value);
			}
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
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME) || key.equals(COLUMN_DESCRIPTION) || key.equals(COLUMN_CODENAME)) {
			return String.class;
		}
		return null;
	}
}
