/*-
 * $Id: ActionParameterWrapper.java,v 1.1.2.1 2006/02/11 18:40:45 arseniy Exp $
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
public final class ActionParameterWrapper extends StorableObjectWrapper<ActionParameter> {
	public static final String COLUMN_BINDING_ID = "binding_id";
	public static final String COLUMN_VALUE = "value";

	private static ActionParameterWrapper instance;

	private List<String> keys;

	private ActionParameterWrapper() {
		final String[] keysArray = new String[] { COLUMN_BINDING_ID,
				COLUMN_VALUE };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ActionParameterWrapper getInstance() {
		if (instance == null) {
			instance = new ActionParameterWrapper();
		}
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
	public Object getValue(final ActionParameter object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_BINDING_ID)) {
				return object.getBindingId();
			}
			if (key.equals(COLUMN_VALUE)) {
				return object.getValue();
			}
		}
		return value;
	}

	public boolean isEditable(String key) {
		return false;
	}

	@Override
	public void setValue(final ActionParameter storableObject, final String key, final Object value) throws PropertyChangeException {
		if (storableObject != null) {
			if (key.equals(COLUMN_VALUE)) {
				storableObject.setValue((byte[]) value);
			}
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
		if (key.equals(COLUMN_BINDING_ID)) {
			return Identifier.class;
		}
		if (key.equals(COLUMN_VALUE)) {
			return Object.class;
		}
		return null;
	}
}
