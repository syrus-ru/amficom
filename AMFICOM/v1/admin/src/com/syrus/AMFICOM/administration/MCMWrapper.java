/*
 * $Id: MCMWrapper.java,v 1.2 2005/02/03 08:36:54 bob Exp $
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
public class MCMWrapper implements StorableObjectWrapper {

	public static final String	COLUMN_USER_ID			= "user_id";
	public static final String	COLUMN_SERVER_ID		= "server_id";
	// public static final String COLUMN_LOCATION = "location";
	public static final String	COLUMN_HOSTNAME			= "hostname";

	public static final String	COLUMN_KIS_IDS			= "kis_ids";

	private static MCMWrapper	instance;

	private List				keys;

	private MCMWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_USER_ID, COLUMN_SERVER_ID,
				COLUMN_HOSTNAME, COLUMN_CHARACTERISTICS, COLUMN_KIS_IDS };

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static MCMWrapper getInstance() {
		if (instance == null)
			instance = new MCMWrapper();
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
		if (object instanceof MCM) {
			MCM mcm = (MCM) object;
			if (key.equals(COLUMN_NAME))
				return mcm.getName();
			if (key.equals(COLUMN_DESCRIPTION))
				return mcm.getDescription();
			if (key.equals(COLUMN_USER_ID))
				return mcm.getUserId();
			if (key.equals(COLUMN_SERVER_ID))
				return mcm.getServerId();
			if (key.equals(COLUMN_HOSTNAME))
				return mcm.getHostName();
			if (key.equals(COLUMN_CHARACTERISTICS))
				return mcm.getCharacteristics();
			if (key.equals(COLUMN_KIS_IDS))
				return mcm.getKISIds();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof MCM) {
			MCM mcm = (MCM) object;
			if (key.equals(COLUMN_NAME))
				mcm.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				mcm.setDescription((String) value);
			else if (key.equals(COLUMN_USER_ID))
				mcm.setUserId((Identifier) value);
			else if (key.equals(COLUMN_SERVER_ID))
				mcm.setServerId((Identifier) value);
			else if (key.equals(COLUMN_HOSTNAME))
				mcm.setHostName((String) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				mcm.setCharacteristics((List) value);
			else if (key.equals(COLUMN_KIS_IDS))
				mcm.setKISIds((List) value);

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
		if (key.equals(COLUMN_CHARACTERISTICS) || key.equals(COLUMN_KIS_IDS)) { return List.class; }
		return String.class;
	}

}
