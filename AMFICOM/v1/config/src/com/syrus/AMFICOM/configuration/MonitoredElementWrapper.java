/*
 * $Id: MonitoredElementWrapper.java,v 1.7 2005/04/01 07:57:28 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.7 $, $Date: 2005/04/01 07:57:28 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class MonitoredElementWrapper implements StorableObjectWrapper {

	public static final String				COLUMN_MEASUREMENT_PORT_ID			= "measurement_port_id";
	// sort NUMBER(2) NOT NULL,
	public static final String				COLUMN_SORT							= "sort";

	public static final String				COLUMN_LOCAL_ADDRESS				= "local_address";

	public static final String				COLUMN_MONITORED_DOMAIN_MEMBER		= "MonitoredDomainMember";

	public static final String				LINK_COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String				LINK_COLUMN_EQUIPMENT_ID			= "equipment_id";
	public static final String				LINK_COLUMN_TRANSMISSION_PATH_ID	= "transmission_path_id";

	private static MonitoredElementWrapper	instance;

	private List							keys;

	private MonitoredElementWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME, COLUMN_MEASUREMENT_PORT_ID, COLUMN_SORT, COLUMN_LOCAL_ADDRESS,
				COLUMN_MONITORED_DOMAIN_MEMBER};

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
			if (key.equals(COLUMN_NAME))
				return me.getName();
			if (key.equals(COLUMN_MEASUREMENT_PORT_ID))
				return me.getMeasurementPortId();
			if (key.equals(COLUMN_SORT))
				return new Integer(me.getSort().value());
			if (key.equals(COLUMN_LOCAL_ADDRESS))
				return me.getLocalAddress();
			if (key.equals(COLUMN_MONITORED_DOMAIN_MEMBER))
				return me.getMonitoredDomainMemberIds();
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
			if (key.equals(COLUMN_MONITORED_DOMAIN_MEMBER)) {
				Set meDomainMemeberIds = new HashSet(((Set) value).size());
				for (Iterator it = ((List) value).iterator(); it.hasNext();)
					meDomainMemeberIds.add(new Identifier((String) it.next()));
				me.setMonitoredDomainMemberIds(meDomainMemeberIds);
			}
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
		if (key.equals(COLUMN_MONITORED_DOMAIN_MEMBER))
			return Set.class;
		return String.class;
	}
}
