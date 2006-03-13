/*
 * $Id: RoleWrapper.java,v 1.3 2006/03/13 15:54:25 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2006/03/13 15:54:25 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
public final class RoleWrapper extends StorableObjectWrapper<Role> {

	private static RoleWrapper instance;

	private List<String> keys;

	private RoleWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_CODENAME};

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
		return null;
	}

}
