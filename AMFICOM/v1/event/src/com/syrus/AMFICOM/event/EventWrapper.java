/*
 * $Id: EventWrapper.java,v 1.7 2005/04/01 09:00:59 bob Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/04/01 09:00:59 $
 * @author $Author: bob $
 * @module event_v1
 */
public class EventWrapper implements StorableObjectWrapper {

	public static final String LINK_COLUMN_EVENT_ID	= "event_id";

	public static final String LINK_FIELD_EVENT_PARAMETERS = "event_parameters";
	public static final String LINK_COLUMN_PARAMETER_VALUE = "value";

	public static final String LINK_FIELD_EVENT_SOURCES = "event_sources";
	public static final String LINK_COLUMN_SOURCE_ID	= "source_id";

	private static EventWrapper instance;

	private List keys;

	private EventWrapper() {
		//private constructor
		String[] keysArray = new String[] {COLUMN_TYPE_ID,
				COLUMN_DESCRIPTION,
				LINK_FIELD_EVENT_PARAMETERS,
				LINK_FIELD_EVENT_SOURCES};
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EventWrapper getInstance() {
		if (instance == null)
			instance = new EventWrapper();
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
		if (object instanceof Event) {
			Event event = (Event) object;
			if (key.equals(COLUMN_TYPE_ID))
				return event.getType();
			if (key.equals(COLUMN_DESCRIPTION))
				return event.getDescription();
			if (key.equals(LINK_FIELD_EVENT_PARAMETERS))
				return event.getParameters();
			if (key.equals(LINK_FIELD_EVENT_SOURCES))
				return event.getEventSourceIds();
		}
		return null;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Event) {
			Event event = (Event) object;
			if (key.equals(COLUMN_TYPE_ID))
				event.setType((EventType) value);
			else
				if (key.equals(COLUMN_DESCRIPTION))
					event.setDescription((String) value);
				else
					if (key.equals(LINK_FIELD_EVENT_PARAMETERS))
						event.setEventParameters((Set) value);
					else
						if (key.equals(LINK_FIELD_EVENT_SOURCES))
							event.setEventSourceIds((Set) value);
		}
	}

	public boolean isEditable(String key) {
		return false;
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_TYPE_ID))
			return EventType.class;
		if (key.equals(LINK_FIELD_EVENT_PARAMETERS))
			return Set.class;
		if (key.equals(LINK_FIELD_EVENT_SOURCES))
			return Set.class;
		return String.class;
	}

}
