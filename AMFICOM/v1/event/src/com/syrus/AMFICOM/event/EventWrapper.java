/*
 * $Id: EventWrapper.java,v 1.1 2005/01/31 13:15:50 arseniy Exp $
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

import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/31 13:15:50 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventWrapper implements Wrapper {

	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_DESCRIPTION = "description";

	private static EventWrapper instance;

	private List keys;

	private EventWrapper() {
		//private constructor
		String[] keysArray = new String[] {StorableObjectDatabase.COLUMN_ID,
				StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID,
				StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID,
				COLUMN_TYPE_ID,
				COLUMN_STATUS,
				COLUMN_DESCRIPTION};
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static EventWrapper getInstance() {
		if (instance == null)
			instance = new EventWrapper();
		return instance;
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public List getKeys() {
		//TODO implement
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		//TODO implement
		return key;
	}

	public Class getPropertyClass(String key) {
		//TODO implement
		return String.class;
	}

	public Object getPropertyValue(String key) {
		//TODO implement
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		//TODO implement
	}

	public Object getValue(final Object object, final String key) {
		if (object instanceof Event) {
			Event event = (Event) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return event.getId();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return Long.toString(event.getCreated().getTime());
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return event.getCreatorId();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return Long.toString(event.getModified().getTime());
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return event.getModifierId();
			if (key.equals(COLUMN_TYPE_ID))
				return event.getType();
			if (key.equals(COLUMN_STATUS))
				return Integer.toString(event.getStatus().value());
			if (key.equals(COLUMN_DESCRIPTION))
				return event.getDescription();
		}
		return null;
	}

	public boolean isEditable(String key) {
		//TODO implement
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		//TODO implement
	}

}
