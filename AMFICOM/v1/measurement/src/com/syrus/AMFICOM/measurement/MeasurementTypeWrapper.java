/*
 * $Id: MeasurementTypeWrapper.java,v 1.7 2005/04/11 11:49:13 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.7 $, $Date: 2005/04/11 11:49:13 $
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
		if (object instanceof MeasurementType) {
			MeasurementType measurementType = (MeasurementType) object;
			if (key.equals(COLUMN_CODENAME))
				return measurementType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return measurementType.getDescription();
			if (key.equals(MODE_IN))
				return measurementType.getInParameterTypes();
			if (key.equals(MODE_OUT))
				return measurementType.getOutParameterTypes();
			if (key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID))
				return measurementType.getMeasurementPortTypes();

		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof MeasurementType) {
			MeasurementType measurementType = (MeasurementType) object;
			if (key.equals(COLUMN_CODENAME))
				measurementType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				measurementType.setDescription((String) value);
			else if (key.equals(MODE_IN))
				measurementType.setInParameterTypes((java.util.Set) value);
			else if (key.equals(MODE_OUT))
				measurementType.setOutParameterTypes((java.util.Set) value);
			else if (key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID))
				measurementType.setMeasurementPortTypes((java.util.Set) value);
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
		if (key.equals(MODE_IN) || key.equals(MODE_OUT) || key.equals(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID))
			return java.util.Set.class;
		return String.class;
	}
}
