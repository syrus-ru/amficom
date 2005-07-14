/*
 * $Id: EventTypeWrapper.java,v 1.12 2005/07/14 20:11:24 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.12 $, $Date: 2005/07/14 20:11:24 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventTypeWrapper extends StorableObjectWrapper {

	public static final String LINK_COLUMN_EVENT_TYPE_ID = "event_type_id";
	public static final String LINK_FIELD_PARAMETER_TYPES = "parameter_types";
	public static final String LINK_COLUMN_USER_ID = "user_id";
	public static final String LINK_COLUMN_ALERT_KIND = "alert_kind";

	private static EventTypeWrapper instance;

	private List keys;

	private EventTypeWrapper() {
		//	private constructor
		String[] keysArray = new String[] {COLUMN_CODENAME, COLUMN_DESCRIPTION, LINK_FIELD_PARAMETER_TYPES};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
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

	@Override
	public Object getValue(final Object object, final String key) {
		Object value = super.getValue(object, key);
		if (value == null && object instanceof EventType) {
			EventType eventType = (EventType) object;
			if (key.equals(COLUMN_CODENAME))
				return eventType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return eventType.getDescription();
			if (key.equals(LINK_FIELD_PARAMETER_TYPES))
				return eventType.getParameterTypeIds();
		}
		return value;
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
						eventType.setParameterTypeIds((Set) value);
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

	@Override
	public Class getPropertyClass(String key) {
		Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME)
				|| key.equals(COLUMN_DESCRIPTION))
			return String.class;
		if (key.equals(LINK_FIELD_PARAMETER_TYPES))
			return Set.class;
		
		return null;
	}

}
