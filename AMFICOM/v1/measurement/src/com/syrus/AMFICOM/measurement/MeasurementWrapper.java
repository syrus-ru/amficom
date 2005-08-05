/*
 * $Id: MeasurementWrapper.java,v 1.10 2005/08/05 09:48:24 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.MeasurementStatus;

/**
 * @version $Revision: 1.10 $, $Date: 2005/08/05 09:48:24 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementWrapper extends StorableObjectWrapper<Measurement> {

	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_SETUP_ID = "setup_id";
	public static final String COLUMN_START_TIME = "start_time";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_LOCAL_ADDRESS = "local_address";
	public static final String COLUMN_TEST_ID = "test_id";

	private static MeasurementWrapper instance;

	private List<String> keys;

	private MeasurementWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_TYPE_ID,
				COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_NAME,
				COLUMN_SETUP_ID,
				COLUMN_START_TIME,
				COLUMN_DURATION,
				COLUMN_STATUS,
				COLUMN_LOCAL_ADDRESS,
				COLUMN_TEST_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static MeasurementWrapper getInstance() {
		if (instance == null)
			instance = new MeasurementWrapper();
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
	public Object getValue(final Measurement measurement, final String key) {
		final Object value = super.getValue(measurement, key);
		if (value == null && measurement != null) {
			if (key.equals(COLUMN_TYPE_ID))
				return measurement.getType();
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return measurement.getMonitoredElementId();
			if (key.equals(COLUMN_NAME))
				return measurement.getName();
			if (key.equals(COLUMN_SETUP_ID))
				return measurement.getSetup();
			if (key.equals(COLUMN_START_TIME))
				return measurement.getStartTime();
			if (key.equals(COLUMN_STATUS))
				return new Integer(measurement.getStatus().value());
			if (key.equals(COLUMN_LOCAL_ADDRESS))
				return measurement.getLocalAddress();
			if (key.equals(COLUMN_TEST_ID))
				return measurement.getTestId();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Measurement measurement, 
	                     final String key, 
	                     final Object value) {
		if (measurement != null) {
			if (key.equals(COLUMN_TYPE_ID))
				measurement.setType((ActionType) value);
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				measurement.setMonitoredElementId((Identifier) value);
			else if (key.equals(COLUMN_NAME))
				measurement.setName((String) value);
			else if (key.equals(COLUMN_SETUP_ID))
				measurement.setSetup((MeasurementSetup) value);
			else if (key.equals(COLUMN_START_TIME))
				measurement.setStartTime((Date) value);
			else if (key.equals(COLUMN_STATUS))
				measurement.setStatus(MeasurementStatus.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_LOCAL_ADDRESS))
				measurement.setLocalAddress((String) value);
			else if (key.equals(COLUMN_TEST_ID))
				measurement.setTestId((Identifier) value);

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
		if (key.equals(COLUMN_TYPE_ID)) {
			return MeasurementType.class;
		}
		if (key.equals(COLUMN_MONITORED_ELEMENT_ID)
				|| key.equals(COLUMN_TEST_ID)) {
			return Identifier.class;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_STATUS)) {
			return String.class;
		}
		if (key.equals(COLUMN_SETUP_ID)) {
			return MeasurementSetup.class;
		}
		if (key.equals(COLUMN_START_TIME)) {
			return Date.class;
		}
		if (key.equals(COLUMN_STATUS)) {
			return Integer.class;
		}
		return null;
	}

}
