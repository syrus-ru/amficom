/*
 * $Id: ModelingWrapper.java,v 1.2 2005/02/01 06:38:49 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/01 06:38:49 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class ModelingWrapper implements Wrapper {

	public static final String		COLUMN_TYPE_ID				= "type_id";
	public static final String		COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String		COLUMN_ARGUMENT_SET_ID		= "argument_set_id";
	public static final String		COLUMN_NAME					= "name";

	private static ModelingWrapper	instance;

	private List					keys;

	private ModelingWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_TYPE_ID, COLUMN_MONITORED_ELEMENT_ID, COLUMN_ARGUMENT_SET_ID,
				COLUMN_NAME};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static ModelingWrapper getInstance() {
		if (instance == null)
			instance = new ModelingWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	public Object getValue(final Object object, final String key) {
		if (object instanceof Modeling) {
			Modeling modeling = (Modeling) object;
			if (key.equals(COLUMN_TYPE_ID))
				return modeling.getType();
			if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				return modeling.getMonitoredElementId();
			if (key.equals(COLUMN_ARGUMENT_SET_ID))
				return modeling.getArgumentSet();
			if (key.equals(COLUMN_NAME))
				return modeling.getName();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Modeling) {
			Modeling modeling = (Modeling) object;
			if (key.equals(COLUMN_TYPE_ID))
				modeling.setType((ActionType) value);
			else if (key.equals(COLUMN_MONITORED_ELEMENT_ID))
				modeling.setMonitoredElementId((Identifier) value);
			else if (key.equals(COLUMN_ARGUMENT_SET_ID))
				modeling.setArgumentSet((Set) value);
			else if (key.equals(COLUMN_NAME))
				modeling.setName((String) value);
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

	public Class getPropertyClass(String key) {
		return String.class;
	}

}
