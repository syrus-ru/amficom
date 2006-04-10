/*
 * $Id: RoleWrapper.java,v 1.4 2006/04/10 16:56:18 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2006/04/10 16:56:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
public final class RoleWrapper extends StorableObjectWrapper<Role> {
	public static final String LINK_COLUMN_ROLE_ID = "role_id";
	public static final String LINK_COLUMN_SYSTEM_USER_ID = "system_user_id";

	private static RoleWrapper instance;

	private List<String> keys;

	private RoleWrapper() {
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_CODENAME, LINK_COLUMN_SYSTEM_USER_ID };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static RoleWrapper getInstance() {
		if (instance == null) {
			instance = new RoleWrapper();
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
	public Object getValue(final Role role, final String key) {
		final Object value = super.getValue(role, key);
		if (value == null && role != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				return role.getDescription();
			}
			if (key.equals(COLUMN_CODENAME)) {
				return role.getCodename();
			}
			if (key.equals(LINK_COLUMN_SYSTEM_USER_ID)) {
				return role.getSystemUserIds();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final Role user, final String key, final Object value) {
		if (user != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				user.setDescription((String) value);
			} else if (key.equals(COLUMN_CODENAME)) {
				user.setCodename((String) value);
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
		if (key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_CODENAME)) {
			return String.class;
		}
		if (key.equals(LINK_COLUMN_SYSTEM_USER_ID)) {
			return Set.class;
		}
		return null;
	}

}
