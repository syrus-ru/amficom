/*
 * $Id: CableThreadWrapper.java,v 1.4 2005/02/01 06:15:29 bob Exp $
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

import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/01 06:15:29 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public final class CableThreadWrapper implements Wrapper {

	private static CableThreadWrapper	instance;

	private List						keys;

	// table :: CableThread
	public static final String			COLUMN_TYPE_ID		= "type_id";

	// name VARCHAR2(64) NOT NULL,
	public static final String			COLUMN_NAME			= "name";

	// description VARCHAR2(256),
	public static final String			COLUMN_DESCRIPTION	= "description";

	private CableThreadWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_TYPE_ID};

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
			if (key.equals(COLUMN_DESCRIPTION))
				return thread.getDescription();
			if (key.equals(COLUMN_NAME))
				return thread.getName();
			if (key.equals(COLUMN_TYPE_ID))
				return thread.getType();
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
			else if (key.equals(COLUMN_DESCRIPTION))
				thread.setDescription((String) value);
			else if (key.equals(COLUMN_TYPE_ID)) 
				thread.setType((CableThreadType)value);
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
