/*
 * $Id: MeasurementWrapper.java,v 1.1 2005/01/27 15:49:23 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/27 15:49:23 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementWrapper implements Wrapper {

	public static final String			COLUMN_TYPE_ID				= "type_id";
	public static final String			COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String			COLUMN_NAME					= "name";
	public static final String			COLUMN_SETUP_ID				= "setup_id";
	public static final String			COLUMN_START_TIME			= "start_time";
	public static final String			COLUMN_DURATION				= "duration";
	public static final String			COLUMN_STATUS				= "status";
	public static final String			COLUMN_LOCAL_ADDRESS		= "local_address";
	public static final String			COLUMN_TEST_ID				= "test_id";

	private static MeasurementWrapper	instance;

	private List						keys;

	private MeasurementWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_TYPE_ID, COLUMN_MONITORED_ELEMENT_ID, COLUMN_NAME,
				COLUMN_SETUP_ID, COLUMN_START_TIME, COLUMN_DURATION, COLUMN_STATUS, COLUMN_LOCAL_ADDRESS,
				COLUMN_TEST_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MeasurementWrapper getInstance() {
		if (instance == null)
			instance = new MeasurementWrapper();
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
		if (object instanceof Measurement) {
			Measurement measurement = (Measurement) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return measurement.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return measurement.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return measurement.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return measurement.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return measurement.getModifierId().getIdentifierString();
			if (key.equals(COLUMN_TYPE_ID))
				return measurement.getType().getId().getIdentifierString();
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return measurement.getMonitoredElementId().getIdentifierString();
			if (key.equals(COLUMN_NAME))
				return measurement.getName();
			if (key.equals(COLUMN_SETUP_ID))
				return measurement.getSetup();
			if (key.equals(COLUMN_START_TIME))
				return Long.toString(measurement.getStartTime().getTime());
			if (key.equals(COLUMN_STATUS))
				return Integer.toString(measurement.getStatus().value());
			if (key.equals(COLUMN_LOCAL_ADDRESS))
				return measurement.getLocalAddress();
			if (key.equals(COLUMN_TEST_ID))
				return measurement.getTestId();

		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Measurement) {
			Measurement measurement = (Measurement) object;
			if (key.equals(COLUMN_TYPE_ID))
				try {
					measurement.setType((ActionType) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("MeasurementWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				measurement.setMonitoredElementId(new Identifier((String) value));
			else if (key.equals(COLUMN_NAME))
				measurement.setName((String) value);
			else if (key.equals(COLUMN_SETUP_ID))
				try {
					measurement.setSetup((MeasurementSetup) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("MeasurementWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_START_TIME))
				measurement.setStartTime(new Date(Long.parseLong((String) value)));
			else if (key.equals(COLUMN_STATUS))
				measurement.setStatus(MeasurementStatus.from_int(Integer.parseInt((String) value)));
			else if (key.equals(COLUMN_LOCAL_ADDRESS))
				measurement.setLocalAddress((String) value);
			else if (key.equals(COLUMN_TEST_ID))
				measurement.setTestId(new Identifier((String) value));

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
		return String.class;
	}

}
