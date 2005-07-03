/*
 * $Id: SystemUserWrapper.java,v 1.7 2005/06/25 17:50:50 bass Exp $
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
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/25 17:50:50 $
 * @author $Author: bass $
 * @module admin_v1
 */
public class SystemUserWrapper extends StorableObjectWrapper {
	public static final String	SYS_LOGIN		= "sys";
	public static final String LOGINPROCESSOR_LOGIN = "loginprocessor";
	public static final String EVENTPROCESSOR_LOGIN = "eventprocessor";
	public static final String MSERVER_LOGIN = "mserver";
	public static final String CMSERVER_LOGIN = "cmserver";
	public static final String MSCHARSERVER_LOGIN = "mscharserver";
	public static final String MCM_LOGIN = "mcm";

	// table :: users
	// description VARCHAR2(256),
	// login VARCHAR2(32) NOT NULL,
	public static final String	COLUMN_LOGIN		= "login";
	// name VARCHAR2(64) not NULL,
	// sort NUMBER(2, 0) NOT NULL,
	public static final String	COLUMN_SORT			= "sort";

	private static SystemUserWrapper	instance;

	private List				keys;

	private SystemUserWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_LOGIN, COLUMN_NAME, COLUMN_SORT, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static SystemUserWrapper getInstance() {
		if (instance == null)
			instance = new SystemUserWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final Object object, final String key) {
		Object value = super.getValue(object, key);
		if (value == null && object instanceof SystemUser) {
			SystemUser user = (SystemUser) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return user.getDescription();
			if (key.equals(COLUMN_LOGIN))
				return user.getLogin();
			if (key.equals(COLUMN_NAME))
				return user.getName();
			if (key.equals(COLUMN_SORT))
				return new Integer(user.getSort().value());
			if (key.equals(COLUMN_CHARACTERISTICS))
				return user.getCharacteristics();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof SystemUser) {
			SystemUser user = (SystemUser) object;
			if (key.equals(COLUMN_DESCRIPTION))
				user.setDescription((String) value);
			else if (key.equals(COLUMN_LOGIN))
				user.setLogin((String) value);
			else if (key.equals(COLUMN_NAME))
				user.setName((String) value);
			else if (key.equals(COLUMN_SORT))
				user.setSort(SystemUserSort.from_int(((Integer) value).intValue()));
			else if (key.equals(COLUMN_CHARACTERISTICS))
				user.setCharacteristics((Set<Characteristic>) value);
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

	@Override
	public Class getPropertyClass(String key) {
		Class clazz = super.getPropertyClass(key); 
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
		if (key.equals(COLUMN_CHARACTERISTICS)) {
			return Set.class;
		}
		return null;
	}

}
