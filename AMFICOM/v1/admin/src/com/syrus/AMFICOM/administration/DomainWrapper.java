/*
 * $Id: DomainWrapper.java,v 1.18 2005/10/25 19:53:15 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.18 $, $Date: 2005/10/25 19:53:15 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
public final class DomainWrapper extends StorableObjectWrapper<Domain> {

	private static DomainWrapper instance;

	private List<String> keys;

	private DomainWrapper() {
		// empty private constructor
		final String[] keysArray = new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_DOMAIN_ID };

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static DomainWrapper getInstance() {
		if (instance == null) {
			instance = new DomainWrapper();
		}
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
	public Object getValue(final Domain domain, final String key) {
		final Object value = super.getValue(domain, key);
		if (value == null && domain != null) {
			if (key.equals(COLUMN_NAME)) {
				return domain.getName();
			}
			if (key.equals(COLUMN_DESCRIPTION)) {
				return domain.getDescription();
			}
			if (key.equals(COLUMN_DOMAIN_ID)) {
				return domain.getDomainId();
			}
		}
		return value;
	}

	public boolean isEditable(final String key) {
		return false;
	}

	@Override
	public void setValue(Domain domain, final String key, final Object value) {
		if (domain != null) {
			if (key.equals(COLUMN_NAME)) {
				domain.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				domain.setDescription((String) value);
			} else if (key.equals(COLUMN_DOMAIN_ID)) {
				domain.setDomainId((Identifier) value);
			}
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
		if (key.equals(COLUMN_DOMAIN_ID)) {
			return Identifier.class;
		}
		return null;
	}

}
