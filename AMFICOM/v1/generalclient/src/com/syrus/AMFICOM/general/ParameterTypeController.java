/*
 * $Id: ParameterTypeController.java,v 1.1 2005/02/16 12:56:15 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public final class ParameterTypeController implements ObjectResourceController {

	private static ParameterTypeController	instance;

	private List								keys;

	private ParameterTypeController() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION };

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static ParameterTypeController getInstance() {
		if (instance == null)
			instance = new ParameterTypeController();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		String name = null;
		if (key.equals(COLUMN_DESCRIPTION))
			name = "Название";
		return name;
	}

	public Object getValue(final Object object, final String key) {
		Object result = null;
		if (object instanceof ParameterType) {
			ParameterType type = (ParameterType) object;
			if (key.equals(COLUMN_DESCRIPTION))
				result = type.getDescription();
		}
		return result;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		// TODO empty method !!!
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		Object result = "";
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		// TODO empty method !!!
	}

	public Class getPropertyClass(String key) {
		Class clazz = String.class;
		return clazz;
	}
}
