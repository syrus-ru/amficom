/*
 * $Id: AnalysisWrapper.java,v 1.1 2005/01/27 11:54:26 bob Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/27 11:54:26 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class AnalysisWrapper implements Wrapper {

	public static final String		COLUMN_TYPE_ID				= "type_id";

	public static final String		COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";

	public static final String		COLUMN_MEASUREMENT_ID		= "measurement_id";

	public static final String		COLUMN_CRITERIA_SET_ID		= "criteria_set_id";

	private static AnalysisWrapper	instance;

	private List					keys;

	private AnalysisWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_TYPE_ID, COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_MEASUREMENT_ID, COLUMN_CRITERIA_SET_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static AnalysisWrapper getInstance() {
		if (instance == null)
			instance = new AnalysisWrapper();
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
		if (object instanceof Analysis) {
			Analysis analysis = (Analysis) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return analysis.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return analysis.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return analysis.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return analysis.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return analysis.getModifierId().getIdentifierString();
			if (key.equals(COLUMN_TYPE_ID))
				return analysis.getType().getId().getIdentifierString();
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return analysis.getMonitoredElementId().getIdentifierString();
			if (key.equals(COLUMN_MEASUREMENT_ID))
				return analysis.getMeasurement().getId().getIdentifierString();
			if (key.equals(COLUMN_CRITERIA_SET_ID))
				return analysis.getCriteriaSet().getId().getIdentifierString();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Analysis) {
			Analysis analysis = (Analysis) object;
			if (key.equals(COLUMN_TYPE_ID))
				try {
					analysis.setType((ActionType) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("AnalysisWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				analysis.setMonitoredElementId(new Identifier((String) value));
			else if (key.equals(COLUMN_MEASUREMENT_ID))
				try {
					analysis.setMeasurement((Measurement) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("AnalysisWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_CRITERIA_SET_ID))
				try {
					analysis.setCriteriaSet((Set) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("AnalysisWrapper.setValue | key '" + key + "' caught " + e.getMessage());
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
		return String.class;
	}

}
