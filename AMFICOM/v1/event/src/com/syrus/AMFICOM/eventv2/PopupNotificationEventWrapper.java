/*-
 * $Id: PopupNotificationEventWrapper.java,v 1.2.4.1 2006/03/23 10:48:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.PropertyChangeException;
import com.syrus.util.Wrapper;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2.4.1 $, $Date: 2006/03/23 10:48:43 $
 * @module event
 */
public final class PopupNotificationEventWrapper
		implements Wrapper<PopupNotificationEvent> {
	public static final String KEY_TYPE = "type";
	public static final String KEY_DELIVERY_METHOD = "delivery_method";
	public static final String COLUMN_TARGET_USER_ID = "target_user_id";
	public static final String COLUMN_LINE_MISMATCH_EVENT_ID = "reflectogram_mismatch_event_id";

	private static PopupNotificationEventWrapper instance;

	private final List<String> keys;

	private PopupNotificationEventWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(
				KEY_TYPE,
				KEY_DELIVERY_METHOD,
				COLUMN_TARGET_USER_ID,
				COLUMN_LINE_MISMATCH_EVENT_ID));
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
		if (internedKey == KEY_TYPE) {
			return EventType.class;
		} else if (internedKey == KEY_DELIVERY_METHOD) {
			return DeliveryMethod.class;
		} else if (internedKey == COLUMN_TARGET_USER_ID
				|| internedKey == COLUMN_LINE_MISMATCH_EVENT_ID) {
			return Identifier.class;
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
		if (internedKey == KEY_TYPE) {
			return popupNotificationEvent.getType();
		} else if (internedKey == KEY_DELIVERY_METHOD) {
			return popupNotificationEvent.getDeliveryMethod();
		} else if (internedKey == COLUMN_TARGET_USER_ID) {
			return popupNotificationEvent.getTargetUserId();
		} else if (internedKey == COLUMN_LINE_MISMATCH_EVENT_ID) {
			return popupNotificationEvent.getLineMismatchEventId();
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
