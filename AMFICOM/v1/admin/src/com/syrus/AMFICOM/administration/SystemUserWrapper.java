/*
 * $Id: SystemUserWrapper.java,v 1.15 2005/10/10 15:47:19 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.15 $, $Date: 2005/10/10 15:47:19 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
public class SystemUserWrapper extends StorableObjectWrapper<SystemUser> {
	public static final String SYS_LOGIN = "sys";
	public static final String LOGINPROCESSOR_LOGIN = "loginprocessor";
	public static final String EVENTPROCESSOR_LOGIN = "eventprocessor";
	public static final String MSERVER_LOGIN = "mserver";
	public static final String CMSERVER_LOGIN = "cmserver";
	public static final String MSCHARSERVER_LOGIN = "mscharserver";
	public static final String MCM_LOGIN = "mcm";
	
	

	// table :: users
	// description VARCHAR2(256),
	// login VARCHAR2(32) NOT NULL,
	public static final String COLUMN_LOGIN = "login";
	// name VARCHAR2(64) not NULL,
	// sort NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_SORT = "sort";

	public static final String LINK_COLUMN_ROLE_ID = "role_id";
	
	public static final String LINK_COLUMN_SYSTEM_USER_ID = "system_user_id";
	
	private static SystemUserWrapper instance;

	private List<String> keys;

	private SystemUserWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_LOGIN, COLUMN_NAME, COLUMN_SORT};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static SystemUserWrapper getInstance() {
		if (instance == null) {
			instance = new SystemUserWrapper();
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
	public Object getValue(final SystemUser user, final String key) {
		final Object value = super.getValue(user, key);
		if (value == null && user != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				return user.getDescription();
			}
			if (key.equals(COLUMN_LOGIN)) {
				return user.getLogin();
			}
			if (key.equals(COLUMN_NAME)) {
				return user.getName();
			}
			if (key.equals(COLUMN_SORT)) {
				return new Integer(user.getSort().value());
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final SystemUser user, final String key, final Object value) {
		if (user != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				user.setDescription((String) value);
			} else if (key.equals(COLUMN_LOGIN)) {
				user.setLogin((String) value);
			} else if (key.equals(COLUMN_NAME)) {
				user.setName((String) value);
			} else if (key.equals(COLUMN_SORT)) {
				user.setSort(SystemUserSort.from_int(((Integer) value).intValue()));
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
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_LOGIN)
				|| key.equals(COLUMN_NAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_SORT)) {
			return Integer.class;
		}
		return null;
	}

}
