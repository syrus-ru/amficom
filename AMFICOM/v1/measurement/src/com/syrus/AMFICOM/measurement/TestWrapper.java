/*
 * $Id: TestWrapper.java,v 1.1 2005/01/28 08:20:26 bob Exp $
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
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/28 08:20:26 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class TestWrapper implements Wrapper {

	public static final String	COLUMN_ANALYSIS_TYPE_ID			= "analysis_type_id";
	public static final String	COLUMN_DESCRIPTION				= "description";
	public static final String	COLUMN_END_TIME					= "end_time";
	public static final String	COLUMN_EVALUATION_TYPE_ID		= "evaluation_type_id";
	public static final String	COLUMN_MEASUREMENT_TYPE_ID		= "measurement_type_id";
	public static final String	COLUMN_MONITORED_ELEMENT_ID		= "monitored_element_id";
	public static final String	COLUMN_RETURN_TYPE				= "return_type";
	public static final String	COLUMN_START_TIME				= "start_time";
	public static final String	COLUMN_STATUS					= "status";
	public static final String	COLUMN_TEMPORAL_PATTERN_ID		= "temporal_pattern_id";
	public static final String	COLUMN_TEMPORAL_TYPE			= "temporal_type";
	public static final String	LINK_COLMN_MEASUREMENT_SETUP_ID	= "measurement_setup_id";

	private static TestWrapper	instance;

	private List				keys;

	private TestWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_TEMPORAL_TYPE, COLUMN_START_TIME, COLUMN_END_TIME,
				COLUMN_TEMPORAL_PATTERN_ID, COLUMN_MEASUREMENT_TYPE_ID, COLUMN_ANALYSIS_TYPE_ID,
				COLUMN_EVALUATION_TYPE_ID, COLUMN_STATUS, COLUMN_MONITORED_ELEMENT_ID, COLUMN_RETURN_TYPE,
				COLUMN_DESCRIPTION, LINK_COLMN_MEASUREMENT_SETUP_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static TestWrapper getInstance() {
		if (instance == null)
			instance = new TestWrapper();
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
		if (object instanceof Test) {
			Test test = (Test) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return test.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return test.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return test.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return test.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return test.getModifierId().getIdentifierString();
			if (key.equals(COLUMN_TEMPORAL_TYPE))
				return Integer.toString(test.getTemporalType().value());
			if (key.equals(COLUMN_START_TIME))
				return Long.toString(test.getStartTime().getTime());
			if (key.equals(COLUMN_END_TIME))
				return Long.toString(test.getEndTime().getTime());
			if (key.equals(COLUMN_TEMPORAL_PATTERN_ID))
				return test.getTemporalPattern();
			if (key.equals(COLUMN_MEASUREMENT_TYPE_ID))
				return test.getMeasurementType();
			if (key.equals(COLUMN_ANALYSIS_TYPE_ID))
				return test.getAnalysisType();
			if (key.equals(COLUMN_EVALUATION_TYPE_ID))
				return test.getEvaluationType();
			if (key.equals(COLUMN_STATUS))
				return Integer.toString(test.getStatus().value());
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return test.getMonitoredElement();
			if (key.equals(COLUMN_RETURN_TYPE))
				return Integer.toString(test.getReturnType().value());
			if (key.equals(COLUMN_DESCRIPTION))
				return test.getDescription();
			if (key.equals(LINK_COLMN_MEASUREMENT_SETUP_ID))
				return test.getMeasurementSetupIds();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Test) {
			Test test = (Test) object;
			if (key.equals(COLUMN_TEMPORAL_TYPE))
				test.setTemporalType(TestTemporalType.from_int(Integer.parseInt((String) value)));
			else if (key.equals(COLUMN_START_TIME))
				test.setStartTime(new Date(Long.parseLong((String) value)));
			else if (key.equals(COLUMN_END_TIME))
				test.setEndTime(new Date(Long.parseLong((String) value)));
			else if (key.equals(COLUMN_TEMPORAL_PATTERN_ID))
				try {
					test.setTemporalPattern((TemporalPattern) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("TestWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_MEASUREMENT_TYPE_ID))
				try {
					test.setMeasurementType((MeasurementType) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("TestWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_ANALYSIS_TYPE_ID))
				try {
					test.setAnalysisType((AnalysisType) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("TestWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_EVALUATION_TYPE_ID))
				try {
					test.setEvaluationType((EvaluationType) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("TestWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_STATUS))
				test.setStatus(TestStatus.from_int(Integer.parseInt((String) value)));
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				try {
					test.setMonitoredElement((MonitoredElement) MeasurementStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("TestWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			else if (key.equals(COLUMN_RETURN_TYPE))
				test.setReturnType(TestReturnType.from_int(Integer.parseInt((String) value)));
			else if (key.equals(COLUMN_DESCRIPTION))
				test.setDescription((String) value);
			else if (key.equals(LINK_COLMN_MEASUREMENT_SETUP_ID)) {
				List msIdStr = (List) value;
				List msIds = new ArrayList(msIdStr.size());
				for (Iterator it = msIdStr.iterator(); it.hasNext();)
					msIds.add(new Identifier((String) it.next()));
				test.setMeasurementSetupIds0(msIds);
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
