/*-
 * $Id: SchemeMonitoringSolutionWrapper.java,v 1.15 2006/03/15 17:31:13 bass Exp $
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
 * @version $Revision: 1.15 $, $Date: 2006/03/15 17:31:13 $
 * @author $Author: bass $
 * @module scheme
 */
public final class SchemeMonitoringSolutionWrapper extends StorableObjectWrapper<SchemeMonitoringSolution> {
	public static final String COLUMN_PRICE_USD = "price_usd";

	public static final String COLUMN_ACTIVE = "active";

	public static final String COLUMN_PARENT_SCHEME_ID = "parent_scheme_id";

	public static final String COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID = "parent_scheme_optimize_info_id";

	private static SchemeMonitoringSolutionWrapper instance;

	private List<String> keys;

	private SchemeMonitoringSolutionWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_PRICE_USD,
				COLUMN_ACTIVE,
				COLUMN_PARENT_SCHEME_ID,
				COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID}));
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		return key;
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME) || key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_PRICE_USD)) {
			return Integer.class;
		} else if (key.equals(COLUMN_ACTIVE)) {
			return Boolean.class;
		} else if (key.equals(COLUMN_PARENT_SCHEME_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(final String key) {
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		// empty
	}

	@Override
	public Object getValue(final SchemeMonitoringSolution schemeMonitoringSolution, final String key) {
		final Object value = super.getValue(schemeMonitoringSolution, key);
		if (value != null) {
			return value;
		}
		if (schemeMonitoringSolution != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeMonitoringSolution.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeMonitoringSolution.getDescription();
			} else if (key.equals(COLUMN_PRICE_USD)) {
				return new Integer(schemeMonitoringSolution.getPrice());
			} else if (key.equals(COLUMN_ACTIVE)) {
				return Boolean.valueOf(schemeMonitoringSolution.isActive());
			} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
				return schemeMonitoringSolution.getParentSchemeId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID)) {
				return schemeMonitoringSolution.getParentSchemeOptimizeInfoId();
			}
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(
			final SchemeMonitoringSolution schemeMonitoringSolution,
			final String key, final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (schemeMonitoringSolution != null) {
				if (key.equals(COLUMN_NAME)) {
					schemeMonitoringSolution.setName((String) value);
				} else if (key.equals(COLUMN_DESCRIPTION)) {
					schemeMonitoringSolution.setDescription((String) value);
				} else if (key.equals(COLUMN_PRICE_USD)) {
					schemeMonitoringSolution.setPrice(((Integer) value).intValue());
				} else if (key.equals(COLUMN_ACTIVE)) {
					schemeMonitoringSolution.setActive(((Boolean) value).booleanValue(), usePool);
				} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
					schemeMonitoringSolution.setParentSchemeId((Identifier) value, usePool);
				} else if (key.equals(COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID)) {
					schemeMonitoringSolution.setParentSchemeOptimizeInfoId((Identifier) value, usePool);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static SchemeMonitoringSolutionWrapper getInstance() {
		if (instance == null)
			instance = new SchemeMonitoringSolutionWrapper();
		return instance;
	}
}
