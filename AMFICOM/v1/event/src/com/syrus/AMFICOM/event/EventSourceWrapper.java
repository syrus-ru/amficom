/*
 * $Id: EventSourceWrapper.java,v 1.2 2005/04/04 07:04:31 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/04 07:04:31 $
 * @author $Author: bob $
 * @module event_v1
 */
public class EventSourceWrapper implements StorableObjectWrapper {

	public static final String COLUMN_SOURCE_ENTITY_CODE = "source_entity_code";

	public static final String LINK_FIELD_SOURCE_ENTITY_ID = "source_entity_id";

	public static final String COLUMN_MCM_ID = "mcm_id";

	public static final String COLUMN_PORT_ID = "port_id";
	public static final String COLUMN_EQUIPMENT_ID = "equipment_id";
	public static final String COLUMN_TRANSMISSION_PATH_ID = "transmission_path_id";
	public static final String COLUMN_LINK_ID = "link_id";

	private static EventSourceWrapper instance;

	private List keys;

	private EventSourceWrapper() {
		//private constructor
		String[] keysArray = new String[] {LINK_FIELD_SOURCE_ENTITY_ID};
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EventSourceWrapper getInstance() {
		if (instance == null)
			instance = new EventSourceWrapper();
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
		if (object instanceof EventSource) {
			EventSource eventSource = (EventSource) object;
			if (key.equals(LINK_FIELD_SOURCE_ENTITY_ID))
				return eventSource.getSourceEntityId();
		}
		
		return null;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof EventSource) {
			EventSource eventSource = (EventSource) object;
			if (key.equals(LINK_FIELD_SOURCE_ENTITY_ID))
				eventSource.setSourceEntityId((Identifier) value);
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
		return String.class;
	}

}
