package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

public final class MeasurementSetupWrapper extends StorableObjectWrapper<MeasurementSetup> {
	public static final String COLUMN_MEASUREMENT_TEMPLATE_ID = "measurement_template_id";
	public static final String COLUMN_ANALYSIS_TEMPLATE_ID = "analysis_template_id";
	public static final String COLUMN_ETALON_TEMPLATE_ID = "etalon_template_id";
	public static final String LINK_COLUMN_MEASUREMENT_SETUP_ID = "measurement_setup_id";
	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";

	private static MeasurementSetupWrapper instance;

	private List<String> keys;

	private MeasurementSetupWrapper() {
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION,
				COLUMN_MEASUREMENT_TEMPLATE_ID,
				COLUMN_ANALYSIS_TEMPLATE_ID,
				COLUMN_ETALON_TEMPLATE_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID };
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
	public Object getValue(final MeasurementSetup object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_MEASUREMENT_TEMPLATE_ID)) {
				return object.getMeasurementTemplateId();
			}
			if (key.equals(COLUMN_ANALYSIS_TEMPLATE_ID)) {
				return object.getAnalysisTemplateId();
			}
			if (key.equals(COLUMN_ETALON_TEMPLATE_ID)) {
				return object.getEtalonTemplateId();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return object.getDescription();
			}
			if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID)) {
				return object.getMonitoredElementIds();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final MeasurementSetup storableObject, final String key, final Object value) throws PropertyChangeException {
		if (storableObject != null) {
			if (key.equals(COLUMN_MEASUREMENT_TEMPLATE_ID)) {
				storableObject.setMeasurementTemplateId((Identifier) value);
			}
			if (key.equals(COLUMN_ANALYSIS_TEMPLATE_ID)) {
				storableObject.setAnalysisTemplateId((Identifier) value);
			}
			if (key.equals(COLUMN_ETALON_TEMPLATE_ID)) {
				storableObject.setEtalonTemplateId((Identifier) value);
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				storableObject.setDescription((String) value);
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
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_MEASUREMENT_TEMPLATE_ID)
				|| key.equals(COLUMN_ANALYSIS_TEMPLATE_ID)
				|| key.equals(COLUMN_ETALON_TEMPLATE_ID)) {
			return Identifier.class;
		}
		if (key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		return null;
	}

}
