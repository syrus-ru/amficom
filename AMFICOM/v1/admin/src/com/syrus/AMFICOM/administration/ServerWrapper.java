/*
 * $Id: ServerWrapper.java,v 1.11 2005/07/25 20:48:15 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.11 $, $Date: 2005/07/25 20:48:15 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
public class ServerWrapper extends StorableObjectWrapper {

	// description VARCHAR2(256),
	// name VARCHAR2(64) NOT NULL,
	// hostname VARCHAR2(64) NOT NULL,
	public static final String COLUMN_HOSTNAME = "hostname";

	private static ServerWrapper instance;

	private List<String> keys;

	private ServerWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, DomainMember.COLUMN_DOMAIN_ID, COLUMN_HOSTNAME };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ServerWrapper getInstance() {
		if (instance == null)
			instance = new ServerWrapper();
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
	public Object getValue(final Object object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object instanceof Server) {
			final Server server = (Server) object;
			if (key.equals(COLUMN_NAME))
				return server.getName();
			if (key.equals(COLUMN_DESCRIPTION))
				return server.getDescription();
			if (key.equals(DomainMember.COLUMN_DOMAIN_ID))
				return server.getDomainId();
			if (key.equals(COLUMN_HOSTNAME))
				return server.getHostName();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof Server) {
			final Server server = (Server) object;
			if (key.equals(COLUMN_NAME))
				server.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				server.setDescription((String) value);
			else if (key.equals(DomainMember.COLUMN_DOMAIN_ID))
				server.setDomainId((Identifier) value);
			else if (key.equals(COLUMN_HOSTNAME))
				server.setHostName((String) value);
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
		}
		if (key.equals(DomainMember.COLUMN_DOMAIN_ID)) {
			return Identifier.class;
		}
		return null;
	}

}
