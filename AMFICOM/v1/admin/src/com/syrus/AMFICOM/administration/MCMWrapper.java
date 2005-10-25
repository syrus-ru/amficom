/*
 * $Id: MCMWrapper.java,v 1.19 2005/10/25 19:53:15 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;

/**
 * @version $Revision: 1.19 $, $Date: 2005/10/25 19:53:15 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
public final class MCMWrapper extends StorableObjectWrapper<MCM> {

	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_SERVER_ID = "server_id";
	// public static final String COLUMN_LOCATION = "location";
	public static final String COLUMN_HOSTNAME = "hostname";

	public static final String COLUMN_KIS_IDS = "kis_ids";

	private static MCMWrapper instance;

	private List<String> keys;
	public static final String LINK_COLUMN_MCM_ID = "mcm_id";

	private MCMWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME, 
				COLUMN_DESCRIPTION, 
				COLUMN_USER_ID, 
				COLUMN_SERVER_ID, 
				COLUMN_DOMAIN_ID,
				COLUMN_HOSTNAME };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static MCMWrapper getInstance() {
		if (instance == null) {
			instance = new MCMWrapper();
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
	public Object getValue(final MCM mcm, final String key) {
		final Object value = super.getValue(mcm, key);
		if (value == null && mcm != null) {
			if (key.equals(COLUMN_NAME))
				return mcm.getName();
			if (key.equals(COLUMN_DESCRIPTION))
				return mcm.getDescription();
			if (key.equals(COLUMN_USER_ID))
				return mcm.getUserId();
			if (key.equals(COLUMN_SERVER_ID))
				return mcm.getServerId();
			else if (key.equals(COLUMN_DOMAIN_ID)) 
				return mcm.getDomainId();
			if (key.equals(COLUMN_HOSTNAME))
				return mcm.getHostName();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final MCM mcm, final String key, final Object value) {
		if (mcm != null) {
			if (key.equals(COLUMN_NAME))
				mcm.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				mcm.setDescription((String) value);
			else if (key.equals(COLUMN_USER_ID))
				mcm.setUserId((Identifier) value);
			else if (key.equals(COLUMN_SERVER_ID))
				mcm.setServerId((Identifier) value);
			else if (key.equals(COLUMN_DOMAIN_ID)) 
				mcm.setDomainId((Identifier) value);
			else if (key.equals(COLUMN_HOSTNAME))
				mcm.setHostName((String) value);

		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(String key) {
		Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_HOSTNAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_USER_ID)
				|| key.equals(COLUMN_SERVER_ID)
				|| key.equals(COLUMN_DOMAIN_ID)) {
			return Identifier.class;
		}
		return null;
	}

}
