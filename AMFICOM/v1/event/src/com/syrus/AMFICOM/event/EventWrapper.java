/*
 * $Id: EventWrapper.java,v 1.5 2005/02/28 15:31:47 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.event.corba.EventStatus;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/28 15:31:47 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventWrapper implements StorableObjectWrapper {

	public static final String COLUMN_STATUS = "status";

	public static final String LINK_FIELD_EVENT_PARAMETERS = "event_parameters";

	public static final String LINK_COLUMN_EVENT_ID	= "event_id";

	public static final String LINK_FIELD_EVENT_SOURCES = "event_sources";
	public static final String LINK_COLUMN_SOURCE_ID	= "source_id";

	private static EventWrapper instance;

	private List keys;

	private EventWrapper() {
		//private constructor
		String[] keysArray = new String[] {COLUMN_TYPE_ID,
				COLUMN_STATUS,
				COLUMN_DESCRIPTION,
				LINK_FIELD_EVENT_PARAMETERS,
				LINK_FIELD_EVENT_SOURCES};
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
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
			if (key.equals(COLUMN_STATUS))
				return new Integer(event.getStatus().value());
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
				if (key.equals(COLUMN_STATUS))
					event.setStatus(EventStatus.from_int(((Integer) value).intValue()));
				else
					if (key.equals(COLUMN_DESCRIPTION))
						event.setDescription((String) value);
					else
						if (key.equals(LINK_FIELD_EVENT_PARAMETERS)) {
							event.setEventParameters((Collection) value);
						}
						else
							if (key.equals(LINK_FIELD_EVENT_SOURCES))
								event.setEventSourceIds((Collection) value);
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
		if (key.equals(COLUMN_STATUS))
			return Integer.class;
		if (key.equals(LINK_FIELD_EVENT_PARAMETERS))
			return Map.class;
		if (key.equals(LINK_FIELD_EVENT_SOURCES))
			return List.class;
		return String.class;
	}

}
