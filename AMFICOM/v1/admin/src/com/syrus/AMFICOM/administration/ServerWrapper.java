/*
 * $Id: ServerWrapper.java,v 1.2 2005/02/03 08:36:54 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/03 08:36:54 $
 * @author $Author: bob $
 * @module admin_v1
 */
public class ServerWrapper implements StorableObjectWrapper {

	// description VARCHAR2(256),
	// name VARCHAR2(64) NOT NULL,
	// hostname VARCHAR2(64) NOT NULL,
	public static final String		COLUMN_HOSTNAME			= "hostname";
	// user_id VARCHAR2(32) NOT NULL,
	public static final String		COLUMN_USER_ID			= "user_id";

	private static ServerWrapper	instance;

	private List					keys;

	private ServerWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, DomainMember.COLUMN_DOMAIN_ID,
				COLUMN_HOSTNAME, COLUMN_USER_ID, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static ServerWrapper getInstance() {
		if (instance == null)
			instance = new ServerWrapper();
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
		if (object instanceof Server) {
			Server server = (Server) object;
			if (key.equals(COLUMN_NAME))
				return server.getName();
			if (key.equals(COLUMN_DESCRIPTION))
				return server.getDescription();
			if (key.equals(DomainMember.COLUMN_DOMAIN_ID))
				return server.getDomainId();
			if (key.equals(COLUMN_HOSTNAME))
				return server.getHostName();
			if (key.equals(COLUMN_USER_ID))
				return server.getUserId();
			if (key.equals(COLUMN_CHARACTERISTICS))
				return server.getCharacteristics();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Server) {
			Server server = (Server) object;
			if (key.equals(COLUMN_NAME))
				server.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				server.setDescription((String) value);
			else if (key.equals(DomainMember.COLUMN_DOMAIN_ID))
				server.setDomainId((Identifier) value);
			else if (key.equals(COLUMN_HOSTNAME))
				server.setHostName((String) value);
			else if (key.equals(COLUMN_USER_ID))
				server.setUserId((Identifier) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				server.setCharacteristics((List) value);
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
		if (key.equals(COLUMN_CHARACTERISTICS)) { return List.class; }
		return String.class;
	}

}
