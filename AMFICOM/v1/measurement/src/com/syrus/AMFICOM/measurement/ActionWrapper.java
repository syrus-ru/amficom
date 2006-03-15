/*-
 * $Id: ActionWrapper.java,v 1.1.2.4 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.Action.ActionStatus;
import com.syrus.util.PropertyChangeException;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionWrapper<A extends Action<R>, R extends ActionResultParameter<A>> extends StorableObjectWrapper<A> {
	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_ACTION_TEMPLATE_ID = "action_template_id";
	public static final String COLUMN_START_TIME = "start_time";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_STATUS = "status";

	private List<String> keys;

	ActionWrapper(final String[] keysArray) {
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
	public Object getValue(final A object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_TYPE_ID)) {
				return object.getTypeId();
			}
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID)) {
				return object.getMonitoredElementId();
			}
			if (key.equals(COLUMN_ACTION_TEMPLATE_ID)) {
				return object.getActionTemplateId();
			}
			if (key.equals(COLUMN_NAME)) {
				return object.getName();
			}
			if (key.equals(COLUMN_START_TIME)) {
				return object.getStartTime();
			}
			if (key.equals(COLUMN_DURATION)) {
				return Long.valueOf(object.getDuration());
			}
			if (key.equals(COLUMN_STATUS)) {
				return object.getStatus();
			}
		}
		return value;
	}

	public final boolean isEditable(final String key) {
		return false;
	}

	@Override
	public final void setValue(final A object, final String key, final Object value) throws PropertyChangeException {
		if (object != null) {
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID)) {
				object.setMonitoredElementId((Identifier) value);
			} else if (key.equals(COLUMN_STATUS)) {
				object.setStatus((ActionStatus) value);
			}
		}
	}

	public final Object getPropertyValue(final String key) {
		/* there is no properties */
		return null;
	}

	public final void setPropertyValue(final String key, final Object objectKey, final Object objectValue) {
		/* there is no properties */
	}

	@Override
	public Class<?> getPropertyClass(final String key) {
		final Class<?> clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_TYPE_ID) || key.equals(COLUMN_MONITORED_ELEMENT_ID) || key.equals(COLUMN_ACTION_TEMPLATE_ID)) {
			return Identifier.class;
		}
		if (key.equals(COLUMN_NAME)) {
			return String.class;
		}
		if (key.equals(COLUMN_START_TIME)) {
			return Date.class;
		}
		if (key.equals(COLUMN_DURATION)) {
			return Long.class;
		}
		if (key.equals(COLUMN_STATUS)) {
			return ActionStatus.class;
		}
		return null;
	}

}
