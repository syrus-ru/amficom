/*
 * $Id: MeasurementSetupWrapper.java,v 1.1 2005/01/27 15:13:52 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/27 15:13:52 $
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
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_PARAMETER_SET_ID, COLUMN_CRITERIA_SET_ID,
				COLUMN_THRESHOLD_SET_ID, COLUMN_ETALON_ID, COLUMN_DESCRIPTION, COLUMN_MEASUREMENT_DURAION,
				LINK_COLUMN_ME_ID};

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
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return measurementSetup.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return measurementSetup.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return measurementSetup.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return measurementSetup.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return measurementSetup.getModifierId().getIdentifierString();
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
				return Long.toString(measurementSetup.getMeasurementDuration());
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
				try {
					measurementSetup.setParameterSet((Set) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("MeasurementSetupWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			if (key.equals(COLUMN_CRITERIA_SET_ID))
				try {
					measurementSetup.setCriteriaSet((Set) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("MeasurementSetupWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			if (key.equals(COLUMN_THRESHOLD_SET_ID))
				try {
					measurementSetup.setThresholdSet((Set) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("MeasurementSetupWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			if (key.equals(COLUMN_ETALON_ID))
				try {
					measurementSetup.setEtalon((Set) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("MeasurementSetupWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			if (key.equals(COLUMN_DESCRIPTION))
				measurementSetup.setDescription((String) value);
			if (key.equals(COLUMN_MEASUREMENT_DURAION))
				measurementSetup.setMeasurementDuration(Long.parseLong((String) value));
			if (key.equals(LINK_COLUMN_ME_ID)) {
				List meIdStr = (List) value;
				List meIds = new ArrayList(meIdStr.size());
				for (Iterator it = meIdStr.iterator(); it.hasNext();)
					meIds.add(new Identifier((String) it.next()));
				measurementSetup.setMonitoredElementIds(meIds);
			}

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
