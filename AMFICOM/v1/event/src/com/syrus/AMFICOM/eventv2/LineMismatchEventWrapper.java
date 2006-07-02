/*-
 * $Id: LineMismatchEventWrapper.java,v 1.7 2006/07/02 18:45:42 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StringToValueConverter;
import com.syrus.util.PropertyChangeException;
import com.syrus.util.PropertyQueryException;
import com.syrus.util.Wrapper;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2006/07/02 18:45:42 $
 * @module event
 */
public final class LineMismatchEventWrapper
		implements Wrapper<LineMismatchEvent>, StringToValueConverter {
	public static final String KEY_TYPE = "type";
	public static final String COLUMN_AFFECTED_PATH_ELEMENT_ID = "affected_path_element_id";
	public static final String KEY_AFFECTED_PATH_ELEMENT_SPACIOUS = "affected_path_element_spacious";
	public static final String COLUMN_PHYSICAL_DISTANCE_TO_START = "physical_distance_to_start";
	public static final String COLUMN_PHYSICAL_DISTANCE_TO_END = "physical_distance_to_end";
	public static final String COLUMN_MISMATCH_OPTICAL_DISTANCE = "mismatch_optical_distance";
	public static final String COLUMN_MISMATCH_PHYSICAL_DISTANCE = "mismatch_physical_distance";
	public static final String COLUMN_PLAIN_TEXT_MESSAGE = "plain_text_message";
	public static final String COLUMN_RICH_TEXT_MESSAGE = "rich_text_message";
	public static final String COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID = "reflectogram_mismatch_event_id";
	public static final String COLUMN_REFLECTOGRAM_MISMATCH_EVENT = "reflectogram_mismatch_event";
	public static final String COLUMN_ALARM_STATUS = "alarm_status";
	public static final String COLUMN_ALARM_STATUS_INT = "alarm_status_int";
	public static final String COLUMN_PARENT_LINE_MISMATCH_EVENT_ID = "parent_line_mismatch_event_id";
	public static final String COLUMN_PARENT_LINE_MISMATCH_EVENT = "parent_line_mismatch_event";

	private final List<String> keys;

	private static LineMismatchEventWrapper instance;

	private LineMismatchEventWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(
				KEY_TYPE,
				COLUMN_AFFECTED_PATH_ELEMENT_ID,
				KEY_AFFECTED_PATH_ELEMENT_SPACIOUS,
				COLUMN_PHYSICAL_DISTANCE_TO_START,
				COLUMN_PHYSICAL_DISTANCE_TO_END,
				COLUMN_MISMATCH_OPTICAL_DISTANCE,
				COLUMN_MISMATCH_PHYSICAL_DISTANCE,
				COLUMN_PLAIN_TEXT_MESSAGE,
				COLUMN_RICH_TEXT_MESSAGE,
				COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID,
				COLUMN_REFLECTOGRAM_MISMATCH_EVENT,
				COLUMN_ALARM_STATUS,
				COLUMN_ALARM_STATUS_INT,
				COLUMN_PARENT_LINE_MISMATCH_EVENT_ID,
				COLUMN_PARENT_LINE_MISMATCH_EVENT));
	}

	/**
	 * @see com.syrus.util.Wrapper#getKeys()
	 */
	public List<String> getKeys() {
		return this.keys;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getName(String)
	 * @todo localize me.
	 */
	public String getName(final String key) {
		return key;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getPropertyClass(String)
	 */
	public Class<?> getPropertyClass(final String key) {
		final String internedKey = key.intern();
		if (internedKey == KEY_TYPE) {
			return EventType.class;
		} else if (internedKey == KEY_AFFECTED_PATH_ELEMENT_SPACIOUS) {
			return Boolean.class;
		} else if (internedKey == COLUMN_PHYSICAL_DISTANCE_TO_START
				|| internedKey == COLUMN_PHYSICAL_DISTANCE_TO_END
				|| internedKey == COLUMN_MISMATCH_OPTICAL_DISTANCE
				|| internedKey == COLUMN_MISMATCH_PHYSICAL_DISTANCE) {
			return Double.class;
		} else if (internedKey == COLUMN_AFFECTED_PATH_ELEMENT_ID
				|| internedKey == COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID
				|| internedKey == COLUMN_PARENT_LINE_MISMATCH_EVENT_ID) {
			return Identifier.class;
		} else if (internedKey == COLUMN_PLAIN_TEXT_MESSAGE
				|| internedKey == COLUMN_RICH_TEXT_MESSAGE) {
			return String.class;
		} else if (internedKey == COLUMN_REFLECTOGRAM_MISMATCH_EVENT) {
			return ReflectogramMismatchEvent.class;
		} else if (internedKey == COLUMN_ALARM_STATUS) {
			return AlarmStatus.class;
		} else if (internedKey == COLUMN_ALARM_STATUS_INT) {
			return Integer.class;
		} else if (internedKey == COLUMN_PARENT_LINE_MISMATCH_EVENT) {
			return LineMismatchEvent.class;
		}
		return null;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getPropertyValue(String)
	 */
	public Object getPropertyValue(final String key) {
		return null;
	}

	/**
	 * @param key
	 * @param objectKey
	 * @param objectValue
	 * @see com.syrus.util.Wrapper#setPropertyValue(String, Object, Object)
	 */
	public void setPropertyValue(final String key, final Object objectKey,
			final Object objectValue) {
		// empty
	}

	/**
	 * @param lineMismatchEvent
	 * @param key
	 * @throws PropertyQueryException
	 * @see com.syrus.util.Wrapper#getValue(Object, String)
	 */
	public Object getValue(final LineMismatchEvent lineMismatchEvent,
			final String key)
	throws PropertyQueryException {
		if (lineMismatchEvent == null) {
			return null;
		}

		final String internedKey = key.intern();

		try {
			if (internedKey == KEY_TYPE) {
				return lineMismatchEvent.getType();
			} else if (internedKey == COLUMN_AFFECTED_PATH_ELEMENT_ID) {
				return lineMismatchEvent.getAffectedPathElementId();
			} else if (internedKey == KEY_AFFECTED_PATH_ELEMENT_SPACIOUS) {
				return Boolean.valueOf(lineMismatchEvent.isAffectedPathElementSpacious());
			} else if (internedKey == COLUMN_PHYSICAL_DISTANCE_TO_START) {
				return Double.valueOf(lineMismatchEvent.getPhysicalDistanceToStart());
			} else if (internedKey == COLUMN_PHYSICAL_DISTANCE_TO_END) {
				return Double.valueOf(lineMismatchEvent.getPhysicalDistanceToEnd());
			} else if (internedKey == COLUMN_MISMATCH_OPTICAL_DISTANCE) {
				return Double.valueOf(lineMismatchEvent.getMismatchOpticalDistance());
			} else if (internedKey == COLUMN_MISMATCH_PHYSICAL_DISTANCE) {
				return Double.valueOf(lineMismatchEvent.getMismatchPhysicalDistance());
			} else if (internedKey == COLUMN_PLAIN_TEXT_MESSAGE) {
				return lineMismatchEvent.getPlainTextMessage();
			} else if (internedKey == COLUMN_RICH_TEXT_MESSAGE) {
				return lineMismatchEvent.getRichTextMessage();
			} else if (internedKey == COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID) {
				return lineMismatchEvent.getReflectogramMismatchEventId();
			} else if (internedKey == COLUMN_REFLECTOGRAM_MISMATCH_EVENT) {
				return lineMismatchEvent.getReflectogramMismatchEvent();
			} else if (internedKey == COLUMN_ALARM_STATUS) {
				return lineMismatchEvent.getAlarmStatus();
			} else if (internedKey == COLUMN_ALARM_STATUS_INT) {
				return Integer.valueOf(lineMismatchEvent.getAlarmStatus().ordinal());
			} else if (internedKey == COLUMN_PARENT_LINE_MISMATCH_EVENT_ID) {
				return lineMismatchEvent.getParentLineMismatchEventId();
			} else if (internedKey == COLUMN_PARENT_LINE_MISMATCH_EVENT) {
				return lineMismatchEvent.getParentLineMismatchEvent();
			}
			return null;
		} catch (final ApplicationException ae) {
			throw new PropertyQueryException(ae);
		}
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#isEditable(String)
	 */
	public boolean isEditable(final String key) {
		return false;
	}

	/**
	 * @param lineMismatchEvent
	 * @param key
	 * @param value
	 * @throws PropertyChangeException
	 * @see com.syrus.util.Wrapper#setValue(Object, String, Object)
	 */
	public void setValue(final LineMismatchEvent lineMismatchEvent,
			final String key,
			final Object value)
	throws PropertyChangeException {
		if (lineMismatchEvent == null) {
			return;
		}

		final String internedKey = key.intern();

		try {
			if (internedKey == COLUMN_ALARM_STATUS) {
				lineMismatchEvent.setAlarmStatus((AlarmStatus) value);
			} else if (internedKey == COLUMN_ALARM_STATUS_INT) {
				lineMismatchEvent.setAlarmStatus(AlarmStatus.valueOf(((Integer) value).intValue()));
			} else if (internedKey == COLUMN_PARENT_LINE_MISMATCH_EVENT_ID) {
				lineMismatchEvent.setParentLineMismatchEventId((Identifier) value);
			} else if (internedKey == COLUMN_PARENT_LINE_MISMATCH_EVENT) {
				lineMismatchEvent.setParentLineMismatchEvent((LineMismatchEvent) value);
			} else {
				throw new UnsupportedOperationException("Unable to assign value: ``"
						+ value + "''; field: ``" + internedKey + "'' is read-only.");
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	/**
	 * Current implementation only works for {@code Identifier}s, {@code
	 * AlarmStatus}es and {@code String}s.
	 *
	 * @see StringToValueConverter#valueToString(String, Object)
	 */
	public String valueToString(final String key, final Object value) {
		if (value == null) {
			return null;
		}

		final Class<?> clazz = this.getPropertyClass(key);
		if (clazz == Identifier.class) {
			return ((Identifier) value).getIdentifierString();
		} else if (clazz == AlarmStatus.class) {
			return ((AlarmStatus) value).name();
		} else if (clazz == String.class) {
			return (String) value;
		} else {
			return value.toString();
		}
	}

	/**
	 * Current implementation only works for {@code Identifier}s, {@code
	 * AlarmStatus}es and {@code String}s.
	 *
	 * @see StringToValueConverter#stringToValue(String, String)
	 */
	public Object stringToValue(final String key, final String stringValue) {
		if (stringValue == null) {
			return null;
		}

		final Class<?> clazz = this.getPropertyClass(key);
		if (clazz == Identifier.class) {
			return Identifier.valueOf(stringValue);
		} else if (clazz == AlarmStatus.class) {
			return AlarmStatus.valueOf(stringValue);
		} else if (clazz == String.class) {
			return stringValue;
		} else {
			try {
				return clazz.getMethod("valueOf", String.class).invoke(clazz, stringValue);
			} catch (final RuntimeException re) {
				throw re;
			} catch (final Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}
	}

	public static LineMismatchEventWrapper getInstance() {
		return instance == null
				? instance = new LineMismatchEventWrapper()
				: instance;
	}
}
