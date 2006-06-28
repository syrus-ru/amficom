/*-
 * $Id: ReflectogramMismatchEventWrapper.java,v 1.3 2006/03/30 12:10:05 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.PropertyChangeException;
import com.syrus.util.Wrapper;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2006/03/30 12:10:05 $
 * @module event
 */
public final class ReflectogramMismatchEventWrapper
		implements Wrapper<ReflectogramMismatchEvent> {
	public static final String KEY_TYPE = "type";
	public static final String COLUMN_ALARM_TYPE = "alarm_type";
	public static final String COLUMN_SEVERITY = "severity";
	public static final String COLUMN_COORD = "coord";
	public static final String COLUMN_END_COORD = "end_coord";
	public static final String COLUMN_DELTA_X = "delta_x";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_MISMATCH = "mismatch";
	public static final String COLUMN_MIN_MISMATCH = "min_mismatch";
	public static final String COLUMN_MAX_MISMATCH = "max_mismatch";
	public static final String KEY_ANCHORS = "anchors";
	public static final String COLUMN_ANCHOR1_ID = "anchor1_id";
	public static final String COLUMN_ANCHOR2_ID = "anchor2_id";
	public static final String COLUMN_ANCHOR1_COORD = "anchor1_coord";
	public static final String COLUMN_ANCHOR2_COORD = "anchor2_coord";
	public static final String COLUMN_MEASUREMENT_ID = "measurement_id";

	private final List<String> keys;

	private static ReflectogramMismatchEventWrapper instance;

	private ReflectogramMismatchEventWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(
				KEY_TYPE,
				COLUMN_ALARM_TYPE,
				COLUMN_SEVERITY,
				COLUMN_COORD,
				COLUMN_END_COORD,
				COLUMN_DELTA_X,
				KEY_DISTANCE,
				KEY_MISMATCH,
				COLUMN_MIN_MISMATCH,
				COLUMN_MAX_MISMATCH,
				KEY_ANCHORS,
				COLUMN_ANCHOR1_ID,
				COLUMN_ANCHOR2_ID,
				COLUMN_ANCHOR1_COORD,
				COLUMN_ANCHOR2_COORD,
				COLUMN_MEASUREMENT_ID));
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
	 * @todo localize me
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
		} else if (internedKey == COLUMN_ALARM_TYPE) {
			return AlarmType.class;
		} else if (internedKey == COLUMN_SEVERITY) {
			return Severity.class;
		} else if (internedKey == COLUMN_COORD
				|| internedKey == COLUMN_END_COORD
				|| internedKey == COLUMN_ANCHOR1_COORD
				|| internedKey == COLUMN_ANCHOR2_COORD) {
			return Integer.class;
		} else if (internedKey == COLUMN_DELTA_X
				|| internedKey == KEY_DISTANCE
				|| internedKey == COLUMN_MIN_MISMATCH
				|| internedKey == COLUMN_MAX_MISMATCH) {
			return Double.class;
		} else if (internedKey == KEY_MISMATCH
				|| internedKey == KEY_ANCHORS) {
			return Boolean.class;
		} else if (internedKey == COLUMN_ANCHOR1_ID
				|| internedKey == COLUMN_ANCHOR2_ID
				|| internedKey == COLUMN_MEASUREMENT_ID) {
			return Identifier.class;
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
	 * @param reflectogramMismatchEvent
	 * @param key
	 * @see com.syrus.util.Wrapper#getValue(Object, String)
	 */
	public Object getValue(final ReflectogramMismatchEvent reflectogramMismatchEvent,
			final String key) {
		if (reflectogramMismatchEvent == null) {
			return null;
		}

		final String internedKey = key.intern();
		if (internedKey == KEY_TYPE) {
			return reflectogramMismatchEvent.getType();
		} else if (internedKey == COLUMN_ALARM_TYPE) {
			return reflectogramMismatchEvent.getAlarmType();
		} else if (internedKey == COLUMN_SEVERITY) {
			return reflectogramMismatchEvent.getSeverity();
		} else if (internedKey == COLUMN_COORD) {
			return Integer.valueOf(reflectogramMismatchEvent.getCoord());
		} else if (internedKey == COLUMN_END_COORD) {
			return Integer.valueOf(reflectogramMismatchEvent.getEndCoord());
		} else if (internedKey == COLUMN_DELTA_X) {
			return Double.valueOf(reflectogramMismatchEvent.getDeltaX());
		} else if (internedKey == KEY_DISTANCE) {
			return Double.valueOf(reflectogramMismatchEvent.getDistance());
		} else if (internedKey == KEY_MISMATCH) {
			return Boolean.valueOf(reflectogramMismatchEvent.hasMismatch());
		} else if (internedKey == COLUMN_MIN_MISMATCH) {
			return Double.valueOf(reflectogramMismatchEvent.getMinMismatch());
		} else if (internedKey == COLUMN_MAX_MISMATCH) {
			return Double.valueOf(reflectogramMismatchEvent.getMaxMismatch());
		} else if (internedKey == KEY_ANCHORS) {
			return Boolean.valueOf(reflectogramMismatchEvent.hasAnchors());
		} else if (internedKey == COLUMN_ANCHOR1_ID) {
			return Identifier.valueOf(reflectogramMismatchEvent.getAnchor1Id().getValue());
		} else if (internedKey == COLUMN_ANCHOR2_ID) {
			return Identifier.valueOf(reflectogramMismatchEvent.getAnchor2Id().getValue());
		} else if (internedKey == COLUMN_ANCHOR1_COORD) {
			return Integer.valueOf(reflectogramMismatchEvent.getAnchor1Coord());
		} else if (internedKey == COLUMN_ANCHOR2_COORD) {
			return Integer.valueOf(reflectogramMismatchEvent.getAnchor2Coord());
		} else if (internedKey == COLUMN_MEASUREMENT_ID) {
			return reflectogramMismatchEvent.getMeasurementId();
		}
		return null;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#isEditable(String)
	 */
	public boolean isEditable(final String key) {
		return false;
	}

	/**
	 * @param reflectogramMismatchEvent
	 * @param key
	 * @param value
	 * @throws PropertyChangeException
	 * @see com.syrus.util.Wrapper#setValue(Object, String, Object)
	 */
	public void setValue(final ReflectogramMismatchEvent reflectogramMismatchEvent,
			final String key,
			final Object value)
	throws PropertyChangeException {
		throw new UnsupportedOperationException("Unable to assign value: ``"
				+ value + "''; field: ``" + key + "'' is read-only.");
	}

	public static ReflectogramMismatchEventWrapper getInstance() {
		return instance == null
				? instance = new ReflectogramMismatchEventWrapper()
				: instance;
	}
}
