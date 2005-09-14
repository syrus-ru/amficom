/*
 * $Id: TransmissionPathWrapper.java,v 1.21 2005/09/14 18:42:07 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TableNames;

/**
 * @version $Revision: 1.21 $, $Date: 2005/09/14 18:42:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class TransmissionPathWrapper extends StorableObjectWrapper<TransmissionPath> {

	// table :: TransmissionPath
	// description VARCHAR2(256),
	// name VARCHAR2(64) NOT NULL,
	// start_port_id VARCHAR2(32),
	public static final String COLUMN_START_PORT_ID = "start_port_id";

	// finish_port_id VARCHAR2(32),
	public static final String COLUMN_FINISH_PORT_ID = "finish_port_id";

	private static TransmissionPathWrapper instance;

	private List<String> keys;

	private TransmissionPathWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION, 
				COLUMN_DOMAIN_ID, 
				COLUMN_NAME, 
				TableNames.TRANSMISSIONPATH_ME_LINK };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static TransmissionPathWrapper getInstance() {
		if (instance == null) {
			instance = new TransmissionPathWrapper();
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
	public Object getValue(final TransmissionPath transmissionPath, final String key) {
		final Object value = super.getValue(transmissionPath, key);
		if (value == null && transmissionPath != null) {
			if (key.equals(COLUMN_DESCRIPTION))
				return transmissionPath.getDescription();
			if (key.equals(COLUMN_NAME))
				return transmissionPath.getName();
			if (key.equals(COLUMN_DOMAIN_ID))
				return transmissionPath.getDomainId();
			if (key.equals(COLUMN_TYPE_ID))
				return transmissionPath.getType();
			if (key.equals(COLUMN_START_PORT_ID))
				return transmissionPath.getStartPortId();
			if (key.equals(COLUMN_FINISH_PORT_ID))
				return transmissionPath.getFinishPortId();
			if (key.equals(TableNames.TRANSMISSIONPATH_ME_LINK))
				return transmissionPath.getMonitoredElementIds();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final TransmissionPath transmissionPath, final String key, final Object value) {
		if (transmissionPath != null) {
			if (key.equals(COLUMN_NAME))
				transmissionPath.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				transmissionPath.setDescription((String) value);
			else if (key.equals(COLUMN_DOMAIN_ID))
				transmissionPath.setDomainId((Identifier)value);
			else if (key.equals(COLUMN_TYPE_ID))
				transmissionPath.setType((TransmissionPathType) value);
			else if (key.equals(COLUMN_START_PORT_ID))
				transmissionPath.setStartPortId((Identifier) value);
			else if (key.equals(COLUMN_FINISH_PORT_ID))
				transmissionPath.setFinishPortId((Identifier) value);
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
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		if (key.equals(COLUMN_TYPE_ID))
			return TransmissionPathType.class;
		else if (key.equals(COLUMN_START_PORT_ID)
				|| key.equals(COLUMN_FINISH_PORT_ID)
				|| key.equals(COLUMN_DOMAIN_ID))
			return Identifier.class;
		if (key.equals(TableNames.TRANSMISSIONPATH_ME_LINK)) {
			return Set.class;
		}
		return null;
	}
}
