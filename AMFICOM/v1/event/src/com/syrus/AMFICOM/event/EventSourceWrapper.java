/*
 * $Id: EventSourceWrapper.java,v 1.12 2005/10/25 19:53:15 bass Exp $
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
 * @version $Revision: 1.12 $, $Date: 2005/10/25 19:53:15 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventSourceWrapper extends StorableObjectWrapper<EventSource> {

	public static final String COLUMN_SOURCE_ENTITY_CODE = "source_entity_code";

	public static final String LINK_FIELD_SOURCE_ENTITY_ID = "source_entity_id";

	public static final String COLUMN_MCM_ID = "mcm_id";

	public static final String COLUMN_PORT_ID = "port_id";
	public static final String COLUMN_EQUIPMENT_ID = "equipment_id";
	public static final String COLUMN_TRANSMISSION_PATH_ID = "transmission_path_id";
	public static final String COLUMN_LINK_ID = "link_id";

	private static EventSourceWrapper instance;

	private List<String> keys;

	private EventSourceWrapper() {
		//private constructor
		final String[] keysArray = new String[] { LINK_FIELD_SOURCE_ENTITY_ID };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static EventSourceWrapper getInstance() {
		if (instance == null)
			instance = new EventSourceWrapper();
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
	public Object getValue(final EventSource eventSource, final String key) {
		final Object value = super.getValue(eventSource, key);
		if (value == null && eventSource != null) {
			if (key.equals(LINK_FIELD_SOURCE_ENTITY_ID))
				return eventSource.getSourceEntityId();
		}

		return value;
	}

	@Override
	public void setValue(final EventSource eventSource, final String key, final Object value) {
		if (eventSource != null) {
			if (key.equals(LINK_FIELD_SOURCE_ENTITY_ID)) {
				eventSource.setSourceEntityId((Identifier) value);
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
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(LINK_FIELD_SOURCE_ENTITY_ID))
			return Identifier.class;
		return null;
	}

}
