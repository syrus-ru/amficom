/*
 * $Id: ModelingTypeWrapper.java,v 1.3 2005/02/03 08:36:47 bob Exp $
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

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/03 08:36:47 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class ModelingTypeWrapper implements StorableObjectWrapper {

	public static final String			MODE_IN				= "IN";
	public static final String			MODE_OUT			= "OUT";
	public static final String			PARAMETER_TYPE_ID	= "parameter_type_id";

	private static ModelingTypeWrapper	instance;

	private List						keys;

	private ModelingTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION, MODE_IN, MODE_OUT};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static ModelingTypeWrapper getInstance() {
		if (instance == null)
			instance = new ModelingTypeWrapper();
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
		if (object instanceof ModelingType) {
			ModelingType modelingType = (ModelingType) object;
			if (key.equals(COLUMN_CODENAME))
				return modelingType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return modelingType.getDescription();
			if (key.equals(MODE_IN))
				return modelingType.getInParameterTypes();
			if (key.equals(MODE_OUT))
				return modelingType.getOutParameterTypes();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof ModelingType) {
			ModelingType modelingType = (ModelingType) object;
			if (key.equals(COLUMN_CODENAME))
				modelingType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				modelingType.setDescription((String) value);
			else if (key.equals(MODE_IN))
				modelingType.setInParameterTypes((List) value);
			else if (key.equals(MODE_OUT))
				modelingType.setOutParameterTypes((List) value);
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
		if (key.equals(MODE_IN) || key.equals(MODE_OUT))
			return List.class;
		return String.class;
	}

}
