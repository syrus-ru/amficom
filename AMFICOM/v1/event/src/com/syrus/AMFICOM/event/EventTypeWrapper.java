/*
 * $Id: EventTypeWrapper.java,v 1.4 2005/02/07 11:58:10 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/07 11:58:10 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventTypeWrapper implements StorableObjectWrapper {

	public static final String LINK_FIELD_PARAMETER_TYPES = "parameter_types";

	private static EventTypeWrapper instance;

	private List keys;

	private EventTypeWrapper() {
		//	private constructor
		String[] keysArray = new String[] {COLUMN_CODENAME, COLUMN_DESCRIPTION, LINK_FIELD_PARAMETER_TYPES};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static EventTypeWrapper getInstance() {
		if (instance == null)
			instance = new EventTypeWrapper();
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
		if (object instanceof EventType) {
			EventType eventType = (EventType) object;
			if (key.equals(COLUMN_CODENAME))
				return eventType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return eventType.getDescription();
			if (key.equals(LINK_FIELD_PARAMETER_TYPES))
				return eventType.getParameterTypes();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof EventType) {
			EventType eventType = (EventType) object;
			if (key.equals(COLUMN_CODENAME))
				eventType.setCodename((String) value);
			else
				if (key.equals(COLUMN_DESCRIPTION))
					eventType.setDescription((String) value);
				else
					if (key.equals(LINK_FIELD_PARAMETER_TYPES))
						eventType.setParameterTypes((List) value);
		}
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
		if (key.equals(LINK_FIELD_PARAMETER_TYPES))
			return List.class;
		return String.class;
	}

}
