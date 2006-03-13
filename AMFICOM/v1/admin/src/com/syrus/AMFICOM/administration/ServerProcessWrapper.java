/*
 * $Id: ServerProcessWrapper.java,v 1.15 2006/03/13 15:54:25 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.15 $, $Date: 2006/03/13 15:54:25 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
public final class ServerProcessWrapper extends StorableObjectWrapper<ServerProcess> {
	public static final String KEY_LOGIN_PROCESS_CODENAME = "LoginProcessCodename";
	public static final String KEY_EVENT_PROCESS_CODENAME = "EventProcessCodename";
	public static final String KEY_MSERVER_PROCESS_CODENAME = "MServerProcessCodename";
	public static final String KEY_CMSERVER_PROCESS_CODENAME = "CMServerProcessCodename";
	public static final String KEY_MSCHARSERVER_PROCESS_CODENAME = "MSchARServerProcessCodename";

	public static final String LOGIN_PROCESS_CODENAME = "LoginServer";
	public static final String EVENT_PROCESS_CODENAME = "EventServer";
	public static final String MSERVER_PROCESS_CODENAME = "MServer";
	public static final String CMSERVER_PROCESS_CODENAME = "CMServer";
	public static final String MSCHARSERVER_PROCESS_CODENAME = "MSchARServer";

	public static final String	COLUMN_SERVER_ID = "server_id";
	public static final String	COLUMN_USER_ID = "user_id";

	private static ServerProcessWrapper instance;

	private List<String> keys;

	private ServerProcessWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_SERVER_ID, COLUMN_USER_ID, COLUMN_DESCRIPTION};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ServerProcessWrapper getInstance() {
		if (instance == null) {
			instance = new ServerProcessWrapper();
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
	public Object getValue(final ServerProcess serverProcess, 
	                       final String key) {
		final Object value = super.getValue(serverProcess, key);
		if (value == null && serverProcess != null) {
			if (key.equals(COLUMN_CODENAME))
				return serverProcess.getCodename();
			if (key.equals(COLUMN_SERVER_ID))
				return serverProcess.getServerId();
			if (key.equals(COLUMN_USER_ID))
				return serverProcess.getUserId();
			if (key.equals(COLUMN_DESCRIPTION))
				return serverProcess.getDescription();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final ServerProcess serverProcess, final String key, final Object value) {
		if (serverProcess != null) {
			if (key.equals(COLUMN_DESCRIPTION))
				serverProcess.setDescription((String) value);
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
		if (key.equals(COLUMN_CODENAME)
				|| key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		if (key.equals(COLUMN_SERVER_ID)
				|| key.equals(COLUMN_USER_ID)) {
			return Identifier.class;
		}
		return null;
	}

}
