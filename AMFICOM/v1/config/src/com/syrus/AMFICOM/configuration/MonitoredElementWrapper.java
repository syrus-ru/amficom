/*
 * $Id: MonitoredElementWrapper.java,v 1.2 2005/01/26 15:09:22 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/26 15:09:22 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class MonitoredElementWrapper implements Wrapper {
	public static final String COLUMN_MEASUREMENT_PORT_ID = "measurement_port_id";
	// sort NUMBER(2) NOT NULL,
	public static final String COLUMN_NAME = "name";

	public static final String COLUMN_SORT = "sort";

	public static final String COLUMN_LOCAL_ADDRESS = "local_address";

	private static MonitoredElementWrapper	instance;

	private List							keys;

	private MonitoredElementWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_NAME, COLUMN_MEASUREMENT_PORT_ID, COLUMN_SORT,
				COLUMN_LOCAL_ADDRESS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MonitoredElementWrapper getInstance() {
		if (instance == null)
			instance = new MonitoredElementWrapper();
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
		if (object instanceof MonitoredElement) {
			MonitoredElement me = (MonitoredElement) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return me.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return me.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return me.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return me.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return me.getModifierId().getIdentifierString();
			if (key.equals(COLUMN_NAME))
				return me.getName();
			if (key.equals(COLUMN_MEASUREMENT_PORT_ID))
				return me.getMeasurementPortId().getIdentifierString();
			if (key.equals(COLUMN_SORT))
				return Integer.toString(me.getSort().value());
			if (key.equals(COLUMN_LOCAL_ADDRESS))
				return me.getLocalAddress();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof MonitoredElement) {
			MonitoredElement me = (MonitoredElement) object;
			if (key.equals(COLUMN_NAME))
				me.setName((String) value);
			else if (key.equals(COLUMN_MEASUREMENT_PORT_ID))
				me.setMeasurementPortId(new Identifier((String) value));
			else if (key.equals(COLUMN_SORT))
				me.setSort(MonitoredElementSort.from_int(Integer.parseInt((String) value)));
			else if (key.equals(COLUMN_LOCAL_ADDRESS))
				me.setLocalAddress((String) value);
		}
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
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
