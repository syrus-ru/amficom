/*
 * $Id: SystemUserWrapper.java,v 1.19 2006/04/10 16:56:18 arseniy Exp $
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

import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.19 $, $Date: 2006/04/10 16:56:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
public final class SystemUserWrapper extends StorableObjectWrapper<SystemUser> {
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
	
	public static final String LINK_COLUMN_ROLE_IDS = "role_ids";
	
	private static SystemUserWrapper instance;

	private List<String> keys;

	private SystemUserWrapper() {
		this.keys = Collections.unmodifiableList(
			Arrays.asList(
				new String[] { COLUMN_DESCRIPTION, 
						COLUMN_LOGIN, 
						COLUMN_NAME, 
						COLUMN_SORT, 
						LINK_COLUMN_ROLE_IDS}));
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
			if (key.equals(LINK_COLUMN_ROLE_IDS)) {
				try {
					return user.getRoleIds();
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
					return Collections.emptySet();
				}
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@SuppressWarnings("unchecked")
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
			} else if (key.equals(LINK_COLUMN_ROLE_IDS)) {
				try {
					user.setRoleIds((Set<Identifier>) value);
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}
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
				|| key.equals(COLUMN_LOGIN)
				|| key.equals(COLUMN_NAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_SORT)) {
			return Integer.class;
		}
		
		if (key.equals(LINK_COLUMN_ROLE_IDS)) {
			return Set.class;
		}
		
		return null;
	}

}
