/*-
 * $Id: AlarmWrapper.java,v 1.4 2006/06/06 13:19:44 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer.ui;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.alarm.Alarm;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.PropertyChangeException;
import com.syrus.util.Wrapper;

public class AlarmWrapper implements Wrapper<Alarm> {
	public static final String COLUMN_MEASURE_COUNT = "measure_count";
	public static final String COLUMN_DATE_STARTED = "date_started";
	public static final String COLUMN_DATE_FINISHED = "date_finished";
	public static final String COLUMN_PATHELEMENT_NAME = "path_element_name";
	public static final String COLUMN_PATH_NAME = "path_name";
	public static final String COLUMN_MISMATCH_OPTICAL_DISTANCE = "mismatch_optical_distance";
	public static final String COLUMN_MISMATCH_PHYSICAL_DISTANCE = "mismatch_physical_distance";
	public static final String COLOR = "color";
		
	private final List<String> keys;
	
	private static AlarmWrapper instance;
	private static final String WRAPPER_KEY = "Wrapper.Keys.";
	
	public static AlarmWrapper getInstance() {
		return instance == null
				? instance = new AlarmWrapper()
				: instance;
	}
	
	private AlarmWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(
				COLUMN_MEASURE_COUNT,
				COLUMN_DATE_STARTED,
				COLUMN_DATE_FINISHED,
				COLUMN_PATHELEMENT_NAME, 
				COLUMN_PATH_NAME,
				COLUMN_MISMATCH_OPTICAL_DISTANCE,
				COLUMN_MISMATCH_PHYSICAL_DISTANCE));
	}
	
	public List<String> getKeys() {
		return this.keys;		
	}

	public String getName(final String key) {
		final String internedKey = key.intern();
		if (internedKey == COLOR) {
			return " ";
		}
		return I18N.getString(WRAPPER_KEY + key);
	}

	public Class< ? > getPropertyClass(final String key) {
		final String internedKey = key.intern();
		if (internedKey == COLUMN_MEASURE_COUNT) {
			return Integer.class;
		}
		if (internedKey == COLUMN_DATE_STARTED 
				|| internedKey == COLUMN_DATE_FINISHED) {
			return Date.class;
		}
		if (internedKey == COLUMN_PATHELEMENT_NAME
				|| internedKey == COLUMN_PATH_NAME) {
			return String.class;
		}
		if (internedKey == COLUMN_MISMATCH_OPTICAL_DISTANCE
				|| internedKey == COLUMN_MISMATCH_PHYSICAL_DISTANCE) {
			return Double.class;
		}
		if (internedKey == COLOR) {
			return Color.class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// empty
	}

	public Object getValue(final Alarm alarm, final String key) {
		if (alarm == null) {
			return null;
		}
		
		final String internedKey = key.intern();
		if (internedKey == COLUMN_MEASURE_COUNT) {
			return Integer.valueOf(alarm.getEventsCount());
		}
		if (internedKey == COLUMN_DATE_STARTED) {
			return alarm.getStartDate();
		}
		if (internedKey == COLUMN_DATE_FINISHED) {
			return alarm.getEndDate();
		}
		if (internedKey == COLUMN_PATHELEMENT_NAME) {
			return alarm.getPathElement().getName();
		} 
		if (internedKey == COLUMN_PATH_NAME) {
			return alarm.getSchemePath().getName();
		}
		if (internedKey == COLUMN_MISMATCH_OPTICAL_DISTANCE) {
			return Double.valueOf(alarm.getMismatchOpticalDistance());
		} 
		if (internedKey == COLUMN_MISMATCH_PHYSICAL_DISTANCE) {
			return Double.valueOf(alarm.getMismatchPhysicalDistance());
		}
		if (internedKey == COLOR) {
			AlarmStatus state = alarm.getState();
			
			if (state == AlarmStatus.PENDING) {
					if (alarm.getSeverity() == Severity.SEVERITY_HARD) {
						return Color.RED;
					}
					if (alarm.getSeverity() == Severity.SEVERITY_SOFT) {
						return Color.YELLOW;
					}
					return Color.WHITE;
				}
				if (state == AlarmStatus.ACKNOWLEDGED 
						|| state == AlarmStatus.IN_PROGRESS
						|| state == AlarmStatus.XTRA_ASSIGNED
						|| state == AlarmStatus.XTRA_VTEST_IN_PROGRESS) {
					return Color.ORANGE;
				}
				if (state == AlarmStatus.IGNORED
						|| state == AlarmStatus.TIMED_OUT 
						|| state == AlarmStatus.ABANDONED) {
					return Color.GRAY;
				}
				if (state == AlarmStatus.XTRA_TT_COMPLETED) {
					return Color.CYAN;
				}
				if (state == AlarmStatus.RESOLVED
						|| state == AlarmStatus.XTRA_VERIFIED
						|| state == AlarmStatus.XTRA_CLOSED) {
					return Color.GREEN; 
				}
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Alarm object, String key, Object value) throws PropertyChangeException {
		// empty
	}
}
