/*
 * $Id: MeasurementTypeWrapper.java,v 1.13 2005/08/05 09:48:24 bob Exp $
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
 * @version $Revision: 1.13 $, $Date: 2005/08/05 09:48:24 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementTypeWrapper extends StorableObjectWrapper<MeasurementType> {

	public static final String LINK_COLUMN_MEASUREMENT_TYPE_ID = "measurement_type_id";
	public static final String LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID = "measurement_port_type_id";

	private static MeasurementTypeWrapper instance;

	private List<String> keys;

	private MeasurementTypeWrapper() {
		// private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static MeasurementTypeWrapper getInstance() {
		if (instance == null)
			instance = new MeasurementTypeWrapper();
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
	public Object getValue(final MeasurementType measurementType, final String key) {
		final Object value = super.getValue(measurementType, key);
		if (value == null && measurementType != null) {
			if (key.equals(COLUMN_CODENAME))
				return measurementType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return measurementType.getDescription();
			if (key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID))
				return measurementType.getMeasurementPortTypeIds();

		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final MeasurementType measurementType, 
	                     final String key, 
	                     final Object value) {
		if (measurementType != null) {
			if (key.equals(COLUMN_CODENAME))
				measurementType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				measurementType.setDescription((String) value);
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
		if (key.equals(COLUMN_CODENAME)
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		return null;
	}
}
