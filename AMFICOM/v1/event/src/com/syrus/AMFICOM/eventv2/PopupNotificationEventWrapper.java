/*-
 * $Id: PopupNotificationEventWrapper.java,v 1.2 2005/11/17 16:20:30 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.PropertyChangeException;
import com.syrus.util.Wrapper;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/11/17 16:20:30 $
 * @module event
 */
public final class PopupNotificationEventWrapper
		implements Wrapper<PopupNotificationEvent> {
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_DELIVERY_METHOD = "delivery_method";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_TARGET_USER_ID = "target_user_id";
	public static final String COLUMN_RESULT_ID = "result_id";
	public static final String COLUMN_MISMATCH_OPTICAL_DISTANCE = "mismatch_optical_distance";
	public static final String COLUMN_MISMATCH_PHYSICAL_DISTANCE = "mismatch_physical_distance";
	public static final String COLUMN_MISMATCH_CREATED = "mismatch_created";
	public static final String COLUMN_SEVERITY = "severity";

	private static PopupNotificationEventWrapper instance;

	private final List<String> keys;

	private PopupNotificationEventWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String []{
				COLUMN_TYPE,
				COLUMN_DELIVERY_METHOD,
				COLUMN_MESSAGE,
				COLUMN_TARGET_USER_ID,
				COLUMN_RESULT_ID,
				COLUMN_MISMATCH_OPTICAL_DISTANCE,
				COLUMN_MISMATCH_PHYSICAL_DISTANCE,
				COLUMN_MISMATCH_CREATED,
				COLUMN_SEVERITY}));
	}

	/**
	 * @see Wrapper#getKeys()
	 */
	public List<String> getKeys() {
		return this.keys;
	}

	/**
	 * @param key
	 * @see Wrapper#getName(String)
	 */
	public String getName(final String key) {
		return key;
	}

	/**
	 * @param key
	 * @see Wrapper#getPropertyClass(String)
	 */
	public Class<?> getPropertyClass(final String key) {
		final String internedKey = key.intern();
		if (internedKey == COLUMN_TYPE) {
			return EventType.class;
		} else if (internedKey == COLUMN_DELIVERY_METHOD) {
			return DeliveryMethod.class;
		} else if (internedKey == COLUMN_MESSAGE) {
			return String.class;
		} else if (internedKey == COLUMN_TARGET_USER_ID
				|| internedKey == COLUMN_RESULT_ID) {
			return Identifier.class;
		} else if (internedKey == COLUMN_MISMATCH_OPTICAL_DISTANCE
				|| internedKey == COLUMN_MISMATCH_PHYSICAL_DISTANCE) {
			return Double.class;
		} else if (internedKey == COLUMN_MISMATCH_CREATED) {
			return Date.class;
		} else if (internedKey == COLUMN_SEVERITY) {
			return Severity.class;
		}
		return null;
	}

	/**
	 * @param key
	 * @see Wrapper#getPropertyValue(String)
	 */
	public Object getPropertyValue(final String key) {
		return null;
	}

	/**
	 * @param key
	 * @param objectKey
	 * @param objectValue
	 * @see Wrapper#setPropertyValue(String, Object, Object)
	 */
	public void setPropertyValue(final String key, final Object objectKey,
			final Object objectValue) {
		// empty
	}

	/**
	 * @param popupNotificationEvent
	 * @param key
	 * @see Wrapper#getValue(Object, String)
	 */
	public Object getValue(final PopupNotificationEvent popupNotificationEvent,
			final String key) {
		if (popupNotificationEvent == null) {
			return null;
		}

		final String internedKey = key.intern();
		if (internedKey == COLUMN_TYPE) {
			return popupNotificationEvent.getType();
		} else if (internedKey == COLUMN_DELIVERY_METHOD) {
			return popupNotificationEvent.getDeliveryMethod();
		} else if (internedKey == COLUMN_MESSAGE) {
			return popupNotificationEvent.getMessage();
		} else if (internedKey == COLUMN_TARGET_USER_ID) {
			return popupNotificationEvent.getTargetUserId();
		} else if (internedKey == COLUMN_RESULT_ID) {
			return popupNotificationEvent.getResultId();
		} else if (internedKey == COLUMN_MISMATCH_OPTICAL_DISTANCE) {
			return Double.valueOf(popupNotificationEvent.getMismatchOpticalDistance());
		} else if (internedKey == COLUMN_MISMATCH_PHYSICAL_DISTANCE) {
			return Double.valueOf(popupNotificationEvent.getMismatchPhysicalDistance());
		} else if (internedKey == COLUMN_MISMATCH_CREATED) {
			return popupNotificationEvent.getMismatchCreated();
		} else if (internedKey == COLUMN_SEVERITY) {
			return popupNotificationEvent.getSeverity();
		}
		return null;
	}

	/**
	 * @param key
	 * @see Wrapper#isEditable(String)
	 */
	public boolean isEditable(final String key) {
		return false;
	}

	/**
	 * @param object
	 * @param key
	 * @param value
	 * @throws PropertyChangeException
	 * @see Wrapper#setValue(Object, String, Object)
	 */
	public void setValue(final PopupNotificationEvent object,
			final String key,
			final Object value)
	throws PropertyChangeException {
		// empty
	}

	public static PopupNotificationEventWrapper getInstance() {
		return (instance == null)
				? instance = new PopupNotificationEventWrapper()
				: instance;
	}
}
