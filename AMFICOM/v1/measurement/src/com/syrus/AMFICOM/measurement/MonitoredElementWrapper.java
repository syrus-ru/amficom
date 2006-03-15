/*
 * $Id: MonitoredElementWrapper.java,v 1.3.2.2 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.IdlMonitoredElementKind;

/**
 * @version $Revision: 1.3.2.2 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MonitoredElementWrapper extends StorableObjectWrapper<MonitoredElement> {

	public static final String COLUMN_MEASUREMENT_PORT_ID = "measurement_port_id";
	// sort NUMBER(2) NOT NULL,
	public static final String COLUMN_KIND = "kind";

	public static final String COLUMN_LOCAL_ADDRESS = "local_address";

	public static final String COLUMN_MONITORED_DOMAIN_MEMBER = "MonitoredDomainMember";

	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String LINK_COLUMN_EQUIPMENT_ID = "equipment_id";
	public static final String LINK_COLUMN_TRANSMISSION_PATH_ID = "transmission_path_id";

	private static MonitoredElementWrapper instance;

	private List<String> keys;

	private MonitoredElementWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME,
				COLUMN_MEASUREMENT_PORT_ID,
				COLUMN_KIND,
				COLUMN_LOCAL_ADDRESS,
				COLUMN_MONITORED_DOMAIN_MEMBER };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static MonitoredElementWrapper getInstance() {
		if (instance == null)
			instance = new MonitoredElementWrapper();
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
	public Object getValue(final MonitoredElement monitoredElement,
	                       final String key) {
		final Object value = super.getValue(monitoredElement, key);
		if (value == null && monitoredElement != null) {
			if (key.equals(COLUMN_NAME))
				return monitoredElement.getName();
			if (key.equals(COLUMN_MEASUREMENT_PORT_ID))
				return monitoredElement.getMeasurementPortId();
			if (key.equals(COLUMN_KIND))
				return new Integer(monitoredElement.getKind().value());
			if (key.equals(COLUMN_LOCAL_ADDRESS))
				return monitoredElement.getLocalAddress();
			if (key.equals(COLUMN_MONITORED_DOMAIN_MEMBER))
				return monitoredElement.getMonitoredDomainMemberIds();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final MonitoredElement monitoredElement, final String key, final Object value) {
		if (monitoredElement != null) {
			if (key.equals(COLUMN_NAME))
				monitoredElement.setName((String) value);
			else if (key.equals(COLUMN_MEASUREMENT_PORT_ID))
				monitoredElement.setMeasurementPortId((Identifier) value);
			else if (key.equals(COLUMN_KIND))
				monitoredElement.setKind(IdlMonitoredElementKind.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_LOCAL_ADDRESS))
				monitoredElement.setLocalAddress((String) value);
			if (key.equals(COLUMN_MONITORED_DOMAIN_MEMBER)) {
				final Set<Identifier> meDomainMemeberIds = new HashSet<Identifier>(((Set) value).size());
				for (final Iterator it = ((List) value).iterator(); it.hasNext();)
					meDomainMemeberIds.add((Identifier) it.next());
				monitoredElement.setMonitoredDomainMemberIds(meDomainMemeberIds);
			}
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_LOCAL_ADDRESS)) {
			return String.class;
		} else if (key.equals(COLUMN_MEASUREMENT_PORT_ID)) {
			return Identifier.class;
		} else if (key.equals(COLUMN_KIND)) {
			return Integer.class;
		} else if (key.equals(COLUMN_MONITORED_DOMAIN_MEMBER)) {
			return Set.class;
		}
		return null;
	}
}
