/*
 * $Id: MeasurementSetupWrapper.java,v 1.2 2005/02/01 06:38:49 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/01 06:38:49 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementSetupWrapper implements Wrapper {

	public static final String				COLUMN_CRITERIA_SET_ID				= "criteria_set_id";
	public static final String				COLUMN_DESCRIPTION					= "description";
	public static final String				COLUMN_ETALON_ID					= "etalon_id";
	public static final String				COLUMN_MEASUREMENT_DURAION			= "measurement_duration";
	public static final String				COLUMN_PARAMETER_SET_ID				= "parameter_set_id";
	public static final String				COLUMN_THRESHOLD_SET_ID				= "threshold_set_id";
	public static final String				LINK_COLUMN_ME_ID					= "monitored_element_id";
	public static final String				LINK_COLUMN_MEASUREMENT_SETUP_ID	= "measurement_setup_id";

	private static MeasurementSetupWrapper	instance;

	private List							keys;

	private MeasurementSetupWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_PARAMETER_SET_ID, COLUMN_CRITERIA_SET_ID, COLUMN_THRESHOLD_SET_ID,
				COLUMN_ETALON_ID, COLUMN_DESCRIPTION, COLUMN_MEASUREMENT_DURAION, LINK_COLUMN_ME_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MeasurementSetupWrapper getInstance() {
		if (instance == null)
			instance = new MeasurementSetupWrapper();
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
		if (object instanceof MeasurementSetup) {
			MeasurementSetup measurementSetup = (MeasurementSetup) object;
			if (key.equals(COLUMN_PARAMETER_SET_ID))
				return measurementSetup.getParameterSet();
			if (key.equals(COLUMN_CRITERIA_SET_ID))
				return measurementSetup.getCriteriaSet();
			if (key.equals(COLUMN_THRESHOLD_SET_ID))
				return measurementSetup.getThresholdSet();
			if (key.equals(COLUMN_ETALON_ID))
				return measurementSetup.getEtalon();
			if (key.equals(COLUMN_DESCRIPTION))
				return measurementSetup.getDescription();
			if (key.equals(COLUMN_MEASUREMENT_DURAION))
				return new Long(measurementSetup.getMeasurementDuration());
			if (key.equals(LINK_COLUMN_ME_ID))
				return measurementSetup.getMonitoredElementIds();

		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof MeasurementSetup) {
			MeasurementSetup measurementSetup = (MeasurementSetup) object;

			if (key.equals(COLUMN_PARAMETER_SET_ID))
				measurementSetup.setParameterSet((Set) value);
			else if (key.equals(COLUMN_CRITERIA_SET_ID))
				measurementSetup.setCriteriaSet((Set) value);
			else if (key.equals(COLUMN_THRESHOLD_SET_ID))
				measurementSetup.setThresholdSet((Set) value);
			else if (key.equals(COLUMN_ETALON_ID))
				measurementSetup.setEtalon((Set) value);
			if (key.equals(COLUMN_DESCRIPTION))
				measurementSetup.setDescription((String) value);
			if (key.equals(COLUMN_MEASUREMENT_DURAION))
				measurementSetup.setMeasurementDuration(((Long) value).longValue());
			if (key.equals(LINK_COLUMN_ME_ID))
				measurementSetup.setMonitoredElementIds((List) value);
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
		if (key.equals(LINK_COLUMN_ME_ID))
			return List.class;
		return String.class;
	}
}
