/*
 * $Id: CollectorWrapper.java,v 1.5 2005/02/03 08:38:02 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/03 08:38:02 $
 * @author $Author: bob $
 * @module map_v1
 */
public class CollectorWrapper implements StorableObjectWrapper {

	// name VARCHAR2(128),
	// description VARCHAR2(256),

	// collector_id VARCHAR2(32),
	public static final String			LINK_COLUMN_COLLECTOR_ID		= "collector_id";
	// physical_link_id VARCHAR2(32),
	public static final String			LINK_COLUMN_PHYSICAL_LINK_ID	= "physical_link_id";

	protected static CollectorWrapper	instance;

	protected List						keys;

	private CollectorWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_CHARACTERISTICS,
				LINK_COLUMN_PHYSICAL_LINK_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

	}

	public static CollectorWrapper getInstance() {
		if (instance == null)
			instance = new CollectorWrapper();
		return instance;
	}

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		/* there is no reason rename it */
		return key;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_CHARACTERISTICS) || key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
			return List.class;
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public Object getValue(Object object, String key) {
		if (object instanceof Collector) {
			Collector collector = (Collector) object;
			if (key.equals(COLUMN_NAME))
				return collector.getName();
			else if (key.equals(COLUMN_DESCRIPTION))
				return collector.getDescription();
			else if (key.equals(COLUMN_CHARACTERISTICS))
				return collector.getCharacteristics();
			else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
				return collector.getPhysicalLinks();

		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof Collector) {
			Collector collector = (Collector) object;
			if (key.equals(COLUMN_NAME))
				collector.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				collector.setDescription((String) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				collector.setCharacteristics((List) value);
			else if (key.equals(LINK_COLUMN_PHYSICAL_LINK_ID))
				collector.setPhysicalLinks((List) value);
		}
	}

}
