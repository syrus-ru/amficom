/*
 * $Id: KISWrapper.java,v 1.5 2005/02/03 08:37:00 bob Exp $
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

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/03 08:37:00 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class KISWrapper implements StorableObjectWrapper {

	// table :: kis
	// description VARCHAR2(256),
	// name VARCHAR2(64) NOT NULL,
	// hostname VARCHAR2(64),
	public static final String COLUMN_HOSTNAME  	= "hostname";
	// tcp_port NUMBER(5,0),
	public static final String COLUMN_TCP_PORT  		= "tcp_port";
	// equipment_id Identifier NOT NULL
	public static final String COLUMN_EQUIPMENT_ID 	= "equipment_id";
	// mcm_id Identifier NOT NULL
	public static final String COLUMN_MCM_ID 		= "mcm_id";
	
	public static final String	COLUMN_MEASUREMENT_PORT_IDS	= "measurementPortIds";

	private static KISWrapper	instance;

	private List				keys;
	
	private KISWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_EQUIPMENT_ID, COLUMN_MCM_ID, COLUMN_HOSTNAME, COLUMN_TCP_PORT, COLUMN_MEASUREMENT_PORT_IDS,
				COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static KISWrapper getInstance() {
		if (instance == null)
			instance = new KISWrapper();
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
		if (object instanceof KIS) {
			KIS kis = (KIS) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return kis.getDescription();
			if (key.equals(COLUMN_NAME))
				return kis.getName();
			if (key.equals(COLUMN_EQUIPMENT_ID))
				return kis.getEquipmentId();
			if (key.equals(COLUMN_MCM_ID))
				return kis.getMCMId();
			if (key.equals(COLUMN_HOSTNAME))
				return kis.getHostName();
			if (key.equals(COLUMN_TCP_PORT))
				return new Short(kis.getTCPPort());
			if (key.equals(COLUMN_MEASUREMENT_PORT_IDS))
				return kis.getMeasurementPortIds();
			if (key.equals(COLUMN_CHARACTERISTICS))
				return kis.getCharacteristics();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof KIS) {
			KIS kis = (KIS) object;
			if (key.equals(COLUMN_NAME))
				kis.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				kis.setDescription((String) value);
			else if (key.equals(COLUMN_EQUIPMENT_ID))
				kis.setEquipmentId((Identifier) value);
			else if (key.equals(COLUMN_MCM_ID))
				kis.setMCMId((Identifier) value);
			else if (key.equals(COLUMN_HOSTNAME))
				kis.setHostName((String) value);
			else if (key.equals(COLUMN_TCP_PORT))
				kis.setTCPPort(((Short)value).shortValue());
			else if (key.equals(COLUMN_MEASUREMENT_PORT_IDS)) 
				kis.setMeasurementPortIds((List) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				kis.setCharacteristics((List) value);
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
		if (key.equals(COLUMN_MEASUREMENT_PORT_IDS) || key.equals(COLUMN_CHARACTERISTICS))
			return List.class;
		return String.class;
	}
}
