/*-
 * $Id: SchemePathWrapper.java,v 1.15 2006/03/15 17:32:11 bass Exp $
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
 * @version $Revision: 1.15 $, $Date: 2006/03/15 17:32:11 $
 * @author $Author: bass $
 * @module scheme
 */
public final class SchemePathWrapper extends StorableObjectWrapper<SchemePath> {
	public static final String COLUMN_TRANSMISSION_PATH_ID = "transmission_path_id";

	public static final String COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID = "prnt_schm_mntrng_sltn_id";
	
	private List<String> keys;
	
	private SchemePathWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_TRANSMISSION_PATH_ID,
				COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID}));
	}
	
	private static SchemePathWrapper instance;

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		return key;
	}

	@Override
	public Class<?> getPropertyClass(String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_TRANSMISSION_PATH_ID)
				|| key.equals(COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(String key) {
//		there is no property
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
//		there is no property
	}

	@Override
	public Object getValue(SchemePath schemePath, String key) {
		final Object value = super.getValue(schemePath, key);
		if (value != null) {
			return value;
		}
		if (schemePath != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemePath.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemePath.getDescription();
			} else if (key.equals(COLUMN_TRANSMISSION_PATH_ID)) {
				return schemePath.getTransmissionPathId();
			} else if (key.equals(COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID)) {
				return schemePath.getParentSchemeMonitoringSolutionId();
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	@Override
	public void setValue(final SchemePath schemePath, final String key,
			final Object value)
	throws PropertyChangeException {
		final boolean usePool = false;

		try {
			if (schemePath != null) {
				if (key.equals(COLUMN_NAME)) {
					schemePath.setName((String) value);
				} else if (key.equals(COLUMN_DESCRIPTION)) {
					schemePath.setDescription((String) value);
				} else if (key.equals(COLUMN_TRANSMISSION_PATH_ID)) {
					schemePath.setTransmissionPathId((Identifier) value);
				} else if (key.equals(COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID)) {
					schemePath.setParentSchemeMonitoringSolutionId((Identifier) value, usePool);
				}
			}
		} catch (final ApplicationException ae) {
			throw new PropertyChangeException(ae);
		}
	}

	public static SchemePathWrapper getInstance() {
		if (instance == null)
			instance = new SchemePathWrapper();
		return instance;
	}
}
