/*
 * $Id: UserWrapper.java,v 1.1 2005/02/01 11:36:30 bob Exp $
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

import com.syrus.AMFICOM.administration.corba.UserSort;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/01 11:36:30 $
 * @author $Author: bob $
 * @module admin_v1
 */
public class UserWrapper implements Wrapper {

	// table :: users
	// description VARCHAR2(256),
	public static final String	COLUMN_DESCRIPTION	= "description";
	// login VARCHAR2(32) NOT NULL,
	public static final String	COLUMN_LOGIN		= "login";
	// name VARCHAR2(64) not NULL,
	public static final String	COLUMN_NAME			= "name";
	// sort NUMBER(2, 0) NOT NULL,
	public static final String	COLUMN_SORT			= "sort";

	private static UserWrapper	instance;

	private List				keys;

	private UserWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_LOGIN, COLUMN_NAME, COLUMN_SORT};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static UserWrapper getInstance() {
		if (instance == null)
			instance = new UserWrapper();
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
		if (object instanceof User) {
			User user = (User) object;
			if (key.equals(COLUMN_DESCRIPTION))
				return user.getDescription();
			if (key.equals(COLUMN_LOGIN))
				return user.getLogin();
			if (key.equals(COLUMN_NAME))
				return user.getName();
			if (key.equals(COLUMN_SORT))
				return new Integer(user.getSort().value());
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof User) {
			User user = (User) object;
			if (key.equals(COLUMN_DESCRIPTION))
				user.setDescription((String) value);
			else if (key.equals(COLUMN_LOGIN))
				user.setLogin((String) value);
			else if (key.equals(COLUMN_NAME))
				user.setName((String) value);
			else if (key.equals(COLUMN_SORT))
				user.setSort(UserSort.from_int(((Integer) value).intValue()));
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
