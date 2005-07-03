/*
 * $Id: MeasurementSetupWrapper.java,v 1.15 2005/06/22 10:22:59 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.LangModelMeasurement;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2005/06/22 10:22:59 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class MeasurementSetupWrapper extends StorableObjectWrapper {

	public static final String COLUMN_CRITERIA_SET_ID = "criteria_set_id";
	public static final String COLUMN_ETALON_ID = "etalon_id";
	public static final String COLUMN_MEASUREMENT_DURAION = "measurement_duration";
	public static final String COLUMN_PARAMETER_SET_ID = "parameter_set_id";
	public static final String COLUMN_THRESHOLD_SET_ID = "threshold_set_id";
	public static final String SUMMARY_INFO = "summary_info";
	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String LINK_COLUMN_MEASUREMENT_TYPE_ID = "measurement_type_id";
	public static final String LINK_COLUMN_MEASUREMENT_SETUP_ID = "measurement_setup_id";

	private static MeasurementSetupWrapper instance;

	private List keys;

	private MeasurementSetupWrapper() {
		// empty private constructor
		String[] keysArray = new String[] {COLUMN_PARAMETER_SET_ID,
				COLUMN_CRITERIA_SET_ID,
				COLUMN_THRESHOLD_SET_ID,
				COLUMN_ETALON_ID,
				COLUMN_DESCRIPTION,
				COLUMN_MEASUREMENT_DURAION,
				LINK_COLUMN_MONITORED_ELEMENT_ID,
				LINK_COLUMN_MEASUREMENT_TYPE_ID};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
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
		
		Object value = super.getValue(object, key);
		if (value == null && object instanceof MeasurementSetup) {
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
			if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID))
				return measurementSetup.getMonitoredElementIds();
			if (key.equals(LINK_COLUMN_MEASUREMENT_TYPE_ID))
				return measurementSetup.getMeasurementTypeIds();
			if (key.equals(SUMMARY_INFO)) {
				return this.getMeasurementSetupInfo(measurementSetup);
			}

		}
		return value;
	}
	
	private void addSetParameterInfo(StringBuffer buffer, String title, Parameter[] parameters) {
		if (parameters.length == 0)
			return;
		buffer.append(title);
		buffer.append('\n');
		for (int i = 0; i < parameters.length; i++) {				
			String string = parameters[i].getStringValue();
			if (string != null) {
				ParameterType parameterType = (ParameterType) parameters[i].getType();
				buffer.append(parameterType.getDescription() + ':' + string);
				java.util.Set characteristics = parameterType.getCharacteristics();
				Log.debugMessage("MeasurementSetupWrapper.addSetParameterInfo | ParameterType " + parameterType.getId() + ", "
					+ parameterType.getCodename() + ", characteristics size:" + characteristics.size(), Log.FINEST);
				if (!characteristics.isEmpty()) {
					for (Iterator it = characteristics.iterator(); it.hasNext();) {
						Characteristic characteristic = (Characteristic) it.next();
						StorableObjectType type = characteristic.getType();
						Log.debugMessage("MeasurementSetupWrapper.addSetParameterInfo | characteristic type codename " + type.getCodename(), Log.FINEST);
						if (type.getCodename().startsWith(CharacteristicTypeCodenames.UNITS_PREFIX)) {
							buffer.append(' ' + characteristic.getValue());
							/* TODO check for all codename ?*/
							break;
						}
					}
				}
				buffer.append('\n');
			}
		}
		buffer.append('\n');
	}
	
	private String getMeasurementSetupInfo(MeasurementSetup measurementSetup) {
		ParameterSet parameterSet = measurementSetup.getParameterSet();
		ParameterSet criteriaSet = measurementSetup.getCriteriaSet();
		ParameterSet etalon = measurementSetup.getEtalon();
		StringBuffer buffer = new StringBuffer();
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

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof MeasurementSetup) {
			MeasurementSetup measurementSetup = (MeasurementSetup) object;

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
				measurementSetup.setMonitoredElementIds((java.util.Set) value);
			if (key.equals(LINK_COLUMN_MEASUREMENT_TYPE_ID))
				measurementSetup.setMeasurementTypeIds((java.util.Set) value);
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
		Class clazz = super.getPropertyClass(key); 
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
		if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID) 
				|| key.equals(LINK_COLUMN_MEASUREMENT_TYPE_ID)) {
			return Set.class;
		}
		return null;
	}
}
