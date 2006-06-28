/*
 * $Id: EventWrapper.java,v 1.22 2006/03/15 16:47:02 bass Exp $
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
 * @version $Revision: 1.22 $, $Date: 2006/03/15 16:47:02 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventWrapper extends StorableObjectWrapper<Event> {

	public static final String LINK_COLUMN_EVENT_ID	= "event_id";

	public static final String LINK_FIELD_EVENT_PARAMETERS = "event_parameters";
	public static final String LINK_COLUMN_PARAMETER_VALUE = "value";

	public static final String LINK_FIELD_EVENT_SOURCES = "event_sources";
	public static final String LINK_COLUMN_SOURCE_ID	= "source_id";

	private static EventWrapper instance;

	private List<String> keys;

	private EventWrapper() {
		//private constructor
		final String[] keysArray = new String[] { COLUMN_TYPE_ID,
				COLUMN_DESCRIPTION,
				LINK_FIELD_EVENT_PARAMETERS,
				LINK_FIELD_EVENT_SOURCES };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EventWrapper getInstance() {
		if (instance == null) {
			instance = new EventWrapper();
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
	public Object getValue(final Event event, final String key) {
		final Object value = super.getValue(event, key);
		if (value == null && event != null) {
			if (key.equals(COLUMN_TYPE_ID))
				return event.getType();
			if (key.equals(COLUMN_DESCRIPTION))
				return event.getDescription();
			if (key.equals(LINK_FIELD_EVENT_PARAMETERS))
				return event.getParameters();
			if (key.equals(LINK_FIELD_EVENT_SOURCES))
				return event.getEventSourceIds();
		}
		return value;
	}

	@Override
	public void setValue(final Event event, final String key, final Object value) {
		if (event != null) {
			if (key.equals(COLUMN_TYPE_ID)) {
				event.setType((EventType) value);
			}
			else if (key.equals(COLUMN_DESCRIPTION)) {
				event.setDescription((String) value);
			}
		}
	}

	public boolean isEditable(final String key) {
		return false;
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
		if (key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		if (key.equals(COLUMN_TYPE_ID)) {
			return EventType.class;
		}
		if (key.equals(LINK_FIELD_EVENT_PARAMETERS)) {
			return Set.class;
		}
		if (key.equals(LINK_FIELD_EVENT_SOURCES)) {
			return Set.class;
		}
		return null;
	}

}
