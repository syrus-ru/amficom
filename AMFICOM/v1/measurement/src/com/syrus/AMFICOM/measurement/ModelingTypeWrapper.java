/*
 * $Id: ModelingTypeWrapper.java,v 1.1 2005/01/28 09:40:05 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/28 09:40:05 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class ModelingTypeWrapper implements Wrapper {

	public static final String			MODE_IN				= "IN";
	public static final String			MODE_OUT			= "OUT";
	public static final String			COLUMN_CODENAME		= "codename";
	public static final String			COLUMN_DESCRIPTION	= "description";
	public static final String			PARAMETER_TYPE_ID	= "parameter_type_id";

	private static ModelingTypeWrapper	instance;

	private List						keys;

	private ModelingTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_CODENAME, COLUMN_DESCRIPTION, MODE_IN, MODE_OUT};

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
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return modelingType.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return modelingType.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return modelingType.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return modelingType.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return modelingType.getModifierId().getIdentifierString();
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
			else if (key.equals(MODE_IN)) {
				List paramTypeIdStr = (List) value;
				List paramTypeIds = new ArrayList(paramTypeIdStr.size());
				for (Iterator it = paramTypeIdStr.iterator(); it.hasNext();)
					paramTypeIds.add(new Identifier((String) it.next()));
				try {
					modelingType.setInParameterTypes(GeneralStorableObjectPool.getStorableObjects(paramTypeIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("ModelingTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			} else if (key.equals(MODE_OUT)) {
				List paramTypeIdStr = (List) value;
				List paramTypeIds = new ArrayList(paramTypeIdStr.size());
				for (Iterator it = paramTypeIdStr.iterator(); it.hasNext();)
					paramTypeIds.add(new Identifier((String) it.next()));
				try {
					modelingType.setOutParameterTypes(GeneralStorableObjectPool.getStorableObjects(paramTypeIds, true));
				} catch (ApplicationException e) {
					Log.errorMessage("ModelingTypeWrapper.setValue | key '" + key + "' caught " + e.getMessage());
				}
			}
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
