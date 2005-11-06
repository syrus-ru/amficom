/**
 * $Id: AlarmWrapper.java,v 1.2 2005/11/06 14:37:32 stas Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.observer.alarm;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.PropertyChangeException;

public class AlarmWrapper extends StorableObjectWrapper<Alarm> {
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_OPTICAL_DISTANCE = "optical_distance";
	public static final String COLUMN_PHYSICAL_DISTANCE = "physical_distance";
	public static final String COLUMN_PATH = "path";
	public static final String COLUMN_ELEMENT = "element";
	
	private final List<String> keys;
	
	public AlarmWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_MESSAGE,
				COLUMN_DATE, 
				COLUMN_OPTICAL_DISTANCE, 
				COLUMN_PHYSICAL_DISTANCE,
				COLUMN_PATH, 
				COLUMN_ELEMENT
		}));
	}
	
	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		return key;
	}

	@Override
	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_OPTICAL_DISTANCE)
				|| key.equals(COLUMN_PHYSICAL_DISTANCE)) {
			return Double.class;
		}
		if (key.equals(COLUMN_DATE)) {
			return Date.class;
		}
		if (key.equals(COLUMN_MESSAGE)
				|| key.equals(COLUMN_PATH)
				|| key.equals(COLUMN_ELEMENT)) {
			return String.class;
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
	}

	@Override
	public Object getValue(Alarm alarm, String key) {
		final Object value = super.getValue(alarm, key);
		if (value != null) {
			return value;
		}
		if (alarm != null) {
			if (key.equals(COLUMN_MESSAGE)) {
				return alarm.getEvent().getMessage();
			}
			if (key.equals(COLUMN_DATE)) {
				return alarm.getEvent().getMismatchCreated();
			}
			if (key.equals(COLUMN_OPTICAL_DISTANCE)) {
				return Double.valueOf(alarm.getEvent().getMismatchOpticalDistance());
			}
			if (key.equals(COLUMN_PHYSICAL_DISTANCE)) {
				return Double.valueOf(alarm.getEvent().getMismatchPhysicalDistance());
			}
			if (key.equals(COLUMN_PATH)) {
				SchemePath path = alarm.getPath();
				return path != null ? path.getName() : "?";
			}
			if (key.equals(COLUMN_ELEMENT)) {
				PathElement pathElement = alarm.getPathElement();
				return pathElement != null ? pathElement.getName() : "?";
			}
		}
		return null;
	}

	@Override
	public void setValue(Alarm storableObject, String key, Object value) throws PropertyChangeException {
		// not editable
	}

	public boolean isEditable(String key) {
		return false;
	}
}
