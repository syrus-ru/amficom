/*-
 * $Id: ActionTemplateWrapper.java,v 1.1.2.1 2006/02/11 18:40:45 arseniy Exp $
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
public final class ActionTemplateWrapper extends StorableObjectWrapper<ActionTemplate> {
	public static final String LINK_COLUMN_ACTION_TEMPLATE_ID = "action_template_id";
	public static final String LINK_COLUMN_ACTION_PARAMETER_ID = "action_parameter_id";

	private static ActionTemplateWrapper instance;

	private List<String> keys;

	private ActionTemplateWrapper() {
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ActionTemplateWrapper getInstance() {
		if (instance == null) {
			instance = new ActionTemplateWrapper();
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
	public Object getValue(final ActionTemplate object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				return object.getDescription();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final ActionTemplate storableObject, final String key, Object value) throws PropertyChangeException {
		if (storableObject != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				storableObject.setDescription((String) value);
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
		if (key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		return null;
	}
}