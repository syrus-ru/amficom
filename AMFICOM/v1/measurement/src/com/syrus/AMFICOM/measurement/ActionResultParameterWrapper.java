/*-
 * $Id: ActionResultParameterWrapper.java,v 1.1.2.1 2006/02/11 18:40:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/11 18:40:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionResultParameterWrapper<T extends ActionResultParameter<T>> extends StorableObjectWrapper<T> {
	public static final String COLUMN_VALUE = "value";

	private List<String> keys;

	ActionResultParameterWrapper(final String[] keysArray) {
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
	public Object getValue(final T object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_TYPE_ID)) {
				return object.getTypeId();
			}
			if (key.equals(COLUMN_VALUE)) {
				return object.getValue();
			}
		}
		return value;
	}

	public final boolean isEditable(final String key) {
		return false;
	}

	@Override
	public final void setValue(final T storableObject, final String key, final Object value) throws PropertyChangeException {
		//Nothing to set
	}

	public final Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public final void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class getPropertyClass(final String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_TYPE_ID)) {
			return Identifier.class;
		}
		if (key.equals(COLUMN_VALUE)) {
			return Object.class;
		}
		return null;
	}
}
