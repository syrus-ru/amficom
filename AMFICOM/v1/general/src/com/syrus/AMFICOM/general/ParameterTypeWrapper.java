/*-
 * $Id: ParameterTypeWrapper.java,v 1.16.2.1 2006/02/11 18:54:53 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.16.2.1 $, $Date: 2006/02/11 18:54:53 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class ParameterTypeWrapper extends StorableObjectWrapper<ParameterType> {
	public static final String COLUMN_DATA_TYPE_CODE = "data_type_code";
	public static final String COLUMN_MEASUREMENT_UNIT_CODE = "measurement_unit_code";

	private static ParameterTypeWrapper instance;

	private List<String> keys;

	private ParameterTypeWrapper() {
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_DATA_TYPE_CODE, COLUMN_MEASUREMENT_UNIT_CODE };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ParameterTypeWrapper getInstance() {
		if (instance == null) {
			instance = new ParameterTypeWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return null;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final ParameterType object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_CODENAME)) {
				return object.getCodename();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return object.getDescription();
			}
			if (key.equals(COLUMN_DATA_TYPE_CODE)) {
				return object.getDataType();
			}
			if (key.equals(COLUMN_MEASUREMENT_UNIT_CODE)) {
				return object.getMeasurementUnit();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final ParameterType object, final String key, final Object value) throws PropertyChangeException {
		if (object != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
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
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION) || key.equals(COLUMN_CODENAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_DATA_TYPE_CODE)) {
			return DataType.class;
		}
		if (key.equals(COLUMN_MEASUREMENT_UNIT_CODE)) {
			return MeasurementUnit.class;
		}
		return null;
	}

}
