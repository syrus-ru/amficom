/*
 * $Id: EvaluationWrapper.java,v 1.2 2005/01/27 15:14:41 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/01/27 15:14:41 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class EvaluationWrapper implements Wrapper {

	public static final String			COLUMN_TYPE_ID				= "type_id";
	public static final String			COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String			COLUMN_MEASUREMENT_ID		= "measurement_id";
	public static final String			COLUMN_THRESHOLD_SET_ID		= "threshold_set_id";

	private static EvaluationWrapper	instance;

	private List						keys;

	private EvaluationWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_TYPE_ID, COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_MEASUREMENT_ID, COLUMN_THRESHOLD_SET_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static EvaluationWrapper getInstance() {
		if (instance == null)
			instance = new EvaluationWrapper();
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
		if (object instanceof Evaluation) {
			Evaluation evaluation = (Evaluation) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return evaluation.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return evaluation.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return evaluation.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return evaluation.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return evaluation.getModifierId().getIdentifierString();
			if (key.equals(COLUMN_TYPE_ID))
				return evaluation.getType();
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return evaluation.getMonitoredElementId().getIdentifierString();
			if (key.equals(COLUMN_MEASUREMENT_ID))
				return evaluation.getMeasurement();
			if (key.equals(COLUMN_THRESHOLD_SET_ID))
				return evaluation.getThresholdSet();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Evaluation) {
			Evaluation evaluation = (Evaluation) object;
			if (key.equals(COLUMN_TYPE_ID))
				try {
					evaluation.setType((ActionType) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("EvaluationWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				evaluation.setMonitoredElementId(new Identifier((String) value));
			else if (key.equals(COLUMN_MEASUREMENT_ID))
				try {
					evaluation.setMeasurement((Measurement) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("EvaluationWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_THRESHOLD_SET_ID))
				try {
					evaluation.setThresholdSet((Set) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("EvaluationWrapper.setValue | key '" + key + "' caught " + e.getMessage());
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
