/*-
 * $Id: ActionParameterTypeBindingWrapper.java,v 1.1.2.2 2006/02/22 15:49:27 arseniy Exp $
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
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/22 15:49:27 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public class ActionParameterTypeBindingWrapper extends StorableObjectWrapper<ActionParameterTypeBinding> {
	public static final String COLUMN_PARAMETER_VALUE_KIND = "parameter_value_kind";
	public static final String COLUMN_PARAMETER_TYPE_ID = "parameter_type_id";
	public static final String COLUMN_ACTION_TYPE_KIND_CODE = "action_type_kind_code";
	public static final String COLUMN_MEASUREMENT_TYPE_ID = "measurement_type_id";
	public static final String COLUMN_ANALYSIS_TYPE_ID = "analysis_type_id";
	public static final String COLUMN_MODELING_TYPE_ID = "modeling_type_id";
	public static final String COLUMN_MEASUREMENT_PORT_TYPE_ID = "measurement_port_type_id";

	private static ActionParameterTypeBindingWrapper instance;

	private List<String> keys;

	private ActionParameterTypeBindingWrapper() {
		final String[] keysArray = new String[] { COLUMN_PARAMETER_VALUE_KIND,
				COLUMN_PARAMETER_TYPE_ID,
				COLUMN_MEASUREMENT_TYPE_ID,
				COLUMN_MEASUREMENT_PORT_TYPE_ID };
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ActionParameterTypeBindingWrapper getInstance() {
		if (instance == null) {
			instance = new ActionParameterTypeBindingWrapper();
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
	public Object getValue(final ActionParameterTypeBinding object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object != null) {
			if (key.equals(COLUMN_PARAMETER_VALUE_KIND)) {
				return object.getParameterValueKind();
			}
			if (key.equals(COLUMN_PARAMETER_TYPE_ID)) {
				return object.getParameterTypeId();
			}
			if (key.equals(COLUMN_MEASUREMENT_TYPE_ID)) {
				return object.getActionTypeId();
			}
			if (key.equals(COLUMN_MEASUREMENT_PORT_TYPE_ID)) {
				return object.getMeasurementPortTypeId();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(final ActionParameterTypeBinding object, final String key, final Object value) throws PropertyChangeException {
		/* Nothing to set */
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
		if (key.equals(COLUMN_PARAMETER_VALUE_KIND)) {
			return Enum.class;
		}
		if (key.equals(COLUMN_PARAMETER_TYPE_ID)
				|| key.equals(COLUMN_MEASUREMENT_TYPE_ID)
				|| key.equals(COLUMN_MEASUREMENT_PORT_TYPE_ID)) {
			return Identifier.class;
		}
		return null;
	}

}
