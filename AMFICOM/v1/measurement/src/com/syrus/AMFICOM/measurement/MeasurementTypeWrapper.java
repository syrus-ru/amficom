/*
 * $Id: MeasurementTypeWrapper.java,v 1.9 2005/06/22 10:22:59 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/22 10:22:59 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementTypeWrapper extends StorableObjectWrapper {

	public static final String				MODE_IN									= "IN";
	public static final String				MODE_OUT								= "OUT";
	public static final String				LINK_COLUMN_MEASUREMENT_TYPE_ID			= "measurement_type_id";
	public static final String				LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID	= "measurement_port_type_id";

	private static MeasurementTypeWrapper	instance;

	private List							keys;

	private MeasurementTypeWrapper() {
		// private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, MODE_IN, MODE_OUT,
				LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static MeasurementTypeWrapper getInstance() {
		if (instance == null)
			instance = new MeasurementTypeWrapper();
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
		if (value == null && object instanceof MeasurementType) {
			MeasurementType measurementType = (MeasurementType) object;
			if (key.equals(COLUMN_CODENAME))
				return measurementType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return measurementType.getDescription();
			if (key.equals(MODE_IN))
				return measurementType.getInParameterTypeIds();
			if (key.equals(MODE_OUT))
				return measurementType.getOutParameterTypeIds();
			if (key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID))
				return measurementType.getMeasurementPortTypeIds();

		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, 
	                     final String key, 
	                     final Object value) {
		if (object instanceof MeasurementType) {
			MeasurementType measurementType = (MeasurementType) object;
			if (key.equals(COLUMN_CODENAME))
				measurementType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				measurementType.setDescription((String) value);
			else if (key.equals(MODE_IN))
				measurementType.setInParameterTypeIds((Set) value);
			else if (key.equals(MODE_OUT))
				measurementType.setOutParameterTypeIds((Set) value);
			else if (key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID))
				measurementType.setMeasurementPortTypeIds((Set) value);
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, 
	                             final Object objectKey, 
	                             final Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(final String key) {
		Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME)
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		if (key.equals(MODE_IN) 
				|| key.equals(MODE_OUT) 
				|| key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID)) {
			return Set.class;
		}
		return null;
	}
}
