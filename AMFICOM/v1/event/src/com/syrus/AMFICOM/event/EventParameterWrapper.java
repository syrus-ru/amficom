/*
 * $Id: EventParameterWrapper.java,v 1.1 2005/02/28 15:31:36 arseniy Exp $
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

import com.syrus.AMFICOM.event.corba.EventParameter_TransferablePackage.EventParameterSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/28 15:31:36 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventParameterWrapper implements StorableObjectWrapper {
	public static final String COLUMN_SORT	= "sort";
	public static final String COLUMN_EVENT_ID	= "event_id";
	public static final String COLUMN_VALUE_NUMBER	= "value_number";
	public static final String COLUMN_VALUE_STRING	= "value_string";
	public static final String COLUMN_VALUE_RAW	= "value_raw";

	public static final String FIELD_VALUE = "value";

	private static EventParameterWrapper instance;

	private List keys;

	/**
	 * 
	 */
	private EventParameterWrapper() {
		//private constructor
		String[] keysArray = new String[] {COLUMN_TYPE_ID,
				COLUMN_EVENT_ID,
				COLUMN_SORT,
				FIELD_VALUE
		};
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static EventParameterWrapper getInstance() {
		if (instance == null)
			instance = new EventParameterWrapper();
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
		if (object instanceof EventParameter) {
			EventParameter eventParameter = (EventParameter) object;
			if (key.equals(COLUMN_TYPE_ID))
				return eventParameter.getType();
			if (key.equals(COLUMN_EVENT_ID))
				return eventParameter.getEventId();
			if (key.equals(COLUMN_SORT))
				return new Integer(eventParameter.getSort().value());
			if (key.equals(FIELD_VALUE)) {
				switch (eventParameter.getSort().value()) {
					case EventParameterSort._PARAMETER_SORT_NUMBER:
						return new Integer(eventParameter.getValueNumber());
					case EventParameterSort._PARAMETER_SORT_STRING:
						return eventParameter.getValueString();
					case EventParameterSort._PARAMETER_SORT_RAW:
						return eventParameter.getValueRaw();
				}
			}
		}
		return null;
	}

	public void setValue(Object object, final String key, final Object value) {
		//Not need to implement this method
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
			return ParameterType.class;
		if (key.equals(COLUMN_EVENT_ID))
			return Identifier.class;
		if (key.equals(COLUMN_SORT))
			return Integer.class;
		if (key.equals(FIELD_VALUE))
			return Object.class;
		return null;
	}

}
