/*
 * $Id: TransmissionPathWrapper.java,v 1.4 2005/01/31 14:42:35 bob Exp $
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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/01/31 14:42:35 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class TransmissionPathWrapper implements Wrapper {

	private static TransmissionPathWrapper	instance;

	private List							keys;

	// table :: TransmissionPath
	// description VARCHAR2(256),
	public static final String				COLUMN_DESCRIPTION		= "description";

	// name VARCHAR2(64) NOT NULL,
	public static final String				COLUMN_NAME				= "name";

	// start_port_id VARCHAR2(32),
	public static final String				COLUMN_START_PORT_ID	= "start_port_id";

	// finish_port_id VARCHAR2(32),
	public static final String				COLUMN_FINISH_PORT_ID	= "finish_port_id";

	public static final String				COLUMN_TYPE_ID			= "type_id";

	public static final String				COLUMN_CHARACTERISTICS	= "characteristics";

	private TransmissionPathWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_CHARACTERISTICS,
				ObjectEntities.TRANSPATHMELINK_ENTITY};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static TransmissionPathWrapper getInstance() {
		if (instance == null)
			instance = new TransmissionPathWrapper();
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
		if (object instanceof TransmissionPath) {
			TransmissionPath path = (TransmissionPath) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return path.getId();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return path.getCreated();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return path.getCreatorId();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return path.getModified();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return path.getModifierId();
			if (key.equals(COLUMN_DESCRIPTION))
				return path.getDescription();
			if (key.equals(COLUMN_NAME))
				return path.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return path.getType();
			if (key.equals(COLUMN_START_PORT_ID))
				return path.getStartPortId();
			if (key.equals(COLUMN_FINISH_PORT_ID))
				return path.getFinishPortId();
			if (key.equals(COLUMN_CHARACTERISTICS))
				return path.getCharacteristics();
			if (key.equals(ObjectEntities.TRANSPATHMELINK_ENTITY))
				return path.getMonitoredElementIds();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof TransmissionPath) {
			TransmissionPath path = (TransmissionPath) object;
			if (key.equals(COLUMN_NAME))
				path.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				path.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID))
				path.setType((TransmissionPathType) value);
			else if (key.equals(COLUMN_START_PORT_ID))
				path.setStartPortId((Identifier) value);
			else if (key.equals(COLUMN_FINISH_PORT_ID))
				path.setFinishPortId((Identifier) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				path.setCharacteristics((List) value);
			else if (key.equals(ObjectEntities.EQUIPMENTMELINK_ENTITY))
				path.setMonitoredElementIds((List) value);
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
		if (key.equals(COLUMN_CHARACTERISTICS) || key.equals(ObjectEntities.EQUIPMENTMELINK_ENTITY))
			return List.class;
		return String.class;
	}
}
