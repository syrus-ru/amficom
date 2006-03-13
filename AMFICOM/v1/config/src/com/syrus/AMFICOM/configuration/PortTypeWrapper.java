/*
 * $Id: PortTypeWrapper.java,v 1.22 2006/03/13 15:54:24 bass Exp $
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
 * @version $Revision: 1.22 $, $Date: 2006/03/13 15:54:24 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class PortTypeWrapper extends StorableObjectWrapper<PortType> {

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
	public Object getValue(final PortType portType, final String key) {
		Object value = super.getValue(portType, key);
		if (value == null && portType != null) {
			if (key.equals(COLUMN_CODENAME))
				return portType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return portType.getDescription();
			if (key.equals(COLUMN_NAME))
				return portType.getName();
			if (key.equals(COLUMN_SORT))
				return new Integer(portType.getSort().value());
			if (key.equals(COLUMN_KIND))
				return new Integer(portType.getKind().value());
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final PortType object, final String key, final Object value) {
		if (object != null) {
			if (key.equals(COLUMN_NAME))
				object.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				object.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				object.setCodename((String) value);
			else if (key.equals(COLUMN_SORT))
				object.setSort(PortTypeSort.from_int(((Integer) value).intValue()));
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
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key); 
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
