/*-
 * $Id: SchemeOptimizeInfoSwitchWrapper.java,v 1.7 2005/08/16 12:01:39 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: max $
 * @version $Revision: 1.7 $, $Date: 2005/08/16 12:01:39 $
 * @module scheme
 */
public final class SchemeOptimizeInfoSwitchWrapper extends StorableObjectWrapper<SchemeOptimizeInfoSwitch> {
	public static final String COLUMN_PRICE = "price_usd";

	public static final String COLUMN_NO_OF_PORTS = "no_of_ports";

	public static final String COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID = "parent_scheme_optimize_info_id";

	private static SchemeOptimizeInfoSwitchWrapper instance;

	private final List<String> keys;
	
	private SchemeOptimizeInfoSwitchWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_PRICE,
				COLUMN_NO_OF_PORTS,
				COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID}));
	}
	
	public List<String> getKeys() {
		return this.keys;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getName(java.lang.String)
	 */
	public String getName(String key) {
		return key;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getPropertyClass(java.lang.String)
	 */
	@Override
	public Class getPropertyClass(String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)) {
			return String.class;
		} else if (key.equals(COLUMN_PRICE)) {
			return Integer.class;
		} else if (key.equals(COLUMN_NO_OF_PORTS)) {
			return Byte.class;
		} else if (key.equals(COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID)) {
			return Identifier.class;
		}
		return null;
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#getPropertyValue(java.lang.String)
	 */
	public Object getPropertyValue(String key) {
		//there is no property
		return null;
	}

	/**
	 * @param key
	 * @param objectKey
	 * @param objectValue
	 * @see com.syrus.util.Wrapper#setPropertyValue(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(String key, Object objectKey,
			Object objectValue) {
		//there is no property
	}

	/**
	 * @param key
	 * @see com.syrus.util.Wrapper#isEditable(java.lang.String)
	 */
	public boolean isEditable(String key) {
		return false;
	}

	/**
	 * @param schemeOptimizeInfoSwitch
	 * @param key
	 * @param value
	 * @see com.syrus.util.Wrapper#setValue(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	@SuppressWarnings("boxing")
	public void setValue(SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch, String key, Object value) {
		if (schemeOptimizeInfoSwitch != null) {
			if (key.equals(COLUMN_NAME)) {
				schemeOptimizeInfoSwitch.setName((String) value);
			} else if (key.equals(COLUMN_PRICE)) {
				schemeOptimizeInfoSwitch.setPriceUsd((Integer) value);
			} else if (key.equals(COLUMN_NO_OF_PORTS)) {
				schemeOptimizeInfoSwitch.setNoOfPorts((Byte) value);
			} else if (key.equals(COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID)) {
				schemeOptimizeInfoSwitch.setParentSchemeOptimizeInfoId((Identifier) value);
			}
		}
	}

	/**
	 * @param schemeOptimizeInfoSwitch
	 * @param key
	 */
	@Override
	@SuppressWarnings("boxing")
	public Object getValue(SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch, String key) {
		final Object value = super.getValue(schemeOptimizeInfoSwitch, key);
		if (value != null) {
			return value;
		}
		if (schemeOptimizeInfoSwitch != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeOptimizeInfoSwitch.getName();
			} else if (key.equals(COLUMN_PRICE)) {
				return schemeOptimizeInfoSwitch.getPriceUsd();
			} else if (key.equals(COLUMN_NO_OF_PORTS)) {
				return schemeOptimizeInfoSwitch.getNoOfPorts();
			} else if (key.equals(COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID)) {
				return schemeOptimizeInfoSwitch.getParentSchemeOptimizeInfoId();
			}
		}
		return null;
	}

	public static SchemeOptimizeInfoSwitchWrapper getInstance() {
		if (instance == null)
			instance = new SchemeOptimizeInfoSwitchWrapper();
		return instance;
	}
}
