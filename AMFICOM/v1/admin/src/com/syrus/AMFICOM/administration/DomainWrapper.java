/*
 * $Id: DomainWrapper.java,v 1.1 2005/02/01 11:36:30 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/01 11:36:30 $
 * @author $Author: bob $
 * @module admin_v1
 */
public class DomainWrapper implements Wrapper {

	public static final String		COLUMN_NAME				= "name";
	public static final String		COLUMN_DESCRIPTION		= "description";

	public static final String		COLUMN_CHARACTERISTICS	= "characteristics";

	private static DomainWrapper	instance;

	private List					keys;

	private DomainWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_CHARACTERISTICS};

		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static DomainWrapper getInstance() {
		if (instance == null)
			instance = new DomainWrapper();
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
		if (object instanceof Domain) {
			Domain domain = (Domain) object;
			if (key.equals(COLUMN_NAME))
				return domain.getName();
			if (key.equals(COLUMN_DESCRIPTION))
				return domain.getDescription();
			if (key.equals(COLUMN_CHARACTERISTICS))
				return domain.getCharacteristics();
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		if (object instanceof Domain) {
			Domain domain = (Domain) object;
			if (key.equals(COLUMN_NAME))
				domain.setName((String) value);
			else if (key.equals(COLUMN_DESCRIPTION))
				domain.setDescription((String) value);
			else if (key.equals(COLUMN_CHARACTERISTICS))
				domain.setCharacteristics((List) value);
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
		if (key.equals(COLUMN_CHARACTERISTICS)) { return List.class; }
		return String.class;
	}

}
