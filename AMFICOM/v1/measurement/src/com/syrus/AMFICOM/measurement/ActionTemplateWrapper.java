/*-
 * $Id: ActionTemplateWrapper.java,v 1.1.2.3 2006/02/15 19:36:15 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.1.2.3 $, $Date: 2006/02/15 19:36:15 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionTemplateWrapper extends StorableObjectWrapper<ActionTemplate> {
	public static final String COLUMN_APPROXIMATE_ACTION_DURATION = "approximate_action_duration";
	public static final String LINK_COLUMN_ACTION_TEMPLATE_ID = "action_template_id";
	public static final String LINK_COLUMN_ACTION_PARAMETER_ID = "action_parameter_id";
	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";

	private static ActionTemplateWrapper instance;

	private List<String> keys;

	private ActionTemplateWrapper() {
		final String[] keysArray = new String[] { COLUMN_DESCRIPTION,
				COLUMN_APPROXIMATE_ACTION_DURATION,
				LINK_COLUMN_MONITORED_ELEMENT_ID };
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
			if (key.equals(COLUMN_APPROXIMATE_ACTION_DURATION)) {
				return Long.valueOf(object.getApproximateActionDuration());
			}
			if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID)) {
				return object.getMonitoredElementIds();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final ActionTemplate storableObject, final String key, final Object value) throws PropertyChangeException {
		if (storableObject != null) {
			if (key.equals(COLUMN_DESCRIPTION)) {
				storableObject.setDescription((String) value);
			}
			if (key.equals(COLUMN_APPROXIMATE_ACTION_DURATION)) {
				storableObject.setApproximateActionDuration(((Long) value).longValue());
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
		if (key.equals(COLUMN_APPROXIMATE_ACTION_DURATION)) {
			return Long.class;
		}
		if (key.equals(LINK_COLUMN_MONITORED_ELEMENT_ID)) {
			return Set.class;
		}
		return null;
	}
}
