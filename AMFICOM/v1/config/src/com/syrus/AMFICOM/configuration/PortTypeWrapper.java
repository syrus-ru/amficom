/*
 * $Id: PortTypeWrapper.java,v 1.18 2005/07/27 15:59:22 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.18 $, $Date: 2005/07/27 15:59:22 $
 * @author $Author: bass $
 * @module config
 */
public final class PortTypeWrapper extends StorableObjectWrapper {

	public static final String COLUMN_SORT = "sort";
	public static final String COLUMN_KIND = "kind";

	private static PortTypeWrapper instance;

	private List<String> keys;

	private PortTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_SORT, COLUMN_KIND };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static PortTypeWrapper getInstance() {
		if (instance == null)
			instance = new PortTypeWrapper();
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
	public Object getValue(final Object object, final String key) {
		Object value = super.getValue(object, key);
		if (value == null && object instanceof PortType) {
			PortType type = (PortType) object;
			if (key.equals(COLUMN_CODENAME))
				return type.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return type.getDescription();
			if (key.equals(COLUMN_NAME))
				return type.getName();
			if (key.equals(COLUMN_SORT))
				return new Integer(type.getSort().value());
			if (key.equals(COLUMN_KIND))
				return new Integer(type.getKind().value());
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof PortType) {
			PortType type = (PortType) object;
			if (key.equals(COLUMN_NAME))
				type.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				type.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				type.setCodename((String) value);
			else if (key.equals(COLUMN_SORT))
				type.setSort(PortTypeSort.from_int(((Integer) value).intValue()));
		}
	}

	public Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_CODENAME)
				|| key.equals(COLUMN_DESCRIPTION)
				|| key.equals(COLUMN_NAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_SORT)
				|| key.equals(COLUMN_KIND)) {
			return Integer.class;
		}
		return null;
	}
}
