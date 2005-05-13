/*
 * $Id: ServerProcessWrapper.java,v 1.3 2005/05/13 17:32:00 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/13 17:32:00 $
 * @author $Author: bass $
 * @module admin_v1
 */
public class ServerProcessWrapper extends StorableObjectWrapper {
	public static final String KEY_LOGIN_PROCESS_CODENAME = "LoginProcessCodename"; //$NON-NLS-1$
	public static final String KEY_EVENT_PROCESS_CODENAME = "EventProcessCodename"; //$NON-NLS-1$
	public static final String KEY_MSERVER_PROCESS_CODENAME = "MServerProcessCodename"; //$NON-NLS-1$
	public static final String KEY_CMSERVER_PROCESS_CODENAME = "CMServerProcessCodename"; //$NON-NLS-1$
	public static final String KEY_MSHSERVER_PROCESS_CODENAME = "MSHServerProcessCodename"; //$NON-NLS-1$
	public static final String KEY_ARSERVER_PROCESS_CODENAME = "ARServerProcessCodename"; //$NON-NLS-1$

	public static final String LOGIN_PROCESS_CODENAME = "LoginServer"; //$NON-NLS-1$
	public static final String EVENT_PROCESS_CODENAME = "EventServer"; //$NON-NLS-1$
	public static final String MSERVER_PROCESS_CODENAME = "MServer"; //$NON-NLS-1$
	public static final String CMSERVER_PROCESS_CODENAME = "CMServer"; //$NON-NLS-1$
	public static final String MSHSERVER_PROCESS_CODENAME = "MSHServer"; //$NON-NLS-1$
	public static final String ARSERVER_PROCESS_CODENAME = "ARServer"; //$NON-NLS-1$

	public static final String	COLUMN_SERVER_ID = "server_id"; //$NON-NLS-1$
	public static final String	COLUMN_USER_ID = "user_id"; //$NON-NLS-1$

	private static ServerProcessWrapper instance;

	private List keys;

	private ServerProcessWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_SERVER_ID, COLUMN_USER_ID, COLUMN_DESCRIPTION};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ServerProcessWrapper getInstance() {
		if (instance == null)
			instance = new ServerProcessWrapper();
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
		if (object instanceof ServerProcess) {
			ServerProcess serverProcess = (ServerProcess) object;
			if (key.equals(COLUMN_CODENAME))
				return serverProcess.getCodename();
			if (key.equals(COLUMN_SERVER_ID))
				return serverProcess.getServerId();
			if (key.equals(COLUMN_USER_ID))
				return serverProcess.getUserId();
			if (key.equals(COLUMN_DESCRIPTION))
				return serverProcess.getDescription();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof ServerProcess) {
			ServerProcess serverProcess = (ServerProcess) object;
			if (key.equals(COLUMN_DESCRIPTION))
				serverProcess.setDescription((String) value);
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
		return String.class;
	}

}
