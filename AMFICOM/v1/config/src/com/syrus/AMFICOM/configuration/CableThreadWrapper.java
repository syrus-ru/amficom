/*
 * $Id: CableThreadWrapper.java,v 1.1 2005/01/26 13:18:49 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.Wrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/26 13:18:49 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public final class CableThreadWrapper implements Wrapper {

	public static final String			COLUMN_NAME		= "name";
	public static final String			COLUMN_TYPE_ID	= "type_id";

	private static CableThreadWrapper	instance;

	private List						keys;

	private CableThreadWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, StorableObjectType.COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_TYPE_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static CableThreadWrapper getInstance() {
		if (instance == null)
			instance = new CableThreadWrapper();
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
		if (object instanceof CableThread) {
			CableThread thread = (CableThread) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return thread.getId().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return thread.getCreated().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return thread.getCreatorId().getIdentifierString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return thread.getModified().toString();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return thread.getModifierId().getIdentifierString();
			if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				return thread.getDescription();
			if (key.equals(COLUMN_NAME))
				return thread.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return thread.getType().getId().getIdentifierString();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof CableThread) {
			CableThread thread = (CableThread) object;
			if (key.equals(COLUMN_NAME))
				thread.setName((String) value);
			else if (key.equals(StorableObjectType.COLUMN_DESCRIPTION))
				thread.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID)) {
				try {
					thread.setType((CableThreadType) ConfigurationStorableObjectPool.getStorableObject(
						new Identifier((String) value), true));
				} catch (ApplicationException e) {
					Log.errorMessage("CharacteristicWrapper.setValue | key '" + key + "' caught " + e.getMessage());
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
		return String.class;
	}
}
