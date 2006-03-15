/*
 * $Id: EventTypeWrapper.java,v 1.24 2006/03/15 15:47:20 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.24 $, $Date: 2006/03/15 15:47:20 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventTypeWrapper extends StorableObjectWrapper<EventType> {

	public static final String LINK_COLUMN_EVENT_TYPE_ID = "event_type_id";
	public static final String LINK_COLUMN_USER_ID = "user_id";
	public static final String LINK_COLUMN_ALERT_KIND = "alert_kind";

	private static EventTypeWrapper instance;

	private List<String> keys;

	private EventTypeWrapper() {
		//	private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, LINK_COLUMN_PARAMETER_TYPE_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EventTypeWrapper getInstance() {
		if (instance == null) {
			instance = new EventTypeWrapper();
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
	public Object getValue(final EventType eventType, final String key) {
		final Object value = super.getValue(eventType, key);
		if (value == null && eventType != null) {
			if (key.equals(COLUMN_CODENAME)) {
				return eventType.getCodename();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return eventType.getDescription();
			}
			if (key.equals(LINK_COLUMN_PARAMETER_TYPE_ID)) {
				return eventType.getParameterTypeIds();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final EventType eventType, final String key, final Object value) {
		if (eventType != null) {
			if (key.equals(COLUMN_CODENAME)) {
				eventType.setCodename((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				eventType.setDescription((String) value);
			}
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
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME) || key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		if (key.equals(LINK_COLUMN_PARAMETER_TYPE_ID)) {
			return Set.class;
		}
		return null;
	}

}
