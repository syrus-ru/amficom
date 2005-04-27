/*
 * $Id: ServerProcessWrapper.java,v 1.1 2005/04/27 17:44:08 arseniy Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/04/27 17:44:08 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
public class ServerProcessWrapper extends StorableObjectWrapper {
	public static final String	COLUMN_SERVER_ID = "server_id";
	public static final String	COLUMN_USER_ID = "user_id";

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
