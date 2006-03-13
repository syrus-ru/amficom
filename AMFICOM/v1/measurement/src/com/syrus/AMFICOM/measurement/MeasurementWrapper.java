/*
 * $Id: MeasurementWrapper.java,v 1.17 2006/03/13 15:54:25 bass Exp $
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
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.IdlMeasurementStatus;

/**
 * @version $Revision: 1.17 $, $Date: 2006/03/13 15:54:25 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementWrapper extends StorableObjectWrapper<Measurement> {

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
		final String[] keysArray = new String[] { COLUMN_TYPE_CODE,
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
		if (instance == null) {
			instance = new MeasurementWrapper();
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
	public Object getValue(final Measurement measurement, final String key) {
		final Object value = super.getValue(measurement, key);
		if (value == null && measurement != null) {
			if (key.equals(COLUMN_TYPE_CODE)) {
				return measurement.getType();
			}
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID)) {
				return measurement.getMonitoredElementId();
			}
			if (key.equals(COLUMN_NAME)) {
				return measurement.getName();
			}
			if (key.equals(COLUMN_SETUP_ID)) {
				return measurement.getSetup();
			}
			if (key.equals(COLUMN_START_TIME)) {
				return measurement.getStartTime();
			}
			if (key.equals(COLUMN_STATUS)) {
				return new Integer(measurement.getStatus().value());
			}
			if (key.equals(COLUMN_LOCAL_ADDRESS)) {
				return measurement.getLocalAddress();
			}
			if (key.equals(COLUMN_TEST_ID)) {
				return measurement.getTestId();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final Measurement measurement, final String key, final Object value) {
		if (measurement != null) {
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID)) {
				measurement.setMonitoredElementId((Identifier) value);
			}
			else if (key.equals(COLUMN_NAME)) {
				measurement.setName((String) value);
			}
			else if (key.equals(COLUMN_SETUP_ID)) {
				measurement.setSetup((MeasurementSetup) value);
			}
			else if (key.equals(COLUMN_STATUS)) {
				measurement.setStatus(IdlMeasurementStatus.from_int(((Integer) value).intValue()));
			}
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
		if (key.equals(COLUMN_TYPE_CODE)) {
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
