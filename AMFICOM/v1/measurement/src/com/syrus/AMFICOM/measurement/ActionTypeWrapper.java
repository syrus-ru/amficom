/*-
 * $Id: ActionTypeWrapper.java,v 1.1.2.1 2006/02/11 18:40:45 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/11 18:40:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionTypeWrapper<T extends ActionType<T>> extends StorableObjectWrapper<T> {

	private List<String> keys;

	ActionTypeWrapper(final String[] keysArray) {
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public final List<String> getKeys() {
		return this.keys;
	}

	public final String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	@Override
	public final Object getValue(final T object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_CODENAME)) {
				return object.getCodename();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return object.getDescription();
			}
		}
		return value;
	}

	public final boolean isEditable(final String key) {
		return false;
	}

	@Override
	public final void setValue(final T object, final String key, final Object value) throws PropertyChangeException {
		if (object != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				object.setDescription((String) value);
			} else if (key.equals(COLUMN_CODENAME)) {
				object.setCodename((String) value);
			}
		}
	}

	public final String getKey(final int index) {
		return this.keys.get(index);
	}

	public final Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public final void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public final Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_DESCRIPTION) || key.equals(COLUMN_CODENAME)) {
			return String.class;
		}
		return null;
	}

}