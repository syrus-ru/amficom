/*
 * $Id: ModelingTypeWrapper.java,v 1.12 2005/07/25 20:50:00 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.12 $, $Date: 2005/07/25 20:50:00 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class ModelingTypeWrapper extends StorableObjectWrapper {

	public static final String LINK_COLUMN_MODELING_TYPE_ID = "modeling_type_id";

	private static ModelingTypeWrapper instance;

	private List<String> keys;

	private ModelingTypeWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static ModelingTypeWrapper getInstance() {
		if (instance == null)
			instance = new ModelingTypeWrapper();
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
	public Object getValue(final Object object, final String key) {
		final Object value = super.getValue(object, key);
		if (value == null && object instanceof ModelingType) {
			final ModelingType modelingType = (ModelingType) object;
			if (key.equals(COLUMN_CODENAME))
				return modelingType.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return modelingType.getDescription();
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(final Object object, final String key, final Object value) {
		if (object instanceof ModelingType) {
			final ModelingType modelingType = (ModelingType) object;
			if (key.equals(COLUMN_CODENAME))
				modelingType.setCodename((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				modelingType.setDescription((String) value);
		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
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
		if (key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		}
		return null;
	}

}
