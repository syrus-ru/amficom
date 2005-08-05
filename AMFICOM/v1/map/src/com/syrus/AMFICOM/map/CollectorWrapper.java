/*
 * $Id: CollectorWrapper.java,v 1.10 2005/08/05 10:01:06 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.10 $, $Date: 2005/08/05 10:01:06 $
 * @author $Author: bob $
 * @module map_v1
 */
public class CollectorWrapper extends StorableObjectWrapper<Collector> {

	// name VARCHAR2(128),
	// description VARCHAR2(256),

	// collector_id VARCHAR2(32),
	public static final String LINK_COLUMN_COLLECTOR_ID = "collector_id";
	// physical_link_id VARCHAR2(32),
	public static final String LINK_COLUMN_PHYSICAL_LINK_ID = "physical_link_id";

	protected static CollectorWrapper instance;

	protected List<String> keys;

	private CollectorWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, LINK_COLUMN_PHYSICAL_LINK_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static CollectorWrapper getInstance() {
		if (instance == null)
			instance = new CollectorWrapper();
		return instance;
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason rename it */
		return key;
	}

	@Override
	public Class getPropertyClass(final String key) {
		if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
			return List.class;
		return String.class;
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	@Override
	public Object getValue(final Collector collector, final String key) {
		if (collector != null) {
			if (key.equals(COLUMN_NAME))
				return collector.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return collector.getDescription();
			else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
				return collector.getPhysicalLinks();

		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	public void setValue(final Collector collector, final String key, final Object value) {
		if (collector != null) {
			if (key.equals(COLUMN_NAME))
				collector.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				collector.setDescription((String) value);
			else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
				collector.setPhysicalLinks((Set) value);
		}
	}

}
