/*
 * $Id: KISWrapper.java,v 1.1 2005/01/26 13:18:49 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/26 13:18:49 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class KISWrapper implements Wrapper {

	public static final String	COLUMN_NAME					= "name";
	public static final String	COLUMN_EQUIPMENT_ID			= "equipment_id";
	public static final String	COLUMN_MCM_ID				= "mcm_id";
	public static final String	COLUMN_HOSTNAME				= "hostname";
	public static final String	COLUMN_TCP_PORT				= "tcp_port";
	public static final String	COLUMN_MEASUREMENT_PORT_IDS	= "measurementPortIds";
	public static final String	COLUMN_CHARACTERISTICS		= "characteristics";

	private static KISWrapper	instance;

	private List				keys;

	private KISWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, StorableObjectType.COLUMN_DESCRIPTION, COLUMN_NAME,
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
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return kis.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return kis.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return kis.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return kis.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return kis.getModifierId().getIdentifierString();
			if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				return kis.getDescription();
			if (key.equals(COLUMN_NAME))
				return kis.getName();
			if (key.equals(COLUMN_EQUIPMENT_ID))
				return kis.getEquipmentId().getIdentifierString();
			if (key.equals(COLUMN_MCM_ID))
				return kis.getMCMId().getIdentifierString();
			if (key.equals(COLUMN_HOSTNAME))
				return kis.getHostName();
			if (key.equals(COLUMN_TCP_PORT))
				return Short.toString(kis.getTCPPort());
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
			else if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				kis.setDescription((String) value);
			else if (key.equals(COLUMN_EQUIPMENT_ID))
				kis.setEquipmentId(new Identifier((String) value));
			else if (key.equals(COLUMN_MCM_ID))
				kis.setMCMId(new Identifier((String) value));
			else if (key.equals(COLUMN_HOSTNAME))
				kis.setHostName((String) value);
			else if (key.equals(COLUMN_TCP_PORT))
				kis.setTCPPort(Short.parseShort((String) value));
			else if (key.equals(COLUMN_MEASUREMENT_PORT_IDS)) {
				List portIds = new ArrayList(((List) value).size());
				for (Iterator it = ((List) value).iterator(); it.hasNext();)
					portIds.add(new Identifier((String) it.next()));
				kis.setMeasurementPortIds(portIds);
			} else if (key.equals(COLUMN_CHARACTERISTICS)) {
				List charIdStr = (List) value;
				List characteristicIds = new ArrayList(charIdStr.size());
				for (Iterator it = charIdStr.iterator(); it.hasNext();)
					characteristicIds.add(new Identifier((String) it.next()));
				try {
					kis.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("KISWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
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
		if (key.equals(COLUMN_MEASUREMENT_PORT_IDS) || key.equals(COLUMN_CHARACTERISTICS))
			return List.class;
		return String.class;
	}
}
