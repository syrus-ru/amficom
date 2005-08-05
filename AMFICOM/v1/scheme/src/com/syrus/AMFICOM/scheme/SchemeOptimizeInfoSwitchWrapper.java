/*-
 * $Id: SchemeOptimizeInfoSwitchWrapper.java,v 1.6 2005/08/05 11:20:03 bass Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/08/05 11:20:03 $
 * @module scheme
 */
public final class SchemeOptimizeInfoSwitchWrapper extends StorableObjectWrapper<SchemeOptimizeInfoSwitch> {
	public static final String COLUMN_PRICE = "price_usd";

	public static final String COLUMN_NO_OF_PORTS = "no_of_ports";

	public static final String COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID = "parent_scheme_optimize_info_id";

	private static SchemeOptimizeInfoSwitchWrapper instance;

	/**
	 * @see com.syrus.util.Wrapper#getKeys()
	 */
	public List<String> getKeys() {
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
	@Override
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
	@Override
	public void setValue(SchemeOptimizeInfoSwitch object, String key, Object value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param object
	 * @param key
	 */
	@Override
	public Object getValue(SchemeOptimizeInfoSwitch object, String key) {
		throw new UnsupportedOperationException();
	}

	public static SchemeOptimizeInfoSwitchWrapper getInstance() {
		if (instance == null)
			instance = new SchemeOptimizeInfoSwitchWrapper();
		return instance;
	}
}
