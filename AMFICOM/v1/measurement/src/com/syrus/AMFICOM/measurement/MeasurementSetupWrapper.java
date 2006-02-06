/*
 * $Id: MeasurementSetupWrapper.java,v 1.30 2005/10/25 19:53:05 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.30 $, $Date: 2005/10/25 19:53:05 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementSetupWrapper extends StorableObjectWrapper<MeasurementSetup> {

	public static final String COLUMN_CRITERIA_SET_ID = "criteria_set_id";
	public static final String COLUMN_ETALON_ID = "etalon_id";
	public static final String COLUMN_MEASUREMENT_DURAION = "measurement_duration";
	public static final String COLUMN_PARAMETER_SET_ID = "parameter_set_id";
	public static final String COLUMN_THRESHOLD_SET_ID = "threshold_set_id";
	public static final String SUMMARY_INFO = "summary_info";
	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String LINK_COLUMN_MEASUREMENT_TYPE_CODE = "measurement_type_code";
	public static final String LINK_COLUMN_MEASUREMENT_SETUP_ID = "measurement_setup_id";

	private static MeasurementSetupWrapper instance;

	private List<String> keys;

	private MeasurementSetupWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] {COLUMN_PARAMETER_SET_ID,
				COLUMN_CRITERIA_SET_ID,
				COLUMN_THRESHOLD_SET_ID,
				COLUMN_ETALON_ID,
				COLUMN_DESCRIPTION,
				COLUMN_MEASUREMENT_DURAION,
				LINK_COLUMN_MONITORED_ELEMENT_ID,
				LINK_COLUMN_MEASUREMENT_TYPE_CODE};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static MeasurementSetupWrapper getInstance() {
		if (instance == null) {
			instance = new MeasurementSetupWrapper();
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
	public Object getValue(final MeasurementSetup measurementSetup, final String key) {
		final Object value = super.getValue(measurementSetup, key);
		if (value == null && measurementSetup != null) {
			if (key.equals(COLUMN_PARAMETER_SET_ID)) {
				return measurementSetup.getParameterSet();
			}
			if (key.equals(COLUMN_CRITERIA_SET_ID)) {
				return measurementSetup.getCriteriaSet();
			}
			if (key.equals(COLUMN_THRESHOLD_SET_ID)) {
				return measurementSetup.getThresholdSet();
			}
			if (key.equals(COLUMN_ETALON_ID)) {
				return measurementSetup.getEtalon();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return measurementSetup.getDescription();
			}
			if (key.equals(COLUMN_MEASUREMENT_DURAION)) {
				return new Long(measurementSetup.getMeasurementDuration());
			}
			if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID)) {
				return measurementSetup.getMonitoredElementIds();
			}
			if (key.equals(LINK_COLUMN_MEASUREMENT_TYPE_CODE)) {
				return measurementSetup.getMeasurementTypes();
			}
			if (key.equals(SUMMARY_INFO)) {
				return this.getMeasurementSetupInfo(measurementSetup);
			}
		}
		return value;
	}

	private void addSetParameterInfo(final StringBuffer buffer, final String title, final Parameter[] parameters) {
		if (parameters.length == 0) {
			return;
		}
		buffer.append(title);
		buffer.append('\n');
		
		List<String> infoList = new ArrayList<String>(parameters.length);
		
		for (int i = 0; i < parameters.length; i++) {				
			final String string = parameters[i].getStringValue();
			if (string != null) {
				final ParameterType parameterType = parameters[i].getType();
				String s = parameterType.getDescription() + ": " + string;
//				buffer.append(parameterType.getDescription() + ": " + string);
				s = s + ' ' + parameterType.getMeasurementUnit().getName();
//				buffer.append('\n');
				infoList.add(s);
			}
		}
		Collections.sort(infoList);
		for(String s : infoList) {
			buffer.append(s + '\n');			
		}
		buffer.append('\n');
	}
	
	private String getMeasurementSetupInfo(final MeasurementSetup measurementSetup) {
		final ParameterSet parameterSet = measurementSetup.getParameterSet();
		final ParameterSet criteriaSet = measurementSetup.getCriteriaSet();
		final ParameterSet etalon = measurementSetup.getEtalon();
		final StringBuffer buffer = new StringBuffer();
		if (parameterSet != null) {
			this.addSetParameterInfo(buffer, LangModelMeasurement.getString("Measurement parameters") + ':',  parameterSet.getParameters());
		}
		
		if (criteriaSet != null) {
			this.addSetParameterInfo(buffer, LangModelMeasurement.getString("Criteria parameters") + ':',  criteriaSet.getParameters());
		}
		
		if (etalon != null) {
			this.addSetParameterInfo(buffer, LangModelMeasurement.getString("Etalon parameters") + ':',  etalon.getParameters());			
		}

		return buffer.toString();
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final MeasurementSetup measurementSetup, final String key, final Object value) {
		if (measurementSetup != null) {
			if (key.equals(COLUMN_PARAMETER_SET_ID))
				measurementSetup.setParameterSet((ParameterSet) value);
			else if (key.equals(COLUMN_CRITERIA_SET_ID))
				measurementSetup.setCriteriaSet((ParameterSet) value);
			else if (key.equals(COLUMN_THRESHOLD_SET_ID))
				measurementSetup.setThresholdSet((ParameterSet) value);
			else if (key.equals(COLUMN_ETALON_ID))
				measurementSetup.setEtalon((ParameterSet) value);
			if (key.equals(COLUMN_DESCRIPTION))
				measurementSetup.setDescription((String) value);
			if (key.equals(COLUMN_MEASUREMENT_DURAION))
				measurementSetup.setMeasurementDuration(((Long) value).longValue());
			if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID))
				measurementSetup.setMonitoredElementIds((Set) value);
			if (key.equals(LINK_COLUMN_MEASUREMENT_TYPE_CODE))
				measurementSetup.setMeasurementTypes((EnumSet) value);
		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
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
		if (key.equals(COLUMN_PARAMETER_SET_ID) 
				|| key.equals(COLUMN_CRITERIA_SET_ID)
				|| key.equals(COLUMN_THRESHOLD_SET_ID)
				|| key.equals(COLUMN_ETALON_ID)) {
			return ParameterSet.class;
		}
		if (key.equals(COLUMN_DESCRIPTION)
				|| key.equals(SUMMARY_INFO)) {
			return String.class;
		}
		if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID)) {
			return Set.class;
		}
		if (key.equals(LINK_COLUMN_MEASUREMENT_TYPE_CODE)) {
			return EnumSet.class;
		}
		return null;
	}
}
