/*
 * $Id: CableThreadTypeWrapper.java,v 1.3 2005/01/31 14:42:34 bob Exp $
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

import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/01/31 14:42:34 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public final class CableThreadTypeWrapper implements Wrapper {

	// codename VARCHAR2(32) NOT NULL,
	public static final String				COLUMN_CODENAME		= "codename";

	// description VARCHAR2(256),
	public static final String				COLUMN_DESCRIPTION	= "description";

	// name VARCHAR2(64),
	public static final String				COLUMN_NAME			= "name";

	// color NUMBER(38),
	public static final String				COLUMN_COLOR		= "color";

	// cable_link_type_id VARCHAR2(32),
	public static final String				COLUMN_LINK_TYPE_ID	= "link_type_id";

	private static CableThreadTypeWrapper	instance;

	private List							keys;

	private CableThreadTypeWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { StorableObjectDatabase.COLUMN_ID, StorableObjectDatabase.COLUMN_CREATED,
				StorableObjectDatabase.COLUMN_CREATOR_ID, StorableObjectDatabase.COLUMN_MODIFIED,
				StorableObjectDatabase.COLUMN_MODIFIER_ID, COLUMN_CODENAME, COLUMN_DESCRIPTION, COLUMN_NAME,
				COLUMN_COLOR, COLUMN_LINK_TYPE_ID};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static CableThreadTypeWrapper getInstance() {
		if (instance == null)
			instance = new CableThreadTypeWrapper();
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
		if (object instanceof CableThreadType) {
			CableThreadType type = (CableThreadType) object;
			if (key.equals(StorableObjectDatabase.COLUMN_ID))
				return type.getId();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATED))
				return type.getCreated();
			if (key.equals(StorableObjectDatabase.COLUMN_CREATOR_ID))
				return type.getCreatorId();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIED))
				return type.getModified();
			if (key.equals(StorableObjectDatabase.COLUMN_MODIFIER_ID))
				return type.getModifierId();
			if (key.equals(COLUMN_CODENAME))
				return type.getCodename();
			if (key.equals(COLUMN_DESCRIPTION))
				return type.getDescription();
			if (key.equals(COLUMN_NAME))
				return type.getName();
			if (key.equals(COLUMN_COLOR))
				return new Integer(type.getColor());
			if (key.equals(COLUMN_LINK_TYPE_ID))
				return type.getLinkType();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof CableThreadType) {
			CableThreadType type = (CableThreadType) object;
			if (key.equals(COLUMN_NAME))
				type.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				type.setDescription((String) value);
			else if (key.equals(COLUMN_CODENAME))
				type.setCodename((String) value);
			else if (key.equals(COLUMN_COLOR))
				type.setColor(((Integer)value).intValue());
			else if (key.equals(COLUMN_LINK_TYPE_ID)) 
				type.setLinkType((LinkType)value);
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
		Class clazz = String.class;
		return clazz;
	}
}
