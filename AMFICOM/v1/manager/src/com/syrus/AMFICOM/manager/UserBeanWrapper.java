
package com.syrus.AMFICOM.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.util.Wrapper;

/*-
 * $Id: UserBeanWrapper.java,v 1.1 2005/07/19 09:49:00 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

/**
 * @version $Revision: 1.1 $, $Date: 2005/07/19 09:49:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module Miscs
 */
public class UserBeanWrapper implements Wrapper {

	public static final String		KEY_FULL_NAME	= "fullName";
	public static final String		KEY_USER_NATURE	= "nature";

	private static UserBeanWrapper	instance;

	private List					keys;

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	private UserBeanWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_FULL_NAME, KEY_USER_NATURE};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static UserBeanWrapper getInstance() {
		if (instance == null)
			instance = new UserBeanWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(KEY_FULL_NAME) || key.equals(KEY_USER_NATURE)) { return String.class; }
		return null;
	}

	public Object getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(	Object object,
							String key) {
		if (object instanceof UserBean) {
			UserBean userBean = (UserBean) object;
			if (key.equals(KEY_FULL_NAME)) {
				return userBean.getFullName();
			} else if (key.equals(KEY_USER_NATURE)) { return userBean.getNature(); }
		}
		return null;
	}

	public boolean isEditable(String key) {
		return !key.equals(KEY_USER_NATURE);
	}

	public void setPropertyValue(	String key,
									Object objectKey,
									Object objectValue) {
		// TODO Auto-generated method stub

	}

	public void setValue(	Object object,
							String key,
							Object value) {
		if (object instanceof UserBean) {
			UserBean userBean = (UserBean) object;
			if (key.equals(KEY_FULL_NAME)) {
				userBean.setFullName((String) value);
			} else if (key.equals(KEY_USER_NATURE)) {
				userBean.setNature((String) value);
			}
		}
	}

}
