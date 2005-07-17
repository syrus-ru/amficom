/*
 * $Id: DomainWrapper.java,v 1.11 2005/07/17 05:18:01 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.11 $, $Date: 2005/07/17 05:18:01 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
public class DomainWrapper extends StorableObjectWrapper {

	private static DomainWrapper	instance;

	private List					keys;

	private DomainWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static DomainWrapper getInstance() {
		if (instance == null)
			instance = new DomainWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public Object getValue(final Object object, final String key) {
		Object value = super.getValue(object, key);
		if (value == null && object instanceof Domain) {
			Domain domain = (Domain) object;
			if (key.equals(COLUMN_NAME))
				return domain.getName();
			if (key.equals(COLUMN_DESCRIPTION))
				return domain.getDescription();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Domain) {
			Domain domain = (Domain) object;
			if (key.equals(COLUMN_NAME))
				domain.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				domain.setDescription((String) value);
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

	@Override
	public Class getPropertyClass(String key) {
		Class clazz = super.getPropertyClass(key); 
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME)
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		return null;
	}

}
