/*-
 * $Id: ParameterController.java,v 1.2 2006/03/13 15:54:26 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.Parameter;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/03/13 15:54:26 $
 * @module measurement_v1
 */

public class ParameterController extends StorableObjectWrapper {

	public static final String				COLUMN_VALUE	= "value";

	protected static ParameterController	instance;

	protected List							keys;

	private ParameterController() {
		// empty private constructor
		String[] keysArray = new String[] { 
				StorableObjectWrapper.COLUMN_NAME,
				COLUMN_VALUE };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));

	}

	public static ParameterController getInstance() {
		if (instance == null) {
			instance = new ParameterController();
		}
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getKey(int index) {
		return (String) this.keys.get(index);
	}

	public String getName(String key) {
		/* there is no reason rename it */
		return key;
	}

	@Override
	public Class<?> getPropertyClass(String key) {
		return String.class;
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public boolean isEditable(String key) {
		return false;
	}



	@Override
	public void setValue(StorableObject object, String key, Object value) {
		//	 not set here
	}

	public Object getValue(final Object object, final String key) {
		if (object instanceof Parameter) {
			Parameter parameter = (Parameter) object;
			if (key.equals(StorableObjectWrapper.COLUMN_NAME))
				return parameter.getType().getDescription();
			else if (key.equals(COLUMN_VALUE))
				return parameter.getStringValue();
		}
		return null;
	}

	public void setValue(Object object, String key, Object value) {
		//	 not set here
	}
}
