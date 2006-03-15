/*-
 * $Id: SchemeOptimizeInfoSwitchWrapper.java,v 1.11 2006/03/15 15:49:10 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2006/03/15 15:49:10 $
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
	public Class<?> getPropertyClass(String key) {
		final Class<?> clazz = super.getPropertyClass(key);
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
	public void setValue(final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch,
			final String key, final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (schemeOptimizeInfoSwitch != null) {
				if (key.equals(COLUMN_NAME)) {
					schemeOptimizeInfoSwitch.setName((String) value);
				} else if (key.equals(COLUMN_PRICE)) {
					schemeOptimizeInfoSwitch.setPriceUsd(((Integer) value).intValue());
				} else if (key.equals(COLUMN_NO_OF_PORTS)) {
					schemeOptimizeInfoSwitch.setNoOfPorts(((Byte) value).byteValue());
				} else if (key.equals(COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID)) {
					schemeOptimizeInfoSwitch.setParentSchemeOptimizeInfoId((Identifier) value, usePool);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	/**
	 * @param schemeOptimizeInfoSwitch
	 * @param key
	 */
	@Override
	public Object getValue(SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch, String key) {
		final Object value = super.getValue(schemeOptimizeInfoSwitch, key);
		if (value != null) {
			return value;
		}
		if (schemeOptimizeInfoSwitch != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeOptimizeInfoSwitch.getName();
			} else if (key.equals(COLUMN_PRICE)) {
				return Integer.valueOf(schemeOptimizeInfoSwitch.getPriceUsd());
			} else if (key.equals(COLUMN_NO_OF_PORTS)) {
				return Byte.valueOf(schemeOptimizeInfoSwitch.getNoOfPorts());
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
