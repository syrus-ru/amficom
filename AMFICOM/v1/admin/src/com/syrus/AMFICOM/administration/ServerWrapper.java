/*
 * $Id: ServerWrapper.java,v 1.18.4.1 2006/06/09 15:59:59 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.18.4.1 $, $Date: 2006/06/09 15:59:59 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
public final class ServerWrapper extends StorableObjectWrapper<Server> {
	public static final String COLUMN_HOSTNAME = "hostname";
	public static final String COLUMN_SYSTEM_USER_ID = "system_user_id";

	private static ServerWrapper instance;

	private List<String> keys;

	private ServerWrapper() {
		final String[] keysArray = new String[] { COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_DOMAIN_ID,
				COLUMN_HOSTNAME,
				COLUMN_SYSTEM_USER_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ServerWrapper getInstance() {
		if (instance == null) {
			instance = new ServerWrapper();
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
	public Object getValue(final Server server, final String key) {
		final Object value = super.getValue(server, key);
		if (value == null && server != null) {
			if (key.equals(COLUMN_NAME)) {
				return server.getName();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return server.getDescription();
			}
			if (key.equals(COLUMN_DOMAIN_ID)) {
				return server.getDomainId();
			}
			if (key.equals(COLUMN_HOSTNAME)) {
				return server.getHostName();
			}
			if (key.equals(COLUMN_SYSTEM_USER_ID)) {
				return server.getSystemUserId();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final Server server, final String key, final Object value) {
		if (server != null) {
			if (key.equals(COLUMN_NAME)) {
				server.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				server.setDescription((String) value);
			} else if (key.equals(COLUMN_DOMAIN_ID)) {
				server.setDomainId((Identifier) value);
			} else if (key.equals(COLUMN_HOSTNAME)) {
				server.setHostName((String) value);
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
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_HOSTNAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_DOMAIN_ID)
				|| key.equals(COLUMN_SYSTEM_USER_ID)) {
			return Identifier.class;
		}
		return null;
	}

}
