/*-
 * $Id: SchemeOptimizeInfoSwitchWrapper.java,v 1.2 2005/06/07 16:32:58 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/06/07 16:32:58 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfoSwitchWrapper extends StorableObjectWrapper {
	private static SchemeOptimizeInfoSwitchWrapper instance;

	/**
	 * @see com.syrus.util.Wrapper#getKeys()
	 */
	public List getKeys() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getName(java.lang.String)
	 */
	public String getName(String key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getPropertyClass(java.lang.String)
	 */
	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getPropertyValue(java.lang.String)
	 */
	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param key
	 * @param objectKey
	 * @param objectValue
	 * @see com.syrus.util.Wrapper#setPropertyValue(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(String key, Object objectKey,
			Object objectValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#isEditable(java.lang.String)
	 */
	public boolean isEditable(String key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param object
	 * @param key
	 * @param value
	 * @see com.syrus.util.Wrapper#setValue(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException();
	}

	public static SchemeOptimizeInfoSwitchWrapper getInstance() {
		if (instance == null)
			instance = new SchemeOptimizeInfoSwitchWrapper();
		return instance;
	}
}
