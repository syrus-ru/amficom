/*
 * $Id: KISWrapper.java,v 1.3.2.1 2006/03/07 10:42:49 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;

/**
 * @version $Revision: 1.3.2.1 $, $Date: 2006/03/07 10:42:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class KISWrapper extends StorableObjectWrapper<KIS> {

	// table :: kis
	// description VARCHAR2(256),
	// name VARCHAR2(64) NOT NULL,
	// hostname VARCHAR2(64),
	public static final String COLUMN_HOSTNAME = "hostname";
	// tcp_port NUMBER(5,0),
	public static final String COLUMN_TCP_PORT = "tcp_port";
	// equipment_id Identifier NOT NULL
	public static final String COLUMN_EQUIPMENT_ID = "equipment_id";
	// mcm_id Identifier NOT NULL
	public static final String COLUMN_MCM_ID = "mcm_id";

	public static final String COLUMN_ON_SERVICE = "on_service";

	public static final String COLUMN_MEASUREMENT_PORT_IDS = "measurementPortIds";

	private static KISWrapper instance;

	private List<String> keys;
	
	private KISWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION,
				COLUMN_NAME,
				COLUMN_DOMAIN_ID,
				COLUMN_HOSTNAME,
				COLUMN_TCP_PORT,
				COLUMN_EQUIPMENT_ID,
				COLUMN_MCM_ID,
				COLUMN_ON_SERVICE };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static KISWrapper getInstance() {
		if (instance == null) {
			instance = new KISWrapper();
		}
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
	public Object getValue(final KIS kis, final String key) {
		final Object value = super.getValue(kis, key);
		if (value == null && kis != null) {			
			if (key.equals(COLUMN_DESCRIPTION)) {
				return kis.getDescription();
			}
			if (key.equals(COLUMN_NAME)) {
				return kis.getName();
			}
			if (key.equals(COLUMN_DOMAIN_ID)) { 
				return kis.getDomainId();		
			}
			if (key.equals(COLUMN_HOSTNAME)) {
				return kis.getHostName();
			}
			if (key.equals(COLUMN_TCP_PORT)) {
				return Short.valueOf(kis.getTCPPort());
			}
			if (key.equals(COLUMN_EQUIPMENT_ID)) {
				return kis.getEquipmentId();
			}
			if (key.equals(COLUMN_MCM_ID)) {
				return kis.getMCMId();
			}
			if (key.equals(COLUMN_ON_SERVICE)) {
				return Boolean.valueOf(kis.isOnService());
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final KIS kis, final String key, final Object value) {
		if (kis != null) {
			if (key.equals(COLUMN_NAME)) {
				kis.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				kis.setDescription((String) value);
			} else if (key.equals(COLUMN_DOMAIN_ID)) {
				kis.setDomainId((Identifier) value);
			} else if (key.equals(COLUMN_HOSTNAME)) {
				kis.setHostName((String) value);
			} else if (key.equals(COLUMN_TCP_PORT)) {
				kis.setTCPPort(((Short) value).shortValue());
			} else if (key.equals(COLUMN_EQUIPMENT_ID)) {
				kis.setEquipmentId((Identifier) value);
			} else if (key.equals(COLUMN_MCM_ID)) {
				kis.setMCMId((Identifier) value);
			} else if (key.equals(COLUMN_ON_SERVICE)) {
				kis.setOnService(((Boolean) value).booleanValue());
			}
		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
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
		if (key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_HOSTNAME)) {
			return String.class;
		} else if (key.equals(COLUMN_TCP_PORT)) {
			return Short.class;			
		} else if (key.equals(COLUMN_EQUIPMENT_ID)
				|| key.equals(COLUMN_MCM_ID) 
				|| key.equals(COLUMN_DOMAIN_ID)) {
			return Identifier.class;
		}
		return null;
	}
}
